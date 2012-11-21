package master;

import lejos.nxt.Sound;
import master.Forklift.LiftLevel;

/**
 * This abstract class defines a Role which only contains
 * constructors common to both Attacker and Defender,
 * as well as global variables that can be statically
 * accessed by behaviors
 * 
 * @author Antoine Tu
 *
 */
public abstract class Role {
	protected static TwoWheeledRobot robot; //changed to static
	protected Odometer odometer;
	protected static Navigation navigation; // changed to static
	//private UltrasonicSensor ultrasonicLocalizerSensor = new UltrasonicSensor(SensorPort.S1);
	protected BTConnector btConnector;
	protected BeaconLocalizer beaconLocalizer;
	protected double xBeacon, yBeacon, zBeacon;
	protected double xDest, yDest;
	protected static double[] startingPosition = new double[3];
	protected static Clamp clamp;
	protected static Forklift forklift;
	
	// Set state booleans for our behaviors
	public static boolean originReached = false;
	public static boolean destinationReached = false;
	public static boolean beaconDetected = false;
	public static boolean beaconReached = false;
	public static boolean beaconGrabbed = false;
	public static boolean beaconDropped = false;
	
	/**
	 * Initializes a new Role.
	 * @param robot the robot.
	 * @param the odometer of the robot.
	 * @param xFlag the x-coord of the beacon.
	 * @param yFlag the y-coord of the beacon.
	 * @param xDest the x-coord the attacker has to drop the beacon at.
	 * @param yDest the y-coord the attacker has to drop the beacon at.
	 * @param remoteNXTName the name of the remote NXT.
	 */
	public Role(TwoWheeledRobot robot, Odometer odometer, double xFlag, double yFlag, double xDest, double yDest, String remoteNXTName) {
		// Keep trying to establish connection to remote NXT if unsuccessful
		btConnector = new BTConnector();
		while ( !btConnector.doRemoteConnection(remoteNXTName) ) {
			Sound.twoBeeps();	// FIXME: for debugging purposes only
		}
		this.robot = robot;
		this.odometer = odometer;
		this.navigation = new Navigation(odometer);
		xBeacon = xFlag;
		yBeacon = yFlag;
		this.xDest = xDest;
		this.yDest = yDest;
		clamp = new Clamp(btConnector.getClampMotor());
		forklift = new Forklift(btConnector.getForkliftMotor());
		
		// Start by setting the lift high so that it doesn't collide with walls.
		//forklift.goToHeight(LiftLevel.HIGH);
		
		// Get the starting position
		odometer.getPosition(startingPosition);
	}
}