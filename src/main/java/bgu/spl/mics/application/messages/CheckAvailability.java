package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class CheckAvailability implements Event<String>{

    private String senderName;

    public CheckAvailability(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}