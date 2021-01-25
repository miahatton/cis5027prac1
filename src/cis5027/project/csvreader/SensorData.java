package cis5027.project.csvreader;

import java.io.Serializable;

public class SensorData implements Serializable {

	private double currentTemperature;
	private int currentLightLevel;
	private boolean tempValueSet;
	private boolean lightValueSet;
	private boolean lightClientConnected;
	private boolean tempClientConnected;
	private CsvReader csvReader;
	
	public SensorData(CsvReader csvReader) {
		this.tempValueSet = false;
		this.lightValueSet = false;
		this.lightClientConnected = false;
		this.tempClientConnected = false;
		
		this.csvReader = csvReader;
	}
	
	public synchronized void setCurrentTemperature(double temp) {
		while(tempValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				csvReader.updateFeed("[SensorData: ] Interrupted exception in setCurrentTemperature method: " + e.toString(), true);
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
				csvReader.updateFeed("[SensorData: ] Interrupted exception in getCurrentTemperature: " + e.toString(), true);
			}
		}
		tempValueSet = false;
		notify();
		return this.currentTemperature;
	}
	
	public synchronized void setCurrentLightLevel(int lumens) {
		while(lightValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				csvReader.updateFeed("[SensorData: ] Interrupted exception in setCurrentLightLevel method: " + e.toString(), true);
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
				csvReader.updateFeed("[SensorData: ] Interrupted exception in getCurrentTemperature method: " + e.toString(), true);
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
