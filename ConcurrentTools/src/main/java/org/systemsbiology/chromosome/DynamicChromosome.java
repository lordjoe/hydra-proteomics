package org.systemsbiology.chromosome;

/**
 * org.systemsbiology.chromosome.DynamicChromosome
 * written by Steve Lewis
 * on Apr 16, 2010
 */
public class DynamicChromosome implements IChromosome
{
    public static final DynamicChromosome[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = DynamicChromosome.class;

    private final String m_Name;
    private final int m_Length;
    private final short m_Index;
    private String m_AlternateName;

    public DynamicChromosome(String pName, int pLength,short index) {
        m_Name = pName;
        m_Length = pLength;
        m_Index = index;
    }

    public int getLength() {
        return m_Length;
    }

    public String getName() {
        return m_Name;
    }

    public String getAlternateName() {
        return m_AlternateName;
    }

    public void setAlternateName(String pAlternateName) {
        m_AlternateName = pAlternateName;
    }

    public short getIndex() {
        return m_Index;
    }
}

