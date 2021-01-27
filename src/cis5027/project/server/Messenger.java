package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import cis5027.project.csvreader.SensorData;

/**
 * @author miach
 * Each instance of the Messenger class connects to a client, sends readings to the client and waits for a response.
 */
public class Messenger implements Runnable {
	ServerApp app;
	Server server;
	boolean clientConnected;
	String clientType;
	Socket clientSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	boolean stopConnection;
	
	SensorData data;
	
	/*
	 * Constructor
	 */
	public Messenger(Server server, Socket clientSocket, SensorData data, ServerApp app) {
		this.server = server;
		this.app = app;
		this.clientType = null;
		this.clientSocket = clientSocket;
		
		// link the Messenger to the SensorData object so that it can access readings.
		this.data = data;
		connectToClient();
	}
	
	/*
	 * While connected to a client, the Messenger object gets the current light or temperature reading (depending on client type) and sends to the client.
	 * It waits for a response before repeating.
	 */
	public void run() {
		
		String notification;
		
		while(clientConnected) {
			
			notification = "Sending reading to the "+ clientType + " client: [";
			
			try {

				/*
				 * This block is synchronized so that the csvreader and messenger do not try to access the SensorData object at the same time.
				 */
				synchronized(data) {
					switch(clientType) {
					case "light":
						// get light level variable from SensorData object
						int lightLevel = data.getCurrentLightLevel();
						// send to client
						out.writeObject(lightLevel);
						notification += ("Light level = " + String.valueOf(lightLevel) + "]");
						break;
					case "fan":
						// get temperature variable from SensorData object
						double temperature = data.getCurrentTemperature();
						// send to client
						out.writeObject(temperature);
						notification += ("Temperature = " + String.valueOf(temperature) + "]");
						break;
					}
				}

				out.flush();
		
				server.app.displayMessage(notification);
				
			} catch (SocketException e1) {
				app.displayMessage(clientType + "client has closed connection.");
				tryToClose();
			} catch (IOException e2) {
				app.displayMessage("Error receiving message from client: " + e2.toString());
				tryToClose();
			} catch(NullPointerException e3) {
				
				app.displayMessage("Null pointer exception: " + e3.toString());
				
			} finally {
				
				// wait for a response before continuing, in case client sends "STOP" message (connection closed)
				handleMessageFromClient();
				
			} 
		}
	}
	
	/*
	 * When a message is received from the client, display it. If the message is "STOP", close the connection.
	 */
	private void handleMessageFromClient() {
		
		if(clientSocket!=null) {
			
			String inwardMessage;
			Object messageFromClient = null;
			
			try {
				// check that message was received from the client
				try {
					messageFromClient = in.readObject();
				} catch (ClassNotFoundException e1) {
					throw new IOException(e1);
				} 
					
				// cast message to String.
				inwardMessage = (String) messageFromClient;
				
				app.displayMessage("Message received from " + clientType + " client: [" + inwardMessage + "]");
				
				// close connection if message is "STOP"
				if (inwardMessage.equals("STOP")) {
					
					closeAll();
					
				} 	
			} catch (SocketException e2) {
				app.displayMessage(clientType + "client has closed connection.");
				tryToClose();
			} catch (IOException e4) {
				app.displayMessage("Error receiving message from client: " + e4.toString());
				tryToClose();
			}
		}
	}
	
	/*
	 * Handles errors on attempts to close connections.
	 */
	public void tryToClose() {
		try {
			closeAll();
		} catch (IOException e) {	
			
			if(!e.toString().equals("Socket closed")) {
				app.displayMessage("Error closing connections: " + e.toString());
			}
		}
		
	}
	
	/*
	 * Closes connection to client and sets socket and IO streams to null.
	 */
	private void closeAll() throws IOException {
		
		app.displayMessage("Closing connection to " + clientType + " client...");
		
		try {
			
			// close the socket
			if (this.clientSocket != null) this.clientSocket.close();
			
			// close the output stream
			if (this.out != null) this.out.close();
			
			// close the input stream
			if (this.in != null) this.in.close();
			
		} finally {
			
			// set socket and IO Streams to null no matter what.
			this.clientSocket = null;
			this.out = null;
			this.in = null;
			this.clientConnected = false;
			
		}
		
	}
	
	/*
	 * Sets up IO streams for client and waits for message from client to say what type of client it is (light or fan).
	 */
	private void connectToClient() {
		
		try {
			
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			
		} catch (IOException e) {
			app.displayMessage("Error setting up IO streams to client");
		}
		
		try {
			clientType = (String) in.readObject();
		} catch (ClassNotFoundException e1) {		
			app.displayMessage("Cannot find class 'String'");	
		} catch (IOException e2) {
			app.displayMessage("Error getting client type from client.");	
		}
		
		app.displayMessage("Connected to client of type " + clientType + ". Port: " + clientSocket.getPort());
		clientConnected = true;
		
		/*
		 * Let the SensorData object know that this type of client is connected.
		 * Now it will wait for the Messenger to read the data for this client type before attempting to write a new value.
		 */
		data.connectClient(clientType);
	}

}

