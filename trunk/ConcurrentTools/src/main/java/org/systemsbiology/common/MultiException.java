package org.systemsbiology.common;

import java.util.*;

/**
 * org.systemsbiology.common.MultiException
 * written by Steve Lewis
 * on Apr 20, 2010
 */
public class MultiException  extends RuntimeException
{
    public static final MultiException[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = MultiException.class;


    private List<Exception> m_Problems =
                new ArrayList<Exception>();

    public void addProblem(Exception added)  {
        added.printStackTrace();
        m_Problems.add(added);
    }

    public boolean hasProblem()
    {
        return !m_Problems.isEmpty();
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for(Exception e : m_Problems)
            sb.append(e.getMessage() + "\n");
        return sb.toString();
    }
}
