package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.location.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.BreakFinder
 * written by Steve Lewis
 * on Apr 26, 2010
 */
public class BreakFinder extends ImplementationBase implements ICoverageAnalyzer, IBreakFinderParameters {
    public static final BreakFinder[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = BreakFinder.class;

    public static final String SCRIPT_NAME = "BreakFinder";


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
    private RegionOfInterest m_ActiveRegion;
    private int m_NumberGoodReads;


    public BreakFinder() {
    }

    public int getNumberGoodReads() {
        return m_NumberGoodReads;
    }

    public void setNumberGoodReads(final int pNumberGoodReads) {
        m_NumberGoodReads = pNumberGoodReads;
    }

    public RegionOfInterest getActiveRegion() {
        return m_ActiveRegion;
    }

    public void setActiveRegion(final RegionOfInterest pActiveRegion) {
        m_ActiveRegion = pActiveRegion;
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
        File file = getReportFile();
        XMLSerializer.parseFile(file, ret);
        return ret;
    }

    protected GenomeStatistics buildStatistics(String report) {
        GenomeStatistics ret = new GenomeStatistics();
        XMLSerializer.parseXMLString(report, ret);
        return ret;
    }

    @Override
    public void startWindow(final Object... more) {
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


        }
        else {
            bst = buildStatistics();
        }
        DistanceDistribution dst = bst.getDistanceDistribution();
        PartitionSetCoverage ps = getCoverage();
        RegionOfInterest activeRegion = null;

    }

    @Override
    public void startActiveWindow(final Object... more) {

    }

    @Override
    public void leaveActiveWindow(final Object... more) {

    }

    @Override
    public void endWindow(final Object... more) {

    }

    public static short[] getDistancesAtPosition(IExtendedPairedSamRecord[] recs) {
        short[] ret = new short[recs.length];
        for (int i = 0; i < ret.length; i++) {
            long l = recs[i].getMateDistance();
            ret[i] = (short) Math.min(Short.MAX_VALUE, Math.max(0, l));
        }
        Arrays.sort(ret);
        return ret;
    }

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    @Override
    public void visit(final SinglePositionCoverage data, final Object... more) {
        IExtendedPairedSamRecord[] recs = data.getCoveringRecords();
        int nRecords = recs.length;
        if (nRecords < getMinimumCoverage())
            return;
        short[] distances = getDistancesAtPosition(recs);
        if (distances == null || distances.length < getMinimumCoverage())
            return;

        String className = getClass().getName();
        TaskInputOutputContext ctx = null;
         if (more.length > 0 && more[0] instanceof TaskInputOutputContext) {
             ctx = (TaskInputOutputContext) more[0];
            // BamHadoopUtilities.writeText(ctx, className, className + " Analyzing " + data + " with " + nRecords + " records");
         }

        DistanceDistribution dst = m_Statistics.getDistanceDistribution();
        double[] probs = dst.getTriTileProbabilities(distances);
        //    double prob = dst.getProbability(distances);
        double prob1 = probs[0];
        double prob2 = probs[1];
        if (prob1 < getMaxProbabilityToStartRegion() || prob2 < getMaxProbabilityToStartRegion()) {
            IChromosome chr = data.getChromosome();
            int index = data.getCurrentPosition();
            double prob = Math.min(probs[0], probs[1]);
            IGeneLocation loc = new GenomeLocation(chr, index);
            if (getActiveRegion() == null) {
                ChromosomalAbnormalityType type = determineTypeAbnormality(distances, dst);
                RegionOfInterest activeRegion = new RegionOfInterest(chr, type);
                activeRegion.setParameters(this);
                setActiveRegion(activeRegion);
            }
            setNumberGoodReads(0);
            PositionOfInterest positionOfInterest = new PositionOfInterest(loc, (short) nRecords, distances, recs);
            positionOfInterest.setDistanceProbability(prob);
            getActiveRegion().addPositionOfInterest(positionOfInterest);
        }
        else {
            RegionOfInterest activeRegion = getActiveRegion();
            setNumberGoodReads(getNumberGoodReads() + 1);
            if (activeRegion != null) {
                if (getNumberGoodReads() > getNumberGoodToExitRegion()) {
                    activeRegion.close(dst);
                    String valid = activeRegion.isValid();
                    if (valid == null) {
                        addRegionOfInterest(activeRegion);
                        // I think hadoop likes output on one linr
                        String message = activeRegion.getXMLString().replace("\n", " ");
                        System.out.println(message);
                     //     BamHadoopUtilities.writeText(ctx,className, message);
                        
                        System.out.println("Saving active region " + activeRegion);
                    }
                    else {
                        System.out.println("Dropping region " + activeRegion + "because " + valid);
                    }
                    setActiveRegion(null);
                 }
            }

        }

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