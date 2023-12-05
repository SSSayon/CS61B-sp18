package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int SIZE;
    private int[][] grids; // 0 for blocked, 1 for open
    private int openSites;
    private final int TOP;
    private final int BOTTOM;
    private final WeightedQuickUnionUF UF;

    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        SIZE = N;
        grids = new int[N][N];
        UF = new WeightedQuickUnionUF(N * N + 2); // 0 for TOP (all top grids), N * N + 1 for BOTTOM (all bottom grids)
        TOP = 0;
        BOTTOM = N * N + 1;
        openSites = 0;
    }

    private void checkIndex(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void open(int row, int col) { // open the site (row, col) if it is not open already
        checkIndex(row, col);
        if (!isOpen(row, col)) {
            grids[row][col] = 1;
            openSites += 1;
            
            int index = getIndex(row, col); // 先检查是否和 TOP 和 BOTTOM 相连，再检查四周的四个格子
            if (row == 0) {
                UF.union(index, TOP);
            }
            if (row == SIZE - 1) {
                UF.union(index, BOTTOM);
            }
            if (row > 0 && isOpen(row - 1, col)) {
                UF.union(index, getIndex(row - 1, col));
            }
            if (row < SIZE - 1 && isOpen(row + 1, col)) {
                UF.union(index, getIndex(row + 1, col));
            }
            if (col > 0 && isOpen(row, col - 1)) {
                UF.union(index, getIndex(row, col - 1));
            }
            if (col < SIZE - 1 && isOpen(row, col + 1)) {
                UF.union(index, getIndex(row, col + 1));
            }
        }
    }

    public boolean isOpen(int row, int col) { // is the site (row, col) open?
        checkIndex(row, col);
        return (grids[row][col] == 1);
    }

    private int getIndex(int row, int col) {
        return row * SIZE + col + 1;
    }

    public boolean isFull(int row, int col) { // is the site (row, col) full?
        checkIndex(row, col);
        int index = getIndex(row, col);
        return (isOpen(row, col) && UF.connected(index, TOP));
    }

    public int numberOfOpenSites() { // number of open sites
        return openSites;
    }

    public boolean percolates() { // does the system percolate?
        return (UF.connected(TOP, BOTTOM));
    }

    public static void main(String[] args) { // use for unit testing (not required)
        Percolation percolation = new Percolation(5);
        percolation.open(0, 1);
        percolation.open(1, 1);
        percolation.open(1, 2);
        percolation.open(2, 2);
        percolation.open(3, 2);
        percolation.open(3, 3);
        percolation.open(4, 3);
        System.out.println("Percolates: " + percolation.percolates());
    }
}