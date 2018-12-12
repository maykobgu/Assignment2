package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;

public class TerminateBroadcast implements Broadcast {

    private Future future;

    public TerminateBroadcast() {
        future = null;
    }

    public Future getFuture() {
        return future;
    }

}