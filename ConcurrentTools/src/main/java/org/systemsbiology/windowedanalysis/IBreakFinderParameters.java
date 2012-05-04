package org.systemsbiology.windowedanalysis;

/**
 * org.systemsbiology.windowedanalysis.IBreakFinderParameters
 * written by Steve Lewis
 * on Apr 29, 2010
 */
public interface IBreakFinderParameters
{
    public static final IBreakFinderParameters[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IBreakFinderParameters.class;

    public static final double LOW_PROBABILITY_ = 1.0 / (1 * 1000);
    public static final int MINIMUM_COVERAGE_ = 6;
    public static final int NUMBER_GOOD_TOEXIT_REGION_ = 8;
    public static final int MINIMUM_TEST_COVERAGE_ = 12;
    public static final int MINIMUM_WIDTH = 30;
    public static final int MINIMUM_READS = 25;
    // at least this fraction of the reads need to support
    public static final double MINIMUM_FRACTION_SUPPORTING = 0.25;


    public int getMinimumWidth();

    public int getMinimumReads();


    public double getMinimumFractionSupporting();


    public int getMinimumTestCoverage();


    public int getMinimumCoverage();

    public int getNumberGoodToExitRegion();

    public double getMaxProbabilityToStartRegion();


}