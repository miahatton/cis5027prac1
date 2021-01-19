package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.server.helpers.AbstractServer;

public class Server extends AbstractServer {

	ArrayList<ObjectOutputStream> clientOutputStreams;
	ArrayList<String> clientTypes;
	ServerSocket serverSocket;
	boolean sending;
	int		delay;
	public Server(CsvReader csvReader, ServerApp app, int port) {
		super(csvReader, app, port);
		sending = false;
		delay = 3000;
	}

	@Override
	public void handleMessagesFromClient(String msg, ClientHandler client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendReadingToClient(ClientHandler client) {
		// TODO Auto-generated method stub
		csvReader.readLine(data);
		
		try {
			client.sendMessageToClient(data);
		} catch (IOException e){
			System.err.println("[server: ] error sending message to client... " + e.toString());
		}
	}

	@Override
	public void run() {
		try {
			initializeServer();
		} catch (IOException e) {
			app.displayMessage("Exception: " + e.toString());
		}
		
		Thread messengerThread = new Thread(new Messenger(this));
		
		messengerThread.start();
		
		String clientType;
		
		while (!stopServer) {
			
			try {
				
				clientType = null;
				Socket clientSocket = serverSocket.accept();
	
				ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
				
				/*
				 * Get the name of the client and set it to the name of the socket.
				 */
				
				try {
					clientType = (String) inputStream.readObject();
				} catch (ClassNotFoundException e1) {		
					app.displayMessage("Cannot find class 'String'");	
				} catch (IOException e2) {
					app.displayMessage("Error getting client type from client.");	
				}
				
				clientOutputStreams.add(outputStream);
				clientTypes.add(clientType);
				
				app.displayMessage("Connected to client of type " + clientType);
		
			} catch (IOException e) {
				app.displayMessage("Error connecting to client: " + e.toString());
			}
		}
	}
	
	public void initializeServer() throws IOException {

		clientOutputStreams = new ArrayList<ObjectOutputStream>();
		clientTypes = new ArrayList<String>();
		
		if(serverSocket == null & port > 0) {
			//TODO better validation
			serverSocket = new ServerSocket(port);
			
		}
		
		this.stopServer = false;
	}
	
	public class Messenger implements Runnable {
		Server server;
		public Messenger(Server server) {
			this.server = server;
		}
		public void run() {
			while(!stopServer) {
				
				csvReader.readLine(data);
				if(clientOutputStreams.size() > 0) {
					
					
					for(int i = 0; i < clientOutputStreams.size(); i++) {
						
						ObjectOutputStream out = clientOutputStreams.get(i);
						String message = "";
						
						try {
						
							switch(clientTypes.get(i)) {
							
							case "light":
								out.writeObject(data.getCurrentLightLevel());
								message = "Light level: " + data.getCurrentLightLevel();
								break;
								
							case "fan":
								out.writeObject(data.getCurrentTemperature());
								message = "Temperature: " + data.getCurrentTemperature();
								break;
							default:
								break;
								
							} // end switch
							
							out.flush();
							
							server.app.displayMessage("Sending readings to the client...\n" + message);
							
						} catch (IOException e) {
							app.displayMessage("Error sending data to client: " + e.toString());
						} // end try/catch
					} // end for loop
					
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						
						app.displayMessage("Thread interrupted: " + e.toString());
					}
					
				}
			}
		}
	}
	

}
