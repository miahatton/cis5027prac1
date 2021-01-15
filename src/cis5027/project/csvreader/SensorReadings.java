package cis5027.project.csvreader;

public class SensorReadings {

	private int currentLightLevel;
	private double currentTemperature;
	
	public void setCurrentLightLevel(int lumens) {
		this.currentLightLevel = lumens;
	}
	
	public int getCurrentLightLevel() {
		return currentLightLevel;
	}

	public void setCurrentTemperature(double temp) {
		this.currentTemperature = temp;
	}
	
	public double getCurrentTemperature() {
		return currentTemperature;
	}
	
}
