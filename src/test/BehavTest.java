package test;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import master.*;
import master.Forklift.LiftLevel;

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
	public static int buttonChoice;
	
	// Make the robot go to a point then back to the origin
	public static void main(String[] args) {
		robot = new TwoWheeledRobot(Motor.A, Motor.B);
		odometer = new Odometer(robot, true);
		defender = new Defender(robot, odometer, 4, 4, 2, 5, "Scorpio");
		
		//Behavior b0 = new ExitFieldBehavior();
		
		Behavior b0 = new GoForwardBehavior();
		Behavior b1 = new TravelToBehavior(2 * TILE_FACTOR, 2 * TILE_FACTOR);	//travel to tile (10,10)
		Behavior b2 = new ObstacleAvoidanceBehavior();
		Behavior b3 = new BeaconSweepBehavior();
		Behavior b4 = new BeaconGrabBehavior();
		Behavior b5 = new BeaconDropBehavior();
		
		//Behavior[] behaviors = {b0, b1, b2, b3, b4};
		Behavior[] behaviors = {b0,b4};
		Role.beaconDetected =true;
		Arbitrator arbitrator = new Arbitrator(behaviors);
	
		
		
		
		do {
			LCD.clear();

			LCD.drawString("< Left 	| Right >", 0, 0);
			LCD.drawString("       	|        ", 0, 1);
			LCD.drawString(" START	| 	", 0, 2);


			buttonChoice = Button.waitForPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		while (buttonChoice != Button.ID_ESCAPE){
			buttonChoice = Button.waitForPress();
			if (buttonChoice == Button.ID_LEFT) {
				
				LCDInfo lcd = new LCDInfo();
				arbitrator.start();
				
			} else if (buttonChoice == Button.ID_RIGHT) {
				
			}
		}
	
	
		//arbitrator.start();
		//Sound.twoBeeps();
		System.exit(0);
	}
}