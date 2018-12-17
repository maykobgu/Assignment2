package bgu.spl.mics.example.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class ExampleEvent implements Event<String> {
private Future future;
    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
        future = null;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public Future getFuture() {
        if (future == null)
            future = new Future();
        return future;
    }
}