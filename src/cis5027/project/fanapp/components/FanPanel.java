package cis5027.project.fanapp.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import cis5027.project.helpers.AppliancePanel;


public class FanPanel extends AppliancePanel implements ActionListener {
	
	Timer timer;
	
	private final static int DEFAULT_DELAY_TIME = 10;

	public FanPanel(int width, int height) {

		super(width, height, Color.white);
		
		appInstance = new Fan(150, 150);
		appInstance.setPanel(this);
		
		timer = new Timer(DEFAULT_DELAY_TIME, this); //Update our animations every n ms
		timer.setInitialDelay(DEFAULT_DELAY_TIME * 7);
		timer.start();
	}
	
	
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
		((Fan) appInstance).update(); //Let the fan move
		repaint(); //Draw the changes
	}
}