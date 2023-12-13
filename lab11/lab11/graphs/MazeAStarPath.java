package lab11.graphs;

import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((v1, v2) -> (distTo[v1] + h(v1)) - (distTo[v2] + h(v2)));
        pq.add(s);
        
        while (!pq.isEmpty()) {
            int cur = pq.poll();
            marked[cur] = true;
            announce();

            if (cur == t) {
                targetFound = true;
                return;
            }

            for (int nxt : maze.adj(cur)) {
                if (!marked[nxt]) {
                    int dist = distTo[cur] + 1;
                    if (dist < distTo[nxt]) {
                        distTo[nxt] = dist;
                        edgeTo[nxt] = cur;
                        announce();
                        if (pq.contains(nxt)) {
                            pq.remove(nxt);
                        }
                        pq.add(nxt);
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

