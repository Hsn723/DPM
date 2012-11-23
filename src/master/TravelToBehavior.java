package master;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import lejos.nxt.Sound;
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
	private static final int FORWARD_SPEED = 10;
	private static final int ROTATE_SPEED = 50;
	private static final double ANGLE_BAND = 1;
	private static double[] headings = new double[3];
	private static final int DISTANCE_TOLERANCE = 5;	//CHANGEME
	private boolean suppressed = false;
	
	public TravelToBehavior(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void action() {
		suppressed = false;
		
		Role.odometer.getPosition(headings);
		double xHeading = x - headings[0];
		double yHeading = y - headings[1];
		
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

		while(!suppressed && !Role.destinationReached) {
			Role.odometer.getPosition(headings);
			xHeading = x - headings[0];
			yHeading = y - headings[1];
			
			double distance = sqrt(pow(xHeading, 2) + pow(yHeading,2));
			Role.destinationReached = distance < DISTANCE_TOLERANCE;
			
			Thread.yield();
		}
		Sound.beepSequence();
		Role.robot.stop();
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