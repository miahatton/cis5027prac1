package cis5027.project.clients.lightapp.frames.apps;


import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cis5027.project.clients.ClientConnectPanel;
import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.lightapp.components.BrightnessAdjustPanel;
import cis5027.project.clients.lightapp.components.BrightnessPanel;
import cis5027.project.clients.lightapp.components.Light;
import cis5027.project.clients.lightapp.components.LightPanel;


public class LightApp extends ApplianceApp {
	
	private static LightApp		lightUiInstance;
	
	LightPanel lPanel;
	BrightnessPanel bPanel;
	BrightnessAdjustPanel aPanel;
	Light lightInstance;
	
	public LightApp(String ip) {
		
		this.ip = ip;
	}
	
	public void go() {
		
		setSize(400,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		// initialise light panel
		lPanel = new LightPanel(400, 380);
		getContentPane().add(BorderLayout.CENTER, lPanel);
		
		lightInstance = (Light) lPanel.getAppInstance();
				
		// initialise brightness panel
		bPanel = new BrightnessPanel(lightInstance, "Set brightness %", "100", "Set Brightness");
		
		// initialise brightness adjustment panel
		aPanel = new BrightnessAdjustPanel(lightInstance, bPanel);
		
		// add brightness panels to top panel
		topPanel.add(bPanel);
		topPanel.add(aPanel);
		// add top panel to JFrame content pane
		getContentPane().add(BorderLayout.NORTH, topPanel);
		
		/* TEST */
		cPanel = new ClientConnectPanel("Choose port number: ", "5000", "Start Client", "Light");
		cPanel.setApp(this);
		
		getContentPane().add(BorderLayout.SOUTH, cPanel.getContainerPanel());
		//connectToPanel(cPanel);
		pack();
		setVisible(true);
		
	}
		
	// main
	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {			
				LightApp.getUiInstance().go();				
			}
		} );

	}

	public static LightApp getUiInstance() {


		if(lightUiInstance == null) {
			lightUiInstance = new LightApp("127.0.0.1");
		}
		
		return lightUiInstance;
		
	}
}