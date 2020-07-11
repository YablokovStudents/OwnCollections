package my.collections;

public interface List extends Collection {
    int INDEX_NOT_FOUND = -1;

    void add(int index, Object item);
    void set(int index, Object item);
    Object get(int index);
    int indexOf(Object item);
    int lastIndexOf(Object item);
    void remove(int index);
    List subList(int from, int to);
}
