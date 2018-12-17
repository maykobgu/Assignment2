package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Customer;

public class TickBroadcast implements Broadcast {

    private int tickNumber;

    public TickBroadcast(int tickNumber) {
        this.tickNumber=tickNumber;

    }

    public int getCurrentTick(){
        return tickNumber;
    }

}