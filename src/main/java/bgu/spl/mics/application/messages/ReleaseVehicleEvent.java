package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class ReleaseVehicleEvent implements Event<String> {
    private DeliveryVehicle vehicle;
    private Future future;

    public ReleaseVehicleEvent(DeliveryVehicle v) {
        vehicle = v;
    }

    public Future getFuture() {
        return future;
    }
    public DeliveryVehicle getVehicle() {
        return vehicle;
    }

}