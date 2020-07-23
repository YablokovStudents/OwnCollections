package my.collections;

import java.util.*;

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
        return getNode(key) != null;
    }

    private Node<K, V> getNode(K key) {
        if (comparator instanceof EmbeddedComparator) {
            if (key == null) {
                throw new NullPointerException();
            } else if (!(key instanceof Comparable)) {
                throw new IllegalArgumentException();
            }
        }
        return getNodeByComparator(key);
    }

    private Node<K, V> getNodeByComparator(K key) {
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
            for (V valueFromMap : values()) {
                if (valueFromMap == null) {
                    return true;
                }
            }
        } else {
            for (V valueFromMap : values()) {
                if (value.equals(valueFromMap)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = getNode(key);
        return (entry == null) ? null : entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        if (comparator instanceof EmbeddedComparator) {
            if (key == null) {
                throw new NullPointerException();
            } else if (!(key instanceof Comparable)) {
                throw new IllegalArgumentException();
            }
        }
        return putValueByComparator(key, value);
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
        Node<K, V> deletingNode = getNode(key);
        if (deletingNode == null) {
            return null;
        }

        V deletedNodeValue = deletingNode.value;
        if (deletingNode.right != null) {
            replaceNodeBySmallestFromRightSide(deletingNode);
        } else if (deletingNode.left != null) {
            replaceNodeByGreatestFromLeftSide(deletingNode);
        } else {
            if (deletingNode.parent.right == deletingNode) {
                deletingNode.parent.right = null;
            } else {
                deletingNode.parent.left = null;
            }
            --size;
            clearNodeLinks(deletingNode);
        }
        return deletedNodeValue;
    }

    private void replaceNodeBySmallestFromRightSide(Node<K, V> deletingNode) {
        Node<K, V> smallestNodeFromRightSide = deletingNode.right;
        while (smallestNodeFromRightSide.left != null) {
            smallestNodeFromRightSide = smallestNodeFromRightSide.left;
        }

        if (smallestNodeFromRightSide.parent != deletingNode) {
            smallestNodeFromRightSide.parent.left = smallestNodeFromRightSide.right;
            if (smallestNodeFromRightSide.right != null) {
                smallestNodeFromRightSide.right.parent = smallestNodeFromRightSide.parent;
            }

            smallestNodeFromRightSide.right = deletingNode.right;
            deletingNode.right.parent = smallestNodeFromRightSide;
        }

        smallestNodeFromRightSide.parent = deletingNode.parent;
        if (deletingNode.parent != null) {
            if (deletingNode.parent.left == deletingNode) {
                deletingNode.parent.left = smallestNodeFromRightSide;
            } else {
                deletingNode.parent.right = smallestNodeFromRightSide;
            }
        } else {
            root = smallestNodeFromRightSide;
        }

        smallestNodeFromRightSide.left = deletingNode.left;
        if (deletingNode.left != null) {
            deletingNode.left.parent = smallestNodeFromRightSide;
        }

        --size;
        clearNodeLinks(deletingNode);
    }

    private void replaceNodeByGreatestFromLeftSide(Node<K, V> deletingNode) {
        Node<K, V> greatestNodeFromLeftSide = deletingNode.left;
        while (greatestNodeFromLeftSide.right != null) {
            greatestNodeFromLeftSide = greatestNodeFromLeftSide.right;
        }

        if (greatestNodeFromLeftSide.parent != deletingNode) {
            greatestNodeFromLeftSide.parent.right = greatestNodeFromLeftSide.left;
            if (greatestNodeFromLeftSide.left != null) {
                greatestNodeFromLeftSide.left.parent = greatestNodeFromLeftSide.parent;
            }

            greatestNodeFromLeftSide.left = deletingNode.left;
            deletingNode.left.parent = greatestNodeFromLeftSide;
        }

        greatestNodeFromLeftSide.parent = deletingNode.parent;
        if (deletingNode.parent != null) {
            if (deletingNode.parent.right == deletingNode) {
                deletingNode.parent.right = greatestNodeFromLeftSide;
            } else {
                deletingNode.parent.left = greatestNodeFromLeftSide;
            }
        } else {
            root = greatestNodeFromLeftSide;
        }

        greatestNodeFromLeftSide.right = deletingNode.right;
        if (deletingNode.right != null) {
            deletingNode.right.parent = greatestNodeFromLeftSide;
        }

        --size;
        clearNodeLinks(deletingNode);
    }

    @Override
    public void clear() {
        clearSubNodeLinks(root);
        size = 0;
    }

    private void clearSubNodeLinks(Node<K, V> node) {
        if (node == null) return;

        clearSubNodeLinks(node.left);
        clearSubNodeLinks(node.right);
        clearNodeLinks(node);
    }

    private void clearNodeLinks(Node<K, V> deletingNode) {
        deletingNode.parent = null;
        deletingNode.left = null;
        deletingNode.right = null;
        deletingNode.value = null;
    }

    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>(size);
        addSubNodeValues(collection, root);
        return collection;
    }

    private void addSubNodeValues(Collection<V> collection, Node<K, V> node) {
        if (node == null) return;

        addSubNodeValues(collection, node.left);
        collection.add(node.value);
        addSubNodeValues(collection, node.right);
    }

    @Override
    public Collection<K> keySet() {
        Collection<K> collection = new ArrayList<>(size);
        addSubNodeKeys(collection, root);
        return collection;
    }

    private void addSubNodeKeys(Collection<K> collection, Node<K, V> node) {
        if (node == null) return;

        addSubNodeKeys(collection, node.left);
        collection.add(node.key);
        addSubNodeKeys(collection, node.right);
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        Collection<Entry<K, V>> collection = new ArrayList<>(size);
        addSubNodes(collection, root);
        return collection;
    }

    private void addSubNodes(Collection<Entry<K, V>> collection, Node<K, V> node) {
        if (node == null) return;

        addSubNodes(collection, node.left);
        collection.add(node);
        addSubNodes(collection, node.right);
    }
}
