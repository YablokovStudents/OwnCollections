package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link Iterable}.
 */
public class IterableTest {
    @DataProvider(name = "emptyIterables")
    public Object[][] getEmptyIterables() {
        return new Object[][]{
                {new ArrayList()},
                {new LinkedList()}
        };
    }

    @DataProvider(name = "filledIterables")
    public Object[][] getFilledIterables() {
        return getFilledLists();
    }

    @DataProvider(name = "filledLists")
    public Object[][] getFilledLists() {
        return new Object[][]{
                {getFilled(new ArrayList())},
                {getFilled(new LinkedList())}
        };
    }

    private List getFilled(List collection) {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(null);
        return collection;
    }

    @Test(dataProvider = "emptyIterables", expectedExceptions = NoSuchElementException.class)
    public void iterator_EmptyCase(Iterable iterable) {
        Iterator iterator = iterable.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test(dataProvider = "filledIterables")
    public void iterator_multipleCase(Iterable iterable) {
        Iterator iterator1 = iterable.iterator();
        Iterator iterator2 = iterable.iterator();

        assertTrue(iterator1.hasNext());
        assertTrue(iterator2.hasNext());
        assertEquals(iterator1.next(), 1);
        assertEquals(iterator2.next(), 1);

        assertTrue(iterator1.hasNext());
        assertEquals(iterator1.next(), 2);

        assertTrue(iterator1.hasNext());
        assertEquals(iterator1.next(), 3);

        assertTrue(iterator1.hasNext());
        assertNull(iterator1.next());

        assertTrue(iterator2.hasNext());
        assertEquals(iterator2.next(), 2);

        assertTrue(iterator2.hasNext());
        assertEquals(iterator2.next(), 3);

        assertTrue(iterator2.hasNext());
        assertNull(iterator2.next());

        assertFalse(iterator1.hasNext());
        assertFalse(iterator2.hasNext());
    }

    @Test(dataProvider = "filledLists", expectedExceptions = ConcurrentModificationException.class)
    public void iterator_ConcurrentModificationCase(List list) {
        Iterator iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), 1);
        list.add(5);
        list.add(null);
        iterator.next();

    }
}