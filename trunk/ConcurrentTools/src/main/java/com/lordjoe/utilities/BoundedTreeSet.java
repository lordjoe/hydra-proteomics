package com.lordjoe.utilities;

import java.util.*;

/**
 * com.lordjoe.utilities.BoundedTreeSet
 * a set keepint the lowest sorting items up to maxitems
 * User: steven
 * Date: 12/5/11
 */
public class BoundedTreeSet<T> extends TreeSet<T> {
    public static final BoundedTreeSet[] EMPTY_ARRAY = {};

    private final int m_MaxItems;

    /**
     * Constructs a new, empty tree set, sorted according to the specified
     * comparator.  All elements inserted into the set must be <i>mutually
     * comparable</i> by the specified comparator: {@code comparator.compare(e1,
     *e2)} must not throw a {@code ClassCastException} for any elements
     * {@code e1} and {@code e2} in the set.  If the user attempts to add
     * an element to the set that violates this constraint, the
     * {@code add} call will throw a {@code ClassCastException}.
     *
     * @param comparator the comparator that will be used to order this set.
     *                   If {@code null}, the {@linkplain Comparable natural
     *                   ordering} of the elements will be used.
     */
    public BoundedTreeSet(final Comparator<? super T> comparator, int maxitems) {
        super(comparator);
        m_MaxItems = maxitems;
    }

    /**
     * return the maximum items allowed in the set
     *
     * @return
     */
    public int getMaxItems() {
        return m_MaxItems;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * More formally, adds the specified element {@code e} to this set if
     * the set contains no element {@code e2} such that
     * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
     * If this set already contains the element, the call leaves the set
     * unchanged and returns {@code false}.
     *
     * @param e element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     *         element
     * @throws ClassCastException   if the specified object cannot be compared
     *                              with the elements currently in this set
     * @throws NullPointerException if the specified element is null
     *                              and this set uses natural ordering, or its comparator
     *                              does not permit null elements
     */
    @Override
    public boolean add(final T e) {
        boolean ret = super.add(e);
        while (size() > getMaxItems()) {
            T last = last();
            remove(last);
        }
        return ret;
    }

    /**
     * Adds all of the elements in the specified collection to this set.
     *
     * @param c collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws ClassCastException   if the elements provided cannot be compared
     *                              with the elements currently in the set
     * @throws NullPointerException if the specified collection is null or
     *                              if any element is null and this set uses natural ordering, or
     *                              its comparator does not permit null elements
     */
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        boolean ret = true;
        for (T item : c)
            ret &= add(item);

        return ret;
    }

    /**
     * return the item nth in the ordered list
     * @param n  number >= 0
     * @return  possibly null item
     */
    public T nthBest(int n) {
        if (size() < n)
            return null;
        for (T match : this) {
            if (n-- <= 0)
                return match;
        }
        return null;
    }
}
