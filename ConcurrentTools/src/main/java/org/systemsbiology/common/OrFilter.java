package org.systemsbiology.common;

import java.util.List;

/**
 * org.systemsbiology.common.AndFilter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public class OrFilter<T> extends CompositeFilter<T> {
    public static final CompositeFilter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = OrFilter.class;

    public OrFilter(IFilter<T>... added) {
        super(added);
    }

    /**
     * determine whether this matches a specific filter
     *
     * @param test  - non-null test item
     * @param added any added data
     * @return tru for a match
     */
    @Override
    public boolean accept(T test, Object... added) {
        List<IFilter<T>> list = getFilters();
        synchronized (list) {

            for (IFilter<T> testf : list) {
                if (testf.accept(test, added))
                    return true;
            }
            return false; // all pass
        }
    }
}