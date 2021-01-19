package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.helpers.SensorData;
import cis5027.project.server.helpers.AbstractServer;

public class Server extends AbstractServer {

	ServerSocket serverSocket;
	boolean sending;
	int		delay;
	SensorData data;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	CsvReader csvReader;
	
	public Server(CsvReader csvReader, ServerApp app, int port) {
		super(csvReader, app, port);
		sending = false;

		SensorData data = new SensorData();
		csvReader.setTarget(data);
		
		Thread csvReaderThread = new Thread(csvReader);
		csvReaderThread.start();
	}


	@Override
	public void run() {
		try {
			initializeServer();
		} catch (IOException e) {
			app.displayMessage("Exception: " + e.toString());
		}
		
		while (!stopServer) {
			
			try {

				Socket clientSocket = serverSocket.accept();
	
				Thread messengerThread = new Thread(new Messenger(this, clientSocket, data));
				
				messengerThread.start();
		
			} catch (IOException e) {
				app.displayMessage("Error connecting to client: " + e.toString());
			}
		}
	}
	
	public void initializeServer() throws IOException {

		
		if(serverSocket == null & port > 0) {
			//TODO better validation
			serverSocket = new ServerSocket(port);
			
		}
		
		this.stopServer = false;
	}

	public ServerApp getApp() {
		return this.app;
	}

}
