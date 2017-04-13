/******************************************************************************
 *  Compilation: javac Percolation.java
 *
 *  Execution: java Percolation
 *  
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data 
 *                structures to determine the threshold. woot. woot.
 ******************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private boolean[] idOpen;
    private int n;
    private int numberOfOpen;
    private WeightedQuickUnionUF uf;
    // private WeightedQuickUnionUF uf2;

    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();
        this.n = n;
        idOpen = new boolean [n*n];
        uf = new WeightedQuickUnionUF(n*n + 2);
        // uf2 = new WeightedQuickUnionUF(n*n + 1);
        for (int i = 0; i < n*n; i++)
            idOpen[i] = false;
        
    }

    public void open(int i, int j) {
        if (i > n || i <= 0)
            throw new java.lang.IndexOutOfBoundsException("row index i out of bounds");
        if (j > n || j <= 0)
            throw new java.lang.IndexOutOfBoundsException("index j out of bounds");
        if (!isOpen(i, j)) {
            numberOfOpen++;
            idOpen[(i-1)*n+(j-1)] = true;
            
            if (i == 1)
                uf.union(j-1, n*n);
            if (i-1 > 0 && isOpen(i-1, j)) //  && !uf.connected((i-1)*n+(j-1), (i-2)*n+(j-1))) 
                uf.union((i-1)*n+(j-1), (i-2)*n+(j-1));
            if (j-1 > 0 && isOpen(i, j-1)) //  && !uf.connected((i-1)*n+(j-1), (i-1)*n+(j-2))) 
                uf.union((i-1)*n+(j-1), (i-1)*n+(j-2));
            if (i+1 <= n && isOpen(i+1, j)) //  && !uf.connected((i-1)*n+(j-1), (i)*n+(j-1))) 
                uf.union((i-1)*n+(j-1), (i)*n+(j-1));
            if (j+1 <= n && isOpen(i, j+1)) //  && !uf.connected((i-1)*n+(j-1), (i-1)*n+(j))) 
                uf.union((i-1)*n+(j-1), (i-1)*n+(j));
            if (i == n) //  && isFull(i,j))
                uf.union((n-1)*n+j-1, n*n+1);
        }
    }

    public boolean isOpen(int i, int j) {
        if (i > n || i <= 0)
            throw new java.lang.IndexOutOfBoundsException("row index i out of bounds");
        if (j > n || j <= 0)
            throw new java.lang.IndexOutOfBoundsException("index j out of bounds");
        return idOpen[(i-1)*n+(j-1)];
    }
    
    public boolean isFull(int i, int j) {
        if (i > n || i <= 0)
            throw new java.lang.IndexOutOfBoundsException("row index out of bounds");
        if (j > n || j <= 0)
            throw new java.lang.IndexOutOfBoundsException("index j out of bounds");
        return uf.connected((i-1)*n+(j-1), n*n) && isOpen(i, j);
    }

    public int numberOfOpenSites() {
        return numberOfOpen;
    }
    
    public boolean percolates() {
        return uf.connected(n*n, n*n+1);
        // for (int j = 0; j < n; j++)
        //    if (isFull(n, j+1))
        //        return true;
        // return false;
    }

}
