package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

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
    private static Semaphore s= new Semaphore(vehicles.length, true);

    private static class SingletonHolder {
        private static ResourcesHolder instance = new ResourcesHolder();
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
    public Future<DeliveryVehicle> acquireVehicle() throws InterruptedException {
        while (!s.tryAcquire()) ;
        s.acquire();
        Future result = null;
        boolean found = false;
        for (int i = 0; i < vehicles.length & !found; i++) {
            if (vehicles[i].isAvailable()) {
                result.resolve(vehicles[i]);
                found = true;
                vehicles[i].acquire();
            }
        }
        return result;
    }

    /**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     *
     * @param vehicle {@link DeliveryVehicle} to be released.
     */
    public void releaseVehicle(DeliveryVehicle vehicle) {
        s.release();
    }

    /**
     * Receives a collection of vehicles and stores them.
     * <p>
     *
     * @param vehicles Array of {@link DeliveryVehicle} instances to store.
     */
    public void load(DeliveryVehicle[] vehicles) {
        this.vehicles = vehicles;
    }
}
