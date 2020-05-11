package my.collections;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CollectionTest {
    @Test
    public void isEmpty() {
        Collection collection = new ArrayList();
        collection.add(new Object());
        assertTrue(collection.isEmpty());
    }

    @Test
    public void size() {
        Collection collection = new ArrayList();
        collection.add(new Object());
        assertEquals(collection.size(), 1);
    }

    @Test
    public void contains() {
        Collection collection = new ArrayList();
        Object object = new Object();
        collection.add(object);
        assertTrue(collection.contains("object"));

    }

    @Test
    public void add() {
        Collection collection = new ArrayList();
      //  collection.add(new Object());
        assertTrue(collection.add(new Object()));
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
    public void indexOf(){
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));

        assertEquals(collection.indexOf("tyy"),1);
    }
    @Test
    public void lastIndexOf(){
        ArrayList collection = new ArrayList();
        collection.add(new String("ty"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tyy"));
        collection.add(new String("tty"));
        assertEquals(collection.lastIndexOf("tyy"),2);
    }
}