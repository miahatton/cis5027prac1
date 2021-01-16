package cis5027.project.server.helpers;

import java.net.ServerSocket;

import cis5027.project.server.ClientHandler;

abstract public class AbstractServer implements Runnable {

	protected 	int 			port;
	protected 	ServerSocket 	serverSocket;
	protected 	boolean 		stopServer;
	protected 	Thread 			serverListenerThread;
	
	public abstract void handleMessagesFromClient(String msg, ClientHandler client);
	public abstract void sendMessageToClient(String msg, ClientHandler client);
	
}
