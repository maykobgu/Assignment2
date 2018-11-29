package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private static MessageBusImpl instance = null;

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBusImpl getInstance(){
        if(instance == null) {
            instance = new MessageBusImpl();
        }
        return instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        // TODO Auto-generated method stub

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
        return null;
    }

    @Override
    public void register(MicroService m) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregister(MicroService m) {
        // TODO Auto-generated method stub

    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }



}
