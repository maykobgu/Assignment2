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
    private ConcurrentHashMap<Class<? extends Message>, Queue<MicroService>> queuesByEvent;
    private ConcurrentHashMap<MicroService, ArrayBlockingQueue> queues;


    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {
        queuesByEvent = new ConcurrentHashMap<>();
        queues = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBusImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        if (queuesByEvent.get(type) == null) {
            ArrayBlockingQueue arr = new ArrayBlockingQueue<>(1000);
            synchronized (queuesByEvent) {
                queuesByEvent.put(type, arr);
                queuesByEvent.get(type).add(m);
            }
        } else synchronized (queuesByEvent) {
            queuesByEvent.get(type).add(m);
        }
        // TODO capacity?
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        if (queuesByEvent.get(type) == null) {
            queuesByEvent.put(type, new ArrayBlockingQueue<>(1000));
            queuesByEvent.get(type).add(m);
        } else queuesByEvent.get(type).add(m);
        // TODO capacity?
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        e.getFuture().resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
//        System.out.println("sending broadcast  " + b);
        while (queuesByEvent.get(b.getClass()) == null || queuesByEvent.get(b.getClass()).isEmpty()) ;
        Queue q = queuesByEvent.get(b.getClass());
        synchronized (q) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                MicroService m = (MicroService) q.poll();
                queues.get(m).add(b);
                q.add(m);
            }
        }
//        notifyAll();
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        while (queuesByEvent.get(e.getClass()) == null || queuesByEvent.get(e.getClass()).isEmpty()) ;
        MicroService m = queuesByEvent.get(e.getClass()).poll(); //get the first micro service
        ArrayBlockingQueue q = queues.get(m);
        synchronized (q) {
            try {
                q.put(e); //find the relevant queue and push the message
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            queuesByEvent.get(e.getClass()).add(m);
        }//push the micro service back to it's roundRobins queue
        return e.getFuture();
    }


    @Override
    public void register(MicroService m) {
        // TODO capacity?
        queues.put(m, new ArrayBlockingQueue<>(100));
        queues.get(0);
    }

    @Override
    public void unregister(MicroService m) {
        queues.remove(m);
    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        if (queues.get(m) == null)
            throw new NullPointerException();
        return (Message) (queues.get(m).take()); //takes a message from the queue
    }

}
