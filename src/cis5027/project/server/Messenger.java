package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cis5027.project.helpers.SensorData;

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
	}
	
	public void run() {
		
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
		
		app.displayMessage("Connected to client of type " + clientType);
		clientConnected = true;
		
		data.connectClient(clientType);
		
		while(clientConnected) {
			
			try {

				synchronized(data) {
					switch(clientType) {
					case "light":
						out.writeObject(data.getCurrentLightLevel());
						break;
					case "temp":
						out.writeObject(data.getCurrentTemperature());
						break;
					}
				}

				out.flush();
		
				server.app.displayMessage("Sending reading to the client of type " + clientType);
					
				app.displayMessage("Message received from client: " + (String) in.readObject());
				
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					app.displayMessage("Thread sleep interrupted");
				}
				
			} catch (IOException e1) {
					app.displayMessage("Error sending data to client: " + e1.toString());
			} // end try/catch
			  catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch(NullPointerException e3) {
				app.displayMessage("Null pointer exception: " + e3.toString());
			}

			
		}
		
				
			//TODO The server should stop communicating with the client (s) when the send the STOP command
	}
	
}
