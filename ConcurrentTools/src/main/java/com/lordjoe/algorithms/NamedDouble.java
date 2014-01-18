package com.lordjoe.algorithms;

/**
 * com.lordjoe.algorithms.CountedString
 * holds a string and a count - immutable with good hash,equals and compateTO
 * sort by high count then alphabetically
 * User: Steve
 * Date: 7/11/13
 */
public class NamedDouble implements Comparable<NamedDouble> {
     private final String m_Name;
    private final double m_Value;

    public NamedDouble(final String name, final double pCount) {
        m_Name = name;
        m_Value = pCount;
    }

    public String getName() {
        return m_Name;
    }

    public double getValue() {
        return m_Value;
    }

    /**
     * sort by count then value
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(final NamedDouble o) {
        double count = getValue();
        double count2 = o.getValue();
       int ret = Double.compare(count,count2);
       if(ret != 0)
           return ret;
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return getName() + ":" + getValue();
    }


}
