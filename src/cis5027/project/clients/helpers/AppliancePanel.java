package cis5027.project.clients.helpers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author miahatton
 * Abstract class extended by FanPanel and LightPanel class.
 * It's a JPanel that displays an instance of an Appliance subclass.
 */
public class AppliancePanel extends JPanel {

	protected Appliance applianceInstance;
	protected Color backgroundColor;
	
	/**
	 * Constructor
	 * @param width
	 * @param height
	 * @param bgColor		[background colour]
	 */
	public AppliancePanel(int width, int height, Color bgColor) {
		
		setPreferredSize(new Dimension(width, height));
		backgroundColor = bgColor;

	}
	
	/**
	 * Getter for appliance instance
	 * @return Appliance instance
	 */
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
