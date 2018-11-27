package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

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
//        TODO
    }

    @Test
    public void load() throws Exception {
//        TODO
//        inv.load(arr);
//        assertEquals("harry potter", arr[0].getBookTitle());
//        assertEquals(5, arr[0].getAmountInInventory());
//        assertEquals(10, arr[0].getPrice());
//        assertEquals("snow white", arr[1].getBookTitle());
//        assertEquals(3, arr[1].getAmountInInventory());
//        assertEquals(100, arr[1].getPrice());
//        assertEquals("little prince", arr[2].getBookTitle());
//        assertEquals(0, arr[2].getAmountInInventory());
//        assertEquals(20, arr[2].getPrice());
    }

    @Test
    public void take() throws Exception {
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
//        inv.printInventoryToFile("file");
//        String readFromFile = new String(Files.readAllBytes(Paths.get("file")));
//        String map = "harry potter:5,snow white:3,little prince:0";
//        assertEquals(map, readFromFile);
    }

    @After
    public void tearDown() {
        inv = null;
        arr = null;
    }
}