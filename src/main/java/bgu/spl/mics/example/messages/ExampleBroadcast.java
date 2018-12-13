package bgu.spl.mics.example.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;

public class ExampleBroadcast implements Broadcast {
    private Future future;
    private String senderId;

    public ExampleBroadcast(String senderId) {
        this.senderId = senderId;
        future = new Future();
    }

    public String getSenderId() {
        return senderId;
    }

    @Override
    public Future getFuture() {
        return future;
    }
}
