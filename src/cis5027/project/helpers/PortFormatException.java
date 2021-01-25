package cis5027.project.helpers;

public class PortFormatException extends Exception {

	private String invalidPort;
	
	public PortFormatException(String portInput) {
		this.invalidPort = portInput;
	}
	
	public String toString() {
		return "Invalid input for port number: " + invalidPort + ". Must be an integer between 1024 and 65535.";
	}
	
}
