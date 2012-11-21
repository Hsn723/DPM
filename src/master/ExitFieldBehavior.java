package master;
import lejos.robotics.subsumption.Behavior;

public class ExitFieldBehavior implements Behavior {
	private boolean suppressed = false;
	@Override
	public void action() {
		suppressed = false;
		Role.navigation.travelTo(Role.startingPosition[0], Role.startingPosition[1]);
		while(!suppressed) {
			Thread.yield();
		}
		Role.robot.stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	@Override
	public boolean takeControl() {
		return !Role.originReached;
	}
	
}