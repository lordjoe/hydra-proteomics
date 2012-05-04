package org.systemsbiology.fasta;

import org.junit.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.fasta.PackedFastaTest
 * User: steven
 * Date: Jun 21, 2010
 */
public class PackedFastaTest {
    public static final PackedFastaTest[] EMPTY_ARRAY = {};


    public static final Random RND = new Random();

    public static final File ORIGINAL = new File("E:/Genome/hg18/chr3.fa");
    public static final File PACKED = new File("E:/PackedGenome/hg18/chr3.fabytes");
    public static final int NUMBER_TESTS = 10;

    @Test
    public void testNothing() {

    }

    public void testPackedFasta() {
        for (int i = 0; i < NUMBER_TESTS; i++) {
           runOneTest();
        }


    }

    private void runOneTest() {
        int len = (int) PACKED.length();
        int start = RND.nextInt(len - 10000);
        int end = start + 100 + RND.nextInt(len - start - 100);
        String original = FastaPacker.getFastaData(ORIGINAL, start, end);
        String packed = FastaPacker.getPackedFastaData(PACKED, start, end);
        Assert.assertEquals(original,packed);
    }


}
