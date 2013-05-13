package com.lordjoe.utilities;

import com.lordjoe.algorithms.*;
import org.junit.*;

/**
 * com.lordjoe.utilities.BinnerTests
 *
 * @author Steve Lewis
 * @date 11/05/13
 */
public class BinnerTests {
    public static BinnerTests[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = BinnerTests.class;

    public static final double MAX_VALUE = 1000;
    public static final double BIN_SIZE = 10;

    public static IBinner buildLinearBinner(double max, double binsize) {
        return new LinearBinner(max, binsize);
    }

    @Test
    public void testBinning() throws Exception {
        IBinner binner = new LinearBinner(MAX_VALUE, BIN_SIZE);
        // normal inrange values
        for (int i = 0; i < MAX_VALUE; i++) {
            Assert.assertEquals((int) (i / BIN_SIZE), binner.asBin(i));
        }
        // default out of range is to bin
        final int expected = binner.getMaxBin() - 1;
        Assert.assertEquals(expected, binner.asBin(MAX_VALUE + BIN_SIZE));
        // default out of range is to bin
        Assert.assertEquals(0, binner.asBin(-BIN_SIZE));
    }


}
