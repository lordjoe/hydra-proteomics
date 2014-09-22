package com.lordjoe.logging;

import com.lordjoe.lib.xml.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

/**
 * com.lordjoe.logging.AbstractObjectLogger
 *
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public class AbstractObjectLogger<T> implements IObjectLogger<T>
{
    public static AbstractObjectLogger[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AbstractObjectLogger.class;

    private final T[] m_TypeArray;
    private final String m_Name;
    private final LogEnum m_Enum;
    private final List<LoggedObject<T>> m_Items;
    private final List<ObjectLogChangedListener> m_ObjectLogChangedListeners;

    public AbstractObjectLogger(String name,T[] type)
    {
        m_Name = name;
        m_TypeArray = (T[])Array.newInstance(type.getClass().getComponentType(),type.length);
        System.arraycopy(type,0,m_TypeArray,0, type.length);
        try {
            LogEnum.EXEMPLAR.getValueOf(name);
            throw new IllegalArgumentException("duplicate Log Name " + name);
        }
        catch(Exception ex)  {
            // exception is good
        }
        m_Enum =  LogEnum.getInstance(name);
        m_Items = new ArrayList<LoggedObject<T>>();
        m_ObjectLogChangedListeners = new CopyOnWriteArrayList<ObjectLogChangedListener>();
    }

    public T[] getTypeArray()
    {
         T[] ret = (T[])Array.newInstance(m_TypeArray.getClass().getComponentType(), m_TypeArray.length);
            System.arraycopy(m_TypeArray, 0, ret, 0, ret.length);
           return ret;
      }

    public LogEnum getLogType()
    {
        return m_Enum;
    }

    /**
     * clear the contents
     */
    public void clear()
    {
       m_Items.clear();
       notifyObjectLogChangedListeners();
    }

    /**
     * save the contents
     *
     * @param dest non-null writeable  destination file
     */
    public void save(File dest)
    {
        if (true) throw new UnsupportedOperationException("Fix This");

    }

    /**
     * restore the contents
     *
     * @param dest non-null existing readable source file
     */
    public void restore(File source)
    {
        if (true) throw new UnsupportedOperationException("Fix This");

    }

    /**
     * return the object's name - frequently this is
     * final
     *
     * @return unsually non-null name
     */
    public String getName()
    {
        return m_Name;
    }

    // Add to constructor


    /**
     * add a change listener
     *
     * @param added non-null change listener
     */
    public void addObjectLogChangedListener(ObjectLogChangedListener added)
    {
        if (!m_ObjectLogChangedListeners.contains(added))
            m_ObjectLogChangedListeners.add(added);
    }

    /**
     * remove a change listener
     *
     * @param removed non-null change listener
     */
    public void removeObjectLogChangedListener(ObjectLogChangedListener removed)
    {
        while (m_ObjectLogChangedListeners.contains(removed))
            m_ObjectLogChangedListeners.remove(removed);
    }


    /**
     * notify any state change listeners - probably should
     * be protected but is in the interface to form an event cluster
     *
     * @param oldState
     * @param newState
     * @param commanded
     */
    public void notifyObjectLogChangedListeners(LoggedObject<T> added)
    {
        if (m_ObjectLogChangedListeners.isEmpty())
            return;
        ObjectLogChangedEvent evt = new ObjectLogChangedEvent(this,added);
        for (ObjectLogChangedListener listener : m_ObjectLogChangedListeners) {
            listener.onLogChanged(evt);
        }
    }
    /**
     * notify any state change listeners - probably should
     * be protected but is in the interface to form an event cluster
     *
     * @param oldState
     * @param newState
     * @param commanded
     */
    public void notifyObjectLogChangedListeners()
    {
        if (m_ObjectLogChangedListeners.isEmpty())
            return;
        for (ObjectLogChangedListener listener : m_ObjectLogChangedListeners) {
            listener.onLogCleared();
        }
    }


    /**
     * build a LoggedObject and log it
     *
     * @param data non-null data
     */
    public void log(T data)
    {
        log(new LoggedObject<T>(data));

    }

    /**
     * log a complete package
     *
     * @param data non-null data
     */
    public void log(LoggedObject<T> data)
    {
        synchronized (m_Items) {
            m_Items.add(data);
            notifyObjectLogChangedListeners(data);
        }

    }

//    /**
//     * return all objects passing all filters
//     *
//     * @param filters o or more filters
//     * @return non-null array of objects
//     */
//    public LoggedObject<T>[] getObjects(ILoggedObjectFilter<T>... filters)
//    {
//        LoggedObject<T>[] items = null;
//        synchronized (m_Items) {
//            items = m_Items.toArray(LoggedObject.EMPTY_ARRAY);
//        }
//        if (filters.length == 0)
//            return items;
//
//        List<LoggedObject<T>> holder = new ArrayList<LoggedObject<T>>();
//        for (int i = 0; i < items.length; i++) {
//            boolean isOK = true;
//            LoggedObject<T> item = items[i];
//            for (int k = 0; k < filters.length; k++) {
//                ILoggedObjectFilter<T> filter = filters[k];
//                if (!filter.acceptable(item)) {
//                    isOK = false;
//                    break;
//                }
//            }
//            if (isOK)
//                holder.add(item);
//        }
//        LoggedObject<T>[] ret = new LoggedObject[holder.size()];
//        holder.toArray(ret);
//        return ret;
//    }


}
