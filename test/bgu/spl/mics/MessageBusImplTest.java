package bgu.spl.mics;

import bgu.spl.mics.application.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageBusImplTest {
    MessageBusImpl bus;
    MicroService api;
    MicroService inventory;
    MicroService logistic;
    MicroService resource;
    MicroService selling;
    MicroService time;

    @Before
    public void setUp() {
        bus = new MessageBusImpl();
        api = new APIService();
        inventory = new InventoryService();
        logistic = new LogisticsService();
        resource = new ResourceService();
        selling = new SellingService();
        time = new TimeService();
    }

    @Test
    public void subscribeEvent() throws Exception {
//        bus.subscribeEvent(, api);
    }

    @Test
    public void subscribeBroadcast() throws Exception {
    }

    @Test
    public void complete() throws Exception {
    }

    @Test
    public void sendBroadcast() throws Exception {
    }

    @Test
    public void sendEvent() throws Exception {
    }

    @Test
    public void register() throws Exception {
    }

    @Test
    public void unregister() throws Exception {
    }

    @Test
    public void awaitMessage() throws Exception {
    }

    @After
    public void tearDown() {
        bus = null;
    }
}