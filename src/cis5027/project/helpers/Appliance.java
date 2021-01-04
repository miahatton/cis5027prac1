package cis5027.project.helpers;

import java.awt.Graphics;

public abstract class Appliance {

	public AppliancePanel panel;
	
	
	
	public void setPanel(AppliancePanel panel) {
		
		this.panel = panel;
		
	}
	
	public abstract void draw(Graphics g);
	
	// I think we'll need methods for responding to input here.
}
