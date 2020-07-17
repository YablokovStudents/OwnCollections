package my.collections;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MapTest {
    @DataProvider(name = "hashMap")
    public Object[][] getCollections() {
        return new Object[][]{
                {new HashMap<Integer, Integer>()}
        };
    }

    @Test(dataProvider = "hashMap")
    public void isEmpty(Map<Integer, Integer> map) {
        assertTrue(map.isEmpty());
        map.put(1,1);
        assertFalse(map.isEmpty());
    }

            @Test(dataProvider = "hashMap")
    public void size(Map<Integer, Integer> map) {
        assertEquals(map.size(), 0);
        map.put(1, 2);
        assertEquals(map.size(), 1);
        map.put(null, null);
        assertEquals(map.size(), 2);
    }

    @Test(dataProvider = "hashMap")
    public void put(Map<Integer, Integer> map) {
        assertEquals(map.size(), 0);
        map.put(1, 2);
        assertEquals(map.size(), 1);
        map.put(null, null);
        assertEquals(map.size(), 2);
        map.put(1, 2);
        assertEquals(map.size(), 3);
        map.put(null, null);
        assertEquals(map.size(), 4);
    }

    @Test(dataProvider = "hashMap")
    public void remove(Map<Integer, Integer> map) {
        assertEquals(map.size(), 0);
        map.put(1, 2);
        assertEquals(map.size(), 1);
        map.put(null, null);
        map.remove(null);
        assertTrue(map.containsKey(1));
        assertEquals(map.size(), 1);
        assertNull(map.remove(2));
    }

    @Test(dataProvider = "hashMap")
    public void get(Map<Integer, Integer> map) {

        map.put(2, 1);
        map.put(3, null);
        map.put(null, 2);
        assertEquals(map.get(2), 1);
        assertNull(map.get(3));
        assertEquals(map.get(null), 2);
    }

    @Test(dataProvider = "hashMap")
    public void containsKey(Map<Integer, Integer> map) {
        assertFalse(map.containsKey(3));
        map.put(2, 1);
        map.put(3, null);
        map.put(null, 2);
        assertTrue(map.containsKey(3));
        assertTrue(map.containsKey(null));
        assertFalse(map.containsKey(5));
    }

    @Test(dataProvider = "hashMap")
    public void containsValue(Map<Integer, Integer> map) {
        assertFalse(map.containsValue(3));
        map.put(2, 1);
        map.put(3, null);
        map.put(null, 2);
        assertTrue(map.containsValue(1));
        assertTrue(map.containsKey(null));
        assertFalse(map.containsKey(5));
    }
}