import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
/**
 * This class contains a simple main method that
 * prints the light reading from the light sensor
 * to RConsole whenever a button is pressed, until
 * the exit button is pressed.
 * 
 * @author Antoine Tu
 *
 */
public class LightTest {
	static LightSensor lightSensor = new LightSensor(SensorPort.S1);
	public static void main(String[] args) {
		RConsole.openBluetooth(20000);
		lightSensor.calibrateHigh();
		lightSensor.calibrateLow();
		while (Button.waitForPress() != Button.ID_ESCAPE) {
			RConsole.println(String.valueOf(lightSensor.readNormalizedValue()));
		}
	}
}