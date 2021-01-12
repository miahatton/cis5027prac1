package cis5027.project.csvreader;

public class LightLevel extends SensorData {

	private int currentLightLevel;
	
	public void setCurrentLightLevel(int lumens) {
		this.currentLightLevel = lumens;
	}
	
	public int getCurrentLightLevel() {
		return currentLightLevel;
	}
	
}
