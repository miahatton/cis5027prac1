package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import cis5027.project.helpers.AbstractServer;

public class ClientHandler extends Thread {

	
	private Socket					clientSocket;
	private AbstractServer			server;
	private boolean					stopConnection;
	
	private ObjectOutputStream		out;
	private ObjectInputStream		in;
	
	private int						clientID;

	
	public ClientHandler(ThreadGroup threadgroup, Socket socket, int clientID, AbstractServer server) {
		
		this.clientSocket = socket;
		this.server = server;
		this.stopConnection = false;
		this.clientID = clientID;
		
		System.out.println("[ClientHandler: ] new client request received, port " + socket.getPort());
		
		try {
			
			this.out = new ObjectOutputStream(this.clientSocket.getOutputStream());
			this.in = new ObjectInputStream(this.clientSocket.getInputStream());
			
		} catch (IOException e) {
			
			System.err.println("[ClientHandler: ] error when establishing IO streams on client socket.");
			
			try {
				
				closeAll();
				
			} catch (IOException e1) {
				
				System.err.println("[ClientHandler: ] error when closing connections..." + e1.toString());
			}
		
		}
		
		start();
		
	}
	
	private void closeAll() throws IOException {
		
		try {
			
			// Close the socket
			if (this.clientSocket != null) this.clientSocket.close();
			
			// Close the output stream
			if (this.out != null) this.out.close();
			
			// Close the input stream
			if (this.in != null) this.in.close();
	
		} finally {
			
			// set all streams and sockets to null
			this.in = null;
			this.out = null;
			this.clientSocket = null;
			
		}
	}

	public int getClientID() {

		return this.clientID;
	}

	public void sendMessageToClient (String msg) throws IOException {
		
		if (this.clientSocket == null || this.out == null) {
			
			throw new SocketException("socket does not exist");
			
		}
		
		this.out.writeObject(msg);
		
	}
	
	@Override
	public void run() {
		
		// message from the client
		String msg = "";
		
		try {

			while(!this.stopConnection) {
				
				msg = (String) this.in.readObject();
				this.server.handleMessagesFromClient(msg, this);
				
				if(msg.equals("over")) {
					this.stopConnection = true;
				}
			}
			
			System.out.println("[ClientHandler: ] Stopping connection to the client connection ID: " + this.clientID);
			
		} catch (Exception e) {
			
			System.err.println("[ClientHandler: ] error when reading message from client..." + e.toString());
			
			if(!this.stopConnection) {
				try {
					closeAll();
				} catch (Exception e1) {
					System.err.println("[ClientHandler: ] error when closing connection... " + e1.toString());
				}
			}
			
		} finally {
			
			if(this.stopConnection) {
				
				try {
					closeAll();
				} catch (Exception e1) {
					System.err.println("[ClientHandler: ] error when closing connection... " + e1.toString());
				}
				
			} else this.stopConnection = true;
		}
		
	}
	
	public String toString() {
		return this.clientSocket == null ? null: this.clientSocket.getInetAddress().getHostName() + "(" + this.clientSocket.getInetAddress().getHostAddress() + ")";
	}
	
}
