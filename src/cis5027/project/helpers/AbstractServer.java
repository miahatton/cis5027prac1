package cis5027.project.helpers;

import cis5027.project.server.ClientHandler;

abstract public class AbstractServer {

	public abstract void handleMessagesFromClient(String msg, ClientHandler client);
	public abstract void sendMessageToClient(String msg, ClientHandler client);
	
}
