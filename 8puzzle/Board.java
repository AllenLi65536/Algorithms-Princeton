/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board
 *  Dependencies: 
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class Board {
    private int n;
    private final char[] blocks;
    private int manhattan = -1;
    private int hamming = -1;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new char[n*n]; 
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) { 
                this.blocks[i*n+j] = (char) blocks[i][j];
            }
    }
    public int dimension() {                 // board dimension n
        return n;
    }
    public int hamming() {                  // number of blocks out of place
        if (hamming != -1)
            return hamming;
        hamming = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i*n+j] != i*n+j+1 && blocks[i*n+j] != 0)
                    hamming++;
        return hamming;
    }
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        if (manhattan != -1)
            return manhattan;
        manhattan = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i*n+j] != i*n+j+1 && blocks[i*n+j] != 0) {
                    manhattan += Math.abs((blocks[i*n+j]-1)/n - i);
                    manhattan += Math.abs((blocks[i*n+j]-1) % n - j);
                }
        return manhattan;
    }
    public boolean isGoal() {                          // is this board the goal board?
        return hamming() == 0;
    }
    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != n) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (that.blocks[i*n+j] != this.blocks[i*n+j])
                    return false;
        return true;

    }
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int[][] retBlocks = new int[n][n];
        int[][] blockij = new int[2][2];
        int count = 0;
        for (int i = 0; i < n && count < 2; i++)
            for (int j = 0; j < n && count < 2; j++) {
                if (blocks[i*n+j] != 0) {
                    blockij[count][0] = i;
                    blockij[count++][1] = j;
                }
            }
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (i == blockij[0][0] && j == blockij[0][1])
                    retBlocks[i][j] = blocks[blockij[1][0]*n+blockij[1][1]];
                else if (i == blockij[1][0] && j == blockij[1][1])
                    retBlocks[i][j] = blocks[blockij[0][0]*n+blockij[0][1]];
                else
                    retBlocks[i][j] = blocks[i*n+j];
            }
       
        return new Board(retBlocks);
    }
    
    public Iterable<Board> neighbors() {     // all neighboring boards
        Queue<Board> neighborB;
        neighborB = new Queue<Board>();
        
        int[][] retBlocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                retBlocks[i][j] = blocks[i*n+j];
            }

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i*n+j] == 0) {
                    if (i-1 >= 0) {
                        retBlocks[i][j] = retBlocks[i-1][j];
                        retBlocks[i-1][j] = 0;
                        neighborB.enqueue(new Board(retBlocks));
                        retBlocks[i-1][j] = retBlocks[i][j];
                        retBlocks[i][j] = 0;
                    }
                    if (i+1 < n) {
                        retBlocks[i][j] = retBlocks[i+1][j];
                        retBlocks[i+1][j] = 0;
                        neighborB.enqueue(new Board(retBlocks));
                        retBlocks[i+1][j] = retBlocks[i][j];
                        retBlocks[i][j] = 0;
                    }
                    if (j-1 >= 0) {
                        retBlocks[i][j] = retBlocks[i][j-1];
                        retBlocks[i][j-1] = 0;
                        neighborB.enqueue(new Board(retBlocks));
                        retBlocks[i][j-1] = retBlocks[i][j];
                        retBlocks[i][j] = 0;
                    }
                    if (j+1 < n) {
                        retBlocks[i][j] = retBlocks[i][j+1];
                        retBlocks[i][j+1] = 0;
                        neighborB.enqueue(new Board(retBlocks));
                        retBlocks[i][j+1] = retBlocks[i][j];
                        retBlocks[i][j] = 0;
                    }
                    return neighborB;
                }

        return neighborB;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", (int) blocks[i*n+j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    /* public String toString() {              // string representation of this board (in the output format specified below)
        String retVal = ""; // new String();
        retVal += n + "\n";
        for (int i = 0; i < n; i++) {            
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] >= 10)
                    retVal += blocks[i][j]+" ";
                else
                    retVal += " "+blocks[i][j]+" ";
            }
            retVal += "\n";
        }
        return retVal;
    } */

    public static void main(String[] args) { // unit tests (not graded)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.toString());
        StdOut.println("hamming "+initial.hamming());
        StdOut.println("manhattan "+initial.manhattan());
        StdOut.println("twin "+initial.twin().toString());
        
        for (Board board : initial.neighbors())
            StdOut.println("neighbor "+board);

    }
}
