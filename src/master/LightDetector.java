package master;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

/** 
 * OdometryCorrection.java
 * This class corrects the odometer by detecting the lines
 * 
 * @author Jeffrey Durocher
 */

public class LightDetector extends Thread {
	private static final long SAMPLING_PERIOD = 50;
	
	private Odometer odometer;
	private LightSensor lightSensor; 
	private LightLocalizer lightLoc;
	
	
	//parameters to help keeping track of the average light values, used for line detection
	private final int bufferSize = 5;
	private int[] previousDistanceValues = new int[bufferSize];
	private int averageLightValueUpdateCounter = 0;
	private int averageLightValue = 0;
	private int changeThreshold = 8;
	private int currentLightValue = 0;
	private int totalReadCount = 0;
	
	//parameter to keep track of the direction of travel
	private boolean travellingInX;
	private final int angleTolerance = 30;
	
	private boolean on = true;
	
	// constructor
	@SuppressWarnings("static-access")
	public LightDetector(Odometer odometer, LightLocalizer lightLoc) {
		this.odometer = odometer;
		this.lightSensor = odometer.getTwoWheeledRobot().getMasterLightSensor();
		this.lightLoc = lightLoc;
	}

	
	// run method (required for Thread)
	public void run() {
		long samplingStart, samplingEnd;

		while (on) {			
			samplingStart = System.currentTimeMillis();
			
			currentLightValue = lightSensor.getLightValue();
			totalReadCount++;
			
			
			// the odometer is corrected to the nearest checkpoint when a sharp change in lightSensor value
			// is detected, and also if there are sufficient data in our buffer , otherwise it will simply update the average value. 
			if (Math.abs(currentLightValue - averageLightValue) > changeThreshold){
				if (this.totalReadCount > this.bufferSize){
					lightLoc.pushAngle(odometer.getTheta());
					Sound.beep();
					}
			
				else {
					this.updateAverageLightValue(currentLightValue);
				}
			}
			
			
			else {
				this.updateAverageLightValue(currentLightValue);
			}
			

			// this ensure the odometry correction occurs only once every period
			samplingEnd = System.currentTimeMillis();
			if (samplingEnd - samplingStart < SAMPLING_PERIOD) {
				try {
					Thread.sleep(SAMPLING_PERIOD
							- (samplingEnd - samplingStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}
	
	//this method updates the averageLight value when provided with a lightValue.
	private void updateAverageLightValue(int lightValue){
		this.previousDistanceValues[this.averageLightValueUpdateCounter % bufferSize] = lightValue;
		averageLightValueUpdateCounter++;
				
		int sum = 0;
		for (int i=0;i<this.previousDistanceValues.length;i++){
			if (this.previousDistanceValues[i] != 0){
				sum += this.previousDistanceValues[i];
			}
		}
		
		this.averageLightValue = sum/this.previousDistanceValues.length;
	}
	
	
	
	
	// this method checks for travelling direction based on the Theta.
	private void checkTravellingDirection(){
		//check for travelling direction
				if ( (this.odometer.getTheta() < 90 + this.angleTolerance && this.odometer.getTheta() > 90 - this.angleTolerance) || 
						(this.odometer.getTheta() < 270 + this.angleTolerance && this.odometer.getTheta() > 270 - this.angleTolerance) ) {
					travellingInX = true;
				}
				
				else {
					travellingInX = false;
				}
	}
	
	public void setOn(boolean on){
		this.on = on;
	}
	
}