package test;

import lejos.nxt.*;
import lejos.nxt.remote.RemoteMotor;
import master.BTConnector;

/**
 * This class contains a single main method that accesses
 * a remote NXT using BTConnector in order to test the
 * various methods that BTConnector offers.
 * <p>This will allow us to demonstrate if it is feasible
 * to simply rely on the master brick for all the computation.
 * 
 * @author Antoine Tu
 *
 */
public class BTConnectorTest {
	static BTConnector btConnector;
	static LightSensor remoteLightSensor;
	static RemoteMotor remoteMotor;
	static int lastLightReading = 0;
	public static void main(String[] args) {
		btConnector = new BTConnector();
		btConnector.doRemoteConnection("Potato");
		remoteLightSensor = btConnector.getRemoteLightSensor();
		remoteMotor = btConnector.getRemoteClamp();
		remoteLightSensor.calibrateHigh();
		remoteLightSensor.calibrateLow();
		while(Button.waitForPress() != Button.ID_ESCAPE) {
			if (remoteLightSensor.getNormalizedLightValue() > lastLightReading) {
				remoteMotor.rotate(90);
			}
		}
	}
}