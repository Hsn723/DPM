package master;
import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.nxt.remote.*;

/**
 * This class contains methods that allow to
 * access a remote/slave NXT and gets its various
 * motors and sensors. This way, they can be used
 * in other methods with zero or minimal additional
 * setup.
 * 
 * @author Antoine Tu
 *
 */
public class BTConnector {
	private static RemoteNXT slaveNXT;

	/**
	 * Attempt to connect to a specified remote NXT.
	 * 
	 * @param name	the name of the remote NXT
	 * @return	returns successful connection state
	 */
	public boolean doRemoteConnection(String name) {
		try {
			slaveNXT = new RemoteNXT(name, Bluetooth.getConnector());
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Gets the LightSensor on the remote NXT.
	 * This sensor can then be accessed the same way
	 * as if it was local to our master NXT.
	 * @return	a LightSensor object
	 */
	public LightSensor getRemoteLightSensor() {
		return new LightSensor(slaveNXT.S2);
	}
	
	/**
	 * Gets the Forklift motor of the remote NXT.
	 * This motor can then be controlled the same way
	 * as if it was local to our master NXT.
	 * @return	a RemoteMotor object
	 */
	public RemoteMotor getRemoteForklift() {
		return slaveNXT.B;
	}
	
	/**
	 * Gets the UltrasonicSensor on the remote NXT.
	 * This sensor can then be accessed the same way
	 * as if it was local to our master NXT.
	 * @return	an UltrasonicSensor object
	 */
	public UltrasonicSensor getRemoteUltrasonicSensor() {
		return new UltrasonicSensor(slaveNXT.S1);
	}
	
	/**
	 * Gets the Clamp motor of the remote NXT.
	 * This motor cna then be controlled the same way
	 * as if it was local to our master NXT.
	 * @return	a RemoteMotor object
	 */
	public RemoteMotor getRemoteClamp() {
		return slaveNXT.A;
	}
}