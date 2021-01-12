package cis5027.project.csvreader;

import java.util.ArrayList;

public class SensorData {
	
	private ArrayList<Double> temperatures = new ArrayList<Double>();
	private ArrayList<Double> lightLevels = new ArrayList<Double>();
	private double currentTemperature;
	private int currentLightLevel;
	
	public void setCurrentTemperature(double temp) {
		this.currentTemperature = temp;
	}
	
	public double getCurrentTemperature() {
		return currentTemperature;
	}
	
	public void setCurrentLightLevel(int lumens) {
		this.currentLightLevel = lumens;
	}
	
	public int getCurrentLightLevel() {
		return currentLightLevel;
	}
	
	public SensorData(){
		
	}
	
	public void addLightLevel(double lightLevel) {
		this.lightLevels.add(lightLevel);
	}
	
	public void addTemperature(double temperature) {
		this.temperatures.add(temperature);
	}

	public ArrayList<Double> getTemperatures() {
		return temperatures;
	}

	public void setTemperatures(ArrayList<Double> temperatures) {
		this.temperatures = temperatures;
	}

	public ArrayList<Double> getLightLevels() {
		return lightLevels;
	}

	public void setLightLevels(ArrayList<Double> lightLevels) {
		this.lightLevels = lightLevels;
	}
	
}
