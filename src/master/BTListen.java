package master;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import bluetooth.BluetoothConnection;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import bluetooth.Transmission;

/**
 * Main class 
 * 
 * Listens to the server and waits for instructions
 * on attacker, defender etc...
 * 
 * Calls attacker or defender class once all data is received.
 * @author  Antoine Tu
 *
 */

public class BTListen {
	private static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private static Odometer odometer = new Odometer(robot, true);
	private static StartCorner corner;
	private static final String remoteNXTName = "Scorpio";
	private static final double TILE_FACTOR = 30.48;
	
	private static SoundSample soundSample;
	//private static UltrasonicSensor ultrasonicSensor;
	// Add one localizer
	//private static USLocalizer ultrasonicLocalizer;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		BluetoothConnection bluetoothConnection = new BluetoothConnection();
		// as of this point the bluetooth connection is closed again, and you can pair to another NXT (or PC) if you wish
		
		// Get the transmission
		Transmission transmission = bluetoothConnection.getTransmission();
		
		// Get the information from transmission
		corner = transmission.startingCorner;	//do something with the corner
		PlayerRole role = transmission.role;
		
		// Flag position (defense) and destination (attack)
		double xFlag = transmission.fx * TILE_FACTOR - 30, yFlag = transmission.fy * TILE_FACTOR - 30;
		double xDest = transmission.dx * TILE_FACTOR, yDest = transmission.dy * TILE_FACTOR;
		
		// Start role
		if (role == PlayerRole.DEFENDER) {
			Defender defender = new Defender(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
			// Once we have localized, update the position.
			odometer.setPosition(getStartingPose(), new boolean[] {true, true, true});
			
			//TODO: reactivate this once we get everything working.
			/*
			Thread thread = new Thread(new Runnable(){
				public void run() {
				soundSample = new SoundSample("Benny Hill");
				soundSample.play();
				}
			});
			thread.start();
			*/

			defender.getBeacon();
			defender.hideBeacon();
		} else if (role == PlayerRole.ATTACKER) {
			
			Attacker attacker = new Attacker(robot, odometer, xFlag, yFlag, xDest, yDest, remoteNXTName);
			// Once we have localized, update the position.
			odometer.setPosition(getStartingPose(), new boolean[] {true, true, true});

			// Sleep for 5 minutes
			Thread timerThread = new Thread(new Runnable() {
				public void run() {
					try { 
						Thread.sleep(5 * 60 * 1000); 
					} catch (InterruptedException e) {
						return;
					}
				}
			});
			timerThread.start();
			
			// Check if the timer should be interrupted.
			int buttonChoice;
			do {
				buttonChoice = Button.waitForPress();
			} while(buttonChoice != Button.ID_LEFT
		&& buttonChoice != Button.ID_RIGHT);
			if (buttonChoice == Button.ID_LEFT || buttonChoice == Button.ID_RIGHT) {
				timerThread.interrupt();
			}
			//Sleep 5 minutes
			//try { Thread.sleep((5*60 + 20) * 1000); } catch (InterruptedException e) {}
			
			/*
			Thread thread = new Thread(new Runnable(){
				public void run() {
				soundSample = new SoundSample("Imperial March");
				soundSample.play();
				}
			});
			thread.start();
			*/
			
			attacker.searchBeacon();
			attacker.depositBeacon();
		}
		
		System.exit(0);
	}
	
	/**
	 * Determine the starting location and heading
	 * of the robot.
	 * @return an array containing the x, y and theta.
	 */
	@SuppressWarnings("static-access")
	private static double[] getStartingPose() {
		double xPos = corner.getX() * TILE_FACTOR;
		double yPos = corner.getY() * TILE_FACTOR;
		double heading = 0;
		if (corner == StartCorner.BOTTOM_LEFT) {
			heading = 0;
		} else if (corner == StartCorner.BOTTOM_RIGHT) {
			heading = -90;
		} else if (corner == StartCorner.TOP_LEFT) {
			heading = 90;
		} else if (corner == StartCorner.TOP_RIGHT) {
			heading = 180;
		}
		
		double[] position = { xPos, yPos, heading };
		return position;
	}
}