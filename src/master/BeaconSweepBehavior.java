package master;

import lejos.robotics.subsumption.Behavior;
import lejos.util.TimerListener;
import lejos.util.Timer;
/**
 * This class implements Behavior and defines a timer
 * that sets the interval at which the robot will scan
 * for a beacon.
 * 
 * @author Lixuan Tang
 *
 */
public class BeaconSweepBehavior implements Behavior, TimerListener {
	//private static final int SCAN_PERIOD = 45 * 1000;
	private static  boolean SCANNING = false;
	protected static boolean scanOnce = false;
	
	private boolean timedOut = true;
	
	public BeaconSweepBehavior(int period) {
		Timer timer = new Timer(period * 1000, this);
		timer.start();
	}
	
	public BeaconSweepBehavior(int period, boolean scanOnce) {
		Timer timer = new Timer(period * 1000, this);
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
		return timedOut && !SCANNING && !Role.beaconGrabbed;
	}

	@Override
	public void timedOut() {
		timedOut = true;
		
	}
	
}