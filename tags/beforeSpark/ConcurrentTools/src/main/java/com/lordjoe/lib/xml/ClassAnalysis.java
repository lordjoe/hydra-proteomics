package com.lordjoe.lib.xml;

import java.lang.reflect.*;
import java.util.*;

/**
 * com.lordjoe.lib.xml.ClassAnalysis
 *
 * @author Steve Lewis
 * @date Dec 18, 2007
 */
public class ClassAnalysis {
    private final Map<String, ClassProperty> m_CaselessProperties;
    private final Map<String, ClassCollection> m_CaselessCollections;
    private final Map<String, ClassProperty> m_Properties;
    private final Map<String, Method> m_NonOverloadedMethods;
    private final Map<String, ClassCollection> m_Collections;
    private final Map<String, Object> m_Attributes;
    private ClassProperty[] m_PropertiesList;
    private ClassCollection[] m_CollectionsList;
    private final Field[] m_Fields;
    private final Class m_Type;

    protected ClassAnalysis(Class type) {
        m_Type = type;
        m_Attributes = new HashMap<String, Object>();
        m_Properties = new HashMap<String, ClassProperty>();
        m_Collections = new HashMap<String, ClassCollection>();
        m_CaselessProperties = new HashMap<String, ClassProperty>();
        m_CaselessCollections = new HashMap<String, ClassCollection>();
        m_NonOverloadedMethods = new HashMap<String, Method>();
        m_Fields = ClassAnalyzer.getClassFields(type);
    }

    protected Map<String, ClassProperty> getCaselessProperties() {
        return m_CaselessProperties;
    }

    protected Map<String, Method> getNonOverloadedMethods() {
        return m_NonOverloadedMethods;
    }

    protected Map<String, ClassCollection> getCaselessCollections() {
        return m_CaselessCollections;
    }

    protected Map<String, ClassProperty> getProperties() {
        return m_Properties;
    }

    protected Map<String, ClassCollection> getCollections() {
        return m_Collections;
    }

    protected Map<String, Object> getAttributes() {
        return m_Attributes;
    }

    public ClassProperty[] getPropertiesList() {
        synchronized (this) {
            ClassProperty[] ret = new ClassProperty[m_PropertiesList.length];
            System.arraycopy(m_PropertiesList, 0, ret, 0, ret.length);
            return ret;
        }
    }

    public ClassCollection[] getCollectionsList() {
        synchronized (this) {
            ClassCollection[] ret = new ClassCollection[m_CollectionsList.length];
            System.arraycopy(m_CollectionsList, 0, ret, 0, ret.length);
            return ret;
        }
    }

    public Field[] getFields() {
        Field[] ret = new Field[m_Fields.length];
        System.arraycopy(m_Fields, 0, ret, 0, ret.length);
        return ret;
    }

    public Class getType() {
        return m_Type;
    }

    protected synchronized void buildCollectionsList() {
        if (m_CollectionsList != null)
            return;
        Set<ClassCollection> items = new HashSet<ClassCollection>();
        Iterator<ClassCollection> e = m_Collections.values().iterator();
        while (e.hasNext()) {
            items.add(e.next());
        }
        ClassCollection[] data = new ClassCollection[items.size()];
        items.toArray(data);
        m_CollectionsList = data;
    }

    protected synchronized void buildPropertiesList() {
        if (m_PropertiesList != null)
            return;
        Set<ClassProperty> items = new HashSet<ClassProperty>();
        Iterator<ClassProperty> e = m_Properties.values().iterator();
        while (e.hasNext()) {
            items.add(e.next());
        }
        ClassProperty[] data = new ClassProperty[items.size()];
        items.toArray(data);
        m_PropertiesList = data;
    }

    public Object getAttribute(String s) {
        Object ret = m_Attributes.get(s);
        if (ret == null) {
            ret = ClassAnalyzer.buildAttribute(s, this);
        }
        return ret;
    }

    public void putAttribute(String s, Object o) {
        m_Attributes.put(s, o);
    }
}
