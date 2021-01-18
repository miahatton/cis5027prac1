package cis5027.project.csvreader;

import cis5027.project.helpers.SensorData;

public class LightLevel extends SensorData {

	private int currentLightLevel;
	
	public void setCurrentLightLevel(int lumens) {
		this.currentLightLevel = lumens;
	}
	
	public int getCurrentLightLevel() {
		return currentLightLevel;
	}
	
}
