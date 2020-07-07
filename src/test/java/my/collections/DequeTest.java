package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.*;

public class DequeTest {
    @DataProvider(name = "deques")
    public Object[][] getDeques() {
        return new Object[][]{
                {new LinkedList()}
        };
    }

    @Test(dataProvider = "deques")
    public void addFirst(Deque deque) {
        deque.add("j");
        deque.add("k");
        deque.add("h");
        deque.add("j");
        deque.add("j");
        deque.add("o");
        deque.add("j");
        deque.addFirst("a");
        assertEquals(deque.getFirst(), "a");
        for (Object o : deque) System.out.println(o);
    }

    @Test
    public void testAddLast() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("j");
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("j");
        linkedList.add("j");
        linkedList.add("o");
        linkedList.add("j");
        linkedList.addLast("a");
        assertEquals(linkedList.getLast(), "a");
        for (Object o : linkedList) System.out.println(o);
    }

    @Test
    public void testPollFirst() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o2");
        linkedList.add("o3");
        linkedList.add("o4");
        assertEquals(linkedList.pollFirst(), "o1");
        assertEquals(linkedList.getFirst(), "o2");
        for (Object o : linkedList) System.out.println(o);
    }

    @Test
    public void testPollLast() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("j");
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("j");
        linkedList.add("j");
        linkedList.add("o");
        linkedList.add("j");
        assertEquals(linkedList.pollLast(), "j");
        assertEquals(linkedList.getLast(), "o");
        for (Object o : linkedList) System.out.println(o);
    }

    @Test
    public void testRemoveFirst() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o2");
        linkedList.add("o3");
        linkedList.add("o4");
        assertEquals(linkedList.removeFirst(), "o1");
        assertEquals(linkedList.getFirst(), "o2");
        for (Object o : linkedList) System.out.println(o);
    }

    @Test
    public void testRemoveLast() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o2");
        linkedList.add("o3");
        linkedList.add("o4");
        assertEquals(linkedList.removeLast(), "o4");
        assertEquals(linkedList.getLast(), "o3");
        for (Object o : linkedList) System.out.println(o);
    }

    @Test
    public void testAdd() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o2");
        linkedList.add("o3");
        linkedList.add(2, "o4");
        assertEquals(linkedList.get(2), "o4");
        for (Object o : linkedList) System.out.println(o);
    }

    @Test
    public void testSet() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o2");
        linkedList.add("o3");
        linkedList.set(2, "o4");
        assertEquals(linkedList.get(2), "o4");
    }

    @Test
    public void testIndexOf() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o2");
        linkedList.add("o3");
        assertEquals(linkedList.indexOf("o2"), 1);

    }

    @Test
    public void testLastIndexOf() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("o1");
        linkedList.add("o44");
        linkedList.add("o2");
        linkedList.add("o3");
        linkedList.add("o3");
        linkedList.add("o3");
        linkedList.add("o3");
        linkedList.add("o2");
        assertEquals(linkedList.lastIndexOf("o3"), 6);
    }

    @Test
    public void testRemove() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("o");
        linkedList.add("j");
        linkedList.remove(2);
        // assertEquals(linkedList.get(0),"k");
        for (int i = 0; i < linkedList.size; i++)
            System.out.println(linkedList.get(i));
    }

    @Test
    public void testSubList() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("o");
        linkedList.add("j");
        LinkedList linkedList1 = (LinkedList) linkedList.subList(0, 2);
        for (int i = 0; i < linkedList1.size; i++)
            System.out.println(linkedList1.get(i));
        System.out.println(linkedList1.size);

    }

    @Test
    public void testContains() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("o");
        linkedList.add("j");
        assertTrue(linkedList.contains("k"));
    }

    @Test
    public void testTestRemove() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("j");
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("j");
        linkedList.add("j");
        linkedList.add("o");
        linkedList.add("j");
        assertTrue(linkedList.remove("j"));
        for (int i = 0; i < linkedList.size; i++)
            System.out.println(linkedList.get(i));
        System.out.println(linkedList.size);
    }

    @Test
    public void testIterator() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("j");
        linkedList.add("k");
        linkedList.add("h");
        linkedList.add("j");
        linkedList.add("j");
        linkedList.add("o");
        linkedList.add("j");
        for (Object o : linkedList) System.out.println(o);
    }
}