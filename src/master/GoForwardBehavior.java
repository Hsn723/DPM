package master;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

/**
 * This class defines a default behavior that makes the
 * robot keep going straight.
 * @author Antoine Tu
 *
 */
public class GoForwardBehavior implements Behavior {
	private boolean suppressed = false;
	private static final int FORWARD_SPEED = 10;
	@Override
	public void action() {
		suppressed = false;
		Sound.beepSequenceUp();
		Role.robot.setSpeeds(FORWARD_SPEED, 0);
		Role.robot.goForward();
		while(!suppressed) {
			Thread.yield();	//keep going forward until suppressed
		}
		Role.robot.stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return true;
	}
	
}