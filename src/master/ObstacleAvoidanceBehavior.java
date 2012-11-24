package master;

import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public class ObstacleAvoidanceBehavior implements Behavior {
	private static final int OBSTACLE_STAND_OFF_DISTANCE = 20;
	private static final int ERROR = 5;
	private static final int MOTOR_STRAIGHT = 100;
	private static final int MOTOR_LOW = MOTOR_STRAIGHT/2 - 25;
	private static final int MOTOR_HIGH = MOTOR_STRAIGHT * 3 / 2;
	private static final double THETA_ERROR = 2;
	
	private boolean suppressed = false;
	private double originalTheta = 0;
	private boolean initialized = false;
	
	private boolean obstacleDetected = false;
	
	

	@Override
	public void action() {
		
		if (!initialized){
			Role.robot.setMotorSpeed(MOTOR_STRAIGHT, MOTOR_STRAIGHT);
			Role.robot.rotate(90);
			Role.robot.goForward();
			initialized = true;
		}
		
		while (!suppressed && avoidingInAction() ){
			int distance = Role.robot.getSideUltrasonicSensor().getDistance();
			if (distance - OBSTACLE_STAND_OFF_DISTANCE > ERROR){
				Role.robot.setMotorSpeed(MOTOR_HIGH, MOTOR_LOW);
			}
			
			else if (distance - OBSTACLE_STAND_OFF_DISTANCE < -1 * ERROR ){
				Role.robot.setMotorSpeed(MOTOR_LOW, MOTOR_HIGH);
			}
			
			else {
				Role.robot.setMotorSpeed(MOTOR_STRAIGHT, MOTOR_STRAIGHT);
			}
			
		}
		
		obstacleDetected = false;
		Sound.beepSequence();
		
		
	}

	@Override
	public void suppress() {
		suppressed = true;
		
	}

	@Override
	public boolean takeControl() {
		
		if (obstacleDetected()) obstacleDetected = true;
		
		return obstacleDetected && avoidingInAction(); 
	}
	
	private boolean obstacleDetected(){
		return !Role.beaconGrabbed && !Role.beaconReached 
				&& Role.robot.getFrontUltrasonicSensor().getDistance() < OBSTACLE_STAND_OFF_DISTANCE;
	}
	
	private boolean avoidingInAction(){
		return obstacleDetected && (Math.abs(originalTheta + 270 - Role.odometer.getTheta()) > THETA_ERROR);
	}

}
