/**
 * @author miahatton 
 *
 */
package cis5027.project.clients.fanapp.components;

import java.awt.event.ActionEvent;

import cis5027.project.clients.fanapp.FanClient;
import cis5027.project.clients.helpers.AbstractClientConnectPanel;
import cis5027.project.clients.helpers.PortFormatException;
import cis5027.project.clients.lightapp.LightClient;

/**
 * @author miahatton
 *
 */
public class FanClientConnectPanel extends AbstractClientConnectPanel {

	
	public FanClientConnectPanel(String labelText, String btnText) {
		super(labelText, btnText);
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
				
				client = new FanClient(this, port, app);
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
}

