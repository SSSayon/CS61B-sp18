import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


public class RBTreeTest {
    public static void main(String[] args) {
        RBTree<Integer, Integer> rbTree = new RBTree<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();

        Random random = new Random();
        int numOperations = 10000;
        String log = "\n";

        for (int i = 0; i < numOperations; i++) {
            int key = random.nextInt(20);
            int operation = random.nextInt(3);

            switch (operation) {
                case 0:
                    int value = key;
                    rbTree.put(key, value);
                    treeMap.put(key, value);
                    log += "put(" + String.valueOf(key) + ")\n";
                    break;
                case 1:
                    if (treeMap.get(key) == null) break;
                    Integer rbTreeValue = rbTree.get(key);
                    Integer treeMapValue = treeMap.get(key);
                    log += "get(" + String.valueOf(key) + ")\n";
                    if (!isEqual(rbTreeValue, treeMapValue)) {
                        System.out.println("get error: key=" + key);
                        System.out.println("RBTree value: " + rbTreeValue);
                        System.out.println("TreeMap value: " + treeMapValue);
                        throw new RuntimeException(log);
                    }
                    break;
                case 2:
                    if (treeMap.get(key) == null) break;
                    Integer rbTreeDeletedValue = rbTree.delete(key);
                    Integer treeMapDeletedValue = treeMap.remove(key);
                    log += "delete(" + String.valueOf(key) + ")\n";
                    if (!isEqual(rbTreeDeletedValue, treeMapDeletedValue)) {
                        System.out.println("delete error: key=" + key);
                        System.out.println("Deleted RBTree value: " + rbTreeDeletedValue);
                        System.out.println("Deleted TreeMap value: " + treeMapDeletedValue);
                        throw new RuntimeException(log);
                    }
                    break;     
            }
        }

        System.out.println("RBTree size: " + rbTree.size());
        System.out.println("TreeMap size: " + treeMap.size());
    }

    private static boolean isEqual(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }
}