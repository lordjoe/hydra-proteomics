package org.systemsbiology.common;

import org.systemsbiology.sam.*;

import java.util.*;

/**
 * org.systemsbiology.common.DistributionCounts
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public class DistributionCounts implements Resetable
{
    public static final DistributionCounts[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = DistributionCounts.class;

    private boolean m_Dirty;
    private boolean m_DirtyRanks;
    private final List<Integer> m_OrderedKeys = new ArrayList<Integer>();
    private final Map<Integer,Integer> m_Counts = new HashMap<Integer,Integer>();
    private final Map<Integer,RankDistance> m_Ranks = new HashMap<Integer,RankDistance>();

    public boolean isDirty() {
        return m_Dirty;
    }

    public void setDirty(boolean pDirty) {
        m_Dirty = pDirty;
    }

    public boolean isDirtyRanks() {
        return m_DirtyRanks;
    }

    public void setDirtyRanks(boolean pDirtyRanks) {
        m_DirtyRanks = pDirtyRanks;
    }

    /**
     * reset to base state
     */
    public void reset() {
        m_OrderedKeys.clear();
        m_Counts.clear();
        m_Ranks.clear();
        setDirty(false);
        setDirtyRanks(false);
    }

    public void addCount(int location,int count)
    {
        if(m_OrderedKeys.contains(location))
        {
           m_Counts.put(location,count + m_Counts.get(location)) ;
        }
        else {
           m_OrderedKeys.add(location);
           m_Counts.put(location,count);
            setDirty(true);
        }
        setDirtyRanks(true);

    }

    public DistanceCount[] getCounts()
     {
         guaranteeNotDirty();
         List<DistanceCount> holder = new ArrayList<DistanceCount>();
         for(int key : m_OrderedKeys) {
             holder.add(new DistanceCount(key,m_Counts.get(key)));
         }

         DistanceCount[] ret = holder.toArray(DistanceCount.EMPTY_ARRAY);
         Arrays.sort(ret);
         return ret;
     }

    private void guaranteeNotDirty() {
        synchronized (m_OrderedKeys) {
          if(isDirty())    {
             Collections.sort(m_OrderedKeys);
             setDirty(false);
         }
        }
    }

    /**
      *
      * @param distance
      * @return
      */
     public double rankDistance(int distance)
     {
         guaranteeRanks();
         RankDistance rd = m_Ranks.get(distance);
         if(rd == null)
             throw new IllegalArgumentException("Distance not ranked");
         return rd.getRank();
     }

     protected  void guaranteeRanks()
     {
         guaranteeNotDirty();
           synchronized (m_Ranks) {
             if(isDirtyRanks()) {
                 long sum = 0;
                 m_Ranks.clear();
                 for(int key : m_OrderedKeys) {
                     Integer distance = m_Counts.get(key);
                     sum += distance;
                     m_Ranks.put(key,new RankDistance(key,sum));
                 }

             }

         }
     }

     public RankDistance[] getRanks()
     {
         DistanceCount[] dcs = getCounts();
         double total = 0;
         for (int i = 0; i < dcs.length; i++) {
             DistanceCount dc = dcs[i];
             if(dc.getDistance() < 0)
                 continue;
             total += dc.getCount();
         }
         long rank = 0;
          List<RankDistance> holder = new ArrayList<RankDistance>();
         for (int i = 0; i < dcs.length; i++) {
              DistanceCount dc = dcs[i];
             if(dc.getDistance() < 0)
                 continue;
              rank += dc.getCount();
             holder.add(new RankDistance( rank / total,dc.getDistance()));
          }
           RankDistance[] ret = holder.toArray(RankDistance.EMPTY_ARRAY);
         Arrays.sort(ret);
         return ret;
     }




}
