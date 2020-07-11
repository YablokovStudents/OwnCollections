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
                {new ArrayList<Integer>()},
                {new LinkedList<Integer>()}
        };
    }

    @DataProvider(name = "filledIterables")
    public Object[][] getFilledIterables() {
        return getFilledLists();
    }

    @DataProvider(name = "filledLists")
    public Object[][] getFilledLists() {
        return new Object[][]{
                {getFilled(new ArrayList<>())},
                {getFilled(new LinkedList<>())}
        };
    }

    private List<Integer> getFilled(List<Integer> collection) {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(null);
        return collection;
    }

    @Test(dataProvider = "emptyIterables", expectedExceptions = NoSuchElementException.class)
    public void iterator_EmptyCase(Iterable<Integer> iterable) {
        Iterator<Integer> iterator = iterable.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test(dataProvider = "filledIterables")
    public void iterator_multipleCase(Iterable<Integer> iterable) {
        Iterator<Integer> iterator1 = iterable.iterator();
        Iterator<Integer> iterator2 = iterable.iterator();

        assertTrue(iterator1.hasNext());
        assertTrue(iterator2.hasNext());
        assertEquals(iterator1.next().intValue(), 1);
        assertEquals(iterator2.next().intValue(), 1);

        assertTrue(iterator1.hasNext());
        assertEquals(iterator1.next().intValue(), 2);

        assertTrue(iterator1.hasNext());
        assertEquals(iterator1.next().intValue(), 3);

        assertTrue(iterator1.hasNext());
        assertNull(iterator1.next());

        assertTrue(iterator2.hasNext());
        assertEquals(iterator2.next().intValue(), 2);

        assertTrue(iterator2.hasNext());
        assertEquals(iterator2.next().intValue(), 3);

        assertTrue(iterator2.hasNext());
        assertNull(iterator2.next());

        assertFalse(iterator1.hasNext());
        assertFalse(iterator2.hasNext());
    }

    @Test(dataProvider = "filledLists", expectedExceptions = ConcurrentModificationException.class)
    public void iterator_ConcurrentModificationCase(List<Integer> list) {
        Iterator<Integer> iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 1);
        list.add(5);
        list.add(null);
        iterator.next();
    }
}