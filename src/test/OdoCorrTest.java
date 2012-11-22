package test;
import lejos.nxt.*;
import master.*;

/**
 * This class still needs testing
 * 
 * Test class for determining the accuracy of our 
 * navigation and odometer when traveling to a specific 
 * location, then turning and traveling to a different 
 * location.
 * 
 * Uses Odometry Correction
 * 
 * Travels 2 tiles up, then 2 tiles to the right,
 * then 2 tiles down, and 2 tiles to the left 
 * to complete a square
 * @author Jeffrey Durocher
 *
 */

public class OdoCorrTest {
	static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	static Odometer odometer = new Odometer(robot, true);
	static Navigation navigation = new Navigation(odometer);
	static OdometryCorrection odometryCorrection = new OdometryCorrection(odometer);
	public static void main(String[] args) {
		LCDInfo lcdInfo = new LCDInfo(odometer);
		odometryCorrection.start();
		navigation.travelTo( 0, 60.96 );
		navigation.travelTo( 60.96, 60.96 );
		navigation.travelTo( 60.96, 0 );
		navigation.travelTo( 0, 0);
		navigation.turnTo( 0 );	//might not be needed?
		while(Button.waitForPress() != Button.ID_ESCAPE) {}
	}
}