package org.systemsbiology.xtandem.fdr;

import org.junit.*;

/**
 * org.systemsbiology.xtandem.fdr.FDRUtilitiesTests
 *
 * @author Steve Lewis
 * @date 09/05/13
 */
public class FDRUtilitiesTests {
    public static FDRUtilitiesTests[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = FDRUtilitiesTests.class;

    /**
     * make sure the static asbin function has reasonable limits and enough resolution - > 1 part in 20
     */
    @Test
    public void testAsBin() {
        // check the limits
        Assert.assertEquals(0,FDRUtilities.asBin(0));
        Assert.assertEquals(0,FDRUtilities.asBin(FDRUtilities.MINIMUM_SCORE));
        Assert.assertEquals(FDRUtilities.NUMBER_BINS - 1,FDRUtilities.asBin(FDRUtilities.MAXIMUM_SCORE));
        Assert.assertEquals(FDRUtilities.NUMBER_BINS - 1,FDRUtilities.asBin(Double.MAX_VALUE));

        double factor = 1.05;
        int start = -1;
        // make sure we have enough distinct values
        for (double v = FDRUtilities.MINIMUM_SCORE; v < FDRUtilities.MAXIMUM_SCORE; v *= factor) {
            int test = FDRUtilities.asBin( v); //    get a new value
            Assert.assertNotSame(test,start);
            start = test;

        }

     }
}
