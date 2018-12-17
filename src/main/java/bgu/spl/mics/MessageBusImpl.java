package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<Class<? extends Message>, Queue<MicroService>> queuesByEvent;
    private ConcurrentHashMap<MicroService, ArrayBlockingQueue<Message>> queues;
    private ConcurrentHashMap<MicroService, LinkedList<Class<? extends Message>>> eventMapping;
    private ConcurrentHashMap<MicroService, LinkedList<Class<? extends Message>>> broadcastMapping;


    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {
        queuesByEvent = new ConcurrentHashMap<>();
        queues = new ConcurrentHashMap<>();
        eventMapping = new ConcurrentHashMap<>();
        broadcastMapping = new ConcurrentHashMap<>();
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
        if (eventMapping.get(type) == null) {
            LinkedList<Class<? extends Message>> list = new LinkedList<>();
            synchronized (eventMapping) {
                eventMapping.put(m, list);
                eventMapping.get(m).add(type);
            }
        } else synchronized (eventMapping) {
            eventMapping.get(m).add(type);
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        if (queuesByEvent.get(type) == null) {
            queuesByEvent.put(type, new ArrayBlockingQueue<>(1000));
            queuesByEvent.get(type).add(m);
        } else queuesByEvent.get(type).add(m);
        if (broadcastMapping.get(type) == null) {
            LinkedList<Class<? extends Message>> list = new LinkedList<>();
            synchronized (broadcastMapping) {
                broadcastMapping.put(m, list);
                broadcastMapping.get(m).add(type);
            }
        } else synchronized (broadcastMapping) {
            broadcastMapping.get(m).add(type);
        }
        // TODO capacity?
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        e.getFuture().resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        if (queuesByEvent.get(b.getClass()) != null && !queuesByEvent.get(b.getClass()).isEmpty()) {
            Queue q = queuesByEvent.get(b.getClass());
            synchronized (q) {
                int size = q.size();
                for (int i = 0; i < size; i++) {
                    MicroService m = (MicroService) q.poll();
                    Queue tmp = queues.get(m);
                    if (tmp != null)
                        tmp.add(b);
                    q.add(m);
                }
            }
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        if (queuesByEvent.get(e.getClass()) != null && !queuesByEvent.get(e.getClass()).isEmpty()) {
            synchronized (queuesByEvent) {
                MicroService m = queuesByEvent.get(e.getClass()).poll(); //get the first micro service
                ArrayBlockingQueue q = queues.get(m);
                if (q != null) {
                    synchronized (q) {
                        try {
                            q.put(e); //find the relevant queue and push the message
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        queuesByEvent.get(e.getClass()).add(m);
                    }
                }//push the micro service back to it's roundRobins queue
            }
        } else
            complete(e, null);
        return e.getFuture();
    }

    @Override
    public void register(MicroService m) {
        queues.put(m, new ArrayBlockingQueue<>(100));
    }

    @Override
    public void unregister(MicroService m) {
        System.out.println("Starting to unregister "+m.getName());
        Queue q = queues.get(m);
        for (int i = 0; i < q.size(); i++) {
            Message message = (Message) q.poll();
            if (message instanceof Event)
                complete((Event) message, null);
        }
        queues.remove(m);
        unregisterHelper(eventMapping, m);
        unregisterHelper(broadcastMapping, m);
        System.out.println(("Finished to unregister "+m.getName()));
    }


    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        if (queues.get(m) == null)
            throw new NullPointerException();
        return (Message) (queues.get(m).take()); //takes a message from the queue
    }

    private void unregisterHelper(ConcurrentHashMap Mapping, MicroService m) {
        synchronized (Mapping) {
            List list = (List) Mapping.get(m);
            if (list != null) {
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        Queue tmp = queuesByEvent.get(list.get(i));
                        for (int j = 0; j < tmp.size(); j++) {
                            MicroService service = (MicroService) tmp.poll();
                            if (!service.equals(m))
                                tmp.add(service);
                        }
                    }
                    Mapping.remove(m);
                } else Mapping.remove(m);
            }
        }
    }

}
