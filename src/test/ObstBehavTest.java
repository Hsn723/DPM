package test;

import lejos.nxt.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import master.Defender;
import master.GoForwardBehavior;
import master.ObstacleAvoidanceBehavior;
import master.Odometer;
import master.TwoWheeledRobot;

public class ObstBehavTest {
	private static TwoWheeledRobot robot;
	private static Odometer odometer;
	private static final double TILE_FACTOR = 30.48;
	// Start a dummy role just for testing.
	static Defender defender;
	
	// Make the robot go to a point then back to the origin
	public static void main(String[] args) {
		robot = new TwoWheeledRobot(Motor.A, Motor.B);
		odometer = new Odometer(robot, true);
		defender = new Defender(robot, odometer, 4, 4, 2, 5);
		
		//Behavior b0 = new ExitFieldBehavior();
		Behavior b0 = new GoForwardBehavior();
		Behavior b1 = new ObstacleAvoidanceBehavior();	
		Behavior[] behaviors = {b0, b1};
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
	}
}
