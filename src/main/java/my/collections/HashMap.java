package my.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

    private static class ComparatorForBucket<K> implements Comparator<K> {

        @Override
        public int compare(K o1, K o2) {
            if (o1 == null) {
                return (o2 == null) ? 0 : -1;
            } else if (o2 == null) {
                return 1;
            } else {
                if (o1.hashCode() == o2.hashCode()) {
                    if (o1.equals(o2)) return 0;
                    else return 1;
                } else return o1.hashCode() - o2.hashCode();
            }
        }
    }

    private static class HashTreeMap<K, V> extends TreeMap<K, V> {
        public HashTreeMap() {
            super(new ComparatorForBucket<>());
        }
        @Override
        public V get(K key) {
            TreeMap.Node<K,V> current = super.root;
            Comparator<K> comparator = super.comparator;
            return getValueByComparator(key, current, comparator);

        }

        private V getValueByComparator(K key, Node<K, V> current, Comparator<K> comparator) {
            Node<K, V> currentLeft = current.left;
            while (current != null) {
                int compareResult = comparator.compare(key, current.key);
                if (compareResult > 0) {
                    current = current.right;
                } else if (compareResult < 0) {
                    current = current.left;
                } else {
                    return current.value;
                }

            }
            return getValueByComparator(key,currentLeft,comparator);
        }
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

    private static class Bucket<K, V> {
        private Deque<Entry<K, V>> deque;
        private Map<K, V> map;

        private Bucket(Deque<Entry<K, V>> deque) {
            this.deque = deque;
        }

        private Bucket(Map<K, V> map) {
            this.map = map;
        }

        private boolean isQueue() {
            return deque != null;
        }

        private Deque<Entry<K, V>> asQueue() {
            return deque;
        }

        private boolean isMap() {
            return map != null;
        }

        private Map<K, V> asMap() {
            return map;
        }

    }

    private final float loadFactor;
    private int threshold;

    private Bucket<K, V>[] buckets;
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

        buckets = new Bucket[capacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new Bucket<>(new LinkedList<>());
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
    @SuppressWarnings("unchecked")
    public boolean containsKey(K key) {
        Bucket<K, V> bucket = buckets[getBucketIndex(key)];
        if (bucket.isQueue()) {
            return getNodeByKey(bucket.asQueue(), key) != null;
        } else return bucket.asMap().get(key) != null;
    }

    private int getBucketIndex(K key) {
        return getBucketIndex(key, buckets.length);
    }

    private int getBucketIndex(K key, int bucketLength) {
        if (key == null) {
            return 0;
        }
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
//        if (value == null) {
//            for (Deque<Node<K, V>> deque : buckets)
//                for (Node<K, V> node : deque) {
//                    if (node.value == null) {
//                        return true;
//                    }
//                }
//        } else {
//            for (Deque<Node<K, V>> deque : buckets) {
//                for (Node<K, V> node : deque) {
//                    if (value.equals(node.value)) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;

        for (Entry<K, V> node : entrySet()) {
            if (value == null) {
                if (node.getValue() == null) return true;
            } else if (value.equals(node.getValue())) return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        Bucket<K, V> bucket = buckets[getBucketIndex(key)];
        if (bucket.isQueue()) {
            Entry<K, V> node = getNodeByKey(bucket.asQueue(), key);
            return node != null ? node.getValue() : null;
        } else {
            return bucket.asMap().get(key);
        }

    }

    private Entry<K, V> getNodeByKey(Deque<Entry<K, V>> bucket, K key) {
        if (key == null) {
            for (Entry<K, V> node : bucket) {
                if (node.getKey() == null) {
                    return node;
                }
            }
        } else {
            for (Entry<K, V> node : bucket) {
                if (key.equals(node.getKey())) {
                    return node;
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
       System.out.println("RRRRRRRRRRRRR");
        int index = getBucketIndex(key);
        Bucket<K, V> bucket = buckets[index];
        System.out.println(key + " ключ " + " номер корзины " + index);
        if (bucket.isQueue()) {
            System.out.println(1111);
            Entry<K, V> foundNode = getNodeByKey(bucket.asQueue(), key);
            if (foundNode != null) {
                V oldValue = foundNode.getValue();
                foundNode.setValue(value);
                return oldValue;
            } else {
                bucket.asQueue().add(new Node<>(key, value));
                if (++size > threshold) {
                    increaseBucketNumber();
                }
                if (bucket.asQueue().size() > 8) {
                    System.out.println(1234);
                    Bucket<K, V> newBucket = new Bucket<>(new HashTreeMap<>());
                    for (Entry<K, V> kvEntry : bucket.asQueue()) {
                        newBucket.asMap().put(kvEntry.getKey(), kvEntry.getValue());
                    }
                    buckets[index] = newBucket;
                }
                return null;
            }
        } else {
            System.out.println(2222);
            bucket.asMap().put(key, value);
            return bucket.asMap().get(key);
        }
    }

    @SuppressWarnings("unchecked")
    private void increaseBucketNumber() {
        Bucket<K, V>[] newBuckets = new Bucket[buckets.length << 1];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = new Bucket<K, V>(new LinkedList<>());
        }
        for (Entry<K, V> entry : entrySet()) {
            Bucket<K, V> bucket = newBuckets[getBucketIndex(entry.getKey(), newBuckets.length)];
            if (bucket.isQueue()) bucket.asQueue().add(entry);
            else bucket.asMap().put(entry.getKey(), entry.getValue());
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
        Bucket<K, V> bucket = buckets[getBucketIndex(key)];
        if (bucket.isQueue()) {
            Entry<K, V> foundNode = getNodeByKey(bucket.asQueue(), key);
            if (foundNode == null) {
                return null;
            }

            V removedValue = foundNode.getValue();
            bucket.asQueue().remove(foundNode);
            --size;
            return removedValue;
        } else {
            V value = bucket.asMap().remove(key);
            if (bucket.asMap().size() < 6) {
                Bucket<K, V> newBucket = new Bucket<K, V>(new LinkedList<>());
                for (Entry<K, V> kvEntry : bucket.asMap().entrySet()) {
                    newBucket.asQueue().add(kvEntry);
                }

            }
            return value;
        }
    }

    @Override
    public void clear() {
        size = 0;
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.isQueue()) bucket.asQueue().clear();
            else bucket.asMap().clear();
        }
    }


    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>();
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.asQueue() instanceof java.util.Deque) {
                for (Entry<K, V> node : bucket.asQueue())
                    collection.add(node.getValue());
            } else {
                for (Entry<K, V> node : bucket.asMap().entrySet())
                    collection.add(node.getValue());
            }
        }
        return collection;
    }


    @Override
    public Collection<K> keySet() {
        Collection<K> collection = new ArrayList<>(size);
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.asQueue() instanceof java.util.Deque) {
                for (Entry<K, V> node : bucket.asQueue())
                    collection.add(node.getKey());
            } else {
                for (Entry<K, V> node : bucket.asMap().entrySet())
                    collection.add(node.getKey());
            }
        }
        return collection;
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> collection = new ArrayList<>(size);
        for (Bucket<K, V> bucket : buckets) {
            if (bucket.isQueue()) {
                for (Entry<K, V> node : bucket.asQueue())
                    collection.add(node);
            } else {
                for (Entry<K, V> node : bucket.asMap().entrySet())
                    collection.add(node);
            }
        }
        return collection;
    }
}