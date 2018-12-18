package bgu.spl.mics;

import bgu.spl.mics.application.services.ResourceService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<Class<? extends Message>, Queue<MicroService>> queuesByEvent;
    private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> queues;
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
        synchronized (queuesByEvent) {
            if (queuesByEvent.get(type) == null) {
                LinkedBlockingQueue arr = new LinkedBlockingQueue();
                queuesByEvent.put(type, arr);
                queuesByEvent.get(type).add(m);
            } else
                queuesByEvent.get(type).add(m);
        }
        synchronized (eventMapping) {
            if (eventMapping.get(type) == null) {
                LinkedList<Class<? extends Message>> list = new LinkedList<>();

                eventMapping.put(m, list);
                eventMapping.get(m).add(type);
            } else
                eventMapping.get(m).add(type);
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        synchronized (queuesByEvent) {

            if (queuesByEvent.get(type) == null) {
                queuesByEvent.put(type, new LinkedBlockingQueue());
                queuesByEvent.get(type).add(m);
            } else {
                queuesByEvent.get(type).add(m);
            }
        }
        synchronized (broadcastMapping) {
            if (broadcastMapping.get(type) == null) {
                LinkedList<Class<? extends Message>> list = new LinkedList<>();
                broadcastMapping.put(m, list);
                broadcastMapping.get(m).add(type);
            } else
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
            synchronized (queuesByEvent) {
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
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (queuesByEvent) {
            if (queuesByEvent.get(e.getClass()) != null && !queuesByEvent.get(e.getClass()).isEmpty()) {
                MicroService m = queuesByEvent.get(e.getClass()).poll(); //get the first micro service
                LinkedBlockingQueue q = queues.get(m);
                if(q!=null){
                synchronized (q) {
                    if (q != null) {
                        try {
                            q.put(e); //find the relevant queue and push the message
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        queuesByEvent.get(e.getClass()).add(m);
                    }
                    //push the micro service back to it's roundRobins queue
                    else
                        complete(e, null);
                }
                }
                else complete(e,null);
            } else
                complete(e, null);
        }
        return e.getFuture();
    }

    @Override
    public void register(MicroService m) {
        queues.put(m, new LinkedBlockingQueue());
    }

    @Override
    public void unregister(MicroService m) {
        unregisterHelper(eventMapping, m);
        unregisterHelper(broadcastMapping, m);
        synchronized(queues.get(m)) {
            Queue q = queues.get(m);
            for (int i = 0; i < q.size(); i++) {
                Message message = (Message) q.poll();
                if (message instanceof Event)
                    complete((Event) message, null);
            }
            queues.remove(m);
        }
    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        if (queues.get(m) == null) {
            throw new NullPointerException();
        }
        return queues.get(m).take(); //takes a message from the queue
    }

    private void unregisterHelper(ConcurrentHashMap Mapping, MicroService m) {
        synchronized (queuesByEvent) {
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

}
