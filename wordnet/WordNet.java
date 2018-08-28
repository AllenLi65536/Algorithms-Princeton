/******************************************************************************
 *  Compilation:  javac WordNet.java
 *  Execution:    java WordNet
 *  Dependencies: SAP 
 *  
 *  
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class WordNet {

    private HashMap<Integer, Set<String>> synsetIDs;
    private HashMap<String, Set<Integer>> nouns;
    private Digraph graph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        synsetIDs = new HashMap<Integer, Set<String>>();
        nouns = new HashMap<String, Set<Integer>>();

        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] firstSplit = in.readLine().split(",");
            int id = Integer.parseInt(firstSplit[0]);
            String[] secondSplit = firstSplit[1].split(" ");
            for (String s : secondSplit) {
                if (!synsetIDs.containsKey(id))
                    synsetIDs.put(id, new LinkedHashSet<String>());
                synsetIDs.get(id).add(s);
                
                if (!nouns.containsKey(s))
                    nouns.put(s, new LinkedHashSet<Integer>());
                nouns.get(s).add(id);
            }
        }  

        in = new In(hypernyms);
        graph = new Digraph(synsetIDs.size());
        while (!in.isEmpty()) {
            String[] line = in.readLine().split(",");
            if (line.length < 2)
                continue;
            int source = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(source, Integer.parseInt(line[i]));
            }
        }
        if (invalidGraph())
            throw new IllegalArgumentException();
        
        sap = new SAP(graph);

    }

    private boolean invalidGraph() {
        int rootCount = 0;
        for (int i = 0; i < graph.V(); i++) 
            if (!graph.adj(i).iterator().hasNext())
                rootCount++;
        if (rootCount != 1)
            return true;
        DirectedCycle cycle = new DirectedCycle(graph);
        return cycle.hasCycle();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new IllegalArgumentException();
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new IllegalArgumentException();

        StringBuilder ret = new StringBuilder();
        for (String s : synsetIDs.get(sap.ancestor(nouns.get(nounA), nouns.get(nounB))))
            ret.append(s + " ");

        return ret.toString().trim();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet word = new WordNet(args[0], args[1]);
        // for (String noun : word.nouns())
        //    StdOut.println(noun);
        StdOut.println(word.sap("worm", "bird"));
        StdOut.println(word.distance("worm", "bird"));

    }
}
