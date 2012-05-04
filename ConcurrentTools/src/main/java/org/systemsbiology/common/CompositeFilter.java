package org.systemsbiology.common;

import java.util.ArrayList;
import java.util.List;

/**
 * org.systemsbiology.common.CompositeFilter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public abstract class CompositeFilter<T> implements IFilter<T> {
    public static final CompositeFilter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = CompositeFilter.class;
    private final List<IFilter<T>> m_Filters = new ArrayList<IFilter<T>>();


    public CompositeFilter(IFilter<T>... added) {
        for (int i = 0; i < added.length; i++) {
            IFilter<T> tiFilter = added[i];
            addFilter(tiFilter);
        }
    }


    public void addFilter(IFilter<T> added) {
        synchronized (m_Filters) {
            if (!m_Filters.contains(added))
                m_Filters.add(added);
        }
    }

    protected List<IFilter<T>> getFilters() {
        return m_Filters;
    }

    /**
     * determine whether this matches a specific filter
     *
     * @param test  - non-null test item
     * @param added any added data
     * @return tru for a match
     */
    public abstract boolean accept(T test, Object... added);
}
