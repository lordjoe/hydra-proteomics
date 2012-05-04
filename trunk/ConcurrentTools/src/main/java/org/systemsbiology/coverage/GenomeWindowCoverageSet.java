package org.systemsbiology.coverage;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.sam.*;
import org.systemsbiology.tcga.*;

import java.util.*;

/**
 * org.systemsbiology.coverage.ChromosomeWindowCoverageSet
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class GenomeWindowCoverageSet  
{
    public static final GenomeWindowCoverageSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = GenomeWindowCoverageSet.class;


    private final TCGASample m_GenomeName;
    private final Map<IChromosome,ChromosomeWindowCoverageSet> m_Coverages =
            new HashMap<IChromosome,ChromosomeWindowCoverageSet>();

    public GenomeWindowCoverageSet(TCGASample sample) {
        m_GenomeName = sample;
         IChromosome[] chromosomes = AnalysisParameters.getInstance().getDefaultChromosomes();;
         for (int i = 0; i < chromosomes.length; i++) {
             IChromosome chromosome = chromosomes[i];
             m_Coverages.put(chromosome,new ChromosomeWindowCoverageSet(chromosome));
         }
    }

    public ChromosomeWindowCoverageSet[] getCoverages()
    {
        IChromosome[] chromosomes = DefaultChromosome.getDefaultChromosomeSet();
        ChromosomeWindowCoverageSet[] ret = new ChromosomeWindowCoverageSet[chromosomes.length];
        for (int i = 0; i < chromosomes.length; i++) {
            IChromosome chromosome = chromosomes[i];
            ret[i] = m_Coverages.get(chromosome);
        }
        return ret;
    }

    public ChromosomeWindowCoverageSet getCoverage(IChromosome chr)
    {
        return m_Coverages.get(chr);
    }


}