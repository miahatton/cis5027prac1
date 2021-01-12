package cis5027.project.helpers;

import java.io.BufferedReader;
import java.io.File;

import cis5027.project.csvreader.LightLevel;
import cis5027.project.csvreader.Temperature;

abstract public class InputFileReader {

	protected File file;
	protected String split;
	protected int delay; 
	protected BufferedReader br;
	protected String fileExtension;
	
	abstract public void loadFile(boolean fetchHeader);
	abstract public void readLine(Temperature temp, LightLevel light);
	abstract public void closeBuffer();
	
	public InputFileReader(String fileLocation, String split) {
		this.file = new File(fileLocation);
		this.split = split;
		delay = 1000; // default
	}
	
	public InputFileReader(String fileLocation, String split, int delay) {
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
		
		if(fileLocation.contains(this.fileExtension)) {
		
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
	
	
}
