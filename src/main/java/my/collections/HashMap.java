package my.collections;

import java.util.Arrays;
import java.util.Comparator;

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

    private static class EmbeddedTreeMap<K, V> extends TreeMap<K, V> {
        private enum CompareResult {
            LOWER(-1),
            EQUALS(0),
            GREATER(1),
            MAYBE_GREATER_OR_LOWER(2);

            private final int value;

            CompareResult(int value) {
                this.value = value;
            }

            private static CompareResult valueFrom(int value) {
                for (CompareResult compareResult : values()) {
                    if (compareResult.value == value) {
                        return compareResult;
                    }
                }
                throw new IllegalArgumentException();
            }
        }

        private EmbeddedTreeMap() {
            super(new HashCodeBasedComparator<>());
        }

        private static class HashCodeBasedComparator<K> implements Comparator<K> {
            @Override
            public int compare(K key1, K key2) {
                if (key1 == null) {
                    return ((key2 == null) ? CompareResult.EQUALS : CompareResult.LOWER).value;
                }
                if (key2 == null) {
                    return CompareResult.GREATER.value;
                }
                if (key1.hashCode() == key2.hashCode()) {
                    return (key1.equals(key2) ? CompareResult.EQUALS : CompareResult.MAYBE_GREATER_OR_LOWER).value;
                }
                return ((key1.hashCode() < key2.hashCode()) ? CompareResult.LOWER : CompareResult.GREATER).value;
            }
        }

        @Override
        protected Node<K, V> getNode(K key) {
            return getNode(key, root);
        }

        private Node<K, V> getNode(K key, Node<K, V> currentNode) {
            if (currentNode == null)  {
                return null;
            }

            int compareResult = comparator.compare(key, currentNode.key);
            switch (CompareResult.valueFrom(compareResult)) {
                case EQUALS:
                    return currentNode;

                case LOWER:
                    return getNode(key, currentNode.left);

                case GREATER:
                    return getNode(key, currentNode.right);

                case MAYBE_GREATER_OR_LOWER: {
                    Node<K, V> foundNode = getNode(key, currentNode.right);
                    return (foundNode != null) ? foundNode : getNode(key, currentNode.left);
                }

                default:
                    throw new IllegalArgumentException();
            }
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
        for (V nodeValue : values()) {
            if (value == null) {
                if (nodeValue == null) {
                    return true;
                }
            } else if (value.equals(nodeValue)) {
                return true;
            }
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
            return putToQueueBucket(key, value, bucket.asQueue(), index);
        } else {
            System.out.println(2222);
            bucket.asMap().put(key, value);
            return bucket.asMap().get(key);
        }
    }

    private V putToQueueBucket(K key, V value, Deque<Entry<K, V>> bucket, int index) {
        System.out.println(1111);
        Entry<K, V> foundNode = getNodeByKey(bucket, key);
        if (foundNode != null) {
            V oldValue = foundNode.getValue();
            foundNode.setValue(value);
            return oldValue;
        } else {
            bucket.add(new Node<>(key, value));
            if (++size > threshold) {
                increaseBucketNumber();
            }
            if (bucket.size() >= TREEIFY_THRESHOLD) {
                System.out.println(1234);
                Bucket<K, V> newBucket = new Bucket<>(new EmbeddedTreeMap<>());
                for (Entry<K, V> kvEntry : bucket) {
                    newBucket.asMap().put(kvEntry.getKey(), kvEntry.getValue());
                }
                buckets[index] = newBucket;
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void increaseBucketNumber() {
        Bucket<K, V>[] newBuckets = new Bucket[buckets.length << 1];
        for (int i = 0; i < newBuckets.length; i++) {
            newBuckets[i] = new Bucket<>(new LinkedList<>());
        }
        for (Entry<K, V> entry : entrySet()) {
            Bucket<K, V> bucket = newBuckets[getBucketIndex(entry.getKey(), newBuckets.length)];
            if (bucket.isQueue()) {
                bucket.asQueue().add(entry);
                // проверять на превышение порога и превращать в TreeMap
            } else bucket.asMap().put(entry.getKey(), entry.getValue());
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
            if (bucket.asMap().size() <= UNTREEIFY_THRESHOLD) {
                Bucket<K, V> newBucket = new Bucket<>(new LinkedList<>());
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