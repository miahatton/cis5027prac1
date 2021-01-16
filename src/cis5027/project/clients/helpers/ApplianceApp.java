package cis5027.project.clients.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

import cis5027.project.clients.Client;
import cis5027.project.clients.ClientConnectPanel;

public abstract class ApplianceApp extends JFrame {

	public Client client;
	protected ClientConnectPanel cPanel;
	protected int port;
	protected String ip;
	
	protected Socket socket;
	protected BufferedReader reader;
	protected PrintWriter writer;
	
	
	
	public void setUpNetworking() {
		try {
			
			port = cPanel.getPortNumber();
			
			if (port > 0) {
				
				socket = new Socket(ip, port);
				
				InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
				reader = new BufferedReader(streamReader);
				writer = new PrintWriter(socket.getOutputStream());
				cPanel.display("Networking established");
			}
			
			} catch(IOException ex) {
				
				ex.printStackTrace();
			}
	}
}
