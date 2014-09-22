package com.lordjoe.lib.xml;

import com.lordjoe.utilities.*;


import java.lang.reflect.*;

/**
 * Data object used by ClassAnalyzer to hold a property
 *
 * @author Steve Lewis
 * @since july 99
 */

public class ClassProperty  
{
    private String m_Name;
    private Method m_SetMethod;
    private Method m_GetMethod;
    private Field m_BackingField;
    private Class m_OwningClass;
    private Class m_Type;
    private Class m_ObjectType;
    private Boolean m_Nullable;
    private boolean m_ReadOnly;

    public ClassProperty() {
        m_ReadOnly = true;
    }

    public Class getOwningClass() {
        return m_OwningClass;
    }

    public void setOwningClass(Class pOwningClass) {
        m_OwningClass = pOwningClass;
    }

    public Class getType()
    {
        if (m_Type == null)
            buildType();
        return (m_Type);
    }

    /**
     * same as Type but with primitives converted to
     * wrappers
     * @return  non-null n-n-primitive class
     */
    public Class getObjectType()
    {
        if (m_Type == null)
            buildType();
        return (m_ObjectType);
    }

    public Field getBackingField() {
        return m_BackingField;
    }

    public void setName(String pName) {
        m_Name = pName;
    }

    public void setSetMethod(Method pSetMethod) {
        m_SetMethod = pSetMethod;
        if( m_GetMethod == null)
            setReadOnly(m_SetMethod != null);
        else
             setReadOnly(m_SetMethod == null );
    }

    public void setGetMethod(Method pGetMethod) {
        m_GetMethod = pGetMethod;
        if(m_SetMethod == null)
              setReadOnly(true);
     }

    public boolean isReadOnly() {
        return m_ReadOnly;
    }

    public void setReadOnly(boolean pReadOnly) {
        if("SiteType".equals(m_Name) && !pReadOnly)
            Util.breakHere();
        m_ReadOnly = pReadOnly;
    }

    public void setBackingField(Field pBackingField) {
        m_BackingField = pBackingField;
    }

    public void setNullable(Boolean pNullable) {
        m_Nullable = pNullable;
    }

    protected void buildType()
    {
        if (m_GetMethod != null)
            m_Type = m_GetMethod.getReturnType();
        else
            m_Type = m_SetMethod.getParameterTypes()[0];
        // objectType is a non-primitive type
        if(m_Type.isPrimitive())
            m_ObjectType = ClassUtilities.primitiveToWrapper(m_Type);
        else
            m_ObjectType = m_Type;
    }

    public boolean isIndexed()
    {
        if (m_GetMethod != null) {
            Class[] args = m_GetMethod.getParameterTypes();
            return (args.length > 0);
        }
        else {
            Class[] args = m_SetMethod.getParameterTypes();
            return (args.length > 1);
        }
    }

    public boolean isNullable()
    {
        if (m_Nullable == null)
            buildNullable();
        return m_Nullable;
    }

    private void buildNullable()
    {
            m_Nullable = Boolean.FALSE;

     }

    public Class getPropertyType()
    {
        return (m_Type);
    }

    public Method getSetMethod()
    {
        return (m_SetMethod);
    }

    public Method getGetMethod()
    {
        return (m_GetMethod);
    }

    public String getName()
    {
        return (m_Name);
    }

    public String toString() {
        return getName();

    }

    public boolean isStatic()
    {
        if (m_GetMethod != null)
            return (Modifier.isStatic(m_GetMethod.getModifiers()));
        else
            return (Modifier.isStatic(m_SetMethod.getModifiers()));
    }

    public Object getValue(Object target)
    {
        Method gm = getGetMethod();
        if (gm == null)
            throw new IllegalStateException("no getter for this property");
        try {
            return gm.invoke(target);
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void setValue(Object target, Object value)
    {
        Method gm = getSetMethod();
        if (gm == null)  {
            throw new IllegalStateException("no setter for property " + getName() + 
               " in class " + getOwningClass());
        }
        try {
            gm.invoke(target, value);
        }
        catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
