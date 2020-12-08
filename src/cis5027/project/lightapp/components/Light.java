package cis5027.project.lightapp.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import cis5027.project.lightapp.components.LightPanel;

import java.awt.RenderingHints;

public class Light {

	private final int circDiam = 20; // circle diameter

	private int centreX, centreY; //The centre of the light array
		
	private	int lightBrightness;
	private LightPanel lightPanel;

	public void setFanJPanel(LightPanel lightJPanel) {
		this.lightPanel = lightJPanel;
	}
	
	public int getLightBrightness() {
		return lightBrightness;
	}
	
	public void setLightBrightness(int brightness) {
		this.lightBrightness = brightness;
		// this.lightPanel.setTimer(this.fanSpeed); NOT SURE IF NECESSARY
	}
	
	public Light(int centreX, int centreY) {
		this.centreX = centreX;
		this.centreY = centreY;
	}

	public void draw(Graphics g) {
		Graphics2D gx = (Graphics2D) g;
		//Make it look a little prettier
		// gx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // GO without rendering hints for now
		
		Ellipse2D.Double circle = new Ellipse2D.Double(this.centreX, this.centreY, circDiam, circDiam);

		gx.fill(circle);
		
		/* COME BACK TO THIS LATER - WOULD BE NICE TO HAVE ARRAY OF LIGHTS BUT WHATEVER
		for (int i = 0; i < 4; i++) {
			//Draw the wing arc (top)
			//Start with an angle of 0 for starting horizontal left to right. Let the arc have an angle of 180
			gx.fillArc(centerX - triWidth / 2, centerY - triHeight - arcHeight / 2, arcWidth, arcHeight, 0, 180);

			//Draw the wing triangle
			gx.fillPolygon(triangle);
			
			//Rotate on top of the existing rotation. Rotate 45 degrees
			gx.rotate(Math.PI / 2, centerX, centerY);
		} 
		*/
	}
}
