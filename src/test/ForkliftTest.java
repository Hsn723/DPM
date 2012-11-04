package test;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import master.*;

/**
 * This class contains a simple method
 * that gives the option to choose between
 * testing the ascend or descend mechanism
 * of a remote Forklift object.
 * 
 * @author Lixuan Tang
 * @author Antoine Tu
 *
 */
public class ForkliftTest {
	private static int buttonChoice;
	
	public static void main(String[] args) {
		Forklift forklift;
		BTConnector btConnector = new BTConnector();
		if (btConnector.doRemoteConnection("Scorpio")) {
			forklift = new Forklift(btConnector.getRemoteForklift());
			do {
				LCD.clear();

				LCD.drawString("< Left 	| Right >", 0, 0);
				LCD.drawString("       	|        ", 0, 1);
				LCD.drawString(" ascend	| descend", 0, 2);


				buttonChoice = Button.waitForPress();
			} while (buttonChoice != Button.ID_LEFT
					&& buttonChoice != Button.ID_RIGHT);

			while (buttonChoice != Button.ID_ESCAPE){
				buttonChoice = Button.waitForPress();
				if (buttonChoice == Button.ID_LEFT) {
					forklift.ascend();
					
				} else {
					forklift.descend();
				}
			}
		}
		System.exit(0);
	}
}
