package master;

import master.Forklift.LiftLevel;

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
	public Attacker(TwoWheeledRobot robot, Odometer odometer, int xFlag, int yFlag, int xDest, int yDest, String remoteNXTName) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
	}

	public void searchBeacon() {
		// Write algorithm to search for the beacon here.
		//TODO
	}
	
	/**
	 * Make the robot move to the designated
	 * dropoff point, then release the beacon
	 * and move out of the field.
	 */
	public void depositBeacon() {
		navigation.travelTo(xDest, yDest);
		forklift.goToHeight(LiftLevel.LOW);
		clamp.release();
		// TODO: we might want to decide on another dropoff point
		navigation.travelTo(startingPosition[0], startingPosition[1]);
	}
}