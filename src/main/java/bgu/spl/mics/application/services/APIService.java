package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.OrderBookEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import javafx.util.Pair;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.List;


/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link OrderBookEvent}.
 * This class may not hold references for ob,jects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService {
    private Customer customer;
    private List<Pair<String, Integer>> orderSchedule;

    public APIService(Customer customer,String name) {
//        super("APIService");
        super(name);
        this.customer = customer;
        orderSchedule = customer.getOrderSchedule();
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, this::act);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);
    }

    private void act(TickBroadcast e) {
        for (int i = 0; i < orderSchedule.size(); i++) {
            if (orderSchedule.get(i).getValue() == e.getCurrentTick()) {
                OrderBookEvent order = new OrderBookEvent(customer, orderSchedule.get(i).getKey(), e.getCurrentTick());
                Future result = sendEvent(order); //last result- receipt
                if (result.get()!= null && !result.get().equals(-1)) {
                    customer.addReceipt((OrderReceipt) result.get());
                    DeliveryEvent deliver = new DeliveryEvent(customer);
                    sendEvent(deliver); //does not need to wait
                }
            }
        }
    }

    private void finish(TerminateBroadcast e) {
        terminate();
    }
}