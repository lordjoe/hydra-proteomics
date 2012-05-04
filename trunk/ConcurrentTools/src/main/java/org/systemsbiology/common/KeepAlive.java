package org.systemsbiology.common;

/**
 * org.systemsbiology.common.KeepAlive
 *
 * @author Steve Lewis
 * @date Jun 30, 2010
 */
public interface KeepAlive
{
    public static KeepAlive[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = KeepAlive.class;

    public void started();
}
