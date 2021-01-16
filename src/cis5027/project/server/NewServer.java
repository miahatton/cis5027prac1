package cis5027.project.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.csvreader.CsvReader;
import cis5027.project.helpers.ScrollingTextBox;
import cis5027.project.server.helpers.AbstractServer;

public class NewServer extends AbstractServer {

	CsvReader csvReader;
	String defaultFileLoc = "inputs/sensor_data.csv";
	
	ScrollingTextBox textBox;
	JTextField fileBox;
	JTextField portInput;
	JFrame frame;
	
	private ThreadGroup		clientThreadGroup;
	private BufferedReader	reader;
	
	public NewServer () {
		
		csvReader = new CsvReader(defaultFileLoc);
	}
	
	public void go() {
		
		draw();
		
		frame.setVisible(true);
		}
	
	public void draw() {
		
		frame = new JFrame("Server");
		JPanel mainPanel = new JPanel(); 
		JPanel filePanel = new JPanel();
		
		fileBox = new JTextField(20);
		portInput = new JTextField(5);
		
		JButton loadButton = new JButton("Load CSV");
		JButton startButton = new JButton("Start Server");
		JButton stopButton = new JButton("Stop Server");
		
		textBox = new ScrollingTextBox(15,50);
		
		loadButton.addActionListener(new LoadButtonListener());
		startButton.addActionListener(new StartButtonListener());
		stopButton.addActionListener(new StopButtonListener());
		
		filePanel.add(fileBox);
		fileBox.setText(csvReader.getFileLocation());
		fileBox.setCaretPosition(fileBox.getText().length());
		filePanel.add(loadButton);
		
		mainPanel.add(portInput);
		portInput.setText("5000");
		portInput.setCaretPosition(portInput.getText().length());
		mainPanel.add(startButton);
		mainPanel.add(stopButton);
		mainPanel.add(textBox.getScrollPane());
		
		frame.getContentPane().add(BorderLayout.NORTH, filePanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,400);
		
	}
	
	@Override
	public void run() {
		
		System.out.println("[server: ] starting server. Listening @ port " + port);
		
		
		
		// increments when client connects.
		int clientCount = 0;
		
		// loops until stopserver flag is set to true.
		while (!this.stopServer) {
			
			String clientType = null;
			Socket clientSocket = null;
			
			try {
				clientSocket = serverSocket.accept();
				InputStreamReader input = new InputStreamReader(clientSocket.getInputStream());
				reader = new BufferedReader(input);
				
				clientType = reader.readLine();

			} catch (IOException e) {
				System.err.println("[server: ] Error when handling client connections on port " + port);
			}
			
			// ClientHandler ch = new ClientHandler(this.clientThreadGroup, clientSocket, clientCount, this);
			
			new ClientHandler(this.clientThreadGroup, clientSocket, clientCount, this, clientType);
			
			try {
				
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				System.err.println("[server: ] server listener thread interrupted..." + e.toString());
			}
			
			clientCount++;
			
			textBox.displayMessage("Connected to client of type " + clientType);
		}
	}
	
	@Override
	public void handleMessagesFromClient(String msg, ClientHandler client) {
		// TODO Auto-generated method stub
		
		/*
		 * When the client connects it should somehow send its name
		 * The server should note what kind of client it is
		 * 
		 */
	}

	@Override
	public void sendMessageToClient(String msg, ClientHandler client) {

		/*
		 * The server should send temperature readings to the temp client and light readings to the light client
		 */
		
	}
	
	
	public class LoadButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if (!fileBox.getText().equals(csvReader.getFileLocation())) {
				csvReader.setFileLocation(fileBox.getText());
			}
			
			//TODO check that file exists.
			
			textBox.displayMessage("Loading csv file from " + csvReader.getFileLocation());
			
		}
	}
	
	public class StartButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			textBox.displayMessage("Starting server on port " + portInput.getText());
			//TODO call server start method
			try {
				
				initializeServer();
			} catch (IOException ex) {
				textBox.displayMessage("Error initiating server");
			}
			
		}
	}
	
	public class StopButtonListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			
			// TODO call server stop method.
		}
	}
	
	public void initializeServer() throws IOException {
		
		try {
			this.port = Integer.parseInt(portInput.getText());
		} catch (NumberFormatException e) {
			
			// TODO dialog box?
			textBox.displayMessage("Port must be a number between X and Y");
		}
		
		
		if(serverSocket == null) {
			
			serverSocket = new ServerSocket(port);
			
		}
		
		stopServer = false;
		serverListenerThread = new Thread(this);
		serverListenerThread.start();
		
	}
	
	public static void main(String[] args) {
		
		new NewServer().go(); 
		
	}
}
