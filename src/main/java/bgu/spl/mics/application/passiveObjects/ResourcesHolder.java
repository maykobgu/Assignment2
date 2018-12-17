package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
    private static DeliveryVehicle[] vehicles; //Holds a collection of DeliveryVehicle
    private static Queue<Future> vehicleRequests;
    private static Queue<DeliveryVehicle> freeVehicles;
    private Object lock;
    private Semaphore lockerofVehicles;

    private static class SingletonHolder {
        private static ResourcesHolder instance = new ResourcesHolder();
    }

    private ResourcesHolder() {
        vehicleRequests = new ConcurrentLinkedQueue<>();
        freeVehicles = new ConcurrentLinkedQueue<>();
        lock = new Object();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static ResourcesHolder getInstance() {
        return ResourcesHolder.SingletonHolder.instance;
    }


    /**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     *
     * @return {@link Future<DeliveryVehicle>} object which will resolve to a
     * {@link DeliveryVehicle} when completed.
     */
    public Future<DeliveryVehicle> acquireVehicle() {
        synchronized (lock) {
            Future<DeliveryVehicle> f = new Future<>();
            vehicleRequests.add(f);
            System.out.println("acquiring vehicle by resHolder");
            if (!freeVehicles.isEmpty()) {
                Future firstFuture = vehicleRequests.poll();
                DeliveryVehicle firstFreeVehicle = freeVehicles.poll();
                firstFuture.resolve(firstFreeVehicle);
                System.out.println("firstFuture.get  " + firstFuture.get());
            }
            return f;
        }
    }

    /**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     *
     * @param vehicle {@link DeliveryVehicle} to be released.
     */
    public void releaseVehicle(DeliveryVehicle vehicle) {
        synchronized (lock) {
            freeVehicles.add(vehicle);
            if (!vehicleRequests.isEmpty()) {
                Future firstFuture = vehicleRequests.poll();
                DeliveryVehicle firstFreeVehicle = freeVehicles.poll();
                firstFuture.resolve(firstFreeVehicle);
                System.out.println("vehicle released");
            }
        }
    }

    /**
     * Receives a collection of vehicles and stores them.
     * <p>
     *
     * @param vehicles Array of {@link DeliveryVehicle} instances to store.
     */
    public void load(DeliveryVehicle[] vehicles) {
        this.vehicles = vehicles;
        for (int i = 0; i < vehicles.length; i++) {
            freeVehicles.add(vehicles[i]);
        }
        lockerofVehicles=new Semaphore(vehicles.length);
    }
}
