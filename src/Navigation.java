import static java.lang.Math.*;
import lejos.nxt.Sound;


public class Navigation {
	// put your navigation code here
	
	// Define constants
	private static final int FORWARD_SPEED = 5;
	private static final int ROTATE_SPEED = 20;
	private static final double ANGLE_BAND = 1;
	private static double[] headings = new double[3];
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
	}
	/*
	 * TODO: sweep the ultrasonic sensor for obstacles,
	 * start WallFollower to go around them.
	 */
	public void travelTo(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		// Poll the odometer
		this.odo.getPosition(headings);
		double xHeading = x - headings[0];
		double yHeading = y - headings[1];
		
		double deltaT = atan2(yHeading, xHeading);
		if (deltaT < 0) deltaT += 2*PI;
		// Decide if we need to turn
		deltaT = toDegrees(deltaT);
		if (abs(deltaT - headings[2]) > ANGLE_BAND)
			turnTo(deltaT);
		// Advance
		double distance = sqrt(pow(xHeading, 2) + pow(yHeading,2));
		this.robot.setSpeeds(FORWARD_SPEED, 0);
		this.robot.goForward(distance);
		 
		 // Stop the NXT
		 Sound.beep();
		 this.robot.setSpeeds(0,0);
	}
	
	public void turnTo(double angle) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		// Get and display the current and target angle
		this.odo.getPosition(headings);
		double actualTheta = headings[2]; 
		double rotationAngle = angle - actualTheta;
		 
		// Check if we need to turn
		if (abs(rotationAngle) > ANGLE_BAND)
		{
			if (rotationAngle < 0) rotationAngle += 360;
			this.robot.setSpeeds(0, ROTATE_SPEED);
			if (rotationAngle <= 180) this.robot.rotate((int) rotationAngle);
			else
			{
				rotationAngle = 360 - rotationAngle;
				this.robot.rotate((int) -rotationAngle);
			}
		}
		Sound.twoBeeps();
		this.robot.setSpeeds(0,0);
	}
}
