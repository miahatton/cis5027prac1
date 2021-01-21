package cis5027.project.clients;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.helpers.ScrollingTextBox;
import cis5027.project.server.Server;
import cis5027.project.server.ServerApp;


public class ClientConnectPanel extends ValueButtonPanel {

	String lastReading;
	ScrollingTextBox	clientOutput;
	ApplianceApp app;
	JPanel containerPanel;
	AbstractClient client;
	int port;
	JButton stopButton;
	
	private String clientType;
	
	public ClientConnectPanel(String labelText, String defaultVal, String btnText, String type) {
		
		super(labelText, defaultVal, btnText);
		this.clientType = type;
		
		clientOutput = new ScrollingTextBox(12,30);
		
		containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		
		containerPanel.add(this);
		containerPanel.add(clientOutput.getScrollPane());
		
		setButtonActions();
	}
	
	public JPanel getContainerPanel() {
		return this.containerPanel;
	}
	
	public void setApp(ApplianceApp app) {
		this.app = app;
	}
	
	public int getPortNumber() {
		try {
			
			return Integer.parseInt(textField.getText());
		} catch (NumberFormatException e) {
			displayMessage("Port must be a number");
			return 0;
		}
		
	}
	
	@Override
	protected void setButtonActions() {
		button.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) { 
		
		port = getPortNumber();
		
		if (port > 0) {
			
			switch (clientType) {
			case "light":
				client = new LightClient(this, port, app);
				break;
			case "fan":
				client = new FanClient(this, port, app);
				break;
			default:
				//TODO obviously improve this.
				app.displayMessage("Something has gone terribly wrong");
			}
			
			client.initialiseClient();
			
			Thread clientThread = new Thread(client);
			clientThread.start();
			//TODO check what X and Y are hahahaha
		} else displayMessage("Port must be between X and Y");
		
	}
	
	public void displayMessage(String msg) {
		clientOutput.displayMessage(msg);
		
	}
	
}
