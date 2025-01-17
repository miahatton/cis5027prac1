package cis5027.project.server.helpers;

import java.net.ServerSocket;

import cis5027.project.server.ServerApp;

/**
 * @author miahatton
 * Abstract server class. Server class extends this class.
 */

abstract public class AbstractServer implements Runnable {

	protected 	int 			port;
	protected 	boolean 		stopServer;
	protected 	Thread 			serverListenerThread;
	public 		ServerApp 		app;
	protected	ServerSocket 	serverSocket;
	protected	boolean 		sending;
	
	/*
	 * Constructor
	 */
	public AbstractServer(int port) {
		this.port = port;
	}
	
}
