package org.systemsbiology.hadoop.consolidator;

import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.MultiQueue
 *
 * @author Steve Lewis
 * @date Nov 1, 2010
 */
public class MultiQueue<K> extends UnimplementedQueue<K> {
    public static MultiQueue[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = MultiQueue.class;

    private final Set<Queue<K>> m_ActiveQueues = new HashSet<Queue<K>>();
    private Comparator<K> m_Comparator;

    public MultiQueue() {

    }

    public MultiQueue(Comparator<K> pComparator) {
        this();
        setComparator(pComparator);
    }

    public void setComparator(Comparator<K> pComparator) {
        m_Comparator = pComparator;
    }

    public Comparator<K> getComparator() {
        return m_Comparator;
    }

    protected void addQueue(Queue<K> added) {
        m_ActiveQueues.add(added);
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public K peek() {
        List<Queue<K>> emptyQueues = new ArrayList<Queue<K>>();
        final Comparator<K> comparator = getComparator();
        K ret = null;
        // find the lowest top of the queue
        for (Queue<K> test : m_ActiveQueues) {
            if (test.isEmpty())
                emptyQueues.add(test);
            final K thisValue = test.peek();
            if (ret == null) {
                ret = thisValue;
            } else {

                if (comparator.compare(ret, thisValue) > 0) {
                    ret = thisValue;
                }
            }

        }
        // drop any empty queues
        m_ActiveQueues.removeAll(emptyQueues);
        return ret;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    @Override
    public K poll() {
        List<Queue<K>> emptyQueues = new ArrayList<Queue<K>>();
        final Comparator<K> comparator = getComparator();
        K ret = null;
        Queue<K> removedFrom = null;
        // find the lowest top of the queue
        for (Queue<K> test : m_ActiveQueues) {
            if (test.isEmpty())
                emptyQueues.add(test);
            final K thisValue = test.peek();
            if (ret == null) {
                ret = thisValue;
                removedFrom = test;
            } else {

                if (comparator.compare(ret, thisValue) > 0) {
                    ret = thisValue;
                    removedFrom = test;
                }
            }

        }
        if (removedFrom != null) {
            removedFrom.poll();  // pop the item - so far it is only a peek
            if (removedFrom.isEmpty())
                emptyQueues.add(removedFrom);
        }
        // drop any empty queues
        m_ActiveQueues.removeAll(emptyQueues);
        return ret;

    }


    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     */
    @Override
    public boolean isEmpty() {
        if (m_ActiveQueues.isEmpty())
            return true;
        List<Queue<K>> emptyQueues = new ArrayList<Queue<K>>();

        boolean ret = true;
        for (Queue<K> test : m_ActiveQueues) {
            if (test.isEmpty()) {
                emptyQueues.add(test);
            } else {
                ret = false; // not empty
                break;
            }
        }
        m_ActiveQueues.removeAll(emptyQueues);
        return ret;
    }


    /**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    @Override
    public Iterator<K> iterator() {
        return new MultiQueueIterator();
    }

    public class MultiQueueIterator implements Iterator<K> {
        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        @Override
        public boolean hasNext() {
            if (isEmpty())
                return false;
            return peek() != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws java.util.NoSuchElementException
         *          iteration has no more elements.
         */
        @Override
        public K next() {
            return poll();
        }

        /**
         * Removes from the underlying collection the last element returned by the
         * iterator (optional operation).  This method can be called only once per
         * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
         * the underlying collection is modified while the iteration is in
         * progress in any way other than by calling this method.
         *
         * @throws UnsupportedOperationException if the <tt>remove</tt>
         *                                       operation is not supported by this Iterator.
         * @throws IllegalStateException         if the <tt>next</tt> method has not
         *                                       yet been called, or the <tt>remove</tt> method has already
         *                                       been called after the last call to the <tt>next</tt>
         *                                       method.
         */
        @Override
        public void remove() {
            if (true) throw new UnsupportedOperationException("Fix This");

        }
    }


    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear() {
        if (true) throw new UnsupportedOperationException("Fix This");

    }
}
