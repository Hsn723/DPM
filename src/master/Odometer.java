package master;
import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * Odometer for the robot
 * Handles and calculates x,y and current heading of the robot
 * @author 
 *
 */

public class Odometer implements TimerListener {
	public static final int DEFAULT_PERIOD = 25;
	private TwoWheeledRobot robot;
	private Timer odometerTimer;
	private Navigation nav;
	// position data
	private Object lock;
	private double x, y, theta;
	private double [] oldDH, dDH;
	
	/**
	 * 
	 * @param robot
	 * @param period
	 * @param start
	 */
	public Odometer(TwoWheeledRobot robot, int period, boolean start) {
		// initialise variables
		this.robot = robot;
		this.nav = new Navigation(this);
		odometerTimer = new Timer(period, this);
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		oldDH = new double [2];
		dDH = new double [2];
		lock = new Object();
		
		// start the odometer immediately, if necessary
		if (start)
			odometerTimer.start();
	}
	
	/**
	 * 
	 * @param robot
	 */
	public Odometer(TwoWheeledRobot robot) {
		this(robot, DEFAULT_PERIOD, false);
	}
	
	/**
	 * 
	 * @param robot
	 * @param start
	 */
	public Odometer(TwoWheeledRobot robot, boolean start) {
		this(robot, DEFAULT_PERIOD, start);
	}
	
	
	/**
	 * @param robot our robot
	 * @param period period to update the odometer
	 */
	public Odometer(TwoWheeledRobot robot, int period) {
		this(robot, period, false);
	}
	
	/**
	 * 
	 */
	public void timedOut() {
		robot.getDisplacementAndHeading(dDH);
		dDH[0] -= oldDH[0];
		dDH[1] -= oldDH[1];
		
		// update the position in a critical region
		synchronized (lock) {
			theta += dDH[1];
			theta = fixDegAngle(theta);
			
			x += dDH[0] * Math.sin(Math.toRadians(theta));
			y += dDH[0] * Math.cos(Math.toRadians(theta));
		}
		
		oldDH[0] += dDH[0];
		oldDH[1] += dDH[1];
	}
	
	/**
	 * @param pos array to put the position of the robot in {x,y,theta}
	 */
	public void getPosition(double [] pos) {
		synchronized (lock) {
			pos[0] = -x;
			pos[1] = -y;
			pos[2] = theta;
		}
	}
	
	/**
	 * @return current heading of the robot
	 */
	public double getTheta(){
		synchronized (lock) {
			return theta;
		}
	}
	
	/**
	 * @return our robot
	 */
	public TwoWheeledRobot getTwoWheeledRobot() {
		return robot;
	}
	
	/**
	 * @return Navigation object
	 */
	public Navigation getNavigation() {
		return this.nav;
	}
	
	/**
	 * Update the x,y,theta of our robot
	 * @param pos Holds doubles of the current position of the robot {x,y,theta}
	 * @param update Holds booleans whether to update the given the pos or not {x,y,theta}
	 */
	public void setPosition(double [] pos, boolean [] update) {
		synchronized (lock) {
			if (update[0]) x = pos[0];
			if (update[1]) y = pos[1];
			if (update[2]) theta = pos[2];
		}
	}
	
	
	/**
	 * @param angle negative or over 360
	 * @return a value from 0-360
	 */
	public static double fixDegAngle(double angle) {		
		if (angle < 0.0)
			angle = 360.0 + (angle % 360.0);
		
		return angle % 360.0;
	}
	
	/**
	 * Determine the minimum angle to turn from current heading 
	 * to the desired heading
	 * 
	 * @param a current angle of the robot
	 * @param b desired angle of the robot
	 * @return
	 */
	public static double minimumAngleFromTo(double a, double b) {
		double d = fixDegAngle(b - a);
		
		if (d < 180.0)
			return d;
		else
			return d - 360.0;
	}
	
	/**
	 * get X value of the robot
	 * @return x position in cm
	 */
	public double getX(){
		return this.x;
	}
	
	/**
	 * get Y value of the robot
	 * @return y position in cm
	 */
	public double getY(){
		return this.y;
	}
	
	
}
