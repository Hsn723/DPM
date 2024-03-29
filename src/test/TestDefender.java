package test;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import master.*;
import master.USLocalizer.LocalizationType;
import bluetooth.StartCorner;

/**
 * Test Class for Defender Mode
 * @author Jeffrey Durocher
 * 
 */
public class TestDefender {
	private static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private static Odometer odometer = new Odometer(robot, true);
	private static StartCorner corner;
	private static final String remoteNXTName = "Scorpio";
	private static final double TILE_FACTOR = 30.48;
	
	private static UltrasonicSensor ultrasonicSensor;
	// Add one localizer
	private static USLocalizer ultrasonicLocalizer;
	
	public static void main(String[] args) {	
		//ultrasonicSensor = robot.getFrontUltrasonicSensor();
		//ultrasonicLocalizer = new USLocalizer(odometer, ultrasonicSensor, LocalizationType.FALLING_EDGE);
		//bottom left starting
		corner = StartCorner.BOTTOM_LEFT;
		//Beacon starts at 5,5
		double fx = 4;
		double fy = 4;
		
		//These are the end coordinates so we might want to put the beacon 
		//as far away from end point as possible to make it hard on the attacker
		double dx = 5;
		double dy = 2;
		
		// Flag position (defense) and destination (attack)
		double xFlag = fx * TILE_FACTOR, yFlag = fy * TILE_FACTOR;
		double xDest = dx * TILE_FACTOR, yDest = dy * TILE_FACTOR;
		// Localize
		//ultrasonicLocalizer.doLocalization();
		
		// Once we have localized, update the position.
		
		
		// Start role
		
		Defender defender = new Defender(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
		odometer.setPosition(getStartingPose(), new boolean[] {true, true, true});
		defender.getBeacon();
		defender.hideBeacon();
		
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