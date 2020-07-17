package my.collections;

import java.util.LinkedList;
import java.util.Queue;

public class TreeMap<K, V> implements Map<K, V> {
    public int size;
    public DataImpl<K, V> root;

    public TreeMap() {
    }

    public static class DataImpl<Integer, V> implements Entry<Integer, V>, Comparable<Entry<Integer, V>> {
        private Integer Key;
        private V value;
        private DataImpl<Integer, V> parent;
        private DataImpl<Integer, V> leftChild;
        private DataImpl<Integer, V> rightChild;

        public DataImpl(Integer key, V value, DataImpl<Integer, V> parent) {
            Key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public Integer getKey() {
            return Key;
        }

        @Override
        public void setKey(Integer key) {
            this.Key = key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public int compareTo(Entry<Integer, V> o) {
            return java.lang.Integer.compare((java.lang.Integer) this.Key, (java.lang.Integer) o.getKey());
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
        if (root == null) return false;
        DataImpl<K, V> current = root;
        DataImpl<K, V> current1 = new DataImpl<K, V>((K) key, null, null);
        while (current.compareTo(current1) != 0) {
            if (current.compareTo(current1) > 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
            if (current == null) {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean containsValue(Object value) {
        if (root == null) return false;
        Queue<DataImpl<K, V>> queue = new LinkedList<>();
        if (root.leftChild != null) queue.add(root.leftChild);
        if (root.rightChild != null) queue.add(root.rightChild);
        DataImpl<K, V> current = queue.poll();
        do {
            if (value == null) {
                if (current.value == null) return true;
            }
            if (current.value.equals(value)) return true;
            if (current.leftChild != null) queue.add(current.leftChild);
            if (current.rightChild != null) queue.add(current.rightChild);
        }
        while ((current = queue.poll()) != null);
        return false;
    }

    @Override
    public Object get(Object key) {
        if (key == null) throw new NullPointerException();
        if (root == null) return null;
        DataImpl<K, V> current = root;
        DataImpl<K, V> current1 = new DataImpl<K, V>((K) key, null, null);
        while (current.compareTo(current1) != 0) {
            if (current.compareTo(current1) > 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
            if (current == null) {
                return null;
            }
        }
        return current.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(Object key, Object value) {
        if (key == null) throw new NullPointerException();
        DataImpl<K, V> node = new DataImpl<K, V>((K) key, (V) value, null);
        if (root == null) {
            root = node;
            size++;
            return null;
        }
        DataImpl<K, V> current = root;
        DataImpl<K, V> prev = null;
        while (true) {
            prev = current;
            if (prev.compareTo(node) > 0) {
                current = current.leftChild;
                if (current == null) {
                    prev.leftChild = node;
                    node.parent = prev;
                    size++;
                    return null;
                }
            }
            if (prev.compareTo(node) < 0) {
                current = current.rightChild;
                if (current == null) {
                    prev.rightChild = node;
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
        DataImpl<K, V> node = new DataImpl<K, V>((K) key, (V) null, null);
        if (root.compareTo(node) == 0) {
            node = root;
            clear();
            return node.value;
        }
        DataImpl<K, V> current = root;
        DataImpl<K, V> prev = null;
        while (true) {
            prev = current;
            if (prev.compareTo(node) > 0) {
                current = current.leftChild;
                if (current == null) {
                    return null;
                }
            } else if (prev.compareTo(node) < 0) {
                current = current.rightChild;
                if (current == null) {
                    return null;
                }
            } else {
                if (prev.parent.rightChild != null && prev.parent.rightChild.compareTo(prev) == 0) {
                    prev.parent.rightChild = null;
                    size--;
                }
                if (prev.parent.leftChild != null && prev.parent.leftChild.compareTo(prev) == 0) {
                    prev.parent.leftChild = null;
                    size--;
                }
                Queue<DataImpl<K, V>> queue = new LinkedList<>();
                if (prev.leftChild != null) queue.add(prev.leftChild);
                if (prev.rightChild != null) queue.add(prev.rightChild);
                DataImpl<K, V> addElement = queue.poll();
                if (addElement != null) {
                    DataImpl<K, V> addCurrent = null;
                    DataImpl<K, V> prev1 = null;
                    do {
                        if (addElement.leftChild != null) queue.add(addElement.leftChild);
                        if (addElement.rightChild != null) queue.add(addElement.rightChild);
                        addCurrent = addElement.parent.parent;
                        prev1 = null;
                        while (true) {
                            prev1 = addCurrent;
                            if (prev1.compareTo(addElement) > 0) {
                                addCurrent = addCurrent.leftChild;
                                if (addCurrent == null) {
                                    prev1.leftChild = addElement;
                                    addElement.parent = prev1;
                                    break;
                                }
                            } else if (prev1.compareTo(addElement) < 0) {
                                // System.out.println(33);
                                addCurrent = addCurrent.rightChild;
                                if (addCurrent == null) {
                                    prev1.rightChild = addElement;
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
        Queue<DataImpl<K, V>> queue = new LinkedList<>();
        if (root.leftChild != null) queue.add(root.leftChild);
        if (root.rightChild != null) queue.add(root.rightChild);
        DataImpl<K, V> current = queue.poll();
        do {
            values.add(current.value);
            if (current.leftChild != null) queue.add(current.leftChild);
            if (current.rightChild != null) queue.add(current.rightChild);
        }
        while ((current = queue.poll()) != null);
        return values;
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> values = new ArrayList<>();
        if (isEmpty()) return values;
        values.add(root.getKey());
        Queue<DataImpl<K, V>> queue = new LinkedList<>();
        if (root.leftChild != null) queue.add(root.leftChild);
        if (root.rightChild != null) queue.add(root.rightChild);
        DataImpl<K, V> current = queue.poll();
        do {
            values.add(current.getKey());
            if (current.leftChild != null) queue.add(current.leftChild);
            if (current.rightChild != null) queue.add(current.rightChild);
        }
        while ((current = queue.poll()) != null);
        return values;

    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> values = new ArrayList<>();
        if (isEmpty()) return values;
        values.add(root);
        Queue<DataImpl<K, V>> queue = new LinkedList<>();
        if (root.leftChild != null) queue.add(root.leftChild);
        if (root.rightChild != null) queue.add(root.rightChild);
        DataImpl<K, V> current = queue.poll();
        do {
            values.add(current);
            if (current.leftChild != null) queue.add(current.leftChild);
            if (current.rightChild != null) queue.add(current.rightChild);
        }
        while ((current = queue.poll()) != null);
        return values;
    }
}
