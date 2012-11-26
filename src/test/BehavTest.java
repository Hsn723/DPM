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
 * Test file for behaviors. In this test, we start by calling
 * the most basic behavior (GoForward). When it is shown to work
 * in a predictable way, we stack the next behavior and reiterate the test.
 * This test allows us to observe the correctness of the interactions between
 * the behaviors when they are stacked together and fed to the arbitrator.
 * 
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
		Behavior b3 = new BeaconSweepBehavior(45);
		Behavior b4 = new BeaconGrabBehavior();
		Behavior b5 = new BeaconDropBehavior();
		
		//Behavior[] behaviors = {b0, b1, b2, b3, b4};
		Behavior[] behaviors = {b3,b4};
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