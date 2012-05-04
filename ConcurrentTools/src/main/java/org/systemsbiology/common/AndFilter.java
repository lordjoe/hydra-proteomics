package org.systemsbiology.common;

import java.util.ArrayList;
import java.util.List;

/**
 * org.systemsbiology.common.AndFilter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public class AndFilter<T> extends CompositeFilter<T> {
    public static final AndFilter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = AndFilter.class;


    public AndFilter(IFilter<T>... added) {
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
                boolean accepted = testf.accept(test, added);
                if (!accepted)
                    return false;
            }
            return true; // all pass
        }
    }

}
