package org.systemsbiology.igv;

import com.lordjoe.utilities.*;

import java.io.*;

/**
 * org.systemsbiology.igv.igvutilities
 * User: Steve
 * Date: 4/30/12
 */
public class IGVUtilities {
    public static final IGVUtilities[] EMPTY_ARRAY = {};

    public static void writeIGVFile(PrintWriter out, TranscriptomeData[] data) {
        writeIGVFile(out, data, true);
    }

    public static final double MAX_HEAT_MAP = 1.5;
    public static final double MIN_HEAT_MAP = -MAX_HEAT_MAP;
    public static final double MAX_DATA_VALUE = 2000;
    public static final double MIN_DATA_VALUE = -MAX_DATA_VALUE;
    public static final double MIN_RESOLVED_DATA = MAX_DATA_VALUE / 100;
    public static final double LOG_MIN_RESOLVED_DATA = Math.log(MIN_RESOLVED_DATA / MAX_DATA_VALUE);

    public static double positiveLogFunction(double in) {
        if (in < MIN_RESOLVED_DATA)
            return 0;
        if (in > MAX_DATA_VALUE)
            return MAX_HEAT_MAP;
        double val = Math.log(in / MAX_DATA_VALUE);

        double v = (LOG_MIN_RESOLVED_DATA - val) / LOG_MIN_RESOLVED_DATA;
        double ret = MAX_HEAT_MAP * v;
        return ret;
    }

    public static double logFunction(double in) {
        if (in >= 0)
            return positiveLogFunction(in);
        else
            return -positiveLogFunction(-in);
    }


    public static final String[] TYPES = {"24 Hours", "48 Hours", "Difference"};

    /**
     * http://www.broadinstitute.org/software/igv/IGV
     *
     * @param out
     * @param data
     * @param asTrack
     */
    public static void writeIGVFile(PrintWriter out, TranscriptomeData[] data, boolean asTrack) {
        if (asTrack)
            // http://www.broadinstitute.org/software/igv/TrackLine
            out.println("#track");
        // http://www.broadinstitute.org/software/igv/TypeLine
        out.println("#type=GENE_EXPRESSION");

        /*
An IGV file (.igv) is a tab-delimited text file that defines tracks.
The first row contains column headings for chromosome, start location, end location, and feature followed by the name of each track defined in the .igv file. Each subsequent row contains a locus and the associated numeric values for each track. IGV interprets the first four columns as chromosome, start location, end location, and feature name regardless of the column headings in the file. IGV uses the column headings for the fifth and subsequent columns as track names. Feature names are not displayed in IGV.


         */
        out.println("Chromosome\tStart\tEnd\tFeature\t24 Hours\t48 Hours\tDifference");
        for (int i = 0; i < data.length; i++) {
            TranscriptomeData td = data[i];
            writeIGVLine(out, td);
        }
        out.close();
    }

    /**
     * http://www.broadinstitute.org/software/igv/IGV
     *
     * @param out
     * @param data
     * @param asTrack
     */
    public static void writeIGVLine(PrintWriter out, TranscriptomeData data) {
        TranscriptionValue H24 = data.getTranscriptionValue(TYPES[0]);
        TranscriptionValue H48 = data.getTranscriptionValue(TYPES[1]);
        TranscriptionValue df = data.getTranscriptionValue(TYPES[2]);
        if (H24.getOriginalValue() == 0 && H48.getOriginalValue() == 0)
            return;
        out.print(data.getChromosome());
        out.print("\t");
        out.print(data.getStart());
        out.print("\t");
        out.print(data.getEnd());
        out.print("\t");
        out.print(data.getGeneName());
        out.print("\t");
        out.print(H24.getNormalizedValue());
        out.print("\t");
        out.print(H48.getNormalizedValue());
        out.print("\t");
        out.print(df.getNormalizedValue());


        out.println();
    }

    /**
     * http://www.broadinstitute.org/software/igv/IGV
     *
     * @param out
     * @param data
     * @param asTrack
     */
    public static void writeGFFFile(PrintWriter out, TranscriptomeData[] data) {

        /*
An IGV file (.igv) is a tab-delimited text file that defines tracks.
The first row contains column headings for chromosome, start location, end location, and feature followed by the name of each track defined in the .igv file. Each subsequent row contains a locus and the associated numeric values for each track. IGV interprets the first four columns as chromosome, start location, end location, and feature name regardless of the column headings in the file. IGV uses the column headings for the fifth and subsequent columns as track names. Feature names are not displayed in IGV.


         */
        writeGffHeader(out);
        out.println("Chromosome\tStart\tEnd\tFeature\t24 Hours\t48 Hours\tDifference");
        for (int i = 0; i < data.length; i++) {
            TranscriptomeData td = data[i];
            writeGFFLine(out, td);
        }
        out.close();
    }

    private static void writeGffHeader(PrintWriter out) {
        out.println("##gff-version 3\n" +
                "#!gff-spec-version 1.20\n" +
                "#!processor NCBI annotwriter\n" +
                "##sequence-region NC_006570.2 1 1892775");
    }

    /**
     * http://www.broadinstitute.org/software/igv/IGV
     *
     * @param out
     * @param data
     * @param asTrack
     */
    public static void writeGFFLine(PrintWriter out, TranscriptomeData data) {
        out.print(data.getChromosome());
        out.print("\t");
        out.print("RefSeq");
        out.print("\t");
        out.print("gene");
        out.print("\t");
        out.print(data.getStart());
        out.print("\t");
        out.print(data.getEnd());
        out.print("\t");
        out.print(".\t+\t."); // no score posisive direction no group
        out.print("\t");
        out.print(data.getGeneName());

        out.println();
    }

    public static void normalizeData(TranscriptomeData[] data) {
        double maxData = Double.MIN_VALUE;
        double maxDiff = Double.MIN_VALUE;
        double minDiff = Double.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            TranscriptomeData td = data[i];
            TranscriptionValue tv = td.getTranscriptionValue("24 Hours");
            if (tv != null)
                maxData = Math.max(tv.getOriginalValue(), maxData);
            tv = td.getTranscriptionValue("48 Hours");
            if (tv != null)
                maxData = Math.max(tv.getOriginalValue(), maxData);
            tv = td.getTranscriptionValue("Difference");
            if (tv != null) {
                maxDiff = Math.max(tv.getOriginalValue(), maxDiff);
                minDiff = Math.min(tv.getOriginalValue(), minDiff);
            }
        }
        maxData = 2000;
        maxDiff = 2000;
        minDiff = -2000;

        maxDiff = Math.max(maxDiff, Math.abs(minDiff));
        for (int i = 0; i < data.length; i++) {
            TranscriptomeData td = data[i];
            TranscriptionValue tv = td.getTranscriptionValue("24 Hours");
            if (td.getGeneName().equals("FTT_0101"))
                IGVUtilities.breakHere();
            if (tv != null)
                tv.setNormalizedValue(logFunction(tv.getOriginalValue()));
            tv = td.getTranscriptionValue("48 Hours");
            if (tv != null)
                tv.setNormalizedValue(logFunction(tv.getOriginalValue()));
        }

    }

    public static void filterGenesOnly(String originalGFF, String newGFF) {
        try {
            String[] lines = FileUtilities.readInLines(originalGFF);
            PrintWriter out = new PrintWriter(new FileWriter(newGFF));
            // start at 1 to drop headers
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.startsWith("#")) {
                    out.println(line);
                    continue;
                }
                if (line.contains("\tgene\t"))
                    out.println(line);
            }
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private static void buildIGVFile(String arg) throws IOException {
        TranscriptomeData[] data = TranscriptomeData.readCSV(arg);
        PrintWriter out = new PrintWriter(new FileWriter("data.igv"));
        normalizeData(data);
        writeIGVFile(out, data);
        out = new PrintWriter(new FileWriter("data.gff"));
        writeGFFFile(out, data);
    }


    public static void breakHere() {
    }

    public static void main(String[] args) throws Exception {
       // filterGenesOnly(args[0], args[1]);
          buildIGVFile(args[0]);
    }

}
