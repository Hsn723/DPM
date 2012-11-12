package master;

/**
 * This abstract class defines a Role which only contains
 * constructors common to both Attacker and Defender.
 * @author Antoine Tu
 *
 */
public abstract class Role {
	protected TwoWheeledRobot robot;
	protected Odometer odometer;
	protected Navigation navigation = new Navigation(odometer);
	//private UltrasonicSensor ultrasonicLocalizerSensor = new UltrasonicSensor(SensorPort.S1);
	protected BTConnector btConnector;
	protected BeaconLocalizer beaconLocalizer;
	protected int xBeacon, yBeacon, zBeacon;
	protected int xDest, yDest;
	protected double[] startingPosition = new double[3];
	protected Clamp clamp;
	protected Forklift forklift;
	
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
	public Role(TwoWheeledRobot robot, Odometer odometer, int xFlag, int yFlag, int xDest, int yDest, String remoteNXTName) {
		// Keep trying to establish connection to remote NXT if unsuccessful
		while ( !btConnector.doRemoteConnection(remoteNXTName) ) { }
		this.robot = robot;
		this.odometer = odometer;
		xBeacon = xFlag;
		yBeacon = yFlag;
		this.xDest = xDest;
		this.yDest = yDest;
		clamp = new Clamp(btConnector.getClampMotor());
		forklift = new Forklift(btConnector.getForkliftMotor());
		
		// Get the starting position
		odometer.getPosition(startingPosition);
	}
}