package master;
import lejos.nxt.LightSensor;

/**
 * Performs light localization on the robot
 * @author 
 *
 */


public class LightLocalizer {
	private Odometer odometer;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	private LightDetector lightDetector;
	private Navigation nav;
	
	private static final int ROTATION_SPEED = 20;
	private static final double SENSOR_DISTANCE = 19;
	private static final double ANGLE_FILTER = 10;
	
	private double[] angles;
	private int angleIndex;
	
	public LightLocalizer(Odometer odo, LightSensor ls) {
		this.odometer = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		
		angles = new double[4];
		angleIndex = 0;
		
		// turn on the light
		ls.setFloodlight(false);
	}
	
	public void doLocalization() {
		nav = new Navigation(odometer);
		
		lightDetector = new LightDetector(odometer,this);
		lightDetector.start();
		
		
		
		//do a 360 spin and detect 4 grid lines
		while (odometer.getTheta()<=359)
		{
			robot.setRotationSpeed(ROTATION_SPEED);
		}
		
		robot.stop();
		
		//determine angle and distance from origin (0,0)
		double theta = 90 + (angles[3] - angles[1])/2 - angles[3];
		double errorX = SENSOR_DISTANCE * Math.cos(Math.toRadians((angles[3] - angles[1])/2));
		double errorY = SENSOR_DISTANCE * Math.cos(Math.toRadians((angles[2] - angles[0])/2));
		
		//set new coordinates in the odometer 
		odometer.setPosition(new double [] {errorX,errorY,theta}, new boolean [] {true,true,true});
		
		robot.stop();
		//turn light off
		lightDetector.setOn(false);
		
	    //nav.turnTo(0);
		
		//travel to origin
		nav.travelTo(0, 0);
		
		//once at origin, turn to 0 degrees
		nav.turnTo(0);
		
		
		
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		// do trig to compute (0,0) and 0 degrees
		// when done travel to (0,0) and turn to 0 degrees
	}

	
	public void pushAngle(double angle){
		if (angleIndex < 4){
			if (angleIndex>0 && angle - angles[angleIndex-1] > ANGLE_FILTER ){
				this.angles[angleIndex] = angle;
				angleIndex++;
			}
			
			else if (angleIndex == 0){
				this.angles[angleIndex] = angle;
				angleIndex++;
			}
		}
		
		
	}
	
	public double[] getAngles(){
		return this.angles;
	}
	
	private void sleep(int duration){
		try { Thread.sleep(duration); } catch (InterruptedException e) {}
	}
	
	
}
