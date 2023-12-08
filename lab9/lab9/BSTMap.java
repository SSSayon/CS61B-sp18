package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node parent;

        private Node(K k, V v, Node pa) {
            key = k;
            value = v;
            parent = pa;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) { 
            return null;
        }
        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            return p.value;
        } else if (cmp > 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p, Node parent) {
        if (p == null) {
            size += 1;
            return new Node(key, value, parent);
        }
        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            p.value = value;
        } else if (cmp > 0) {
            p.left = putHelper(key, value, p.left, p);
        } else {
            p.right = putHelper(key, value, p.right, p);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root, null);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void keySetHelper(Node p, Set<K> set) {
        if (p == null) {
            return;
        }
        set.add(p.key);
        keySetHelper(p.left, set);
        keySetHelper(p.right, set);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        keySetHelper(root, set);
        return set;
    }

    private Node removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeHelper(key, p.left);
        } else if (cmp > 0) {
            p.right = removeHelper(key, p.right);
        } else { // remove 当前节点 p
            if (p.left == null) {
                return p.right;
            } else if (p.right == null) {
                return p.left;
            } else { // p 左右儿子都有，找到继位者，再递归删掉继位者原来所在位置
                Node succ = findMin(p.right);
                p.key = succ.key;
                p.value = succ.value;
                p.right = removeMin(p.right);
            }
        }
        return p;
    }

    private Node findMin(Node p) {
        if (p.left == null) {
            return p;
        }
        return findMin(p.left);
    }

    private Node removeMin(Node p) {
        if (p.left == null) {
            return p.right;
        }
        p.left = removeMin(p.left);
        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        V val = get(key);
        if (val == null) {
            return null;
        }
        root = removeHelper(key, root);
        size -= 1;
        return val;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V val = get(key);
        if (val == null || !val.equals(value)) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    private class BSTIterator implements Iterator<K> {
        private Node cur;

        public BSTIterator() {
            cur = findMin(root);
        }

        public boolean hasNext() {
            return (cur != null);
        }

        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            K key = cur.key;
            cur = findSuccessor(cur);
            return key;
        }

        private Node findSuccessor(Node p) {
            if (p.right != null) {
                return findMin(p.right);
            }
            Node parent = p.parent;
            while (parent != null && parent.right == p) { // iter 到最后一个后，自然 parent 会等于 null
                p = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    // public static void main(String[] args) {
    //     BSTMap<Integer, String> map = new BSTMap<>();

    //     map.put(3, "Three");
    //     map.put(1, "One");
    //     map.put(2, "Two");

    //     System.out.println("Initial map: " + map);
    //     System.out.print("Iterator output: ");
    //     for (Integer key : map) {
    //         System.out.print(key + " ");
    //     }
    //     System.out.println();

    //     map.remove(2);

    //     System.out.println("Map after removing key 2: " + map);
    //     System.out.print("Iterator output: ");
    //     for (Integer key : map) {
    //         System.out.print(key + " ");
    //     }
    //     System.out.println();

    //     map.put(4, "Four");

    //     System.out.println("Map after adding key 4: " + map);
    //     System.out.print("Iterator output: ");
    //     for (Integer key : map) {
    //         System.out.print(key + " ");
    //     }
    //     System.out.println();
    // }
}
