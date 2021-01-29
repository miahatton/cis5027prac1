package cis5027.project.helpers;

/**
 * @author miahatton
 * Custom exception to handle port number formatting.
 * Port number must be an integer between 1024 and 65535.
 */
public class PortFormatException extends Exception {

	private String invalidPort;
	
	/*
	 * Constructor for string input port number
	 */
	public PortFormatException(String portInput) {
		this.invalidPort = portInput;
	}
	
	/*
	 * Constructor for integer port number.
	 */
	public PortFormatException(int portNum) {
		this.invalidPort = String.valueOf(portNum);
	}
	
	@Override
	public String toString() {
		return "Invalid input for port number: " + invalidPort + ". Must be an integer between 1024 and 65535.";
	}
	
}
