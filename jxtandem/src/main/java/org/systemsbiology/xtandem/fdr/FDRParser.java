package org.systemsbiology.xtandem.fdr;

import com.lordjoe.lib.xml.*;

import java.io.*;
import java.util.*;


/**
 * org.systemsbiology.xtandem.fdr.FDRParser
 *
 * @author attilacsordas
 * @date 09/05/13
 */
public class FDRParser {

    public static final boolean USE_EXPECTED = false; // otherwise use score

    private final File m_File;
    private final IDiscoveryDataHolder m_Handler;
    private final IDiscoveryDataHolder m_ModifiedHandler;
    private final IDiscoveryDataHolder m_UnModifiedHandler;
    private final int m_MaxHits;


    public FDRParser(String filename) {
        this(filename, 1);
    }

    public FDRParser(String filename, int maxhits) {
        m_File = new File(filename);
        m_MaxHits = maxhits;
        if (USE_EXPECTED)
            m_Handler = FDRUtilities.getDiscoveryDataHolder("Default algorithm", false);      // better us low
        else
            m_Handler = FDRUtilities.getDiscoveryDataHolder("Default algorithm", true);   // better us high


        m_ModifiedHandler = FDRUtilities.getDiscoveryDataHolder("Default algorithm", true);   // better us high

        m_UnModifiedHandler = FDRUtilities.getDiscoveryDataHolder("Default algorithm", true);   // better us high


    }

    public File getFilename() {
        return m_File;
    }

    public IDiscoveryDataHolder getHandler() {
        return m_Handler;
    }

    public IDiscoveryDataHolder getModifiedHandler() {
        return m_ModifiedHandler;
    }

    public IDiscoveryDataHolder getUnModifiedHandler() {
        return m_UnModifiedHandler;
    }

    /**
     *
     */
    public void readFileAndGenerateFDR(PrintWriter out,ISpectrumDataFilter... filters) {
        int numberProcessed = 0;
        int numberUnProcessed = 0;
        double lastRetentionTime = 0;
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(m_File));
            String line = rdr.readLine();
            while (line != null) {
                if (line.contains("<spectrum_query")) {
                     String retention_time_sec = XMLUtil.extractAttribute(line, "retention_time_sec");
                     if(retention_time_sec == null)    {
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
                if (line.contains("<search_hit")) {
                    String[] searchHitLines = readSearchHitLines(line, rdr);
                    //              System.out.println(line);
                    boolean processed = handleSearchHit(searchHitLines, filters);
                    if (processed) {
                        for (int i = 0; i < searchHitLines.length; i++) {
                            String searchHitLine = searchHitLines[i];
                            out.println(searchHitLine);
                        }
                        numberProcessed++;
                    } else
                        numberUnProcessed++;
                }
                else {
                    out.println(line);
                }
                line = rdr.readLine();

            }

            //noinspection UnnecessaryReturnStatement
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected String[] readSearchHitLines(String line, LineNumberReader rdr, ISpectrumDataFilter... filters) {
        List<String> holder = new ArrayList<String>();

        try {
            while (line != null) {
                holder.add(line);
                if (line.contains("</search_hit")) {
                    break; // done
                }
                line = rdr.readLine();  // read next line

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    protected boolean handleSearchHit(String[] lines, ISpectrumDataFilter... filters) {

        Double expectedValue = null;
        Double hyperScoreValue = null;
        int index = 0;
        String line = lines[index++];   // handle first line
        boolean trueHit = !line.contains("protein=\"DECOY_");
        boolean processSpectrum = parseHitValue(line) <= m_MaxHits;
        boolean isModified = false;

        for (; index < lines.length; index++) {
            line = lines[index];

            if (line.contains("</search_hit"))
                break;         // we are done

            if (line.contains(" modified_peptide="))
                isModified = true;


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
        }

        if (processSpectrum) {
            final IDiscoveryDataHolder hd = getHandler();
            boolean processData = true;
            // apply any filters
            //noinspection ConstantConditions
            SpectrumData spectrum = new SpectrumData(expectedValue, hyperScoreValue, trueHit, isModified);
            for (int i = 0; i < filters.length; i++) {
                ISpectrumDataFilter s = filters[i];
                processData &= s.isSpectrumKept(spectrum);
            }
            if (processData) {
                processSpectrum(spectrum, hd);
                return true; // processed
            }

        }
        return false; // unprocessed


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
            if (spectrum.isModified())
                m_ModifiedHandler.addTrueDiscovery(score);
            else
                m_UnModifiedHandler.addTrueDiscovery(score);
        } else {
            hd.addFalseDiscovery(score);
            if (spectrum.isModified())
                m_ModifiedHandler.addFalseDiscovery(score);
            else
                m_UnModifiedHandler.addFalseDiscovery(score);
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

    public void appendFDRRates(Appendable out) {
        try {
            final IDiscoveryDataHolder handler = getHandler();
            //final String s = FDRUtilities.listFDRAndCount(handler);
            final String s = FDRUtilities.listFDRAndRates(handler);
            out.append(s);
            System.out.println("====================================\n");
            final String smod = FDRUtilities.listFDRAndRates(getModifiedHandler());
            out.append(smod);
            out.append("====================================\n");
            final String sunmod = FDRUtilities.listFDRAndRates(getUnModifiedHandler());
            out.append(sunmod);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) throws Exception {

        PrintWriter px  = new PrintWriter(new FileWriter("output.pep.xml"));
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            FDRParser fdrParser = new FDRParser(arg);
            fdrParser.readFileAndGenerateFDR(px);
            fdrParser.appendFDRRates(System.out);
        }

    }

}
