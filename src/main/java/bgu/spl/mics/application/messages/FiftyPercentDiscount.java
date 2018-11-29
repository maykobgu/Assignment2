package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;

public class FiftyPercentDiscount implements Broadcast {
    private Future future;
    private String senderId;

    public FiftyPercentDiscount(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }
    public Future getFuture() {
        return future;
    }
}
