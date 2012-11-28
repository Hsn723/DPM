package master;

import lejos.nxt.remote.RemoteMotor;

/**
 * This class defines a Forklift object along with
 * methods that control it's movement.
 * 
 * @author Lixuan Tang
 * @author Antoine Tu
 *
 */
public class Forklift {
	private RemoteMotor motor;
	private static final int ELEVATION_SPEED = 100;
	private static final double MOTOR_RADIUS = 0.8;
	private LiftLevel currentLiftLevel;
	
	public enum LiftLevel {
		HIGH (0d),
		MIDHIGH (-6d),
		MIDLOW (-10d),
		LOW (-16d);
		private final double height;

		LiftLevel(double height) {
			this.height = height;
		}
	}
	
	/**
	 * Initializes a Forklift object using
	 * a remote motor.
	 * @param motor	a RemoteMotor
	 */
	public Forklift(RemoteMotor motor) {
		this.motor = motor;
		this.motor.resetTachoCount();
		this.motor.setSpeed(ELEVATION_SPEED);
		currentLiftLevel = LiftLevel.LOW;
	}
	
	/**
	 * Rotates the forklift motor in order
	 * to reach the desired height level.
	 * @param level the height level to reach.
	 */
	public void goToHeight(LiftLevel level) {
		if (level == currentLiftLevel) return;
		
		
		try {
			this.motor.rotate(convertDistance(level.height - currentLiftLevel.height));
			motor.stop();
		} catch (Exception e) {
			this.currentLiftLevel = level;
			return;
		}
		
		this.currentLiftLevel = level;
			
		
		
		
		/*int distance = motor.getTachoCount();
		int target = convertDistance(level.height - currentLiftLevel.height);
		if (target >= 0) {
			do {
				motor.forward();
			} while (distance < target);
		} else {
			do {
				motor.backward();
			} while (distance > target);
		}
		motor.stop();*/
		
	}
	
	/**
	 * Ascends the forklift by the specified distance.
	 * @param distance the elevation distance.
	 * @deprecated
	 */
	public void ascend(double distance) {
		this.motor.rotate(convertDistance(distance));
	}
	
	/**
	 * Descends the forklift by the specified distance.
	 * @param distance the distance to descend.
	 * @deprecated
	 */
	public void desccend(double distance) {
		this.motor.rotate(-convertDistance(distance));
	}
	
	/**
	 * Ascends the forklift.
	 * @deprecated
	 */
	public void ascend() {
		this.motor.forward();
	}
	
	/**
	 * Descends the forklift.
	 * @deprecated
	 */
	public void descend() {
		this.motor.backward();
	}
	
	
	/**
	 * Helper method to convert a distance
	 * into an angle to which the motor turns.
	 * @param distance
	 * @return
	 */
	private static int convertDistance(double distance) {
		return (int) ((180.0 * distance) / (Math.PI * MOTOR_RADIUS));
	}
}