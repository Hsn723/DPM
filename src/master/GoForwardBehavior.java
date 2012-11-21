package master;
import lejos.robotics.subsumption.Behavior;

/**
 * This class defines a default behavior that makes the
 * robot keep going straight.
 * @author Antoine Tu
 *
 */
public class GoForwardBehavior implements Behavior {
	private boolean suppressed = false;
	@Override
	public void action() {
		suppressed = false;
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