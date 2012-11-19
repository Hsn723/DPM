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
	public Attacker(TwoWheeledRobot robot, Odometer odometer, double xFlag, double yFlag, double xDest, double yDest, String remoteNXTName) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
	}

	public void searchBeacon() {
		// Write better algorithm to search for the beacon here.
		
		//TODO this is only for the demo
		BeaconLocalizer beaconLocalizer = new BeaconLocalizer(robot, odometer, btConnector.getRemoteLightSensor(), forklift, getCorner());
		beaconLocalizer.doSearch();
		clamp.grip();
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
	
	private int getCorner() {
		double tolerance = 5;	//give 5cm of error margin, it doesn't really matter
		if (startingPosition[0] - startingPosition[1] < tolerance)
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