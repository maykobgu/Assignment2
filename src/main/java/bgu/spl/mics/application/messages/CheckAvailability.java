package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;

public class CheckAvailability implements Event<String> {
    private Customer customer;
    private String book;
    private Future future;

    public CheckAvailability(Customer customer, String book) {
        this.customer = customer;
        this.book = book;
    }

    public String getBook() {
        return book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Future getFuture() {
        return future;
    }


}