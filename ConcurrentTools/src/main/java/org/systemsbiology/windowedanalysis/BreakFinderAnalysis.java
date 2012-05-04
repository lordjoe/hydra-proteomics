package org.systemsbiology.windowedanalysis;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.location.*;
import org.systemsbiology.partitioning.*;
import com.lordjoe.lib.xml.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.BreakFinderAnalysis
 * written by Steve Lewis
 * on Apr 26, 2010
 */
public class BreakFinderAnalysis extends ImplementationBase implements IPartitionedSetAnalysis, IBreakFinderParameters {
    public static final BreakFinderAnalysis[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = BreakFinderAnalysis.class;

    public static final String SCRIPT_NAME = "BreakFinderAnalysis";


    private File m_ReportFile;
    private GenomeStatistics m_Statistics;
    private List<RegionOfInterest> m_Regions =
            new ArrayList<RegionOfInterest>();

    private final PartitionSetCoverage m_Coverage =
            new PartitionSetCoverage();
    private int m_MinimumWidth = MINIMUM_WIDTH;
    private int m_MinimumReads = MINIMUM_READS;
    private double m_MinimumFractionSupporting = MINIMUM_FRACTION_SUPPORTING;
    private int m_MinimumTestCoverage = MINIMUM_TEST_COVERAGE_;


    private int m_MinimumCoverage = MINIMUM_COVERAGE_;

    private int m_NumberGoodToExitRegion = NUMBER_GOOD_TOEXIT_REGION_;

    private double m_MaxProbabilityToStartRegion = LOW_PROBABILITY_;


    public BreakFinderAnalysis() {
    }


    public int getMinimumTestCoverage() {
        return m_MinimumTestCoverage;
    }

    public void setMinimumTestCoverage(int pMinimumTestCoverage) {
        m_MinimumTestCoverage = pMinimumTestCoverage;
    }

    public int getMinimumCoverage() {
        return m_MinimumCoverage;
    }

    public void setMinimumCoverage(int pMinimumCoverage) {
        m_MinimumCoverage = pMinimumCoverage;
    }

    public int getNumberGoodToExitRegion() {
        return m_NumberGoodToExitRegion;
    }

    public void setNumberGoodToExitRegion(int pNumberGoodToExitRegion) {
        m_NumberGoodToExitRegion = pNumberGoodToExitRegion;
    }

    public double getMaxProbabilityToStartRegion() {
        return m_MaxProbabilityToStartRegion;
    }

    public void setMaxProbabilityToStartRegion(double pMaxProbabilityToStartRegion) {
        m_MaxProbabilityToStartRegion = pMaxProbabilityToStartRegion;
    }

    public int getMinimumWidth() {
        return m_MinimumWidth;
    }

    public void setMinimumWidth(int pMinimumWidth) {
        m_MinimumWidth = pMinimumWidth;
    }

    public int getMinimumReads() {
        return m_MinimumReads;
    }

    public void setMinimumReads(int pMinimumReads) {
        m_MinimumReads = pMinimumReads;
    }

    public double getMinimumFractionSupporting() {
        return m_MinimumFractionSupporting;
    }

    public void setMinimumFractionSupporting(double pMinimumFractionSupporting) {
        m_MinimumFractionSupporting = pMinimumFractionSupporting;
    }


    public File getReportFile() {
        return m_ReportFile;
    }

    public RegionOfInterest[] getRegionsOfInterest() {
        return m_Regions.toArray(RegionOfInterest.EMPTY_ARRAY);
    }

    public void clearRegionsOfInterest() {
          m_Regions.clear();
    }

    public void addRegionOfInterest(RegionOfInterest added) {
        m_Regions.add(added);
    }

    public void setReportFile(File pReportFile) {
        if (m_ReportFile == pReportFile)
            return;
        m_ReportFile = pReportFile;
    }

    public PartitionSetCoverage getCoverage() {
        return m_Coverage;
    }

    public GenomeStatistics getStatistics() {
        if (m_Statistics == null)
            m_Statistics = buildStatistics();
        return m_Statistics;
    }

 


    protected GenomeStatistics buildStatistics() {
        GenomeStatistics ret = new GenomeStatistics();
        XMLSerializer.parseFile(getReportFile(), ret);
        return ret;
    }

    protected GenomeStatistics buildStatistics(String report) {
        GenomeStatistics ret = new GenomeStatistics();
        XMLSerializer.parseXMLString(report, ret);
        return ret;
    }

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data, Object... more) {
        String className = getClass().getName();
        GenomeStatistics bst = null;
        TaskInputOutputContext ctx = null;
        if (more.length > 0 && more[0] instanceof TaskInputOutputContext) {
            ctx = (TaskInputOutputContext) more[0];
            Configuration conf = ctx.getConfiguration();
            if (m_Statistics == null) {
                String report = conf.get(HadoopUtilities.STATISTICS_REPORT_KEY);
                m_Statistics = buildStatistics(report);
            }
            bst = m_Statistics;

            System.out.println("Analyzing " + data);
            //  BamHadoopUtilities.writeText(ctx,className,"Analyzing " + data);

        }
        else {
            bst = buildStatistics();
        }

        DistanceDistribution dst = bst.getDistanceDistribution();
        PartitionSetCoverage ps = getCoverage();
        RegionOfInterest activeRegion = null;

        ps.analyze(data, dst);
        ISubChromosomeInterval interval = data.getActiveInterval();
        IChromosome chr = interval.getChromosome();
        int start = interval.getStart();

        int number_good_reads = 0;
        for (int index = start; index < interval.getEnd(); index++) {
            short coverage = ps.getCoverageAtPosition((int) (index - start));
            if (coverage < getMinimumCoverage())
                continue;
            short[] distances = ps.getDistancesAtPosition((int) (index - start));
            if (distances == null || distances.length < getMinimumCoverage())
                continue;
            Arrays.sort(distances);
            double[] probs = dst.getTriTileProbabilities(distances);
            //    double prob = dst.getProbability(distances);
            if (index % 200 == 0)
                CommonUtilities.breakHere();
            double prob1 = probs[0];
            double prob2 = probs[1];
            if (prob1 < getMaxProbabilityToStartRegion() || prob2 < getMaxProbabilityToStartRegion()) {
                double prob = Math.min(probs[0], probs[1]);
                IGeneLocation loc = new GenomeLocation(chr, index);
                if (activeRegion == null) {
                    ChromosomalAbnormalityType type = determineTypeAbnormality(distances, dst);
                    activeRegion = new RegionOfInterest(chr, type);
                    activeRegion.setParameters(this);
                }
                number_good_reads = 0; // we are in an active region
                IExtendedPairedSamRecord[] recs = ((PartitionedSamSet)data).getAllRecordsInInterval(new GenomeInterval(loc));
                 PositionOfInterest positionOfInterest = new PositionOfInterest(loc, coverage, distances,recs);
                positionOfInterest.setDistanceProbability(prob);
                activeRegion.addPositionOfInterest(positionOfInterest);
            }
            else {
                number_good_reads++;
                if (activeRegion != null && number_good_reads > getNumberGoodToExitRegion()) {
                    activeRegion.close(  dst);
                    String valid = activeRegion.isValid();
                    if (valid == null) {
                        addRegionOfInterest(activeRegion);
                         System.out.println("Saving active region " + activeRegion + " offset "  + activeRegion.getOffsetMedian() +
                         " reads " +  activeRegion.getAllReads().length +
                         " supporting " +  activeRegion.getSupportingReads().length );
                    }
                    else {
                        System.out.println("Dropping region " + activeRegion + "because " + valid);
                    }
                    activeRegion = null;
                }
            }
        }
        RegionOfInterest[] ret = getRegionsOfInterest();
        for (int i = 0; i < ret.length; i++) {
            RegionOfInterest ri = ret[i];
            if(ctx != null) {
                 String xml = ri.getXMLString();
                 try {
                    ctx.write(new Text(getClass().getName()),  new Text(xml));
                 }
                 catch (IOException e) {
                     throw new RuntimeException(e);

                 }
                 catch (InterruptedException e) {
                     throw new RuntimeException(e);

                 }
             }

        }
        ;

        ps = null;
    }

    protected ChromosomalAbnormalityType determineTypeAbnormality(short[] distances, DistanceDistribution dst) {
        Arrays.sort(distances);
        double[] probs = dst.getTriTileProbabilities(distances);
        if (probs[1] < getMaxProbabilityToStartRegion())
            return ChromosomalAbnormalityType.Deletion;
        if (probs[0] < getMaxProbabilityToStartRegion())
            return ChromosomalAbnormalityType.Insertion;
        return ChromosomalAbnormalityType.TransChromosomal;

    }

    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
        for (RegionOfInterest rg : getRegionsOfInterest()) {
            rg.appendXML(props, sb, indent + 1);
        }
    }

    /**
     * take action at the end of a process
     *
     * @param added  other data
     */
    public void finish(Object... added) {
        // we will be called by our m_ParentStream to write results
    }
}
