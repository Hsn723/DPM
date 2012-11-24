package master;

import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.robotics.subsumption.Behavior;

public class OdometryCorrentionBehavior implements Behavior {
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
	
	private boolean suppressed = false;

	@Override
	public void action() {
		double coordinate = 0;
		
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

	@Override
	public void suppress() {
		//Not sure where to put suppressed in this code 
		suppressed = true;
	}

	@Override
	public boolean takeControl() {
		checkTravellingDirection();
		return isOverLine() && (travellingInX || travellingInY);
	}
	
	private boolean isOverLine(){	
		currentLightValue = lightSensor.getLightValue();
		totalReadCount++;
		
		// the odometer is corrected to the nearest checkpoint when a sharp change in lightSensor value
		// is detected, and also if there are sufficient data in our buffer , otherwise it will simply update the average value. 
		if (Math.abs(currentLightValue - averageLightValue) > changeThreshold){
			if (this.totalReadCount > this.bufferSize){
				return true;
			}
		
			else {
				updateAverageLightValue(currentLightValue);
				return false;
			}
		}
		else {
			updateAverageLightValue(currentLightValue);
			return false;
		}
	}
	
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
