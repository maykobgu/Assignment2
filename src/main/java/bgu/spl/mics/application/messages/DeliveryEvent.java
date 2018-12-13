package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class DeliveryEvent implements Event<String> {
    private Future future;
    private String adress;
    private int distance;

    public DeliveryEvent(Customer customer) {
        this.adress = customer.getAddress();
        this.distance = customer.getDistance();
        future= new Future();

    }

    public Future getFuture() {
        return future;
    }

    public String getAdress() {
        return adress;
    }

    public int getDistance() {
        return distance;
    }

}