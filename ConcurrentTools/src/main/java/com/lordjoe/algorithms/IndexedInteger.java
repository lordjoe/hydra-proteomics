package com.lordjoe.algorithms;

/**
 * track value and the original index - comparator sorts by value
 */
public class IndexedInteger implements Comparable<IndexedInteger> {
    private final int m_Value;
    private final int m_Index;

    public IndexedInteger(final int pValue, final int pIndex) {
        m_Value = pValue;
        m_Index = pIndex;
    }

    public int getValue() {
        return m_Value;
    }

    public int getIndex() {
        return m_Index;
    }


    @Override
    public int compareTo(final IndexedInteger o) {
        if (o == this || o.equals(this))
            return 0;
        int v1 = getValue();
        int v2 = o.getValue();
        if (v1 < v2)
            return -1;
        if (v1 > v2)
            return 1;

        return getIndex() < o.getIndex() ? -1 : 1;
    }
}
