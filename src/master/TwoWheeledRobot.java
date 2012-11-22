package master;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/**
 * Holds all the information about our robot
 * and the algorithms needed to convert high level distance
 * into lower level wheel rotations and motor controls
 * @author 
 *
 */
public class TwoWheeledRobot {
	
	// Sensor ports belonging to the master
	private static LightSensor masterLightSensor = new LightSensor(SensorPort.S1);
	private UltrasonicSensor sideUltrasonicSensor = new UltrasonicSensor(SensorPort.S2);
	private UltrasonicSensor frontUltrasonicSensor = new UltrasonicSensor(SensorPort.S3);
	
	public static final double DEFAULT_LEFT_RADIUS = 2.05;	//2.75
	public static final double DEFAULT_RIGHT_RADIUS = 2.05;	//2.75
	public static final double DEFAULT_WIDTH = 18.48;	//15.8	//21.40
	
	// Wheel constants when the robot has a load.
	public static final double LOAD_LEFT_RADIUS = 0;
	public static final double LOAD_RIGHT_RADIUS = 0;
	public static final double LOAD_WIDTH = 0;
	
	private NXTRegulatedMotor leftMotor, rightMotor;
	private double leftRadius, rightRadius, width;
	private double forwardSpeed, rotationSpeed;
	private boolean isTurning = false;
	
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor,
						   NXTRegulatedMotor rightMotor,
						   double width,
						   double leftRadius,
						   double rightRadius) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.leftRadius = leftRadius;
		this.rightRadius = rightRadius;
		this.width = width;
		this.leftMotor.setAcceleration(150);
		this.rightMotor.setAcceleration(150);
	}

	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this(leftMotor, rightMotor, DEFAULT_WIDTH, DEFAULT_LEFT_RADIUS, DEFAULT_RIGHT_RADIUS);
	}
	
	public TwoWheeledRobot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, double width) {
		this(leftMotor, rightMotor, width, DEFAULT_LEFT_RADIUS, DEFAULT_RIGHT_RADIUS);
	}
	
	/**
	 * @return the displacement of the robot from the start of movement
	 */
	public double getDisplacement() {
		return (leftMotor.getTachoCount() * leftRadius +
				rightMotor.getTachoCount() * rightRadius) *
				Math.PI / 360.0;
	}
	
	/**
	 * @return current heading of the robot using the tacho count of both motors
	 */
	public double getHeading() {
		return (leftMotor.getTachoCount() * leftRadius -
				rightMotor.getTachoCount() * rightRadius) / width;
	}
	
	/**
	 * A combination of the getDisplacement and getHeading methods
	 * @param data an array that will store the data {displacement, heading}
	 */
	public void getDisplacementAndHeading(double [] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();
		
		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) *	Math.PI / 360.0;
		data[1] = (leftTacho * leftRadius - rightTacho * rightRadius) / width;
	}
	
	/**
	 * Set the speed of the robot when moving forward
	 * @param speed in degrees per second
	 */
	public void setForwardSpeed(double speed) {
		forwardSpeed = speed;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	
	/**
	 * Set the speed of the robot when turning
	 * @param speed in degrees per second
	 */
	public void setRotationSpeed(double speed) {
		rotationSpeed = speed;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	
	/**
	 * Combination of setForwardSpeed and setRotationSpeed
	 * @param forwardSpeed speed in degrees per second
	 * @param rotationalSpeed speed in degrees per second
	 */
	public void setSpeeds(double forwardSpeed, double rotationalSpeed) {
		double leftSpeed, rightSpeed;

		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationalSpeed; 

		leftSpeed = (forwardSpeed + rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (leftRadius * Math.PI);
		rightSpeed = (forwardSpeed - rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (rightRadius * Math.PI);

		// set motor directions
		if (leftSpeed > 0.0)
			leftMotor.forward();
		else {
			leftMotor.backward();
			leftSpeed = -leftSpeed;
		}
		
		if (rightSpeed > 0.0)
			rightMotor.forward();
		else {
			rightMotor.backward();
			rightSpeed = -rightSpeed;
		}
		
		// set motor speeds
		if (leftSpeed > 900.0)
			leftMotor.setSpeed(900);
		else
			leftMotor.setSpeed((int)leftSpeed);
		
		if (rightSpeed > 900.0)
			rightMotor.setSpeed(900);
		else
			rightMotor.setSpeed((int)rightSpeed);
	}
	
	public void setMotorSpeed(double leftSpeed, double rightSpeed){
		leftMotor.setSpeed( (int)leftSpeed);
		rightMotor.setSpeed( (int)rightSpeed);
	}
	
	/**
	 * Rotate the robot (with blocking)
	 * @param angle in degrees
	 */
	public void rotate(int angle)
	{
		leftMotor.rotate(convertAngle(DEFAULT_LEFT_RADIUS, DEFAULT_WIDTH, angle), true);
		rightMotor.rotate(-convertAngle(DEFAULT_RIGHT_RADIUS, DEFAULT_WIDTH, angle));
	}
	/**
	 * Rotate the robot without blocking
	 * @param angle in degrees
	 */
	public void rotateIndependently(int angle)
	{
		leftMotor.rotate(convertAngle(DEFAULT_LEFT_RADIUS, DEFAULT_WIDTH, angle), true);
		rightMotor.rotate(-convertAngle(DEFAULT_RIGHT_RADIUS, DEFAULT_WIDTH, angle), true);
	}
	
	/**
	 * Moves the robot forward a given distance
	 * @param distance in cm
	 */
	public void goForward(double distance)
	{
		leftMotor.rotate(-convertDistance(DEFAULT_LEFT_RADIUS, distance), true);
		rightMotor.rotate(-convertDistance(DEFAULT_RIGHT_RADIUS,distance));
	}
	
	/**
	 * Moves the robot forward a given distance
	 * @param distance in cm
	 */
	public void goForwardIndependently(double distance)
	{
		leftMotor.rotate(-convertDistance(DEFAULT_LEFT_RADIUS, distance), true);
		rightMotor.rotate(-convertDistance(DEFAULT_RIGHT_RADIUS,distance), true);
	}
	
	
	/**
	 * Moves the robot forward until told to stop
	 */
	public void goForward()
	{
		leftMotor.backward();
		rightMotor.backward();
	}
	
	/**
	 * Stops any kind of motor rotations instantly
	 */
	public void stop(){
		leftMotor.stop(true); //don't wait for engine to stop
		rightMotor.stop(); //this makes sure both motors stop at the same time
	}
	
	/**
	 * Moves the robot backwards (direction of the clamp)
	 */
	public void start(){
		leftMotor.forward();
		rightMotor.forward();
	}
	
	/**
	 * Coverts distance into number of degrees needed for the motor to turn
	 * @param radius of the wheel in cm
	 * @param distance needed to travel
	 * @return
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	/**
	 * Convert angle into number of degrees needed for each motor to turn
	 * @param radius of the wheels in cm
	 * @param width distance between both wheels
	 * @param angle angle needed to turn in degrees
	 * @return
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	/**
	 * @return boolean of if the robot is turning or not
	 */
	public boolean isTurning(){
		return this.isTurning;
	}

	/**
	 * Get the front Ultrasonic Sensor.
	 * @return an UltrasonicSensor object.
	 */
	public UltrasonicSensor getFrontUltrasonicSensor() {
		return frontUltrasonicSensor;
	}
	
	/**
	 * Get the side Ultrasonic Sensor.
	 * @return an UltrasonicSensor object.
	 */
	public UltrasonicSensor getSideUltrasonicSensor() {
		return sideUltrasonicSensor;
	}
	
	/**
	 * Get the Light Sensor of the master.
	 * @return an LightSensor object.
	 */
	public static LightSensor getMasterLightSensor() {
		return masterLightSensor;
	}
}
