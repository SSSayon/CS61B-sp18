package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    private final int[][] tiles;
    private final int N;
    private final int BLANK = 0;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (i < 0 || j < 0 || i >= N || j >= N) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    /**
     * Returns the board size N
     */
    public int size() {
        return N;
    }

    /**
     * Returns the neighbors of the current board
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    private int index(int i, int j) {
        return i * N + j + 1;
    }
    /**
     * Returns the Hamming estimate described below
     */
    public int hamming() {
        int dist = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (tiles[i][j] != index(i, j)) {
                    dist += 1;
                }
            }
        }
        return dist;
    }

    /**
     * Returns the Manhattan estimate described below
     */
    public int manhattan() {
        int dist = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int tile = tiles[i][j];
                if (tile == 0) {
                    continue;
                }
                int rightI = (tile - 1) / N;
                int rightJ = (tile - 1) % N;
                dist += Math.abs(rightI - i) + Math.abs(rightJ - j);
            }
        }
        return dist;
    }

    /**
     * Returns the estimated distance to the goal.
     * This method should simply return the results of manhattan()
     * when submitted to Gradescope.
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are in the same
     * position as y's
     */
    @Override
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board o = (Board) y;
        if (this.N != o.N) {
            return false;
        }
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (this.tileAt(i, j) != o.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
