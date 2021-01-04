package cis5027.project.lightapp.components;

import java.awt.Color;
import java.awt.Graphics;

import cis5027.project.helpers.AppliancePanel;

public class LightPanel extends AppliancePanel {
	
	
	// constructor
	public LightPanel(int width, int height) {
		
		super(width, height, Color.black);
		appInstance = new Light(width, height, 10);
		appInstance.setPanel(this);
		
	}

	
}
