package my.collections;

/**
 * Коллекция объектов, представленная в виде отображения: ключ - значение, т.е. состоящая из пар {@code <key, value>},
 * где {@code key} уникален в рамках всей коллекции пар, а на {@code value} ограничения не накладываются.
 */
public interface Map<K, V> {
    /** @return количество элементов в коллекции */
    int size();

    /** @return результат проверки коллекции на пустоту (отсутствие элементов) */
    boolean isEmpty();

    /** @return результат проверки на наличие ключа {@code key} в рамках какой-либо из хранимых в коллекции
     * пар {@code <key, value>} */
    boolean containsKey(K key);

    /** @return результат проверки на наличие значения {@code value} в рамках какой-либо из хранимых в коллекции
     * пар {@code <key, value>} */
    boolean containsValue(V value);

    /**
     * Возвращение значения из пары {@code <key, value>}, в рамках которой ключ равен переданному {@code key}.
     * Если значение не было найдено, то возвращается {@code null}.
     * <p>Хранимое в рамках коллекции значение также может иметь значение {@code null},
     * поэтому получение {@code null} не может быть использовано как признак отсутствия элемента в коллекции.
     * <p>Допустимость использования {@code null} в качестве ключа коллекции зависит от конкретной реализации.
     */
    V get(K key);


    /**
     * Вставка пары {@code <key, value>} в коллекцию.
     * Если в коллекции уже присутствует пара {@code <key, value>}, которая имеет равный {@code key},
     * то в рамках данной пары произойдет замена {@code value}.
     * Хранимое в рамках коллекции {@code value} также может иметь значение {@code null}.
     * Допустимость использования {@code null} в качестве ключа коллекции зависит от конкретной реализации.
     */
    V put(K key, V value);

    /**
     * Удаление из коллекции пары {@code <key, value>}, в рамках которой ключ равен {@code key}.
     * Возвращается значение {@code value}` из удаленной пары {@code <key, value>}.
     * Если соответствующая пара не была найдена, то возвращается {@code null}.
     * Хранимое в рамках коллекции значение также может иметь значение {@code null},
     * поэтому получение {@code null} не может быть использовано как признак отсутствия пары {@code <key, value>},
     * в рамках которой {@code key} равен переданному.
     */
    V remove(K key);

    /** Удаление всех пар {@code <key, value>} из коллекции. */
    void clear();

    /**
     * Получение новой коллекции, состоящей из всех значений, которые хранятся в рамках пар {@code <key, value>}
     * данной коллекции.
     */
    Collection<V> values();

    /**
     * Получение новой коллекции, состоящей из всех ключей, которые хранятся в рамках пар {@code <key, value>}
     * данной коллекции.
     */
    Collection<K> keySet();


    /** Получение новой коллекции, состоящей из пар `<key, value>`, которые хранятся в рамках данной коллекции. */
    Collection<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();
        V getValue();
        void setValue(V value);
    }
}
