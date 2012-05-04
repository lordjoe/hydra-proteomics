package org.systemsbiology.common;

/**
 * org.systemsbiology.common.IntegerHistogramBin
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class IntegerHistogramBin implements Comparable<IntegerHistogramBin>
{
    public static final IntegerHistogramBin[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IntegerHistogramBin.class;


    private final int m_Start;
    private final int m_End;
    private final int m_Count;

    public IntegerHistogramBin(int pStart, int pEnd, int pCount) {
        m_Start = pStart;
        m_End = pEnd;
        m_Count = pCount;
    }

    public int getStart() {
        return m_Start;
    }

    public int getEnd() {
        return m_End;
    }

    public int getCount() {
        return m_Count;
    }

    @Override
    public String toString() {
        return Integer.toString(getStart()) + "\t" +
                getEnd() + "\t" + getCount();
    }

    public int compareTo(IntegerHistogramBin o) {
        if(getStart() != o.getStart())
            return getStart() <  o.getStart() ? -1 : 1;
        if(getEnd() != o.getEnd())
            return getEnd() <  o.getEnd() ? -1 : 1;
        if(getCount() != o.getCount())
            return getCount() <  o.getCount() ? -1 : 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerHistogramBin that = (IntegerHistogramBin) o;

        if (m_Count != that.m_Count) return false;
        if (m_End != that.m_End) return false;
        if (m_Start != that.m_Start) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_Start;
        result = 31 * result + m_End;
        result = 31 * result + m_Count;
        return result;
    }
}
