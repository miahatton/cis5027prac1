package cis5027.project.clients.lightapp.components;

import java.awt.Color;

import cis5027.project.clients.helpers.AppliancePanel;

public class LightPanel extends AppliancePanel {
	
	
	// constructor
	public LightPanel(int width, int height) {
		
		super(width, height, Color.black);
		applianceInstance = new Light(width, height, 10);
		applianceInstance.setPanel(this);
		
	}

	
}
