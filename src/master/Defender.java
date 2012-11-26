package master;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * This class defines a Defender role for the robot.
 * The Defender contains two methods for reaching and
 * grabbing the beacon, and to hide the beacon.
 * 
 * @author Antoine Tu
 *
 */
public class Defender extends Role {
	/**
	 * Initializes a new Defender.
	 * @param robot the robot.
	 * @param the odometer of the robot.
	 * @param xFlag the x-coord of the beacon.
	 * @param yFlag the y-coord of the beacon.
	 * @param xDest the x-coord the attacker has to drop the beacon at.
	 * @param yDest the y-coord the attacker has to drop the beacon at.
	 * @param remoteNXTName the name of the remote NXT.
	 */
	public Defender(TwoWheeledRobot robot, Odometer odometer, double xFlag, double yFlag, double xDest, double yDest, String remoteNXTName) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
	}
	
	/**
	 * Make the robot travel to the beacon's given location
	 * and then grab the beacon.
	 */
	public void getBeacon() {
		// Instantiate behaviors.
		Behavior b0 = new GoForwardBehavior();
		Behavior b1 = new BeaconSweepBehavior(45);
		Behavior b2 = new TravelToBehavior(xBeacon, yBeacon);
		Behavior b3 = new ObstacleAvoidanceBehavior();
		Behavior b4 = new BeaconGrabBehavior();
		Behavior[] behaviors = {b0, b1, b2, b3, b4}; 

		// Instantiate and start arbitrator.
		Arbitrator arbitrator = new Arbitrator(behaviors, true);
		arbitrator.start();
	}
	
	/**
	 * Make the robot decide upon a hiding spot,
	 * drop the beacon, and then move out of the
	 * field.
	 */
	public void hideBeacon() {
		// Reset our booleans
		destinationReached = false;
		
		// Initialize our behaviors.
		Behavior b0 = new ExitFieldBehavior();
		Behavior b1 = new TravelToBehavior(yDest, xDest);
		Behavior b2 = new ObstacleAvoidanceBehavior();
		Behavior b3 = new BeaconDropBehavior();
		Behavior[] behaviors = {b0, b1, b2, b3};
		
		Arbitrator arbitrator = new Arbitrator(behaviors, true);
		arbitrator.start();		
	}
}