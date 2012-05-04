package org.systemsbiology.sam;

import net.sf.samtools.*;
import org.systemsbiology.chromosome.*;
import org.systemsbiology.picard.*;

import java.util.*;

/**
 * org.systemsbiology.sam.SamUtilities
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public class SamUtilities
{
    public static final SamUtilities[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = SamUtilities.class;

    public static final Random RND = new Random();

    private SamUtilities() {
    } // do not even think of instantiating

    /***
     * obfuscate the sequence data
     * @param sr  non-null record
     */
    public static void obfuscate(IExtendedSamRecord sr)
    {
        if(sr instanceof ExtendedSamRecord) {
            ISamRecord rec = ((ExtendedSamRecord)sr).getRecord();
            obfuscate(rec);
        }
        else {
            throw new UnsupportedOperationException("We cannot support implementations with out data");  
        }
    }

    public static void obfuscate(ISamRecord sr)
    {
       String bts = sr.getBaseQualityString();
        if(bts.length() == 0)
            return;
        String newbts = pseudoACGT(bts.length());
        sr.setBaseQualityString(newbts);
    }

    
    public static String pseudoACGT(int n)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            switch(RND.nextInt(4)) {
                case 0:
                    sb.append("A");
                    break;
                case 1:
                    sb.append("C");
                    break;
                case 2:
                    sb.append("G");
                    break;
                case 3:
                    sb.append("T");
                    break;
            }

        }
        return sb.toString();
    }

    /**
     * change chromosome name foe coverage mapping
     * see
     *
     * @param chr
     * @return
     */
    public String alternateChromosomeName(IChromosome chr) {
        return chr.getAlternateName();
    }

    /**
     * return the distance between the start and and
     *
     * @param rec
     * @return
     */
    public static int getDistance(ISamRecord rec) {
        if (!isDistanceApplicable(rec))
            return Integer.MIN_VALUE;   // not applicable

        int ret = rec.getInferredInsertSize();
        String rn = rec.getReferenceName();
        String mn = rec.getMateReferenceName();
        if ("=".equals(mn) || rn.equals(mn)) {
            return ret;
        }
        if ("*".equals(rn)) {
            return DistanceStatisticsAccumulator.UNMAPPED;
        }
        return DistanceStatisticsAccumulator.OFF_CHROMOSOME;
    }


    /* return the distance between the start and and
    * @param rec
    * @return
    */

    public static boolean isDistanceApplicable(ISamRecord rec) {
        if ("*".equals(rec.getMateReferenceName())) {
            return false;
        }
        if (rec.isValid() != null)
            return false;

        return true;
    }


    /**
     * return the distance between the start and and
     *
     * @param rec
     * @return
     */
    public static int getDistance(IExtendedSamRecord rec) {
        if (!isDistanceApplicable(rec))
            return Integer.MIN_VALUE;   // not applicable

        int ret = rec.getInferredInsertSize();
        String rn = rec.getReferenceName();
        String mn = rec.getMateReferenceName();
        if ("=".equals(mn) || rn.equals(mn)) {
            return ret;
        }
        if ("*".equals(rn)) {
            return DistanceStatisticsAccumulator.UNMAPPED;
        }
        return DistanceStatisticsAccumulator.OFF_CHROMOSOME;
    }


    /* return the distance between the start and and
     * @param rec
     * @return
     */

    public static boolean isDistanceApplicable(IExtendedSamRecord rec) {
        if ("*".equals(rec.getMateReferenceName())) {
            return false;
        }
        if (AnalysisParameters.getInstance().isStrictParsing()) {
            if (rec.isValid() != null)
                return false;
        }
        else {
            IChromosome chr = rec.getChromosome();
            return chr != null && chr == rec.getMateChromosome();
        }
        return true;
    }


    /**
     * look up the value of a chromosome
     *
     * @param s string value
     * @return
     */
    public static IChromosome stringToChromosome(String s) {
        if (s.endsWith("_random"))
            return IChromosome.NULL_CHROMOSOME;
        try {
            IChromosome ret = DefaultChromosome.parseChromosome(s);
            if (ret == null)  {
                return IChromosome.NULL_CHROMOSOME;
            }

            return ret;
        }
        catch (Exception e) {
            return IChromosome.NULL_CHROMOSOME;
        }
    }


}
