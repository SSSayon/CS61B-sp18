package hw4.puzzle;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        public WorldState state;
        public int moves;
        public SearchNode pre;
        private int thisStateEstimatedDistanceToGoal;

        public SearchNode(WorldState state, int moves, SearchNode pre) {
            this.state = state;
            this.moves = moves;
            this.pre = pre;
            this.thisStateEstimatedDistanceToGoal = state.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.moves + this.thisStateEstimatedDistanceToGoal
                    - o.moves - o.thisStateEstimatedDistanceToGoal;

        }
    }

    private MinPQ<SearchNode> pq = new MinPQ<>();
    private List<WorldState> sol = new ArrayList<>();
    private int minMoves;
    // public int testCount = 1;


    /** Constructor which solves the puzzle, computing
    everything necessary for moves() and solution() to
    not have to solve the problem again. Solves the
    puzzle using the A* algorithm. Assumes a solution exists. */
    public Solver(WorldState initial) {
        pq.insert(new SearchNode(initial, 0, null));
        while (true) {
            SearchNode node = pq.delMin();
            if (node.state.isGoal()) {
                minMoves = node.moves;
                sol.clear();
                SearchNode tmp = node;
                while (tmp != null) {
                    sol.add(tmp.state);
                    tmp = tmp.pre;
                }
                return;
            }
            for (WorldState nbr : node.state.neighbors()) {
                if (node.pre == null || !nbr.equals(node.pre.state)) {
                    pq.insert(new SearchNode(nbr, node.moves + 1, node));
                    // testCount += 1;
                }
            }
        }
    }

    /** Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState. */
    public int moves() {
        return minMoves;
    }

    /* Returns a sequence of WorldStates from the initial WorldState
    to the solution. */
    public Iterable<WorldState> solution() {
        List<WorldState> reversedSol = new ArrayList<>();
        for (int i = minMoves; i >= 0; --i) {
            reversedSol.add(sol.get(i));
        }
        return reversedSol;
    }
}
