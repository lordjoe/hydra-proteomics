package com.lordjoe.utilities;


/*
 * com.lordjoe.Utilities.INameable
 * @author smlewis
 * Date: May 1, 2003
 */

public interface INameable 
{
    public static final INameable[] EMPTY_ARRAY = {};

    /**
     * return a human readable name
     * @return non-null name
     */
    public String getName();
}