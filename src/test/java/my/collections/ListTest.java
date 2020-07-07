package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
        list.add("value1");
        list.add("value2");
        list.add("value3");
        list.add("value4");

        list.add(1, "value5");
        assertEquals(list.get(1), "value5");
    }

    @Test(dataProvider = "lists")
    public void set(List list) {
        list.set(0, "value1");
        assertEquals(list.get(1), "value1");

        list.add("value2");
        list.add("value3");
        list.add("value4");
        list.add("value5");
        list.set(1, "value6");
        list.set(5, "value7");
        assertEquals(list.get(1), "value6");
        assertEquals(list.get(5), "value7");
    }

    @Test(dataProvider = "lists")
    public void subList(List list) {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        List subList = list.subList(1, 4);
        assertEquals(subList.size(), 3);
        assertEquals(subList.get(0), 2);
        assertEquals(subList.get(1), 3);
        assertEquals(subList.get(2), 4);
    }

    @Test(dataProvider = "lists")
    public void indexOf(List list) {
        assertEquals(list.indexOf(1), List.NOT_FOUND);

        list.add(1);
        list.add(2);
        list.add(4);
        list.add(3);
        list.add(4);
        assertEquals(list.indexOf(1), 0);
        assertEquals(list.indexOf(2), 1);
        assertEquals(list.indexOf(4), 2);
        assertEquals(list.indexOf(3), 3);
    }

    @Test(dataProvider = "lists")
    public void lastIndexOf(List list) {
        assertEquals(list.lastIndexOf(1), List.NOT_FOUND);

        list.add(1);
        list.add(2);
        list.add(4);
        list.add(3);
        list.add(4);
        assertEquals(list.lastIndexOf(1), 0);
        assertEquals(list.lastIndexOf(2), 1);
        assertEquals(list.lastIndexOf(3), 3);
        assertEquals(list.lastIndexOf(4), 4);
    }
}