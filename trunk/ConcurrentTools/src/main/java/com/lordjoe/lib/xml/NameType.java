package com.lordjoe.lib.xml;


import java.io.Serializable;
import java.util.*;

/**
 * Data class for name value pairs
 * system
 */
public class NameType implements Serializable  {
    
    public NameType() {}
    public NameType(String name,Class type)
    {
        m_Name = name;
        m_Type = type;
    }
    public static Map buildMap(NameType[] items)
    {
        Map ret = new HashMap();
        for(int i = 0; i < items.length; i++)
            ret.put(items[i].m_Name,items[i].m_Type);
        return(ret);
    }
	public static final NameType[] NULL_ARRAY = {};
	public String					m_Name;
	public Class					m_Type;
}
