package my.collections;

import java.util.LinkedList;
import java.util.*;

public class TreeMap<K, V> implements Map<K, V> {
    private int size;
    private Node<K, V> root;
    private Comparator<K> comparator;

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

    public TreeMap() {
    }

    public TreeMap(Comparator<K> comparator) {
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

    @SuppressWarnings("unchecked")
    private Entry<K, V> getEntry(K key) {
        if (comparator != null) {
            return getEntryByComparator(key);
        } else if (key == null) {
            throw new NullPointerException();
        } else if (!(key instanceof Comparable)) {
            throw new IllegalArgumentException();
        } else {
            return getEntryByComparableKey((Comparable<K>) key);
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

    private Entry<K, V> getEntryByComparableKey(Comparable<K> key) {
        Node<K, V> current = root;
        while (current != null) {
            int compareResult = key.compareTo(current.key);
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

    @SuppressWarnings("unchecked")
    @Override
    public V put(Object key, Object value) {
        if (key == null) throw new NullPointerException();
        Node<K, V> node = new Node<K, V>((K) key, (V) value, null);
        if (root == null) {
            root = node;
            size++;
            return null;
        }
        Node<K, V> current = root;
        Node<K, V> prev = null;
        while (true) {
            prev = current;
            if (prev.compareTo(node) > 0) {
                current = current.left;
                if (current == null) {
                    prev.left = node;
                    node.parent = prev;
                    size++;
                    return null;
                }
            }
            if (prev.compareTo(node) < 0) {
                current = current.right;
                if (current == null) {
                    prev.right = node;
                    node.parent = prev;
                    size++;
                    return null;
                }
            }
            if (prev.compareTo(node) == 0) {
                Object oldValue = null;
                oldValue = prev.value;
                prev.setValue((V) value);
                return (V) oldValue;
            }
        }
    }

    @Override
    public Object remove(Object key) {
        if (root == null) return null;
        Node<K, V> node = new Node<K, V>((K) key, (V) null, null);
        if (root.compareTo(node) == 0) {
            node = root;
            clear();
            return node.value;
        }
        Node<K, V> current = root;
        Node<K, V> prev = null;
        while (true) {
            prev = current;
            if (prev.compareTo(node) > 0) {
                current = current.left;
                if (current == null) {
                    return null;
                }
            } else if (prev.compareTo(node) < 0) {
                current = current.right;
                if (current == null) {
                    return null;
                }
            } else {
                if (prev.parent.right != null && prev.parent.right.compareTo(prev) == 0) {
                    prev.parent.right = null;
                    size--;
                }
                if (prev.parent.left != null && prev.parent.left.compareTo(prev) == 0) {
                    prev.parent.left = null;
                    size--;
                }
                Queue<Node<K, V>> queue = new LinkedList<>();
                if (prev.left != null) queue.add(prev.left);
                if (prev.right != null) queue.add(prev.right);
                Node<K, V> addElement = queue.poll();
                if (addElement != null) {
                    Node<K, V> addCurrent = null;
                    Node<K, V> prev1 = null;
                    do {
                        if (addElement.left != null) queue.add(addElement.left);
                        if (addElement.right != null) queue.add(addElement.right);
                        addCurrent = addElement.parent.parent;
                        prev1 = null;
                        while (true) {
                            prev1 = addCurrent;
                            if (prev1.compareTo(addElement) > 0) {
                                addCurrent = addCurrent.left;
                                if (addCurrent == null) {
                                    prev1.left = addElement;
                                    addElement.parent = prev1;
                                    break;
                                }
                            } else if (prev1.compareTo(addElement) < 0) {
                                // System.out.println(33);
                                addCurrent = addCurrent.right;
                                if (addCurrent == null) {
                                    prev1.right = addElement;
                                    addElement.parent = prev1;
                                    break;
                                }
                            }
                        }
                    }
                    while ((addElement = queue.poll()) != null);
                }
                return prev.value;
            }
        }
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
