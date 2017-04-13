/******************************************************************************
 *  Compilation: javac RandomizedQueue.java
 *
 *  Execution: java RandomizedQueue
 *  
 *  Description:  return a randomized queue.
 *  
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] items;
    private int size;
    
    public RandomizedQueue() {
        n = 100;
        items = (Item[]) new Object[n];
        size = 0;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();
        if (n == size)
            resize(2*n);
        items[size++] = item;
    }
    private void resize(int newN) {
        Item[] copy = (Item[]) new Object[newN];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];        
        }
        n = newN;
        items = copy;    
        // if (size > 1)
        //    StdRandom.shuffle(items, 0, size-1);
    }
    public Item dequeue() {
        if (size == 0)
            throw new java.util.NoSuchElementException();
        if (size > 0 && size == n/4)
            resize(n/2);
        int randomRet = StdRandom.uniform(0, size);
        Item returnItem = items[randomRet];
        items[randomRet] = items[--size];
        items[size] = null;
        return returnItem;
    }
    public Item sample() {
        if (size == 0)
            throw new java.util.NoSuchElementException();
        return items[StdRandom.uniform(0, size)];
    }
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item> {
        private int current;
        private int iterateSize;
        private Item[] iterateItems;
        public ListIterator() {
            iterateSize = size;
            iterateItems = (Item[]) new Object[iterateSize];
            // if (size > 1)
             //   StdRandom.shuffle(items, 0, size-1);
            for (int i = 0; i < iterateSize; i++) {
                iterateItems[i] = items[(i)];        
            }
            StdRandom.shuffle(iterateItems);
            current = 0;
        }

        public boolean hasNext() {
            return iterateSize > 0;
        }
        public void remove() {
            /* Not supported. */
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() { 
            if (iterateSize == 0)
                throw new java.util.NoSuchElementException();
            Item item = iterateItems[current];
            current++;
            iterateSize--;
            return item;
        }
    }
    public static void main(String[] args) {
        RandomizedQueue<String> test = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("-"))
                break;
            test.enqueue(item);
        }
        for (String s : test) {
            StdOut.print(s+" ");
           // for(String s2 : test)
            //    StdOut.print(s2+" ");
        }
        StdOut.println();
        
        for (int i = 0; i < 5; i++)
            StdOut.print(test.sample()+" ");
        StdOut.println();

        while (!test.isEmpty())
            StdOut.print(test.dequeue()+" ");
        StdOut.println();
    }

}
