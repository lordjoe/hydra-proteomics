package com.lordjoe.lib.xml;

import java.lang.reflect.*;

/**
 * Data object used by ClassAnalyzer to hold a Collection
 * @author Steve Lewis
 * @since july 99
 */

public class ClassCollection
{
    private String m_Name;
    private final Class m_OwnerClass;
    private Method m_AddMethod;
    private Method m_RemoveMethod;
    private Method m_CountMethod;
    private Method m_EnumerateMethod;
    private Method m_ItemsMethod;

    public ClassCollection(Class owner) {
        m_OwnerClass = owner;
    }

    public Class getType()
    {
         return m_AddMethod.getParameterTypes()[0];
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String pName) {
        m_Name = pName;
    }

    public Class getOwnerClass() {
        return m_OwnerClass;
    }


    public Method getAddMethod() {
        return m_AddMethod;
    }

    public void setAddMethod(Method pAddMethod) {
        m_AddMethod = pAddMethod;
    }

    public Method getRemoveMethod() {
        return m_RemoveMethod;
    }

    public void setRemoveMethod(Method pRemoveMethod) {
        m_RemoveMethod = pRemoveMethod;
    }

    public Method getCountMethod() {
        return m_CountMethod;
    }

    public void setCountMethod(Method pCountMethod) {
        m_CountMethod = pCountMethod;
    }

    public Method getEnumerateMethod() {
        return m_EnumerateMethod;
    }

    public void setEnumerateMethod(Method pEnumerateMethod) {
        m_EnumerateMethod = pEnumerateMethod;
    }

    public Method getItemsMethod() {
        return m_ItemsMethod;
    }

    public void setItemsMethod(Method pItemsMethod) {
        m_ItemsMethod = pItemsMethod;
    }
}
