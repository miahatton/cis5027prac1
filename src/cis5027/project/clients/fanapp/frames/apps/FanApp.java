package cis5027.project.clients.fanapp.frames.apps;


import java.awt.BorderLayout;

import cis5027.project.clients.fanapp.components.Fan;
import cis5027.project.clients.fanapp.components.FanClientConnectPanel;
import cis5027.project.clients.fanapp.components.FanPanel;
import cis5027.project.clients.fanapp.components.SpeedPanel;
import cis5027.project.clients.helpers.ApplianceApp;


/**
 * @author thanu
 * @author miahatton
 * Adapted from FanApp class provided by Thanuja Mallikarachchi (Cardiff Metropolitan University)
 * The FanApp is the frame that holds the light components:
 * 		* Speed Panel
 * 		* Fan Panel
 * 		* ClientConnectPanel 
 */
public class FanApp extends ApplianceApp {
	
	// static instance to support singleton
	private static FanApp	fanUiInstance;
	
	private FanPanel 		fanPanel;
	private SpeedPanel		speedPanel;
	private Fan				fanInstance;
	
	/*
	 * Main method initialises FanApp and calls superclass go() method
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {			
				FanApp.getUiInstance().go();				
			}
		} );
	}
	
	/**
	 * Public getinstance method to create an instance of the AppFrame class. 
	 *  
	 * @return an instance of AppFrame class. 
	 */
	public static FanApp getUiInstance() {
		if(fanUiInstance == null) {
			fanUiInstance = new FanApp();
		}
		
		return fanUiInstance;
	}
	
	/**
	 * Constructor
	 */
	public FanApp() {
		
		this.clientType = "fan";
	}
	
	
	/*
	 * Adds components to the frame
	 */
	protected void draw() {
		
		initialiseAppliancePanel();
		fanInstance = (Fan) fanPanel.getApplianceInstance();
		
		initialiseValueButtonPanel();
		
		initialiseCPanel();
		
	}

	/**
	 * Inititalise the cPanel as a FanClientConnectPanel
	 */
	protected void initialiseCPanel() {
		cPanel = new FanClientConnectPanel("Choose port number: ", "Start Client");
		cPanel.setApp(this);
		getContentPane().add(BorderLayout.SOUTH, cPanel.getContainerPanel());
	}
	
	/**
	 * Initialise the appliance panel as a fan panel
	 */
	protected void initialiseAppliancePanel() {
		fanPanel = new FanPanel(300, 300); // size of panel
		add(fanPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Initialise the value button panel as a speed panel
	 */
	protected void initialiseValueButtonPanel() {
		speedPanel = new SpeedPanel(fanInstance, "Fan speed (delay in ms): ", "10", "Set speed");
		speedPanel.setFanAppInstance(this);		
		
		add(speedPanel, BorderLayout.NORTH);
	}
	
	/*
	 * Getter for speed_panel
	 */
	public SpeedPanel getSpeedPanelInstance() {
		return this.speedPanel;
	}
}