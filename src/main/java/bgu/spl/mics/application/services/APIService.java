package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.passiveObjects.Customer;
import javafx.util.Pair;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService {
    private Customer customer;

    public APIService(Customer customer) {
        super("APIService");
        this.customer = customer;
    }

    @Override
    protected void initialize() {
        for (Pair<String, Integer> book : customer.getOrderSchedule()) {
            OrderBookEvent order = new OrderBookEvent(customer, book.getKey());
            Future result = sendEvent(order); //last result- book taken or not

        }
//        subscribeEvent(CheckAvailability.class, this::processEvent);
    }

    private void processEvent(CheckAvailability e) {
//        int result = inventory.checkAvailabiltyAndGetPrice(e.getBook());
//        if (result != -1)
//            this.complete((Event) e, true);
//        else this.complete((Event) e, false);
    }
}