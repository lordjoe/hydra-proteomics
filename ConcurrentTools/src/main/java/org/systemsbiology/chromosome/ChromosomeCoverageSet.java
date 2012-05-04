package org.systemsbiology.chromosome;

import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.chromosome.ChromosomeCoverageSet
 * written by Steve Lewis
 * on Apr 9, 2010
 */
public class ChromosomeCoverageSet implements ISamRecordVisitor
{
    public static final ChromosomeCoverageSet[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ChromosomeCoverageSet.class;

    private final int m_BinWidth;
    private final Map<IChromosome,ChromosomeCoverage> m_Coverages =
            new HashMap<IChromosome,ChromosomeCoverage>();

    public ChromosomeCoverageSet(int binwidth) {
        m_BinWidth = binwidth;
        IChromosome[] chromosomes = AnalysisParameters.getInstance().getDefaultChromosomes();
         for (int i = 0; i < chromosomes.length; i++) {
             IChromosome chromosome = chromosomes[i];
             m_Coverages.put(chromosome,new ChromosomeCoverage(chromosome,binwidth));
         }
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

    public ChromosomeCoverage[] getCoverages()
    {
        IChromosome[] chromosomes = AnalysisParameters.getInstance().getDefaultChromosomes();
        ChromosomeCoverage[] ret = new ChromosomeCoverage[chromosomes.length];
        for (int i = 0; i < chromosomes.length; i++) {
            IChromosome chromosome = chromosomes[i];
            ret[i] = m_Coverages.get(chromosome);
        }
        return ret;
    }

    public ChromosomeCoverage getCoverage(IChromosome chr)
    {
        return m_Coverages.get(chr);
    }

    /**
     * implemt the visitor pattern for a SAMRecord
     *
     * @param record
     */
    public boolean visit(IExtendedSamRecord record) {
        ChromosomeCoverage cvg = getCoverage(record.getChromosome());
        if(cvg == null)
            return false;
        return cvg.visit(record);
    }

    /**
     * reset to base state
     */
    public void reset() {
        ChromosomeCoverage[] coverages = getCoverages();
        for (int i = 0; i < coverages.length; i++) {
            ChromosomeCoverage coverage = coverages[i];
            coverage.reset();
        }
    }

    /**
     * reset to base state
     */
    public void finish() {
        try {
            String name = AnalysisParameters.getInstance().getJobName();
            PrintWriter out = new PrintWriter(new FileWriter(name + ".coverage"));
            ChromosomeCoverage[] coverages = getCoverages();
            for (int i = 0; i < coverages.length; i++) {
                ChromosomeCoverage coverage = coverages[i];
                coverage.writeData(out);
            }

            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
