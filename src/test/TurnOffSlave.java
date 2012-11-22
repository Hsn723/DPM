package test;

import master.BTConnector;

public class TurnOffSlave {
	static BTConnector btConnector;
	public static void main(String[] args) {
		btConnector = new BTConnector();
		if(btConnector.doRemoteConnection("Scorpio")) {
			btConnector.startProgram("TurnOff.nxj");
		}
		System.exit(0);
	}
}