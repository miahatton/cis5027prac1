package cis5027.project.csvreader;


import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;

import cis5027.project.helpers.ScrollingTextBox;
import cis5027.project.server.ServerApp;
import cis5027.project.server.helpers.AbstractFileReader;

public class CsvReader extends AbstractFileReader implements Runnable {

	private final String split = ",";
	BufferedReader br;
	int lightIndex;
	int tempIndex;
	boolean fileLoaded;
	boolean stopThread;
	ServerApp app;
	SensorData data;

	JFrame displayFrame;
	ScrollingTextBox displayBox;
	boolean feedVisible;
	
	// Constructor
	public CsvReader(String fileLocation) {
		super(fileLocation, ".csv");
	}
	
	// Overload constructor with ability to choose delay time
	public CsvReader(ServerApp app, String fileLocation, int delay) {
		super(fileLocation, ".csv", delay);
		this.app = app;
	}


	private int[] getColumnHeaders(String line, String split) {
		
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
	
	public void loadFile (boolean fetchHeader) {
			
		try {
			
			br = new BufferedReader(new FileReader(this.file));
			
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

	public void readLine() {
		
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
	
	public void updateFeed(String msg, boolean isError) {
		if(feedVisible) {
			displayBox.displayMessage(msg);
			displayBox.scrollToBottom();
		} else if (isError) {
			app.displayMessage("[Csv Reader: ] " + msg);
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
		} finally {
			this.stopThread = true;
		}
		
	}

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

	public void setTarget(SensorData data) {
		this.data = data;
	}
	
	public void setDelay(int newDelay)  {

		this.delay = newDelay;
		
	}
	
	public void draw() {
		
		displayFrame = new JFrame("CSV Reader Feed");
		displayBox = new ScrollingTextBox(20,20);
		displayFrame.getContentPane().add(displayBox.getScrollPane());
		
		displayFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	feedVisible = false;
            }
        });
		
		this.feedVisible = true;
		displayFrame.setSize(200,200);
		displayFrame.setVisible(true);
	}


}