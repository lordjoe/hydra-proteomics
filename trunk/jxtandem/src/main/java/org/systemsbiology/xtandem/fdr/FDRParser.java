package org.systemsbiology.xtandem.fdr;

import com.lordjoe.utilities.FileUtilities;


/**
 * org.systemsbiology.xtandem.fdr.FDRUtilities
 *
 * @author attilacsordas
 * @date 09/05/13
 */
public class FDRParser {

    public static final boolean USE_EXPECTED = false; // otherwise use score

    private final String m_Filename;
    private final IDiscoveryDataHolder m_Handler;
    private final int m_MaxHits;


    public FDRParser(String filename) {
        this(filename, Integer.MAX_VALUE);
    }

    public FDRParser(String filename, int maxhits) {
        m_Filename = filename;
        m_MaxHits = maxhits;
        if (USE_EXPECTED)
            m_Handler = FDRUtilities.getDiscoveryDataHolder("Default Algorighm", false);      // better us low
        else
            m_Handler = FDRUtilities.getDiscoveryDataHolder("Default Algorighm", true);   // better us high

        readFileAndGenerateFDR();    // read file populate the  IDiscoveryDataHolder

    }

    public String getFilename() {
        return m_Filename;
    }

    public IDiscoveryDataHolder getHandler() {
        return m_Handler;
    }

    /**
     *
     */
    private void readFileAndGenerateFDR() {
        String[] lines = FileUtilities.readInLines(m_Filename);

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains("<search_hit")) {
                //              System.out.println(line);
                i = handleSearchHit(lines, i);

            }

        }

    }


    protected int handleSearchHit(String[] lines, int index) {

        Double expectedValue = null;
        Double hyperScoreValue = null;

        String line = lines[index++];   // handle first line
        Boolean trueHit = !line.contains("protein=\"DECOY_");
        boolean processSpectrum = parseHitValue(line) <= m_MaxHits;

        for (; index < lines.length; index++) {
            line = lines[index];

            if (line.contains("</search_hit"))
                break;         // we are done
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
        }

        if (processSpectrum) {
            final IDiscoveryDataHolder hd = getHandler();
            double score;
            if (USE_EXPECTED)
                score = expectedValue;
            else
                score = hyperScoreValue;

            if (trueHit)
                hd.addTrueDiscovery(score);
            else
                hd.addFalseDiscovery(score);

        }
        return index;

    }

    public static double parseValue(String line) {
        String s = parseQuotedValue(line, "value");
        if (s.length() == 0)
            return 0;
        return Double.parseDouble(s);
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


    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            FDRParser fdrParser = new FDRParser(arg);
            final IDiscoveryDataHolder handler = fdrParser.getHandler();
            final String s = FDRUtilities.listFDRAndCount(handler);
            System.out.println(s);
        }

    }

}
