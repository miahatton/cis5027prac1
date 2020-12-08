package cis5027.project.csvreader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CsvReader {
	
	private String fileLocation;
	private String split = ",";
	int delay = 0; // For later!
	
	public CsvReader(String fileLocation, String split) {
		this.fileLocation = fileLocation;
		this.split = split;
	}
	
	public CsvReader(String fileLocation, String split, int delay) {
		this.fileLocation = fileLocation;
		this.split = split;
		this.delay = delay;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		if(fileLocation.contains("csv")) {
		
			this.fileLocation = fileLocation; 
		}
	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}
	
	public SensorData readCsv() {
		
		// SensorData object to hold data once read from csv
		SensorData sensorData = new SensorData();
		
		// String variable to hold each line of the csv while reading
		String line;
		
		int i = 0; // FOR TESTING!
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			
			while((line = br.readLine()) != null) {
				
				String[] items = line.split(split);
				
				// REMOVE BELOW THIS -- testing only
				if(i<10) {
					System.out.println("Light level: " + items[7] + "\t Temperature: " + items[9]);
				}
				i++;
				// REMOVE ABOVE -- TESTING ONLY
				
				//TODO include BETTER error handling here for parsing strings to doubles
				try {
					sensorData.addLightLevel(Double.parseDouble(items[7]));
					sensorData.addTemperature(Double.parseDouble(items[9]));
				} catch(NumberFormatException e) {
					continue;
				}
				
				try {
				    TimeUnit.SECONDS.sleep(this.delay);
				    
				    // TEST BELOW
				    if(i<10) {
				    	System.out.println("Sleeping");
				    }
				    // END TEST
				    
				} catch (InterruptedException ie) {
				    Thread.currentThread().interrupt();
				}
			}
			
			br.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		return sensorData;
	}
	

}
