package cis5027.project.csvreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CsvReader {

	private File file;
	private String split;
	int delay; 
	
	// Constructor
	public CsvReader(String fileLocation, String split) {
		
		this.file = new File(fileLocation);
		this.split = split;
		delay = 0; // default
	}
	
	// Overload constructor with ability to choose delay time
	public CsvReader(String fileLocation, String split, int delay) {

		this.file = new File(fileLocation);
		this.split = split;
		this.delay = delay;
	}

	// Getter for file location
	public String getFileLocation() {
		return file.getAbsolutePath();
	}

	// setter for file location
	public void setFileLocation(String fileLocation) {
		if(fileLocation.contains(".csv")) {
		
			//TODO try/catch for validating file location
		
			this.file = new File(fileLocation); 
		}
	}

	// getter for split character
	public String getSplit() {
		return split;
	}

	// setter for split character
	public void setSplit(String split) {
		this.split = split;
	}
	
	private int[] getColumnHeaders(String line){
		
		boolean lightFound = false;
		boolean tempFound = false;
		String errorString = " column header not found. Please ensure csv includes column header: ";
		
		//TODO what if the file has NO column headers? Can we set a default? maybe a try/catch ParseInt - if it works we can return the default values.
		
		int[] lightTempIndices = {-1, -1}; // default in case headers not found
		
		String[] allHeaders = line.split(split); // split first line into array
		
		// loop through all headers on first line
		for (int i = 0; i < allHeaders.length; i++) {
				
			// if the header is light level, its index is stored at index 0 of lightTempIndices.
			if(allHeaders[i].toLowerCase().equals("light level")) {
					
				lightTempIndices[0] = i; // index of light level header
				lightFound = true;
					
				// if the header is temperature, its index is stored at index 1 of lightTempIndices.
			} else if (allHeaders[i].toLowerCase().equals("temperature")) {
					
				lightTempIndices[1] = i; // index of temperature header
				tempFound = true;
					
			}
				
		}			
		
		if(!lightFound) {
			System.out.println("Light level" + errorString);
		}
		
		if(!tempFound) {
			System.out.println("Temperature" + errorString);
		}
		
		return lightTempIndices;
		
	}
	
	private SensorData readLines (BufferedReader br, int[] lightTempIndices) throws IOException {
		
		// SensorData object to hold data once read from csv
		SensorData sensorData = new SensorData();
		// String variable to hold each line of the csv while reading
		String line;
		
		while((line = br.readLine()) != null) {
			
			String[] items = line.split(split);

			try {
				
				sensorData.addLightLevel(Double.parseDouble(items[lightTempIndices[0]]));
				sensorData.addTemperature(Double.parseDouble(items[lightTempIndices[1]]));
				
			} catch(NumberFormatException e) {
				//TODO include BETTER error handling here for parsing strings to doubles
				continue;
			}
			
			try {
			    TimeUnit.SECONDS.sleep(delay);
			    
			} catch (InterruptedException ie) {
				
			    Thread.currentThread().interrupt();
			    
			}
		}
		
		return sensorData;
		
	}
	
	// read csv method that returns data from sensor.
	public SensorData loadCsv() {

		int[] lightTempIndices;
		SensorData sensorData = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line = br.readLine();
			
			if (line != null) { // if file is not empty
				
				// get indexes of relevant column headers (light level, temperature)
				lightTempIndices = getColumnHeaders(line);
			
				//TODO surround this in a while loop resetting the line to 0 each time end of file is reached. 
				//TODO Add something to allow the program to STOP.
				
				sensorData = readLines(br, lightTempIndices);
			
			br.close();
			
			} else {
				
				//TODO improve error handling
				System.out.println("File is empty!");
				
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		return sensorData;
	}
	

}
