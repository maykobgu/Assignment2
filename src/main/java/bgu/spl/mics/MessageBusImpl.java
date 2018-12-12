package bgu.spl.mics;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private static ConcurrentHashMap<Class, Queue<MicroService>> queuesByEvent;
    private static ConcurrentHashMap<MicroService, ArrayBlockingQueue> queues;


    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBusImpl getInstance() {
        queuesByEvent = new ConcurrentHashMap<>();
        queues = new ConcurrentHashMap<>();
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        if (queuesByEvent.get(type.getClass()) == null) {
            queuesByEvent.put(type, new ArrayBlockingQueue<>(1000));
            queuesByEvent.get(type.getClass()).add(m);
        } else queuesByEvent.get(type.getClass()).add(m);
        // TODO capacity?
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        if (queuesByEvent.get(type.getClass()) == null) {
            queuesByEvent.put(type, new ArrayBlockingQueue<>(1000));
            queuesByEvent.get(type.getClass()).add(m);
        } else queuesByEvent.get(type.getClass()).add(m);
        // TODO capacity?
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        e.getFuture().resolve(result);
        notifyAll();
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        Queue q = queuesByEvent.get(b.getClass());
        while (q == null || q.isEmpty()) ;
        int size = q.size();
        for (int i = 0; i < size; i++) {
            MicroService m = (MicroService) q.poll();
            queues.get(m).add(b);
            q.add(m);
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        while (queuesByEvent.get(e.getClass()) == null || queuesByEvent.get(e.getClass()).isEmpty()) ;
        MicroService m = queuesByEvent.get(e.getClass()).poll(); //get the first micro service
        queues.get(m).add(e); //find the relevant queue and push the message
        queuesByEvent.get(e.getClass()).add(m); //push the micro service back to it's roundRobins queue
        return e.getFuture();
    }


    @Override
    public void register(MicroService m) {
        // TODO capacity?
        queues.put(m, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void unregister(MicroService m) {
        queues.remove(m);
    }

    @Override
    public Message awaitMessage(MicroService m) {
        ArrayBlockingQueue mqueue = queues.get(m);
        while (mqueue.isEmpty()) ;  //waits for message to be available
        return (Message) mqueue.poll();  //takes a message from the queue
    }

}
