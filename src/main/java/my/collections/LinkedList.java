package my.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList implements List, Deque {
    private Node first;
    private Node last;
    int size = 0;

    public class LinkedIterator implements Iterator {
        /*Node number = first;
        Node numberNext = first;*/
        int position = 0;
        Node node = first;
        Object[] arr = new Object[size];

        @Override
        public boolean hasNext() {
            /*if (number != null) {
                number = number.next;*/
            return position < size;
        }

        @Override
        public Object next() {
            if (hasNext()) {
                if (position == 0) {
                    arr[0] = node.item;
                    node = node.next;
                    for (int i = 1; i < size - 1; i++) {
                        arr[i] = node.item;
                        node = node.next;
                    }
                    arr[size - 1] = node.item;
                    node = first;
                    position++;
                    return first.item;
                } else {
                    Node node1 = first;
                    for (int j = 0; j < arr.length; j++) {
                        if (arr[j] != null) {
                            if (!arr[j].equals(node1.item)) throw new ConcurrentModificationException();
                        } else if (node1.item != null) throw new ConcurrentModificationException();
                        node1 = node1.next;
                    }
                    if (node1 != null) throw new ConcurrentModificationException();
                    node = node.next;
                    position++;
                }
            } else throw new NoSuchElementException();
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
        if (size != 0) {
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
        if (size != 0) {
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
        if (index > size) throw new IndexOutOfBoundsException();
        if (size == 0)
            add(item);
        if (index == size)
            addLast(item);
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
        if (isEmpty()) return List.NOT_FOUND;
        if (item != null) {
            Node obj = first;
            if (obj.item.equals(item)) return 0;
            for (int i = 1; i < size; i++) {
                obj = obj.next;
                if (obj.item == item) return i;
            }
        } else {
            Node obj = first;
            if (obj.item == null) return 0;
            for (int i = 1; i < size; i++) {
                obj = obj.next;
                if (obj.item == null) return i;
            }
        }
        return List.NOT_FOUND;
    }

    @Override
    public int lastIndexOf(Object item) {
        if (isEmpty()) return List.NOT_FOUND;
        if (item != null) {
            Node obj = last;
            if (obj.item == item) return size - 1;
            for (int i = size - 2; i >= 0; i--) {
                obj = obj.prev;
                if (obj.item == item) return i;
            }
        } else {
            Node obj = last;
            if (obj.item == null) return size - 1;
            for (int i = size - 2; i >= 0; i--) {
                obj = obj.prev;
                if (obj.item == null) return i;
            }
        }

        return List.NOT_FOUND;
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
        if (from < 0 || to > size || to < 0 || from >= size || isEmpty()) throw new IndexOutOfBoundsException();
        LinkedList linkedList = new LinkedList();
        if (from == to)
            return linkedList;
        Node node = first;
        Node node1 = last;
        int i1 = 0;
        for (int i = 0; i < from; i++) {
            node = node.next;
            i1++;
        }
        first = node;
        first.prev = null;
        for (int j = size - 1; j > to - 1; j--) {
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
        if (size == 0) return false;
        Node node = first;
        if (item == null) {
            if (first.item == null) return true;
            else
                for (int i = 0; i < size - 1; i++) {
                    node = node.next;
                    if (node.item == null) return true;
                }
            return false;
        } else {
            node = first;
            if (item.equals(node.item)) return true;
            for (int i = 0; i < size - 1; i++) {
                node = node.next;
                if (item.equals(node.item)) return true;
            }
            return false;
        }
    }

    @Override
    public boolean add(Object item) {
        Node node1 = new Node(item);
        if (first == null) {
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

    @Override
    public boolean remove(Object item) {
        if (size == 0) return false;
        Node node = first;
        int sz = 0;

        if (item == null) {
            if (first.item == null) {
                first = first.next;
                if (first != null)
                    first.prev = null;

                sz++;
            }

            for (int i = 0; i < size - 2; i++) {
                node = node.next;
                if (node.item == null) {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                    sz++;
                }
                if (last.item == null) {
                    last = last.prev;
                    if (last != null)
                        last.next = null;
                    sz++;
                }
            }
        } else {

            if (item.equals(first.item)) {
                first = first.next;
                first.prev = null;
                sz++;
            }
            for (int i = 0; i < size - 2; i++) {
                node = node.next;
                if (item.equals(node.item)) {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;

                    sz++;
                }
            }
            if (item.equals(last.item)) {
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
