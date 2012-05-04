package org.systemsbiology.coverage;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.coverage.SortedChromosomeCoverage
 * User: steven
 * Date: May 28, 2010
 */
public class SortedChromosomeCoverage implements ISamRecordVisitor , Comparable<SortedChromosomeCoverage> {
    public static final SortedChromosomeCoverage[] EMPTY_ARRAY = {};



    private static final Map<IChromosome,SortedChromosomeCoverage> gChromosomeToCoverage= new HashMap<IChromosome,SortedChromosomeCoverage>();

    public static SortedChromosomeCoverage getChromosomeCoverage(IChromosome chr)   {
        SortedChromosomeCoverage ret;
        synchronized (gChromosomeToCoverage)   {
             ret = gChromosomeToCoverage.get(chr);
            if(ret == null ) {
                ret = new SortedChromosomeCoverage(chr);
                gChromosomeToCoverage.put(chr,ret);
            }
        }
        return ret;
    }

    public static final int DEFAULT_BIN_DIWTH = 100;
    private static int gBatchBinWidth = DEFAULT_BIN_DIWTH;


    public static int getBatchBinWidth() {
        return gBatchBinWidth;
    }

    public static void setBatchBinWidth(final int pBatchBinWidth) {
        gBatchBinWidth = pBatchBinWidth;
    }

    private PrintWriter m_Output;
    private final IChromosome m_Chromosome;
    private final int m_BinWidth;
    private int m_CurrentPosition;
    private int m_CurrentBin;

    private final CoverageSet m_Coverage = new CoverageSet();


    private SortedChromosomeCoverage( final IChromosome pChromosome) {
        m_BinWidth =  getBatchBinWidth();
        m_Chromosome = pChromosome;
    }

    @Override
    public int compareTo(final SortedChromosomeCoverage o) {
       if(o == this)
           return 0;
        return getChromosome().toString().compareTo(o.getChromosome().toString());
    }

    public String getOutputFileName()
    {
        IAnalysisParameters ap = AnalysisParameters.getInstance();
        String name = ap.getJobName();
        return name + "_" + getChromosome() + ".coverage";
    }

    public PrintWriter getOutput() {
        try {
            if(m_Output == null)   {
                m_Output = new PrintWriter(new FileWriter( getOutputFileName()));
            }
            return m_Output;
        }
        catch (IOException e) {
             throw new RuntimeException(e);
         }
    }

    public int getBinWidth() {
        return m_BinWidth;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public int getCurrentPosition() {
        return m_CurrentPosition;
    }

    public void setCurrentPosition(final int pCurrentPosition) {
        m_CurrentPosition = pCurrentPosition;
    }

    public int getCurrentBin() {
        return m_CurrentBin;
    }

    public void setCurrentBin(final int pCurrentBin) {
        m_CurrentBin = pCurrentBin;
    }


    public int getBinEnd()
    {
        return getCurrentBin() + getBinWidth();
    }

    @Override
    public boolean visit(final IExtendedSamRecord record, final Object... added) {
        int pos = record.getAlignmentStart();
        if(pos < getCurrentPosition() || pos < getCurrentBin())
             throw new IllegalStateException("Input MUST be sorted");
         int oldPos = m_Coverage.getPosition();
        while(oldPos < pos)   {
            if(oldPos > getBinEnd())
                closeCurrentBin(pos);
           oldPos = m_Coverage.incrementPosition();
        }
        m_Coverage.addRecord(record);
        return true;
    }

    private void closeCurrentBin(int newPos) {
        final int count = m_Coverage.getTotalCoverage();
        if(count > 0)
            writeCurrentBin(m_Coverage.getAverageCoverage());
        while(getBinEnd() < newPos)
            setCurrentBin(getCurrentBin() + getBinWidth());
        m_Coverage.resetCount();
      }

    private void writeCurrentBin(double coverage) {
        final int cb = getCurrentBin();
        final int be = getBinEnd();
          final PrintWriter writer = getOutput();
        writer.println(getChromosome().toString() + "\t" + Integer.toString(cb) + "\t" + String.format("%6.1f",coverage).trim());
    }

    @Override
    public void initialize(final Object... added) {
         reset();
    }

    @Override
    public void finish(final Object... added) {
        if(m_Output!= null)  {
             m_Output.flush();
             m_Output.close();
             m_Output = null;
         }

    }

    @Override
    public boolean isSortedRequired() {
        return true;
    }

    @Override
    public void reset() {
                m_Output = null;
     }
}
