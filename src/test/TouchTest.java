package test;

import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import master.BTConnector;

public class TouchTest {
	static BTConnector btConnector;
	static TouchSensor touchSensor;
	public static void main(String[] args) {
		btConnector = new BTConnector();
		if(btConnector.doRemoteConnection("Scorpio")) {
			touchSensor = btConnector.getRemoteTouchSensor();
		}
		while(true) {
			if (touchSensor.isPressed()) {
				Sound.twoBeeps();
			}
		}
	}	
}