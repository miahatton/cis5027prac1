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
	private boolean lightClientConnected;
	private boolean tempClientConnected;
	
	public SensorData() {
		this.tempValueSet = false;
		this.lightValueSet = false;
		this.lightClientConnected = false;
		this.tempClientConnected = false;
	}
	
	public synchronized void setCurrentTemperature(double temp) {
		while(tempValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception in setValues: " + e.toString());
			}	
		}
		
		this.currentTemperature = temp;
		
		if (tempClientConnected) {
			tempValueSet = true;
			notify();
		}
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
		while(lightValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception in setValues: " + e.toString());
			}	
		}

		this.currentLightLevel = lumens;
		
		if (lightClientConnected) {
			lightValueSet = true;
			notify();
		}
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

	public void connectClient(String clientType){
		switch (clientType) {
		
		case "light":
			this.lightClientConnected = true;
			break;
		case "fan":
			this.tempClientConnected = true;
			break;
		}
	}
	
}
