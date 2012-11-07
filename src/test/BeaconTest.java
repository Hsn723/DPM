package test;

import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteMotor;
import master.*;

/**
 * This class contains a single main method that accesses
 * a remote NXT using BTConnector in order to test the
 * various methods that BTConnector offers.
 * <p>This will allow us to demonstrate if it is feasible
 * to simply rely on the master brick for all the computation.
 * 
 * @author Antoine Tu
 * @author Jeffrey Durocher
 * @author Lixuan Tang
 *
 */
public class BeaconTest {
	private static int buttonChoice;

	public static void main(String[] args) {
		// setup the odometer, display, and ultrasonic and light sensors
		TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(patBot, true);
		LightSensor ls = new LightSensor(SensorPort.S1);
		//RConsole.openBluetooth(20000);
		//RConsole.openUSB(10000);
		
		while (Button.waitForPress() != Button.ID_ESCAPE) {
			// perform the light sensor localization
			BeaconLocalizer lsl = new BeaconLocalizer(odo, ls);
			Motor.A.setSpeed(100);
			Motor.B.setSpeed(100);
			patBot.rotateIndependently(360); //rotate 360 degrees
			lsl.doSearch(); //search for light source				
		}
}
}

