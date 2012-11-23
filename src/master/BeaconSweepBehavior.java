package master;

import lejos.robotics.subsumption.Behavior;
import lejos.util.TimerListener;
import lejos.util.Timer;

public class BeaconSweepBehavior implements Behavior, TimerListener {
	private static final int SCAN_PERIOD = 45 * 1000;
	private static  boolean SCANNING = false;
	
	private boolean timedOut = true;
	
	public BeaconSweepBehavior() {
		Timer timer = new Timer(SCAN_PERIOD, this);
		timer.start();
	}
	
	@Override
	public void action() {
		timedOut = false;
		SCANNING = true;
		
		Role.beaconLocalizer.doSearchBehavior();
		
		timedOut = false;
		SCANNING = false;
		
	}

	@Override
	public void suppress() {
		
	}

	@Override
	public boolean takeControl() {
		return timedOut && !SCANNING;
	}

	@Override
	public void timedOut() {
		timedOut = true;
		
	}
	
}