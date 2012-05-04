package org.systemsbiology.common;

/**
 * org.systemsbiology.common.Resetable
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public interface Resetable {
    public static final Resetable[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = Resetable.class;

    /**
     * reset to base state
     */
    public void reset();
}