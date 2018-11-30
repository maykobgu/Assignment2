package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import com.sun.tools.javac.util.Pair;

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
    private TimeService time;

    public APIService(Customer customer, int speed, int duration) {
        super("APIService");
        this.customer = customer;
        time = new TimeService(speed, duration);
    }

    @Override
    protected void initialize() {
        time.run();
        for (Pair<String, Integer> book : customer.getOrderSchedule()) {
            OrderBookEvent order = new OrderBookEvent(customer, book.fst);
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