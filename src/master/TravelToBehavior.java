package master;
import lejos.robotics.subsumption.Behavior;

/**
 * TravelToBehavior defines a behavior that
 * makes the robot travel to a specified
 * point.
 * @author Antoine Tu
 *
 */
public class TravelToBehavior implements Behavior {
	private double x, y;
	private boolean suppressed = false;
	
	public TravelToBehavior(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void action() {
		suppressed = false;
		Role.navigation.travelTo(x, y);	//FIXME: should make sure if travelto returns
		Role.destinationReached = true;	//TODO: make sure this isn't called prematurely.
		//FIXME: not clear if we should add a suppressed check here.
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return !Role.destinationReached;
	}
}