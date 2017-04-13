/******************************************************************************
 *  Compilation: javac PercolationStats.java
 *
 *  Execution: java PercolationStats
 *  
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data 
 *                structures to determine the threshold. woot. woot.
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] p;
    private int trials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)    
            throw new java.lang.IllegalArgumentException();
        this.trials = trials;
        p = new double [trials];
        
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                perc.open(StdRandom.uniform(n)+1, StdRandom.uniform(n)+1);
            }
            p[i] = perc.numberOfOpenSites()*1.0/ (n * n);
        }
    }
    public double mean() {
        return StdStats.mean(p);
    }
    public double stddev() {
        return StdStats.stddev(p);
    }
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        
        System.out.println("mean = " + percStats.mean());
        System.out.println("stddev = " + percStats.stddev());
        System.out.println("95% confidence interval = " + percStats.confidenceLo() + " " + percStats.confidenceHi());
    }

}
