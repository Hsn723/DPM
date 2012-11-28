package master;

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
/**
 * This class implements Behavior and defines a set
 * of steps that the robot uses to go around an obstacle
 * when it is detected.
 * @author Lixuan Tang
 *
 */
public class ObstacleAvoidanceBehavior implements Behavior {
	private static final int OBSTACLE_STAND_OFF_DISTANCE = 20;
	private static final int ERROR = 5;
	private static final int MOTOR_STRAIGHT = 100;
	private static final int MOTOR_LOW = MOTOR_STRAIGHT/2+20;
	private static final int MOTOR_HIGH = MOTOR_STRAIGHT*2;
	private static final double THETA_ERROR = 5;
	
	private boolean suppressed = false;
	private double originalTheta = 0;
	private boolean initialized = false;
	
	private boolean obstacleDetected = false;
	
	private UltrasonicSensor ultrasonicSensor = Role.robot.getFrontUltrasonicSensor();
	private UltrasonicSensor sideSensor = Role.robot.getSideUltrasonicSensor();

	@Override
	public void action() {
		
		if (!initialized){
			Role.robot.setMotorSpeed(MOTOR_STRAIGHT, MOTOR_STRAIGHT);
			Role.robot.rotate(90);
			Role.robot.goForward();
			initialized = true;
		}
		long startTime = System.currentTimeMillis();
		while (!suppressed && avoidingInAction() ){
			
			if (System.currentTimeMillis() - startTime > 20000 && Role.beaconDropped){
				System.exit(0);
			}
			int distance = getFilteredData(sideSensor);
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
		initialized = false;
		Sound.beepSequence();
		
		
	}

	@Override
	public void suppress() {
		suppressed = true;
		
	}

	@Override
	public boolean takeControl() {
		
		if (obstacleDetected()){
			obstacleDetected = true;
			originalTheta = Role.odometer.getTheta();
		}
		
		return obstacleDetected && avoidingInAction(); 
	}
	
	private boolean obstacleDetected(){
		return !(Role.originReached && Role.beaconGrabbed) 
				&& getFilteredData(ultrasonicSensor) < OBSTACLE_STAND_OFF_DISTANCE;
	}
	
	@SuppressWarnings("static-access")
	private boolean avoidingInAction(){
		return obstacleDetected 
				&& (Math.abs(Role.odometer.fixDegAngle(originalTheta + 315) - Role.odometer.getTheta()) > THETA_ERROR);
	}
	
	private int getFilteredData(UltrasonicSensor us) {
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
		return distance;
	}

}
