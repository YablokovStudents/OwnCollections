package my.collections;

/** Список объектов. */
public interface List<T> extends Collection<T> {
    /** Индекс, указывающий на отсутствие искомого элемента. */
    int INDEX_NOT_FOUND = -1;

    /**
     * Вставка элемента item на позицию index.
     * В результате вставки список раздвигается, т.е. все элементы, начиная с позиции {@code index},
     * увеличивают свой индекс на 1, т.е. сдвигаются на один элемент вправо.
     */
    void add(int index, T item);


    /**
     * Замена элемента, находящегося на позиции {@code index} объектом {@code item}.
     * Если {@code index} указывает на адрес первой свободной позиции (позиции, находящейся сразу за последним элементом),
     * то будет произведена вставка элемента в конец списка.
     */
    void set(int index, T item);

    /**
     * Получение объекта, находящегося на позиции index.
     * В случае отсутствия в коллекции элемента на позиции {@code index} генерируется исключение {@link IndexOutOfBoundsException}.
     */
    T get(int index);

    /**
     * @return индекс первого появления элемента {@code item} в списке,
     * либо {@link List#INDEX_NOT_FOUND} в случае отсутствия элемента в списке
     */
    int indexOf(T item);

    /**
     * @return индекс последнего появления элемента {@code item} в списке,
     * либо {@link List#INDEX_NOT_FOUND} в случае отсутствия элемента в списке
     */
    int lastIndexOf(T item);

    /**
     * Удаление элемента, находящегося на позиции {@code index}. Возвращается удаленный элемент.
     * В случае отсутствия элементов в коллекции генерируется исключение {@link IndexOutOfBoundsException}.
     */
    T remove(int index);

    /**
     * Получение нового списка, представляющего собой часть данного, начиная с позиции {@code from} включительно
     * до позиции {@code to} не включительно, т.е. интервал элементов {@code [from, to - 1]}.
     * В случае выхода за границы списка генерируется исключение {@link IndexOutOfBoundsException}.
     */
    List<T> subList(int from, int to);
}
