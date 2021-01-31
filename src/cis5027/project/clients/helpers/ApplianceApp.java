package cis5027.project.clients.helpers;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cis5027.project.clients.ClientConnectPanel;

/**
 * @author miahatton
 * Abstract class extended by the LightApp and FanApp classes
 * The class implements a GUI to control the light and fan clients and display the light/fan objects.
 */
public abstract class ApplianceApp extends JFrame {

	public 		AbstractClient 		client;
	protected 	ClientConnectPanel 	cPanel;
	protected 	int 				port;
	protected 	String 				ip;
	
	protected 	String 				clientType;
	
	/*
	 * Calls the draw method to set up GUI and adds a ClientConnectPanel instance to connect to the server.
	 */
	public void go() {
		draw();
		
		cPanel = new ClientConnectPanel("Choose port number: ", "Start Client", this.clientType);
		cPanel.setApp(this);
		
		getContentPane().add(BorderLayout.SOUTH, cPanel.getContainerPanel());
		pack();
		
		// When the window is closed, stop the client.
		this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(client != null) {
                	client.closeAll();
                }
            	System.exit(0);
            }
        });
		
		setVisible(true);
	}
	
	abstract protected void draw();
	
	/*
	 * Allows messages to be displayed to the user
	 * @param msg
	 */
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
