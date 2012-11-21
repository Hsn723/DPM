package test;

import lejos.nxt.*;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteMotor;
import master.*;
import master.Forklift.LiftLevel;
import master.USLocalizer.LocalizationType;

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

		BTConnector btConnector = new BTConnector();
		btConnector.doRemoteConnection("Scorpio");
		Forklift forklift = new Forklift(btConnector.getForkliftMotor());
		LightSensor remoteLightSensor = btConnector.getRemoteLightSensor();
		Navigation nav = new Navigation(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		LCDInfo lcd = new LCDInfo(odo);
		
		//RConsole.openBluetooth(20000);
		//RConsole.openUSB(10000);
		
		while (Button.waitForPress() != Button.ID_ESCAPE) {
			// perform the light sensor localization
			/*BeaconLocalizer lsl = new BeaconLocalizer(odo, remoteLightSensor, forklift);
			Motor.A.setSpeed(100);
			Motor.B.setSpeed(100);
			patBot.rotateIndependently(360); //rotate 360 degrees
			lsl.doSearch(); //search for light source
			patBot.stop(); //stop the current spin
			patBot.start(); //restart the engines
			patBot.rotate(180); //rotate 180 from light detection
			Motor.A.setSpeed(-100); //set reverse speeds
			Motor.B.setSpeed(-100);
			while (us.getDistance() > 25){
				patBot.goForward(); //keep going forward until 25 units away from light source
			}
			patBot.stop(); //stop the current motion
			patBot.start(); //restart engines
			patBot.rotate(180); //rotate 180 degrees to have clamp facing the beacon
			*/
			
			//USLocalizer usLocalizer = new USLocalizer(odo, us, LocalizationType.FALLING_EDGE);
			//usLocalizer.doLocalization();
			
			//sleep(3000);
			
			
			
			BeaconLocalizer lsl = new BeaconLocalizer(patBot, odo, remoteLightSensor, forklift);
			lsl.doSearch();
			
			
			Clamp clamp = new Clamp(btConnector.getClampMotor());
			clamp.grip();
			
			
			
			forklift.goToHeight(LiftLevel.HIGH);
			
			nav.travelTo(0, 0);
			
			patBot.rotate(180);
			
			forklift.goToHeight(LiftLevel.LOW);
			
			clamp.release();
			
		}
	}
	
	/**
	 * Make the thread sleep for a given amount of time (in ms)
	 * @param timeout
	 */
	public static void sleep(long timeout){
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			
		}
	}
}

