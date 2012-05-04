package com.lordjoe.gdata;

import java.util.*;

/**
 * com.lordjoe.gdata.GoogleColumn
 * User: steven
 * Date: 4/10/12
 */
public class GoogleColumn implements Comparable<GoogleColumn> {
    public static final GoogleColumn[] EMPTY_ARRAY = {};

    public static final Comparator<GoogleColumn> BY_INDEX = null;

    private final GoogleWorksheet m_Parent;
    private final String m_Name;
    private int m_Index;

    public GoogleColumn(final String pName, GoogleWorksheet parent, final int pIndex) {
        m_Name = pName;
        m_Parent = parent;
        m_Index = pIndex;
    }

    public GoogleColumn(final String pName, GoogleWorksheet parent) {
        this(pName, parent, -1);
    }

    public GoogleWorksheet getParent() {
        return m_Parent;
    }

    public String getName() {
        return m_Name;
    }

    public int getIndex() {
        return m_Index;
    }

    public void setIndex(final int pIndex) {
        m_Index = pIndex;
    }

    @Override
    public int compareTo(final GoogleColumn o) {
        if (o == this)
            return 0;
        int col = getIndex();
        int ocol = o.getIndex();
        if (col != ocol) {
            return col < ocol ? -1 : 1;
        }
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
       String ret = getName();
        if(ret != null)
            return ret;
        return "Col" + getIndex();
     }
}
