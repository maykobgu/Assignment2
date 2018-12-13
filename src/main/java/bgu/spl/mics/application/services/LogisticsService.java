package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.ReleaseVehicleEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

    public LogisticsService() {
        super("LogisticsService");
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeliveryEvent.class, this::processEvent);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);

    }

    private synchronized void processEvent(DeliveryEvent e) throws InterruptedException {
        System.out.println("got a delivery");
        AcquireVehicleEvent acq = new AcquireVehicleEvent();
        Future result = sendEvent(acq);
        DeliveryVehicle vehicle = (DeliveryVehicle) result.get();
        vehicle.deliver(e.getAdress(), e.getDistance());
        ReleaseVehicleEvent rel = new ReleaseVehicleEvent(vehicle);
        sendEvent(rel);
    }

    private void finish(TerminateBroadcast e) {
        terminate();
    }

}
