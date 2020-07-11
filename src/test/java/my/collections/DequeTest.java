package my.collections;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.testng.Assert.*;

/**
 * Тестирование функциональности классов, наследников {@link Deque}.
 */
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

        deque.addFirst(5);
        assertEquals(deque.getFirst(), 5);
    }

    @Test(dataProvider = "deques")
    public void addLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        deque.addLast(5);
        assertEquals(deque.getLast(), 5);
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void getFirst(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.getFirst(), 1);

        deque.clear();
        deque.getFirst();
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void getLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.getLast(), 4);

        deque.clear();
        deque.getLast();
    }

    @Test(dataProvider = "deques")
    public void pollFirst(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.pollFirst(), 1);
        assertEquals(deque.pollFirst(), 2);
        assertEquals(deque.pollFirst(), 3);
        assertEquals(deque.pollFirst(), 4);
        assertNull(deque.pollFirst());
    }

    @Test(dataProvider = "deques")
    public void pollLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.pollLast(), 4);
        assertEquals(deque.pollLast(), 3);
        assertEquals(deque.pollLast(), 2);
        assertEquals(deque.pollLast(), 1);
        assertNull(deque.pollLast());
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void removeFirst(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.removeFirst(), 1);
        assertEquals(deque.removeFirst(), 2);
        assertEquals(deque.removeFirst(), 3);
        assertEquals(deque.removeFirst(), 4);
        deque.removeFirst();
    }

    @Test(dataProvider = "deques", expectedExceptions = NoSuchElementException.class)
    public void removeLast(Deque deque) {
        deque.add(1);
        deque.add(2);
        deque.add(3);
        deque.add(4);

        assertEquals(deque.removeLast(), 4);
        assertEquals(deque.removeLast(), 3);
        assertEquals(deque.removeLast(), 2);
        assertEquals(deque.removeLast(), 1);
        deque.removeLast();
    }
}