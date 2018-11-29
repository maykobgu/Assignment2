package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderResult;

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

    public InventoryService() {
        super("InventoryService");
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeEvent(CheckAvailability.class, this::processEvent);
    }

    private void processEvent(CheckAvailability e) {
        boolean taken = false;
        int result = inventory.checkAvailabiltyAndGetPrice(e.getBook());
        if (result != -1) {
            if (e.getCustomer().getAvailableCreditAmount() >= result) {
                OrderResult orderResult = inventory.take(e.getBook()); //attempt to take book
                if (orderResult == SUCCESSFULLY_TAKEN) {
                    OrderReceipt receipt = new OrderReceipt(0, e.getCustomer().getName(), e.getCustomer().getId(),
                            e.getBook(), inventory.getPrice(e.getBook()), 0, 0, 0);
                    //make receipt
                    e.getCustomer().getCustomerReceiptList().add(receipt);  //added the receipt to customer receipts list
                    e.getCustomer().charge(inventory.getPrice(e.getBook()));  //charge the customer for this book
                    taken = true;
                    //TODO change all the zeroes
                    //TODO add receipt to money register - pass by the messagebus with future as result
                }
            }
        }
        this.complete((Event) e, taken);
    }
}
