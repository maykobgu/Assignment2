package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;

import java.util.Timer;

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

	public TimeService(int duration, int speed) {
		super("TimeService");
		this.speed = speed;
		this.duration = duration;
	}
//use Timer class in java
	@Override
	protected void initialize() {
		Timer time = new Timer();
		// TODO Implement this
	}

}
