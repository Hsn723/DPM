package master;

import lejos.robotics.subsumption.Behavior;

/**
 * This behavior takes over control when beacon is reached. It simply does a 180 turn so the clamp
 * is facing the beacon, travels forward and then grabs it, and finally travels backward.
 * @author Lixuan
 *
 */
public class BeaconGrabBehavior implements Behavior {
	private static final int STAND_OFF_DISTANCE = 20;
	private boolean suppressed = false;
	
	@Override
	public boolean takeControl() {
		return Role.beaconDetected && Role.robot.getFrontUltrasonicSensor().getDistance() < 25 && !Role.beaconGrabbed;
	}

	@Override
	public void action() {
		suppressed = false;
		Role.robot.rotate(180);
		Role.robot.goForward(STAND_OFF_DISTANCE);
		Role.clamp.grip();
		Role.robot.goForward(-STAND_OFF_DISTANCE);
		while(!suppressed) {
			Thread.yield();	//make sure we don't prematurely set beaconGrabbed to true
		}
		Role.beaconGrabbed = true;
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
}
