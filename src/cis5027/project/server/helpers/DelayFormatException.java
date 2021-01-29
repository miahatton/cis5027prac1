package cis5027.project.server.helpers;

/**
 * @author miahatton
 * Custom Exception to handle issues delay input value. 
 * "Delay" must be an integer greater than 0.
 */

public class DelayFormatException extends Exception {

	String inputDelay;
	
	/*
	 * Constructor
	 * @param delayText - the value in the text box on the server app that caused the error.
	 */
	public DelayFormatException(String delayText) {
		this.inputDelay = delayText;
	}

	@Override
	public String toString() {
		return "Invalid input for delay: " + inputDelay + "s. Must be a positive integer.";
	}
	
}
