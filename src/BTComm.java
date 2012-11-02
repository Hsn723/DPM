import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.comm.*;
import lejos.nxt.remote.*;

public class BTComm {
	private static RemoteNXT slaveNXT;
	public static void main(String[] args) {
		try {
			slaveNXT = new RemoteNXT("Scorpio", Bluetooth.getConnector());
			//slaveNXT.startProgram("driver.nxj");
			slaveNXT.B.rotate(90);
		} catch (IOException e) {}
		Button.waitForPress();
	}
}