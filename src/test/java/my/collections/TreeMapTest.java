package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNull;

public class TreeMapTest {
    @DataProvider(name = "TreeMap")
    public Object[][] getCollections() {
        return new Object[][]{
                {new TreeMap<Integer, Integer>()}
        };
    }

    @Test(dataProvider = "TreeMap")
    public void isEmpty(Map<Integer, Integer> map) {
        assertTrue(map.isEmpty());
        map.put(1, 1);
        assertFalse(map.isEmpty());
    }

    @Test(dataProvider = "TreeMap")
    public void get(Map<Integer, Integer> map) {
        map.put(1, 2);
        map.put(3, 4);
        map.put(5, 6);
        map.put(7, 8);
        assertEquals(map.get(1),2);
        assertEquals(map.get(3),4);
        assertEquals(map.get(5),6);
        assertEquals(map.get(7),8);
    }

    @Test(dataProvider = "TreeMap")
    public void put(Map<Integer, Integer> map) {
        assertNull(map.put(1, 1));
        map.put(2, 1);
        map.put(4, null);
        assertNull(map.put(4, 2));
        assertEquals(map.put(2,5),1);
    }

    @Test(dataProvider = "TreeMap")
    public void size(Map<Integer, Integer> map) {
        assertEquals(map.size(),0);
        map.put(2, 1);
        map.put(4, null);
        map.put(4, 2);
        assertEquals(map.size(),2);
    }

    @Test(dataProvider = "TreeMap")
    public void remove(Map<Integer, Integer> map) {
        map.put(1, 2);
        map.put(4, 6);
        map.put(2, 3);
        map.put(5, 4);
        assertNull(map.remove(55));
        assertEquals(map.get(2),3);
        assertEquals(map.remove(2), 3);
        assertEquals(map.size(), 3);
        assertEquals(map.remove(1), 2);
        assertEquals(map.size(), 0);
        assertFalse(map.containsKey(4));
        assertFalse(map.containsKey(2));
        assertNull(map.remove(5));
        assertNull(map.remove(null));
    }

    @Test(dataProvider = "TreeMap")
    public void containsKey(Map<Integer, Integer> map) {
        assertFalse(map.containsKey(6));
        map.put(1, 1);
        map.put(3, null);
        assertTrue(map.containsKey(3));
        assertEquals(map.get(3),null);
        assertNull(map.put(3, 4));
        assertEquals(map.get(3),4);
        map.remove(3);
        assertFalse(map.containsKey(3));

    }

    @Test(dataProvider = "TreeMap")
    public void containsValue(Map<Integer, Integer> map) {
        assertFalse(map.containsValue(6));
        map.put(1, 1);
        map.put(4, 2);
        map.put(3, null);
        assertTrue(map.containsValue(null));
        assertNull(map.get(3));
        assertNull(map.put(3, 4));
        assertTrue(map.containsValue(2));
        assertEquals(map.get(3),4);
        map.remove(3);
        assertFalse(map.containsValue(4));

    }

    @Test(dataProvider = "TreeMap")
    public void iteratorsTest(Map<Integer, Integer> map) {
        map.put(1, 1);
        map.put(2, 1);
        map.put(4, null);
        for (Map.Entry<Integer, Integer> i : map.entrySet())
            System.out.println("key " + i.getKey() + " value " + i.getValue());
    }

}
