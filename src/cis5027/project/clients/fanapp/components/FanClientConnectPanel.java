/**
 * @author miahatton 
 *
 */
package cis5027.project.clients.fanapp.components;

import java.net.ConnectException;

import cis5027.project.clients.fanapp.FanClient;
import cis5027.project.clients.helpers.AbstractClientConnectPanel;
import cis5027.project.clients.helpers.PortFormatException;

/**
 * @author miahatton
 * FanClientConnectPanel class is a JPanel containing buttons to connect to server and output text box.
 * It implements the actionPerformed method of the abstract class which implements ActionListener/
 */
public class FanClientConnectPanel extends AbstractClientConnectPanel {

	
	public FanClientConnectPanel(String labelText, String btnText) {
		super(labelText, btnText);
	}
	
	protected void startClient() {
		try {
			port = getPortNumber();
			
			client = new FanClient(this, port, app);
			client.initialiseClient();
			Thread clientThread = new Thread(client);
			clientThread.start();
			stopButton.setEnabled(true);
			button.setEnabled(false); // disable start button
			
		} catch (PortFormatException ex) {
			showUserErrorDialog("Input error", ex.toString());
		} catch (ConnectException ex2) {
			displayMessage("Nothing to connect to! Please check the port number and ensure server is connected.");
		}
	}
	
}

