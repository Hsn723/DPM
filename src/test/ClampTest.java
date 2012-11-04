package test;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import master.BTConnector;
import master.Clamp;

/**
 * This class contains a simple method
 * that gives the option to choose between
 * testing the gripping or releasing mechanism
 * of a remote Clamp object.
 * 
 * @author Lixuan Tang
 * @author Luke Potter
 *
 */
public class ClampTest {
	private static int buttonChoice;
	
	public static void main(String[] args) {
		Clamp clamp;
		BTConnector btConnector = new BTConnector();
		if (btConnector.doRemoteConnection("Scorpio")) {
			clamp = new Clamp(btConnector.getRemoteClamp());
			do {
				LCD.clear();

				LCD.drawString("< Left 	| Right >", 0, 0);
				LCD.drawString("       	|        ", 0, 1);
				LCD.drawString(" grip	| release", 0, 2);


				buttonChoice = Button.waitForPress();
			} while (buttonChoice != Button.ID_LEFT
					&& buttonChoice != Button.ID_RIGHT);

			while (buttonChoice != Button.ID_ESCAPE){
				buttonChoice = Button.waitForPress();
				if (buttonChoice == Button.ID_LEFT) {
					clamp.grip();
					
				} else {
					clamp.release();
				}
			}
		}
		System.exit(0);
	}
}
