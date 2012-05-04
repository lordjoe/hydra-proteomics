package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.DualStringValue
 *  An immutable object holding a set of two strings which may be used as a key
 * in a MAP
 * @author Steve Lewis
 * @date Oct 6, 2010
 */
public class DualStringValue
{
    public static DualStringValue[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = DualStringValue.class;

    private final String m_Value1;
    private final String m_Value2;

    public DualStringValue(String pValue1, String pValue2)
    {
        m_Value1 = pValue1;
        m_Value2 = pValue2;
    }

    public String getValue1()
    {
        return m_Value1;
    }

    public String getValue2()
    {
        return m_Value2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DualStringValue that = (DualStringValue) o;

        if (m_Value1 != null ? !m_Value1.equals(that.m_Value1) : that.m_Value1 != null)
            return false;
        if (m_Value2 != null ? !m_Value2.equals(that.m_Value2) : that.m_Value2 != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = m_Value1 != null ? m_Value1.hashCode() : 0;
        result = 31 * result + (m_Value2 != null ? m_Value2.hashCode() : 0);
        return result;
    }
}
