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
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);
        deque.add(5);
        deque.add(6);
        deque.add(7);
        deque.addFirst(8);
        assertEquals(deque.getFirst(), 8);
    }

    @Test(dataProvider = "deques")
    public void addLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);
        deque.add(5);
        deque.add(6);
        deque.add(7);
        deque.addLast(8);
        assertEquals(deque.getLast(), 8);
    }

    @Test(dataProvider = "deques")
    public void pollFirst(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);
        assertEquals(deque.pollFirst(), 1);
        assertEquals(deque.getFirst(), 2);
    }

    @Test(dataProvider = "deques")
    public void pollLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);
        deque.add(5);
        deque.add(6);
        deque.add(7);
        assertEquals(deque.pollLast(), 7);
        assertEquals(deque.getLast(), 6);
    }

    @Test(dataProvider = "deques")
    public void removeFirst(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);
        assertEquals(deque.removeFirst(), 1);
        assertEquals(deque.getFirst(), 2);
    }

    @Test(dataProvider = "deques")
    public void removeLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);
        assertEquals(deque.removeLast(), 4);
        assertEquals(deque.getLast(), 3);
    }
}