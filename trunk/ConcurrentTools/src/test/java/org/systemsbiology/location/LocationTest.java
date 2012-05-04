package org.systemsbiology.location;

import org.junit.*;
import org.systemsbiology.chromosome.*;

import java.util.*;

/**
 * org.systemsbiology.location.LocationTest
 * written by Steve Lewis
 * on Apr 16, 2010
 */
public class LocationTest
{
    public static final LocationTest[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = LocationTest.class;
    public static final Random RND = new Random();
    public static final int DEFAULT_START_MAX = 1000 * 1000 * 1000;
    public static final int DEFAULT_LENGTH = 1000 * 1000;

    @Test
    public void testChromosomeLocation() {
        DefaultChromosome.setDefaultChromosomeSet("Human");
        IChromosome[] chrs = DefaultChromosome.getDefaultChromosomeSet();

        IChromosomeInterval chr = ChromosomeInterval.getChromosomeInterval(chrs[2]);

        for (int i = 0; i < chrs.length; i++) {
            IChromosome chrx = chrs[i];
            int start = RND.nextInt(DEFAULT_START_MAX);
            int end = start + RND.nextInt(DEFAULT_LENGTH);
            ISubChromosomeInterval gi = new GenomeInterval(chrx, start, end);
            Assert.assertEquals(chrx == chrs[2], chr.isContainedIn(gi));
            Assert.assertEquals(chrx == chrs[2], chr.overlaps(gi));
        }
    }

    @Test
    public void testLocation() {
        DefaultChromosome.setDefaultChromosomeSet("Human");
        IChromosome[] chrs = DefaultChromosome.getDefaultChromosomeSet();

        IChromosome chr1 = chrs[2];
        IChromosomeInterval chr = ChromosomeInterval.getChromosomeInterval(chr1);
        int start = 100000;
        int end = start + 1000;
        ISubChromosomeInterval gi = new GenomeInterval(chr1, start, end);

        ISubChromosomeInterval test = new GenomeInterval(chr1, 0, 1000);
        Assert.assertFalse(gi.overlaps(test));
        Assert.assertFalse(gi.isContainedIn(test));

        test = new GenomeInterval(chr1, start - 1000, start);
        Assert.assertTrue(gi.overlaps(test));
        Assert.assertFalse(gi.isContainedIn(test));

        test = new GenomeInterval(chr1, start, end - 100);
        Assert.assertTrue(gi.overlaps(test));
        Assert.assertTrue(gi.isContainedIn(test));

        test = new GenomeInterval(chr1, start, end);
        Assert.assertTrue(gi.overlaps(test));
        Assert.assertTrue(gi.isContainedIn(test));

        test = new GenomeInterval(chr1, start + 100, end + 100);
        Assert.assertTrue(gi.overlaps(test));
        Assert.assertFalse(gi.isContainedIn(test));


        test = new GenomeInterval(chr1, end, end + 100);
        Assert.assertTrue(gi.overlaps(test));
        Assert.assertFalse(gi.isContainedIn(test));


        test = new GenomeInterval(chr1, end + 1, end + 100);
        Assert.assertFalse(gi.overlaps(test));
        Assert.assertFalse(gi.isContainedIn(test));


    }


}
