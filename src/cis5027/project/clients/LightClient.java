package cis5027.project.clients;

import java.io.IOException;

import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.lightapp.components.BrightnessPanel;
import cis5027.project.clients.lightapp.frames.apps.LightApp;

public class LightClient extends AbstractClient {

	private BrightnessPanel brightnessPanelInstance;
	private LightApp app;
	
	public LightClient(ClientConnectPanel cPanel, int port, ApplianceApp app) {
		
		super(cPanel, port);
		this.clientType = "light";
		this.app = (LightApp) app;
		this.brightnessPanelInstance = this.app.getBrightnessPanelInstance();
	}
	
	public void run() {
		
		String msg;
		
		try {
			
			while(!stopClient) {
				
				while ((msg = String.valueOf(reader.readObject())) != null) {

					sendMessageToServer("Reading received: " + msg);
					
					try {
						int newLightLevel = Integer.parseInt(msg);

						displayMessage("Current light level " + newLightLevel);
						
						// set new light value
						this.brightnessPanelInstance.convertReading(newLightLevel);
					} catch (NumberFormatException e) {
						displayMessage("Unusual reading received ("+ msg+"), cannot be converted to integer: " + e.toString());
					}

				} // close while
				
			}
			
		} catch(IOException e) {
			
			displayMessage("Error receiving message from client: " + e.toString());
			closeAll();

		} catch (ClassNotFoundException e) {
			displayMessage("Unusual reading received!");
			sendMessageToServer("Unusual reading");
			//TODO handle this message at the server end.
		}
		
	}
	
}
