package master;
import lejos.nxt.UltrasonicSensor;

/**
 * Ultrasonic localizer for robot
 * Uses falling edge
 * @author 
 */
public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 50;
	public static final int WALL_DIST = 40;	//40
	public static final int MAX_DIST = 50;	//50

	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	private Navigation navigation;
	
	public USLocalizer(Odometer odo, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.locType = locType;
		
		// initialize Navigation
		this.navigation = new Navigation(odo);
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	/**
	 * Perform ultrasonic localization
	 */
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
			this.robot.setRotationSpeed(ROTATION_SPEED);
			int distance;
			while(true)
			{
				distance = getFilteredData();
				if (distance == MAX_DIST) break;
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while(true)
			{
				distance = getFilteredData();
				if (distance < WALL_DIST)
				{
					this.odo.getPosition(pos);	//this works like a pointer?
					angleA = pos[2];
					break;
				}
			}
			// switch direction and wait until it sees no wall
			this.robot.setRotationSpeed(-ROTATION_SPEED);
			while(true)
			{
				distance = getFilteredData();
				if (distance == MAX_DIST) break;
			}
			// keep rotating until the robot sees a wall, then latch the angle
			while(true)
			{
				distance = getFilteredData();
				if (distance < WALL_DIST)
				{
					this.odo.getPosition(pos);
					angleB = pos[2];
					break;
				}
			}
			this.robot.setRotationSpeed(0);
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			double deltaT;
			if (angleA > angleB) deltaT = 45 - (angleA + angleB)/2;
			else deltaT = 225 - (angleA + angleB)/2;
			this.navigation.turnTo(-deltaT);
			
			// update the odometer position
			odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it. 
			 */
			this.robot.setRotationSpeed(-ROTATION_SPEED);
			int distance;
			// Do FALLING_EDGE in reverse: rotate the robot until it sees a wall
			while(true)
			{
				distance = getFilteredData();
				if (distance <= WALL_DIST) break;
			}
			// Then rotate until no walls are seen and set angleA
			while(true)
			{
				distance = getFilteredData();
				if (distance > WALL_DIST)
				{
					this.odo.getPosition(pos);
					angleA = pos[2];
					break;
				}
			}
			// Change direction and rotate until next wall
			this.robot.setRotationSpeed(ROTATION_SPEED);
			while(true)
			{
				distance = getFilteredData();
				if (distance <= WALL_DIST) break;
			}
			// Finally, rotate until no walls are seen and set angleB
			while(true)
			{
				distance = getFilteredData();
				if (distance > WALL_DIST)
				{
					this.odo.getPosition(pos);
					angleB = pos[2];
					break;
				}
			}
			this.robot.setRotationSpeed(0);
			// Apply formula in tutorial slides
			double deltaT;
			if (angleA > angleB) deltaT = 45 - (angleA + angleB)/2;
			else deltaT = 225 - (angleA + angleB)/2;
			this.navigation.turnTo(-deltaT);
			// update the odometer position (example to follow:)
			odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		}
	}
	
	// Filter the distance values to ensure a higher precision
	// To increase precision, we are averaging a sample of 5 readings
	/*
	 * TODO: implement a more accurate filter
	 */
	/**
	 * Basic filter for the ultrasonic sensor
	 * @return the filtered reading of the sensor 
	 */
	private int getFilteredData() {
		int distance;
		int[] rawDistances = new int[5];
		for (int i = 0; i < rawDistances.length; i++)
		{
			// do a ping
			us.ping();
			
			// wait for the ping to complete
			try { Thread.sleep(10); } catch (InterruptedException e) {}
			rawDistances[i] = us.getDistance();
		}
		
		distance = 0;
		for (int dist:rawDistances) distance += dist;
		distance /= rawDistances.length;
		if (distance > 50) distance = 50;
				
		return distance;
	}

}
