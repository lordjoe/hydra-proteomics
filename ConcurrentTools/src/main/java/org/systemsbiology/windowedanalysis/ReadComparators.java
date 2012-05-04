package org.systemsbiology.windowedanalysis;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.common.*;
import org.systemsbiology.location.*;
import org.systemsbiology.picard.*;
import org.systemsbiology.sam.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.windowedanalysis.ReadComparators
 * written by Steve Lewis
 * on Apr 28, 2010
 */
public class ReadComparators
{
    public static final ReadComparators[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ReadComparators.class;

    public static final Comparator<IExtendedPairedSamRecord> LOCATION_COMPATATOR = new LocationComparator();
    public static final Comparator<IExtendedPairedSamRecord> DISTANCE_COMPATATOR = new DistanceComparator();
    public static final Comparator<IGeneLocationAndDirection> GENE_LOCATION_COMPATATOR = new GeneLocationComparator();


    private static class GeneLocationComparator implements  Comparator<IGeneLocationAndDirection>,Serializable {
        public int compare(IGeneLocationAndDirection r1, IGeneLocationAndDirection r2) {
            if(r1 == r2)
                return 0;
            IChromosome c1 = r1.getChromosome();
            IChromosome c2 = r2.getChromosome();
            if(c1 != c2)
                return c1.toString().compareTo(c2.toString());
            return CommonUtilities.compare(r1.getLocation(),r2.getLocation());


        }
    }

    private static class LocationComparator implements  Comparator<IExtendedPairedSamRecord>,Serializable {
        public int compare(IExtendedPairedSamRecord r1, IExtendedPairedSamRecord r2) {
            if(r1 == r2)
                return 0;
            IGeneLocationAndDirection l1 = r1.getStartLocation();
            IGeneLocationAndDirection l2 = r2.getStartLocation();
            return  GENE_LOCATION_COMPATATOR.compare(l1,l2);

        }
    }



    private static class DistanceComparator implements  Comparator<IExtendedPairedSamRecord>,Serializable {
        public int compare(IExtendedPairedSamRecord r1, IExtendedPairedSamRecord r2) {
            int ret = CommonUtilities.compare(r1.getMateDistance(), r2.getMateDistance());
            if(ret == 0)
                return LOCATION_COMPATATOR.compare(r1,r2);
            return ret;
        }
    }

 }
