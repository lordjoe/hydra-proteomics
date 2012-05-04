package org.systemsbiology.coverage;

import org.systemsbiology.chromosome.*;

/**
 * org.systemsbiology.coverage.WindowCoverage
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class WindowCoverage
{
    public static final WindowCoverage[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = WindowCoverage.class;


    private final String m_AccessString;
    private final IChromosome m_Chromosome;
    private final int m_StartPosition;
    private final int m_EndPosition;

    public WindowCoverage(String pAccessString, IChromosome pChromosome, int pStartPosition, int pEndPosition) {
        m_AccessString = pAccessString;
        m_Chromosome = pChromosome;
        m_StartPosition = pStartPosition;
        m_EndPosition = pEndPosition;
    }

    

    public String getAccessString() {
        return m_AccessString;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public int getStartPosition() {
        return m_StartPosition;
    }

    public int getEndPosition() {
        return m_EndPosition;
    }
}
