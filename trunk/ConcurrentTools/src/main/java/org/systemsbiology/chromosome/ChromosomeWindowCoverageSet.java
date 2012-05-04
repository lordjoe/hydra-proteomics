package org.systemsbiology.chromosome;

import org.systemsbiology.sam.*;

import java.util.*;

/**
 * org.systemsbiology.chromosome.ChromosomeWindowCoverageSet
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class ChromosomeWindowCoverageSet  
{
    public static final ChromosomeWindowCoverageSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeWindowCoverageSet.class;


    private final IChromosome m_Chromosome;
    private final Map<Long,WindowCoverage> m_Coverages =
            new HashMap<Long,WindowCoverage>();

    public ChromosomeWindowCoverageSet(IChromosome chr) {
        m_Chromosome = chr;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public WindowCoverage[] getCoverageOfPosition()
    {
        throw new UnsupportedOperationException("Fix This"); // todo
    }

    public WindowCoverage[] getCoverages()
    {
        WindowCoverage[]  ret = m_Coverages.values().toArray(WindowCoverage.EMPTY_ARRAY);
        Arrays.sort(ret);

        return ret;
    }

    public WindowCoverage getCoverage(Long chr)
    {
        return m_Coverages.get(chr);
    }




}