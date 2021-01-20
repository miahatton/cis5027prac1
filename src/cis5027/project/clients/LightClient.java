package cis5027.project.clients;

import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.lightapp.components.Light;
import cis5027.project.clients.lightapp.frames.apps.LightApp;

public class LightClient extends AbstractClient {

	private Light lightInstance;
	private LightApp app;
	
	public LightClient(ClientConnectPanel cPanel, int port, ApplianceApp app) {
		
		super(cPanel, port);
		this.clientType = "light";
		this.app = (LightApp) app;
		this.lightInstance = this.app.getLightInstance();
	}
	
	public void run() {
		
		displayMessage("Client running now.");
		
		String msg;
		
		try {
			while ((msg = String.valueOf(reader.readObject())) != null) {

				writer.writeObject("Reading received: " + msg);
				
				try {
					int newLightLevel = Integer.parseInt(msg);

					displayMessage("Current light level " + newLightLevel);
					
					// set new light value
					this.lightInstance.convertReading(newLightLevel);
				} catch (NumberFormatException e) {
					displayMessage("Unusual reading received ("+ msg+"), cannot be converted to integer: " + e.toString());
				}
				
				
				
				

			} // close while
		} catch(Exception ex) {ex.printStackTrace();}
		
	}
	
}
