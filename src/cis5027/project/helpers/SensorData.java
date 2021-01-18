package cis5027.project.helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class SensorData implements Serializable {
	
	private ArrayList<Double> temperatures = new ArrayList<Double>();
	private ArrayList<Double> lightLevels = new ArrayList<Double>();
	private double currentTemperature;
	private int currentLightLevel;
	private boolean tempValueSet;
	private boolean lightValueSet;
	
	public SensorData() {
		this.tempValueSet = false;
		this.lightValueSet = false;
	}
		
	public synchronized void setValues(double temp, int lumens) {
		
		while(lightValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception in setValues: " + e.toString());
			}	
		}
		
		this.currentTemperature = temp;
		tempValueSet = true;
		this.currentTemperature = temp;
		lightValueSet = true;
		notify();
		
	}
	
	public void setCurrentTemperature(double temp) {
		this.currentTemperature = temp;
	}
	
	public synchronized double getCurrentTemperature() {
		while(!tempValueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception in getCurrentTemperature: " + e.toString());
			}
		}
		tempValueSet = false;
		notify();
		return this.currentTemperature;
	}
	
	public void setCurrentLightLevel(int lumens) {
		this.currentLightLevel = lumens;
	}
	
	public synchronized int getCurrentLightLevel() {
		while(!lightValueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception in getCurrentTemperature: " + e.toString());
			}
		}
		lightValueSet = false;
		notify();
		return this.currentLightLevel;
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
