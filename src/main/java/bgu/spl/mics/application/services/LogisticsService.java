package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
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
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link }.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

    public LogisticsService(String name) {
        super(name);
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeliveryEvent.class, this::processEvent);
        subscribeBroadcast(TerminateBroadcast.class, this::finish);
    }

    private void processEvent(DeliveryEvent e) throws InterruptedException {
        System.out.println(this.getName()+" I got the event "+ e);
//        System.out.println(this.toString());
        AcquireVehicleEvent acq = new AcquireVehicleEvent();
        System.out.println(this.getName()+ "sending the event"+ acq);
        Future result = sendEvent(acq);
//        Future futureResult = (Future) result.get();
//        System.out.println("the futureResult i got is "+futureResult);
        System.out.println(this.getName()+ "Hi I am trying to get the result");
        System.out.println(result.get());
        DeliveryVehicle vehicle = (DeliveryVehicle) ((Future)result.get()).get();
        vehicle.deliver(e.getAdress(), e.getDistance());
        ReleaseVehicleEvent rel = new ReleaseVehicleEvent(vehicle);
        sendEvent(rel);
        System.out.println(this.getName()+ "sending the event"+ rel);
        complete(e, null);
    }

    private void finish(TerminateBroadcast e) {
        terminate();
    }

}
