package cis5027;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cis5027.project.server.ClientHandler;
import cis5027.project.server.helpers.AbstractServer;

public class Server extends AbstractServer implements Runnable {

	private ServerSocket	serverSocket;
	private boolean			stopServer;
	private Thread			serverListenerThread;
	private ThreadGroup		clientThreadGroup;
	
	int port;
	
	// Constructor
	
	public Server() {
		
		this.stopServer = false;
		
		this.clientThreadGroup = new ThreadGroup("ClientHandler threads");
		
	}
	
	public void initializeServer(int port) throws IOException {
		
		this.port = port;
		
		if(serverSocket == null) {
			
			serverSocket = new ServerSocket(port);
			
		}
		
		stopServer = false;
		serverListenerThread = new Thread(this);
		serverListenerThread.start();
		
	}
	
	
	/*
	 * Thread that listens to port and creates client connections.
	 */
	@Override
	public void run() {
		
		System.out.println("[server: ] starting server. Listening @ port " + port);
		
		// increments when client connects.
		int clientCount = 0;
		
		// loops until stopserver flag is set to true.
		while (!this.stopServer) {
			
			Socket clientSocket = null;
			
			try {
				clientSocket = serverSocket.accept();

			} catch (IOException e) {
				System.err.println("[server: ] Error when handling client connections on port " + port);
			}
			
			// ClientHandler ch = new ClientHandler(this.clientThreadGroup, clientSocket, clientCount, this);
			
			new ClientHandler(this.clientThreadGroup, clientSocket, clientCount, this);
			
			try {
				
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				System.err.println("[server: ] server listener thread interrupted..." + e.toString());
			}
			
			clientCount++;
			
		}
		
	}
	
	// TODO modify this so that the client sends its name and the server finds out what type of data to send.
	public synchronized void handleMessagesFromClient(String msg, ClientHandler client) {
		
		String formattedMessage = String.format("[client %d] : %s", client.getClientID(), msg);
		
		display(formattedMessage);
		
		String response = "[server says]: " + msg.toUpperCase();
		sendMessageToClient(response, client);
		
	}

	private void display(String formattedMessage) {
		
		System.out.println(">> " + formattedMessage);
		
	}
	
	//TODO change this method to send data from CSV instead.
	public synchronized void sendMessageToClient(String msg, ClientHandler client) {
		
		try {
			
			client.sendMessageToClient(msg);
			
		} catch (IOException e) {
			
			System.err.println("[server:] Server-to-client message sending failed...");
		}
	}
	
	public Thread[] getClientConnections() {
		
		Thread[] clientThreadList = new Thread[clientThreadGroup.activeCount()];
		clientThreadGroup.enumerate(clientThreadList);
		
		return clientThreadList;
		
	}
	
	public void close() {
		
		if (this.serverSocket == null) return;
		
		try {
			
			this.stopServer = true;
			this.serverSocket.close();
			
		} catch (IOException e) {
			
			System.err.println("[server: ] Error in closing server connection...");
			
		}
		
		
	}
	
	public static void main(String[] args) {
		
		/*Server server = new Server();
		int port = 7777;
		
		try {
			server.initializeServer(port);
		} catch (IOException e) {
			System.err.println("[server: ] Error in initializing the server on port "+ port);
		}*/
		
	}
	
	
}
