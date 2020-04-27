package my.collections;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CollectionTest {
    @Test
    public void size() {
        Collection collection = new ArrayList();
        assertEquals(collection.size(), 0);
    }
}