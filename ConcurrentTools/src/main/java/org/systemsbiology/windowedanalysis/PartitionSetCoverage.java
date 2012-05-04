package org.systemsbiology.windowedanalysis;

import org.systemsbiology.common.*;
import org.systemsbiology.location.*;
import org.systemsbiology.partitioning.*;
import org.systemsbiology.sam.*;

import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.PartitionSetCoverage
 * Fine Grained coverage map
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public class PartitionSetCoverage implements IPartitionedSetAnalysis
{
    public static final PartitionSetCoverage[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = PartitionSetCoverage.class;

    private short[] m_Coverage;
    private short[][] m_Distances;
    private Set<IExtendedPairedSamRecord> m_ActiveRecords = new HashSet();

    /**
     * perform some analysis on a data set
     *
     * @param data
     * @param more
     */
    public void analyze(IPartitionedSamSet data, Object... more) {

        ISubChromosomeInterval ivl = data.getActiveInterval();
        m_ActiveRecords.clear();
        int overlap = data.getPartitionSet().getOverlap();
        int start = ivl.getStart();
        int end = ivl.getEnd();
        int loadStart = Math.max(0, start - overlap);
        ISubChromosomeInterval loadInterval = new GenomeInterval(ivl.getChromosome(),
                loadStart,end + overlap);
        m_Coverage = new short[(int) (end - start)];
        m_Distances = new short[(int) (end - start)][];
        long location = -1;
        int nRecords = 0;
        IExtendedPairedSamRecord[] samRecords = data.getAllRecords();
        int numberRecords =  samRecords.length;
        
        for (IExtendedPairedSamRecord rec : samRecords) {
                nRecords++;
            ISubChromosomeInterval civ = rec.getInterval();
            if (!civ.overlaps(loadInterval))
                continue;
            long recloc = rec.getAlignmentStart();
            // fill in missed locations
            long lastCovered = Math.max(loadStart, location);
            while (lastCovered < recloc)
                getCoverage(start, lastCovered++);

            m_ActiveRecords.add(rec);
            if (recloc < start)
                continue; // too early
            if (recloc > end)
                break; // done
            if (location == recloc)
                continue; // staiiladdding records
            location = getCoverage(start, recloc);


        }
        m_ActiveRecords.clear();
    //    for (int i = 0; i < m_Coverage.length; i++) {
   //         System.out.println(String.format("%06d %03d",i + start,m_Coverage[i]));
   //     }

    }

    public short[] getDistancesAtPosition(int index)
    {
        if(index < 0 || index >= m_Distances.length)
            return new short[0];
        else
            return m_Distances[index];
    }


    public short getCoverageAtPosition(int index)
    {
        if(index < 0 || index >= m_Coverage.length)
            return 0;
        else
            return m_Coverage[index];
    }

     protected long getCoverage(long start, long pRecloc) {
        Set<IExtendedPairedSamRecord> removed = new HashSet<IExtendedPairedSamRecord>();
        for (IExtendedPairedSamRecord rec : m_ActiveRecords) {
            int ae = rec.getAlignmentEnd();
            if (ae < pRecloc)
                removed.add(rec);
        }
        m_ActiveRecords.removeAll(removed);
        int index = (int) (pRecloc - start);
        // just preloading
        if(index < 0 || index >= m_Coverage.length)
            return pRecloc;

        if (m_Coverage[index] != 0) {
             return pRecloc;
          //   throw new IllegalStateException("Multiple coverage set");
         }
        if (index > 1000 && m_ActiveRecords.size() > 50 && m_Coverage[index - 1] == 0) {
            CommonUtilities.breakHere();
         }
        int size = m_ActiveRecords.size();
        m_Coverage[index] = (short)Math.min(size,Short.MAX_VALUE);
        m_Distances[index] = new short[size];
        int index2 = 0;
        for(IExtendedPairedSamRecord rec  : m_ActiveRecords) {
            long l = rec.getMateDistance();
            m_Distances[index][index2++] = (short)Math.min(Short.MAX_VALUE,Math.max(0, l));
        }

        return pRecloc;
    }

    /**
     * take action at the end of a process
     *
     * @param added  other data
     */
    public void finish(Object... added) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}