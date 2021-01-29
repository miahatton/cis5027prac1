package cis5027.project.clients.helpers;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cis5027.project.clients.ClientConnectPanel;

public abstract class ApplianceApp extends JFrame {

	public 		AbstractClient 				client;
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
	
	abstract protected void draw();
	
	public void displayMessage(String msg) {
		cPanel.displayMessage(msg);
	}
	
	/*
	 * Display message dialog when error is caused by user input, to alert the user.
	 * @param errorType
	 * @param errorMessage
	 */
	public void showUserErrorDialog(String errorType, String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, errorType, JOptionPane.ERROR_MESSAGE);
	}
	
}
