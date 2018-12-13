package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService {
    private static ResourcesHolder instance;

    public ResourceService(DeliveryVehicle[] vehicles) {
        super("ResourceService");
        instance = ResourcesHolder.getInstance();
        instance.load(vehicles);
    }

    @Override
    protected void initialize() {
        subscribeEvent(AcquireVehicleEvent.class, this::processEvent);
        subscribeEvent(ReleaseVehicleEvent.class, this::releaseEvent);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);

    }

    private void processEvent(AcquireVehicleEvent e) throws InterruptedException {
        Future result = instance.acquireVehicle();
        complete((Event) e, result.get());
    }

    private void releaseEvent(ReleaseVehicleEvent e) {
        instance.releaseVehicle(e.getVehicle());
    }

    private void finish(TerminateBroadcast e) {
        terminate();
    }
}
