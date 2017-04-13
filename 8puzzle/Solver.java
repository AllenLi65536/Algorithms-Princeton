/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver
 *  Dependencies: 
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
public class Solver {
    private int move = 0;
    private Stack<Board> solution;
    private MinPQ<Node> openSet;
    private MinPQ<Node> openSet2;
    
    private class Node implements Comparable<Node> {
        private Board board;
        private Node prev;
        private int moves;
        private int manhattan;
        
        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.manhattan = board.manhattan();
            this.prev = prev;
        }

        public int compareTo(Node that) {
            int n1 = this.moves+this.manhattan; 
            int n2 = that.moves+that.manhattan;
            
            if (n1 > n2)
                return 1;
            if (n1 < n2)
                return -1;

            n1 = this.moves+this.board.hamming(); 
            n2 = that.moves+that.board.hamming();
            
            if (n1 > n2)
                return 1;
            if (n1 < n2)
                return -1;
            
            return 0;
        }
    }
    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        if (initial == null)
            throw new java.lang.NullPointerException();
        
        Node current = null, current2 = null;
        
        solution = new Stack<Board>();
        
        openSet = new MinPQ<Node>();
        openSet2 = new MinPQ<Node>();
        openSet.insert(new Node(initial, 0, null));
        openSet2.insert(new Node(initial.twin(), 0, null));

        while (!openSet.isEmpty() && !openSet2.isEmpty()) {
            current = openSet.delMin();

            if (current.board.isGoal()) {
                this.move = current.moves;
                while (current != null) {
                    solution.push(current.board);
                    current = current.prev;
                }
                openSet = null;
                openSet2 = null;
                return;
            }

            current2 = openSet2.delMin();

            if (current2.board.isGoal()) {
                move = -1;
                openSet = null;
                openSet2 = null;
                return;
            }

            for (Board neighbor: current.board.neighbors()) {
                if (current.prev == null || !neighbor.equals(current.prev.board))
                    openSet.insert(new Node(neighbor, current.moves+1, current));
            }
            for (Board neighbor: current2.board.neighbors()) {
                if (current2.prev == null || !neighbor.equals(current2.prev.board))
                    openSet2.insert(new Node(neighbor, current2.moves+1, current2));
            }
        }
        move = -1;
    }
    public boolean isSolvable() {           // is the initial board solvable?
        return move != -1;
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return move;
    }
    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        if (move == -1)
            return null;
        return solution;
    }
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);

        }
    }
}
