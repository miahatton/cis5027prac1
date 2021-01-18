package cis5027.project.csvreader;

import cis5027.project.helpers.SensorData;

public class Temperature extends SensorData {

	private double currentTemperature;

	public void setCurrentTemperature(double temp) {
		this.currentTemperature = temp;
	}
	
	public double getCurrentTemperature() {
		return currentTemperature;
	}
	
}
