package org.systemsbiology.windowedanalysis;


import java.io.*;

/**
 * org.systemsbiology.windowedanalysis.WindowAnalysisUtilities
 * User: steven
 * Date: Jun 23, 2010
 */
public class WindowAnalysisUtilities {
    public static final WindowAnalysisUtilities[] EMPTY_ARRAY = {};

    public static final int DEFAULT_WIDTH = 20000;
    public static final int DEFAULT_OVERLAP = 500;

    private static int gPartitionWidth = DEFAULT_WIDTH;
    private static int gPartitionOverlap = DEFAULT_OVERLAP;

    public static int getPartitionWidth() {
        return gPartitionWidth;
    }

    public static void setPartitionWidth(final int pPartitionWidth) {
        gPartitionWidth = pPartitionWidth;
    }

    public static int getPartitionOverlap() {
        return gPartitionOverlap;
    }

    public static void setPartitionOverlap(final int pPartitionOverlap) {
        gPartitionOverlap = pPartitionOverlap;
    }

//    public  static <K,V> void analyzeFile(File input, MockTaskContext<K,V,K,V> ctx ,ICoverageAnalyzer an, IGeneLocation loc) {
//
//         PartitionSet ps = PartitioningFactory.buildConstantWidthPartitions(getPartitionWidth(),  getPartitionOverlap());
//        PartitionedSamSet ds = new PartitionedSamSet(ps.getPartitionSources(loc)[0]);
//        IExtendedSamRecord[] recsx = BamHadoopUtilities.readFile(input);
//        for (int i = 0; i < recsx.length; i++) {
//
//            ExtendedSamRecord record1 = (ExtendedSamRecord) recsx[i];
//            ExtendedPairedSamRecord record = new ExtendedPairedSamRecord(record1.getRecord());
//            ds.addRecord(record);
//
//        }
//        CoverageAnalysisSet cs = new CoverageAnalysisSet();
//
//        an.startWindow(ctx);
//        cs.addAnalyzer("test", an);
//        cs.analyze(ds,ctx);
//
//
//    }


}
