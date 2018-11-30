package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.OrderBookEvent;

import java.util.HashMap;

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

    public SellingService() {
        super("Change_This_Name");
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeEvent(OrderBookEvent.class, this::processEvent);
    }

    private void processEvent(OrderBookEvent e) {
        CheckAvailability check = new CheckAvailability(e.getCustomer(), e.getBookTitle());
        Future result = sendEvent(check);
        this.complete((Event) e, result.get());
    }

}
