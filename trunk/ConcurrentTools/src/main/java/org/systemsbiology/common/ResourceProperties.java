package org.systemsbiology.common;

import java.util.*;

/**
 * org.systemsbiology.common.ResourceProperties
 *
 * @author Steve Lewis
 * @date Nov 15, 2010
 */
public class ResourceProperties
{
    public static ResourceProperties[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ResourceProperties.class;

    private final Class m_Cls;
    private final String m_Resource;
    private final Properties m_Properties;

    public ResourceProperties(Class pCls, String pResource)
    {
        m_Cls = pCls;
        m_Resource = pResource;
        m_Properties = new Properties();
        try {
            m_Properties.load(m_Cls.getResourceAsStream(m_Resource));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key)  {
        return m_Properties.getProperty(key);
    }
}
