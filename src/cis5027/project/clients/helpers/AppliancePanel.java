package cis5027.project.clients.helpers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class AppliancePanel extends JPanel {

	protected Appliance applianceInstance;
	protected Color backgroundColor;
	
	public AppliancePanel(int width, int height, Color bgColor) {
		
		setPreferredSize(new Dimension(width, height));
		backgroundColor = bgColor;

	}
	
	public Appliance getApplianceInstance() {
		
		return this.applianceInstance;
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		// Set colour and draw appliance
		g.setColor(this.backgroundColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		applianceInstance.draw(g);

	}
	

	
}
