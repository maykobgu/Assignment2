package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService {
    private MoneyRegister moneyRegister = MoneyRegister.getInstance();
    private int currentTick;

    public SellingService() {
        super("Change_This_Name");
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeEvent(OrderBookEvent.class, this::processEvent);
        subscribeBroadcast(TickBroadcast.class, this::act);
    }

    private void processEvent(OrderBookEvent e) {
        CheckAvailability check = new CheckAvailability(e.getCustomer(), e.getBookTitle());
        Future price = sendEvent(check);
        OrderReceipt receipt = null;
        if ((int) price.get() != -1) {
            receipt = MoneyRegister.createReceipt(e.getCustomer().getName(), e.getCustomer().getId(),
                    e.getBookTitle(), (int) price.get(), currentTick, e.getOrderedTick(), currentTick); //make receipt
            moneyRegister.chargeCreditCard(e.getCustomer(), (int) price.get()); //charge the customer for this book
            moneyRegister.file(receipt);
            notifyAll();
        }
        this.complete((Event) e, receipt);
    }

    private void act(TickBroadcast e) {
        currentTick = e.getCurrentTick();
    }
}
