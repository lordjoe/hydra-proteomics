package org.systemsbiology.gatk;

import java.util.*;

/**
 * org.systemsbiology.gatk.MapToGeneRegion
 * User: steven
 * Date: 6/12/12
 */
public class MapToGeneRegion {
    public static final MapToGeneRegion[] EMPTY_ARRAY = {};

    private final List<GeneRegion> m_Regions = new ArrayList<GeneRegion>();
    private final Map<GeneRegion,RegionWithVariants> m_RegionsToVariants = new HashMap<GeneRegion,RegionWithVariants>();

    public MapToGeneRegion(GeneRegion[] regions) {
        for (int i = 0; i < regions.length; i++) {
            GeneRegion region = regions[i];
            m_Regions.add(region);
        }
        Collections.sort(m_Regions);
    }


    public GeneRegion[] getRegions( ) {
       return m_Regions.toArray(GeneRegion.EMPTY_ARRAY);
    }

    public GeneRegion  getRegionContaining(GeneLocation loc ) {
       for(GeneRegion region : m_Regions) {
           if(region.isWithin(loc))
               return region;
       }
       return null;
    }


    public RegionWithVariants[] getRegionsWithVariants( ) {
        Collection<RegionWithVariants> values = m_RegionsToVariants.values();
        synchronized (values)   {
            RegionWithVariants[] ret = values.toArray(RegionWithVariants.EMPTY_ARRAY);
            Arrays.sort(ret);
            return ret;
       }
    }

    public RegionWithVariants getRegionsWithVariants(GeneRegion region ) {
       synchronized (m_RegionsToVariants)   {
           RegionWithVariants ret = m_RegionsToVariants.get(region);
           if(ret == null) {
               if(true)
                   throw new UnsupportedOperationException("Fix This"); // handle subject abd id
               ret = new RegionWithVariants(null,null,region);
               m_RegionsToVariants.put(region, ret);
           }
           return ret;
       }
    }

    public void addVariant(GeneVariant variant) {
        GeneRegion regionContaining =  getRegionContaining(variant.getLocation());
        if(regionContaining == null)
            return; // todo throw exception???
        RegionWithVariants regionsWithVariants =  getRegionsWithVariants(regionContaining);
        regionsWithVariants.addVariant(variant);

    }

}
