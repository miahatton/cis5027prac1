package cis5027.project.server.helpers;

public class DelayFormatException extends Exception {

	String inputDelay;
	
	public DelayFormatException(String delayText) {
		this.inputDelay = delayText;
	}

	public String toString() {
		return "Invalid input for delay: " + inputDelay + ". Must be a positive integer.";
	}
	
}
