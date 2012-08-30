package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.CoverageStatistics
 * User: steven
 * Date: 7/5/12
 */
public class CoverageStatistics {
    public static final CoverageStatistics[] EMPTY_ARRAY = {};

    private final ProteinFragmentationDescription m_Fragments;
    private final int[] m_CoverageStatistics;
    private final PartitionStatistics[] m_DivisionStatistics;

    public CoverageStatistics(ProteinFragmentationDescription fragments,FoundPeptide[] peptides) {
        m_Fragments = fragments;
        m_Fragments.buildCoverage(peptides);
        short[] allCoverage = fragments.getAllCoverage();
         m_CoverageStatistics = Util.computeStatistics(allCoverage);
        m_DivisionStatistics = buildPartitionStatistics(allCoverage);
     }

    public CoverageStatistics(ProteinFragmentationDescription fragments) {
        m_Fragments = fragments;
        short[] allCoverage = fragments.getAllCoverage();
        m_CoverageStatistics = Util.computeStatistics(allCoverage);
        m_DivisionStatistics = buildPartitionStatistics(allCoverage);
    }

    public ProteinFragmentationDescription getFragments() {
        return m_Fragments;
    }

    public int[] getCoverageStatistics() {
        return m_CoverageStatistics;
    }

    public PartitionStatistics getPartitionStatistics(int numberPartitions) {
        int index = numberPartitions - 2;
        if (index < 0 || index >= m_DivisionStatistics.length)
            return null;
        return m_DivisionStatistics[index];
    }


    public static PartitionStatistics[] buildPartitionStatistics(short[] coverage) {
        List<PartitionStatistics> holder = new ArrayList<PartitionStatistics>();
        for (int i = 2; i < 5; i++) {
            holder.add(new PartitionStatistics(i, coverage));

        }

        PartitionStatistics[] ret = new PartitionStatistics[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static class PartitionStatistics {
        private final int m_NumberDivisions;
        private final int[] m_DivisionCount;
        private final double m_LowestOverHighest;

        public PartitionStatistics(int numberDivisions, short[] coverage) {
            m_NumberDivisions = numberDivisions;
            m_DivisionCount = new int[numberDivisions];
            m_LowestOverHighest = buildPartitionStatistics(numberDivisions, coverage);
        }


        public double buildPartitionStatistics(int numberDivisions, short[] coverage) {
            int cl = coverage.length;
            int divisionSize = cl / numberDivisions;
            int[] divisionStart = new int[numberDivisions];
            for (int i = 0; i < divisionStart.length; i++) {
                divisionStart[i] = divisionSize * i;

            }
            int min = Integer.MAX_VALUE;
            int max = 0;
            for (int i = 0; i < divisionSize; i++) {
                for (int j = 0; j < divisionStart.length; j++) {
                    int i1 = i + divisionStart[j];
                    m_DivisionCount[j] += coverage[i1];
               }
            }

            for (int j = 0; j < divisionStart.length; j++) {
                max = Math.max(max,m_DivisionCount[j]);
                min = Math.min(min, m_DivisionCount[j]);
               }

            if (max == 0)
                return 0;
            return (double) min / (double) max;
        }

        public int getNumberDivisions() {
            return m_NumberDivisions;
        }

        public int[] getDivisionCount() {
            return m_DivisionCount;
        }

        public double getLowestOverHighest() {
            return m_LowestOverHighest;
        }

        @Override
        public String toString() {
            return "" +
                     + m_NumberDivisions +
                    " " + Arrays.toString(m_DivisionCount) +
                    " " + String.format("%6.3f",m_LowestOverHighest);
        }
    }
}
