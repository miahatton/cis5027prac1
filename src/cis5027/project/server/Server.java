package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.csvreader.SensorData;
import cis5027.project.helpers.PortFormatException;
import cis5027.project.server.helpers.AbstractServer;

/**
 * @author miach
 * The Server Class makes connections to clients and initialised the MEssenger Class that sends messages to the clients.
 */
public class Server extends AbstractServer {
	
	private		CsvReader 			csvReader;
	private 	SensorData 			data;
	
	// ArrayList to keep track of Messenger instances (each Messenger connects to one client)
	private ArrayList<Messenger> messengerList;
	
	/*
	 * Constructor
	 */
	public Server(CsvReader csvReader, ServerApp app, int port, int delay) {
		super(port);
		
		this.csvReader = csvReader;
		this.app = app;
		data = new SensorData(csvReader);	
		sending = false;
		csvReader.setTarget(data);
		
		messengerList = new ArrayList<Messenger>();
		
		Thread csvReaderThread = new Thread(csvReader);
		csvReaderThread.start();
		app.enableCsvButton(true);
		
	}


	@Override
	public void run() {
		try {
			initializeServer();
			
		} catch (IOException e1) {
			app.displayMessage("Exception: " + e1.toString());
		} catch (PortFormatException e2) {
			app.showUserErrorDialog("Invalid Input", e2.toString());
		}
		
		while (!stopServer) {
			
			try {

				Socket clientSocket = serverSocket.accept();
				
				Messenger messenger = new Messenger(this, clientSocket, data, this.app);

				Thread messengerThread = new Thread(messenger);
				messengerThread.start();
				
				messengerList.add(messenger);
		
			} catch (IOException e) {
				app.displayMessage("Error connecting to client: " + e.toString());
			}
		}
	}
	
	/*
	 * Initialises the server socket
	 */
	public void initializeServer() throws IOException, PortFormatException {

		
		if(serverSocket == null) {
			if(port >= app.getMinPortNum() & port <= app.getMaxPortNum()) {
				serverSocket = new ServerSocket(port);
			} else throw new PortFormatException(port);	
		}
		
		this.stopServer = false;
	}

	/*
	 * Closes server socket and all connections to clients.
	 */
	public void closeAll() {
		
		try {
			
			if (this.serverSocket != null) this.serverSocket.close();
			this.stopServer = true;
			
		} catch (IOException e) {
			
			app.displayMessage("Error closing server connection... " + e.toString());
			
		} finally {

			/*
			 * Loop through each messenger in the arraylist and close connection to the client
			 */
			
			for (Messenger messenger: messengerList) {
				
				try {
					messenger.closeAll();
				} catch (Exception e) {	
				} // Ignore all exceptions when closing clients.
			}
			
			this.serverSocket = null;
			
			// close the csvReader.
			this.csvReader.closeBuffer();
			app.reset();
		}
		
	}

}
