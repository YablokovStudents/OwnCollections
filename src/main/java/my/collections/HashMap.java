package my.collections;

import java.util.Arrays;

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
            this.value = value;
        }
    }

    private final float loadFactor;
    private int threshold;

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
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        if (initialCapacity == 0) {
            return 1;
        }

        int result = initialCapacity - 1;
        result |= result >>> 1;  // получаем 2 старших единичных бита
        result |= result >>> 2;  // получаем 4 старших единичных бита
        result |= result >>> 4;  // получаем 8 старших единичных бита
        result |= result >>> 8;  // получаем 16 старших единичных бита
        result |= result >>> 16; // получаем 32 старших единичных бита
        return result + 1;
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
        return getNodeByKey(key) != null;
    }

    private int getBucketIndex(K key) {
        return getBucketIndex(key, buckets.length);
    }

    private int getBucketIndex(K key, int bucketLength) {
        return calculateHashCode(key, bucketLength) & (bucketLength - 1);
    }

    /**
     * @return число, младшие n бит которого попарно связаны с битами из более старших групп,
     * причем n == количеству бит, которые могут использоваться при кодировании индекса в рамках массива корзин.
     */
    private int calculateHashCode(K key, int bucketLength) {
        if (bucketLength == 1) {
            return key.hashCode();
        }

        int originalHashCode = key.hashCode();
        int result = originalHashCode;
        int powerOfTwo = Arrays.binarySearch(POWERS_OF_TWO, bucketLength);
        for (int i = 1; i < (32 / powerOfTwo); i++) {
            result ^= (originalHashCode >>>= powerOfTwo);
        }
        result ^= originalHashCode >>> powerOfTwo; // имеет смысл лишь для случая: if (32 % powerOfTwo != 0)
        return result;
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
        Node<K, V> foundNode = getNodeByKey(key);
        return (foundNode == null) ? null : foundNode.value;
    }

    private Node<K, V> getNodeByKey(K key) {
        if (key == null) {
            for (Node<K, V> node : buckets[0]) {
                if (node.key == null) {
                    return node;
                }
            }
        } else {
            for (Node<K, V> node : buckets[getBucketIndex(key)]) {
                if (key.equals(node.key)) {
                    return node;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> foundNode = getNodeByKey(key);
        if (foundNode != null) {
            V oldValue = foundNode.getValue();
            foundNode.setValue(value);
            return oldValue;
        } else {
            buckets[getBucketIndex(key)].add(new Node<>(key, value));
            if (++size > threshold) {
                increaseBucketNumber();
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void increaseBucketNumber() {
        LinkedList<Node<K, V>>[] newBuckets = new LinkedList[buckets.length << 1];
        for (Entry<K, V> entry : entrySet()) {
            newBuckets[getBucketIndex(entry.getKey(), newBuckets.length)].add((Node<K, V>) entry);
        }
        buckets = newBuckets;
        threshold = calculateNewThreshold();
    }

    private int calculateNewThreshold() {
        if (buckets.length == MAX_CAPACITY) {
            return Integer.MAX_VALUE;
        } else {
            return (int) (buckets.length * loadFactor);
        }
    }

    @Override
    public V remove(K key) {
        Node<K, V> foundNode = getNodeByKey(key);
        if (foundNode == null) {
            return null;
        }

        V removedValue = foundNode.getValue();
        Deque<Node<K, V>> bucket = buckets[getBucketIndex(key)];
        bucket.remove(foundNode);
        return removedValue;
    }

    @Override
    public void clear() {
        size = 0;
        for (Deque<Node<K, V>> bucket : buckets) {
            bucket.clear();
        }
    }


    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>();
        for (Deque<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                collection.add(node.value);
            }
        }
        return collection;
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> collection = new ArrayList<>(size);
        for (Deque<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                collection.add(node.key);
            }
        }
        return collection;
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> collection = new ArrayList<>(size);
        for (Deque<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                collection.add(node);
            }
        }
        return collection;
    }
}
