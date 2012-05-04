package com.lordjoe.algorithms;

import java.util.*;

/**
* com.lordjoe.algorithms.Node
* represents a node in a graph - NOTE nodes have numbers and there is one instance per number
* Threadsafe and immutable
* User: Steve
* Date: 3/28/12
*/
public class GraphNode {

    private static final Map<Integer, GraphNode> gNodes = new HashMap<Integer, GraphNode>();

    /**
     * lookup or generate a node
     *
     * @param n index
     * @return !null node
     */
    public static GraphNode getNode(int n) {
        synchronized (gNodes) {
            GraphNode ret = gNodes.get(n);
            if (ret == null) {
                ret = new GraphNode(n);
            }
            return ret;
        }
    }

    private final Integer m_Number;

    private GraphNode(final Integer pNumber) {
        m_Number = pNumber;
        synchronized (gNodes) {
            if (gNodes.containsKey(pNumber)) {
                throw new IllegalStateException("Should bit create an existing node");
            }
            gNodes.put(pNumber, this);
        }
    }

    public Integer getNumber() {
        return m_Number;
    }

    @Override
    public String toString() {
        return m_Number.toString();
    }
}
