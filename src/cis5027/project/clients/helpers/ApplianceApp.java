package cis5027.project.clients.helpers;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import cis5027.project.clients.Client;
import cis5027.project.clients.ClientConnectPanel;

public abstract class ApplianceApp extends JFrame {

	public 		Client 				client;
	protected 	ClientConnectPanel 	cPanel;
	protected 	int 				port;
	protected 	String 				ip;
	
	protected 	String 				clientType;
	
	public void go() {
		draw();
		
		cPanel = new ClientConnectPanel("Choose port number: ", "5000", "Start Client", this.clientType);
		cPanel.setApp(this);
		
		getContentPane().add(BorderLayout.SOUTH, cPanel.getContainerPanel());
		pack();
		setVisible(true);
	}
	
	abstract public void draw();
	
}
