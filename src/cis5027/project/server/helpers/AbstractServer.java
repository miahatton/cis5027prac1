package cis5027.project.server.helpers;

import java.net.ServerSocket;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.helpers.SensorData;
import cis5027.project.server.ServerApp;

abstract public class AbstractServer implements Runnable {

	protected 	int 			port;
	//protected 	ServerSocket 	serverSocket;
	protected 	boolean 		stopServer;
	protected 	Thread 			serverListenerThread;
	public ServerApp app;
	public SensorData data;
	
	public AbstractServer(int port) {
		this.port = port;
	}
	
}
