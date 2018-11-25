package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.InventoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {
    Inventory inv;
    InventoryService inventService;
    BookInventoryInfo[] arr;

    @Before
    public void setUp() {
        inv = new Inventory();
        inventService = new InventoryService();
    }

    @Test
    public void getInstance() throws Exception {
        getInstance();
    }

    @Test
    public void load() throws Exception {
        BookInventoryInfo book0 = new BookInventoryInfo("harry potter", 5, 10);
        BookInventoryInfo book1 = new BookInventoryInfo("snow white", 3, 100);
        BookInventoryInfo book2 = new BookInventoryInfo("little prince", 0, 20);
        arr[0] = book0;
        arr[1] = book1;
        arr[2] = book2;
        inv.load(arr);
        assertEquals("harry potter", arr[0].getBookTitle());
        assertEquals(5, arr[0].getAmountInInventory());
        assertEquals(10, arr[0].getPrice());
        assertEquals("snow white", arr[1].getBookTitle());
        assertEquals(3, arr[1].getAmountInInventory());
        assertEquals(100, arr[1].getPrice());
        assertEquals("little prince", arr[2].getBookTitle());
        assertEquals(0, arr[2].getAmountInInventory());
        assertEquals(20, arr[2].getPrice());
    }

    @Test
    public void take() throws Exception {
    }

    @Test
    public void checkAvailabiltyAndGetPrice() throws Exception {
    }

    @Test
    public void printInventoryToFile() throws Exception {
    }

    @After
    public void tearDown() {
        inv = null;
        inventService = null;
    }

}