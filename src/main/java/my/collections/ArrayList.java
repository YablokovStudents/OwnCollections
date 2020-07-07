package my.collections;


import java.util.Arrays;
import java.util.Iterator;

public class ArrayList implements List {
    public Object[] array;
    public int size;

    public ArrayList() {
        this(10);
    }

    public ArrayList(int capacity) {
        array = new Object[capacity];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object item) {
        if (item == null) {
            for (int i = 0; i < size; ++i) {
                if (array[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; ++i) {
                if (item.equals(array[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(Object item) {
        add(size, item);
        return true;
    }

    @Override
    public boolean remove(Object item) {
        boolean xz = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return xz;
            } else if (array[i].equals(item)) {
                xz = true;
                for (int j = i; j < array.length - 1; j++)
                    array[j] = array[j + 1];
                size--;
                i--;

            }
        }
        return false;
    }


    @Override
    public void clear() {
        array = new Object[10];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, Object item) {
        checkRange(index);
        extendArrayIfFull();
        for (int i = size; i > index; --i) {
            array[i] = array[i - 1];
        }
        array[index] = item;
        size++;
    }

    @Override
    public void set(int index, Object item) {
        if (index == size) {
            add(item);
        } else {
            checkRange(index);
            array[index] = item;
        }
    }

    private void checkRange(int index) {
        if ((index < 0) || (index > size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void extendArrayIfFull() {
        if (array.length == size) {
            array = Arrays.copyOf(array, (array.length * 3) / 2 + 1);
        }
    }

    @Override
    public Object get(int index) {
        if ((index < 0) || (index >= size)) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(Object item) {
        for (int i = 0; i < size; i++) {
            if (item.equals(array[i])) {
                return i;
            }
        }
        return List.NOT_FOUND;
    }

    @Override
    public int lastIndexOf(Object item) {
        for (int i = size - 1; i > 0; i--)
            if (array[i].equals(item)) return i;
        return -1;
    }

    @Override
    public void remove(int index) {
        if (index >= size) throw new IndexOutOfBoundsException();
        else for (int i = index; i < size - 1; i++)
            array[i] = array[i + 1];
        size--;
    }

    @Override
    public List subList(int from, int to) {
        if (from >= size || to >= size || from > to) throw new IndexOutOfBoundsException();
        else {
            ArrayList arrayList = new ArrayList();
            arrayList.array = Arrays.copyOfRange(array, from, to + 1);
            arrayList.array = Arrays.copyOf(arrayList.array, (arrayList.array.length * 3) / 2 + 1);
            arrayList.size = to - from + 1;
            return arrayList;
        }
    }

    @Override
    public Iterator iterator() {
        return new ArrayListIterator();
    }

    public class ArrayListIterator implements Iterator {
        private int position;

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public Object next() {
            return array[position++];
        }
    }
}

