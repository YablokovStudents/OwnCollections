package my.collections;

import java.util.*;

public class LinkedList implements List, Deque {
    private Node first;
    private Node last;
    private int size;
    private int modificationCount;

    private class IteratorImpl implements Iterator {
        private final int modificationCount = LinkedList.this.modificationCount;
        private Node node = first;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Object next() {
            if (modificationCount != LinkedList.this.modificationCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Object result = node.item;
            node = node.next;
            return result;
        }
    }

    @Override
    public Iterator iterator() {
        return new IteratorImpl();
    }

    private static class Node {
        private Object item;
        private Node prev;
        private Node next;

        public Node(Object item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public void addFirst(Object item) {
        add(0, item);
    }

    @Override
    public void addLast(Object item) {
        add(size, item);
    }

    @Override
    public Object getFirst() {
        checkRangeFotGet(0);
        return first.item;
    }

    @Override
    public Object getLast() {
        checkRangeFotGet(size - 1);
        return last.item;
    }

    private void checkRangeFotGet(int index) {
        if ((index < 0) || (index >= size)) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Object pollFirst() {
        if (first == null) {
            return null;
        }
        return deleteNode(first);
    }

    @Override
    public Object pollLast() {
        if (last == null) {
            return null;
        }
        return deleteNode(last);
    }

    @Override
    public Object removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return deleteNode(first);
    }

    @Override
    public Object removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return deleteNode(last);
    }

    private Object deleteNode(Node deletingNode) {
        Object deletedItem = deletingNode.item;

        if (deletingNode.prev == null) {
            first = deletingNode.next;
        } else {
            deletingNode.prev.next = deletingNode.next;
        }

        if (deletingNode.next == null) {
            last = deletingNode.prev;
        } else {
            deletingNode.next.prev = deletingNode.prev;
        }

        deletingNode.prev = null;
        deletingNode.next = null;

        size--;
        modificationCount++;

        return deletedItem;
    }

    @Override
    public void add(int index, Object item) {
        checkRangeForAdd(index);
        if (index == size) {
            addLastItem(item);
        } else {
            addNotLastItem(index, item);
        }
    }

    private void checkRangeForAdd(int index) {
        if ((index < 0) || (index > size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void addLastItem(Object item) {
        Node addingNode = new Node(item, last, null);
        if (addingNode.prev == null) {
            first = addingNode;
        } else {
            addingNode.prev.next = addingNode;
        }
        last = addingNode;

        size++;
        modificationCount++;
    }

    private void addNotLastItem(int index, Object item) {
        Node nextNode = getNode(index);
        Node prevNode = nextNode.prev;

        Node addingNode = new Node(item, prevNode, nextNode);

        if (addingNode.prev == null) {
            first = addingNode;
        } else {
            addingNode.prev.next = addingNode;
        }

        if (addingNode.next == null) {
            last = addingNode;
        } else {
            addingNode.next.prev = addingNode;
        }

        size++;
        modificationCount++;
    }

    @Override
    public void set(int index, Object item) {
        if (index == size) {
            addLast(item);
        } else {
            checkRangeFotGet(index);
            Node currentNode = getNode(index);
            currentNode.item = item;
            modificationCount++;
        }
    }

    private Node getNode(int index) {
        Node currentNode;
        if (index < (size >>> 1)) { // size >>> 1 == size / 2
            currentNode = getNodeFromLeft(index);
        } else {
            currentNode = getNodeFromRight(index);
        }
        return currentNode;
    }

    private Node getNodeFromLeft(int index) {
        Node result = first;
        for (int i = 0; i < index; i++) {
            result = result.next;
        }
        return result;
    }

    private Node getNodeFromRight(int index) {
        Node result = last;
        for (int i = size - 1; i > index; i--) {
            result = result.prev;
        }
        return result;
    }

    @Override
    public Object get(int index) {
        checkRangeFotGet(index);
        return getNode(index).item;
    }

    @Override
    public int indexOf(Object item) {
        Node currentNode = first;
        if (item == null) {
            for (int index = 0; index < size; index++) {
                if (currentNode.item == null) {
                    return index;
                }
                currentNode = currentNode.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (item.equals(currentNode.item)) {
                    return i;
                }
                currentNode = currentNode.next;
            }
        }
        return List.INDEX_NOT_FOUND;
    }

    @Override
    public int lastIndexOf(Object item) {
        Node currentNode = last;
        if (item == null) {
            for (int index = size - 1; index >= 0; index--) {
                if (currentNode.item == null) {
                    return index;
                }
                currentNode = currentNode.prev;
            }
        } else {
            for (int index = size - 1; index >= 0; index--) {
                if (item.equals(currentNode.item)) {
                    return index;
                }
                currentNode = currentNode.prev;
            }
        }
        return List.INDEX_NOT_FOUND;
    }

    @Override
    public void remove(int index) {
        checkRangeFotGet(index);
        deleteNode(getNode(index));
    }

    @Override
    public List subList(int from, int to) {
        checkRangeForSubList(from, to);

        LinkedList result = new LinkedList();
        Node currentNode = getNode(from);
        for (int index = 0; index < (to - from); index++) {
            result.add(currentNode.item);
            currentNode = currentNode.next;
        }
        return result;
    }

    private void checkRangeForSubList(int from, int to) {
        if ((from < 0) || (from >= size) || (to < 0) || (to > size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(Object item) {
        if (item == null) {
            for (Node currentNode = first; currentNode != null; currentNode = currentNode.next) {
                if (currentNode.item == null) {
                    return true;
                }
            }
        } else {
            for (Node currentNode = first; currentNode != null; currentNode = currentNode.next) {
                if (item.equals(currentNode.item)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(Object item) {
        addLast(item);
        return true;
    }

    @Override
    public boolean remove(Object item) {
        boolean deletedAtLeastOne = false;
        Node currentNode = first;
        if (item == null) {
            while (currentNode != null) {
                Node nextNode = currentNode.next;
                if (currentNode.item == null) {
                    deleteNode(currentNode);
                    deletedAtLeastOne = true;
                }
                currentNode = nextNode;
            }
        } else {
            while (currentNode != null) {
                Node nextNode = currentNode.next;
                if (item.equals(currentNode.item)) {
                    deleteNode(currentNode);
                    deletedAtLeastOne = true;
                }
                currentNode = nextNode;
            }
        }
        return deletedAtLeastOne;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
        modificationCount++;
    }
}
