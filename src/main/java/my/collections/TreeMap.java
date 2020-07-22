package my.collections;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class TreeMap<K, V> implements Map<K, V> {
    private int size;
    private Node<K, V> root;
    private final Comparator<K> comparator;

    private static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
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

    private static class EmbeddedComparator<K extends Comparable<K>> implements Comparator<K> {
        @Override
        public int compare(K key1, K key2) {
            return key1.compareTo(key2);
        }
    }

    @SuppressWarnings("unchecked")
    public TreeMap() {
        this((Comparator<K>) new EmbeddedComparator<>());
    }

    public TreeMap(Comparator<K> comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
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
        return getEntry(key) != null;
    }

    private Entry<K, V> getEntry(K key) {
        if (!(comparator instanceof EmbeddedComparator)) {
            return getEntryByComparator(key);
        } else if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Comparable)) {
            throw new IllegalArgumentException();
        } else {
            return getEntryByComparator(key);
        }
    }

    private Entry<K, V> getEntryByComparator(K key) {
        Node<K, V> current = root;
        while (current != null) {
            int compareResult = comparator.compare(key, current.key);
            if (compareResult > 0) {
                current = current.right;
            } else if (compareResult < 0) {
                current = current.left;
            } else {
                return current;
            }
        }
        return null;
    }

    @Override
    public boolean containsValue(V value) {
        if (value == null) {
            for (Entry<K, V> entry : entrySet()) {
                if (entry.getValue() == null) {
                    return true;
                }
            }
        } else {
            for (Entry<K, V> entry : entrySet()) {
                if (value.equals(entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        return (entry == null) ? null : entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        if (!(comparator instanceof EmbeddedComparator)) {
            return putValueByComparator(key, value);
        } else if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Comparable)) {
            throw new IllegalArgumentException();
        } else {
            return putValueByComparator(key, value);
        }
    }

    private V putValueByComparator(K key, V value) {
        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
            return null;
        }

        Node<K, V> current = root;
        while (true) {
            int compareResult = comparator.compare(key, current.key);
            if (compareResult > 0) {
                if (current.right == null) {
                    current.right = new Node<>(key, value, current);
                    size++;
                    return null;
                } else {
                    current = current.right;
                }
            } else if (compareResult < 0) {
                if (current.left == null) {
                    current.left = new Node<>(key, value, current);
                    size++;
                    return null;
                } else {
                    current = current.left;
                }
            } else {
                V oldValue = current.value;
                current.setValue(value);
                return oldValue;
            }
        }
    }

    @Override
    public V remove(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }

        // ToDo: заменить реализацию обговоренным вариантом
        if (root == null) return null;
        Node<K, V> parent = null;
        Node<K, V> current = (Node<K, V>) entry;
        if (current.right != null) {
            current = current.right;
            while (true) {
                parent = current;
                current = current.left;
                if (current == null) {
                    if (((Node<K, V>) entry).parent.right.equals(entry)) ((Node<K, V>) entry).parent.right = parent;
                    if (((Node<K, V>) entry).parent.left.equals(entry)) ((Node<K, V>) entry).parent.left = parent;
                    parent.parent = ((Node<K, V>) entry).parent;
                    return entry.getValue();
                }
            }
        } else if (current.left != null) {
            current = current.left;
            while (true) {
                parent = current;
                current = current.right;
                if (current == null) {
                    if (((Node<K, V>) entry).parent.right.equals(entry)) ((Node<K, V>) entry).parent.right = parent;
                    if (((Node<K, V>) entry).parent.left.equals(entry)) ((Node<K, V>) entry).parent.left = parent;
                    parent.parent = ((Node<K, V>) entry).parent;
                    return entry.getValue();
                }
            }

        } else if (((Node<K, V>) entry).parent.right.equals(entry)) {
            ((Node<K, V>) entry).parent.right = null;
            return ((Node<K, V>) entry).value;
        }
        if (((Node<K, V>) entry).parent.left.equals(entry)) {
            ((Node<K, V>) entry).parent.left = null;
            return ((Node<K, V>) entry).value;
        }
        return null;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        if (isEmpty()) return values;
        values.add(root.value);
        Queue<Node<K, V>> queue = new LinkedList<>();
        if (root.left != null) queue.add(root.left);
        if (root.right != null) queue.add(root.right);
        Node<K, V> current = queue.poll();
        do {
            values.add(current.value);
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        while ((current = queue.poll()) != null);
        return values;
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> values = new ArrayList<>();
        if (isEmpty()) return values;
        values.add(root.getKey());
        Queue<Node<K, V>> queue = new LinkedList<>();
        if (root.left != null) queue.add(root.left);
        if (root.right != null) queue.add(root.right);
        Node<K, V> current = queue.poll();
        do {
            values.add(current.getKey());
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        while ((current = queue.poll()) != null);
        return values;

    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> values = new ArrayList<>();
        if (isEmpty()) return values;
        values.add(root);
        Queue<Node<K, V>> queue = new LinkedList<>();
        if (root.left != null) queue.add(root.left);
        if (root.right != null) queue.add(root.right);
        Node<K, V> current = queue.poll();
        do {
            values.add(current);
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        while ((current = queue.poll()) != null);
        return values;
    }
}
