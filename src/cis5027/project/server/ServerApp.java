package cis5027.project.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.helpers.PortFormatException;
import cis5027.project.helpers.ScrollingTextBox;
import cis5027.project.server.helpers.DelayFormatException;

public class ServerApp implements ActionListener {

	CsvReader csvReader;
	String defaultFileLoc = "inputs/sensor_data.csv";
	
	ScrollingTextBox textBox;
	JTextField fileBox;
	JTextField portInput;
	JTextField delayInput;
	JFrame frame;
	
	JButton startButton;
	JButton csvReaderFeedBtn;
	
	private static final int DEFAULT_DELAY = 3000;
	private static final int MIN_PORT_NUM = 1024;
	private static final int MAX_PORT_NUM = 65535;
	
	int delay;

	ArrayList<ObjectOutputStream> clientOutputStreams;
	
	Server server;
	
	private boolean isFileLoaded;
	
	public ServerApp () {

		isFileLoaded = false;
		delay = DEFAULT_DELAY; // default.

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
		delayInput = new JTextField(5);
		portInput = new JTextField(5);
		
		JButton loadButton = new JButton("Load CSV");
		JButton stopButton = new JButton("Stop Server");
		JButton delayButton = new JButton("Set delay (ms)");
		startButton = new JButton("Start Server");
		csvReaderFeedBtn = new JButton("Open CSV feed");
		
		startButton.setEnabled(false);
		csvReaderFeedBtn.setEnabled(false);
		
		textBox = new ScrollingTextBox(15,50);
		
		loadButton.addActionListener(new LoadButtonListener());
		startButton.addActionListener(new StartButtonListener());
		delayButton.addActionListener(new DelayButtonListener());
		csvReaderFeedBtn.addActionListener(new csvListener());
		stopButton.addActionListener(this);
		
		filePanel.add(fileBox);
		fileBox.setText(defaultFileLoc);
		fileBox.setCaretPosition(fileBox.getText().length());
		filePanel.add(loadButton);
		filePanel.add(delayInput);
		delayInput.setText(String.valueOf(DEFAULT_DELAY));
		delayInput.setCaretPosition(delayInput.getText().length());
		filePanel.add(delayButton);
		
		mainPanel.add(portInput);
		portInput.setText("5000");
		portInput.setCaretPosition(portInput.getText().length());
		mainPanel.add(startButton);
		mainPanel.add(stopButton);
		mainPanel.add(csvReaderFeedBtn);
		mainPanel.add(textBox.getScrollPane());
		
		frame.getContentPane().add(BorderLayout.NORTH, filePanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(server != null) {
                	server.closeAll();
                }
            	System.exit(0);
            }
        });
		
		
		frame.setSize(600,400);
		
	}

	
	public class LoadButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			csvReader = new CsvReader(ServerApp.this, fileBox.getText(), delay);

			//TODO check that file exists.
			
			textBox.displayMessage("Loading csv file from " + csvReader.getFileLocation());
				
			csvReader.loadFile(true);
			
			textBox.displayMessage("Loaded csv.");
			isFileLoaded = true;
			
			startButton.setEnabled(true);
		}
	}
	
	public void displayMessage(String message) {
		textBox.displayMessage(message);
		textBox.scrollToBottom();
	}
	
	public class StartButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if (isFileLoaded & server == null) {
				
				int port = 0;
				
				try {
					
					String inputPortNumber = portInput.getText().trim();
					
					try {
						port = Integer.parseInt(inputPortNumber);
						
						if (port < MIN_PORT_NUM | port > MAX_PORT_NUM) {
							throw new PortFormatException(inputPortNumber);
						}
						
					} catch(NumberFormatException e1) {
						throw new PortFormatException(inputPortNumber);
					}
	
					server = new Server(csvReader, ServerApp.this, port, delay);
					
					Thread serverThread = new Thread(server);
					serverThread.start();
					
				} catch (PortFormatException ex) {
					
					JOptionPane.showMessageDialog(frame, ex.toString(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
					
				}	
				
			} else if (server != null) {
				displayMessage("Server already running!");
			}
			
			else if (!isFileLoaded) {
				displayMessage("Please load csv file before starting server");
			}

		}
	}
	
	public class DelayButtonListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {

			String delayString = delayInput.getText().trim();
			int newDelay = -1;
			try {
				
				try {
					// check that the number is entered in correct format
					newDelay = Integer.parseInt(delayString);
					
				} catch (NumberFormatException ex1) {
					
					throw new DelayFormatException(delayString);
					
				} 

				if (newDelay >= 0) { // check that delay is a positive number
					delay = newDelay;
					if(csvReader != null) csvReader.setDelay(newDelay);
					if(server != null) server.setDelay(newDelay);
					displayMessage("Delay set to " + newDelay + "ms.");
				} else {
					throw new DelayFormatException(delayString);
				}

			} catch (DelayFormatException ex2) {
				JOptionPane.showMessageDialog(frame, ex2.toString(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
			}

		}
	}
	
	public class csvListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			csvReader.draw();
		}
	}
	
	public void actionPerformed (ActionEvent e) {
			
			server.closeAll();
			
			// reset server reference
			server = null;
	}
	
	public String getFileLocation() {
		return fileBox.getText();
	}
	
	public static void main(String[] args) {
		
		ServerApp serverApp = new ServerApp();
		serverApp.go(); 
		
	}
}
