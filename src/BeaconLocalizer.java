import lejos.nxt.*;
/**
 * This class contains methods which do a
 * search for the beacon and re-orient the
 * NXT according to the detected position
 * of the beacon.
 * 
 * @author Antoine Tu
 *
 */
public class BeaconLocalizer {
	private Odometer odometer;
	//private TwoWheeledRobot robot;
	private LightSensor lightSensor;
	private Navigation navigation;
	//private UltrasonicSensor ultrasonicSensor;
	
	private int brightestLightValue = 0;
	private double lightHeading = 0;
	private static final int BEACON_DISTANCE_THRESHOLD = 20;
	
	/**
	 * Initializes a BeaconLocalizer object.
	 * @param odometer	an Odometer
	 * @param lightSensor	a LightSensor
	 */
	public BeaconLocalizer(Odometer odometer, LightSensor lightSensor) {
		this.odometer = odometer;
		this.navigation = new Navigation(odometer);
		this.lightSensor = lightSensor;
		lightSensor.setFloodlight(true);
	}
	
	/**
	 * TODO
	 */
	public void doSearch() {
		lightSensor.calibrateHigh();
		lightSensor.calibrateLow();
	}
}