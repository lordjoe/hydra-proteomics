package com.lordjoe.algorithms;


import java.util.*;

/**
 * com.lordjoe.algorithms.QuickSort
 * User: Steve
 * Date: 3/21/12
 */
public class QuickSort {
    public static final QuickSort[] EMPTY_ARRAY = {};

    public static final int[] STATE_0 = {3, 8, 2, 5, 1, 4, 7, 6};
    public static final int[] STATE_1 = {1, 2, 3, 5, 8, 4, 7, 6};

    public static void swap(int[] array, int pos1, int pos2) {
        int temp = array[pos2];
        array[pos2] = array[pos1];
        array[pos1] = temp;
    }

    private int m_NumberSwaps;
    private int m_NumberCompares;

    public void quicksort(int[] a) {
        m_NumberSwaps = 0;
        quicksortInternal(a, 0, a.length - 1);
    }

    public void quicksortInternal(int[] a, int start, int end) {
        if (end <= start)
            return;

        chooseAndPositionPivot(a, start, end);

        int[] test = Inversions.buildCopy(a);  // debug stuff

        int position = partition(a, start, end);

        // debug stuff
        if (!validatePartition(a, start, end, position)) {
            position = partition(test, start, end);
            validatePartition(test, start, end, position);
            throw new IllegalStateException("problem"); // ToDo change
        }

        if (position > start + 1)
            quicksortInternal(a, start, position - 1);
        if (position < end - 2)
            quicksortInternal(a, position + 1, end);

    }

    private int partitionX(int arr[], int left, int right) {
        int i = left, j = right;
        int tmp;
        int pivot = arr[(left + right) / 2];

        while (i <= j) {
            while (arr[i] < pivot)
                i++;
            while (arr[j] > pivot)
                j--;
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i + 1;
    }

    private int partition(final int[] a, final int start, final int end) {
        if (end == start + 1) {
            if (a[start] < a[end])
                return start;
            else {
                swap(a, end, start);
                return end;

            }
        }
        int pivot = a[start];
        int greaterThan = end;
        boolean swapLessThan = false;
        boolean swapGreaterThan = false;
        int lastLessThan = start + 1;
        for (; lastLessThan < greaterThan; lastLessThan++) {
            if (a[lastLessThan] < pivot)
                continue;
            swapLessThan = true;
            for (; greaterThan > lastLessThan; greaterThan--) {
                if (a[greaterThan] > pivot)
                    continue;
                swapGreaterThan = true;
                break;
            }
            if (swapLessThan && swapGreaterThan) {
                swap(a, lastLessThan, greaterThan--);
                swapLessThan = false;
                swapGreaterThan = false;
                if (greaterThan <= lastLessThan) {
                    greaterThan++;
                    break;
                }
            }
            else {  // no more swaps
                if (lastLessThan == start + 1)
                    return start;
                else {
                    lastLessThan++;
                    break;
                }

            }

        }
        if (greaterThan == end && a[greaterThan] < pivot) {
            swap(a, end, start);
            return end;
        }
        else {
            if(greaterThan == lastLessThan)  {
                if (a[lastLessThan  ] < pivot) {
                    swap(a, lastLessThan, start);
                         return lastLessThan;
                     }
                 else {
                    swap(a, greaterThan - 1, start);
                     return greaterThan - 1;

                }
            }
            if (a[greaterThan - 1] < pivot && a[lastLessThan] < pivot && lastLessThan < greaterThan - 1) {
                swap(a, lastLessThan, start);
                return lastLessThan;
            }
            else {
                if (a[greaterThan - 1] < pivot) {
                     swap(a, greaterThan - 1, start);
                    return greaterThan - 1;
                }
                else {
                    throw new IllegalStateException("problem"); // ToDo change
                }
            }

        }
    }

    /**
     * make the pivot the first element
     *
     * @param pA
     * @param pStart
     * @param pEnd
     */
    private void chooseAndPositionPivot(final int[] pA, final int pStart, final int pEnd) {
        return; // choose first
    }

    /**
     * test aht algorithm on a arndonly generatesd array
     *
     * @param n
     */
    private static void sort_test(QuickSort qs, int n) {
        int[] data = Inversions.generateRandomArray(n);  // make an array
        qs.quicksort(data);
        sortTest(qs, data);
    }

    private static void sortTest(final QuickSort qs, final int[] pData) {
        int[] test = Inversions.buildCopy(pData);
        Arrays.sort(test);
        for (int i = 0; i < test.length; i++) {
            if (test[i] != pData[i])
                throw new IllegalStateException("problem"); // ToDo change;

        }
    }

    private static boolean validatePartition(int[] a, int start, int end, int position) {
        int pivot = a[position];
        for (int i = start; i < position; i++) {
            if (a[i] > pivot)
                return false;

        }
        for (int i = position; i < end; i++) {
            if (a[i] < pivot)
                return false;

        }
        return true;
    }

    public static void main(String[] args) {
        QuickSort qs = new QuickSort();
        int[] test = Inversions.buildCopy(STATE_0);
        qs.quicksort(test);
        sortTest(qs, test);

        int[] test2 = Inversions.buildCopy(Inversions.MAX_INVERSIONS);
        qs.quicksort(test2);
        sortTest(qs, test);

        for (int i = 10; i < 1000000; i *= 10) {
            sort_test(qs, i);
        }
    }

}
