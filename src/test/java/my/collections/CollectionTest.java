package my.collections;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

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
        collection.add(new Object());
        assertEquals(collection.size(), 2);
    }

    @Test
    public void contains() {
        Collection collection = new ArrayList();
        Object object = new Object();
        collection.add(object);
        collection.add("hil");
        collection.add("object");
        assertTrue(collection.contains(object));

    }

    @Test
    public void add() {
        Collection collection = new ArrayList();
         collection.add(new Object());
        assertTrue(collection.add(new Object()));
    }

    @Test
    public void testAdd1() {
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));
        collection.add(4, "456");
        for (Object o : collection)
            System.out.println(o);
    }

    @Test
    public void testSet() {
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));
        collection.set(1, "6678");
        for (Object o : collection)
            System.out.println(o);
    }

    @Test
    public void testSubList() {
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyyu"));
        collection.add(new String("tty"));
        List list = collection.subList(0, 3);
        for (Object o : list)
            System.out.println(o);
    }

    @Test
    public void remove() {
        Collection collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));
        collection.remove("tyy");
        // collection.remove("tyy");
        assertFalse(collection.contains("tyy"));
    }

    @Test
    public void indexOf() {
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));

        assertEquals(collection.indexOf("tyy"), 1);
    }

    @Test
    public void lastIndexOf() {
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));
        assertEquals(collection.lastIndexOf("tyy"), 2);
    }

    @Test
    public void Iterator() {
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));
        for (Object o : collection) System.out.println(o);
    }
}