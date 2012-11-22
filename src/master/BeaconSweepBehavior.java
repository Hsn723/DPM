package master;

import lejos.robotics.subsumption.Behavior;
import lejos.util.TimerListener;
import lejos.util.Timer;

public class BeaconSweepBehavior implements Behavior, TimerListener {
	private static final int SCAN_PERIOD = 10 * 1000;
	
	private boolean timedOut = false;
	
	public BeaconSweepBehavior() {
		Timer timer = new Timer(SCAN_PERIOD, this);
		timer.start();
	}
	
	@Override
	public void action() {
		Role.beaconLocalizer.doSearchBehavior();
		timedOut = false;
		
	}

	@Override
	public void suppress() {
		
	}

	@Override
	public boolean takeControl() {
		return timedOut;
	}

	@Override
	public void timedOut() {
		timedOut = true;
		
	}
	
}