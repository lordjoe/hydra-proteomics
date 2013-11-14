package org.systemsbiology.hadoop.consolidator;

import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.UnimplementedQueue
 * This Queue does nothing but a subclass might add seriour methods
 *
 * @author Steve Lewis
 * @date Nov 1, 2010
 */
public abstract class UnimplementedQueue<K> extends UniimplementedCollection<K> implements Queue<K> {
    public static UnimplementedQueue[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = UnimplementedQueue.class;

    @Override
    public boolean offer(K e) {
        throw new UnsupportedOperationException("UninplementedQueue does not support this");
    }

    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll poll} only in that it throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException
     *          if this queue is empty
     */
    @Override
    public K remove() {
        throw new UnsupportedOperationException("UninplementedQueue does not support this");

    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public K poll() {
        throw new UnsupportedOperationException("UninplementedQueue does not support this");

    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException
     *          if this queue is empty
     */
    @Override
    public K element() {
        throw new UnsupportedOperationException("UninplementedQueue does not support this");

    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public K peek() {
        throw new UnsupportedOperationException("UninplementedQueue does not support this");

    }
}
