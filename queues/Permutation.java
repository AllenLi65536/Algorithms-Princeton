/******************************************************************************
 *  Compilation: javac Permutation.java
 *
 *  Execution: java Permutation
 *  
 *  Description:  return a randomized queue.
 *  
 ******************************************************************************/
// import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> test = new RandomizedQueue<String>();

        for (int i = 0; i < k; i++)
            test.enqueue(StdIn.readString());

        double j = 1.0;
        while (!StdIn.isEmpty() && k != 0) {
            String item = StdIn.readString();
            if (StdRandom.uniform() < (double) k/(k+j)) {
                test.dequeue();
                test.enqueue(item);
            }
            j++;
        }
        for (int i = 0; i < k; i++)
            StdOut.println(test.dequeue());

    }
}
