package master;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

/** 
 * OdometryCorrection.java
 * This class corrects the odometer by detecting the lines
 */

public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	private Odometer odometer;
	private LightSensor lightSensor; 
	
	//parameters to help keeping track of the average light values, used for line detection
	private final int bufferSize = 10;
	private int[] previousDistanceValues = new int[bufferSize];
	private int averageLightValueUpdateCounter = 0;
	private int averageLightValue = 0;
	private int changeThreshold = 8;
	private int currentLightValue = 0;
	private int totalReadCount = 0;
	
	//parameter to keep track of the direction of travel
	private boolean travellingInX;
	private boolean travellingInY;
	private final int angleTolerance = 1;
	
	
	// constructor
	@SuppressWarnings("static-access")
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
		this.lightSensor = odometer.getTwoWheeledRobot().getMasterLightSensor();
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;

		while (true) {			
			correctionStart = System.currentTimeMillis();
			
			currentLightValue = lightSensor.getLightValue();
			totalReadCount++;
			
			// the odometer is corrected to the nearest checkpoint when a sharp change in lightSensor value
			// is detected, and also if there are sufficient data in our buffer , otherwise it will simply update the average value. 
			if (Math.abs(currentLightValue - averageLightValue) > changeThreshold){
				if (this.totalReadCount > this.bufferSize){
					Sound.beep();
					findNearestCheckpoint();
				}
			
				else {
					this.updateAverageLightValue(currentLightValue);
				}
			}
			
			
			else {
				this.updateAverageLightValue(currentLightValue);
			}
			

			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
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
	
	
	// finds the nearest checkpoint depending on the direction the vehicle is moving and its current coordinates
	private void findNearestCheckpoint(){
		int[] checkpoints = {15,45,75};
		double difference;
		int smallestIndex=1;
		double leastDistance = 999;
		double coordinate = 0;
		
		checkTravellingDirection();
		
		if (travellingInX){		
			coordinate = this.odometer.getX();
		}
		
		if (travellingInY) {
			coordinate = this.odometer.getY();
		}
		
		int newCoord;
		newCoord = (int) (coordinate/10);
		
		
		 
		if (travellingInX){
			this.odometer.setX((int)coordinate);
			//LCD.clear();
			//LCD.drawString("NewX: "+checkpoints[smallestIndex],0,1);
		}
		
		if (travellingInY){
			this.odometer.setY((int)coordinate);
			//LCD.clear();
			//LCD.drawString("NewY: "+checkpoints[smallestIndex],0,1);
		}
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
				
				if ( (this.odometer.getTheta() < 0 + this.angleTolerance && this.odometer.getTheta() > 0 - this.angleTolerance) || 
						(this.odometer.getTheta() < 180 + this.angleTolerance && this.odometer.getTheta() > 180 - this.angleTolerance) ) {
					travellingInY = true;
				}
				
				else {
					travellingInY = false;
				}
	}
	

	
}