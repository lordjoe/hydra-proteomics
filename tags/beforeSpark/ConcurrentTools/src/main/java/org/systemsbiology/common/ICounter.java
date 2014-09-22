package org.systemsbiology.common;

/**
 * org.systemsbiology.common.ICounter
 * written by Steve Lewis
 * on Apr 7, 2010
 */
public interface ICounter<T> extends IConditionalOperator<T> {
    public static final ICounter[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = ICounter.class;

    public boolean perhapsCount(T test);

    public long getCount();

    public void reset();

}