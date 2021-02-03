package cis5027.project.server;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cis5027.project.server.helpers.AbstractFileReader;
import cis5027.project.server.helpers.CsvHeaderException;

/**
 * @author miahatton
 * The CSV reader class handles the input file. 
 * It loads the given CSV file and reads it line by line with the given delay
 * After each line is read the temperature and light values are stored in a SensorData object.
 * The CSV reader runs in a separate thread to free up the buttons on the Server App.
 */

public class CsvReader extends AbstractFileReader {
	
	private 		int 			lightIndex;
	private 		int 			tempIndex;
	private 		boolean 		stopThread;
	public			ServerApp 		app;

	/**
	 * Constructor
	 * @param app
	 * @param fileLocation
	 */
	public CsvReader(ServerApp app, String fileLocation) {
		super(fileLocation, ".csv", "CSV", ",");
		this.app = app;
		feedText = "";
	}
	
	/**
	 * Overload constructor with ability to choose delay time
	 * @param app
	 * @param fileLocation
	 * @param delay
	 */
	public CsvReader(ServerApp app, String fileLocation, int delay) {
		super(fileLocation, ".csv", "CSV", ",", delay);
		this.app = app;
	}


	/**
	 * Find the column number where the light and temperature data is stored (in case a different csv is passed or the csv is modified)
	 * @param line - a line of the csv
	 * @param split - the delimiter
	 */
	private int[] getColumnHeaders(String line, String split) throws CsvHeaderException {
		
		boolean lightFound = false;
		boolean tempFound = false;
		
		
		int[] lightTempIndices = {-1, -1}; 			// default in case headers not found
		String[] allHeaders = line.split(split); 	// split first line into array
		
		if (allHeaders.length > 1) { // if there actually is a comma in the first line!
			
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
		}
		
		
		if(!lightFound & !tempFound) {
			throw new CsvHeaderException("light and temperature");
		}
		
		else if(!tempFound) {
			throw new CsvHeaderException("temperature");
		}
		
		else if(!lightFound) {
			throw new CsvHeaderException("light");
		}
		
		return lightTempIndices;
		
	}
	
	
	/**
	 * Prepares to read CSV file line by line.
	 * @param fetchHeader - boolean value, true if we need to get the indices of the light and temperature columns
	 */
	@Override
	public void loadFile (boolean fetchHeader) {
		
		String errorType = "File format error";
			
		try {
			
			// initialise buffered reader
			br = new BufferedReader(new FileReader(this.file));
			
			// read the first line
			String headerRow = br.readLine();
			
			// only continue if first line is not empty
			if(headerRow != null) {
				
				if(fetchHeader) { // we can skip this if we already know the index of light and temperature in the csv
					
					// get indexes of relevant column headers (light level, temperature)
					int[] lightTempIndices = getColumnHeaders(headerRow, this.split);
					this.lightIndex = lightTempIndices[0];
					this.tempIndex = lightTempIndices[1];
				}
			
			} else app.showUserErrorDialog(errorType, "[csv reader: ] csv file is empty!");
			
		} catch (IOException e1) {
			app.showUserErrorDialog(errorType, "[csv reader: ] error reading file... " + e1.toString());
		} catch (CsvHeaderException e2) {
			app.showUserErrorDialog(errorType, e2.toString());
		}
		
		fileLoaded = true;

	}

	/*
	 * Reads the next line of the CSV and stores the values in the SensorData object.
	 * If the bottom of the file is reached, the file is re-loaded and this method is called again.
	 */
	@Override
	protected void readLine() {
		
		String nextLine;
		String errorString = " value cannot be converted to ";
		int lumens = 0;
		double temp = 0;
		
		try {
			// read the next line
			if ((nextLine = this.br.readLine()) != null) {
				
				// Array to store each value in the row
				String[] items = nextLine.split(split);
				
				try {
					// set current light level
					lumens = Integer.parseInt(items[lightIndex]);
					updateFeed("Light level: " + items[lightIndex], false);
					
				} catch (NumberFormatException e) {
					
					updateFeed("Error reading csv... light level " + errorString + "integer.", true);
				}
				
				try {
					// set current temperature
					temp = Double.parseDouble(items[tempIndex]);
					updateFeed("Temperature: " + items[tempIndex], false);
					
				} catch (NumberFormatException e) {
					
					updateFeed("Error reading csv... temperature " + errorString + "double.", true);
				}
				
				data.setCurrentLightLevel(lumens);
				data.setCurrentTemperature(temp);
				
			} else {
				// load file again if we've reached the bottom
				loadFile(false);
				// try again
				readLine();
			}
			
		} catch (IOException ex) {
			
			System.err.println("[csv reader: ] Error reading file... " + ex.toString());
		}
		
	}
	
	/**
	 * Displays values read and errors thrown by the CSV reader if the feed is visible.
	 * If the feed is not visible and the message is an error, display the error in the server app instead.
	 * @param msg - message to display
	 * @param isError - boolean value to store whether or not the message is an error
	 */
	@Override
	public void updateFeed(String msg, boolean isError) {
		if(feedVisible) {
			displayBox.displayMessage(msg);
			displayBox.scrollToBottom();
		} else if (isError) {
			app.displayMessage("[csv Reader: ] " + msg);
		} 
		
		feedText += msg+"\n"; // save the message either way in case the feed is opened later
		
	}
	
	/*
	 * Closes the BufferedReader stream
	 */
	@Override
	public void closeBuffer() {
		
		// if the feed is open, close it
		if(feedVisible) {
			displayFrame.dispose();
		}
		
		try {
			this.br.close();
		} catch (IOException e) {
			app.displayMessage("[csv reader: ] error closing BufferedReader..." + e.toString());
		} finally {
			this.stopThread = true;
		}
		
	}

	/*
	 * Continuously reads each row in the file with the delay provided.
	 */
	@Override
	public void run() {
		
		while(!stopThread) {
			
			readLine();
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				app.displayMessage("Thread sleep interrupted");
			}
		}
		
		
	}

	
	



}