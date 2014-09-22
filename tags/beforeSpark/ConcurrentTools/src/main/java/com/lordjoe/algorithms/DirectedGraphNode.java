package com.lordjoe.algorithms;

import java.util.*;

/**
 * com.lordjoe.algorithms.DirectedGraphNode
 * User: Steve
 * Date: 4/23/12
 */
public class DirectedGraphNode implements Comparable<DirectedGraphNode> {
    public static final DirectedGraphNode[] EMPTY_ARRAY = {};

    private boolean m_Explored;
    private int m_FinishingTime;
     private final GraphNode m_Node;
    private final List<DirectedEdge> m_Starts = new ArrayList<DirectedEdge>();
    private final List<DirectedEdge> m_Ends = new ArrayList<DirectedEdge>();

    public DirectedGraphNode(final GraphNode node) {
        m_Node = node;
    }

    public int getFinishingTime() {
        return m_FinishingTime;
    }

    public void setFinishingTime(final int finishingTime) {
        m_FinishingTime = finishingTime;
    }

    public GraphNode getNode() {
        return m_Node;
    }

    public boolean isExplored() {
        return m_Explored;
    }

    public void setExplored(final boolean explored) {
        m_Explored = explored;
    }

    public int getStartCount()
    {
        return m_Starts.size();
    }

    public int getEndCount()
    {
        return m_Ends.size();
    }

    public GraphNode[] getDestinations()
    {
        Set<GraphNode> holder = new HashSet<GraphNode>();
        for(DirectedEdge edge : m_Starts) {
            holder.add(edge.getEnd());
           }
        GraphNode[] ret = new GraphNode[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public GraphNode[] getSources()
    {
        Set<GraphNode> holder = new HashSet<GraphNode>();
        for(DirectedEdge edge : m_Ends) {
            holder.add(edge.getEnd());
           }
        GraphNode[] ret = new GraphNode[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public void addEdge(DirectedEdge edge)   {
        GraphNode me = getNode();
        if(me == edge.getStart())
           m_Starts.add(edge);
        if(me == edge.getEnd())
           m_Ends.add(edge);
    }

    /**
     * sort by finishing time
     * @param o
     * @return
     */
    @Override
    public int compareTo(final DirectedGraphNode o) {
        if (o == this)
            return 0;
        if(getFinishingTime() > 0 && o.getFinishingTime() > 0) {
            return getFinishingTime() > o.getFinishingTime() ? -1 : 1;
        }
        return getNode().getNumber() < o.getNode().getNumber() ?  -1 : 1;
    }

    @Override
    public String toString() {
        return getNode().toString();
    }

}
