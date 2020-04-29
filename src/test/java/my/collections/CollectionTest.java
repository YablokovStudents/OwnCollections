package my.collections;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CollectionTest {
    @Test
    public void isEmpty() {
        Collection collection = new ArrayList();
        collection.add(new Object());
        assertFalse(collection.isEmpty());
    }

    @Test
    public void size() {
        Collection collection = new ArrayList();
        collection.add(new Object());
        assertEquals(collection.size(), 1);
    }

    @Test
    public void contains(Object item) {
        Collection collection = new ArrayList();
        Object object = new Object();
        collection.add(object);
        assertTrue(collection.contains(object));

    }

    @Test
    public void add(Object item) {
        Collection collection = new ArrayList();
        assertTrue(collection.add(new Object()));
    }

}