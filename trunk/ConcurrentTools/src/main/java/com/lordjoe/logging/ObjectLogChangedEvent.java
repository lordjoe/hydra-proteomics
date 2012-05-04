package com.lordjoe.logging;

/**
 * com.lordjoe.logging.ObjectLogChangedEvent
 *
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public class ObjectLogChangedEvent<T>
{
    public static ObjectLogChangedEvent[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ObjectLogChangedEvent.class;

    private final IObjectLogger<T> m_Logger;
    private final LoggedObject<T> m_Added;

    public ObjectLogChangedEvent(IObjectLogger<T> pLogger, LoggedObject<T> pAdded)
    {
        m_Logger = pLogger;
        m_Added = pAdded;
    }

    public IObjectLogger<T> getLogger()
    {
        return m_Logger;
    }

    public LoggedObject<T> getAdded()
    {
        return m_Added;
    }
}
