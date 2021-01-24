package cis5027.project.clients;

import java.io.IOException;

import cis5027.project.clients.fanapp.components.SpeedPanel;
import cis5027.project.clients.fanapp.frames.apps.FanApp;
import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;

public class FanClient extends AbstractClient {
	private SpeedPanel speedPanelInstance;
	private FanApp app;
	
	public FanClient(ClientConnectPanel cPanel, int port, ApplianceApp app) {
		
		super(cPanel, port);
		this.clientType = "fan";
		this.app = (FanApp) app;
		this.speedPanelInstance = this.app.getSpeedPanelInstance();
	}
	
	public void run() {

		String msg;
		
		try {
			while ((msg = String.valueOf(reader.readObject())) != null) {

				sendMessageToServer("Reading received: " + msg);
				
				try {
					double newTemperature = Double.parseDouble(msg);

					displayMessage("Current temperature " + newTemperature);
					
					// set new temperature value
					this.speedPanelInstance.convertReading(newTemperature);
				} catch (NumberFormatException e) {
					displayMessage("Unusual reading received ("+ msg+"), cannot be converted to double: " + e.toString());
				}

			} // close while
		} catch(IOException e1) {
			displayMessage("Error receiving message from server: " + e1.toString());
			closeAll();
			
		} catch (ClassNotFoundException e2) {
			displayMessage("Unusual reading received! Class cannot be found: " + e2.toString());
			sendMessageToServer("Unusual reading");
			//TODO handle this message at the server end.
		}
		
	}
}
