package cis5027.project.clients.helpers;

import java.awt.Graphics;

/**
 * @author miahatton
 * Abstract class that is extended by Light and Fan classes
 */

public abstract class Appliance {

	// the Appliance will be displayed on a panel
	public AppliancePanel panel;

	/*
	 * Setter for panel
	 */
	public void setPanel(AppliancePanel panel) {
		
		this.panel = panel;
		
	}
	
	public abstract void draw(Graphics g);
}
