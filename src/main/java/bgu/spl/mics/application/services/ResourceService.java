package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.HashMap;

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
    private static ResourcesHolder instance = ResourcesHolder.getInstance();

    public ResourceService() {
        super("Change_This_Name");
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeEvent(AcquireVehicleEvent.class, this::processEvent);
        subscribeEvent(ReleaseVehicleEvent.class, this::releaseEvent);
    }

    private void processEvent(AcquireVehicleEvent e) throws InterruptedException {
        Future result = instance.acquireVehicle();
        complete((Event) e, result.get());
    }

    private void releaseEvent(ReleaseVehicleEvent e) {
        instance.releaseVehicle(e.getVehicle());
    }
}
