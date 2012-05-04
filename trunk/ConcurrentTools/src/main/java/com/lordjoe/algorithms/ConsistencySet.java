package com.lordjoe.algorithms;

import javax.swing.*;
import java.util.*;

/**
 * com.lordjoe.algorithms.ConsistencySet
 * User: Steve
 * Date: 4/23/12
 */
public class ConsistencySet implements Comparable<ConsistencySet> {
    public static final ConsistencySet[] EMPTY_ARRAY = {};

    private final List<DirectedGraphNode> m_Edges;

    public ConsistencySet(final List<DirectedGraphNode> edges) {
        m_Edges = edges;
    }

    public int getSize() {
        return m_Edges.size();
    }


       @Override
    public String toString() {
        return Integer.toString(getSize());
    }

    @Override
    public int compareTo(final ConsistencySet o) {
        if(this == o)
            return 0;
        int size = getSize();
        int osize = o.getSize();
        if(size < osize)
            return -1;
        if(size > osize)
            return 1;
        return hashCode() < o.hashCode() ? -1 : 1; // I dont care but need to be consistent
    }
}
