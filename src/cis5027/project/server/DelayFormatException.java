package cis5027.project.server;

/**
 * @author miahatton
 * Custom Exception to handle issues delay input value. 
 * "Delay" must be an integer greater than 0.
 */

public class DelayFormatException extends UserInputException {

	/*
	 * Constructor
	 * @param delayText - the value in the text box on the server app that caused the error.
	 */
	public DelayFormatException(String delayText) {
		super(delayText);
		this.inputType = "delay";
		
		// The rule to be displayed to the user - this must be a positive integer.
		this.rule = "a positive integer";
		
	}

	
}
