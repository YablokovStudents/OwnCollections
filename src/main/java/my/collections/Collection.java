package my.collections;

public interface Collection {
    int size();
    boolean isEmpty() ;
    boolean contains(Object item);
    boolean add(Object item);
    boolean remove(Object item);
    void clear();

}
