package com.lordjoe.logging;

import java.util.*;

/**
 * com.lordjoe.logging.LoggedObject
 *   used to log an object and a time
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public class LoggedObject<T> implements ITimedEvent
{
    public static LoggedObject[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = LoggedObject.class;

    public static Calendar buildCalendar(Object pData)
    {
        if(pData instanceof ITimedEvent)
            return ((ITimedEvent)pData).getTime();
        return Calendar.getInstance();
    }
    
    private final Calendar m_Calendar;
    private final T m_Data;

    public LoggedObject(Calendar pCalendar, T pData)
    {
        m_Calendar = pCalendar;
        m_Data = pData;
    }

    public LoggedObject(T pData)
    {
       this(buildCalendar( pData),pData);
    }

    public Calendar getTime()
    {
        return m_Calendar;
    }

    public T getData()
    {
        return m_Data;
    }


    public String toString()
    {
        return super.toString();

    }
}
