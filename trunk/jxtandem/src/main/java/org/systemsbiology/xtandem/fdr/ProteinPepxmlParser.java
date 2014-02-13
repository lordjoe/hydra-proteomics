package org.systemsbiology.xtandem.fdr;

import com.lordjoe.lib.xml.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;


/**
 * org.systemsbiology.xtandem.fdr.ProteinPepxmlParser
 *
 * @author attilacsordas
 * @date 09/05/13
 */
public class ProteinPepxmlParser {

    public static final boolean USE_EXPECTED = false; // otherwise use score

    private final File m_File;
    private final Map<String, Set<Polypeptide>> proteinToHits = new HashMap<String, Set<Polypeptide>>();
    private final Map<String, Set<Polypeptide>> uniqueProteinToHits = new HashMap<String, Set<Polypeptide>>();


    public ProteinPepxmlParser(String filename) {
        this(new File(filename));


    }

    public ProteinPepxmlParser(File file) {
        m_File = file;


    }

    public File getFilename() {
        return m_File;
    }


    /**
     *
     */
    public void readFileAndGenerate(boolean onlyUniquePeptides, ISpectrumDataFilter... filters) {
        @SuppressWarnings("UnusedDeclaration")
        int numberProcessed = 0;
        @SuppressWarnings("UnusedDeclaration")
        double lastRetentionTime = 0;

        @SuppressWarnings("UnusedDeclaration")
        int numberUnProcessed = 0;
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(m_File));
            String line = rdr.readLine();
            while (line != null) {
                if (line.contains("<spectrum_query")) {
                    String retention_time_sec = XMLUtil.extractAttribute(line, "retention_time_sec");
                    if (retention_time_sec == null) {
                        lastRetentionTime = 0;
                    }
                    else {
                        try {
                            lastRetentionTime = Double.parseDouble(retention_time_sec.trim());
                        }
                        catch (NumberFormatException e) {
                            lastRetentionTime = 0;
                        }
                    }
                }

                //noinspection StatementWithEmptyBody,StatementWithEmptyBody
                if (line.contains("<search_result")) {
                    String[] searchHitLines = readSearchHitLines(line, rdr);
                    //              System.out.println(line);
                    boolean processed = handleSearchHit(searchHitLines, lastRetentionTime, onlyUniquePeptides,  filters);
                    if (processed) {
                        for (int i = 0; i < searchHitLines.length; i++) {
                            @SuppressWarnings("UnusedDeclaration")
                            String searchHitLine = searchHitLines[i];
                        }
                        numberProcessed++;
                    }
                    else
                        numberUnProcessed++;
                }
                line = rdr.readLine();

            }

            //noinspection UnnecessaryReturnStatement
            return;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected String[] readSearchHitLines(String line, LineNumberReader rdr, @SuppressWarnings("UnusedParameters") ISpectrumDataFilter... filters) {
        List<String> holder = new ArrayList<String>();

        try {
            while (line != null) {
                holder.add(line);
                if (line.contains("</search_result")) {
                    break; // done
                }
                line = rdr.readLine();  // read next line

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    @SuppressWarnings({"UnusedParameters", "UnusedAssignment"})
    protected boolean handleSearchHit(String[] lines, double retentionTime,boolean onlyUniquePeptides, ISpectrumDataFilter... filters) {
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        Double expectedValue = null;
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        Double hyperScoreValue = null;
        int index = 0;
        String line = lines[index++];   // handle first line
        while (!line.contains("<search_hit")) {
            line = lines[index++];
            if (index >= lines.length)
                return false;
        }
        boolean trueHit = !line.contains("protein=\"DECOY_");
        boolean processSpectrum = parseHitValue(line) <= 2;
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        boolean isUnique = true;
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        boolean isModified = false;
        if (!line.contains("hit_rank=\"1\""))
            return false;
        Polypeptide peptide = processPeptide(line, retentionTime);

        IProtein protein = null;


        for (; index < lines.length; index++) {
            line = lines[index];

            if (line.contains("</search_hit"))
                break;         // we are done

            if (line.contains(" modified_peptide="))
                peptide = processModifiedPeptide(line, retentionTime);

            if (line.contains("<alternative_protein")) {
                isUnique = false;
                if(onlyUniquePeptides)
                     processSpectrum = false; // only process unique hits
            }

            if (line.contains("<search_score name=\"hyperscore\" value=\"")) {
                hyperScoreValue = parseValue(line);
            }
            if (line.contains("<search_score name=\"expect\" value=\"")) {
                expectedValue = parseValue(line);
            }
            if (line.contains("protein=\"DECOY_")) {  // another protein
                if (trueHit)
                    processSpectrum = false; // one decoy one not
            }
            if (line.contains("<alternative_protein")) {  // another protein
                if (!trueHit && !line.contains("protein=\"DECOY_")) // we start as decoy and fit to a real
                    processSpectrum = false; // one decoy one not
            }

            if (line.contains("protein_descr=\"")) {
                protein = processProtein(line);

            }
        }

        if (processSpectrum) {
            @SuppressWarnings("ConstantConditions")
            String id = protein.getId();
            if (id.contains("DECOY"))
                return false;
            Set<Polypeptide> pps = proteinToHits.get(id);
            if (pps == null) {
                pps = new HashSet<Polypeptide>();
                proteinToHits.put(id, pps);
            }
            pps.add(peptide);
            return true; // processed
        }
        return false; // unprocessed


    }

    public static Polypeptide processPeptide(final String line, double retentionTime) {
        String peptide = XMLUtil.extractAttribute(line, "peptide");
        if (peptide == null)
            throw new IllegalArgumentException("bad line " + line);
        Polypeptide polypeptide = Polypeptide.fromString(peptide);
        polypeptide.setRetentionTime(retentionTime);
        return polypeptide;
    }

    public static Polypeptide processModifiedPeptide(final String line, double retentionTime) {
        String peptide = XMLUtil.extractAttribute(line, "modified_peptide");
        if (peptide == null)
            throw new IllegalArgumentException("bad line " + line);
        Polypeptide polypeptide = Polypeptide.fromString(peptide);
        polypeptide.setRetentionTime(retentionTime);
        return polypeptide;
    }

    public static IProtein processProtein(final String line) {
        String peptide = XMLUtil.extractAttribute(line, "protein_descr");
        if (peptide == null)
            throw new IllegalArgumentException("bad line " + line);
        return Protein.getProtein(peptide, "", "", null);
    }

    protected void processSpectrum(SpectrumData spectrum, IDiscoveryDataHolder hd) {
        double score;
        if (USE_EXPECTED)
            score = spectrum.getExpectedValue();
        else
            //noinspection ConstantConditions
            score = spectrum.getHyperScoreValue();

        if (spectrum.isTrueHit()) {
            hd.addTrueDiscovery(score);
//            if (spectrum.isModified())
//                m_ModifiedHandler.addTrueDiscovery(score);
//            else
//                m_UnModifiedHandler.addTrueDiscovery(score);
        }
        else {
            hd.addFalseDiscovery(score);
            if (spectrum.isModified())
                throw new UnsupportedOperationException("Fix This"); // ToDo
//                m_ModifiedHandler.addFalseDiscovery(score);
//            else
//                m_UnModifiedHandler.addFalseDiscovery(score);
        }
    }

    public static double parseValue(String line) {
        String s = parseQuotedValue(line, "value");
        if (s.length() == 0)
            return 0;
        return Double.parseDouble(s);
    }

    public static boolean parseIsModifiedValue(String line) {
        String s = parseQuotedValue(line, "peptide");
        //noinspection SimplifiableIfStatement
        if (s.length() == 0)
            return false;
        return s.contains("[");    // modification string
    }

    public static int parseHitValue(String line) {
        String s = parseQuotedValue(line, "hit_rank");
        if (s.length() == 0)
            return 0;
        return Integer.parseInt(s);
    }

    /**
     * return a section of
     *
     * @param line
     * @param start
     * @return
     */
    public static String parseQuotedValue(String line, String start) {
        final String str = start + "=\"";
        int index = line.indexOf(str);
        if (index == -1)
            return "";
        index += str.length();
        int endIndex = line.indexOf("\"", index);
        if (endIndex == -1)
            return "";
        return line.substring(index, endIndex);
    }

    public void appendPeptides(Appendable out) {
        try {
            List<String> proteins = new ArrayList<String>(proteinToHits.keySet());
            Collections.sort(proteins);
            for (String protein : proteins) {
                Set<Polypeptide> pps = proteinToHits.get(protein);
                List<Polypeptide> peptides = new ArrayList<Polypeptide>(pps);
                Collections.sort(peptides);

                for (Polypeptide peptide : peptides) {
                    IPolypeptide unModified = peptide; // .getUnModified();
                    out.append(unModified.toString());
                    out.append("\t");
                    out.append(protein);
                    out.append("\t");
                    String rt = String.format("%11.3f", peptide.getRetentionTime()).trim();
                    out.append(rt);
                    out.append("\n");
                }


            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void appendProteins(Appendable out) {
        try {
            List<String> proteins = new ArrayList<String>(proteinToHits.keySet());
            Collections.sort(proteins);
            for (String protein : proteins) {
                Set<Polypeptide> pps = proteinToHits.get(protein);
                List<Polypeptide> peptides = new ArrayList<Polypeptide>(pps);
                Collections.sort(peptides);

                out.append(protein);
                for (Polypeptide peptide : peptides) {
                    out.append("\t");
                    IPolypeptide unModified = peptide; // .getUnModified();
                    out.append(unModified.toString());

                }
                out.append("\n");

            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private static void originalMain(final String[] args) throws IOException {
        if (args.length == 0)
            throw new IllegalArgumentException("pass in pep.xml files to process");
        String filename = args[0];
        PrintWriter px = new PrintWriter(new FileWriter(filename + "_AllTargetProteins.tsv"));
        for (int i = 0; i < args.length; i++) {
            boolean onlyUniquePeptides = false;
            String arg = args[i];
            ProteinPepxmlParser fdrParser = new ProteinPepxmlParser(arg);
            fdrParser.readFileAndGenerate(onlyUniquePeptides);
            fdrParser.appendProteins(px);
            px.close();
            PrintWriter ppx = new PrintWriter(new FileWriter(filename + "_AllTargetPeptides.tsv"));
            fdrParser.appendPeptides(ppx);
            ppx.close();
        }
    }


    private static void newMain(final String[] args) throws IOException {
        if (args.length == 0)
            throw new IllegalArgumentException("pass in pep.xml files to process");
        String filename = args[0];
        PrintWriter px = new PrintWriter(new FileWriter(filename + "_UniqueProteins.tsv"));
        for (int i = 0; i < args.length; i++) {
            boolean onlyUniquePeptides = true;
            String arg = args[i];
            ProteinPepxmlParser fdrParser = new ProteinPepxmlParser(arg);
            fdrParser.readFileAndGenerate(onlyUniquePeptides);
            fdrParser.appendProteins(px);
            px.close();
            PrintWriter ppx = new PrintWriter(new FileWriter(filename + "_UniquePeptides.tsv"));
            fdrParser.appendPeptides(ppx);
            ppx.close();
        }
    }



    public static void main(String[] args) throws Exception {


        newMain(args);
        originalMain(args);

    }


}
