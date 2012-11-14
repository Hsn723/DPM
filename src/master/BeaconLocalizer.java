package master;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteMotor;
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
	
	private UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
	
	public double brightestLightAngle = 0;
	private double lightHeading = 0;
	private static final int BEACON_DISTANCE_THRESHOLD = 20;
	
	//parameters to help keeping track of the average light values, used for line detection
	private final int bufferSize = 20;
	private int[] previousLightValues = new int[bufferSize];
	private int averageLightValueUpdateCounter = 0;
	private int averageLightValue = 0;
	private int changeThreshold = 8;
	private int currentLightValue = 0;
	private int totalReadCount = 0;
	private boolean beaconFound = false;

	/**
	 * Initializes a BeaconLocalizer object.
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
	}
	
	/**
	 * Searches for the brightest light value.
	 * TODO: NXT rotation and movement correction should happen here.
	 * Also, we should make use of the forklift to search at different heights.
	 */
	public void doSearch() {
		lightSensor.setFloodlight(true); //turn on light (not necessary, more of a debug function)
		//took off light calibration as it was reporting false positives
		
		robot.rotateIndependently(360); //rotate 360 degrees
		findLight();
		
		robot.rotate(180);
		while (ultrasonicSensor.getDistance() > 25){
			robot.goForward(); //keep going forward until 25 units away from light source
		}
		robot.stop();
		robot.rotate(180);
	}
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
	}
