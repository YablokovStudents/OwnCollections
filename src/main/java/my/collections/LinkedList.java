package my.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList implements List, Deque {
    private Node first;
    private Node last;
    int size = 0;

    public class LinkedIterator implements Iterator {
        Node number = first;
        Node numberNext = first;

        @Override
        public boolean hasNext() {
            if (number != null) {
                number = number.next;
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            Node node = numberNext;
            numberNext = numberNext.next;
            return node.item;
        }
    }

    @Override
    public Iterator iterator() {
        return new LinkedIterator();
    }

    public class Node {
        Object item;
        Node next;
        Node prev;

        public Node(Object item) {
            this.item = item;
        }
    }

    @Override
    public void addFirst(Object item) {
        Node node1 = new Node(item);
        if (first == null) {
            first = node1;
            last = node1;
        } else {
            node1.next = first;
            first = node1;
            node1.next.prev = first;
        }
        size++;
    }

    @Override
    public void addLast(Object item) {
        Node node1 = new Node(item);
        if (last == null) {
            first = node1;
            last = node1;
        } else {
            node1.prev = last;
            last.next = node1;
            last = node1;
        }
        size++;
    }

    @Override
    public Object getFirst() {

        return first.item;
    }

    @Override
    public Object getLast() {
        return last.item;
    }

    @Override
    public Object pollFirst() {
        if (first != null) {
            Object o = first.item;
            first = first.next;
            first.prev = null;
            size--;
            return o;

        }
        return null;
    }

    @Override
    public Object pollLast() {
        if (last != null) {
            Object o = last.item;
            last = last.prev;
            last.next = null;
            size--;
            return o;
        }
        return null;
    }

    @Override
    public Object removeFirst() {
        if (first == null) throw new NoSuchElementException();
        Object o = first.item;
        first = first.next;
        first.prev = null;
        size--;
        return o;
    }

    @Override
    public Object removeLast() {
        if (last == null) throw new NoSuchElementException();
        Object o = last.item;
        last = last.prev;
        last.next = null;
        size--;
        return o;
    }

    @Override
    public void add(int index, Object item) {
        if (index > size || index < 0) throw new IndexOutOfBoundsException();
        if (item == null) throw new NullPointerException();
        if (index == 0) {
            addFirst(item);
            return;
        }
        if (index == size) {
            addLast(item);
            return;
        }
        Node node1 = new Node(item);
        Node obj = first;
        for (int i = 0; i < index; i++)
            obj = obj.next;
        Node olden = obj.prev;
        olden.next = node1;
        node1.next = obj;
        node1.prev = olden;
        size++;
    }

    @Override
    public void set(int index, Object item) {
        if (index >= size || isEmpty()) throw new IndexOutOfBoundsException();
        if (item == null) throw new NullPointerException();
        Node obj = first;
        for (int i = 0; i < index; i++)
            obj = obj.next;
        obj.item = item;
    }

    @Override
    public Object get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException();
        Node obj = first;
        for (int i = 0; i < index; i++)
            obj = obj.next;
        return obj.item;
    }

    @Override
    public int indexOf(Object item) {
        if (item == null) throw new NullPointerException();
        if (isEmpty()) throw new IndexOutOfBoundsException();
        Node obj = first;
        if (obj.item.equals(item)) return 0;
        for (int i = 1; i < size; i++) {
            obj = obj.next;
            if (obj.item == item) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object item) {
        if (item == null) throw new NullPointerException();
        if (isEmpty()) throw new IndexOutOfBoundsException();
        Node obj = last;
        if (obj.item == item) return size - 1;
        for (int i = size - 2; i >= 0; i--) {
            obj = obj.prev;
            if (obj.item == item) return i;
        }
        return -1;
    }

    @Override
    public void remove(int index) {
        if (index >= size || isEmpty()) throw new IndexOutOfBoundsException();
        if (size == 1) {
            last = first = null;
            size = 0;
            return;
        }
        if (index == 0) {
            first = first.next;
            first.prev = null;
            size--;
            return;
        }
        if (index == size - 1) {
            last = last.prev;
            last.next = null;
            size--;
            return;
        }
        Node obj = first;
        for (
                int i = 0;
                i < index; i++)
            obj = obj.next;
        obj.prev.next = obj.next;
        obj.next.prev = obj.prev;
        size--;
    }

    @Override
    public List subList(int from, int to) {
        if (from < 0 || to >= size || to < 0 || from >= size || isEmpty()) throw new IndexOutOfBoundsException();
        LinkedList linkedList = new LinkedList();
        Node node = first;
        Node node1 = last;
        int i1 = 0;
        for (int i = 0; i < from; i++) {
            node = node.next;
            i1++;
        }
        first = node;
        first.prev = null;
        for (int j = size - 1; j > to; j--) {
            node1 = node1.prev;
            i1++;
        }
        last = node1;
        last.next = null;
        linkedList.last = last;
        linkedList.first = first;
        linkedList.size = size - i1;
        return linkedList;
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
        if (item == null) throw new NullPointerException();
        Node node = first;
        if (node.item.equals(item)) return true;
        for (int i = 0; i < size - 1; i++) {
            node = node.next;
            if (node.item.equals(item)) return true;
        }
        return false;
    }

    @Override
    public boolean add(Object item) {
        if (item != null) {
            Node node1 = new Node(item);
            if (last == null) {
                first = node1;
                last = node1;
            } else {
                node1.prev = last;
                last.next = node1;
                last = node1;
            }
            size++;
            return true;

        }
        return false;
    }

    @Override
    public boolean remove(Object item) {
        if (item == null) throw new IndexOutOfBoundsException();
        Node node = first;
        int sz = 0;
        if (first.item.equals(item)) {
            first = first.next;
            first.prev = null;
            sz++;
        }
        for (int i = 1; i < size - 2; i++) {
            node = node.next;
            if (node.item.equals(item)) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                sz++;
            }
            if (last.item.equals(item)) {
                last = last.prev;
                last.next = null;
                sz++;
            }
        }
        size -= sz;
        return sz > 0;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }
}