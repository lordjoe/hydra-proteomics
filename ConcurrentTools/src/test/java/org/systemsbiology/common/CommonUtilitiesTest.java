package org.systemsbiology.common;

import org.junit.*;

/**
 * org.systemsbiology.common.CommonUtilitiesTest
 * User: steven
 * Date: Jun 22, 2010
 */
public class CommonUtilitiesTest {
    public static final CommonUtilitiesTest[] EMPTY_ARRAY = {};

    public static final long ONE_K = 1024L;
    public static final long ONE_MEG = ONE_K * ONE_K;
    public static final long ONE_GIG = ONE_MEG * ONE_K;
    public static final long ONE_T = ONE_GIG * ONE_K;
    public static final long ONE_P = ONE_T * ONE_K;
    public static final long ONE_A = ONE_P * ONE_K;


    @Test
    public void testShiftBy() {
        Assert.assertEquals(ONE_K,CommonUtilities.shiftBy(1L,10));
        Assert.assertEquals(ONE_MEG ,CommonUtilities.shiftBy(1L,20));
        Assert.assertEquals(ONE_GIG ,CommonUtilities.shiftBy(1L,30));
        Assert.assertEquals(ONE_T ,CommonUtilities.shiftBy(1L,40));
        Assert.assertEquals(ONE_P ,CommonUtilities.shiftBy(1L,50));
        Assert.assertEquals(ONE_A ,CommonUtilities.shiftBy(1L,60));

        Assert.assertEquals(31 * ONE_K,CommonUtilities.shiftBy(31L,10));
        Assert.assertEquals(31 *ONE_MEG ,CommonUtilities.shiftBy(31L,20));
        Assert.assertEquals(31 *ONE_GIG ,CommonUtilities.shiftBy(31L,30));
        Assert.assertEquals(31 *ONE_T ,CommonUtilities.shiftBy(31L,40));
        Assert.assertEquals(31 *ONE_P ,CommonUtilities.shiftBy(31L,50));
        Assert.assertEquals(31 *ONE_A ,CommonUtilities.shiftBy(31L,60));

    }


}
