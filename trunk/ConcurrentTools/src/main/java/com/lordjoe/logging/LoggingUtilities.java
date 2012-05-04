package com.lordjoe.logging;

import com.lordjoe.utilities.*;

import javax.swing.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * com.lordjoe.logging.LoggingUtilities
 *
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public abstract class LoggingUtilities
{
    public static LoggingUtilities[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = LoggingUtilities.class;

    private static Map<Class, ListCellRenderer> gCustomRenderers =
            Collections.synchronizedMap(new HashMap<Class, ListCellRenderer>());

    private LoggingUtilities()
    {
    }

    public static ListCellRenderer getCellRenderer(Class type)
    {
        return gCustomRenderers.get(type);
    }

    public static void registerCellRenderer(Class type, ListCellRenderer renderer)
    {
        gCustomRenderers.put(type, renderer);
    }
    /**
     * turn a method into a Method into a string
     * @param m  non-null method
     * @return  non-null string
     */
    public static String buildMethodString(Method m)
    {
        if(m == null)
            return null;
        StringBuilder sb = new StringBuilder();
        String classname = ClassUtilities.shortClassName(m.getDeclaringClass());
        sb.append(classname);
        sb.append(":");
        sb.append(m.getName());
        return sb.toString();
    }
}
