package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

public class FutureTest {
    private Future newFuture;

    @Before
    public void setUp() {
        newFuture = new Future();
    }

    @Test
    public void get() throws Exception {
        assertNull(newFuture.get());
        newFuture.resolve("resolve");
        assertEquals("resolve", newFuture.get());
        newFuture.resolve('s');
        assertEquals('s', newFuture.get());
        newFuture.resolve(2);
        assertEquals(2, newFuture.get());
        newFuture.resolve(3.5);
        assertEquals(3.5, newFuture.get());
    }

    @Test
    public void resolve() throws Exception {
        newFuture.resolve("resolve");
        assertEquals("resolve", newFuture.get());
        newFuture.resolve('s');
        assertEquals('s', newFuture.get());
        newFuture.resolve(2);
        assertEquals(2, newFuture.get());
        newFuture.resolve(3.5);
        assertEquals(3.5, newFuture.get());
    }

    @Test
    public void isDone() throws Exception {
        assertFalse(newFuture.isDone());
        newFuture.resolve("resolve");
        assertTrue(newFuture.isDone());
        newFuture.resolve(null);
        assertFalse(newFuture.isDone());
    }

    @Test
    public void getTimeout() throws Exception {
        assertNull(newFuture.get(10, SECONDS));
        newFuture.resolve("resolve");
        assertEquals("resolve", newFuture.get(5, SECONDS));
        newFuture.resolve('s');
        assertEquals('s', newFuture.get(100, MILLISECONDS));
        newFuture.resolve(2);
        assertEquals(2, newFuture.get(1, SECONDS));
        newFuture.resolve(3.5);
        assertEquals(3.5, newFuture.get(2, SECONDS));
    }

    @After
    public void tearDown() {
        newFuture = null;
    }
}