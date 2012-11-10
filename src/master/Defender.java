package master;

import lejos.nxt.Motor;
import master.Forklift.LiftLevel;

/**
 * This class defines a Defender role for the robot.
 * The Defender contains two methods for reaching and
 * grabbing the beacon, and to hide the beacon.
 * 
 * @author Antoine Tu
 *
 */
public class Defender {
	private TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private Odometer odometer = new Odometer(robot, true);
	private Navigation navigation = new Navigation(odometer);
	//private UltrasonicSensor ultrasonicLocalizerSensor = new UltrasonicSensor(SensorPort.S1);
	private BTConnector btConnector;
	private BeaconLocalizer beaconLocalizer;
	private int xBeacon, yBeacon, zBeacon;
	private Clamp clamp;
	private Forklift forklift;
	
	/*
	 * TODO: check the BT class for info on beacon coordinates.
	 * We will need to update this to take in the coordinates of the beacon.
	 */
	/**
	 * Initializes a new Defender.
	 * @param x the x-coord of the beacon.
	 * @param y the y-coord of the beacon.
	 * @param z the z-coord of the beacon.
	 * @param remoteNXTName the name of the remote NXT.
	 */
	public Defender(int x, int y, int z, String remoteNXTName) {
		// Keep trying to establish connection to remote NXT if unsuccessful
		while ( !btConnector.doRemoteConnection(remoteNXTName) ) { }
		xBeacon = x;
		yBeacon = y;
		zBeacon = z;
		clamp = new Clamp(btConnector.getClampMotor());
		forklift = new Forklift(btConnector.getForkliftMotor());
	}
	
	/**
	 * Make the robot travel to the beacon's given location
	 * and then grab the beacon.
	 */
	public void getBeacon() {
		// First navigate to given coordinates for beacon
		navigation.travelTo(xBeacon, yBeacon);
		
		// Do a beacon localization to make sure we are well placed.
		beaconLocalizer = new BeaconLocalizer(odometer, btConnector.getRemoteLightSensor(), forklift);
		beaconLocalizer.doSearch();	//not complete yet
		
		//Now we should be placed correctly and only need to clamp.
		clamp.grip();
	}
	
	/**
	 * Make the robot decide upon a hiding spot,
	 * drop the beacon, and then move out of the
	 * field.
	 */
	public void hideBeacon() {
		// Determine and move to a hiding spot
		
		// Drop the beacon
		forklift.goToHeight(LiftLevel.LOW);
		clamp.release();
		
		// Move out of the field
		/*
		 * TODO: The origin would be the safest return spot.
		 * However it could be far from our current location.
		 * In the future we could search for the closest safe spot.
		 */
		navigation.travelTo(0,0);
	}
}