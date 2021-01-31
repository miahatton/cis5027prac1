package cis5027.project.server;

import java.io.Serializable;

/**
 * @author miahatton
 * The SensorData class stores data sent by the csv reader for the server to read and send to the clients
 * The methods for getting and setting temperature and light level are synchronized to avoid the clients 
 * reading the values before new ones are set.
 *
 */
public class SensorData implements Serializable {

	private double currentTemperature;
	private int 	currentLightLevel;
	
	// track whether or not the values have been set by the CSV reader
	private boolean tempValueSet;
	private boolean lightValueSet;
	
	/* track whether or not clients are actually connected
	 * (no need to wait for clients to read values if they are not connected) 
	 */ 
	private boolean lightClientConnected;
	private boolean tempClientConnected;
	
	// CsvReader instance
	private AbstractFileReader fileReader;
	
	/**
	 * Constructor
	 * @param csvReader		Link the SensorData object to the CsvReader providing the values
	 */
	public SensorData(AbstractFileReader fileReader) {
		this.tempValueSet = false;
		this.lightValueSet = false;
		this.lightClientConnected = false;
		this.tempClientConnected = false;
		
		this.fileReader = fileReader;
		fileReader.setTarget(this);
	}
	
	/**
	 * Synchronized method to set the temperature
	 * CSV reader waits for server to read value before setting new one
	 * UNLESS no temperature client is connected, in which case it continues setting values with n second delay
	 * @param temp		Temperature value
	 */
	public synchronized void setCurrentTemperature(double temp) {
		while(tempValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				fileReader.updateFeed("[SensorData: ] Interrupted exception in setCurrentTemperature method: " + e.toString(), true);
			}	
		}
		
		this.currentTemperature = temp;
		
		if (tempClientConnected) {
			tempValueSet = true;
			notify();
		}
	}
	
	/**
	 * Synchronized method to get the temperature
	 * Server waits for CSV Reader to set the value before reading again
	 * @returns new temperature value
	 */
	public synchronized double getCurrentTemperature() {

		while(!tempValueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				fileReader.updateFeed("[SensorData: ] Interrupted exception in getCurrentTemperature: " + e.toString(), true);
			}
		}
		tempValueSet = false;
		notify();
		return this.currentTemperature;
	}
	
	/**
	 * Synchronized method to set the light level
	 * CSV reader waits for server to read value before setting new one
	 * UNLESS no light client is connected, in which case it continues setting values with n second delay
	 * @param lumens		light level value
	 */
	public synchronized void setCurrentLightLevel(int lumens) {
		while(lightValueSet) {
			
			try{
				wait();
			} catch (InterruptedException e) {
				fileReader.updateFeed("[SensorData: ] Interrupted exception in setCurrentLightLevel method: " + e.toString(), true);
			}	
		}

		this.currentLightLevel = lumens;
		
		if (lightClientConnected) {
			lightValueSet = true;
			notify();
		}
	}
	
	/**
	 * Synchronized method to get the light level
	 * Server waits for CSV Reader to set the value before reading again
	 * @return new light level
	 */
	public synchronized int getCurrentLightLevel() {
		
		while(!lightValueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				fileReader.updateFeed("[SensorData: ] Interrupted exception in getCurrentTemperature method: " + e.toString(), true);
			}
		}
		lightValueSet = false;
		notify();
		return this.currentLightLevel;
	}

	/**
	 * When a client is connected to the server, the server calls this method to let the SensorData object 
	 * know that the server will be attempting to read values (i.e. the CSV reader should wait after setting)
	 * @param clientType	light or fan
	 */
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
