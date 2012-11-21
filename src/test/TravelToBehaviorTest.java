package test;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import master.*;

/**
 * Test class for TravelToBehavior
 * @author Antoine Tu
 *
 */
public class TravelToBehaviorTest {
	private static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private static Odometer odometer = new Odometer(robot, true);
	private static final double TILE_FACTOR = 30.48;
	// Start a dummy role just for testing.
	Defender defender = new Defender(robot, odometer, 4, 4, 2, 5, "Scorpio");
	
	// Make the robot go to a point then back to the origin
	public static void main(String[] args) {
		Behavior b0 = new ExitFieldBehavior();
		Behavior b1 = new TravelToBehavior(10 * TILE_FACTOR, 10 * TILE_FACTOR);	//travel to tile (10,10)
		Behavior[] behaviors = {b0, b1};
		Arbitrator arbitrator = new Arbitrator(behaviors, true);
		arbitrator.start();
	}
}