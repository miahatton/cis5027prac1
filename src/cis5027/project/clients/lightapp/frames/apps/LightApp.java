package cis5027.project.clients.lightapp.frames.apps;


import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cis5027.project.clients.Client;
import cis5027.project.clients.lightapp.components.BrightnessAdjustPanel;
import cis5027.project.clients.lightapp.components.BrightnessPanel;
import cis5027.project.clients.lightapp.components.Light;
import cis5027.project.clients.lightapp.components.LightPanel;
import cis5027.project.helpers.ApplianceApp;
import cis5027.project.helpers.ClientConnectPanel;


public class LightApp extends ApplianceApp {
	
	private static LightApp		lightUiInstance;
	
	LightPanel lPanel;
	BrightnessPanel bPanel;
	BrightnessAdjustPanel aPanel;
	Light lightInstance;
	
	Client client;
	
	public LightApp() {
		
		super();
		
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
		JPanel cPanel = new ClientConnectPanel("Choose port number: ", "5000", "Start Client");
		getContentPane().add(BorderLayout.SOUTH, cPanel);
		
		pack();
		setVisible(true);
	}
	
	// main
	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {			
				LightApp.getUiInstance();				
			}
		} );
		
	}

	public static LightApp getUiInstance() {


		if(lightUiInstance == null) {
			lightUiInstance = new LightApp();
		}
		
		return lightUiInstance;
		
	}
}