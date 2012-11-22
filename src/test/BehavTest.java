package test;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import master.*;

/**
 * Test class for TravelToBehavior.
 * The robot is expected to travel to
 * coordinates and then exit the field
 * @author Antoine Tu
 *
 */
public class BehavTest {
	private static TwoWheeledRobot robot;
	private static Odometer odometer;
	private static final double TILE_FACTOR = 30.48;
	// Start a dummy role just for testing.
	static Defender defender;
	
	// Make the robot go to a point then back to the origin
	public static void main(String[] args) {
		robot = new TwoWheeledRobot(Motor.A, Motor.B);
		odometer = new Odometer(robot, true);
		defender = new Defender(robot, odometer, 4, 4, 2, 5, "Scorpio");
		
		//Behavior b0 = new ExitFieldBehavior();
		Behavior b0 = new ExitFieldBehavior();
		Behavior b1 = new TravelToBehavior(2 * TILE_FACTOR, 2 * TILE_FACTOR);	//travel to tile (10,10)
		Behavior b2 = new ObstacleAvoidanceBehavior();
		Behavior b3 = new BeaconSweepBehavior();
		Behavior[] behaviors = {b0, b1, b2, b3};
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
		Sound.twoBeeps();
	}
}