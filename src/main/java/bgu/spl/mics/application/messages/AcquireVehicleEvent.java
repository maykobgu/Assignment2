package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;

public class AcquireVehicleEvent implements Event<String> {
    private Future future;

    public AcquireVehicleEvent() {
        future= new Future();

    }

    public Future getFuture() {
        return future;
    }

}