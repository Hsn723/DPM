package master;
import static java.lang.Math.*;
import sun.management.LazyCompositeData;
import lejos.nxt.Sound;

/**
 * Handles the navigation of the robot
 * turning to a given degree or to travel to a given point
 * in the field
 * @author
 *
 */

public class Navigation {
	
	// Define constants
	private static final int FORWARD_SPEED = 20;	//10
	private static final int ROTATE_SPEED = 50;
	private static final double ANGLE_BAND = 1;
	private static double[] headings = new double[3];
	private static final int DISTANCE_TOLERANCE = 5;	//CHANGEME
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	
	public Navigation(Odometer odo) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
	
	}
	
	/**
	 * Makes the robot travel to a given x,y coordinate
	 * x and y are given in cm
	 * absolute positioning (not relative to the current position of the robot)
	 * @param x x coordinate in cm
	 * @param y y coordinate in cm
	 * 
	 * @deprecated The TravelToBehavior and ExitFieldBehavior now implement this locally.
	 * 
	 */
	public void travelToIndependently(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		// Poll the odometer
		this.odo.getPosition(headings);
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
		//if (deltaT < 0) deltaT += 2*PI;
		// Decide if we need to turn
		
		if (abs(deltaT - headings[2]) > ANGLE_BAND)
			turnTo(deltaT);
		// Advance
		double distance = sqrt(pow(xHeading, 2) + pow(yHeading,2));
		this.robot.setSpeeds(FORWARD_SPEED, 0);
		//changed from forward to backward for light localization
		//this.robot.goForward(distance);
		this.robot.goForwardIndependently(distance);
		do {
			this.odo.getPosition(headings);
			Role.destinationReached = Math.abs(x - headings[0]) > DISTANCE_TOLERANCE &&
					Math.abs(y - headings[1]) > DISTANCE_TOLERANCE;
		}
		while (!Role.destinationReached);
		Sound.buzz();
		// Stop the NXT
		//this.robot.setSpeeds(0,0);
		//Role.destinationReached = true;	//set the destination as reached for the behavior
	}
	
	/**
	 * Makes the robot travel to a given x,y coordinate
	 * x and y are given in cm
	 * absolute positioning (not relative to the current position of the robot)
	 * @param x x coordinate in cm
	 * @param y y coordinate in cm
	 * 
	 */
	public void travelTo(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		// Poll the odometer
		this.odo.getPosition(headings);
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
		//if (deltaT < 0) deltaT += 2*PI;
		// Decide if we need to turn
		
		if (abs(deltaT - headings[2]) > ANGLE_BAND)
			turnTo(deltaT);
		// Advance
		double distance = sqrt(pow(xHeading, 2) + pow(yHeading,2));
		this.robot.setSpeeds(FORWARD_SPEED, 0);
		//changed from forward to backward for light localization
		this.robot.goForward(distance);
		//this.robot.goForwardIndependently(distance);
		 
		// Stop the NXT
		//this.robot.setSpeeds(0,0);
	}
	
	/**
	 * Turns the NXT to the specified angle without blocking
	 * @param angle	the angle to which we want to turn
	 */
	public void turnToIndependently(double angle) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		// Get the current and target angle
		this.odo.getPosition(headings);
		double actualTheta = headings[2]; 
		double rotationAngle = angle - actualTheta;
		 
		// Check if we need to turn
		if (abs(rotationAngle) > ANGLE_BAND)
		{
			if (rotationAngle < 0) rotationAngle += 360;
			this.robot.setSpeeds(0, ROTATE_SPEED);
			if (rotationAngle <= 180) this.robot.rotateIndependently((int) rotationAngle);
			else
			{
				rotationAngle = 360 - rotationAngle;
				this.robot.rotateIndependently((int) -rotationAngle);
			}
		}
		//this.robot.setSpeeds(0,0);
	}
	
	/**
	 * Turns the NXT to the specified angle.
	 * @param angle	the angle to which we want to turn
	 * 
	 * 
	 */
	public void turnTo(double angle) {
	
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		// Get the current and target angle
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
		//this.robot.setSpeeds(0,0);
	}
}
