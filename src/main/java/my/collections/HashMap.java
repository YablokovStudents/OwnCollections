package my.collections;

public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private static final int MAX_CAPACITY = 1 << 30; // 2^31

    private static final int TREEIFY_THRESHOLD = 8;
    private static final int UNTREEIFY_THRESHOLD = 6;

    private int threshold;
    private final float loadFactor;

    private Deque<Node<K, V>>[] buckets;
    private int size;

    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public HashMap(int initialCapacity) {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        threshold = (int) (initialCapacity * loadFactor);

        buckets = new LinkedList[initialCapacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        public Node(K key, V value) {
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
            for (Node<K, V> current : buckets[0])
                if (current.key == null) {
                    value1 = current.value;
                    current.value = (V) value;
                    size++;
                    add = true;
                }
            if (!add) {
                buckets[0].add(new Node<K, V>(null, (V) value));
                size++;
            }
        } else {
            for (Node<K,V> current : buckets[getBucketIndex(key)]) {
                if (key.equals(current.key)) {
                    value1 = (V) current.value;
                    current.value = (V) value;
                    size++;
                    add = true;
                }
            }
            if (!add) {
                buckets[getBucketIndex(key)].add(new Node<K, V>((K)key, (V)value));
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
