package cis5027.project.clients.helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import cis5027.project.clients.ClientConnectPanel;
import cis5027.project.helpers.PortFormatException;

/**
 * @author miahatton
 * Abstract client class that handles setting up and closing network streams.
 */
public abstract class AbstractClient implements Runnable {
	
	private static final String IP = "127.0.0.1";
	
	protected ClientConnectPanel cPanel;
	protected ObjectInputStream reader;
	protected ObjectOutputStream writer;

	protected Socket socket;
	protected String clientType;
	
	private int port;
	
	protected boolean stopClient;
	
	/*
	 * Constructor
	 * @param cPanel - ClientConnectPanel instance where messages are displayed
	 * @param port - port number for socket.
	 */
	public AbstractClient(ClientConnectPanel cPanel, int port) {
		this.cPanel = cPanel;
		this.port = port;
		this.stopClient = true;
	}
	
	@Override
	public abstract void run();
	
	/*
	 * Sets up socket and IO streams.
	 * Tells the server what type of client it is.
	 */
	public void initialiseClient() {
		try {
			
			this.stopClient = false;
			
			port = cPanel.getPortNumber();
			
			// TODO better validation
			this.socket = new Socket(IP, port);
			displayMessage("Created socket");
					
			this.writer = new ObjectOutputStream(this.socket.getOutputStream());
			this.reader = new ObjectInputStream(this.socket.getInputStream());
			displayMessage("Set up IO streams");
					
			// Send client type to server
			sendMessageToServer(clientType);
			
		} catch (PortFormatException ex) {
			
			cPanel.showUserErrorDialog("Input error", ex.toString());
			
		} catch (ConnectException e1) {
			
			displayMessage("Nothing to connect to! Please check the port number and ensure server is connected.");
			
		}catch(IOException e2) {
			
			displayMessage("Error connecting to server: " + e2.toString());
		}
	}
	
	/*
	 * Displays message on client connect panel of client.
	 */
	public void displayMessage(String msg) {
		
		cPanel.displayMessage(msg);
	}

	/*
	 * Closes socket and IO streams
	 * Sends message to server to stop incoming messages.
	 */
	public void closeAll() {

		try {
			// tell server to stop connection to this client
			sendMessageToServer("STOP");
		} catch (IOException e) {
			displayMessage("Error sending STOP message to client: " + e.toString());
		}
		
		if(!stopClient) {
			stopClient = true;
			displayMessage("Closing connection...");
		}

		try {
			// close the socket
			if(this.socket != null) this.socket.close();
			
			// close the input stream
			if(this.reader != null) this.reader.close();
			
			// close the output stream
			if(this.writer != null) this.writer.close();
			
			displayMessage("All connections closed.");
		
		} catch (IOException e) {
			
			displayMessage("Error closing client..." + e.toString());
			
		} finally {
			// set the streams and socket to null whatever happens
			this.socket = null;
			this.writer = null;
			this.reader = null;
			cPanel.resetButtons();
		}

	}
	
	/*
	 * Sends a message to the server via the output stream
	 * @param msg
	 * @throws IOException
	 */
	protected void sendMessageToServer (String msg) throws IOException {
		if (!stopClient) {
			writer.writeObject(msg);
		}
	}

	/*
	 * Setter for stopClient.
	 */
	public void setStopClient(Boolean stop) {
		this.stopClient = stop;
		
	}
}
