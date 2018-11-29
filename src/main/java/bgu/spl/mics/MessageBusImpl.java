package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private static MessageBusImpl instance = null;
    private HashMap<MicroService, ArrayBlockingQueue<Message>> queues;

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBusImpl getInstance() {
        if (instance == null) {
            instance = new MessageBusImpl();
        }
        return instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        // TODO Auto-generated method stub
//        m.subscribeEvent(type,  );
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBroadcast(Broadcast b) {
        // TODO Auto-generated method stub

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        // TODO Auto-generated method stub

//        inserts a message to the queue
        return null;
    }

    @Override
    public void register(MicroService m) {
        // TODO Auto-generated method stub
        queues.put(m, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void unregister(MicroService m) {
        // TODO Auto-generated method stub
        queues.remove(m);
    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        // TODO Auto-generated method stub
        ArrayBlockingQueue mqueue = queues.get(m);
        while (mqueue.isEmpty());  //waits for message to be available
        return (Message) mqueue.poll();  //takes a message from the queue
    }
}