package master;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import master.Forklift.LiftLevel;
/**
 * This class contains methods which do a
 * search for the beacon and re-orient the
 * NXT according to the detected position
 * of the beacon.
 * 
 * @author Antoine Tu
 * @author Jeffrey Durocher
 * @author Lixuan Tang
 *
 */
public class BeaconLocalizer {
	private Odometer odometer;
	private TwoWheeledRobot robot;
	private LightSensor lightSensor;
	private Forklift forklift;
	private Navigation navigation;
	
	
	// TEMPORARY FIX: open up the clamp as big as we can
	// so that when be backtrack the beacon fits inside
	private Clamp clamp;
	
	private UltrasonicSensor ultrasonicSensor;
	
	public double brightestLightAngle = 0;
	private double lightHeading = 0;
	private static final int BEACON_DISTANCE_THRESHOLD = 20;
	private final int SENSOR_OFFSET = -5;
	
	//parameters to help keeping track of the average light values, used for line detection
	private final int bufferSize = 10;
	private int[] previousLightValues = new int[bufferSize];
	private int averageLightValueUpdateCounter = 0;
	private int averageLightValue = 0;
	private int changeThreshold = 3;
	private int currentLightValue = 0;
	private int totalReadCount = 0;
	private boolean beaconFound = false;	//deprecate this
	
	private boolean isTurning = false;
	private int corner = 1;
	private boolean destinationArrived = false;
	
	private static final int SEARCH_TRAVEL_DISTANCE = 41;
	
	
	/**
	 * Initializes a BeaconLocalizer object.
	 * @param robot our robot
	 * @param odometer an Odometer
	 * @param lightSensor a LightSensor
	 * @param forkliftMotor the motor of our forklift
	 */
	public BeaconLocalizer(TwoWheeledRobot robot, Odometer odometer, LightSensor lightSensor, Forklift forklift) {
		this.odometer = odometer;
		this.navigation = new Navigation(odometer);
		this.lightSensor = lightSensor;
		this.forklift = forklift;
		this.robot = robot;
		this.ultrasonicSensor = robot.getFrontUltrasonicSensor();
	}
	
	/**
	 * Initializes a BeaconLocalizer object.
	 * @param robot our robot
	 * @param odometer an Odometer
	 * @param lightSensor a LightSensor
	 * @param forkliftMotor the motor of our forklift
	 * @param corner the corner we are starting in
	 * 
	 */
	public BeaconLocalizer(TwoWheeledRobot robot, Odometer odometer, LightSensor lightSensor, Forklift forklift, int corner) {
		this.odometer = odometer;
		this.navigation = new Navigation(odometer);
		this.lightSensor = lightSensor;
		this.forklift = forklift;
		this.robot = robot;
		this.ultrasonicSensor = robot.getFrontUltrasonicSensor();
		this.corner = corner;
	}
	
	/**
	 * Searches for the brightest light at different levels.
	 */
	public void doSearchBehavior() {
		forklift.goToHeight(LiftLevel.LOW);
		robot.rotateIndependently(360);
		collectLightValues(false);
		
		// Search other levels
		/*
		if(!Role.beaconDetected) {
			forklift.goToHeight(LiftLevel.MIDLOW);
			robot.rotateIndependently(360);
			collectLightValues();
		}
		if(!Role.beaconDetected) {
			forklift.goToHeight(LiftLevel.MIDHIGH);
			robot.rotateIndependently(360);
			collectLightValues();
		}
		
		*/
		
		if(!Role.beaconDetected) {
			forklift.goToHeight(LiftLevel.HIGH);
			robot.rotateIndependently(360);
			collectLightValues(false);
		}
		if(Role.beaconDetected) {
			int min = 255;
			double angle = 0;
			int currentReading;
			navigation.turnTo(brightestLightAngle - 180 + SENSOR_OFFSET);
			//robot.rotate(180);
			// note this works only for when the beacon is on the floor.
			// if the beacon is placed on top of a block and there's a obstacle in front of it, this would break
			if (getFilteredData() > BEACON_DISTANCE_THRESHOLD){
				robot.rotateIndependently(30);
				while (robot.isTurning()){
					currentReading = getFilteredData();
					if (currentReading < min){
						min = currentReading;
						angle = Role.odometer.getTheta();
					}
				}
				robot.rotateIndependently(-60);
				while (robot.isTurning()){
					currentReading = getFilteredData();
					if (currentReading < min){
						min = currentReading;
						angle = Role.odometer.getTheta();
					}
				}
				if (min == 255){
					Sound.beep();
					robot.rotate(30);
					robot.goForward(20);
					doSearchBehavior();
					return;
				}
				navigation.turnTo(angle-2);
				robot.goForward(min-20);
			}
			Role.beaconReached = true;
		}
	}
	
	/**
	 * Searches for the brightest light value.
	 * TODO: NXT rotation and movement correction should happen here.
	 * Also, we should make use of the forklift to search at different heights.
	 * @deprecated
	 */
	public void doSearch() {
		lightSensor.setFloodlight(true); //turn on light (not necessary, more of a debug function)
		//took off light calibration as it was reporting false positives
		
		forklift.goToHeight(LiftLevel.LOW);
		
		boolean turnedOnce = false;
		
		while (!beaconFound){
			robot.rotateIndependently(360); //rotate 360 degrees
			//findLight();
			collectLightValues(false);
		
			if (!beaconFound){
				double nextX = odometer.getX(), nextY = odometer.getY();
				nextX += SEARCH_TRAVEL_DISTANCE;
				nextY += SEARCH_TRAVEL_DISTANCE;
				/*switch (this.corner) {
				case 1:
					nextX += SEARCH_TRAVEL_DISTANCE;
					nextY += SEARCH_TRAVEL_DISTANCE;
					break;
					
				case 2:
					nextX -= SEARCH_TRAVEL_DISTANCE;
					nextY += SEARCH_TRAVEL_DISTANCE;
					break;
				
				case 3:
					nextX -= SEARCH_TRAVEL_DISTANCE;
					nextY -= SEARCH_TRAVEL_DISTANCE;
					break;
					
				case 4:
					nextX += SEARCH_TRAVEL_DISTANCE;
					nextY -= SEARCH_TRAVEL_DISTANCE;
					break;

				default:
					break;
				}
				*/
				if (!turnedOnce)	{
					navigation.turnTo(45);
					turnedOnce = true;
					robot.setSpeeds(10d, 0);
				}
				
				robot.goForward(45);
				
				
			}
			
		}
		
		Sound.beepSequenceUp();
		
		
		while (!destinationArrived) {
		navigation.turnTo(brightestLightAngle);
		robot.rotate(180);
		
		
		if (ultrasonicSensor.getDistance() > 5){
			robot.goForward(20);
		}
		
		else{
			destinationArrived = true;
			break;
		}
		
		robot.rotateIndependently(360); //rotate 360 degreesf
		collectLightValues(false);
		//robot.rotate(180);
		//robot.goForward();
		
		/*
			while (ultrasonicSensor.getDistance() > 20){
			robot.goForward(); //keep going forward until 25 units away from light source
			}
			*/
		}
		
		
		
		robot.stop();
		robot.rotate(180);
		
		// TEMPORARY FIX
		//clamp.release();
		
		robot.goForward(-20);	//backtrack so that the beacon gets in the clamp
		
	}
	
	/**
	 * Reads the current light sensor value, compares it with the 
	 * moving average of previous light values and determines if the 
	 * beacon is found
	 * 
	 * Beeps if beacon is found
	 */
	public void collectLightValues(boolean useMotors){
		int brightestLightValue = 0;
		long startTime = System.currentTimeMillis();
		if (useMotors){ robot.setSpeeds(0,35);
		robot.rotateIndependently(200);
		}
		while (System.currentTimeMillis() - startTime < 8000){
			totalReadCount++;
			currentLightValue = lightSensor.getLightValue();
			if (currentLightValue - averageLightValue > changeThreshold){
				if (this.totalReadCount > this.bufferSize){
					if (currentLightValue > brightestLightValue ){
						brightestLightAngle = odometer.getTheta();
						brightestLightValue = currentLightValue;
						Sound.twoBeeps();
						//beaconFound = true;
						if (useMotors){
							Sound.twoBeeps();
							robot.rotate(-5);
							robot.stop();
						}
						Role.beaconDetected = true;
						
					}
				}
			
				else {
					this.updateAverageLightValue(currentLightValue);
				}
			}
			else {
				this.updateAverageLightValue(currentLightValue);
			}
		}
	}
	
	/**
	 * Reads the current light sensor value, compares it with the 
	 * moving average of previous light values and determines if the 
	 * beacon is found
	 * 
	 * Beeps if beacon is found
	 * @deprecated
	 */
	public void findLight() {
		while (!beaconFound){ 
			totalReadCount++; // increase light sensor read count
			//currentLightValue = lightSensor.getLightValue(); //get current light value
			currentLightValue = lightSensor.getLightValue(); //removed normalized so it returns a value between 0 and 100 instead of 0 and 1000
			//RConsole.println(String.valueOf(currentLightValue));
			//RConsole.println("Average: " + String.valueOf(averageLightValue));
			//if the difference between the average and the current is big then light is detected, else add the read to the average
			if (Math.abs(currentLightValue - averageLightValue) > changeThreshold){
				//light is only detected if we know we have a stable average therefore read count must be greater than the buffer
				if (this.totalReadCount > this.bufferSize){
					beaconFound = true;
					Sound.beep();
					brightestLightAngle = odometer.getTheta();
					}
			
				else {
					this.updateAverageLightValue(currentLightValue);
				}
			}
			else {
				this.updateAverageLightValue(currentLightValue);
			}
		}
	}
	
	/**
	 * Updates the average light reading values.
	 * @param lightValue the light value to add to the average.
	 */
	private void updateAverageLightValue(int lightValue){
		this.previousLightValues[this.averageLightValueUpdateCounter % bufferSize] = lightValue;
		averageLightValueUpdateCounter++;
				
		int sum = 0;
		for (int i=0;i<this.previousLightValues.length;i++){
			if (this.previousLightValues[i] != 0){
				sum += this.previousLightValues[i];
			}
		}
		
		this.averageLightValue = sum/this.previousLightValues.length;
	}
	
	private int getFilteredData() {
		int distance;
		int[] rawDistances = new int[5];
		for (int i = 0; i < rawDistances.length; i++)
		{
			// do a ping
			ultrasonicSensor.ping();
			
			// wait for the ping to complete
			try { Thread.sleep(10); } catch (InterruptedException e) {}
			rawDistances[i] = ultrasonicSensor.getDistance();
		}
		
		distance = 0;
		for (int dist:rawDistances) distance += dist;
		distance /= rawDistances.length;	
		return distance;
	}
	}
