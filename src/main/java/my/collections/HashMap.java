package my.collections;

import java.util.Collections;

public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private static final int MAX_CAPACITY = 1 << 30; // 2^31

    private static final int TREEIFY_THRESHOLD = 8;
    private static final int UNTREEIFY_THRESHOLD = 6;

    private static final int[] POWERS_OF_TWO = getPowersOfTwo();

    private static int[] getPowersOfTwo() {
        int[] result = new int[31];
        int powerOfTwo = 1;
        result[0] = powerOfTwo;
        for (int i = 1; i < result.length; ++i) {
            result[i] = (powerOfTwo <<= 1);
        }
        return result;
    }

    private static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {

        }
    }

    private int threshold;
    private final float loadFactor;

    private Deque<Node<K, V>>[] buckets;
    private int size;

    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        int capacity = calculateCapacityByBinaryShift(initialCapacity);

        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);

        buckets = new LinkedList[capacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int calculateCapacityByBinaryShift(int initialCapacity) {
        int result = initialCapacity;
        result |= result >>> 1; // 2 единичных бита
        result |= result >>> 2; // 4 единичных бита
        result |= result >>> 4; // 8 единичных бита
        result |= result >>> 8; // 16 единичных бита
        result |= result >>> 16; // 32 единичных бита
        return result;
    }

    private int calculateCapacityByBinarySearch(int initialCapacity) {
        int itemIndex = binarySearch(initialCapacity, POWERS_OF_TWO, 0, POWERS_OF_TWO.length - 1);
        return POWERS_OF_TWO[itemIndex];
    }

    private int binarySearch(int value, int[] array, int leftIndex, int rightIndex) {
        if (leftIndex == rightIndex) {
            int item = array[leftIndex];
            if (item == value) {
                return leftIndex;
            } else if (item < value) {
                return leftIndex;
            } else {
                return (leftIndex == array.length - 1) ? leftIndex : leftIndex + 1;
            }
        }

        int middleIndex = leftIndex + (rightIndex - leftIndex) >>> 1;
        int arrayItem = array[middleIndex];
        if (value == arrayItem) {
            return middleIndex;
        } else if (value < arrayItem) {
            return binarySearch(value, array, leftIndex, middleIndex - 1);
        } else {
            return binarySearch(value, array, middleIndex + 1, rightIndex);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            for (Node<K, V> node : buckets[0])
                if (node.key == null) {
                    return true;
                }
        } else {
            for (Node<K, V> node : buckets[getBucketIndex(key)]) {
                if (key.equals(node.key)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getBucketIndex(K key) {
        return key.hashCode() % buckets.length;
    }

    @Override
    public boolean containsValue(V value) {
        if (value == null) {
            for (Deque<Node<K, V>> deque : buckets)
                for (Node<K, V> node : deque) {
                    if (node.value == null) {
                        return true;
                    }
                }
        } else {
            for (Deque<Node<K, V>> deque : buckets) {
                for (Node<K, V> node : deque) {
                    if (value.equals(node.value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            for (Node<K, V> node : buckets[0]) {
                if (node.key == null) {
                    return node.value;
                }
            }
        } else {
            for (Node<K, V> node : buckets[getBucketIndex(key)]) {
                if (key.equals(node.key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        movingToBuckets();
        boolean add = false;
        V value1 = null;
        if (key == null) {
            for (Node<K, V> current : buckets[0]) {
                if (current.key == null) {
                    value1 = current.value;
                    current.value = (V) value;
                    size++;
                    add = true;
                }
            }
            if (!add) {
                buckets[0].add(new Node<>(null, value));
                size++;
            }
        } else {
            for (Node<K, V> current : buckets[getBucketIndex(key)]) {
                if (key.equals(current.key)) {
                    value1 = current.value;
                    current.value = value;
                    size++;
                    add = true;
                }
            }
            if (!add) {
                buckets[getBucketIndex(key)].add(new Node<>(key, value));
                size++;
            }
        }
        return value1;
    }

    @SuppressWarnings("unchecked")
    private void movingToBuckets() {
        if (size >= threshold) {
            increaseThreshold();
            LinkedList<Node<K, V>>[] buckets1 = new LinkedList[threshold];
            for (Entry<K, V> pair : entrySet())
                buckets1[pair.getKey().hashCode() % buckets1.length].add((Node<K, V>) pair);
            buckets = buckets1;
        }
    }

    private void increaseThreshold() {
        if (buckets.length == MAX_CAPACITY) {
            threshold = Integer.MAX_VALUE;
        } else {
            threshold <<= 1;
        }
    }

    @Override
    public Object remove(Object key) {
        Object value1 = null;
        LinkedList<Node<K, V>> current;
        if (key == null) {
            current = (LinkedList<Node<K, V>>) buckets[0];
            for (int i = 0; i < current.size(); i++)
                if (current.get(i).key == null) {
                    value1 = current.get(i).value;
                    current.remove(current.get(i));
                    size--;
                }
        } else {
            current = (LinkedList<Node<K, V>>) buckets[getBucketIndex(key)];
            for (int i = 0; i < current.size(); i++)
                if (key.equals(current.get(i).key)) {
                    value1 = current.get(i).value;
                    current.remove(current.get(i));
                    size--;
                }
        }
        return value1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        buckets = new LinkedList[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }


    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<V>();
        for (Deque<Node<K, V>> linkedList : buckets)
            for (Node<K, V> entry : linkedList)
                collection.add(entry.value);
        return collection;
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> collection = new ArrayList<>();
        for (Deque<Node<K, V>> linkedList : buckets)
            for (Node<K, V> entry : linkedList)
                collection.add(entry.key);
        return collection;
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> collection = new ArrayList<>();
        for (Deque<Node<K, V>> linkedList : buckets) {
            for (Node<K, V> current : linkedList)
                collection.add(current);
        }
        return collection;
    }
}
