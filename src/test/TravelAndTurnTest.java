package test;
import lejos.nxt.*;
import master.*;


public class TravelAndTurnTest {
	static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	static Odometer odometer = new Odometer(robot, true);
	static Navigation navigation = new Navigation(odometer);
	public static void main(String[] args) {
		LCDInfo lcdInfo = new LCDInfo(odometer);
		navigation.travelTo( 0, 60.96 );
		navigation.travelTo( 60.96, 60.96 );
		navigation.travelTo( 60.96, 0 );
		navigation.travelTo( 0, 0);
		navigation.turnTo( 0 );	//might not be needed?
		while(Button.waitForPress() != Button.ID_ESCAPE) {}
	}
}