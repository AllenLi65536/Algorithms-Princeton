/******************************************************************************
 *  Compilation: javac Percolation.java
 *
 *  Execution: java Percolation
 *  
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data 
 *                structures to determine the threshold. woot. woot.
 ******************************************************************************/
// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private int[] id;
    private int[] sz;
    private int n;
    private int numberOfOpen;
    // private WeightedQuickUnionUF uf;

    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();
        this.n = n;
        id = new int [n*n+1];
        sz = new int [n*n+1];
        for (int i = 0; i < n; i++) {
            id[i] = n*n;
            sz[i] = 0;
        }
        for (int i = n; i < n*n + 1; i++) {
            id[i] = i;
            sz[i] = 0;
        }
        sz[n*n] = n*n+1;
    }

    public void open(int i, int j) {
        if (i > n || i <= 0)
            throw new java.lang.IndexOutOfBoundsException("row index i out of bounds");
        if (j > n || j <= 0)
            throw new java.lang.IndexOutOfBoundsException("index j out of bounds");
        if (!isOpen(i, j)) {
            numberOfOpen++;
            sz[(i-1)*n+(j-1)] = 1;
            
            if (i-1 > 0 && isOpen(i-1, j)) 
                bind(i, j, i-1, j);
            if (j-1 > 0 && isOpen(i, j-1)) 
                bind(i, j, i, j-1);
            if (i+1 <= n && isOpen(i+1, j)) 
                bind(i, j, i+1, j);
            if (j+1 <= n && isOpen(i, j+1)) 
                bind(i, j, i, j+1);
        }
    }

    private void bind(int i, int j, int x, int y) {
        int root1 = root(i, j);
        int root2 = root(x, y);
        if (root1 == root2)
            return;
        if (sz[root1] < sz[root2]) {
            id[root1] = root2;
            sz[root2] += sz[root1];
        }
        else {
            id[root2] = root1;
            sz[root1] += sz[root2];
        }

    }
    private int root(int i, int j) {
        int trace = (i-1)*n+(j-1);
        while (id[trace] != trace) {
            id[trace] = id[id[trace]];
            trace = id[trace];
        }
        return trace;
    }

    public boolean isOpen(int i, int j) {
        if (i > n || i <= 0)
            throw new java.lang.IndexOutOfBoundsException("row index i out of bounds");
        if (j > n || j <= 0)
            throw new java.lang.IndexOutOfBoundsException("index j out of bounds");
        return sz[(i-1)*n+(j-1)] >= 1;
    }
    
    public boolean isFull(int i, int j) {
        if (i > n || i <= 0)
            throw new java.lang.IndexOutOfBoundsException("row index out of bounds");
        if (j > n || j <= 0)
            throw new java.lang.IndexOutOfBoundsException("index j out of bounds");
        return root(i, j) == n*n && isOpen(i, j);
    }

    public int numberOfOpenSites() {
        return numberOfOpen;
    }
    
    public boolean percolates() {
        for (int j = 0; j < n; j++)
            if (isFull(n, j+1))
                return true;
        return false;
    }

}
