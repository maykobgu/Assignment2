package bgu.spl.mics;

import java.util.LinkedList;
import com.sun.tools.javac.util.Pair;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<MicroService, ArrayBlockingQueue<Message>> queues; //maybe we need to save the name of the microservice instead of the microservice itself
    private ConcurrentHashMap<Class, MicroService> eventMapping;
    private List <Pair<Class, MicroService>> tickBroadcastMapping;


    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBusImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        eventMapping.put(type.getClass(), m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        tickBroadcastMapping.add(new Pair<>(type.getClass(), m));
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        e.getFuture().resolve(result);
        notifyAll();
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        for (int i = 0; i < tickBroadcastMapping.size(); i++) {
            MicroService m;
            if (tickBroadcastMapping.get(i).fst == b.getClass()) {
                m = tickBroadcastMapping.get(i).snd;
                queues.get(m).add(b);
            }
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        MicroService m = eventMapping.get(e.getClass());
        if (m == null) return null;
        queues.get(m).add(e);
        while (e.getFuture().isDone()) ;
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
