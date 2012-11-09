package master;
import lejos.nxt.remote.RemoteMotor;

/**
 * This class defines a Clamp object and a
 * set of methods allowing the clamp to grab
 * onto or release an object.
 * 
 * @author Lixuan Tang
 * @author Antoine Tu
 *
 */
public class Clamp {
	private RemoteMotor motor;	// Modified to use a remote motor
	
	//private static final int ROTATE_ANGLE = 30;
	
	/**
	 * Initializes a Clamp object.
	 * @param motor	a RemoteMotor
	 */
	public Clamp(RemoteMotor motor){
		this.motor = motor;
		this.motor.setSpeed(900);
	}
	
	/**
	 * Moves the Clamp in order to allow it
	 * to grasp onto an object.
	 */
	public void grip(){
		this.motor.backward();
	}
	
	/**
	 * Moves the Clamp in order to allow it
	 * to release an object.
	 */
	public void release(){
		this.motor.forward();
	}
}
