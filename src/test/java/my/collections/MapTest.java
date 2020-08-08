package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Comparator;

import static org.testng.Assert.*;

public class MapTest {
    @DataProvider(name = "maps")
    public Object[][] getMaps() {
        return new Object[][]{
                /*{new HashMap<Integer, Integer>()},*/
                {new TreeMap<Integer, Integer>(new TreeMapComparator<>())}
        };
    }

    private static class TreeMapComparator<K extends Comparable<K>> implements Comparator<K> {
        @Override
        public int compare(K key1, K key2) {
            if (key1 == null) {
                return (key2 == null) ? 0 : -1;
            } else if (key2 == null) {
                return 1;
            } else {
                return key1.compareTo(key2);
            }
        }
    }

    @Test(dataProvider = "maps")
    public void isEmpty(Map<Integer, Integer> map) {
        assertTrue(map.isEmpty());
        map.put(1, 1);
        assertFalse(map.isEmpty());
    }

    @Test(dataProvider = "maps")
    public void size(Map<Integer, Integer> map) {
        assertEquals(map.size(), 0);

        map.put(1, 2);
        assertEquals(map.size(), 1);

        map.put(null, null);
        assertEquals(map.size(), 2);
    }

    @Test(dataProvider = "maps")
    public void put(Map<Integer, Integer> map) {
        assertEquals(map.size(), 0);

        assertNull(map.put(1, 2));
        assertTrue(map.containsKey(1));
        assertEquals(map.size(), 1);

        assertNull(map.put(null, null));
        assertTrue(map.containsKey(null));
        assertEquals(map.size(), 2);

        assertEquals(map.put(1, 2), Integer.valueOf(2));
        assertEquals(map.size(), 2);

        assertNull(map.put(null, null));
        assertEquals(map.size(), 2);
    }

    @Test(dataProvider = "maps")
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

    @Test(dataProvider = "maps")
    public void get(Map<Integer, Integer> map) {
        map.put(2, 1);
        map.put(3, null);
        map.put(null, 2);
        assertEquals(map.get(2).intValue(), 1);
        assertNull(map.get(3));
        assertEquals(map.get(null).intValue(), 2);
    }

    @Test(dataProvider = "maps")
    public void containsKey(Map<Integer, Integer> map) {
        assertFalse(map.containsKey(3));
        map.put(2, 1);
        map.put(3, null);
        map.put(null, 2);
        assertTrue(map.containsKey(3));
        assertTrue(map.containsKey(null));
        assertFalse(map.containsKey(5));
    }

    @Test(dataProvider = "maps")
    public void containsValue(Map<Integer, Integer> map) {
        assertFalse(map.containsValue(3));
        map.put(2, 1);
        map.put(3, null);
        map.put(null, 2);
        assertTrue(map.containsValue(1));
        assertTrue(map.containsKey(null));
        assertFalse(map.containsKey(5));
    }

    @Test
    public void containsValu() {
        Map<Integer, Integer> map = new TreeMap<>();

        map.put(10, null);

        map.put(5, null);
        map.put(15, null);

        map.put(3, null);
        map.put(8, null);

        map.put(13, null);
        map.put(20, null);

        map.put(18, null);
        map.put(22, null);

        map.put(11, null);
        map.put(14, null);

        map.put(1, null);
        map.put(4, null);

        map.put(7, null);
        map.put(9, null);

        System.out.println(map.toString());
    }
}