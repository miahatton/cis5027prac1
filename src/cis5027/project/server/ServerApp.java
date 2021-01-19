package cis5027.project.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
import cis5027.project.helpers.SensorData;
import cis5027.project.server.helpers.AbstractServer;

public class ServerApp {

	CsvReader csvReader;
	String defaultFileLoc = "inputs/sensor_data.csv";
	
	ScrollingTextBox textBox;
	JTextField fileBox;
	JTextField portInput;
	JFrame frame;
	
	int delay;

	ArrayList<ObjectOutputStream> clientOutputStreams;
	
	Server server;
	
	private boolean isFileLoaded;
	
	public ServerApp () {

		isFileLoaded = false;
		delay = 3000; // TODO allow this to change.
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
		
		loadButton.addActionListener(new LoadButtonListener(this));
		startButton.addActionListener(new StartButtonListener());
		stopButton.addActionListener(new StopButtonListener());
		
		filePanel.add(fileBox);
		fileBox.setText(defaultFileLoc);
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

	
	public class LoadButtonListener implements ActionListener {
		
		ServerApp app;
		
		public LoadButtonListener(ServerApp app) {
			this.app = app;
		}
		
		public void actionPerformed(ActionEvent e) {

			csvReader = new CsvReader(app, fileBox.getText(), delay);

			//TODO check that file exists.
			
			textBox.displayMessage("Loading csv file from " + csvReader.getFileLocation());
				
			csvReader.loadFile(true);
			
			textBox.displayMessage("Loaded csv.");
			isFileLoaded = true;
		}
	}
	
	public void displayMessage(String message) {
		textBox.displayMessage(message);
	}
	
	public class StartButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if (isFileLoaded & server == null) {
				
				try {
					
					int port = Integer.parseInt(portInput.getText());
					server = new Server(csvReader, ServerApp.this, port, delay);
					
					Thread serverThread = new Thread(server);
					serverThread.start();
					
				} catch (NumberFormatException ex) {
					
					// TODO dialog box? and better validation
					displayMessage("Port must be a number between X and Y");
				}	
				
			} else if (server != null) {
				displayMessage("Server already running!");
			}
			
			else if (!isFileLoaded) {
				displayMessage("Please load csv file before starting server");
			}

		}
	}
	
	public class StopButtonListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			
			// TODO call server stop method.
		}
	}
	
	public String getFileLocation() {
		return fileBox.getText();
	}
	
	public static void main(String[] args) {
		
		ServerApp serverApp = new ServerApp();
		serverApp.go(); 
		
	}
}
