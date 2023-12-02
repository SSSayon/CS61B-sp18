public class ArrayDeque<T> implements Deque<T> {
    private T arr[];
    private int size;
    private int length;
    private int start;
    private int end;

    public ArrayDeque() {
        arr = (T[]) new Object[8];
        size = 0;
        length = 8;
        start = 0;
        end = 0;
    }

    private void resize(int new_length) {
        T[] new_arr = (T[]) new Object[new_length];
        int p = start;
        int new_end = 0;

        new_arr[new_end] = arr[p];
        p = addOne(p);
        new_end += 1; 

        while (p != end) {
            new_arr[new_end] = arr[p];
            p = addOne(p);
            new_end += 1;
        }
        length = new_length;
        start = 0;
        end = new_end;
        arr = new_arr;
    }

    private void resizeBigger() {
        resize(length * 2);
    }

    private void resizeSmaller() {
        resize(length / 2);
    }

    private int addOne(int index) {
        int ret = (index + 1) % length;
        if (ret < 0) {
            ret += length;
        }
        return ret;
    }

    private int minusOne(int index) {
        int ret = (index - 1) % length;
        if (ret < 0) {
            ret += length;
        }
        return ret;
    }

    public void addFirst(T item) {
        start = minusOne(start);
        arr[start] = item;
        size += 1;
        if (size == length) {
            resizeBigger();
        }
    }

    public void addLast(T item) {
        arr[end] = item;
        end = addOne(end);
        size += 1;
        if (size == length) {
            resizeBigger();
        }
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int p = start;
        while (p != end) {
            System.out.print(arr[p] + " ");
            p = addOne(p);
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T ret = arr[start];
        arr[start] = null;
        start = addOne(start);
        size -= 1;
        if ((size * 4 <= length) && (length >= 16)) {
            resizeSmaller();
        }
        return ret;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        end = minusOne(end);
        T ret = arr[end];
        arr[end] = null;
        size -= 1;
        if ((size * 4 <= length) && (length >= 16)) {
            resizeSmaller();
        }
        return ret;
    }

    public T get(int n) {
        if ((size == 0) || (n > size)) {
            return null;
        }
        int p = start;
        while (n > 0) {
            p = addOne(p);
            n -= 1;
        }
        return arr[p];
    }


    // public static void main(String args[]) {
    //     ArrayDeque<Integer> list = new ArrayDeque<>();
    //     for (int i = 0; i < 6; ++i) {
    //         list.addLast(i + 6);
    //     }
    //     for (int i = 0; i < 6; ++i) {
    //         list.addFirst(5 - i);
    //     }
    //     list.printDeque();
    //     System.out.println();

    //     for (int i = 0; i < 5; ++i) {
    //         list.removeFirst();
    //         list.removeLast();
    //     }
    //     list.printDeque();
    //     System.out.println();
    //     System.out.println(list.size());
    // }
}
