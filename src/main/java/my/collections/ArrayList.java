package my.collections;


import java.util.Arrays;
import java.util.Iterator;

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
        for (int i = 0; i < size; i++)
            if (mass[i].equals(item)) return true;
        return false;
    }

    @Override
    public boolean add(Object item) {
        if (item != null) {
            if (mass[mass.length - 2] != null)
                mass = Arrays.copyOf(mass, (mass.length * 3) / 2 + 1);
            for (int i = 0; i < mass.length; i++)
                if (mass[i] == null) {
                    mass[i] = item;
                    size++;
                    return true;
                }
        } else return false;
        return false;
    }

    @Override
    public boolean remove(Object item) {
        boolean xz = false;
        for (int i = 0; i < mass.length; i++) {
            if (mass[i] == null) {
                return xz;
            } else if (mass[i].equals(item)) {
                xz = true;
                for (int j = i; j < mass.length - 1; j++)
                    mass[j] = mass[j + 1];
                size--;
                i--;

            }
        }
        return false;
    }


    @Override
    public void clear() {
        mass = new Object[10];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, Object item) {
        if (item == null) throw new NullPointerException();
        else if (index > size) throw new IndexOutOfBoundsException();
        else if (mass[mass.length - 2] != null)
            mass = Arrays.copyOf(mass, (mass.length * 3) / 2 + 1);
        for (int i = size; i > index; i--)
            mass[i] = mass[i - 1];
        mass[index] = item;
        size++;
    }

    @Override
    public void set(int index, Object item) {
        if (item == null) throw new NullPointerException();
        else if (index >= size || isEmpty()) throw new IndexOutOfBoundsException();
        else mass[index] = item;
    }

    @Override
    public Object get(int index) {
        if (mass[index] == null) throw new IndexOutOfBoundsException();
        return mass[index];
    }

    @Override
    public int indexOf(Object item) {
        for (int i = 0; i < size; i++)
            if (mass[i].equals(item)) return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object item) {
        for (int i = size - 1; i > 0; i--)
            if (mass[i].equals(item)) return i;
        return -1;
    }

    @Override
    public void remove(int index) {
        if (index >= size) throw new IndexOutOfBoundsException();
        else for (int i = index; i < size - 1; i++)
            mass[i] = mass[i + 1];
        size--;
    }

    @Override
    public List subList(int from, int to) {
        if (from >= size || to >= size || from > to) throw new IndexOutOfBoundsException();
        else {
            ArrayList arrayList = new ArrayList();
            arrayList.mass = Arrays.copyOfRange(mass, from, to + 1);
            arrayList.mass = Arrays.copyOf(arrayList.mass, (arrayList.mass.length * 3) / 2 + 1);
            arrayList.size = to - from + 1;
            return arrayList;
        }

    }


    @Override
    public Iterator iterator() {
        return new ArrayIterator();
    }


    public class ArrayIterator implements Iterator {
        int number = 0;
        int numberNext = 0;

        @Override
        public boolean hasNext() {
            if (mass[number] != null) {
                number++;
                return true;
            }
            return false;
        }

        @Override
        public Object next() {

            return mass[numberNext++];
        }
    }
}

