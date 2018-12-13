package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;

public class TerminateBroadcast implements Broadcast {

    private Future future;

    public TerminateBroadcast() {
        future= new Future();

    }

    public Future getFuture() {
        return future;
    }
    public void setFuture(Future future) {
        this.future = future;
    }
}