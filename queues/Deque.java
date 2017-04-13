/******************************************************************************
 *  Compilation: javac Deque.java
 *
 *  Execution: java Deque
 *  
 *  Description:  double-ended queue.
 *  
 ******************************************************************************/
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();
        Node oldFirst = first;
        first = new Node();
        if (size == 0)
            last = first;
        first.item = item;
        first.next = oldFirst;
        if (oldFirst != null)
            oldFirst.prev = first;
        size++;
    }
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();
        Node oldLast = last;
        last = new Node();
        if (size == 0)
            first = last;
        last.item = item;
        last.prev = oldLast;
        if (oldLast != null)
            oldLast.next = last;
        size++;
    }
    public Item removeFirst() {
        if (size == 0)
            throw new java.util.NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first != null)
            first.prev = null;
        else
            last = null;
        size--;
        return item;
    }
    public Item removeLast() {
        if (size == 0)
            throw new java.util.NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (last != null)
            last.next = null;
        else
            first = null;
        size--;
        return item;
    }
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            /* Not supported. */
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() {
            if (current == null)
                throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args) {
        Deque<String> test = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("-"))
                break;
            test.addFirst(item);
        }
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("-"))
                break;
            test.addLast(item);
        }
        for (String s : test)
            StdOut.print(s+" ");
        StdOut.println();

        while (!test.isEmpty())
            StdOut.print(test.removeFirst()+" ");
        StdOut.println();
    }

}
