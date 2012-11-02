import lejos.nxt.*;
//import lejos.nxt.comm.RConsole;
import static java.lang.Math.*;

public class LightLocalizer {
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	private Navigation navigation;
	private UltrasonicSensor ultrasonicSensor;
	// Define constants
	private static final int FORWARD_SPEED = 5;
	private static final int ROTATE_SPEED = 20;
	private static double[] headings = new double[3];
	private static final double SENSOR_OFFSET = 12;
	private static final int LINE_THRESHOLD = 44;
	
	private static final int LIGHT_DISTANCE_THRESHOLD = 30; //TODO: change this value
	private int brightestLightValue = 0;
	private double lightHeading = 0;
	
	public LightLocalizer(Odometer odo, LightSensor ls) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		this.navigation = new Navigation(odo);
		this.ultrasonicSensor = new UltrasonicSensor(SensorPort.S2);
		// turn on the light
		ls.setFloodlight(true);
	}
		
	public void doSweepingLightLocalization() {
		ls.calibrateHigh();
		ls.calibrateLow();
		NXTRegulatedMotor lightMotor = Motor.C;
		lightMotor.setSpeed(140);
		
		// Poll the ultrasonic sensor for distance
		double distance = ultrasonicSensor.getDistance();
		
		// While we are more than 30 cm away from the light, keep going
		while (distance >= LIGHT_DISTANCE_THRESHOLD) {
			robot.setSpeeds(FORWARD_SPEED, 0);
			robot.goForward();
			odo.getPosition(headings);
			int lightHeadingCorrection = 0;
			// Sweep 90 degrees for the highest light value
			lightMotor.rotate(90, true);
			while (lightMotor.getTachoCount() <= 90) {
				if (ls.getNormalizedLightValue() > brightestLightValue) {
					brightestLightValue = ls.getNormalizedLightValue();
					lightHeadingCorrection = lightMotor.getTachoCount();
				}
			}
			// Sweep -90 degrees for the highest light value
			lightMotor.rotate(-180, true);
			while (lightMotor.getTachoCount() >= -90) {
				if (ls.getNormalizedLightValue() > brightestLightValue) {
					brightestLightValue = ls.getNormalizedLightValue();
					lightHeadingCorrection= lightMotor.getTachoCount();
				}
			}
			// Fix the heading to the light source
			lightHeading += lightHeadingCorrection;
			// Reset the light sensor position to its 0 degrees
			lightMotor.rotate(90);
			// Turn to the brightest source of light found
			navigation.turnTo(lightHeading);
			
			// Update the distance
			distance = ultrasonicSensor.getDistance();
			
			/*if (firstIteration) {
				RConsole.println("Initial bearings to target");
				RConsole.println("x = " + String.valueOf(headings[0]));
				RConsole.println("y = " + String.valueOf(headings[1]));
				RConsole.println("theta = " + String.valueOf(lightHeading));
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				firstIteration = false;
			}*/
		}
		/*RConsole.println("Final bearings to target");
		odo.getPosition(headings);
		RConsole.println("x = " + String.valueOf(headings[0]));
		RConsole.println("y = " + String.valueOf(headings[1]));
		RConsole.println("theta = " + String.valueOf(headings[2]));*/
	}
	/* 
	 * TODO: This method needs to be updated or verified to
	 * correctly place our robot to (0,0) at the beginning of the game.
	*/
	/*
	 * TODO: We need to find a way to add odometry correction
	 */
	public void doLocalization() {
		this.navigation.travelTo(7, 7);
		
		this.robot.setSpeeds(0, -ROTATE_SPEED);
		this.robot.rotateIndependently(360); //turn 360 degrees while we are scanning for lines
		
		//We expect to hit a horizontal, vertical, horizontal and vertical line
		// so an array of 4 values is enough
		double[] lines = new double[4];
		int lineCounter = 0;
		while (lineCounter < 4)
		{
			double lsValue = getFilteredData();
			// If a line is detected, store it in the array, then sleep for a while
			// to ensure a line isn't detected twice
			if (lsValue < LINE_THRESHOLD)
			{
				this.odo.getPosition(headings);
				lines[lineCounter] = headings[2];
				Sound.playTone(1000, 500, 30);
				lineCounter++;
				try	{ Thread.sleep(500); }
				catch (InterruptedException e) {}
			}
		}
		
		// When done scanning, return the robot to its position and stop
		this.navigation.turnTo(0);
		this.odo.setPosition(new double[] {0,0,0}, new boolean[] {true,true,true});
		this.robot.setSpeeds(0,0);
	
		// Using the formulas on slide 24
		double xTheta = lines[0] - lines [2];
		double yTheta = lines[1] - lines [3];
		
		double x = -SENSOR_OFFSET * cos(Math.toRadians(yTheta/2));
		double y = -SENSOR_OFFSET * cos(Math.toRadians(xTheta/2));
		
		double deltaT = 90 - (lines[0] - 180) + yTheta/2;
		
		// Travel to the computed (0,0,0)
		this.navigation.travelTo(x, y);
		this.navigation.turnTo(deltaT);
		//this.navigation.turnTo(0);
		this.odo.setPosition(new double[] {0,0,0}, new boolean[] {true,true,true});
	}
	// We think our sensor might be giving us inconsistent readings,
	// so we introduce a filter.
	private double getFilteredData()
	{
		int[] rawData = new int[20];
		for (int i = 0; i < rawData.length; i++)
			rawData[i] = ls.readValue();
		int average = 0;
		for (int data:rawData)
			average += data;
		return average / rawData.length;
	}
}
