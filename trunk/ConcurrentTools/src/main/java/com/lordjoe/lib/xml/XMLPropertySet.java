package com.lordjoe.lib.xml;

import java.util.*;


/*
 * com.lordjoe.lib.xml.XMLPropertySet
 * @author smlewis
 * Date: Apr 23, 2003
 */

public class XMLPropertySet 
{
    public static final Class THIS_CLASS = XMLPropertySet.class;
    public static final XMLPropertySet[] EMPTY_ARRAY = {};

    private Set m_Handled;
    private boolean m_FixedEntitiesExpanded;

    public XMLPropertySet()
    {
        m_Handled = new HashSet();
    }
    public boolean isHandled(Object test)
    {
        return(m_Handled.contains(test));
    }

    public void setHandled(Object test)
    {
         m_Handled.add(test);
    }

    public boolean isFixedEntitiesExpanded()
    {
        return m_FixedEntitiesExpanded;
    }

    public void setFixedEntitiesExpanded(boolean fixedEntitiesExpanded)
    {
        m_FixedEntitiesExpanded = fixedEntitiesExpanded;
    }
}