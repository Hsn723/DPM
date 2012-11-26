package master;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import lejos.robotics.subsumption.Behavior;
/**
 * This class defines the behavior that makes the robot
 * exit the field by going back to the original coordinates
 * it has started with.
 * 
 * @author Antoine Tu
 *
 */
public class ExitFieldBehavior implements Behavior {
	private static final int FORWARD_SPEED = 10;
	private static final int ROTATE_SPEED = 50;
	private static final double ANGLE_BAND = 1;
	private static double[] headings = new double[3];
	private static final int DISTANCE_TOLERANCE = 5;	//CHANGEME
	private boolean suppressed = false;
	@Override
	public void action() {
		suppressed = false;
		//Role.navigation.travelTo(Role.startingPosition[0], Role.startingPosition[1]);
		Role.odometer.getPosition(headings);
		double xHeading = Role.startingPosition[0] - headings[0];
		double yHeading = Role.startingPosition[1] - headings[1];
		
		//double deltaT = atan2(yHeading, xHeading);
		double deltaT = atan(xHeading/yHeading);
		deltaT = toDegrees(deltaT);
		if ( yHeading <=0 ) {
			if (xHeading <= 0) {
				deltaT -= 180;
			}
			else deltaT += 180;
		}
		if (abs(deltaT - headings[2]) > ANGLE_BAND)
			Role.navigation.turnTo(deltaT);
		// Advance
		Role.robot.goForward();
		
		while(!suppressed && !Role.originReached) {
			Role.odometer.getPosition(headings);
			xHeading = Role.startingPosition[0] - headings[0];
			yHeading = Role.startingPosition[1] - headings[1];
			
			double distance = sqrt(pow(xHeading, 2) + pow(yHeading,2));
			Role.originReached = distance < DISTANCE_TOLERANCE;
			
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