package cis5027.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.csvreader.SensorData;
import cis5027.project.helpers.PortFormatException;
import cis5027.project.server.helpers.AbstractServer;

/**
 * @author miahatton
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
		sending = false;
		
		// create SensorData object 
		data = new SensorData(csvReader);	
		
		// arraylist to store Messenger instances (one per client connected)
		messengerList = new ArrayList<Messenger>();
		
		// start the CSV reader in a separate thread so that it updates SensorData values independently of what the client/server are doing.
		Thread csvReaderThread = new Thread(csvReader);
		csvReaderThread.start();
		
		// once the CSV reader thread has started the feed can be opened so enable the button
		app.enableCsvButton(true);
		
	}

	/*
	 * Initialises server socket and listens for client connections. 
	 * When a client connects, initialise a Messenger object to send messages to the client.
	 * Each Messenger object runs in a separate thread.
	 */
	@Override
	public void run() {
		try {
			
			initializeServer();
			
		} catch (IOException e1) {
			app.displayMessage("Exception: " + e1.toString());
		} catch (PortFormatException e2) {
			app.showUserErrorDialog("Invalid Input", e2.toString());
		}
		
		/*
		 * Loop until server is stopped.
		 */
		while (!stopServer) {
			
			try {
				
				// TODO there should only be <=2 clients connected.
				//if (messengerList.size()<2) {
					Socket clientSocket = serverSocket.accept();
					
					Messenger messenger = new Messenger(this, clientSocket, data, this.app);

					Thread messengerThread = new Thread(messenger);
					messengerThread.start();
					
					messengerList.add(messenger);
				//}
		
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

			 // Validate port number
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
			// close the socket
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
					messenger.tryToClose();
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
