package cis5027.project.clients;


import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.helpers.PortFormatException;
import cis5027.project.helpers.ScrollingTextBox;


/**
 * @author miahatton
 * ClientConnectPanel class is a JPanel containing buttons to connect to server and output text box.
 */
public class ClientConnectPanel extends ValueButtonPanel {

	String lastReading;
	ScrollingTextBox	clientOutput;
	ApplianceApp app;
	JPanel containerPanel;
	AbstractClient client;
	int port;
	JButton stopButton;
	
	private String clientType;
	
	/*
	 * Constructor
	 * @param labelText
	 * @param defaultVal - in input text field
	 * @param btnText
	 * @param type - the type of client connecting
	 */
	public ClientConnectPanel(String labelText, String defaultVal, String btnText, String type) {
		
		super(labelText, defaultVal, btnText);
		this.clientType = type;
		
		stopButton = new JButton("Stop client");
		stopButton.setEnabled(false); // can't stop before you start!
		
		this.add(stopButton);
		
		clientOutput = new ScrollingTextBox(12,30);
		
		containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		
		containerPanel.add(this);
		containerPanel.add(clientOutput.getScrollPane());
		
		setButtonActions();
	}
	
	@Override
	protected void setButtonActions() {
		button.setActionCommand("START");
		stopButton.setActionCommand("STOP");
		button.addActionListener(this);
		stopButton.addActionListener(this);
	}
	
	/*
	 * Performs a different sequence depending on button clicked.
	 * If START then initialises client and starts client thread.
	 * If STOP then closes connections to the server.
	 */
	public void actionPerformed(ActionEvent e) { 
			
		switch(e.getActionCommand()) {
		
			case "START":
				
				try {
					port = getPortNumber();
				} catch (PortFormatException ex) {
					showUserErrorDialog("Input error", e.toString());
				}
				
				switch (clientType) {
					case "light":
						client = new LightClient(ClientConnectPanel.this, port, app);
						break;
					case "fan":
						client = new FanClient(ClientConnectPanel.this, port, app);
						break;
					default:
						displayMessage("Client type not recognised.");
				}
					
				client.initialiseClient();
				Thread clientThread = new Thread(client);
				clientThread.start();
				stopButton.setEnabled(true);
				button.setEnabled(false); // disable start button
				break;
				
			case "STOP":
				stopButton.setEnabled(false);
				button.setEnabled(true);
				client.setStopClient(true);
				client.closeAll();
				break;
		}
						
	}
	
	/*
	 * Outputs messages to the user via the scrolling text box.
	 */
	public void displayMessage(String msg) {
		clientOutput.displayMessage(msg);
		clientOutput.scrollToBottom();
	}
	
	/*
	 * Calls the app's showUserErrorDialog method, making it available to the client
	 * @param errorType
	 * @param msg
	 */
	
	public void showUserErrorDialog(String errorType, String msg) {
		app.showUserErrorDialog(errorType, msg);
	}

	/*
	 *  Getters and setters
	 */
	
	public JPanel getContainerPanel() {
		return this.containerPanel;
	}
	
	public void setApp(ApplianceApp app) {
		this.app = app;
	}
	
	/*
	 * Gets the port number from the input box and performs validation.
	 */
	public int getPortNumber() throws PortFormatException {
		int port;
		try {
			port = Integer.parseInt(textField.getText());
			
			if (port <1024 | port > 65535) {
				throw new PortFormatException(port);
			}
			else {
				return port;
			}
			
		} catch (NumberFormatException e) {
			throw new PortFormatException(textField.getText());
		} 
		
	}
	
}
