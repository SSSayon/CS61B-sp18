public class RBTree<K extends Comparable<K>, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private class Node {
        private K key;
        private V val;
        private boolean color;
        private Node left, right;

        private Node(K key, V val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
        }
    }

    private boolean isRed(Node p) {
        if (p == null) return false;
        return (p.color == RED);
    }
    
    private Node root;
    private int size;

    public RBTree() {
        root = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    private V getHelper(K key, Node p) {
        if (p == null) { 
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            return p.val;
        } else if (cmp < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    public V get(K key) {
        return getHelper(key, root);
    }

    // ---------- ROTATE METHOD ---------- 

    private Node rotateLeft(Node p) {
        Node x = p.right;
        p.right = x.left;
        x.left = p;

        x.color = p.color;
        p.color = RED;

        return x;
    }

    private Node rotateRight(Node p) {
        Node x = p.left;
        p.left = x.right;
        x.right = p;

        x.color = p.color;
        p.color = RED;

        return x;
    }

    private void flipColor(Node p) {
        p.left.color = !p.left.color;
        p.right.color = !p.right.color;
        p.color = !p.color;
    }

    private Node fixUp(Node p) {
        if (isRed(p.right)) {
            p = rotateLeft(p);
        }
        if (isRed(p.left) && isRed(p.left.left)) {
            p = rotateRight(p);
        }
        if (isRed(p.left) && isRed(p.right)) {
            flipColor(p);
        }
        return p;
    }

    // ---------- PUT METHOD ----------

    public void put(K key, V val) {
        root = putHelper(key, val, root);
        root.color = BLACK; // 重要！
    }

    private Node putHelper(K key, V val, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, val, RED);
        }

        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            p.val = val;
        } else if (cmp < 0) {
            p.left = putHelper(key, val, p.left);
        } else {
            p.right = putHelper(key, val, p.right);
        }

        return fixUp(p);
    }

    // ---------- DELETE METHOD ----------

    public void deleteMin() {
        if (size == 0) {
            return;
        }
        root = deleteMinHelper(root);
        if (size > 0) {
            root.color = BLACK;
        }
    }

    private Node deleteMinHelper(Node p) { // key: 在下降的过程中保持当前节点至少是 3-node
        if (p.left == null) {
            return null;
        }
        if (!isRed(p.left) && !isRed(p.left.left)) { // 如果左儿子是 2-node，需要调整为 3-node
            p = moveRedLeft(p);
        }
        p.left = deleteMinHelper(p.left);
        return fixUp(p); // 递归地修正 4-nodes
    }

    private Node moveRedLeft(Node p) {
        flipColor(p); // (*)
        if (isRed(p.right.left)) { // 左儿子的临近兄弟至少是 3-node，则从中拿一个过来
            p.right = rotateRight(p.right);
            p = rotateLeft(p);
            flipColor(p);
        } // 不然两个儿子都是 2-node，什么都不要干，
          // 把两个儿子和父亲合并成一个 4-node 就行了，这在 * 中已经完成了
        return p;
    }

    private Node moveRedRight(Node p) {
        flipColor(p);
        if (isRed(p.left.left)) {
            p = rotateRight(p);
            flipColor(p);
        }
        return p;
    }

    public V delete(K key) {
        V val = get(key);
        if (val == null) {
            return null;
        }

        if (isRed(root.left) && isRed(root.right)) {             
            root.color = RED;
        } // 注意！若 root 的左右儿子均为 2-node，
          // 则要先将 root 视为一个从 3-node/4-node 下来的节点
        root = deleteHelper(key, root);
        size -= 1;
        if (size != 0) {
            root.color = BLACK;
        }
        return val;
    }

    private Node deleteHelper(K key, Node p) {
        if (key.compareTo(p.key) < 0) {
            if (!isRed(p.left) && ! isRed(p.left.left)) {
                p = moveRedLeft(p); // 保证可以向左找下去（在下降的过程中保持当前节点至少是 3-node）
            }
            p.left = deleteHelper(key, p.left);
        } 
        else { // delete 两种情况，一是要找的节点在最底下，直接删去；二是在中间，用右子树中的最小元替换后删除右子树中的最小元
            if (isRed(p.left)) { // 微妙的地方，保证当前节点的左二子不是红色的，
                                 // 这样在 moveRedRight 中（保持当前节点至少是 3-node 的方法）才不会出现一些奇怪的颜色错误
                                 // 类比上面 put 的过程，并没有这句话，那是因为 Left-Lean RBTree 的右儿子不可能是红色的
                p = rotateRight(p);
            }
            if (key.compareTo(p.key) == 0 && p.right == null) { // 节点在最底下
                return null;
            } // 以下，则节点不是在最底下，要继续往下找
            if (!isRed(p.right) && !isRed(p.right.left)) { // 保证可以向右找下去
                p = moveRedRight(p);
            }
            if (key.compareTo(p.key) == 0) { // 节点在中间
                p.key = min(p.right);
                p.val = getHelper(min(p.right), p.right);
                p.right = deleteMinHelper(p.right);
            } else { // 不然，递归地在右子树中找
                p.right = deleteHelper(key, p.right);
            }
        }
        return fixUp(p);
    }

    private K min(Node p) {
        if (p == null) {
            return null;
        }
        if (p.left == null) {
            return p.key;
        }
        return min(p.left);
    }

    public static void main(String args[]) {
        RBTree<Integer, Integer> rbTree = new RBTree<>();
        rbTree.put(4, 4);
        rbTree.put(9, 9);
        rbTree.put(5, 5);
        rbTree.put(7, 7);
        rbTree.put(6, 6);
        rbTree.delete(7);
        rbTree.delete(9);
        System.out.println(rbTree.get(6));
    }

}
