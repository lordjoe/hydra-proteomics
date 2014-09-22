package com.lordjoe.logging;

import com.lordjoe.utilities.*;

/**
 * com.lordjoe.utilities.LogChangedListener
 * interface to changed it an ILogger
 * @author slewis
 * @date Jan 17, 2005
 */
public interface ObjectLogChangedListener
{
    public static final Class THIS_CLASS = ObjectLogChangedListener.class;
    public static final ObjectLogChangedListener EMPTY_ARRAY[] = {};

    /**
     * do whatever is called for when a command is sent
     * @param evt non-null event
     */
    public void onLogChanged(ObjectLogChangedEvent evt);

    /**
     * act when log is cleared
     */
    public void onLogCleared();
}