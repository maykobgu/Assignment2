package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderResult;

import java.util.HashMap;

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
    private MoneyRegister moneyRegister = MoneyRegister.getInstance();

    public InventoryService() {
        super("InventoryService");
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeEvent(CheckAvailability.class, this::processEvent);
        subscribeBroadcast(TickBroadcast.class, this::act);

    }

    private void processEvent(CheckAvailability e) {
        OrderReceipt paid = null;
        int result = inventory.checkAvailabiltyAndGetPrice(e.getBook());
        if (result != -1) {
            if (e.getCustomer().getAvailableCreditAmount() >= result) {
                OrderResult orderResult = inventory.take(e.getBook()); //attempt to take book
                if (orderResult == SUCCESSFULLY_TAKEN) {
                    OrderReceipt receipt = MoneyRegister.createReceipt(e.getCustomer().getName(), e.getCustomer().getId(),
                            e.getBook(), inventory.getPrice(e.getBook()), 0, 0, 0); //make receipt
                    moneyRegister.chargeCreditCard(e.getCustomer(), inventory.getPrice(e.getBook())); //charge the customer for this book
                    moneyRegister.file(receipt);
                    paid = receipt;
                    //TODO change all the zeroes
                }
            }
        }
        this.complete((Event) e, paid);
    }

    private void act(TickBroadcast e) {
    }
}