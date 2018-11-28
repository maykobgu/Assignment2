package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class OrderBookEvent implements Event<String>{

    private String senderName;

    public OrderBookEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}