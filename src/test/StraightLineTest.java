package test;
import lejos.nxt.*;
import master.*;

/**
 * This class tests the accuracy of our 
 * odometer and navigation when travelling 
 * in a straight line.
 * @author 
 *
 */

public class StraightLineTest {
	static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	static Odometer odometer = new Odometer(robot, true);
	static Navigation navigation = new Navigation(odometer);
	public static void main(String[] args) {
		LCDInfo lcdInfo = new LCDInfo(odometer);
		navigation.travelTo(0,20);
		while(Button.waitForPress() != Button.ID_ESCAPE) {}
	}
}