package cis5027.project.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {

private Socket				clientSocket;
	
	private ObjectOutputStream	output;
	private ObjectInputStream	input;
	
	private boolean				stopClient;
	private Thread				clientReader;
	
	private String				host;
	private int					port;
	
	
	/*
	 * Constructor
	 */
	public Client(String host, int port) throws IOException {
		
		this.host = host;
		this.port = port;
		
		openConnection();
		
	}

	/*
	 * Open a connection to the server and setup IO streams for socket.
	 */
	private void openConnection() throws IOException {
		
		// Create sockets and data streams
		try {
			
			this.clientSocket = new Socket(this.host, this.port);
			this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
			this.input = new ObjectInputStream(this.clientSocket.getInputStream());
			
		} catch (IOException e) {
			try {
				closeAll();
			} catch (IOException ex) {
				System.err.println("[client: ] Error in opening a connection to " + this.host + " on " + this.port);
			}
			throw e; // rethrow the exception
		}
		
		// create a thread instance and start the thread
		this.clientReader = new Thread(this);
		this.stopClient = false;
		this.clientReader.start();
		
	}
	
	/*
	 * Handles sending messages to the server
	 * @param msg
	 * @throws IOException
	 */
	public void sendMessageToServer(String msg) throws IOException {
		
		if (this.clientSocket == null || this.output == null) throw new SocketException	("socket does not exist");
		
		this.output.writeObject(msg);
		
	}
	
	/*
	 * Handle messages from the server
	 * @param msg
	 */
	public void handleMessageFromServer (String msg) {
		display(msg);
	}


	private void display(String msg) {
		System.out.println(">> " + msg);
		
	}
	
	/*
	 * Close all connections
	 * @throws IOException
	 */
	private void closeAll() throws IOException {
		
		try {
			
			// close the socket
			if(this.clientSocket != null) clientSocket.close();
			
			// close the output stream
			if(this.output != null) output.close();
			
			// close the input stream
			if(this.input != null) input.close();
			
		} finally {
			
			// set the streams and sockets to null no matter what
			this.clientSocket = null;
			this.output = null;
			this.input = null;
			
		}
	}
	
	/*
	 * handle user inputs from the terminal
	 * Should run as a separate thread (main thread)
	 */
	public void runClient() {
		
		try {
			
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			
			String message = null;
			
			while(true) {
				message = fromConsole.readLine();
				handleUserInput(message);
				if(message.equals("over")) break;
			}
			
			try {
				closeAll();
			} catch (IOException e) {
				System.err.println("[client: ] error closing the client connections..." + e.toString());
			}
			
			System.out.println("[client: ] stopping client...");
			this.stopClient = true;
			fromConsole.close();
			//closeAll();
		} catch (Exception ex) {
			System.out.println("[client: ] unexpected error while reading from console..." + ex.toString());
		}
		
	}

	/*
	 * Perform checking/preprocessing of user input before sending to the server
	 * @param user input
	 */
	private void handleUserInput(String userResponse) {

		if (!this.stopClient) {
			
			try {
				sendMessageToServer(userResponse);
			} catch (IOException e) {
				System.err.println("[client: ] error sending message to server... " + e.toString());
			}
			
		}
	}
		
	
	/*
	* Thread that communicates with server.
	* Receives message from the server, passes it to handleMessageFromServer()
	*/
	
	@Override
	public void run() {
		
		String msg;
		
		// Loop waiting for data
		
		try {
			
			while(!this.stopClient) {
				// Get data from the server and send it to the handler
				// The thread waits indefinitely at this statement until something is received from the server.
				msg = (String) input.readObject();
				
				// concrete subclasses do what they want with the msg by implementing the following method
				handleMessageFromServer(msg);
			}
			
			System.out.println("[client: ] client stopped...");
		
		} catch (Exception e) {
			if(!this.stopClient) {
				
				try {
					closeAll();
				} catch (Exception ex) {
					System.err.println("[client: ] error in closing the client connection...");
				} 
				
			} 
		} finally {
			clientReader = null;
		}
		
		System.out.println("[client: ] exiting thread...");
	}
	
	/*
	 * main() to initiate the client
	 * @param args
	 */
	
	
	
	public void go() {

		// hardcoded server IP 
		String ip = "127.0.0.1";
		
		Client chatClient = null;
		
		// thread to communicate with the server starts here
		try {
			chatClient = new Client(ip, port);	
		} catch (IOException e) {
			System.err.println("[client: ] error in opening the client connection to " + ip + " on " + port);
		}
		
		chatClient.runClient();
	}


	public void setPort(int portNumber) throws NumberFormatException {
		this.port = portNumber;
		
	}
	
	
}
