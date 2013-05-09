package org.systemsbiology.xtandem.fdr;

/**
 * org.systemsbiology.xtandem.fdr.FDRUtilities
 *
 * @author Steve Lewis
 * @date 09/05/13
 */
public class FDRUtilities {
    public static FDRUtilities[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = FDRUtilities.class;

    public static final int NUMBER_BINS = 3200;

    /**
     * we can hard code a reasonable range of values
     */
    public static final double MINIMUM_SCORE = 1.0e-008;
    public static final double MAXIMUM_SCORE = 1.0e008;
    private static final double MINIMUM_SCORE_LOG = Math.log10(MINIMUM_SCORE);
    private static final double MAXIMUM_SCORE_LOG = Math.log10(MAXIMUM_SCORE);
    private static final double DEL_LOG = (MAXIMUM_SCORE_LOG - MINIMUM_SCORE_LOG) / NUMBER_BINS;


    /**
      * convert a score into an index NUMBER_BINS
      * @param score
      * @return   the index >= 0 <  NUMBER_BINS
      */
    public static int asBin(double score)
    {
        if(score <= MINIMUM_SCORE)
            return 0;
        if(score >= MAXIMUM_SCORE)
            return NUMBER_BINS - 1;
        double scoreLog =  Math.log10(score);
        scoreLog -=  MINIMUM_SCORE_LOG;
        int ret = (int)(scoreLog / DEL_LOG);
        return ret;
    }


    /**
     * return a discovery holder for default algorithm and direction
     *
     * @return !null   IDiscoveryDataHolder
     */
    public static IDiscoveryDataHolder getDiscoveryDataHolder() {
        return getDiscoveryDataHolder(null);
    }

    /**
     * return a discovery holder for default algorithm and direction
     *
     * @param algorithmName POSSIBLY NULL ALGORITHM NAME
     * @param direction     true is ascending is good
     * @return
     */
    public static IDiscoveryDataHolder getDiscoveryDataHolder(String algorithmName) {
        return getDiscoveryDataHolder(algorithmName, true);

    }

    /**
     * return a discovery holder for default algorithm and direction
     *
     * @param algorithmName POSSIBLY NULL ALGORITHM NAME
     * @param direction     true is ascending is good
     * @return
     */
    public static IDiscoveryDataHolder getDiscoveryDataHolder(String algorithmName, boolean direction) {
        return new StupidDiscoveryDataHolder( );
    }
}
