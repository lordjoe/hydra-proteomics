package com.lordjoe.utilities;

import org.junit.*;

import java.util.*;

/**
 * com.lordjoe.utilities.BoundedTreeTest
 * User: steven
 * Date: 12/5/11
 */
public class BoundedTreeTest {
    public static final BoundedTreeTest[] EMPTY_ARRAY = {};

    public static final int MAX_ITEMS = 16;
    public static final Comparator<String> COOMPARER = new StringLengthComparator();

    public static final String SOME_TEXT =
            "Lorem ipsum dolor sit amet, consetetur sadipscing elitr," +
                    " sed diam nonumy eirmod tempor invidunt ut labore " +
                    "et dolore magna aliquyam erat, sed diam voluptua." +
                    " At vero eos et accusam et justo duo dolores et ea rebum." +
                    " Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet." +
                    " Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                    "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua." +
                    " At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren," +
                    " no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr," +
                    " sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                    "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren," +
                    " no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    public String[] getWords() {
        if (m_Words == null) {
            m_Words = SOME_TEXT.split(" ");
            for (int i = 0; i < m_Words.length; i++) {
                m_Words[i] = m_Words[i].trim().replace(",", "").replace(".", "");

            }
        }
        return m_Words;
    }

    public static class StringLengthComparator implements Comparator<String> {
        @Override
        public int compare(final String o1, final String o2) {
            if (o1 == o2)
                return 0;
            int l1 = o1.length();
            int l2 = o2.length();
            if (l1 != l2)
                return l1 < l2 ? 1 : -1;
            // same length then compare content
            return -o1.compareTo(o2);
        }
    }

    private int m_Index;
    private String[] m_Words;

    public String getNextString() {
        String[] items = getWords();
        if (m_Index >= items.length)
            return null;
        return items[m_Index++];
    }

    @Test
    public void testBoundedTree() {
        String first;
        String last;
        BoundedTreeSet<String> bs = new BoundedTreeSet<String>(COOMPARER, MAX_ITEMS);
        String nextString;
        for (int i = 0; i < MAX_ITEMS; i++) {
            nextString = getNextString();
            bs.add(nextString);
            Assert.assertEquals(i + 1, bs.size());   // grow to max size
            first = bs.first();
            last = bs.last();
        }
        String added = getNextString();
        while (added != null) {
            if (!bs.contains(added)) {
                last = bs.last();
                first = bs.first();
                bs.add(added);
                int cmp = COOMPARER.compare(last, added);
                if (cmp < 0)
                    if (bs.contains(added))
                        Assert.assertFalse(bs.contains(added));
                if (cmp > 0) {
                    if (bs.contains(last))
                        Assert.assertFalse(bs.contains(last));
                }


                Assert.assertEquals(MAX_ITEMS, bs.size());
                first = bs.first();
                last = bs.last();
            }
            added = getNextString();
        }
        Assert.assertEquals(MAX_ITEMS, bs.size());
        String[] array1 = bs.toArray(new String[0]);
        String[] array2 = bs.toArray(new String[0]);
        Arrays.sort(array2, COOMPARER);
        Assert.assertArrayEquals(array1, array2);

    }

}
