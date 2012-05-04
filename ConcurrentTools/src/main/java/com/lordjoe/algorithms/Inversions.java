package com.lordjoe.algorithms;

import com.lordjoe.utilities.*;

import java.util.*;

/**
 * com.lordjoe.algorithms.Inversions
 * User: Steve
 * Date: 3/12/12
 */
public class Inversions {
    public static final Inversions[] EMPTY_ARRAY = {};

    public static final int[] MAX_INVERSIONS = {6, 5, 4, 3, 2, 1};
    public static final int[] ZERO_INVERSIONS = {1, 2, 3, 4, 5, 6};
    public static final int[] ONE_INVERSIONS = {1, 3, 2, 4, 5, 6};

    public static final Random RND = new Random();
    public static final int VALUE_FACTOR = 4; // limit on values in array relative to array size
    public static final boolean REQUIRE_UNIQUENESS = true;

    public static int[] generateRandomArray(int size) {
        Set<Integer> alreadyUsed = new HashSet<Integer>();
        int[] ret = new int[size];
        for (int i = 0; i < ret.length; i++) {
            int item = RND.nextInt(VALUE_FACTOR * size);
            if (REQUIRE_UNIQUENESS) {
                while (alreadyUsed.contains(item))     // make values distinct
                    item = RND.nextInt(VALUE_FACTOR * size);
                alreadyUsed.add(item);
            }
            ret[i] = item;
        }
        return ret;
    }

    /**
     * count inversions - using the obvious n squared algorithm - this is important for testing
     *
     * @param pValues
     * @return
     */
    private static long countInversionsSlow(final int[] pValues) {
        // sort by values - indices will show position in original array
        long inversions = 0;
        for (int i = 0; i < pValues.length; i++) {
            int value = pValues[i];
            for (int j = i + 1; j < pValues.length; j++) {
                int test = pValues[j];
                if (test < value)
                    inversions++;
            }
        }
        return inversions;
    }

    /**
     * build indexed integers for an array of ints
     *
     * @param values
     * @return
     */
    public static int[] buildCopy(int[] values) {
        int[] ret = new int[values.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = values[i];

        }
        return ret;

    }



    /**
     * run a number of tests - an exception is thrown on failure
     */
    private static void all_tests() {
        for (int i = 10; i < 1000000; i *= 10) {
            random_test(i);
        }
        testCountInversions(ZERO_INVERSIONS, 0);
        testCountInversions(ONE_INVERSIONS, 1);
        testCountInversions(MAX_INVERSIONS, 15);
        for (int i = 2; i < 12; i++) {
            inverse_test(i);
        }

    }

    /**
     * test aht algorithm on a arndonly generatesd array
     * @param n
     */
    private static void random_test(int n) {
        int[] data = generateRandomArray(n);  // make an array
        long desired = countInversionsSlow(data);  // count with the slow n square algorithm
        //       int desired = countInversions (data);  // count with the slow n square algorithm
        testCountInversions(data, desired);  // test the fast algorithm
    }

    private static void inverse_test(int n) {
        int[] data = new int[n];
        // inverse order
        for (int i = 0; i < data.length; i++) {
            data[i] = n - i;
        }
        int desired = (n * (n - 1)) / 2;
        testCountInversions(data, desired);
    }


    /**
     * @param array   array to test
     * @param desired expexted number of inversions
     */
    private static void testCountInversions(final int[] array, final long desired) {
        long nInversions = countInversions(array);
        if (nInversions != desired)
            throw new IllegalStateException("counted " + nInversions + " wanted " + desired);
        if (nInversions != countInversionsSlow(array))
            throw new IllegalStateException("counted " + nInversions + " wanted " + countInversionsSlow(array));
    }



    /**
     * count inversions -
       * @param pValues  !numm array - not altered
     * @return
     */
    private static long countInversions(final int[] pValues) {
        // sort by values - indices will show position in original array
        Mergesort mergesort = new Mergesort(buildCopy(pValues));
        return mergesort.getNumberInversions();
    }



    /**
     * See http://www.vogella.de/articles/JavaAlgorithmsMergesort/article.html
     */
    private static class Mergesort {
        private final int[] numbers;
        private final int[] helper;

        private final int number;
        private long m_NumberInversions;

        public Mergesort(final int[] values) {
            numbers = values;
            number = values.length;
            this.helper = new int[number];
            mergesort(0, number - 1);
        }

        public long getNumberInversions() {
            return m_NumberInversions;
        }

        public void incrementNumberInversions(int added) {
            m_NumberInversions += added;
        }

        private void mergesort(int low, int high) {
            // Check if low is smaller then high, if not then the array is sorted
            if (low < high) {
                // Get the index of the element which is in the middle
                int middle = (low + high) / 2;
                // Sort the left side of the array
                mergesort(low, middle);
                // Sort the right side of the array
                mergesort(middle + 1, high);
                // Combine them both
                merge(low, middle, high);
            }

        }

        private void merge(int low, int middle, int high) {

            // Copy both parts into the helper array
            for (int i = low; i <= high; i++) {
                helper[i] = numbers[i];
            }

            int i = low;
            int j = middle + 1;
            int k = low;
            // Copy the smallest values from either the left or the right side back
            // to the original array
            while (i <= middle && j <= high) {
                if (helper[i] <= helper[j]) {
                    numbers[k] = helper[i];
                    i++;
                }
                else {
                    numbers[k] = helper[j];
                    j++;
                    incrementNumberInversions(middle - i + 1);
                }
                k++;
            }
            // Copy the rest of the left side of the array into the target array
            while (i <= middle) {
                numbers[k] = helper[i];
                k++;
                i++;
            }

        }
    }


    public static int[] readArray(String fileName) {
        // my code to read lines in a file
        String[] lines = FileUtilities.readInLines(fileName);
        int[] ret = new int[lines.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Integer.parseInt(lines[i]);

        }
        return ret;
    }


    public static void main(String[] args) {
        all_tests();
        int[] values = readArray(args[0]);
        long nInversions = countInversions(values);
        long nInversions2 = countInversionsSlow(values);
         System.out.println("Number inversions " + nInversions +  " check " + nInversions2);
    }

}
