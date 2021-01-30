/**
 * @author miahatton 
 *
 */
package cis5027.project.helpers;

/**
 * @author miahatton
 *
 */
abstract public class UserInputException extends Exception {

	protected String input;
	protected String inputType;
	protected String rule;
	
	/*
	 * Constructor
	 * @param inputText - the value in the text box that caused the error.
	 */
	public UserInputException(String inputText) {
		this.input = inputText;
	}

	@Override
	public String toString() {
		return "Invalid input for " + inputType + ": " + input + ". Must be " + rule + ".";
	}
	
}
