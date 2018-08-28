/******************************************************************************
 *  Compilation:  javac SAP.java
 *  Execution:    java SAP
 *  Dependencies: 
 *  
 *  
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {

    private Digraph graph;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G); // Copy constructor
    }

    private boolean isValid(int v) {
        return v <= graph.V() - 1 && v >= 0;
    }
    private boolean isValid(Iterable<Integer> v, Iterable<Integer> w) {
        for (int i : v)
            if (!isValid(i))
                return false;

        for (int i : w)
            if (!isValid(i))
                return false;
        return true;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isValid(v) || !isValid(w))
            throw new IllegalArgumentException();
        int minAncestor = ancestor(v, w);
        if (minAncestor == -1)
            return -1;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        return bfsV.distTo(minAncestor) + bfsW.distTo(minAncestor);

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!isValid(v) || !isValid(w))
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        int minAncestor = -1;
        int minPath = Integer.MAX_VALUE;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (bfsV.distTo(i) + bfsW.distTo(i) < minPath) {
                    minAncestor = i;
                    minPath = bfsV.distTo(i) + bfsW.distTo(i);
                }
            }
        }
        return minAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || !isValid(v, w))
            throw new IllegalArgumentException();
        
        int minAncestor = ancestor(v, w);
        if (minAncestor == -1)
            return -1;
        
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        
        return bfsV.distTo(minAncestor) + bfsW.distTo(minAncestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || !isValid(v, w))
            throw new IllegalArgumentException();
        
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        int minAncestor = -1;
        int minPath = Integer.MAX_VALUE;
        
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (bfsV.distTo(i) + bfsW.distTo(i) < minPath) {
                    minAncestor = i;
                    minPath = bfsV.distTo(i) + bfsW.distTo(i);
                }
            }
        }
        return minAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
