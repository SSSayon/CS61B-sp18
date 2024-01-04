public class Trie {
    public class TrieNode {
        private TrieNode[] children;
        public boolean isEnd;
        public String word;

        public TrieNode(String word) {
            children = new TrieNode[26];
            isEnd = false;
            this.word = word;
        }

        public TrieNode get(char ch) {
            return children[ch - 'a'];
        }

        public boolean contains(char ch) {
            return children[ch - 'a'] != null;
        }

        public void put(char ch) {
            children[ch - 'a'] = new TrieNode(this.word + String.valueOf(ch));
        }
    }

    public TrieNode root;

    public Trie() {
        root = new TrieNode("");
    }

    public void insert(String s) {
        TrieNode node = root;
        for (char ch : s.toCharArray()) {
            if (!node.contains(ch)) {
                node.put(ch);
            }
            node = node.get(ch);
        }
        node.isEnd = true;
    }
}
