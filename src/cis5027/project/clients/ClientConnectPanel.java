package cis5027.project.clients;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.helpers.ScrollingTextBox;


public class ClientConnectPanel extends ValueButtonPanel {

	String lastReading;
	ScrollingTextBox	clientOutput;
	ApplianceApp app;
	JPanel containerPanel;
	Client client;
	int port;
	
	private String clientType;
	
	public ClientConnectPanel(String labelText, String defaultVal, String btnText, String type) {
		
		super(labelText, defaultVal, btnText);
		this.clientType = type;
		
		clientOutput = new ScrollingTextBox(12,30);
		
		containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		
		containerPanel.add(this);
		containerPanel.add(clientOutput.getScrollPane());
		
		this.client = null;
		
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
			clientOutput.displayMessage("Port must be a number");
			return 0;
		}
		
	}
	
	@Override
	protected void setButtonActions() {
		button.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) { 
		
		clientOutput.displayMessage("Button clicked!");
		
	}
	
	public void display(String msg) {
		clientOutput.displayMessage(msg);
	}
	
}
