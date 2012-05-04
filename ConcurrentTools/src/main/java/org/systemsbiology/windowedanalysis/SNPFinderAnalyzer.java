package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.fasta.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.location.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.BreakFinderAnalysis
 * written by Steve Lewis
 * on Apr 26, 2010
 */
public class SNPFinderAnalyzer extends ImplementationBase implements IPartitionedSetAnalysis {
    public static final SNPFinderAnalyzer[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = SNPFinderAnalyzer.class;
    public static final int MINIMAL_COVERAGE = 12;

    public static final int KEEP_ALIVE_RECORDS = 100;

    public static final String SCRIPT_NAME = "SNPFinder";


    private File m_ReportFile;
    private List<SNPCandidate> m_Regions =
            new ArrayList<SNPCandidate>();

    private final PartitionSetCoverage m_Coverage =
            new PartitionSetCoverage();


    public SNPFinderAnalyzer() {
    }


    public File getReportFile() {
        return m_ReportFile;
    }


    public void setReportFile(File pReportFile) {
        if (m_ReportFile == pReportFile)
            return;
        m_ReportFile = pReportFile;
    }

    public PartitionSetCoverage getCoverage() {
        return m_Coverage;
    }

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data, Object... more) {
        GenomeStatistics bst = null;
        PartitionSetCoverage ps = getCoverage();
        String className = getClass().getName();

        TaskInputOutputContext ctx = null;
         if (more.length > 0 && more[0] instanceof TaskInputOutputContext) {
             ctx = (TaskInputOutputContext) more[0];
             System.out.println("Analyzing " + data);
         //     BamHadoopUtilities.writeText(ctx,className,"Analyzing " + data);
         }

        ISubChromosomeInterval interval = data.getActiveInterval();
        IChromosome chr = interval.getChromosome();
        int start = (int) interval.getStart();
        ChromosomeSequence seq = ChromosomeSequence.getSequence(chr);
        int number_good_reads = 0;
        for (int index = start; index < interval.getEnd(); index++) {
            DNABase base = seq.getBase(index);

            if(index % KEEP_ALIVE_RECORDS == 0)
                   HadoopUtilities.keepAlive();

            if (base == null)
                continue;
            IGeneInterval here = new GenomeInterval(chr, index, index);
            final IExtendedPairedSamRecord[] allRecordsInInterval = data.getAllRecordsInInterval(here);
            final int nRecords = allRecordsInInterval.length;
            if (nRecords < MINIMAL_COVERAGE)
                continue;
            int[] baseCount = new int[4];
            int totalReads = 0;
            for (int i = 0; i < allRecordsInInterval.length; i++) {
                IExtendedPairedSamRecord sr = allRecordsInInterval[i];
                final DNABase position = sr.getBaseAtPosition(index);
                if (position != null) {
                    final int index1 = position.getIndex();
                    baseCount[index1]++;
                    totalReads++;
                }
            }

            final int index1 = base.getIndex();
            if (baseCount[index1] > (2 * nRecords) / 3)
                continue;

            final SNPCandidate candidate = new SNPCandidate(chr, index, base, baseCount);
            m_Regions.add(candidate);

        }
         SNPCandidate[] candidates = getSNPCandidates();
        for (int i = 0; i < candidates.length; i++) {
            SNPCandidate candidate = candidates[i];
           System.out.println(candidate.getXMLString().replace("\n"," "));
          //  BamHadoopUtilities.writeText(ctx,className,candidate.getXMLString().replace("\n"," "));
        }

        ps = null;
    }

    public SNPCandidate[] getSNPCandidates() {
        return m_Regions.toArray(new SNPCandidate[0]);
    }

    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
        for (SNPCandidate rg : getSNPCandidates()) {
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

    public static class SNPCandidate extends ImplementationBase {
        private final IChromosome m_Chromosome;
        private final int m_Position;
        private final DNABase m_ReferenceBase;
        private final int[] m_Counts = new int[4];

        public SNPCandidate(final IChromosome pChromosome, final int pPosition, final DNABase pReferenceBase, int[] counts) {
            m_Chromosome = pChromosome;
            m_Position = pPosition;
            m_ReferenceBase = pReferenceBase;
            for (int i = 0; i < counts.length; i++) {
                m_Counts[i] = counts[i];

            }
        }

        public IChromosome getChromosome() {
            return m_Chromosome;
        }

        public int getPosition() {
            return m_Position;
        }

        public DNABase getReferenceBase() {
            return m_ReferenceBase;
        }

        public int[] getCounts() {
            return m_Counts;
        }
    }
}