package org.systemsbiology.xtandem.pepxml;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.ionization.*;
import org.systemsbiology.xtandem.peptide.*;
import org.systemsbiology.xtandem.scoring.*;

import java.io.*;

/**
 * org.systemsbiology.xtandem.pepxml.PepXMLWriter
 * User: Steve
 * Date: 1/26/12
 */
public class PepXMLWriter {
    public static final PepXMLWriter[] EMPTY_ARRAY = {};

    private static   int gMatchesToPrint = OriginatingScoredScan.MAX_SERIALIZED_MATCHED;

    public static int getMatchesToPrint() {
        return gMatchesToPrint;
    }

    public static void setMatchesToPrint(final int matchesToPrint) {
        gMatchesToPrint = matchesToPrint;
    }

    private final IMainData m_Application;
    private String m_Path = "";

    public PepXMLWriter(final IMainData pApplication) {
        m_Application = pApplication;
    }

    public IMainData getApplication() {
        return m_Application;
    }

    public String getPath() {
        return m_Path;
    }

    public void setPath(final String pPath) {
        m_Path = pPath;
    }

    public void writePepXML(IScoredScan scan, File out) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(out);
            writePepXML(scan, out.getAbsolutePath().replace("\\", "/"), os);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    public void writePepXML(IScoredScan scan, String path, OutputStream out) {

        try {
            PrintWriter pw = new PrintWriter(out);
            writePepXML(scan, pw);
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {   // ignore
            }
        }

    }

    public void writePepXML(IScoredScan scan, PrintWriter out) {
        writeSummaries(scan, out);

    }

    public static final String PEPXML_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<?xml-stylesheet type=\"text/xsl\" href=\"pepXML_std.xsl\"?>\n";
//                    "<msms_pipeline_analysis date=\"%DATE%\"" +
//                    " summary_xml=\"%PATH%\"" +
//                    " xmlns=\"http://regis-web.systemsbiology.net/pepXML\"" +
//                    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
//                    " xsi:schemaLocation=\"http://sashimi.sourceforge.net/schema_revision/pepXML/pepXML_v117.xsd\"" +
//                    ">";

    public static final String TTRYPSIN_XML =
            "      <sample_enzyme name=\"trypsin\">\n" +
                    "         <specificity cut=\"KR\" no_cut=\"P\" sense=\"C\"/>\n" +
                    "      </sample_enzyme>";

    public static final String ANALYSIS_HEADER =
            "<msms_pipeline_analysis date=\"%DATE%\"  " +
                    //             "summary_xml=\"c:\\Inetpub\\wwwroot\\ISB\\data\\HighResMS2\\c:/Inetpub/wwwroot/ISB/data/HighResMS2/noHK-centroid.tandem.pep.xml\" " +
                    "xmlns=\"http://regis-web.systemsbiology.net/pepXML\"" +
                    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                    " xsi:schemaLocation=\"http://sashimi.sourceforge.net/schema_revision/pepXML/pepXML_v115.xsd\">";


    public void writePepXMLHeader(String path, PrintWriter out) {
        String now = XTandemUtilities.xTandemNow();
        String header = PEPXML_HEADER.replace("%PATH%", path);
        header = header.replace("%DATE%", now);
        out.println(header);

        header = ANALYSIS_HEADER.replace("%PATH%", path);
        header = header.replace("%DATE%", now);
        out.println(header);

        out.println(TTRYPSIN_XML);  // tod stop hard coding

        showDatabase(out);
        showEnzyme(out);
        showParameters(out);
        out.println("      </search_summary>");
    }


    protected void showEnzyme(PrintWriter out) {
        IMainData application = getApplication();

        out.println("        <enzymatic_search_constraint enzyme=\"trypsin\" max_num_internal_cleavages=\"" +
                application.getDigester().getNumberMissedCleavages() +
                "\" />"
        );
    }

    protected void showDatabase(PrintWriter out) {
        IMainData application = getApplication();
        String[] parameterKeys = application.getParameterKeys();
        out.println("         <search_database local_path=\"" +
                application.getDatabaseName() +
                "\" type=\"AA\"" +
                "\" />"
        );
    }

    protected void showParameters(PrintWriter out) {
        IMainData application = getApplication();
        String[] parameterKeys = application.getParameterKeys();
        out.println("        <!-- Input parameters -->");
        for (int i = 0; i < parameterKeys.length; i++) {
            String parameterKey = parameterKeys[i];
            String value = application.getParameter(parameterKey);
            out.println("        <parameter name=\"" +
                    parameterKey +
                    " value=\"" +
                    value +
                    "\" />"
            );
        }
    }

    public void writePepXMLFooter(PrintWriter out) {
        out.println("     </msms_run_summary>");
        out.println("<msms_pipeline_analysis/>");
    }

    protected void writeSummaries(IScoredScan scan, PrintWriter out) {

        String algorithm = scan.getAlgorithm();
        ISpectralMatch[] spectralMatches = scan.getSpectralMatches();
        if (!"KScore".equals(algorithm))
            XTandemUtilities.breakHere();

        if (spectralMatches.length == 0)
            return;
        writeScanHeader(scan, out);
        switch (spectralMatches.length) {
            case 0:
                return;
            case 1:
                printMatch(scan, spectralMatches[0], null, 1, out);
                break;

            default:
                printLimitedMatches(scan, out, spectralMatches, getMatchesToPrint() );
        }
        out.println("      </spectrum_query>");
    }

    private void printLimitedMatches(final IScoredScan scan, final PrintWriter out, final ISpectralMatch[] pSpectralMatches, int matchesToPrint) {
        for (int i = 0; i < Math.min(matchesToPrint, pSpectralMatches.length); i++) {
            ISpectralMatch match = pSpectralMatches[i];
            int rank = i + 1;
            ISpectralMatch next = null;
            if (i < pSpectralMatches.length - 2)
                next = pSpectralMatches[i + 1];
            printMatch(scan, match, next, rank, out);
        }
    }

    protected void writeScanHeader(IScoredScan scan, PrintWriter out) {

        out.print("      <spectrum_query ");
        double precursorMass = scan.getRaw().getPrecursorMass();
        out.print(" spectrum=\"" + scan.getId() + "\"");
        out.print(" precursor_neutral_mass=\"" + String.format("%10.4f", precursorMass).trim() + "\"");
        out.print(" assumed_charge=\"" + scan.getCharge() + "\"");
        out.println(" >");
    }


    protected void printMatch(IScoredScan scan, ISpectralMatch match, ISpectralMatch nextmatch, int hitNum, PrintWriter out) {
        out.println("         <search_result>");
        out.print("            <search_hit hit_rank=\"" +
                hitNum +
                "\" peptide=\"");
        IPolypeptide peptide = match.getPeptide();
        out.print(peptide);
        out.print("\"");
        IMeasuredSpectrum conditionedScan = scan.getConditionedScan();
        int totalPeaks = conditionedScan.getPeaks().length;
        RawPeptideScan raw = scan.getRaw();
        double precursorMass = raw.getPrecursorMass(scan.getCharge());
        double pepMass = peptide.getMatchingMass();
        double delMass = precursorMass - pepMass;
        int numberMatchedPeaks = match.getNumberMatchedPeaks();
        //     out.print(" peptide_prev_aa=\"K\" ");
        //     out.print("peptide_next_aa=\"I\" " );
        // Let Refresh parser analyze for now
        IProteinPosition[] proteinPositions = peptide.getProteinPositions();
        if (proteinPositions.length > 0) {
            IProteinPosition pp = proteinPositions[0];
            showProteinPosition( pp,out);
        }
        out.print("");
        out.print(" num_tot_proteins=\"" + proteinPositions.length + "\" ");

        out.print(" num_matched_ions=\"" + numberMatchedPeaks + "\"");
        out.print(" tot_num_ions=\"" + totalPeaks + "\"");
        out.print(" calc_neutral_pep_mass=\"" + totalPeaks + "\" ");
        out.print(" massdiff=\"" + String.format("%10.4f", delMass).trim() + "\" ");
        ////      out.print("num_tol_term=\"2\" ");
        int missed_cleavages = peptide.getMissedCleavages();
        out.print("num_missed_cleavages=\"" + missed_cleavages + "\" ");
        //     out.print("is_rejected=\"0\">\n");
        out.println(" />");

        for (int i = 1; i < proteinPositions.length; i++) {
            showAlternateiveProtein(proteinPositions[i], out);

        }

        if (peptide.isModified()) {
            showModificationInfo((IModifiedPeptide) peptide, out);

        }

        double value = 0;
        value = match.getHyperScore();
        out.println("             <search_score name=\"hyperscore\" value=\"" +
                String.format("%10.4f", value).trim() + "\"/>");

        if (nextmatch != null) {
            value = nextmatch.getHyperScore();
            out.println("             <search_score name=\"nextscore\" value=\"" +
                    String.format("%10.4f", value).trim() + "\"/>");
        }
        double bvalue = match.getScore(IonType.B);
        out.println("             <search_score name=\"bscore\" value=\"" +
                String.format("%10.4f", bvalue).trim() + "\"/>");

        double yvalue = match.getScore(IonType.Y);
        out.println("             <search_score name=\"yscore\" value=\"" +
                String.format("%10.4f", yvalue).trim() + "\"/>");

        HyperScoreStatistics hyperScores = scan.getHyperScores();
        double expected = hyperScores.getExpectedValue(match.getScore());
        out.println("             <search_score name=\"expect\" value=\"" +
                String.format("%10.4f", expected).trim() + "\"/>");
        out.println("         </search_result>");
    }

    private void showProteinPosition( final IProteinPosition pPp,final PrintWriter out) {
        out.println(" protein=\"" + pPp.getProtein() + "\"");
        out.println("                       protein_descr=\"" + pPp.getProtein() + "\"");
        out.print("                        ");
          FastaAminoAcid before = pPp.getBefore();
        if (before != null)
            out.print(" peptide_prev_aa=\"" + before + "\"");
        FastaAminoAcid after = pPp.getBefore();
        if (after != null)
            out.print(" peptide_next_aa=\"" + after + "\"");
        out.println();
        out.print("                                     ");
    }

    protected void showModificationInfo(final IModifiedPeptide peptide, final PrintWriter out) {
        out.println("             <modification_info modified_peptide=\"" + peptide.getModifiedSequence() + "\" >");
        PeptideModification[] modifications = peptide.getModifications();
        for (int i = 0; i < modifications.length; i++) {
            PeptideModification modification = modifications[i];
            if (modification != null) {
                showModification(modification, i, out);
            }
        }
        out.println("             </modification>");

    }

    protected void showModification(final PeptideModification pModification, final int index, final PrintWriter out) {
        out.println("             <mod_aminoacid_mass position=\"" + index + "\" mass=\"" + String.format("%10.4f", pModification.getMassChange()).trim() + "\">");
    }

    protected void showAlternateiveProtein(final IProteinPosition pp, final PrintWriter out) {
        out.print("             <alternative_protein protein=\"" + pp.getProtein() + "\"");
        showProteinPosition(pp, out);
        out.println(" />");
    }


}
