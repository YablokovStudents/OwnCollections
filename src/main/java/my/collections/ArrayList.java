package my.collections;

public class ArrayList implements List {
    public Object[] mass;
    public int size;

    public ArrayList() {
        mass = new Object[10];
        size = 0;
    }
    public ArrayList(int capacity) {
        mass = new Object[capacity];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(Object item) {
        for (int i = 0;i < size;i++)
            if (mass[i].equals(item)) return true;
        return false;
    }

    @Override
    public boolean add(Object item) {
        return false;
    }

    @Override
    public boolean remove(Object item) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, Object item) {

    }

    @Override
    public void set(int index, Object item) {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public int indexOf(Object item) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object item) {
        return 0;
    }

    @Override
    public void remove(int index) {

    }

    @Override
    public List subList(int from, int to) {
        return null;
    }
}