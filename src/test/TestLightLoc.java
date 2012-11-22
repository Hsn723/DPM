package test;
import lejos.nxt.*;
import master.*;

public class TestLightLoc{
	private static int buttonChoice;
	static TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
	static Odometer odo = new Odometer(patBot, true);
	LightSensor ls = TwoWheeledRobot.getMasterLightSensor();
	
	public static void main(String[] args) {
		// setup the odometer, display, and ultrasonic and light sensors
		LightSensor ls = TwoWheeledRobot.getMasterLightSensor();		
	
		do {
			// clear the display
			LCD.clear();
			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left 	| Right >", 0, 0);
			LCD.drawString("       	|        ", 0, 1);
			LCD.drawString("      	| Light Loc", 0, 2);
			buttonChoice = Button.waitForPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);
		if (buttonChoice == Button.ID_LEFT) {
		} else {
			// perform the light sensor localization
			LightLocalizer lsl = new LightLocalizer(odo, ls);
			lsl.doLocalization();	
		}	
		while (Button.waitForPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}
