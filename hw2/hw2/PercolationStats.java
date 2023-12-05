package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private final double stats[];
    private final int T;

    public PercolationStats(int N, int T, PercolationFactory pf) { // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        stats = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation grids = pf.make(N);
            while (!grids.percolates()) {
                while (true) {
                    int row = StdRandom.uniform(N);
                    int col = StdRandom.uniform(N);
                    if (!grids.isOpen(row, col)) {
                        grids.open(row, col);
                        break;
                    }
                }
            }
            stats[i] = (double) grids.numberOfOpenSites() / (N * N);
        }

    }

    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(stats);
    }

    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(stats);
    }

    public double confidenceLow() { // low endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHigh() { // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // public static void main(String args[]) {
    //     int N = 100;
    //     int T = 100000;

    //     PercolationFactory pf = new PercolationFactory();
    //     PercolationStats stats = new PercolationStats(N, T, pf);

    //     System.out.println("Mean: " + stats.mean());
    //     System.out.println("Standard Deviation: " + stats.stddev());
    //     System.out.println("Confidence Interval: [" + stats.confidenceLow() + ", " + stats.confidenceHigh() + "]");
    // }
}
