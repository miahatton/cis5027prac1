package cis5027.project.csvreader;

import java.awt.datatransfer.SystemFlavorMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cis5027.project.helpers.SensorData;
import cis5027.project.server.helpers.InputFileReader;

public class CsvReader extends InputFileReader {

	private File file;
	private String split;
	int delay; 
	BufferedReader br;
	int lightIndex;
	int tempIndex;
	boolean fileLoaded;
	
	// Constructor
	public CsvReader(String fileLocation) {
		super(fileLocation, ".csv");
		this.split = ",";
	}
	
	// Overload constructor with ability to choose delay time
	public CsvReader(String fileLocation, int delay) {
		super(fileLocation, ".csv", delay);
		this.split = ",";
	}


	private int[] getColumnHeaders(String line, String split){
		
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
			System.err.println("[csv reader: ] Error: Light level" + errorString);
		}
		
		if(!tempFound) {
			System.err.println("[csv reader: ] Error: Temperature" + errorString);
		}
		
		return lightTempIndices;
		
	}
	
	public void loadFile(boolean fetchHeader) {
			
		try {
			
			br = new BufferedReader(new FileReader(new File(this.fileLocation)));
			
			// read the first line
			String headerRow = br.readLine();
			
			if(headerRow != null) {
				
				if(fetchHeader) { // we can skip this if we already know the index of light and temperature in csv
					
					// get indexes of relevant column headers (light level, temperature)
					int[] lightTempIndices = getColumnHeaders(headerRow, this.split);
					this.lightIndex = lightTempIndices[0];
					this.tempIndex = lightTempIndices[1];
				}
			
			} else System.err.println("[csv reader: ] csv file is empty!");
			
		} catch (IOException e) {
			System.err.println("[csv reader: ] error reading file... " + e.toString());
		}
		
		fileLoaded = true;

	}
	
	
	
	public void readLine(Temperature temp, LightLevel light) {
		
		String nextLine;
		String errorString = " value cannot be converted to ";
		
		try {
			// read the next line
			if ((nextLine = this.br.readLine()) != null) {
				
				String[] items = nextLine.split(split);
				
				try {
					// set current light level
					light.setCurrentLightLevel(Integer.parseInt(items[lightIndex]));
					
				} catch (NumberFormatException e) {
					
					System.err.println("[csv reader: ] Error reading csv... light level " + errorString + "integer.");
				}
				
				try {
					// set current temperature
					temp.setCurrentTemperature(Double.parseDouble(items[tempIndex]));
					
				} catch (NumberFormatException e) {
					
					System.err.println("[csv reader: ] Error reading csv... temperature " + errorString + "double.");
				}
				
			} else {
				// load file again if we've reached the bottom
				loadFile(false);
				// try again
				readLine(temp, light);
			}
			
		} catch (IOException ex) {
			
			System.err.println("[csv reader: ] Error reading file... " + ex.toString());
		}
		
	}
	
	// overload for now
public void readLine(SensorData data) {
		
		String nextLine;
		String errorString = " value cannot be converted to ";
		int lumens = 0;
		double temp = 0;
		
		try {
			// read the next line
			if ((nextLine = this.br.readLine()) != null) {
				
				String[] items = nextLine.split(split);
				
				try {
					// set current light level
					lumens = Integer.parseInt(items[lightIndex]);
					
				} catch (NumberFormatException e) {
					
					System.out.println("[csv reader: ] Error reading csv... light level " + errorString + "integer.");
				}
				
				try {
					// set current temperature
					temp = Double.parseDouble(items[tempIndex]);
					
				} catch (NumberFormatException e) {
					
					System.out.println("[csv reader: ] Error reading csv... temperature " + errorString + "double.");
				}
				
				data.setValues(temp, lumens);
				
			} else {
				// load file again if we've reached the bottom
				loadFile(false);
				// try again
				readLine(data);
			}
			
		} catch (IOException ex) {
			
			System.err.println("[csv reader: ] Error reading file... " + ex.toString());
		}
		
	}
	
	public boolean getFileLoaded() {
		
		return this.fileLoaded;
	}
	
	public void closeBuffer() {
		
		try {
			this.br.close();
		} catch (IOException e) {
			System.err.println("[csv reader: ] error closing BufferedReader..." + e.toString());
		}
		
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
				lightTempIndices = getColumnHeaders(line, this.split);
			
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
