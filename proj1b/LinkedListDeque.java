public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        public T item;
        public Node prev;
        public Node next;

        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        public Node(Node prev, Node next) {
            this.prev = prev;
            this.next = next;
        }
    }

    private int size;
    private Node node0;

    public LinkedListDeque() {
        node0 = new Node(null, null);
        node0.prev = node0;
        node0.next = node0;
        size = 0;
    }

    public void addFirst(T item) {
        Node node = new Node(item, node0, node0.next);
        node0.next = node;
        node.next.prev = node;
        size += 1;
    }

    public void addLast(T item) {
        Node node = new Node(item, node0.prev, node0);
        node.prev.next = node;
        node0.prev = node;
        size += 1;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = node0.next;
        while (p != node0) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T ret = node0.next.item;
        node0.next = node0.next.next;
        node0.next.prev = node0;
        size -= 1;
        return ret;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T ret = node0.prev.item;
        node0.prev = node0.prev.prev;
        node0.prev.next = node0;
        size -= 1;
        return ret;
    }

    public T get(int n) {
        if ((size == 0) || (n > size)) {
            return null;
        }
        Node p = node0;
        while (n >= 0) {
            p = p.next;
            n -= 1;
        }
        return p.item;
    }

    private T _getRecursiveHelper(int n, Node p) {
        if (n == 0) {
            return p.item;
        }
        return _getRecursiveHelper(n - 1, p.next);
    }

    public T getRecursive(int n) {
        if ((size == 0) || (n > size)) {
            return null;
        }
        return _getRecursiveHelper(n, node0.next);
    }

    // public void _Test() {
    // printDeque();
    // System.out.println();
    // System.out.println(size);
    // }

    // public static void main(String args[]) {
    // LinkedListDeque<Integer> list = new LinkedListDeque<>();
    // list.addFirst(1);
    // list.addFirst(0);
    // list.addLast(2);
    // list.addLast(3);
    // list._Test();
    // System.out.println(list.get(2) + " " + list.getRecursive(2));
    // }
}
