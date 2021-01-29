package cis5027.project.server.helpers;

import java.io.BufferedReader;
import java.io.File;

import javax.swing.JFrame;

import cis5027.project.csvreader.SensorData;
import cis5027.project.helpers.ScrollingTextBox;

/**
 * @author miahatton
 * Abstract file reader class extended by CSV reader class.
 * Includes generic method draw() for generating UI feed, and getters and setters.
 */
abstract public class AbstractFileReader implements Runnable {

	protected 	File 			file;
	protected 	String 			fileLocation;
	
	protected 	int 			delay; 
	protected 	BufferedReader 	br;
	public 		SensorData 		data;
	protected	boolean 		fileLoaded;
	
	/*
	 * These instance variables are final because they relate to the type of 
	 * file reader and shouldn't be changed once file reader is initialised.
	 */
	protected 	final String 	fileExtension;
	protected 	final String 	split;
	protected 	final String 	fileType;
	
	// Instance variables for the UI feed where readings can be viewed.
	protected JFrame 				displayFrame;
	protected ScrollingTextBox 		displayBox;
	protected boolean 				feedVisible;
	
	/*
	 * Abstract methods
	 */
	abstract public void loadFile(boolean fetchHeader);
	abstract public void readLine();
	abstract public void closeBuffer();
	abstract public void updateFeed(String msg, boolean isError);
	abstract public void run();
	
	/*
	 * Constructor with default delay of 1000 ms.
	 */
	public AbstractFileReader(String fileLocation, String fileExtension, String fileType, String split)  {
		this(fileLocation, fileExtension, fileType, split, 1000); 
	}
	
	/*
	 * Overload constructor with delay parameter - for setting custom delay in ms
	 */
	public AbstractFileReader(String fileLocation, String fileExtension, String fileType, String split, int delay) {
		this.fileExtension = fileExtension;
		this.fileType = fileType;
		this.split = split;
		
		setFileLocation(fileLocation);
		this.file = new File(fileLocation);
		
		this.delay = delay;
	}
	
	/*
	 * Generates UI for viewing the values as they are read from the CSV. 
	 * Used for testing. 
	 * Can be viewed by clicking the "Open CSV feed" button on the server app.
	 */
	public void draw() {
		
		displayFrame = new JFrame(fileType + "Reader Feed");
		displayBox = new ScrollingTextBox(20,20);
		displayFrame.getContentPane().add(displayBox.getScrollPane());
		
		// stop the fileReader from updating the feed if it is closed.
		displayFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	feedVisible = false;
            }
        });
		
		this.feedVisible = true;
		displayFrame.setSize(20,20);
		displayFrame.setVisible(true);
	}
	
	/*
	 * Getters and setters
	 */
	
	// Getter for file location
	public String getFileLocation() {
		return file.getAbsolutePath();
	}

	// setter for file location
	public void setFileLocation(String fileLocation) {
		
		if(fileLocation.contains(this.fileExtension)) {
		
			//TODO try/catch for validating file location
		
			this.file = new File(fileLocation); 
		}
	}
	
	// link fileReader to data object where values are stored
	public void setTarget(SensorData data) {
		this.data = data;
	}
	
	// setter for delay variable
	public void setDelay(int newDelay)  {

		this.delay = newDelay;
		
	}
	
	//getter for fileLoaded variable
	public boolean getFileLoaded() {
		
		return this.fileLoaded;
	}
	
	
}
