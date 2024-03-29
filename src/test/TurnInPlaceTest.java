package test;
import lejos.nxt.*;
import master.*;

/** 
 * Test class to measure the accuracy of our odometer when turning
 * 
 * Does a 360 spin, verfied visually.
 * @author 
 *
 */
public class TurnInPlaceTest {
	static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	static Odometer odometer = new Odometer(robot, true);
	static Navigation navigation = new Navigation(odometer);
	public static void main(String[] args) {
		LCDInfo lcdInfo = new LCDInfo(odometer);
		robot.rotate(360);
		while(Button.waitForPress() != Button.ID_ESCAPE) {}
	}
}