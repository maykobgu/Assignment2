package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import com.sun.tools.javac.util.Pair;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for ob,jects which it is not responsible for:
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
        subscribeEvent(TickBroadcast.class, this::act);
        for (Pair<String, Integer> book : customer.getOrderSchedule()) {
            OrderBookEvent order = new OrderBookEvent(customer, book.fst);
            Future result = sendEvent(order); //last result- receipt or null
            if (result != null) {
                customer.addReceipt((OrderReceipt) result.get());
                DeliveryEvent deliver = new DeliveryEvent(customer);
                Future deliveryResult = sendEvent(deliver);
            }
        }
    }

    private void act(TickBroadcast e) {
// TICKS
    }
}