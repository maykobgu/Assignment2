package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
      private int speed;
      private int duration;
//      private static TimeService instance = null;

	public TimeService(int duration, int speed) {
		super("TimeService");
		this.speed = speed;
		this.duration = duration;
	}

	@Override
    protected void initialize() {
        Timer time = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i<duration; i++){
                    try {
                        sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TickBroadcast tickMessage = new TickBroadcast(i);
                    sendBroadcast(tickMessage);
                }

            }
        };
        time.schedule(task,  speed, duration);
    }

}
