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
				
				try {
					
					this.socket = new Socket(ip, port);
					displayMessage("Created socket");
					
					this.writer = new ObjectOutputStream(this.socket.getOutputStream());
					this.reader = new ObjectInputStream(this.socket.getInputStream());
					displayMessage("Set up IO streams");
					
					// Send client type to server
					writer.writeObject(clientType);
					
				} catch (ConnectException e) {
					displayMessage("Nothing to connect to! Please start the server and check the port number.");
					
				}
				
			}
			
			} catch(IOException ex) {
				
				ex.printStackTrace();
			}
	}
	
	public void displayMessage(String msg) {
		
		cPanel.displayMessage(msg);
	}
}
