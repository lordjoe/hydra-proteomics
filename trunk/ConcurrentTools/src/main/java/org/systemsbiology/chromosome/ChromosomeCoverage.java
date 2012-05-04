package org.systemsbiology.chromosome;

import org.systemsbiology.common.*;
import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;

/**
 * org.systemsbiology.chromosome.ChromosomeCoverage
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class ChromosomeCoverage implements ISamRecordVisitor
{
    public static final ChromosomeCoverage[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeCoverage.class;

    private final IChromosome m_Chromosome;
    private final int m_BinWidth;
    private final IntegerHistogram m_Values;

    public ChromosomeCoverage(IChromosome pChromosome, int pBinWidth) {
        m_Chromosome = pChromosome;
        m_BinWidth = pBinWidth;
        m_Values = new IntegerHistogram(m_BinWidth);
        m_Values.setNumberBins((int)(m_Chromosome.getLength() / m_BinWidth));
    }

    @Override
    public boolean visit(final IExtendedSamRecord record, final Object... added) {
        return false;
    }

    @Override
    public void initialize(final Object... added) {

    }

    @Override
    public void finish(final Object... added) {

    }

    @Override
    public boolean isSortedRequired() {
        return false;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public int getBinWidth() {
        return m_BinWidth;
    }

    public void addStart(int start)
    {
        m_Values.addItemAt(start);
    }

    public IntegerHistogramBin[] getAllBins()
      {
          return m_Values.getAllBins();
      }

      public IntegerHistogramBin[] getAllNonZeroBins()  {
          return m_Values.getAllNonZeroBins();

      }

     public void writeData(PrintWriter out) {
         IntegerHistogramBin[] bins = getAllNonZeroBins();
         for (int i = 0; i < bins.length; i++) {
             IntegerHistogramBin bin = bins[i];
             out.print(getChromosome().getAlternateName() + "\t");
             out.println(bin);
         }
     }

    /**
     * implemt the visitor pattern for a SAMRecord
     *
     * @param record
     */
    public boolean visit(IExtendedSamRecord record) {
        if(record.getChromosome() != getChromosome())
            return false;
        m_Values.addItemAt(record.getAlignmentStart());
        return true;
    }

    /**
     * reset to base state
     */
    public void reset() {
        m_Values.reset();
        m_Values.setNumberBins((int)(m_Chromosome.getLength() / m_BinWidth));
     }

    /**
     * reset to base state
     */
    public void finish() {
        // do nothing
    }
}
