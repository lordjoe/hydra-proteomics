package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import org.apache.hadoop.mapreduce.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.fasta.*;
import org.systemsbiology.sam.*;

import java.io.*;

/**
 * org.systemsbiology.windowedanalysis.SNPFinder
 * Class rewritten to sit on the coverage howdah
 * written by Steve Lewis
 * on Apr 26, 2010
 */
public class SNPFinder extends ImplementationBase implements ICoverageAnalyzer {
    public static final SNPFinder[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = SNPFinder.class;

    //
    public static final int DEFAULT_MINIMUM_COVERAGE = 12;

    public static final String SCRIPT_NAME = "SNPFinder";

    private int m_MinmumuCoverage = DEFAULT_MINIMUM_COVERAGE;

    public SNPFinder() {
    }

    public int getMinmumuCoverage() {
        return m_MinmumuCoverage;
    }

    public void setMinmumuCoverage(final int pMinmumuCoverage) {
        m_MinmumuCoverage = pMinmumuCoverage;
    }

    @Override
    public void startWindow(final Object... more) {
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

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    @Override
    public void visit(final SinglePositionCoverage data, final Object... more) {
        GenomeStatistics bst = null;
        // fix bugs later
        if(true)
             return;

        String className = getClass().getName();

        IExtendedPairedSamRecord[] recs = data.getCoveringRecords();
        // too few records
        int nRecords = recs.length;
        if (nRecords < getMinmumuCoverage())
            return;
        TaskInputOutputContext ctx = null;
        if (more.length > 0 && more[0] instanceof TaskInputOutputContext) {
            ctx = (TaskInputOutputContext) more[0];
           // BamHadoopUtilities.writeText(ctx, className, className + " Analyzing " + data + " with " + nRecords + " records");
        }
        IChromosome chr = data.getChromosome();
        ChromosomeSequence seq = ChromosomeSequence.getSequence(chr);

        int index = data.getCurrentPosition();
        DNABase base = seq.getBase(index);
        if (base == null)
            return;
        int[] baseCount = new int[4];
        int totalReads = 0;
        for (int i = 0; i < recs.length; i++) {
            IExtendedPairedSamRecord sr = recs[i];
            final DNABase position = sr.getBaseAtPosition(index);
            if (position != null) {
                final int index1 = position.getIndex();
                baseCount[index1]++;
                totalReads++;
            }
        }

        final int index1 = base.getIndex();
        if (baseCount[index1] > (2 * nRecords) / 3)
            return;

        final SNPCandidate candidate = new SNPCandidate(chr, index, base, baseCount);

        // Drop until this works well
      //  BamHadoopUtilities.writeText(ctx, className, candidate.getXMLString().replace("\n", " "));

    }


    @Override
    protected void appendCollectionProperties(XMLPropertySet props, Appendable sb, int indent) throws IOException {
        super.appendCollectionProperties(props, sb, indent);
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

        @Override
        protected void appendAttributes(XMLPropertySet props, Appendable sb) throws IOException {
            super.appendAttributes(props, sb);
            addPropertyAttribute("chromosome", props, sb);
            addPropertyAttribute("position", props, sb);
            addPropertyAttribute("referenceBase", props, sb);
            addPropertyAttribute("counts", props, sb);
        }

    }
}