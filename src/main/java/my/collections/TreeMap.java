package my.collections;

import java.util.Comparator;

public class TreeMap<K, V> implements Map<K, V> {
    private int size;
    private Node<K, V> root;
    private final Comparator<K> comparator;
    public static final int ITEM_SHIFT = 2;
    public static final int ITEM_WIDTH = 4;

    private static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> parent;
        private Node<K, V> left;
        private Node<K, V> right;
        private int height;
        private int balanceFactor;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            height = 0;
            balanceFactor = 0;
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

        public int getHeight() {
            return height;
        }
    }

    public Node<K, V> getRoot() {
        return root;
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
        System.out.println("=====================================");
        System.out.println("ВСТАВКА ключ " + key);
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
                    calculateHeight(current.right);
                    return null;
                } else {
                    current = current.right;
                }
            } else if (compareResult < 0) {
                if (current.left == null) {
                    current.left = new Node<>(key, value, current);
                    size++;
                    calculateHeight(current.left);
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

    public void calculateHeight(Node<K, V> node) {
        System.out.println(888);
        if (node != null) {
            Node<K, V> current = node;
            while (true) {
                int oldHeight = current.height;
                if (current.left != null && current.right != null) {
                    if (current.left.height > current.right.height) {
                        current.height = current.left.height + 1;
                        // if (current.height == oldHeight) return;
                        // else {
                        System.out.println(current.key + "  " + current.height);
                        System.out.println("добавилась высота от левой");
                        balanceFactorAndRotate(current);
                        // }
                    } else if (current.left.height < current.right.height) {
                        current.height = current.right.height + 1;
                        //  if (current.height == oldHeight) return;
                        //  else {
                        System.out.println(current.key + "  " + current.height);
                        System.out.println("добавилась высота от правой");
                        balanceFactorAndRotate(current);
                        //  }
                    } else {
                        current.height = current.left.height + 1;
                        System.out.println(current.key + "  " + current.height);
                        System.out.println("у детей равные высоты");
                    }
                } else if (current.left == null && current.right != null) {
                    current.height = current.right.height + 1;
                    // if (current.height == oldHeight) return;
                    // else {
                    System.out.println(current.key + "  " + current.height);
                    System.out.println("добавилась высота от правой левого нет");
                    balanceFactorAndRotate(current);
                    //  }
                } else if (current.left != null) {
                    current.height = current.left.height + 1;
                    // if (current.height == oldHeight) return;
                    // else {
                    System.out.println(current.key + "  " + current.height);
                    System.out.println("добавилась высота от левой правого нет");
                    balanceFactorAndRotate(current);
                    //  }
                } else current.height = 0;
                System.out.println(current.key + " ключ с высотой " + current.height + " переход выше" + current.balanceFactor + " баланс");
                if (current.parent != null) current = current.parent;
                else return;
            }
        }
    }

    public Node<K, V> calculateHeightForOneNode(Node<K, V> node) {
        if (node != null) {
            if (node.left != null && node.right != null) {
                if (node.left.height > node.right.height) {
                    node.height = node.left.height + 1;
                    System.out.println(node.key + "  " + node.height);
                    System.out.println("добавилась высота от левой");
                    balanceFactorAndRotate(node);
                } else if (node.left.height < node.right.height) {
                    node.height = node.right.height + 1;
                    System.out.println(node.key + "  " + node.height);
                    System.out.println("добавилась высота от правой");
                    balanceFactorAndRotate(node);
                } else {
                    node.height = node.left.height + 1;
                    System.out.println(node.key + "  " + node.height);
                    System.out.println("у детей равные высоты");
                }
            } else if (node.left == null && node.right != null) {
                node.height = node.right.height + 1;
                System.out.println(node.key + "  " + node.height);
                System.out.println("добавилась высота от правой левого нет");
                balanceFactorAndRotate(node);
            } else if (node.left != null) {
                node.height = node.left.height + 1;
                System.out.println(node.key + "  " + node.height);
                System.out.println("добавилась высота от левой правого нет высота левого сына " + node.left.key + " - " + node.left.height);
                balanceFactorAndRotate(node);
            } else node.height = 0;
            System.out.println(node.key + " ключ с высотойй " + node.height);
            if (node.parent != null) return node.parent;
        }
        return null;
    }

    private void balanceFactorAndRotate(Node<K, V> current) {
        System.out.println("1баланс фактор ключа " + current.key + " - " + current.balanceFactor);
        balanceFactor(current);
        System.out.println("баланс фактор ключа после подсчета" + current.key + " - " + current.balanceFactor);
        if (current.balanceFactor < -1 && current.right != null /*&& current.right.right != null*/ && balanceFactor(current.right) > 0) {
            System.out.println("большой левый поворот ключа " + current.key);
            Node<K, V> cr = current.right;
            rotateRight(current.right);
            calculateHeightForOneNode(cr);
            rotateLeft(current);
            calculateHeight(calculateHeightForOneNode(current));
        } else if (current.balanceFactor > 1 && current.left != null /*&& current.left.left != null*/ && balanceFactor(current.left) < 0) {
            System.out.println("большой правй поворот ключа " + current.key);
            Node<K, V> cl = current.left;
            rotateLeft(current.left);
            calculateHeightForOneNode(cl);
            rotateRight(current);
            calculateHeight(calculateHeightForOneNode(current));
        } else if (current.balanceFactor < -1) {
            rotateLeft(current);
            calculateHeight(calculateHeightForOneNode(current));
        } else if (current.balanceFactor > 1) {
            rotateRight(current);
            calculateHeight(calculateHeightForOneNode(current));
        } else if (current.balanceFactor == 0) System.out.println("баланс 0");
        else
            System.out.println(current.key + " КЛЮЧ с балансом " + current.balanceFactor/* + current.right.key + " правый " + current.left.key + " левый "*/);
        System.out.println("2баланс фактор ключа " + current.key + " - " + current.balanceFactor);
        if (current.right != null)
            System.out.println("дети правый key " + current.right.key + " высота " + current.right.height);
        if (current.left != null)
            System.out.println("левый ребенок " + current.left.key + " высота " + current.left.height);
        if (current.parent != null)
            System.out.println(" отец " + current.parent.key);

    }

    private int balanceFactor(Node<K, V> current) {
        if (current.left != null && current.right != null)
            current.balanceFactor = current.left.height - current.right.height;
        else if (current.left != null)
            current.balanceFactor = current.left.height;
        else if (current.right != null)
            current.balanceFactor = -current.right.height;
        else current.balanceFactor = 0;
        return current.balanceFactor;
    }

    @Override
    public V remove(K key) {
        Node<K, V> deletingNode = getNode(key);
        if (deletingNode == null) {
            return null;
        }
        V deletedNodeValue = deletingNode.value;
        if (deletingNode == root) {
            clear();
            return deletedNodeValue;
        }
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
        clearNodeLinks(deletingNode);
        return deletedNodeValue;
    }


    private void replaceNodeBySmallestFromRightSide(Node<K, V> deletingNode) {
        Node<K, V> smallestNodeFromRightSide = deletingNode.right;
        Node<K, V> smallestNodeFromRightSideParent = null;
        while (smallestNodeFromRightSide.left != null) {
            smallestNodeFromRightSide = smallestNodeFromRightSide.left;
        }
        Node<K, V> current = smallestNodeFromRightSide.parent;
        if (smallestNodeFromRightSide.parent != deletingNode) {
            smallestNodeFromRightSideParent = smallestNodeFromRightSide.parent;
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
        if (smallestNodeFromRightSideParent != null) calculateHeight(smallestNodeFromRightSideParent);
        else calculateHeight(smallestNodeFromRightSide);
        --size;
    }

    private void replaceNodeByGreatestFromLeftSide(Node<K, V> deletingNode) {
        Node<K, V> greatestNodeFromLeftSide = deletingNode.left;
        Node<K, V> greatestNodeFromLeftSideParent = null;
        while (greatestNodeFromLeftSide.right != null) {
            greatestNodeFromLeftSide = greatestNodeFromLeftSide.right;
        }
        Node<K, V> current = greatestNodeFromLeftSide.parent;
        if (greatestNodeFromLeftSide.parent != deletingNode) {
            greatestNodeFromLeftSideParent = greatestNodeFromLeftSide.parent;
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
        if (greatestNodeFromLeftSideParent != null) calculateHeight(greatestNodeFromLeftSideParent);
        else calculateHeight(greatestNodeFromLeftSide);
        --size;
    }

    public void rotateRight(Node<K, V> node) {
        if (node.left != null) {
            if (node == root) {
                root = node.left;
            }
            if (node.parent != null) {
                Node<K, V> parent = node.parent;
                if (parent.left == node) parent.left = node.left;
                else parent.right = node.left;
            }
            Node<K, V> rc = node.left.right;
            node.left.right = node;
            node.left.parent = node.parent;
            node.parent = node.left;
            node.left = rc;
            // calculateHeight(calculateHeightForOneNode(node));
            System.out.println("правый поворот ключа " + node.key);
        }
    }

    public void rotateLeft(Node<K, V> node) {
        if (node.right != null) {
            if (node == root) {
                root = node.right;
            }
            if (node.parent != null) {
                Node<K, V> parent = node.parent;
                if (parent.left == node) parent.left = node.right;
                else parent.right = node.right;
            }
            Node<K, V> lc = node.right.left;
            node.right.left = node;
            node.right.parent = node.parent;
            node.parent = node.right;
            node.right = lc;
            // calculateHeight(calculateHeightForOneNode(node));
            System.out.println("левый поворот ключа " + node.key);
        }
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

    @Override
    public String toString() {
        if (isEmpty()) return "";

        StringBuilder stringBuilder = new StringBuilder();

        List<List<PrintingNode<K, V>>> rowsOfNodes = new ArrayList<>();
        addSubNodesInfo(rowsOfNodes, 0, root, 1);

        for (int rowNumber = 0; rowNumber < rowsOfNodes.size(); ++rowNumber) {
            int itemsIndent = getIndent(rowsOfNodes.size(), rowNumber);
            int prevNodeNumberInRow = 0;

            List<PrintingNode<K, V>> rowOfNodes = rowsOfNodes.get(rowNumber);
            for (int nodeNumber = 0; nodeNumber < rowOfNodes.size(); ++nodeNumber) {
                PrintingNode<K, V> node = rowOfNodes.get(nodeNumber);

                if (nodeNumber == 0) {
                    for (int j = 0; j < (itemsIndent >>> 1); ++j) {
                        stringBuilder.append(" ");
                    }
                }

                int distanceBetweenNodes = node.numberInRow - prevNodeNumberInRow;
                for (int i = 0; i < distanceBetweenNodes; ++i) {
                    for (int j = 0; j < itemsIndent; ++j) {
                        stringBuilder.append(" ");
                    }
                }
                for (int i = 1; i < distanceBetweenNodes; ++i) {
                    for (int j = 0; j < ITEM_WIDTH; ++j) {
                        stringBuilder.append(" ");
                    }
                }
                prevNodeNumberInRow = node.numberInRow;

                stringBuilder.append(String.format("%" + ITEM_WIDTH + "s", node.key));
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

//    private int getIndent(int rowsCount, int rowNumber) {
//        int powerOfTwo = 1 << (rowsCount - rowNumber - 1);
//        return ITEM_SHIFT * powerOfTwo + ITEM_WIDTH * (powerOfTwo >>> 1) + ITEM_WIDTH;
//    }

    private int getIndent(int size, int rowNumber) {
        if (rowNumber >= size - 1) {
            return ITEM_SHIFT;
        }
        return getIndent(size, rowNumber + 1) << 1 + ITEM_WIDTH;
    }

    private void addSubNodesInfo(List<List<PrintingNode<K, V>>> rowsOfNodes, int rowNumber, Node<K, V> node, int nodeNumberInRow) {
        if (node == null) return;

        addNodeToRow(rowsOfNodes, rowNumber, node, nodeNumberInRow);
        addSubNodesInfo(rowsOfNodes, rowNumber + 1, node.left, (nodeNumberInRow << 1) - 1);
        addSubNodesInfo(rowsOfNodes, rowNumber + 1, node.right, nodeNumberInRow << 1);
    }

    private void addNodeToRow(List<List<PrintingNode<K, V>>> rowsOfNodes, int rowNumber, Node<K, V> node, int nodeNumberInRow) {
        List<PrintingNode<K, V>> rowOfNodes;
        if (rowNumber >= rowsOfNodes.size()) {
            rowOfNodes = new ArrayList<>();
            rowsOfNodes.add(rowNumber, rowOfNodes);
        } else {
            rowOfNodes = rowsOfNodes.get(rowNumber);
        }
        rowOfNodes.add(new PrintingNode<>(node.getKey(), node.getValue(), nodeNumberInRow));
    }

    private static class PrintingNode<K, V> {
        private final K key;
        private final V value;
        private final int numberInRow;

        private PrintingNode(K key, V value, int numberInRow) {
            this.key = key;
            this.value = value;
            this.numberInRow = numberInRow;
        }
    }
}
