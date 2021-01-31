package cis5027.project.clients.fanapp;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import cis5027.project.clients.fanapp.components.SpeedPanel;
import cis5027.project.clients.fanapp.frames.apps.FanApp;
import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.helpers.AbstractClientConnectPanel;

public class FanClient extends AbstractClient {
	private SpeedPanel speedPanelInstance;
	private FanApp app;
	
	public FanClient(AbstractClientConnectPanel cPanel, int port, ApplianceApp app) {
		
		super(cPanel, port);
		this.clientType = "fan";
		this.app = (FanApp) app;
		this.speedPanelInstance = this.app.getSpeedPanelInstance();
	}
	
	public void run() {

		String msg;
		
		try {
			
			while(!stopClient) {
				
				while ((msg = String.valueOf(reader.readObject())) != null) {

					sendMessageToServer("Reading received: " + msg);
					
					try {
						double newTemperature = Double.parseDouble(msg);

						displayMessage("Current temperature " + newTemperature);
						
						// set new temperature value
						this.speedPanelInstance.convertReading(newTemperature);
					} catch (NumberFormatException e) {
						displayMessage("Unusual reading received (" + msg + "), cannot be converted to double: " + e.toString());
					}

				} // close while
				
			} // close while
				
		} catch(SocketException | EOFException e1) {
			displayMessage("Server connection closed.");
			closeAll();
			
		}  catch(IOException e2) {
			displayMessage("Error receiving message from server: " + e2.toString());
			closeAll();
			
		} catch (ClassNotFoundException e3) {
			displayMessage("Unusual reading received! Class cannot be found: " + e3.toString());
			try {
				sendMessageToServer("Unusual reading");
				//TODO handle this message at the server end.
			} catch (IOException ex) {
				displayMessage("Error sending message to server.");
				closeAll();
			}
		}
		
	}
}
