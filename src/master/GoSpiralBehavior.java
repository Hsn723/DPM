package master;

import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public class GoSpiralBehavior implements Behavior {
	private boolean suppressed = false;
	private static final int FORWARD_SPEED = 10;
	
	private boolean speedSwitch = false;
	@Override
	public void action() {
		Role.robot.storeSpeed();
		suppressed = false;
		Sound.beepSequenceUp();	//FIXME: debug only, remove
		
		if (speedSwitch){
			Role.robot.setMotorSpeed(250, 400);
		}
		else{
			Role.robot.setMotorSpeed(200, 200);
		}
		
		Role.robot.goForward();
		while(!suppressed) {
			Thread.yield();	//keep going forward until suppressed
		}
		Role.robot.recoverSpeed();
		Role.robot.stop();
		toggle();
	}

	@Override
	public void suppress() {
		Role.robot.recoverSpeed();
		suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return !Role.beaconGrabbed;	//we can stop once the beacon has been grabbed.
	}
	
	
	private void toggle(){
		speedSwitch = !speedSwitch;
	}
}
