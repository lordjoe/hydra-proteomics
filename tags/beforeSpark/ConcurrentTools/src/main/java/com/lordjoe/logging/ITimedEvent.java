package com.lordjoe.logging;

import java.util.*;

/**
 * com.lordjoe.logging.ITimedEvent
 *
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public interface ITimedEvent
{
    public static ITimedEvent[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ITimedEvent.class;

    public Calendar getTime();


}
