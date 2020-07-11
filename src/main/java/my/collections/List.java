package my.collections;

public interface List<T> extends Collection<T> {
    int INDEX_NOT_FOUND = -1;

    void add(int index, T item);
    void set(int index, T item);
    T get(int index);
    int indexOf(T item);
    int lastIndexOf(T item);
    void remove(int index);
    List<T> subList(int from, int to);
}
