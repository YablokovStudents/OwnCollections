package my.collections;

public interface Deque<T> extends Collection<T> {
    void addFirst(T item);
    void addLast(T item);
    T getFirst();
    T getLast();
    T pollFirst();
    T pollLast();
    T removeFirst();
    T removeLast();
}
