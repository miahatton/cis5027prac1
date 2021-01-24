package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import cis5027.project.csvreader.SensorData;

public class Messenger implements Runnable {
	ServerApp app;
	Server server;
	boolean clientConnected;
	String clientType;
	Socket clientSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	boolean stopConnection;
	int delay;
	
	SensorData data;
	
	public Messenger(Server server, Socket clientSocket, SensorData data, int delay) {
		this.server = server;
		this.app = server.getApp();
		this.clientType = null;
		this.clientSocket = clientSocket;
		this.data = data;
		this.delay = delay;
		connectToClient();
	}
	
	public void run() {
		
		String message = "Sending reading to the "+ clientType + " client: [";
		
		while(clientConnected) {
			
			try {

				synchronized(data) {
					switch(clientType) {
					case "light":
						int lightLevel = data.getCurrentLightLevel();
						out.writeObject(lightLevel);
						message += ("Light level = " + String.valueOf(lightLevel) + "]");
						break;
					case "fan":
						double temperature = data.getCurrentTemperature();
						out.writeObject(temperature);
						message += ("Temperature = " + String.valueOf(temperature) + "]");
						break;
					}
				}

				out.flush();
		
				server.app.displayMessage(message);

				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					app.displayMessage("Thread sleep interrupted");
				}
				
			} catch (IOException e1) {
				app.displayMessage("Error sending data to client: " + e1.toString());
				
				try {
					closeAll();
				} catch (IOException e2) {
					
					app.displayMessage("Error closing connections: " + e2.toString());
					
				}
				
				
			} // end try/catch
			  catch(NullPointerException e3) {
				app.displayMessage("Null pointer exception: " + e3.toString());
			} finally {
				
				handleMessageFromClient();
				
			} 
		}
	}
	
	public void handleMessageFromClient() {
		
		String inwardMessage;
		Object messageFromClient = null;
		
		try {
			// check that message was received from the client
			try {
				messageFromClient = in.readObject();
			} catch (ClassNotFoundException e1) {
				throw new IOException(e1);
			} 
				
			inwardMessage = (String) messageFromClient;
			
			app.displayMessage("Message received from client: " + inwardMessage);
			
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
	
	public void tryToClose() {
		try {
			closeAll();
		} catch (IOException e) {	
			app.displayMessage("Error closing connections: " + e.toString());
		}
		
	}
	
	public void closeAll() throws IOException {
		
		app.displayMessage("Closing connection to " + clientType + " client...");
		
		try {
			
			// close the socket
			if (this.clientSocket != null) this.clientSocket.close();
			
			// close the output stream
			if (this.out != null) this.out.close();
			
			// close the input stream
			if (this.in != null) this.in.close();
			
		} finally {
			
			this.clientSocket = null;
			this.out = null;
			this.in = null;
			this.clientConnected = false;
			
		}
		
	}
	
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
		
		data.connectClient(clientType);
	}
	
}

