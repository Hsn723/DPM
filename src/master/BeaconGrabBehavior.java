package master;

import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import master.Forklift.LiftLevel;

/**
 * This behavior takes over control when beacon is reached. It simply does a 180 turn so the clamp
 * is facing the beacon, travels forward and then grabs it, and finally travels backward.
 * @author Lixuan
 *
 */
public class BeaconGrabBehavior implements Behavior {
	private static final int STAND_OFF_DISTANCE = 20;
	
	private static final int LIGHT_SENSOR_OFFSET_ANGLE = -5;
	private boolean suppressed = false;
	
	@Override
	public boolean takeControl() {
		return Role.beaconDetected 
				&& Role.beaconReached
				&& !Role.beaconGrabbed;
	}

	@Override
	public void action() {
		suppressed = false;
		Role.beaconGrabbed = false;
		Role.robot.rotate(180 + LIGHT_SENSOR_OFFSET_ANGLE);
		Sound.playTone(1000, 2, 100);
		
		
		Role.robot.setAcceleration(800);
		Role.robot.setMotorSpeed(800, 800);
		
		// Since we always forget to open the clamp, let's open it here
		Role.clamp.release();
		
		Role.robot.start();
		Sound.playTone(2000, 2, 100);
		long startTime = System.currentTimeMillis();
		
		while(!Role.beaconTouchSensor.isPressed() && System.currentTimeMillis() - startTime < 1000) {
			Thread.yield();
		}
	
		Role.clamp.grip();
		Role.robot.stop();
		Role.forklift.goToHeight(LiftLevel.HIGH);
		
		Role.beaconGrabbed = true;
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
}
