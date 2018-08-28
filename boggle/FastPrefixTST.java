/******************************************************************************
 *  Compilation:  javac FastPrefixTST.java
 *  Execution:    
 *  Dependencies:   
 *  
 *  For use on Coursera, Algorithms Part II programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
public class FastPrefixTST {
    private Node root;
    private Node lastNode;
    private String lastWord;

    private static class Node {
        private char c;
        private Node left, mid, right;
        private int value;
    }

    public FastPrefixTST() {
    }
    
    /* public boolean hasPrefix(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException();
        return get(prefix) >= 0;
    }*/

    public boolean contains(String key) {
        if (key == null)
            throw new IllegalArgumentException("argument to contains() is null");
        return get(key) > 0;
    } 
    // -1: not exist
    // 0: prefix
    // 1: contain
    public int get(String key) {
        if (key == null)
            throw new IllegalArgumentException("calls get() with null argument");
        if (key.length() == 0)
            throw new IllegalArgumentException("key must have length >= 1");
        if (lastWord != null && lastWord.equals(key.substring(0, key.length() - 1))) {
            lastNode = get(lastNode, key, key.length() - 2);
            // StdOut.println("lastword + key " + lastWord + " " + key);
        } else
            lastNode = get(root, key, 0);
        lastWord = key;
        if (lastNode == null)
            return -1;
        return lastNode.value;
    }

    private Node get(Node x, String key, int d) {
        if (x == null)
            return null;
        if (key.length() == 0)
            throw new IllegalArgumentException();
        char c = key.charAt(d);
        if (c < x.c)
            return get(x.left, key, d);
        else if (c > x.c)
            return get(x.right, key, d);
        else if (d < key.length() - 1)
            return get(x.mid, key, d+1);
        else
            return x;

    }

    public void put(String key) {
        if (key == null)
            throw new IllegalArgumentException();
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c)
            x.left = put(x.left, key, d);
        else if (c > x.c)
            x.right = put(x.right, key, d);
        else if (d < key.length() - 1)
            x.mid = put(x.mid, key, d+1);
        else
            x.value = 1;
        return x;
    }

}
