package org.systemsbiology.gatk;

import java.util.*;

/**
 * org.systemsbiology.gatk.RegionWithVariants
 * User: steven
 * Date: 6/12/12
 */
public class RegionWithVariants implements Comparable<RegionWithVariants> {
    public static final RegionWithVariants[] EMPTY_ARRAY = {};

     private final GeneRegion m_Region;
    private final List<GeneVariant>  m_Variants = new ArrayList<GeneVariant>();

    public RegionWithVariants(final ExperimentalSubject subject, final String id, final GeneRegion region) {
        this(  subject,   id,  region,ExperimentalCondition.unknown);
    }

    public RegionWithVariants(final ExperimentalSubject subject, final String id, final GeneRegion region,ExperimentalCondition ec) {
         m_Region = region;
     }


    public GeneRegion getRegion() {
        return m_Region;
    }


    public void addVariant(GeneVariant added) {
        m_Variants.add(added);
    }
    public GeneVariant[] getVariants( ) {
       return m_Variants.toArray(GeneVariant.EMPTY_ARRAY);
    }
     @Override
    public int compareTo(RegionWithVariants o) {
        if(this == o)
            return 0;
          int ret = getRegion().compareTo(o.getRegion());
             return ret;
    }
}
