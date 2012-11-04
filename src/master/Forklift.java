package master;

import lejos.nxt.remote.*;

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
	
	/**
	 * Initializes a Forklift object using
	 * a remote motor.
	 * @param motor	a RemoteMotor
	 */
	public Forklift(RemoteMotor motor) {
		this.motor = motor;
		this.motor.setSpeed(ELEVATION_SPEED);
	}
	
	/*
	 * TODO: determine distances that the
	 * forklift should ascend/descend in
	 * order to pass these as arguments.
	 */
	public void ascend() {
		this.motor.forward();
	}
	
	public void descend() {
		this.motor.backward();
	}
}