package com.lordjoe.algorithms;

import com.lordjoe.utilities.*;

import java.util.*;

/**
 * com.lordjoe.algorithms.DirectedGraph
 * User: Steve
 * Date: 4/23/12
 */
public class DirectedGraph {
    public static final DirectedGraph[] EMPTY_ARRAY = {};
    public static final Random RND = new Random();

    private final List<DirectedEdge> m_Edges = new ArrayList<DirectedEdge>();
    private final Map<GraphNode, DirectedGraphNode> m_Nodes = new HashMap<GraphNode, DirectedGraphNode>();
    private boolean m_Finished;


    public void guaranteeFinished() {
        if (isFinished())
            return;
        rebuild();
        setFinished(true);
    }

    protected void rebuild() {
        m_Nodes.clear();
        for (DirectedEdge edge : m_Edges) {
            GraphNode start = edge.getStart();
            if (!m_Nodes.containsKey(start))
                m_Nodes.put(start, new DirectedGraphNode(start));
            GraphNode end = edge.getEnd();
            if (!m_Nodes.containsKey(end))
                m_Nodes.put(end, new DirectedGraphNode(end));

        }
        for (DirectedEdge edge : m_Edges) {
            m_Nodes.get(edge.getStart()).addEdge(edge);
            m_Nodes.get(edge.getEnd()).addEdge(edge);
        }

    }

    public boolean isFinished() {
        return m_Finished;
    }

    public void setFinished(final boolean finished) {
        m_Finished = finished;
    }

    public void addEdge(DirectedEdge edge) {
        m_Edges.add(edge);
        setFinished(false);
    }

    public void clearExplored() {
        for (DirectedGraphNode node : m_Nodes.values()) {
            node.setExplored(false);
        }
    }

    public DirectedGraphNode getNode(GraphNode gn) {
        return m_Nodes.get(gn);
    }

    public DirectedGraphNode getNode(int gn) {
        return getNode(GraphNode.getNode(gn));
    }

    public List<DirectedGraphNode> depthFirstSearch(int[] finished,DirectedGraphNode node) {
        List<DirectedGraphNode> ret = new ArrayList<DirectedGraphNode>();
  //      Stack<DirectedGraphNode> exploring = new Stack<DirectedGraphNode>();
        if (node.isExplored())
            return ret;
 //        while(!exploring.isEmpty())  {
            depthFirstSearch (ret,finished, node);
  //      }
        return ret;
    }


    public void depthFirstSearch (List<DirectedGraphNode> found,int[] finished, DirectedGraphNode node) {
        if (node.isExplored())
            return;
        found.add(node);
        node.setExplored(true);
        for (GraphNode nd : node.getDestinations()) {
            DirectedGraphNode dest = getNode(nd);
            if (!dest.isExplored())
                depthFirstSearch(found,finished,dest);

        }
        node.setFinishingTime(finished[0]);
        finished [0]++;
      }


    public void depthFirstSearch1(List<DirectedGraphNode> found,Stack<DirectedGraphNode> exploring, DirectedGraphNode node) {
        if (node.isExplored())
            return;
        found.add(node);
        node.setExplored(true);
        for (GraphNode nd : node.getDestinations()) {
            DirectedGraphNode dest = getNode(nd);
            if (!dest.isExplored())
                exploring.push(dest);

        }
      }



    public DirectedGraphNode[] getStartNodes() {
        List<DirectedGraphNode> holder = new ArrayList<DirectedGraphNode>();
        for (DirectedGraphNode node : m_Nodes.values()) {
            if (node.getEndCount() == 0)
                holder.add(node);
        }
        DirectedGraphNode[] ret = new DirectedGraphNode[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public DirectedGraphNode[] getEndNodes() {
        List<DirectedGraphNode> holder = new ArrayList<DirectedGraphNode>();
        for (DirectedGraphNode node : m_Nodes.values()) {
            if (node.getStartCount() == 0)
                holder.add(node);
        }
        DirectedGraphNode[] ret = new DirectedGraphNode[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    public static DirectedEdge[] readEdges(String fileName) {
        // my code to read lines in a file
        List<DirectedEdge> holder = new ArrayList<DirectedEdge>();

        String[] lines = FileUtilities.readInLines(fileName);
        for (int i = 0; i < lines.length; i++) {
            String[] items = lines[i].split(" ");
            GraphNode start = GraphNode.getNode(Integer.parseInt(items[0]));
            GraphNode end = GraphNode.getNode(Integer.parseInt(items[1]));
            if (end == start)
                continue;
            DirectedEdge edge = new DirectedEdge(start, end);
            holder.add(edge);
        }
        DirectedEdge[] ret = new DirectedEdge[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static void main(String[] args) {
        DirectedEdge[] eges = readEdges(args[0]);
        DirectedGraph graph = new DirectedGraph();
        for (int i = 0; i < eges.length; i++) {
            DirectedEdge ege = eges[i];
            graph.addEdge(ege);
        }
        graph.guaranteeFinished();
        DirectedGraphNode[] starts = graph.getStartNodes();
        DirectedGraphNode[] ends = graph.getEndNodes();
        List<ConsistencySet> holder = new ArrayList<ConsistencySet>();

        for (int i = 0; i < 100 * starts.length; i++) {
            int n = RND.nextInt(starts.length);

            DirectedGraphNode start = starts[n];
            if (!start.isExplored()) {
                int[] finished =  { 1 };
                List<DirectedGraphNode> set = graph.depthFirstSearch(finished,start);
                holder.add(new ConsistencySet(set));
            }
        }
        Arrays.sort(ends);

        ConsistencySet[] answer = new ConsistencySet[holder.size()];
        holder.toArray(answer);
        Arrays.sort(answer);
        for (int i = 0; i < answer.length; i++) {
            ConsistencySet consistencySet = answer[i];
            System.out.println(consistencySet);
        }
    }

}
