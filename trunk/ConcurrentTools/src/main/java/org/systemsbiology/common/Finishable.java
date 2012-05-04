package org.systemsbiology.common;

/**
 * org.systemsbiology.common.Finishable
 * perform some finishing operation
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public interface Finishable {
    public static final Finishable[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = Finishable.class;

    /**
     * take action at the end of a process
     * @param added other data
      */
    public void finish(Object... added);
}