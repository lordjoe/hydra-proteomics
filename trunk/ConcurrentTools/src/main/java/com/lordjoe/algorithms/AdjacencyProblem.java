package com.lordjoe.algorithms;

import java.io.*;
import java.util.*;

/**
 * com.lordjoe.algorithms.AdjacencyProblem
 * User: Steve
 * Date: 3/28/12
 */
public class AdjacencyProblem {

    public static final Random RND = new Random(System.currentTimeMillis());

    private final Map<Integer, Node> m_Nodes;    // index to nodes
    private final List<AdjacencyNode> m_Edges;    // list of edges


    public AdjacencyProblem() {
        m_Nodes = new HashMap<Integer, Node>();
        m_Edges = new ArrayList<AdjacencyNode>();
    }

    /**
     * copy constructor   - finding splite nodifies the structure
      * @param prob !null array to copy
     */
    public AdjacencyProblem(AdjacencyProblem prob) {
        m_Nodes = new HashMap<Integer, Node>(prob.m_Nodes);
        m_Edges = new ArrayList<AdjacencyNode>();
        for (AdjacencyNode n : prob.m_Edges) {
            m_Edges.add(new AdjacencyNode(n.getN1(), n.getN2()));
        }
    }

    /**
     * preform a single randomized split returning an with exactly 2 nodes
     * the split is the number of edges
      * @return
     */
    public AdjacencyProblem randomSplit() {
        // make a copy
        AdjacencyProblem copy = new AdjacencyProblem(this);
        int numberNodes = copy.getNumberNodes();
        // number of nodes should go down by one each iteration
        while (numberNodes > 2) {
            AdjacencyNode edge = copy.pickEdge();
            if (edge == null)
                throw new IllegalStateException("problem");  // shouldnt happen
            copy.collapseEdge(edge);
            numberNodes = copy.getNumberNodes();
        }
        return copy;
    }

    /**
     * keep running random splits tracking the minimum
      * @return split size
     */
    public int minimalSplit() {
        int numberChanges = 0;
        int numberNodes = getNumberNodes();
        int ret = getNumberEdges();
        // WHY IS THIS SO HIGH !!!
        long numberTests = (long) (100  *  numberNodes * Math.log(numberNodes));
        for (long i = 0; i < numberTests; i++) {
            AdjacencyProblem adjacencyProblem = randomSplit();
            int test = adjacencyProblem.getNumberEdges();
            // print every time we find a better solution
            if (ret > test) {
                System.out.println("old " + ret + " new " + test + " changes " + numberChanges + " iterations " + i);
                ret = test;
                numberChanges++;
            }
        }
        return ret;
    }

    /**
     * get the node with an index creating one as needed
      * @param n node indes
     * @return !null node
     */
    public Node getNode(int n) {
        synchronized (m_Nodes) {
            Node ret = m_Nodes.get(n);
            if (ret == null) {
                ret = getNodeObject(n);   // make one if needed
                m_Nodes.put(n, ret);
            }
            return ret;
        }
    }

    /**
     * add an edge
     *
     * @param e1 index 1
     * @param e2 index 2
     */
    public void addEdge(Integer e1, Integer e2) {
        Node n1 = getNode(e1);
        Node n2 = getNode(e2);
        AdjacencyNode added = new AdjacencyNode(n1, n2);
        if(!added.isLoop())
             m_Edges.add(added);

    }


    public int getNumberEdges() {
        return m_Edges.size();
    }


    public int getNumberNodes() {
        return m_Nodes.size();
    }

    /**
     * choose an edge at random
     *
     * @return
     */
    public AdjacencyNode pickEdge() {
        int numberEdges = getNumberEdges();
        switch (numberEdges) {
            case 0:
                return null;
            case 1:
                return m_Edges.get(0);
            default:
                int pick = RND.nextInt(numberEdges);
                return m_Edges.get(pick);

        }
    }

    /**
     * original definition may define edges twice a=>B and b=>a
     * the drops one item
     */
    public void dropDuplicateEdges()
    {
        Collections.sort(m_Edges);
        List<AdjacencyNode> holder = new ArrayList<AdjacencyNode>();
        AdjacencyNode lastEdge = null;
        for(AdjacencyNode edge : m_Edges) {
            if(lastEdge != null && edge.compareTo(lastEdge) == 0)
                holder.add(edge);
            lastEdge = edge;
        }
        m_Edges.removeAll(holder);
    }

    /**
     * collapse the upper node in the edge into the lower
     *
     * @param col !null edge
     */
    public void collapseEdge(AdjacencyNode col) {
        Node remains = col.getN1();
        Node dropped = col.getN2();
        List<AdjacencyNode> loopsToDrop = new ArrayList<AdjacencyNode>();
        // run through all edges (note for larger problems we may track nodes to edges
        for (AdjacencyNode ad : m_Edges) {
            if (dropped == ad.getN1()) {     // starts at the node to drop
                ad.setN1(remains);
                if (ad.isLoop())      // if new edge is loop n1=>n1 drop
                    loopsToDrop.add(ad);
            }
            else {       // ends at the node to drop
                if (dropped == ad.getN2()) {
                    ad.setN2(remains);
                    if (ad.isLoop())
                        loopsToDrop.add(ad);
                }

            }
        }
        // drop all loops
        m_Edges.removeAll(loopsToDrop);
        // drop the node
        m_Nodes.remove(dropped.getNumber());
    }

    /**
     * class representing an edge - this is mutable
     */
    public static class AdjacencyNode implements Comparable<AdjacencyNode> {
        private Node m_N1;
        private Node m_N2;

        public AdjacencyNode(final Node pN1, final Node pN2) {
            if (pN1.getNumber() < pN2.getNumber()) {
                m_N1 = pN1;
                m_N2 = pN2;
            }
            else {
                m_N1 = pN2;
                m_N2 = pN1;

            }
            validate();
        }

        public void validate() {
            int n1 = getN1().getNumber();
            int n2 = getN2().getNumber();
            if (n2 < n1)
                throw new IllegalStateException("bad edge " + this);
        }
         
        public boolean isLoop() {
            return getN1() == getN2();
        }

        public Node getN1() {
            return m_N1;
        }

        public Node getN2() {
            return m_N2;
        }

        /**
         * n1 + 32 << n1 is unique for every choice of node pairs
         * @return
         */
        public  long getValue()
        {
            return (int)getN1().getNumber() + ((long)getN2().getNumber() << 32);
        }

        /**
         * 0 if n! == o.n1 and n2 == 0.n2 else order by value
         * @param o
         * @return
         */
        @Override
        public int compareTo(final AdjacencyNode o) {
              long v1 = getValue();
            long v2 =  o.getValue();
            if(v1 == v2)
                return 0;
            return v1 < v2 ? -1 : 1;
          }

        /**
         * nodes are ordered so n1 < = n2
         * @param pN1
         */
        public void setN1(final Node pN1) {
            if (pN1.getNumber() < m_N2.getNumber()) {
                m_N1 = pN1;
            }
            else {
                m_N1 = m_N2;
                m_N2 = pN1;

            }
            validate();
        }

        /**
           * nodes are ordered so n1 < = n2
           * @param pN1
           */
        public void setN2(final Node pN2) {
            if (pN2.getNumber() > m_N1.getNumber()) {
                m_N2 = pN2;
            }
            else {
                m_N2 = m_N1;
                m_N1 = pN2;

            }
            validate();
        }

        @Override
        public String toString() {
            return getN1().toString() + "=>" + getN2().toString();
        }

    }

    /**
     * hold all existing nodes so there are never two nodes with the same index
     */
    private static final Map<Integer, Node> gNodes = new HashMap<Integer, Node>();

    /**
     * lookup or generate a node
     *
     * @param n index
     * @return !null node
     */
    public static Node getNodeObject(int n) {
        synchronized (gNodes) {
            Node ret = gNodes.get(n);
            if (ret == null) {
                ret = new Node(n);
            }
            return ret;
        }
    }

    /**
     * com.lordjoe.algorithms.Node
     * represents a node in a graph - NOTE nodes have numbers and there is one instance per number
     * Threadsafe and immutable
     * User: Steve
     * Date: 3/28/12
     */
    public static class Node {


        private final Integer m_Number;

        private Node(final Integer pNumber) {
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

    /**
     * read a file to generate a graph
     * @param fileName
     * @return
     */
    public static AdjacencyProblem readAdjacencyProblem(String fileName) {
        AdjacencyProblem prob = new AdjacencyProblem();
        // my code to read lines in a file
        String[] lines = readInLines(fileName);
        int[] ret = new int[lines.length];
        for (int i = 0; i < lines.length; i++) {
            addNodes(prob, lines[i]);

        }
        prob.dropDuplicateEdges();
        return prob;
    }

    /**
     * { method
     *
     * @param FileName the file name
     * @return - array or strings - null if file empty or does not exist
     *         }
     * @name readInLines
     * @function reads all the data in a file into an array of strings - one per line
     */
    public static String[] readInLines(String TheFile) {
        try {
            LineNumberReader r = new LineNumberReader(new FileReader(TheFile));
            List<String> holder = new ArrayList<String>();
            String s = r.readLine();
            while (s != null) {
                holder.add(s);
                s = r.readLine();
            }
            String[] ret = holder.toArray(new String[0]);
            r.close();
            return (ret);
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /**
     * Class sample to prove a simple case
     * @return
     */
    public static AdjacencyProblem simpleAdjacencyProblem() {
        AdjacencyProblem prob = new AdjacencyProblem();
        // my code to read lines in a file
        prob.addEdge(1, 2);
        prob.addEdge(1, 3);
        prob.addEdge(2, 4);
        prob.addEdge(3, 4);
        prob.addEdge(2, 3);

        return prob;
    }

    /**
     * parse a line of the form 12 34 56 67
     * @param pProb  !null graph
     * @param line   !null line as above
     */
    private static void addNodes(final AdjacencyProblem pProb, String line) {
        line = line.replace("\t", " ");
        while (line.contains("  "))
            line = line.replace("  ", " ");
        line = line.trim();
        String[] items = line.split(" ");
        Node n1 = getNodeObject(new Integer(items[0]));
        for (int i = 1; i < items.length; i++) {
            String item = items[i];
            Node n2 = getNodeObject(new Integer(item));
            pProb.addEdge(n1.getNumber(), n2.getNumber());
        }
    }

    
    private static void usage() {
        throw new IllegalStateException("must be called with the file to parse");
      }

    /**
     * call with   kargerAdj.txt as an argument
     * @param args
     */
    public static void main(String[] args) {
        // Test Simple problem
        AdjacencyProblem simple = simpleAdjacencyProblem();
        int simpleAnswer = simple.minimalSplit(); // 2 as expected
        if(2 != simpleAnswer)
            throw new IllegalStateException("Bad answer - not 2 " + simpleAnswer);

        if(args.length < 1)
            usage();
        // test real problem
        AdjacencyProblem prob = readAdjacencyProblem(args[0]);
        int minCut = prob.minimalSplit();
        System.out.println("Minimum " + minCut);
    }



}
