package cis5027.project.clients.lightapp.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import cis5027.project.clients.helpers.Appliance;

/**
 * @author miahatton
 * Light class draws an array of lights whose colour can be adjusted
 */

public class Light extends Appliance {
	
	// maximum brightness is 255, 255, 255
	private static final int 	MAX_BRIGHTNESS = 255;
	private static final Color 	MAX_COLOR = new Color(MAX_BRIGHTNESS, MAX_BRIGHTNESS, MAX_BRIGHTNESS);
	
	// adjustable color value
	private Color 	lightColor;
	private int 	incrementer = (int) 0.1 * MAX_BRIGHTNESS; 
	
	// width and height of panel where lights are drawn
	private int 	panelWidth;
	private int 	panelHeight;
	
	// lights per row and column
	private int 	numLights; 
	
	// padding and gap between lights
	private int 	widthPad;
	private int 	heightPad;
	private int 	gap;
	
	// size of circles representing lights
	private int 	circleSize;
	private int		radius;
	
	/*
	 * Constructor
	 * 
	 * @param width
	 * @param height
	 * @param num
	 */
	public Light(int width, int height, int num) {

		if(num > 0) {
				
			lightColor = MAX_COLOR; 
			panelWidth = width;
			panelHeight = height;
			numLights = num;
				
			widthPad = Math.floorDiv(panelWidth, 20);	// 5% of total width
			heightPad = Math.floorDiv(panelHeight, 20); // 5% of total width
			gap = Math.floorDiv(panelWidth, 100); // 1% of total width
				
			circleSize = Math.floorDiv(panelWidth - 2 * widthPad - (numLights - 1) * gap, numLights);
			radius = (int) circleSize / 2;
				
		}
			
			
	}
	
	/*
	 * Draws the lights
	 */
	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
			
		// Make the centre always 10% brighter than outside, to make the lights look a bit 3D
		int edgeColorVal = (int) Math.max(getCurrentBrightness()  * 0.9, 0);
			
		Color[] colors = {lightColor, new Color(edgeColorVal, edgeColorVal, (int) edgeColorVal/3)}; // introduce a bit of yellow
		float[] dist = {0.0f, 0.8f}; 
				
		int startX;	
		int startY;	
			
		/*
		 * Loop through the number of lights (rows and columns) and draw them.
		 */
		for (int j = 0; j < numLights; j++) {
				
			startY = heightPad + j * (gap + circleSize);
				
			for (int i = 0; i < numLights; i++) {
					
				startX = widthPad + i * (gap + circleSize);
					
				Point2D center = new Point2D.Float(startX + radius, startY + radius);
				RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
				g2d.setPaint(gradient);
				// add colour
				g2d.fillOval(startX, startY, circleSize, circleSize);
			}

		}
		
	}
	
	
	/*
	 * Getters and Setters
	 */
	
	public void setLightColor (int brightnessValue) throws IllegalArgumentException {
		
		int newBrightness = (int) brightnessValue * 255 / 100;
		
		lightColor = new Color(newBrightness, newBrightness, newBrightness);
		panel.repaint();
		
	}

	
	public int getCurrentBrightness() {
		return lightColor.getRed();  // red and green always equal and maximum values in Color() call
	}

	public int getIncrementer() {
		return incrementer;
	}


}