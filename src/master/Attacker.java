package master;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * This class defines a Attacker role for the robot.
 * The Attacker contains two methods for searching and
 * grabbing the beacon, and to put the beacon at a location.
 * 
 * @author Antoine Tu
 *
 */
public class Attacker extends Role {
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
	public Attacker(TwoWheeledRobot robot, Odometer odometer, double xFlag, double yFlag, double xDest, double yDest, String remoteNXTName) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
	}
	
	/**
	 * Search for the beacon using our search algorithm (to be determined)
	 * Once found, use the clamp to grab the beacon
	 */
	public void searchBeacon() {
		Behavior b0 = new GoForwardBehavior();
		Behavior b1 = new TravelToBehavior(xBeacon, yBeacon);
		Behavior b2 = new ObstacleAvoidanceBehavior();
		Behavior b3 = new BeaconSweepBehavior(30);
		Behavior b4 = new BeaconGrabBehavior();
		Behavior[] behaviors = {b0, b1, b2, b3, b4};
		
		Arbitrator arbitrator = new Arbitrator(behaviors, true);
		arbitrator.start();
	}
	
	/**
	 * Make the robot move to the designated
	 * dropoff point, then release the beacon
	 * and move out of the field.
	 */
	public void depositBeacon() {
		// Reset our booleans
		destinationReached = false;
		Behavior b0 = new ExitFieldBehavior();
		Behavior b1 = new TravelToBehavior(xDest, yDest);
		Behavior b2 = new ObstacleAvoidanceBehavior();
		Behavior b3 = new BeaconDropBehavior();
		Behavior[] behaviors = {b0, b1, b2, b3};
		
		Arbitrator arbitrator = new Arbitrator(behaviors, true);
		arbitrator.start();
	}
	
	
	/**
	 * Converts starting position into the number of the corner we start
	 * 1 = bottom left
	 * 2 = top left
	 * 3 = top right
	 * 4 = bottom right
	 * @return
	 * @deprecated
	 */
	private int getCorner() {
		double tolerance = 5;	//give 5cm of error margin, it doesn't really matter
		if (startingPosition[0] < 40 && startingPosition[1] < 40 && startingPosition[0] - startingPosition[1] < tolerance)
			return 1;
		else if (startingPosition[1] + tolerance < startingPosition[0])
			return 2;
		else if (startingPosition[0] > 40 && startingPosition[1] > 40 && startingPosition[0] - startingPosition[1] < tolerance)
			return 3;
		else if (startingPosition[0] + tolerance < startingPosition[1])
			return 4;
		else return 0;	//this should not happen
	}
}