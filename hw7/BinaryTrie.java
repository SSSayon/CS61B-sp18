import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {

    private Node root;

    private static class Node implements Serializable, Comparable<Node> {
        private char symbol;
        private int freq;
        private Node left, right;

        public Node(char symbol, int freq, Node left, Node right) {
            this.symbol = symbol;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return (left == null) && (right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }

    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        root = buildTrie(frequencyTable);
    }

    private Node buildTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);    
        }

        return pq.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node node = root;
        BitSequence prefix = new BitSequence();

        for (int i = 0; i < querySequence.length(); ++i) {
            if (node.isLeaf()) {
                break;
            }
            int bit = querySequence.bitAt(i);
            prefix = prefix.appended(bit);
            node = (bit == 0) ? node.left : node.right;
        }

        return new Match(prefix, node.symbol);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        buildLookupTableHelper(root, new BitSequence(), lookupTable);
        return lookupTable;
    }

    private void buildLookupTableHelper(Node node, BitSequence prefix, Map<Character, BitSequence> lookupTable) {
        if (node.isLeaf()) {
            lookupTable.put(node.symbol, prefix);
        } else {
            buildLookupTableHelper(node.left, prefix.appended(0), lookupTable);
            buildLookupTableHelper(node.right, prefix.appended(1), lookupTable);
        }
    }
}
