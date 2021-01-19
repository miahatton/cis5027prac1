package cis5027.project.clients;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import cis5027.project.helpers.SensorData;

public class Client implements Runnable {
	
	ClientConnectPanel cPanel;
	ObjectInputStream reader;
	ObjectOutputStream writer;
	Socket socket;
	String clientType;
	String ip;
	int port;
	
	public Client(ClientConnectPanel cPanel, int port, String type) {
		this.cPanel = cPanel;
		this.port = port;
		this.ip = "127.0.0.1";
		this.clientType = type;
	}
	
	public void run() {
		
		SensorData reading;
		try {
			while ((reading = (SensorData) reader.readObject()) != null) {
				if (clientType == "light") {

					displayMessage("Current light level: " + reading.getCurrentLightLevel());
				}

			} // close while
		} catch(Exception ex) {ex.printStackTrace();}
		
	}
	
	public void initialiseClient() {
		try {
			
			port = cPanel.getPortNumber();
			// TODO better validation
			if (port > 0) {
				
				try {
					
					socket = new Socket(ip, port);
					displayMessage("Created socket");
					
					writer = new ObjectOutputStream(this.socket.getOutputStream());
					reader = new ObjectInputStream(this.socket.getInputStream());
					displayMessage("Set up IO streams");
					
					// Send client type to server
					writer.writeObject(clientType);
					
				} catch (ConnectException e) {
					displayMessage("Nothing to connect to! Please start the server and check the port number.");
					
				}
				
			}
			
			} catch(IOException ex) {
				
				ex.printStackTrace();
			}
	}
	
	public void displayMessage(String msg) {
		
		cPanel.displayMessage(msg);
	}
}
