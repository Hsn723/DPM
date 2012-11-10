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
	private static final double MOTOR_RADIUS = 0.7;
	private LiftLevel currentLiftLevel = LiftLevel.HIGH;
	
	//TODO: fill in the levels with the right height values.
	public enum LiftLevel {
		HIGH (18),
		MIDHIGH (10),
		MIDLOW (6),
		LOW (0);
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
		this.motor.setSpeed(ELEVATION_SPEED);
	}
	
	/**
	 * Rotates the forklift motor in order
	 * to reach the desired height level.
	 * @param level the height level to reach.
	 */
	public void goToHeight(LiftLevel level) {
		this.motor.rotate(convertDistance(level.height - currentLiftLevel.height));
		this.currentLiftLevel = level;
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