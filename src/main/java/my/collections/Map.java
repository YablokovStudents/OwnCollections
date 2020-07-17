package my.collections;

public interface Map<K, V> {
    int size();
    boolean isEmpty();
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    Object get(Object key);
    Object put(Object key, Object value);
    Object remove(Object key);
    void clear();
    Collection<V> values();
    Collection<K> keySet();
    Collection<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();
        void setKey(K key);

        V getValue();
        void setValue(V value);
    }

    interface Data<K,V>{


    }
}
