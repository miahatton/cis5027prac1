package cis5027.project.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cis5027.project.helpers.PortFormatException;
import cis5027.project.helpers.ScrollingTextBox;
import cis5027.project.server.helpers.DelayFormatException;

/**
 * @author miahatton
 * This class launches the GUI for the server, from which the CSV reader and server itself are launched.
 */


public class ServerApp implements ActionListener {

	// constants
	private static final String DEFAULT_FILE_LOCATION = "inputs/sensor_data.csv";
	private static final int	DEFAULT_PORT = 5000;
	private static final int 	DEFAULT_DELAY = 3000;
	private static final int 	MIN_PORT_NUM = 1024;
	private static final int 	MAX_PORT_NUM = 65535;
	
	// Csv reader and server
	public		Server 				server;
	public		CsvReader 			csvReader;

	// buttons
	private		JButton 			startButton;
	private		JButton 			csvReaderFeedBtn;
	private		JButton				loadButton;
	private 	JButton				stopButton;
	private 	JButton				delayButton;
	
	// Other GUI components
	public 		ScrollingTextBox 	textBox;
	private		JTextField 			fileBox;
	private 	JTextField 			portInput;
	private 	JTextField 			delayInput;
	private 	JFrame 				frame;

	private		int 				delay;
	private 	boolean 			isFileLoaded;
	
	
	/*
	 * Constructor
	 */
	public ServerApp () {

		isFileLoaded = false;
		delay = DEFAULT_DELAY; // default.

	}
	
	/*
	 * Builds the gui and add components to it
	 */
	public void draw() {
		
		// frame with two panels - one with buttons to load csv file and one with scrolling text box
		frame = new JFrame("Server");
		JPanel mainPanel = new JPanel(); 
		JPanel filePanel = new JPanel();
		
		// set up GUI components
		initializeGuiComponents();
		setButtonActions();
		setTextFieldText();

		// add components to top panel
		filePanel.add(fileBox);
		filePanel.add(loadButton);
		filePanel.add(delayInput);
		filePanel.add(delayButton);
		
		// add components to bottom panel
		mainPanel.add(portInput);
		mainPanel.add(startButton);
		mainPanel.add(stopButton);
		mainPanel.add(csvReaderFeedBtn);
		mainPanel.add(textBox.getScrollPane());
		
		// add panels to frame
		frame.getContentPane().add(BorderLayout.NORTH, filePanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		
		// set up window so that server socket and streams are closed if window is closed.
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(server != null) {
                	server.closeAll();
                }
            	System.exit(0);
            }
        });
		
		
		frame.setSize(600,400);
		frame.setVisible(true);
	}

	
	/*
	 *  Initiliases the components that go on the GUI
	 */
	public void initializeGuiComponents() {
		// input fields
		fileBox = new JTextField(20);
		delayInput = new JTextField(5);
		portInput = new JTextField(5);
				
		// buttons
		loadButton = new JButton("Load CSV");
		stopButton = new JButton("Stop Server");
		delayButton = new JButton("Set delay (s)");
		startButton = new JButton("Start Server");
		csvReaderFeedBtn = new JButton("Open CSV feed");
				
		// disable server start button until csv file is loaded
		startButton.setEnabled(false);
				
		// disable csvReader feed until thread has started
		csvReaderFeedBtn.setEnabled(false);
				
		// all system messages and errors will display in here
		textBox = new ScrollingTextBox(15,50);
	}
	
	/*
	 * Sets defaults for text fields.
	 */
	public void setTextFieldText() {
		
		fileBox.setText(DEFAULT_FILE_LOCATION);
		fileBox.setCaretPosition(fileBox.getText().length());
		
		delayInput.setText(String.valueOf(DEFAULT_DELAY));
		delayInput.setCaretPosition(delayInput.getText().length());
		
		portInput.setText(String.valueOf(DEFAULT_PORT));
		portInput.setCaretPosition(portInput.getText().length());
	}
	
	/*
	 * Add action listeners to buttons
	 */
	public void setButtonActions() {
		
		loadButton.addActionListener(new LoadButtonListener());
		startButton.addActionListener(new StartButtonListener());
		delayButton.addActionListener(new DelayButtonListener());
		csvReaderFeedBtn.addActionListener(new csvListener());
		stopButton.addActionListener(this);
		
	}
	
	/*
	 * Inner class with action listener for the load button
	 */
	public class LoadButtonListener implements ActionListener {

		/*
		 * On button click, initialise CSV reader, load file from selected location, and enable server start button.
		 */
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
	
	/*
	 * Adds message to the scrolling text box to update user
	 */
	public void displayMessage(String message) {
		textBox.displayMessage(message);
		textBox.scrollToBottom();
	}
	
	/*
	 * This class implements an action listener for the start server button
	 */
	public class StartButtonListener implements ActionListener {
		
		/*
		 * Initialises server on given port and starts new server thread.
		 */
		public void actionPerformed(ActionEvent e) {
			
			if (isFileLoaded & server == null) {
				
				int port = 0;
				
				try {
					// validate port number input
					String inputPortNumber = portInput.getText().trim();
					
					try {
						port = Integer.parseInt(inputPortNumber);
						
						if (port < MIN_PORT_NUM | port > MAX_PORT_NUM) {
							throw new PortFormatException(inputPortNumber);
						}
						
					} catch(NumberFormatException e1) {
						throw new PortFormatException(inputPortNumber);
					}
	
				} catch (PortFormatException ex) {
					
					showUserErrorDialog("Invalid Input", ex.toString());
					
				}
				
				// initialise server
				
				server = new Server(csvReader, ServerApp.this, port, delay);
				
				// start server thread
				
				Thread serverThread = new Thread(server);
				serverThread.start();
				
			} else if (server != null) {
				displayMessage("Server already running!");
			}
			
			else if (!isFileLoaded) {
				showUserErrorDialog("Please load csv file before starting server", "No file found");
			}

		}
	}
	
	/*
	 * Class that implements action listener for button that sets CSV reader delay in ms
	 */
	public class DelayButtonListener implements ActionListener {
		
		/*
		 * Validate input text and set delay of CSV Reader
		 */
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
					if(csvReader != null) csvReader.setDelay(newDelay*1000); // convert to ms
					displayMessage("Delay set to " + newDelay + " seconds.");
				} else {
					throw new DelayFormatException(delayString);
				}

			} catch (DelayFormatException ex2) {
				showUserErrorDialog("Invalid Input", ex2.toString());
			}

		}
	}
	
	/*
	 * Class that implements action listener for "show CSV feed" button
	 */
	public class csvListener implements ActionListener {

		/*
		 *  generates GUI for CSV Reader.
		 */
		public void actionPerformed(ActionEvent e) {
			
			csvReader.draw();
			displayMessage("Load CSV file to continue.");
		}
	}
	
	/*
	 * Tell the server to stop using the "Stop server" button
	 */
	public void actionPerformed (ActionEvent e) {
			
			server.closeAll();
			
			// reset server reference
			server = null;
	}
	
	/*
	 * @return the text from the file location box
	 */
	public String getFileLocation() {
		return fileBox.getText();
	}
	
	/*
	 * Resets the app if the server is closed, so that it can be restarted
	 */
	public void reset() {
		
		startButton.setEnabled(false);
		csvReaderFeedBtn.setEnabled(false);
		
	}
	
	/*
	 * Display message dialog when error is caused by user input, to alert the user.
	 * @param errorType
	 * @param errorMessage
	 */
	public void showUserErrorDialog(String errorType, String errorMessage) {
		JOptionPane.showMessageDialog(frame, errorMessage, errorType, JOptionPane.ERROR_MESSAGE);
	}
	
	/*
	 * Main function
	 * Create instance of server app and display GUI.
	 */
	public static void main(String[] args) {
		
		ServerApp serverApp = new ServerApp();
		serverApp.draw(); 
		
	}

	/*
	 * Getters and setters
	 */
	
	public int getMinPortNum() {
		return MIN_PORT_NUM;
	}

	public int getMaxPortNum() {
		return MAX_PORT_NUM;
	}
	
	/*
	 * Public method to enable the CsvReaderFeedBtn button
	 * @param enable (boolean)
	 */
	public void enableCsvButton(boolean enable) {
		this.csvReaderFeedBtn.setEnabled(enable);
	}

}
