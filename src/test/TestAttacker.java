package test;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import master.*;
import master.USLocalizer.LocalizationType;
import bluetooth.StartCorner;

/**
 * Test Class for Attacker Mode
 * @author Jeffrey Durocher
 *
 */
public class TestAttacker {
	private static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private static Odometer odometer = new Odometer(robot, true);
	private static StartCorner corner;
	private static final String remoteNXTName = "Scorpio";
	private static final double TILE_FACTOR = 30.48;
	
	private static UltrasonicSensor ultrasonicSensor;
	// Add one localizer
	private static USLocalizer ultrasonicLocalizer;
	
	public static void main(String[] args) {	
		ultrasonicSensor = robot.getFrontUltrasonicSensor();
		ultrasonicLocalizer = new USLocalizer(odometer, ultrasonicSensor, LocalizationType.RISING_EDGE);
		//bottom left starting
		corner = StartCorner.BOTTOM_LEFT;
		//Beacon starts at 5,5 (for the defender but we can use this in our algorithm) and must be dropped at 10,10
		double fx = 5;
		double fy = 5;
				
		double dx = 10;
		double dy = 10;
		
		// Flag position (defense) and destination (attack)
		double xFlag = fx * TILE_FACTOR, yFlag = fy * TILE_FACTOR;
		double xDest = dx * TILE_FACTOR, yDest = dy * TILE_FACTOR;
		
		// Once we have localized, update the position.
		odometer.setPosition(getStartingPose(), new boolean[] {true, true, true});
		
		// Start role
		
			Attacker attacker = new Attacker(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
			attacker.searchBeacon();
			attacker.depositBeacon();
		
		System.exit(0);
	}
	
	/**
	 * Determine the starting location and heading
	 * of the robot.
	 * @return an array containing the x, y and theta.
	 */
	private static double[] getStartingPose() {
		double xPos = corner.getX() * TILE_FACTOR;
		double yPos = corner.getY() * TILE_FACTOR;
		double heading = 0;
		if (corner == StartCorner.BOTTOM_LEFT) {
			heading = 0;
		} else if (corner == StartCorner.BOTTOM_RIGHT) {
			heading = -90;
		} else if (corner == StartCorner.TOP_LEFT) {
			heading = 90;
		} else if (corner == StartCorner.TOP_RIGHT) {
			heading = 180;
		}
		
		double[] position = { xPos, yPos, heading };
		return position;
	}
}