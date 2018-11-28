package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class FiftyPercentDiscount implements Broadcast {

    private String senderId;

    public FiftyPercentDiscount(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

}
