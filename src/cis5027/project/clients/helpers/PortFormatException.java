package cis5027.project.clients.helpers;

/**
 * @author miahatton
 * Custom exception to handle port number formatting.
 * Port number must be an integer between 1024 and 65535.
 */
public class PortFormatException extends UserInputException {
	
	/**
	 * Constructor for string input port number
	 * @param portInput		The text entered that threw the error
	 */
	public PortFormatException(String portInput) {
		super(portInput);
		this.inputType = "port number";
		// the rule to be displayed to the user
		this.rule = "an integer between 1024 and 65535";
	}
	
	/*
	 * Constructor for integer port number.
	 */
	public PortFormatException(int portNum) {
		super(String.valueOf(portNum));
	}
	
}
