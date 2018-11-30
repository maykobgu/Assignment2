package bgu.spl.mics.application.passiveObjects;

import static java.lang.Thread.sleep;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class DeliveryVehicle {
    private boolean available;
    private int license;
    private int speed;

    /**
     * Constructor.
     */
    public DeliveryVehicle(int license, int speed) {
        this.license = license;
        this.speed = speed;
        available = true;
    }

    /**
     * Retrieves the license of this delivery vehicle.
     */
    public int getLicense() {
        return license;
    }

    /**
     * Retrieves the speed of this vehicle person.
     * <p>
     *
     * @return Number of ticks needed for 1 Km.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Simulates a delivery by sleeping for the amount of time that
     * it takes this vehicle to cover {@code distance} KMs.
     * <p>
     *
     * @param address  The address of the customer.
     * @param distance The distance from the store to the customer.
     */
    public void deliver(String address, int distance) throws InterruptedException {
        long deliveryDuration = distance * getSpeed();
        available = false;
        sleep(deliveryDuration);
        available = true;
        // TODO check this
    }

    public boolean isAvailable() {
        return available;
    }
}
