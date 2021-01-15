package cis5027.project.helpers;

import java.io.IOException;

import javax.swing.JLabel;

import cis5027.project.clients.Client;

public class ClientConnectPanel extends ValueButtonPanel {

	Client client;
	String lastReading;
	JLabel lastReadingLabel;
	
	public ClientConnectPanel(String labelText, String defaultVal, String btnText) {
		
		super(labelText, defaultVal, btnText);
		
		lastReadingLabel = new JLabel("No readings received yet.");
		
	}
	
	@Override
	protected void setButtonActions() {
		// TODO check that client is not already running.
		
		try {
			
			String ip = "127.0.0.1";
			int port = Integer.parseInt(textField.getText());
			
			Client chatClient = null;
			
			// thread to communicate with the server starts here
			try {
				chatClient = new Client(ip, port);	
			} catch (IOException e) {
				System.err.println("[client: ] error in opening the client connection to " + ip + " on " + port);
			}
			
			chatClient.runClient();
			
		} catch (NumberFormatException e) {
			
			// TODO
			// warn user that port number is bad
		}
		
	}

	
	
}
