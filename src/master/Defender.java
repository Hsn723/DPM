package master;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
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
	public Defender(TwoWheeledRobot robot, Odometer odometer, double xFlag, double yFlag, double xDest, double yDest, String remoteNXTName) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
	}
	
	/**
	 * Dummy Defender
	 * @param robot
	 * @param odometer
	 * @param xFlag
	 * @param yFlag
	 * @param xDest
	 * @param yDest
	 * 
	 * @deprecated
	 */
	public Defender(TwoWheeledRobot robot, Odometer odometer, double xFlag, double yFlag, double xDest, double yDest) {
		super(robot, odometer, xFlag, yFlag, xDest, yDest);
	}
	
	/**
	 * Make the robot travel to the beacon's given location
	 * and then grab the beacon.
	 */
	public void getBeacon() {
		// Instantiate behaviors.
		//Behavior b0 = new GoForwardBehavior();
		Behavior b0 = new BeaconSweepBehavior(45);
		Behavior b1 = new TravelToBehavior(xBeacon, yBeacon);
		Behavior b2 = new ObstacleAvoidanceBehavior();
		Behavior b3 = new BeaconGrabBehavior();
		Behavior[] behaviors = {b0, b1, b2, b3};

		// Instantiate and start arbitrator.
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
		/*
		// First navigate to given coordinates for beacon
		navigation.travelTo(xBeacon, yBeacon);
		
		// Do a beacon localization to make sure we are well placed.
		beaconLocalizer = new BeaconLocalizer(robot, odometer, btConnector.getRemoteLightSensor(), forklift);
		beaconLocalizer.doSearch();	//not complete yet
		
		//Now we should be placed correctly and only need to clamp.
		clamp.grip();
		*/
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
		
		// Determine and move to a hiding spot
		// FIXME: This is only for the demo
		//navigation.travelTo( yDest, xDest );
		
		// Drop the beacon
		//forklift.goToHeight(LiftLevel.LOW);
		//clamp.release();
		
		// Move out of the field
		/*
		 * TODO: The origin would be the safest return spot.
		 * However it could be far from our current location.
		 * In the future we could search for the closest safe spot.
		 */
		//navigation.travelTo(startingPosition[0], startingPosition[1]);
		
	}
}