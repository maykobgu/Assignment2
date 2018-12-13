package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
    private int speed; //how much time one tick takes
    private int duration; //how many ticks
    private int currentTick; //how many ticks


    public TimeService(int duration, int speed) {
        super("TimeService");
        this.speed = speed;
        this.duration = duration;
        currentTick = 0;
    }

    @Override
    protected void initialize() {
        Timer time = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (currentTick < duration) {
                    currentTick++;
                    System.out.println("Tick number: " + currentTick);
                    TickBroadcast tickMessage = new TickBroadcast(currentTick);
                    sendBroadcast(tickMessage);
                } else {
                    sendBroadcast(new TerminateBroadcast());
                    time.cancel();
                    terminate();
                }
            }
        };
        time.schedule(task, 0, speed);
    }
}
