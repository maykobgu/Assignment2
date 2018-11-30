package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;

public class AcquireVehicleEvent implements Event<String> {
    private DeliveryEvent deliveryEvent;
    private Future future;

    public AcquireVehicleEvent(DeliveryEvent e) {
        deliveryEvent = e;
    }

    public Future getFuture() {
        return future;
    }
    public DeliveryEvent getDeliveryEvent() {
        return deliveryEvent;
    }

}