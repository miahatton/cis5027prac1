package cis5027.project.server.helpers;

public class CsvHeaderException extends Exception {

	String headerType;
	
	public CsvHeaderException(String header) {
		this.headerType = header;
	}
	
	public String toString() {
		return "[csv reader: ] Error: " + headerType + " column header not found. Please ensure csv includes column header: " + headerType;
	}
	
}
