package master;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import master.USLocalizer.LocalizationType;
import bluetooth.BluetoothConnection;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import bluetooth.Transmission;

/**
 * Main class 
 * 
 * Listens to the server and waits for instructions
 * on attacker, defender etc...
 * 
 * Calls attacker or defender class once all data is received.
 * @author 
 *
 */

public class BTListen {
	private static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private static Odometer odometer = new Odometer(robot, true);
	private static StartCorner corner;
	private static final String remoteNXTName = "Scorpio";
	private static final double TILE_FACTOR = 30.48;
	
	private static UltrasonicSensor ultrasonicSensor;
	// Add one localizer
	private static USLocalizer ultrasonicLocalizer;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		BluetoothConnection bluetoothConnection = new BluetoothConnection();
		// as of this point the bluetooth connection is closed again, and you can pair to another NXT (or PC) if you wish
		
		// Get the transmission
		Transmission transmission = bluetoothConnection.getTransmission();
		
		// Initialize objects
		ultrasonicSensor = robot.getFrontUltrasonicSensor();
		ultrasonicLocalizer = new USLocalizer(odometer, ultrasonicSensor, LocalizationType.RISING_EDGE);
		
		// Get the information from transmission
		corner = transmission.startingCorner;	//do something with the corner
		PlayerRole role = transmission.role;
		
		// Flag position (defense) and destination (attack)
		double xFlag = transmission.fx * TILE_FACTOR, yFlag = transmission.fy * TILE_FACTOR;
		double xDest = transmission.dx * TILE_FACTOR, yDest = transmission.dy * TILE_FACTOR;
		// Localize
		ultrasonicLocalizer.doLocalization();
		
		// Once we have localized, update the position.
		odometer.setPosition(getStartingPose(), new boolean[] {true, true, true});
		
		// Start role
		if (role == PlayerRole.DEFENDER) {
			Defender defender = new Defender(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
			defender.getBeacon();
			defender.hideBeacon();
		} else if (role == PlayerRole.ATTACKER) {
			//wait 5 minutes
			Attacker attacker = new Attacker(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
			attacker.searchBeacon();
			attacker.depositBeacon();
		}
		
		System.exit(0);
	}
	
	/**
	 * Determine the starting location and heading
	 * of the robot.
	 * @return an array containing the x, y and theta.
	 */
	@SuppressWarnings("static-access")
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