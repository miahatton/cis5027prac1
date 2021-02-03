package cis5027.project.clients.fanapp.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import cis5027.project.clients.helpers.Appliance;

/**
 * @author thanu
 * The Fan class draws the fan that is animated by the FanPanel class.
 * Created by Thanuja Mallikarachchi (Cardiff Metropolitan University)
 */

public class Fan extends Appliance {
	
	private final int triWidth = 20, triHeight = 40; //Wing triangle variables
	private final int arcWidth = 20, arcHeight = 10; //Wing arc variables

	private int centerX, centerY; //The center of the fan
	private double angle; //Angle in radians
		
	private	int fanSpeed;
	
	
	/*
	 * Constructor
	 */
	public Fan(int centerX, int centerY) {

		this.centerX = centerX;
		this.centerY = centerY;
		
	}
	
	/*
	 * Update the angle of the fan blades
	 */
	public void update() {
		angle = addRad(angle, 0.1);
	}
	
	/*
	 * Increase the angle
	 * @param current
	 * @param addition
	 * @returns new value of angle
	 */
	private double addRad(double current, double addition) {
		double value = current + addition;
		
		if (value >= 2 * Math.PI) 
			value -= 2 * Math.PI;
		return value;
	}
	
	/*
	 * Draw the fan
	 */
	public void draw(Graphics g) {
		
		g.setColor(new Color(0, 0, 0));
		
		Graphics2D gx = (Graphics2D) g;
		//Make it look a little prettier
		gx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Setting up triangle.
		//We only want to do the calculations for the top wing. We rotate the top wing to get the other four wings.
		int[] x = { centerX + triWidth / 2, centerX - triWidth / 2, centerX };
		int[] y = { centerY - triHeight, centerY - triHeight, centerY };
		Polygon triangle = new Polygon(x, y, x.length);
		
		//Rotate 'angle' radians around the center
		gx.rotate(angle, centerX, centerY);
		
		for (int i = 0; i < 4; i++) {
			//Draw the wing arc (top)
			//Start with an angle of 0 for starting horizontal left to right. Let the arc have an angle of 180
			gx.fillArc(centerX - triWidth / 2, centerY - triHeight - arcHeight / 2, arcWidth, arcHeight, 0, 180);

			//Draw the wing triangle
			gx.fillPolygon(triangle);
			
			//Rotate on top of the existing rotation. Rotate 45 degrees
			gx.rotate(Math.PI / 2, centerX, centerY);
		}
	}
	
	/*
	 * Getters and setters
	 */
	
	public int getFanSpeed() {
		return fanSpeed;
	}

	public void setFanSpeed(int speed) {
		this.fanSpeed = speed;
		((FanPanel) this.panel).setTimer(this.fanSpeed);
	}
}
