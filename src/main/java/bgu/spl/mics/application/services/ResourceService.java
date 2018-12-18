package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.LinkedList;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link } singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link }, {@link }.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService {
    private static ResourcesHolder instance;
    private LinkedList<Future> futures;

    public ResourceService(DeliveryVehicle[] vehicles, String name) {
        super(name);
        instance = ResourcesHolder.getInstance();
        instance.load(vehicles);
        futures = new LinkedList();
    }

    @Override
    protected void initialize() {
        subscribeEvent(AcquireVehicleEvent.class, this::processEvent);
        subscribeEvent(ReleaseVehicleEvent.class, this::releaseEvent);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);

    }

    private void processEvent(AcquireVehicleEvent e){
        Future result = instance.acquireVehicle();
        futures.add(result);
        complete((Event) e, result);
    }

    private void releaseEvent(ReleaseVehicleEvent e) {
        instance.releaseVehicle(e.getVehicle());
        complete(e, null);
    }

    private void finish(TerminateBroadcast e) {
        for (int i = 0; i < futures.size(); i++) {
            if (!futures.get(i).isDone())
                futures.get(i).resolve(null);
        }
        terminate();
    }
}
