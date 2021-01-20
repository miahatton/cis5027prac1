package cis5027.project.clients;

import cis5027.project.clients.helpers.AbstractClient;

public class LightClient extends AbstractClient {

	public LightClient(ClientConnectPanel cPanel, int port) {
		
		super(cPanel, port);
		this.clientType = "light";
	}
	
	public void run() {
		
		displayMessage("Client running now.");
		
		Object reading;
		
		try {
			while ((reading = reader.readObject()) != null) {

				switch (clientType) {
				
				case "light":
					displayMessage("Current light level " + reading);
					break;
				case "fan":
					displayMessage("Current temperature " + reading);
					break;
				}
				
				
				writer.writeObject("Reading received: " + reading);

			} // close while
		} catch(Exception ex) {ex.printStackTrace();}
		
	}
	
}
