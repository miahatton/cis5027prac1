package cis5027.project.clients.lightapp;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import cis5027.project.clients.helpers.AbstractClient;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.helpers.AbstractClientConnectPanel;
import cis5027.project.clients.lightapp.components.BrightnessPanel;
import cis5027.project.clients.lightapp.frames.apps.LightApp;

/**
 * @author miahatton
 * Light client that implements run() method. Receives light level values from server.
 */

public class LightClient extends AbstractClient {

	private BrightnessPanel brightnessPanelInstance;
	private LightApp app;
	
	/**
	 * Constructor
	 * @param cPanel - client connect panel where messages are displayed
	 * @param port - port number for socket.
	 * @param app - ApplianceApp for this client.
	 */
	public LightClient(AbstractClientConnectPanel cPanel, int port, ApplianceApp app) {
		
		super(cPanel, port);
		this.clientType = "light";
		this.app = (LightApp) app;
		this.brightnessPanelInstance = this.app.getBrightnessPanelInstance();
	}
	
	@Override
	public void run() {
		
		String msg;
		
		try {
			
			// loop while socket and IO streams are connected
			while(!stopClient) {
				
				while ((msg = String.valueOf(reader.readObject())) != null) {

					// return the message to the Server to confirm receipt.
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
			
		} catch(SocketException | EOFException e) {
			
			displayMessage("Server connection closed.");
			closeAll();

		} catch(IOException e) {
			
			displayMessage("Error receiving message from server: " + e.toString());
			closeAll();

		} catch (ClassNotFoundException e) {
			displayMessage("Unusual reading received! Class cannot be found: " + e.toString());	
		}
		
	}
	
}
