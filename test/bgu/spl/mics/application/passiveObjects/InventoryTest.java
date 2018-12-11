package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static bgu.spl.mics.application.passiveObjects.OrderResult.*;
import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory inv;
    private BookInventoryInfo[] arr;

    @Before
    public void setUp() {
        inv = new Inventory();
        BookInventoryInfo book0 = new BookInventoryInfo("harry potter", 5, 10);
        BookInventoryInfo book1 = new BookInventoryInfo("snow white", 3, 100);
        BookInventoryInfo book2 = new BookInventoryInfo("little prince", 0, 20);
        arr = new BookInventoryInfo[3];
        arr[0] = book0;
        arr[1] = book1;
        arr[2] = book2;
    }

    @Test
    public void getInstance() throws Exception {
        try {
            inv = null;
            assertNotNull(inv.getInstance());
        } catch (Exception e) {
            fail("instance should not be null");
        }
    }

    @Test
    public void load() throws Exception {
        inv.load(arr);
        try {
            assertSame(arr, inv.getInventory());
        } catch (Exception e) {
            fail("inventory didnt load as expected");
        }
    }

    @Test
    public void take() throws Exception {
        inv.load(arr);
        try {
            assertEquals(NOT_IN_STOCK, inv.take(arr[2].getBookTitle()));
        } catch (Exception e) {
            fail("result should be NOT_IN_STOCK");
        }
        try {
            assertEquals(NOT_IN_STOCK, inv.take("my book"));
        } catch (Exception e) {
            fail("result should be NOT_IN_STOCK");
        }
        try {
            assertEquals(SUCCESSFULLY_TAKEN, inv.take(arr[0].getBookTitle()));
        } catch (Exception e) {
            fail("result should be SUCCESSFULLY_TAKEN");
        }
    }


    @Test
    public void checkAvailabiltyAndGetPrice() throws Exception {
        inv.load(arr);
        try {
            assertEquals(10, inv.checkAvailabiltyAndGetPrice(arr[0].getBookTitle()));
        } catch (Exception e) {
            fail("expected result: 10, got: " + inv.checkAvailabiltyAndGetPrice(arr[0].getBookTitle()));
        }
        try {
            assertEquals(100, inv.checkAvailabiltyAndGetPrice(arr[1].getBookTitle()));
        } catch (Exception e) {
            fail("expected result: 100, got: " + inv.checkAvailabiltyAndGetPrice(arr[1].getBookTitle()));
        }
        try {
            assertEquals(-1, inv.checkAvailabiltyAndGetPrice(arr[2].getBookTitle()));
        } catch (Exception e) {
            fail("expected result: -1, got: " + inv.checkAvailabiltyAndGetPrice(arr[2].getBookTitle()));
        }
        try {
            assertEquals(-1, inv.checkAvailabiltyAndGetPrice("my book"));
        } catch (Exception e) {
            fail("expected result: -1, got: " + inv.checkAvailabiltyAndGetPrice("my book"));
        }
    }

    @Test
    public void printInventoryToFile() throws Exception {
//        TODO - dont implement
    }

    @Test
    public void getInventory() {
        inv.load(arr);
        try {
            assertSame(arr, inv.getInventory());
        } catch (Exception e) {
            fail("get inventory dosent work as expected");
        }
    }

    @After
    public void tearDown() {
        inv = null;
        arr = null;
    }
}