package cis5027.project.clients.lightapp.components;

import java.awt.Color;

import cis5027.project.clients.helpers.AppliancePanel;

/**
 * @author miahatton
 * The LightPanel class holds an instance of the Light class.
 */
public class LightPanel extends AppliancePanel {
	
	/**
	 * Constructor
	 * @param width
	 * @param height
	 */
	public LightPanel(int width, int height) {
		
		super(width, height, Color.black);
		applianceInstance = new Light(width, height, 10);
		applianceInstance.setPanel(this);
		
	}

	
}
