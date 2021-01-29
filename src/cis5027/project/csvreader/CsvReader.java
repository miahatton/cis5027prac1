package cis5027.project.csvreader;


import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;

import cis5027.project.helpers.ScrollingTextBox;
import cis5027.project.server.ServerApp;
import cis5027.project.server.helpers.AbstractFileReader;
import cis5027.project.server.helpers.CsvHeaderException;

public class CsvReader extends AbstractFileReader {
	
	private 		BufferedReader 	br;
	private 		int 			lightIndex;
	private 		int 			tempIndex;
	private 		boolean 		stopThread;
	public			ServerApp 		app;

	
	/*
	 * Constructor
	 */
	public CsvReader(ServerApp app, String fileLocation) {
		super(fileLocation, ".csv", "CSV", ",");
		this.app = app;
	}
	
	/*
	 * Overload constructor with ability to choose delay time
	 */
	public CsvReader(ServerApp app, String fileLocation, int delay) {
		super(fileLocation, ".csv", "CSV", ",", delay);
		this.app = app;
	}


	/*
	 * Find the column number where the light and temperature data is stored (in case a different csv is passed or the csv is modified)
	 * @param line - a line of the csv
	 * @param split - the delimiter
	 */
	private int[] getColumnHeaders(String line, String split) throws CsvHeaderException {
		
		boolean lightFound = false;
		boolean tempFound = false;
		
		//TODO what if the file has NO column headers? Can we set a default? maybe a try/catch ParseInt - if it works we can return the default values.
		
		int[] lightTempIndices = {-1, -1}; 			// default in case headers not found
		String[] allHeaders = line.split(split); 	// split first line into array
		
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
			throw new CsvHeaderException("light");
		}
		
		if(!tempFound) {
			throw new CsvHeaderException("temperature");
		}
		
		return lightTempIndices;
		
	}
	
	
	/*
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
				
				if(fetchHeader) { // we can skip this if we already know the index of light and temperature in csv
					
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
	public void readLine() {
		
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
	
	/*
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
	}
	
	/*
	 * Closes the BufferedReader stream
	 */
	@Override
	public void closeBuffer() {
		
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