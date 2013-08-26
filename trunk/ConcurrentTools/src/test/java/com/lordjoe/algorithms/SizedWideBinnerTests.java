package com.lordjoe.algorithms;

import org.junit.*;


public class SizedWideBinnerTests {

    public static final double MAX_VALUE = 1000;
    public static final double BIN_SIZE = 10;
    public static final int NUMBER_OVERLAPS = 1;


    @Test
    public void testBinning() throws Exception {
        //noinspection UnnecessaryLocalVariable,UnusedDeclaration,UnusedAssignment
        int nOverlaps = NUMBER_OVERLAPS;
        double overlapWidth = BIN_SIZE / 2;
        SizedWideBinner  binner = new SizedWideBinner(MAX_VALUE, BIN_SIZE,0, overlapWidth);

        double del = overlapWidth / 4;
        for (double bin = overlapWidth; bin < MAX_VALUE - overlapWidth; bin += del) {
             int binNum = binner.asBin(bin);
             double midBin = binner.fromBin(binNum);
            int[] bins = binner.asBins(bin);
            if(bin < midBin)   {
                int lowBin = binner.asBin(bin - overlapWidth);
                if(lowBin < binNum) {
                    Assert.assertEquals(2,bins.length);
                    Assert.assertEquals(lowBin,bins[0]);
                    Assert.assertEquals(binNum,bins[1]);
                } else {
                    Assert.assertEquals(1,bins.length);
                    Assert.assertEquals(binNum,bins[0]);
                }
            }
            else   {
                 int hiBin = binner.asBin(bin + overlapWidth);
                 if(hiBin > binNum) {
                     Assert.assertEquals(2,bins.length);
                     Assert.assertEquals(hiBin,bins[1]);
                     Assert.assertEquals(binNum,bins[0]);
                 } else {
                     Assert.assertEquals(1,bins.length);
                     Assert.assertEquals(binNum,bins[0]);
                 }
             }

        }

    }



}
