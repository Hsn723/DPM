package test;

import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import master.Odometer;
import master.Role;

/**
 * Class to print odometer information to the lcd of our master brick.
 * @author 
 *
 */
public class LCDInfo implements TimerListener{
	public static final int LCD_REFRESH = 100;
	private Odometer odo;
	private Timer lcdTimer;
	
	// arrays for displaying data
	private double [] pos;
	
	public LCDInfo(Odometer odo) {
		this.odo = odo;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		
		// initialise the arrays for displaying data
		pos = new double [3];
		
		// start the timer
		lcdTimer.start();
	}
	
	public LCDInfo(){
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		lcdTimer.start();
	}
	
	/**
	 * Print odometer information to the screen
	 * Multiplies odometer values by 10 for accuracy 
	 * reasons.
	 */
	/*
	public void timedOut() { 
		odo.getPosition(pos);
		LCD.clear();
		LCD.drawString("X: ", 0, 0);
		LCD.drawString("Y: ", 0, 1);
		LCD.drawString("H: ", 0, 2);
		LCD.drawInt((int)(pos[0] * 10), 3, 0);
		LCD.drawInt((int)(pos[1] * 10), 3, 1);
		LCD.drawInt((int)pos[2], 3, 2);
		
	}
	*/
	
	public void timedOut(){
		LCD.clear();
		LCD.drawString("detected:" + Role.beaconDetected, 0, 0);
		LCD.drawString("reached:" + Role.beaconReached, 0, 1);
		LCD.drawString("grabbed:" + Role.beaconGrabbed, 0, 2);
	}
}
