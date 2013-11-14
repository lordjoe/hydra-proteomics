package com.lordjoe.algorithms;

import java.util.*;

/**
 * com.lordjoe.algorithms.LimitedPriorityQueue
 *
 * @author Steve Lewis
 * @date 12/11/13
 */
public class LimitedPriorityQueue<T> extends PriorityQueue<T> {
    private final int maxSize;

    /**
     * Creates a {@code PriorityQueue} with the specified initial capacity
     * that orders its elements according to the specified comparator.
     *
     * @param initialCapacity the initial capacity for this priority queue
     * @param comparator      the comparator that will be used to order this
     *                        priority queue.  If {@code null}, the {@linkplain Comparable
     *                        natural ordering} of the elements will be used.
     * @throws IllegalArgumentException if {@code initialCapacity} is
     *                                  less than 1
     */
    public LimitedPriorityQueue(int maxSize, int initialCapacity, Comparator<? super T> comparator) {
        super(initialCapacity, comparator);
        this.maxSize = maxSize;
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }
}
