package org.systemsbiology.hadoop.consolidator;

import java.util.*;

/**
 * org.systemsbiology.hadoop.consolidator.UniimplementedCollection
 * The collection interface is very complex
 * sometimes we need to implement a small portion of that interface
 * This class implements the interface and thrown UnsupportedOperationException
 *
 * @author Steve Lewis
 * @date Nov 1, 2010
 */
public abstract class UniimplementedCollection<K> implements Collection<K> {
    public static UniimplementedCollection[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = UniimplementedCollection.class;

    private final Set<Queue<K>> m_ActiveQueues = new HashSet<Queue<K>>();


    @Override
    public boolean add(K e) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }


    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
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
    public Iterator iterator() {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");

    }

    @Override
    public K[] toArray() {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");
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
        throw new UnsupportedOperationException(" UniimplementedCollection does not support this");

    }
}
