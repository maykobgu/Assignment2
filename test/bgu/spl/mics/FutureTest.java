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
        try {
            assertNull(newFuture.get());
        } catch (Exception e) {
            fail("the future object supposed to be null");
        }
        try {
            newFuture.resolve("resolve");
            assertEquals("resolve", newFuture.get());
        } catch (Exception e) {
            fail("String object should be supported");
        }
        try {
            newFuture.resolve('s');
            assertEquals('s', newFuture.get());
        } catch (Exception e) {
            fail("char object should be supported");
        }
        try {
            newFuture.resolve(2);
            assertEquals(2, newFuture.get());
        } catch (Exception e) {
            fail("int object should be supported");
        }
        try {
            newFuture.resolve(3.5);
            assertEquals(3.5, newFuture.get());
        } catch (Exception e) {
            fail("double object should be supported");
        }
    }

    @Test
    public void resolve() throws Exception {
        try {
            newFuture.resolve("resolve");
            assertEquals("resolve", newFuture.get());
        } catch (Exception e) {
            fail("String object should be supported");
        }

        try {
            newFuture.resolve('s');
            assertEquals('s', newFuture.get());
        } catch (Exception e) {
            fail("char object should be supported");
        }

        try {
            newFuture.resolve(2);
            assertEquals(2, newFuture.get());
        } catch (Exception e) {
            fail("int object should be supported");
        }

        try {
            newFuture.resolve(3.5);
            assertEquals(3.5, newFuture.get());
        } catch (Exception e) {
            fail("double object should be supported");
        }
    }

    @Test
    public void isDone() throws Exception {
        try {
            assertFalse(newFuture.isDone());
        } catch (Exception e) {
            fail("Future object supposed to be false");
        }

        try {
            newFuture.resolve("resolve");
            assertTrue(newFuture.isDone());
        } catch (Exception e) {
            fail("Future object supposed to be true");
        }

        try {
            newFuture.resolve(null);
            assertFalse(newFuture.isDone());
        } catch (Exception e) {
            fail("Future object supposed to be false");
        }
    }

    @Test
    public void getTimeout() throws Exception {
        try {
            assertNull(newFuture.get(1, SECONDS));
        } catch (Exception e) {
            fail("Future object supposed to be null");
        }

        try {
            newFuture.resolve("resolve");
            assertEquals("resolve", newFuture.get(1, SECONDS));
        } catch (Exception e) {
            fail("String object should be supported");
        }

        try {
            newFuture.resolve('s');
            assertEquals('s', newFuture.get(1, MILLISECONDS));
        } catch (Exception e) {
            fail("char object should be supported");
        }

        try {
            newFuture.resolve(2);
            assertEquals(2, newFuture.get(1, SECONDS));
        } catch (Exception e) {
            fail("int object should be supported");
        }

        try {
            newFuture.resolve(3.5);
            assertEquals(3.5, newFuture.get(2, SECONDS));
        } catch (Exception e) {
            fail("double object should be supported");
        }
    }

    @After
    public void tearDown() {
        newFuture = null;
    }
}