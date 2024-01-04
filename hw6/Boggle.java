import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import edu.princeton.cs.algs4.In;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static final int[] dx = {-1, -1, -1,  0, 0,  1, 1, 1};
    private static final int[] dy = {-1,  0,  1, -1, 1, -1, 0, 1};

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        
        if (k <= 0) {
            throw new IllegalArgumentException();
        }

        Trie trie = loadTrie();
        char[][] board = loadBoard(boardFilePath);
        int ROW = board.length;
        int COL = board[0].length;

        List<String> words = new ArrayList<>();

        for (int i = 0; i < ROW; ++i) {
            for (int j = 0; j < COL; ++j) {
                boolean[][] visited = new boolean[ROW][COL];
                dfs(board, i, j, visited, trie.root.get(board[i][j]), words, trie);
            }
        }

        Collections.sort(words, (a, b) -> {
            if (a.length() != b.length()) {
                return Integer.compare(b.length(), a.length());
            } else {
                return a.compareTo(b);
            }
        });

        return words.subList(0, Math.min(k, words.size()));
    }

    private static Trie loadTrie() {
        In in = new In(dictPath);
        Trie trie = new Trie();

        while (!in.isEmpty()) {
            trie.insert(in.readLine().replaceAll("[^a-zA-Z]", "").toLowerCase());
        }
        in.close();

        return trie;
    }

    private static char[][] loadBoard(String boardFilePath) {
        In in = new In(boardFilePath);
        String[] lines = in.readAllLines();
        in.close();

        int ROW = lines.length;
        int COL = lines[0].length();

        char[][] board = new char[ROW][COL];

        for (int i = 0; i < ROW; ++i) {
            if (lines[i].length() != COL) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < COL; ++j) {
                board[i][j] = lines[i].charAt(j);
            }
        }
        return board;
    }

    private static void dfs(char[][] board, int row, int col, boolean[][] visited, Trie.TrieNode curNode, List<String> words, Trie trie) {
        int ROW = board.length;
        int COL = board[0].length;

        if (curNode == null) {
            return;
        }
        visited[row][col] = true;

        if (curNode.isEnd && curNode.word.length() >= 3 && !words.contains(curNode.word)) {
            words.add(curNode.word);
        }

        for (int i = 0; i < 8; ++i) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            if (newRow >= 0 && newRow < ROW && newCol >= 0 && newCol < COL && !visited[newRow][newCol]) {
                dfs(board, newRow, newCol, visited, curNode.get(board[newRow][newCol]), words, trie);
            }
        }

        visited[row][col] = false;
    }

    public static void main(String args[]) {
        System.out.println(Boggle.solve(7, "exampleBoard.txt"));
    }
}
