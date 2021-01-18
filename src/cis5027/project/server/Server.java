package cis5027.project.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.server.helpers.AbstractServer;

public class Server extends AbstractServer {

	ArrayList<ObjectOutputStream> clientOutputStreams;
	ServerSocket serverSocket;
	boolean sending;
	int		delay;
	public Server(CsvReader csvReader, ServerApp app, int port) {
		super(csvReader, app, port);
		sending = false;
		delay = 3000;
	}

	@Override
	public void handleMessagesFromClient(String msg, ClientHandler client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendReadingToClient(ClientHandler client) {
		// TODO Auto-generated method stub
		csvReader.readLine(data);
		
		try {
			client.sendMessageToClient(data);
		} catch (IOException e){
			System.err.println("[server: ] error sending message to client... " + e.toString());
		}
	}

	@Override
	public void run() {
		try {
			initializeServer();
		} catch (IOException e) {
			app.displayMessage("Exception: " + e.toString());
		}
		
		Thread messengerThread = new Thread(new Messenger(this));
		messengerThread.start();
		
		while (!stopServer) {
			
			try {
			Socket clientSocket = serverSocket.accept();
			
			ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			
			clientOutputStreams.add(outputStream);
		
			} catch (IOException e) {
				app.displayMessage("Error connecting to client: " + e.toString());
			}
		}
	}
	
	public void initializeServer() throws IOException {

		clientOutputStreams = new ArrayList<ObjectOutputStream>();
		
		if(serverSocket == null & port > 0) {
			//TODO better validation
			serverSocket = new ServerSocket(port);
			
		}
		
		this.stopServer = false;
	}
	
	public class Messenger implements Runnable {
		Server server;
		public Messenger(Server server) {
			this.server = server;
		}
		public void run() {
			while(!stopServer) {
				
				csvReader.readLine(data);
				if(clientOutputStreams.size() > 0) {
					
					Iterator<ObjectOutputStream> it = clientOutputStreams.iterator();
					
					while(it.hasNext()) {
						try {
							ObjectOutputStream out = (ObjectOutputStream) it.next();
							out.writeObject(data);
							
							out.flush();
							
							server.app.displayMessage("Sending readings to the client...\n Temperature: " + data.getCurrentTemperature() + "\t Light level: " + data.getCurrentLightLevel());
							
							
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					} // end while
					
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}
	

}
