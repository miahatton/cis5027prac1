package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.csvreader.SensorData;
import cis5027.project.server.helpers.AbstractServer;

public class Server extends AbstractServer {

	ServerSocket serverSocket;
	boolean sending;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	CsvReader csvReader;
	
	private ArrayList<Messenger> messengerList;
	
	public Server(CsvReader csvReader, ServerApp app, int port, int delay) {
		super(port);
		
		this.csvReader = csvReader;
		this.app = app;
		data = new SensorData();	
		sending = false;
		csvReader.setTarget(data);
		
		messengerList = new ArrayList<Messenger>();
		
		Thread csvReaderThread = new Thread(csvReader);
		csvReaderThread.start();
		app.csvReaderFeedBtn.setEnabled(true);
		
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
				
				Messenger messenger = new Messenger(this, clientSocket, data);

				Thread messengerThread = new Thread(messenger);
				messengerThread.start();
				
				messengerList.add(messenger);
		
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


	public void closeAll() {
		
		try {
			
			if (this.serverSocket != null) this.serverSocket.close();
			this.stopServer = true;
			
		} catch (IOException e) {
			
			app.displayMessage("Error closing server connection... " + e.toString());
			
		} finally {

			for (Messenger messenger: messengerList) {
				
				try {
					messenger.closeAll();
				} catch (Exception e) {	
				}
			}
			
			this.serverSocket = null;
			
			// close the csvReader.
			this.csvReader.closeBuffer();
		}
		
	}

}
