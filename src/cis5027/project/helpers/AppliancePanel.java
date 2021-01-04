package cis5027.project.helpers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class AppliancePanel extends JPanel {

	protected Appliance appInstance;
	protected Color backgroundColor;
	
	public AppliancePanel(int width, int height, Color bgColor) {
		
		setPreferredSize(new Dimension(width, height));
		backgroundColor = bgColor;

	}
	
	public Appliance getAppInstance() {
		
		return this.appInstance;
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		// Set colour and draw appliance
		g.setColor(this.backgroundColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		appInstance.draw(g);

	}
	

	
}
