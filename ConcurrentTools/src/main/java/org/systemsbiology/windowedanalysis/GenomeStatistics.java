package org.systemsbiology.windowedanalysis;

import com.lordjoe.lib.xml.*;
import org.systemsbiology.common.*;
import org.systemsbiology.coverage.*;
import org.systemsbiology.sam.*;

/**
 * org.systemsbiology.windowedanalysis.GenomeStatistics
 * written by Steve Lewis
 * on Apr 27, 2010
 */
public class GenomeStatistics extends ImplementationBase
{
    public static final GenomeStatistics[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = GenomeStatistics.class;

    private final DistanceDistribution m_DistanceDistribution = new DistanceDistribution();
    private final NegPosCounter m_NegPosCounter = new NegPosCounter();
    private final ChromosomeCoverageSet m_ChromosomeCoverageSet =  new ChromosomeCoverageSet();;

    public GenomeStatistics( ) {
     }

    public DistanceDistribution getDistanceDistribution() {
        return m_DistanceDistribution;
    }

    public NegPosCounter getNegPosCounter() {
        return m_NegPosCounter;
    }

    public ChromosomeCoverageSet getChromosomeCoverageSet() {
        return m_ChromosomeCoverageSet;
    }
     @Override
    public Object handleTag(String TagName, NameValue[] attributes) {
        if("BamStatistics".equals(TagName))
              return this;
        if("ChromosomeCoverageSet".equals(TagName)) {
             return getChromosomeCoverageSet();

        }
         if("NegPosCounter".equals(TagName)) {
               return getNegPosCounter();

         }
          if("DistanceStatisticsAccumulator".equals(TagName)) {
             DistanceDistribution cst = getDistanceDistribution();
                return cst;

         }
          return super.handleTag(TagName, attributes);
    }

}
