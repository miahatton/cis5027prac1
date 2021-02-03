package cis5027.project.clients.fanapp.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import cis5027.project.clients.helpers.AppliancePanel;


/**
 * @author thanu
 * The FanPanel has a timer that periodically updates the angle of the fan blades, i.e. animating the fan.
 * Created by Thanuja Mallikarachchi (Cardiff Metropolitan University)
 */
public class FanPanel extends AppliancePanel implements ActionListener {
	
	private Timer timer;
	
	private final static int DEFAULT_DELAY_TIME = 10;

	/**
	 * Constructor
	 * @param width
	 * @param height
	 */
	public FanPanel(int width, int height) {

		super(width, height, Color.white);
		
		applianceInstance = new Fan(150, 150);
		applianceInstance.setPanel(this);
		
		timer = new Timer(DEFAULT_DELAY_TIME, this); //Update our animations every n ms
		timer.setInitialDelay(DEFAULT_DELAY_TIME * 7);
		timer.start();
	}
	
	/**
	 * Sets the delay of the timer and restarts it
	 * @param delay
	 */
	public void setTimer(int delay) {		
		timer.setDelay(delay);
        timer.setInitialDelay(delay * 10);
		timer.restart();
	}
	
	/**
	 * this function is called, everytime the timer goes off (in this case 3 ms). 
	 * https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		((Fan) applianceInstance).update(); //Let the fan move
		repaint(); //Draw the changes
	}
}