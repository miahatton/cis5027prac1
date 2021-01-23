package cis5027.project.clients.helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import cis5027.project.clients.ClientConnectPanel;

public abstract class AbstractClient implements Runnable {
	
	protected ClientConnectPanel cPanel;
	protected ObjectInputStream reader;
	protected ObjectOutputStream writer;

	protected Socket socket;
	protected String clientType;
	private String ip;
	private int port;
	
	public AbstractClient(ClientConnectPanel cPanel, int port) {
		this.cPanel = cPanel;
		this.port = port;
		this.ip = "127.0.0.1";

	}
	
	public abstract void run();
	
	public void initialiseClient() {
		try {
			
			port = cPanel.getPortNumber();
			// TODO better validation
			if (port > 0) {
					
				this.socket = new Socket(ip, port);
				displayMessage("Created socket");
					
				this.writer = new ObjectOutputStream(this.socket.getOutputStream());
				this.reader = new ObjectInputStream(this.socket.getInputStream());
				displayMessage("Set up IO streams");
					
				// Send client type to server
				writer.writeObject(clientType);
				
			}
			
		} catch (ConnectException e1) {
			
			displayMessage("Nothing to connect to! Please check the port number and ensure server is connected.");
			
		}catch(IOException e2) {
			
			//TODO improve
			e2.printStackTrace();
		}
	}
	
	public void displayMessage(String msg) {
		
		cPanel.displayMessage(msg);
	}

	public void closeAll() {
		
		displayMessage("Closing connection...");
		
		try {
			// close the socket
			if(this.socket != null) this.socket.close();
			
			// close the input stream
			if(this.reader != null) this.reader.close();
			
			// close the output stream
			if(this.writer != null) this.writer.close();
		
		} catch (IOException e) {
			
			displayMessage("Error closing client..." + e.toString());
			
		} finally {
			// set the streams and socket to null either way
			this.socket = null;
			this.writer = null;
			this.reader = null;
		}
		
		
		
	}
}
