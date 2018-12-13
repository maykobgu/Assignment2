package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import static bgu.spl.mics.application.passiveObjects.OrderResult.*;


/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService {
    private Inventory inventory = Inventory.getInstance();

    public InventoryService(BookInventoryInfo[] info) {
        super("InventoryService");
        inventory.load(info);
    }

    @Override
    protected void initialize() {
        subscribeEvent(CheckAvailability.class, this::processEvent);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);

    }

    private synchronized void processEvent(CheckAvailability e) {
        System.out.println(" got CheckAvailability ");
        OrderResult orderResult = null;
        int price = -1;
        int currentAmount = e.getCustomer().getAvailableCreditAmount();
        if (currentAmount >= inventory.getPrice(e.getBook())) {
            orderResult = inventory.take(e.getBook()); //attempt to take book
            if (orderResult == SUCCESSFULLY_TAKEN) {
                price = inventory.getPrice(e.getBook());
            }
        }
        if (price == -1)
            System.out.println("customer doesn't have enough money");
        this.complete((Event) e, price);
        if (orderResult == SUCCESSFULLY_TAKEN)
            while (e.getCustomer().getAvailableCreditAmount() !=
                    (currentAmount - inventory.getPrice(e.getBook()))) ;
    }

    private void finish(TerminateBroadcast e) {
        terminate();
    }

}