package cis5027.project.csvreader;

public class Temperature extends SensorData {

	private double currentTemperature;

	public void setCurrentTemperature(double temp) {
		this.currentTemperature = temp;
	}
	
	public double getCurrentTemperature() {
		return currentTemperature;
	}
	
}
