package cis5027.project.clients;

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

				writer.writeObject("Reading received: " + msg);
				
				try {
					double newTemperature = Double.parseDouble(msg);

					displayMessage("Current temperature " + newTemperature);
					
					// set new light value
					this.speedPanelInstance.convertReading(newTemperature);
				} catch (NumberFormatException e) {
					displayMessage("Unusual reading received ("+ msg+"), cannot be converted to double: " + e.toString());
				}

			} // close while
		} catch(Exception ex) {ex.printStackTrace();}
		
	}
}
