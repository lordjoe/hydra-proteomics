package com.lordjoe.algorithms;

/**
 * com.lordjoe.algorithms.DirectedEdge
 * User: Steve
 * Date: 4/23/12
 */
public class DirectedEdge {
    public static final DirectedEdge[] EMPTY_ARRAY = {};

    private final GraphNode m_Start;
    private final GraphNode m_End;

    public DirectedEdge(final int start, final int end) {
       this(GraphNode.getNode(start),GraphNode.getNode(end));
    }

    public DirectedEdge(final GraphNode start, final GraphNode end) {
        m_Start = start;
        m_End = end;
    }

    public GraphNode getStart() {
        return m_Start;
    }

    public GraphNode getEnd() {
        return m_End;
    }

       @Override
    public String toString() {
        return getStart().toString() + "->" + getEnd();
    }
}

