package cis5027.project.server.helpers;

/**
 * @author miahatton
 * Custom Exception to handle issues with CSV headers. 
 * They must include "temperature" and "light level" for the CSV reader to find the correct values.
 */

public class CsvHeaderException extends Exception {

	String headerType;
	
	/*
	 * Constructor
	 * @param header - the header that is missing from the CSV file.
	 */
	public CsvHeaderException(String header) {
		this.headerType = header;
	}
	
	@Override
	public String toString() {
		return "[csv reader: ] Error: " + headerType + " column header not found. Please ensure csv includes column header: " + headerType;
	}
	
}
