package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
		
		while(clientConnected) {
			
			try {

				synchronized(data) {
					switch(clientType) {
					case "light":
						out.writeObject(data.getCurrentLightLevel());
						break;
					case "fan":
						out.writeObject(data.getCurrentTemperature());
						break;
					}
				}

				out.flush();
		
				server.app.displayMessage("Sending reading to the client of type " + clientType);

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
				
				app.displayMessage("Closing connection to client of type " + clientType);
				closeAll();
				
			} 	
		} catch (IOException e2) {
			app.displayMessage("Error getting message from client: " + e2.toString());
			try {
				closeAll();
			} catch (IOException e3) {	
				app.displayMessage("Error closing connections: " + e2.toString());
			}
		} 
	
	}
	
	public void closeAll() throws IOException {
		
		app.displayMessage("Closing connection to client...");
		
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

