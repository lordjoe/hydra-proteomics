package org.systemsbiology.common;

import java.util.*;

/**
 * org.systemsbiology.common.IntegerHistogram
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public class IntegerHistogram implements Resetable
{
    public static final IntegerHistogram[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IntegerHistogram.class;

    private final List<Integer> m_Bins = new ArrayList<Integer>();
    private final long m_Width;

    public IntegerHistogram(long pWidth) {
        m_Width = pWidth;
    }

    public int getNumberBins()
    {
        return m_Bins.size();
    }

    public void setNumberBins(int number)
    {
        while(m_Bins.size() > number) {
            m_Bins.remove(m_Bins.size() - 1);
        }
        guaranteeNumberBins(number);

    }

    private void guaranteeNumberBins(int number) {
        while(m_Bins.size() <= number)
            m_Bins.add(0);
    }

    public long getWidth() {
        return m_Width;
    }

    public int getBinValue(int index)
    {
        return m_Bins.get(index);
    }

    public void addItemAt(long value)
    {
        int index = (int)(value / m_Width);
        guaranteeNumberBins(index);
        int old = m_Bins.get(index);
        m_Bins.set(index,old + 1);

    }

    public Integer[] values()
    {
        return m_Bins.toArray(new Integer[0]);
    }

    /**
     * reset to base state
     */
    public void reset() {
        m_Bins.clear();
    }

    public IntegerHistogramBin[] getAllBins()
    {
        List<IntegerHistogramBin> holder = new ArrayList<IntegerHistogramBin>();
        for(int index = 0;index < m_Bins.size();index++) {
            int count = m_Bins.get(index);
            holder.add(new IntegerHistogramBin((int)(index * getWidth()),(int)((index + 1) * getWidth() - 1),count));
        }
        return holder.toArray(IntegerHistogramBin.EMPTY_ARRAY);
    }

    public IntegerHistogramBin[] getAllNonZeroBins()
    {
        List<IntegerHistogramBin> holder = new ArrayList<IntegerHistogramBin>();
        for(int index = 0;index < m_Bins.size();index++) {
            int count = m_Bins.get(index);
            if(count == 0)
                continue;
            holder.add(new IntegerHistogramBin((int)(index * getWidth()),(int)((index + 1) * getWidth() - 1),count));
        }
        return holder.toArray(IntegerHistogramBin.EMPTY_ARRAY);
    }
}
