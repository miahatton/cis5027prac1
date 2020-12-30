package cis5027.project.lightapp.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;

import cis5027.project.lightapp.components.LightPanel;



public class Light {

	private static final Color MAX_BRIGHTNESS = new Color(253, 255, 176);
	private Color lightColor;
	private int arrayWidth;
	private int arrayHeight;
	private int numLights;
	
	// constructor
	public Light(int width, int height, int num) {
		
		lightColor = MAX_BRIGHTNESS; // yellow for warmth
		arrayWidth = width;
		arrayHeight = height;
		
		// numLights must be square
		if (Math.sqrt(num) - Math.floor(Math.sqrt(num)) == 0) {
			numLights = num;
		} else {
			System.out.println("Number of lights must be square");
		}
		
	}
	
	public void incrementLightColor(boolean dim) {
		
		double incrementer = 0.1 * 5; // TODO make this MAX_BRIGHTNESS - for each colour value
		
		if(dim) {
			
			incrementer *= -1; // make light dimmer
		}
		
		// adjust lightColor;
	}
	
	// draw method
	public void draw(Graphics g) {

		// TODO find way to do gradient from centre to outside. Make the centre always 10% brighter than outside
		// GradientPaint gradient = new GradientPaint(70,70, brightLight, 150, 150, boldLight);
		// g2d.setPaint(gradient);

		g.setColor(lightColor);
		
		int widthPad = Math.floorDiv(arrayWidth, 20);	// 20
		int heightPad = Math.floorDiv(arrayHeight, 20); // 20
		int gap = 10;
		
		int circleSize = Math.floorDiv(arrayWidth - 2 * widthPad - 2 * gap, numLights); // 113
		
		int startX = widthPad;	// 20	
		int startY = heightPad;	//20
		
		for (int j = 0; j < numLights; j++) {
			
			startY = heightPad + j * (gap + circleSize);
			
			for (int i = 0; i < numLights; i++) {
				
				startX = widthPad + i * (gap + circleSize);
				System.out.println("Drawing oval at (" + startX + ", " + startY + ")");
				g.fillOval(startX, startY, circleSize, circleSize);
			}

		}
		
		
	}

	
}
