package com.lordjoe.utilities;

import java.util.*;

/**
 * com.lordjoe.utilities.PseudoEnum
 *  these classes berhave like an enum but are loaded at run time
 * @author Steve Lewis
 * @date Nov 18, 2007
 */
public abstract class PseudoEnum<T extends PseudoEnum> implements Comparable {
    public static PseudoEnum[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = PseudoEnum.class;
    public static final String EXAMPLAR_NAME = "__EXAMPLAR__";
    public static final String SEPARATOR_CHAR = ";";

    private static Map<Class,PseudoEnum> gExemplarByClass = new HashMap<Class,PseudoEnum>();

    public static void populate(Class test,String data)
    {
        PseudoEnum examplar = getExamplar(test);
        examplar.populateValues(data);
    }

    public static PseudoEnum valueOf(Class test,String v)
    {
        if(v == null || "null".equalsIgnoreCase(v))
            return null;
        PseudoEnum examplar = getExamplar(test);
        return examplar.getValueOf(v);
    }

    public static PseudoEnum[] values(Class test)
    {
        PseudoEnum examplar = getExamplar(test);
        return examplar.getValues();
    }

    public static PseudoEnum getExamplar(Class test) {
        PseudoEnum examplar = gExemplarByClass.get(test);
        if(examplar == null)
           throw new IllegalArgumentException("class " + test + " is not a PseudoEnum");
        return examplar;
    }

    private final String m_Name;

    /**
     * constructor always passes in a name
     * @param pName non-null name
     */
    protected PseudoEnum(String pName) {
        m_Name = pName.trim();
        if(isExemplar()) {
            Class<? extends PseudoEnum> aClass = getClass();
            gExemplarByClass.put(aClass,this);
        }
    }

    /**
     * return the name - usually this is the value of the enum
     * @return  non-null name
     */
    public String getName() {
        return m_Name;
    }

    /**
     * {@link #getName()} is the string representation
     * @return non-null String
     */
    @Override
    public String toString() {
        return getName();
    }

    public String getXMLString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<Option ");
        sb.append("class=\"");
        sb.append(getClass().getName());
        sb.append("\" values=\"");
        sb.append(getValuesString());
        sb.append("\" />");
        return sb.toString();
    }

    protected String getValuesString()
    {
        StringBuilder sb = new StringBuilder();
        T[] values = getValues();
        for (int i = 0; i < values.length; i++) {
            T value = values[i];
            if(sb.length() > 0)
                sb.append(SEPARATOR_CHAR);
            sb.append(value.getName());
        }
        return sb.toString();
    }


    /**
     * get a special member which is an examplar - this is constructed at load time
     * @return
     */
    public abstract T getExemplar();

    /**
     * get a special member which is an examplar - this is constructed at load time
     * @return
     */
    public abstract T getValueOf(String name);

    /**
     * one member is an Exemplar and is used to access static methods without
     * reflection
     * @return  true if this is the one class exemplar
     */
    public boolean isExemplar()
    {
        return getName().equals(EXAMPLAR_NAME);
    }

    /**
     * return all possible values - similar th the method for Enum
     * @return
     */
    public abstract T[] getValues();

    /**
     * Use the String provided
     * to populate all values
     * for the instance.
     * @param s usually some kind of delimited String
     */
    protected abstract void populateValues(String s);

    /**
     * sort by name
     * @param o object to compart to - must be PSeudoEnum
     * @return  -1,0,1 for less, equal,greater
     */
    public int compareTo(Object o) {
        if(o == this)
            return 0; // good to handle this case
        PseudoEnum ps = (PseudoEnum)o;
        return getName().compareTo(ps.getName());
    }
}
