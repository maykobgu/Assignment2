package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.InventoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.jvm.hotspot.runtime.Bytes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static bgu.spl.mics.application.passiveObjects.OrderResult.*;
import static org.junit.Assert.*;

public class InventoryTest {
    Inventory inv;
    BookInventoryInfo[] arr;
    HashMap<String, Integer> output;

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
        getInstance();
    }

    @Test
    public void load() throws Exception {
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
        assertEquals(NOT_IN_STOCK, inv.take(arr[2].getBookTitle()));
        assertEquals(NOT_IN_STOCK, inv.take("my book"));
        assertEquals(SUCCESSFULLY_TAKEN, inv.take(arr[0].getBookTitle()));
    }


    @Test
    public void checkAvailabiltyAndGetPrice() throws Exception {
        assertEquals(10, inv.checkAvailabiltyAndGetPrice(arr[0].getBookTitle()));
        assertEquals(100, inv.checkAvailabiltyAndGetPrice(arr[1].getBookTitle()));
        assertEquals(-1, inv.checkAvailabiltyAndGetPrice(arr[2].getBookTitle()));
        assertEquals(-1, inv.checkAvailabiltyAndGetPrice("my book"));
    }

    @Test
    public void printInventoryToFile() throws Exception {
//        save to file
        inv.printInventoryToFile("file");
        String readFromFile = new String(Files.readAllBytes(Paths.get("file")));
        String map = "harry potter:5,snow white:3,little prince:0";
        assertEquals(map, readFromFile);
    }

    @After
    public void tearDown() {
        inv = null;
    }

}