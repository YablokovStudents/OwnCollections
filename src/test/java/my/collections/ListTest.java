package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link List}.
 */
public class ListTest {
    @DataProvider(name = "lists")
    public Object[][] getLists() {
        return new Object[][]{
                {new ArrayList()},
                {new LinkedList()}
        };
    }

    @Test(dataProvider = "lists")
    public void add(List list) {
        list.add(0, null);
        assertEquals(list.get(0), null);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(1, 5);
        assertEquals(list.get(1), 5);
    }

    @Test(dataProvider = "lists")
    public void set(List list) {
        list.set(0, 1);
        assertEquals(list.get(0), 1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.set(1, null);
        list.set(5, 7);
        assertNull(list.get(1));
        assertEquals(list.get(5), 7);
    }

    @Test(dataProvider = "lists")
    public void subList(List list) {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        List subList = list.subList(0, 5);
        assertEquals(subList.size(), 5);
    }

    @Test(dataProvider = "lists")
    public void indexOf(List list) {
        assertEquals(list.indexOf(null), List.NOT_FOUND);
        list.add(1);
        list.add(2);
        list.add(null);
        list.add(3);
        list.add(4);
        assertEquals(list.indexOf(1), 0);
        assertEquals(list.indexOf(2), 1);
        assertEquals(list.indexOf(null), 2);
        assertEquals(list.indexOf(3), 3);
    }

    @Test(dataProvider = "lists")
    public void lastIndexOf(List list) {
        assertEquals(list.lastIndexOf(1), List.NOT_FOUND);
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(null);
        list.add(4);
        assertEquals(list.lastIndexOf(1), 0);
        assertEquals(list.lastIndexOf(2), 1);
        assertEquals(list.lastIndexOf(null), 3);
        assertEquals(list.lastIndexOf(4), 4);
    }

    @Test(dataProvider = "lists")
    public void remove(List list) {
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(null);
        list.add(4);
        list.remove(3);
        assertEquals(list.size(), 4);
    }
}
