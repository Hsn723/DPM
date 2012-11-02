import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.nxt.remote.*;

public class Defender {
	private TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private Odometer odometer = new Odometer(robot, true);
	private Navigation navigation = new Navigation(odometer);
	private UltrasonicSensor ultrasonicLocalizerSensor = new UltrasonicSensor(SensorPort.S1);
	private LightSensor lightLocalizerSensor = new LightSensor(SensorPort.S2);
	private RemoteNXT slaveNXT;
	private int xBeacon, yBeacon, zBeacon;
	
	/*
	 * TODO: check the BT class for info on beacon coordinates.
	 * We will need to update this to take in the coordinates of the beacon.
	 */
	public Defender(int x, int y, int z) {
		// Establish connection to slaveNXT
		try {
			slaveNXT = new RemoteNXT("Scorpio", Bluetooth.getConnector());
		} catch (IOException e) {}
		xBeacon = x;
		yBeacon = y;
		zBeacon = z;
		// First navigate to given coordinates for beacon
		navigation.travelTo(xBeacon, yBeacon);
		
		// Start the slaveNXT's beacon grabbing program:
		// Move the forklift on the slaveNXT if the beacon is elevated. TODO: check how to pass zBeacon
		// Get close to the beacon using the slaveNXT's ultrasonic.
		// Clamp the beacon.
		
		// Determine and move to a hiding spot
		
		// Start the slaveNXT's beacon dropping program:
		// Lower the forklift if elevated
		// Drop the beacon
		
		// Move out of the field
	}
}