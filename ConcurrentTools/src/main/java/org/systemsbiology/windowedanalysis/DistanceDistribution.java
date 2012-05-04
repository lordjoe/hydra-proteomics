package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import org.systemsbiology.common.*;
import org.systemsbiology.sam.*;

import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.DistanceDistribution
 * written by Steve Lewis
 * on Apr 27, 2010
 */
public class DistanceDistribution extends ImplementationBase
{
    public static final DistanceDistribution[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = DistanceDistribution.class;

    public static final double MINIMUM_PROBABILITY = 1.0 / (1000 * 1000);
    public static final int MINIMUM_COVERAGE = 5;

    private final transient Map<Long,RankDistance> m_Ranks = new HashMap<Long,RankDistance>();
    private int m_OffChromosome;
    private Long m_Median;
    private long[] m_LimitsP01;
    private long m_TotalValues;

    public DistanceDistribution() {
    }

    public long getTotalValues()
    {
        return m_TotalValues;
    }

    public void setTotalValues(long pTotalValues)
    {
        m_TotalValues = pTotalValues;
    }

    public int getOffChromosome() {
        return m_OffChromosome;
    }

    public void setOffChromosome(int pOffChromosome) {
        m_OffChromosome = pOffChromosome;
    }

    public long[] getLimitsP01()
    {
       if(m_LimitsP01 == null)  {
           m_LimitsP01 = new long[2];
          for(int i = 0; i < Short.MAX_VALUE; i++)   {
              RankDistance rd = m_Ranks.get((long)i);
              if(rd == null)
                  continue;
              if(m_LimitsP01[0] == 0 && rd.getRank() >= 0.01)  {
                  m_LimitsP01[0] = rd.getDistance();
               }
              if(m_LimitsP01[1] == 0 && rd.getRank() >= 0.99)  {
                  m_LimitsP01[1] = rd.getDistance();
                  break;
              }
          }
           for (int j = 0; j < m_LimitsP01.length; j++) {
                m_LimitsP01[j] -= getMedian();
            }

        }
 
        return m_LimitsP01;
    }

    public long getMedian()
    {
       if(m_Median == null)  {
          for(int i = 0; i < Short.MAX_VALUE; i++)   {
              RankDistance rd = m_Ranks.get((long)i);
              if(rd == null)
                  continue;
              if(rd.getRank() >= 0.5)  {
                  m_Median = rd.getDistance();
                  break;
              }
          }
       }
        if(m_Median != null)
            return m_Median;
        return 0;
    }

    /**
     * retiurn the probabliity of a set of diseances
     * in the range 0.05 to 0.95 p is set to 1
     * @param distance set of distances
     * @return
     */
    public double[] getTriTileProbabilities(short[] distance)
     {
        double ret = 1;
         int len = distance.length;
         if(len == 0)
            return null;
         double[] probs = new double[len];

         for (int i = 0; i < len; i++) {
             short dist = distance[i];
             if(dist == 0)  {
                 probs[i] = 1;
                 continue;
             }
             probs[i] =  getOneProbability(dist);
        }
//         Arrays.sort(probs);
         return CommonUtilities.getNTile(probs,3);
     }


    /**
     * retiurn the probabliity of a set of diseances
     * in the range 0.05 to 0.95 p is set to 1
     * @param distance set of distances
     * @return
     */
    public double getProbability(short... distance)
     {
        double ret = 1;
         int len = distance.length;
         double[] probs = new double[len];

         for (int i = 0; i < len; i++) {
             short dist = distance[i];
             if(dist == 0)  {
                 probs[i] = 1;
                 continue;
             }
             probs[i] =  getOneProbability(dist);
        }
         return CommonUtilities.getMedian(probs);
     }

    /**
     * probability that a random sample is farther from the median than this
     * @param distance value
     * @return  probability
     */
    public double getOneProbability(short distance)
     {
         RankDistance rd = m_Ranks.get(new Long(distance));
         if(rd == null)
             return MINIMUM_PROBABILITY;
         double rank = rd.getRank();
         return Math.min(2 * rank, 2 * (1.0 - rank));


     }

     @Override
    public Object handleTag(String TagName, NameValue[] attributes) {
          if("distance".equals(TagName)) {
              double rank = XMLUtil.handleRequiredNameValueDouble("rank",attributes);
              int distance = XMLUtil.handleRequiredNameValueInt("distance",attributes);
              RankDistance rd = new RankDistance(rank,distance);
              m_Ranks.put((long)distance,rd);
               return this;

         }
            return super.handleTag(TagName, attributes);
    }


}
