package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import com.sun.tools.javac.util.Pair;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import com.sun.tools.javac.util.Pair;

import java.util.HashMap;

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
    private TimeService time;

    public APIService(Customer customer, int speed, int duration) {
        super("APIService");
        this.customer = customer;
        time = new TimeService(speed, duration);
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, this::act);
        time.run();
        for (Pair<String, Integer> book : customer.getOrderSchedule()) {
            OrderBookEvent order = new OrderBookEvent(customer, book.fst);
            Future result = sendEvent(order); //last result- book taken or not
            if (result != null) {
                customer.addReceipt((OrderReceipt) result.get());
                DeliveryEvent deliver = new DeliveryEvent(customer);
                sendEvent(deliver); //does not need to wait
            }

        }
//        subscribeEvent(CheckAvailability.class, this::processEvent);
    }

    private void act(TickBroadcast e) {
// TICKS
    }


}