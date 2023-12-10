package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    public ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        return buckets[index].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (!buckets[index].containsKey(key)) {
            size += 1;
        }
        buckets[index].put(key, value);
        
        if (loadFactor() > MAX_LF) {
            MyHashMap<K, V> _hashMap = new MyHashMap<>();
            _hashMap.buckets = new ArrayMap[buckets.length * 2];
            _hashMap.clear();
            for (int i = 0; i < buckets.length; ++i) {
                for (K _key : buckets[i].keySet()) {
                        _hashMap.put(_key, buckets[i].get(_key));
                }
            }
            buckets = _hashMap.buckets;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < buckets.length; ++i) {
            if (buckets[i].size() > 0) {
                keys.addAll(buckets[i].keySet());
            }
        }
        return keys;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int index = hash(key);
        if (buckets[index].containsKey(key)) {
            size -= 1;
        }
        return buckets[index].remove(key);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int index = hash(key);
        if (buckets[index].get(key).equals(value)) {
            size -= 1;
        }
        return buckets[index].remove(key, value);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }



    // public static void main(String[] args) {
    //     MyHashMap<String, Integer> map = new MyHashMap<>();

    //     for (int i = 0; i < 20; ++i) {
    //         map.put("a" + String.valueOf(i), i);
    //     }

    //     System.out.println(map.get("a2"));

    //     System.out.println(map.size());
    //     System.out.println(map.buckets.length);

    //     Set<String> keys = map.keySet();
    //     System.out.println(keys);
    // }
}
