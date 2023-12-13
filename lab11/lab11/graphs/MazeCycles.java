package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private class Pair {
        public int cur;
        public int pre;
        public Pair(int cur, int pre) {
            this.cur = cur;
            this.pre = pre;
        }
    }

    private Maze maze;
    private int[] unmarkedEdgeTo;
    private Stack<Pair> stack;

    public MazeCycles(Maze m) {
        super(m);
        this.maze = m;
        unmarkedEdgeTo = new int[maze.V()];
        for (int i = 0; i < maze.V(); i += 1) {
            unmarkedEdgeTo[i] = Integer.MAX_VALUE;
        }
    }

    @Override
    public void solve() {
        stack = new Stack<>();
        stack.push(new Pair(0, -1));
        dfs();
    }

    // Helper methods go here

    private void dfs() {
        while (!stack.isEmpty()) {
            Pair pair = stack.pop();
            int cur = pair.cur;
            int pre = pair.pre;
            marked[cur] = true;
            announce();
            for (int nxt : maze.adj(cur)) {
                if (!marked[nxt]) {
                    unmarkedEdgeTo[nxt] = cur;
                    stack.push(new Pair(nxt, cur));
                } else if (nxt != pre) { // there exists a cycle
                    edgeTo[nxt] = cur;
                    announce();
                    for (int i = cur; i != nxt; i = unmarkedEdgeTo[i]) {
                        edgeTo[i] = unmarkedEdgeTo[i];
                        announce();
                    }
                    return;
                }
            }
        }
    }
}

