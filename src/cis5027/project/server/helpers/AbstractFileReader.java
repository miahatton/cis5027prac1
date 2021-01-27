package cis5027.project.server.helpers;

import java.io.BufferedReader;
import java.io.File;

abstract public class AbstractFileReader {

	protected File file;
	protected int delay; 
	protected BufferedReader br;
	protected String fileExtension;
	protected String fileLocation;
	
	abstract public void loadFile(boolean fetchHeader);
	abstract public void closeBuffer();
	
	/*
	 * Constructor
	 */
	public AbstractFileReader(String fileLocation, String fileExtension) {
		this.fileLocation = fileLocation;
		this.fileExtension = fileExtension;
		this.file = new File(fileLocation); 
		delay = 1000; // default
	}
	
	/*
	 * Overload constructor with delay parameter - for setting custom delay in ms
	 */
	public AbstractFileReader(String fileLocation, String fileExtension, int delay) {
		this.fileExtension = fileExtension;
		setFileLocation(fileLocation);
		this.delay = delay;
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
	
}
