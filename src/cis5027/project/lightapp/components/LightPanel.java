package cis5027.project.lightapp.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class LightPanel extends JPanel {
	
	private Light lightInstance;
	
	// constructor
	public LightPanel(int width, int height) {
		
		setPreferredSize(new Dimension(width, height));
		lightInstance = new Light(width, height, 10);
		lightInstance.setLightPanel(this);
		
	}
	

	@Override
	public void paintComponent(Graphics g) {
		
		// draw background in black
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// add lights
		lightInstance.draw(g);
		
	}
	
	// getter for light instance
	public Light getLightInstance() {
		return this.lightInstance;
	}
	
}
