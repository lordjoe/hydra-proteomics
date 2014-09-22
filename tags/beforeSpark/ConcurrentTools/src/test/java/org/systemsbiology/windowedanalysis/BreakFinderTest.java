package org.systemsbiology.windowedanalysis;


/**
 * org.systemsbiology.windowedanalysis.BreakFinderTest
 * User: steven
 * Date: Jun 23, 2010
 */
@SuppressWarnings("UnusedDeclaration")
public class BreakFinderTest {
    public static final BreakFinderTest[] EMPTY_ARRAY = {};

//    @SuppressWarnings("UnusedDeclaration")
//    public static final String TEST_FILE = "PartitionedYeastData/chrIII-120k-140k.sam";
//    @SuppressWarnings("UnusedDeclaration")
//    public static final String CONFIG_FILE = "config/YeastBreaks.config";
//    @SuppressWarnings("UnusedDeclaration")
//    public static final String REPORT_FILE = "YeastReports/yeastreport.xml";
//
//    @Test
//    public void testNothing() {
//
//    }
//
////    public void testOneReduce() {
////        StringRecordWriter<Text, Text> rw = new StringRecordWriter<Text, Text>();
////        MockTaskContext<Text, Text, Text, Text> ctx = new MockTaskContext(rw);
////
////        SamConfigurer sc = SamConfigurer.getInstance();
////        Configuration conf = ctx.getConfiguration();
////
////        BamHadoopUtilities.readConfigFile(conf, CONFIG_FILE);
////
////        conf.set(BamHadoopUtilities.REPORT_KEY, REPORT_FILE);
////
////        // should only be an issue when running on one box
////        synchronized (sc) {
////            if (!sc.isConfigured()) {
////                String[] strings = conf.getStrings(BamHadoopUtilities.CONFIGURATION_KEY);
////                sc.configure(strings);
////            }
////        }
////
////
////        String fileName = conf.get(BamHadoopUtilities.REPORT_KEY);
////        System.err.println("Report file is " + fileName);
////        if (fileName.contains("="))
////            fileName = fileName.split("=")[1];
////
////        String report = null;
////        File reportFile = new File(fileName);
////        if (reportFile.exists()) {
////            report = FileUtilities.readInFile(reportFile);
////            //  System.err.println(report);
////        }
////
////        if (report != null) {
////            conf.set(BamHadoopUtilities.STATISTICS_REPORT_KEY, report);
////        }
////
////
////        BreakFinder bf = new BreakFinder();
////        File testFile = new File(TEST_FILE);
////
////        GenomeLocation loc = new GenomeLocation(DefaultChromosome.parseChromosome("chrIII"), 130000);
////        WindowAnalysisUtilities.analyzeFile(testFile, ctx, bf, loc);
////
////        String output = rw.getText();
////        System.out.println(output);
////        // validate text
////    }
//
}
