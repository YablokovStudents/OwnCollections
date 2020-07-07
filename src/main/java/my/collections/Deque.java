package my.collections;

public interface Deque extends Collection {
    void addFirst(Object item);
    void addLast(Object item);
    Object getFirst();
    Object getLast();
    Object pollFirst();
    Object pollLast();
    Object removeFirst();
    Object removeLast();
}
