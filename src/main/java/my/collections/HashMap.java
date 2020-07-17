package my.collections;

public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75F;

    private final float loadFactor;
    private int threshold;

    private Deque<EntryImpl<K, V>>[] buckets;
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
        threshold = initialCapacity;

        buckets = new LinkedList[initialCapacity];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public static class EntryImpl<K, V> implements Entry<K, V> {
        private K key;
        private V value;

        public EntryImpl(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public void setKey(K key) {

        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {

        }
    }
    @SuppressWarnings("unchecked")
    public void movingToBackets() {
        if (size >= loadFactor * threshold) {
            LinkedList<EntryImpl<K, V>>[] buckets1 = new LinkedList[threshold*=2];
            for (Entry<K, V> pair : entrySet())
                buckets1[pair.getKey().hashCode() % buckets1.length].add((EntryImpl<K, V>) pair);
            buckets = buckets1;
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
    public boolean containsKey(Object key) {
        if (key == null) {
            for (EntryImpl<K, V> current : buckets[0])
                if (current.key == null) return true;
        } else {
            for (EntryImpl<K, V> current : buckets[key.hashCode() % buckets.length]) {
                if (key.equals(current.key)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            for (Deque<EntryImpl<K, V>> deque : buckets)
                for (EntryImpl<K, V> current : deque)
                if (current.value == null) return true;
        }
        for (Deque<EntryImpl<K, V>> deque : buckets) {
            for (EntryImpl<K, V> current : deque)
            if (value.equals(current.value)) return true;
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            for (EntryImpl<K, V> current : buckets[0])
                if (current.key == null) return current.value;
        }
        for (EntryImpl<K, V> current : buckets[key.hashCode() % buckets.length]) {
            if (key.equals(current.key)) return current.value;
        }
        return null;
    }

    @Override
    public V put(Object key, Object value) {
        movingToBackets();
        boolean add = false;
        V value1 = null;
        if (key == null) {
            for (EntryImpl<K, V> current : buckets[0])
                if (current.key == null) {
                    value1 = current.value;
                    current.value = (V) value;
                    size++;
                    add = true;
                }
            if (!add) {
                buckets[0].add(new EntryImpl<K, V>(null, (V) value));
                size++;
            }
        } else {
            for (EntryImpl<K,V> current : buckets[key.hashCode() % buckets.length]) {
                if (key.equals(current.key)) {
                    value1 = (V) current.value;
                    current.value = (V) value;
                    size++;
                    add = true;
                }
            }
            if (!add) {
                buckets[key.hashCode() % buckets.length].add(new EntryImpl<K, V>((K)key, (V)value));
                size++;
            }
        }
        return value1;
    }

    @Override
    public Object remove(Object key) {
        Object value1 = null;
        LinkedList<EntryImpl<K, V>> current;
        if (key == null) {
            current = (LinkedList<EntryImpl<K, V>>) buckets[0];
            for (int i = 0; i < current.size(); i++)
                if (current.get(i).key == null) {
                    value1 = current.get(i).value;
                    current.remove(current.get(i));
                    size--;
                }
        } else {
            current = (LinkedList<EntryImpl<K, V>>) buckets[key.hashCode() % buckets.length];
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
        for (Deque<EntryImpl<K, V>> linkedList : buckets)
            for (EntryImpl<K, V> entry : linkedList)
                collection.add(entry.value);
        return collection;
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> collection = new ArrayList<>();
        for (Deque<EntryImpl<K, V>> linkedList : buckets)
            for (EntryImpl<K, V> entry : linkedList)
                collection.add(entry.key);
        return collection;
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> collection = new ArrayList<>();
        for (Deque<EntryImpl<K, V>> linkedList : buckets) {
            for (EntryImpl<K, V> current : linkedList)
                collection.add(current);
        }
        return collection;
    }
}
