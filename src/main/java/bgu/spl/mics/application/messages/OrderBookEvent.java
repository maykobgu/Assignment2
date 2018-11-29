package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;

public class OrderBookEvent implements Event<String> {
    private Customer customer;
    private String bookTitle;
    private Future future;

    public OrderBookEvent(Customer customer, String bookTitle) {
        this.customer = customer;
        this.bookTitle = bookTitle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getBookTitle() {
        return bookTitle;
    }


    public Future getFuture() {
        return future;
    }

}