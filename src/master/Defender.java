package master;

import master.Forklift.LiftLevel;

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
	public Defender(TwoWheeledRobot robot, Odometer odometer, int xFlag, int yFlag, int xDest, int yDest, String remoteNXTName) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
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
		// TODO
		
		// Drop the beacon
		forklift.goToHeight(LiftLevel.LOW);
		clamp.release();
		
		// Move out of the field
		/*
		 * TODO: The origin would be the safest return spot.
		 * However it could be far from our current location.
		 * In the future we could search for the closest safe spot.
		 */
		navigation.travelTo(startingPosition[0], startingPosition[1]);
	}
}