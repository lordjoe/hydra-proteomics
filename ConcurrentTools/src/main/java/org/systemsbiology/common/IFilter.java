package org.systemsbiology.common;

/**
 * org.systemsbiology.common.IFilter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public interface IFilter<T> {
    public static final IFilter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IFilter.class;

    public static final IFilter NULL_FILTER = new NullFilter();
    public static final IFilter TRUE_FILTER = new TrueFilter();

    /**
     * determine whether this matches a specific filter
     *
     * @param test  - non-null test item
     * @param added any added data
     * @return tru for a match
     */
    public boolean accept(T test, Object... added);

    public static class NullFilter implements IFilter {
        private NullFilter() {
        }

        /**
         * determine whether this matches a specific filter
         *
         * @param test  - non-null test item
         * @param added any added data
         * @return tru for a match
         */
        public boolean accept(Object test, Object... added) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public static class TrueFilter implements IFilter {
        private TrueFilter() {
        }

        /**
         * determine whether this matches a specific filter
         *
         * @param test  - non-null test item
         * @param added any added data
         * @return tru for a match
         */
        public boolean accept(Object test, Object... added) {
            return true;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}