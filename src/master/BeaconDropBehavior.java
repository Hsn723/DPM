package master;
import lejos.robotics.subsumption.Behavior;

/**
 * This class defines a behavior that makes the
 * robot drop the beacon.
 * 
 * @author Antoine Tu
 *
 */
public class BeaconDropBehavior implements Behavior {
	private boolean suppressed = false; //might not even be necessary, top priority
	
	@Override
	public void action() {
		suppressed = false;
		Role.robot.goForward(20);
		Role.clamp.release();
		Role.robot.goForward(20);
		Role.beaconDropped = true;
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return Role.destinationReached && !Role.beaconDropped;
	}
	
}