package cis5027.project.clients.lightapp.frames.apps;


import java.awt.BorderLayout;

import cis5027.project.clients.helpers.ApplianceApp;
import cis5027.project.clients.lightapp.components.BrightnessPanel;
import cis5027.project.clients.lightapp.components.Light;
import cis5027.project.clients.lightapp.components.LightClientConnectPanel;
import cis5027.project.clients.lightapp.components.LightPanel;


/**
 * @author miahatton
 * The LightApp is the frame that holds the light components:
 * 		* Brightness Panel
 * 		* Brightness Adjust Panel
 * 		* Light Panel
 * 		* ClientConnectPanel
 * Main method initialises the frame and draws the components.
 */

public class LightApp extends ApplianceApp {
	
	private static LightApp		lightUiInstance;
	
	private	LightPanel 				lPanel;
	private	BrightnessPanel 		bPanel;
	private	Light 					lightInstance;
	
	
	
	/*
	 * Constructor
	 */
	public LightApp() {
		this.clientType = "light";
	}
	
	/*
	 * Adds the components to the frame
	 */
	protected void draw() {
		
		setSize(400,500);
		
		initialiseAppliancePanel();
		
		lightInstance = (Light) lPanel.getApplianceInstance();
				
		initialiseValueButtonPanel();
		
		initialiseCPanel();
	}
	
	/**
	 * Inititalise the cPanel as a LightClientConnectPanel
	 */
	protected void initialiseCPanel() {
		cPanel = new LightClientConnectPanel("Choose port number: ", "Start Client");
		cPanel.setApp(this);
		
		getContentPane().add(BorderLayout.SOUTH, cPanel.getContainerPanel());
	}
	
	/**
	 * Initialise the lPanel as a LightPanel
	 */
	protected void initialiseAppliancePanel() {
		// initialise light panel
		lPanel = new LightPanel(400, 380);
		getContentPane().add(BorderLayout.CENTER, lPanel);
	}
	
	/**
	 * Initialise the value button panel as a BrightnessPanel	
	 */
	protected void initialiseValueButtonPanel() {
		// initialise brightness panel
		bPanel = new BrightnessPanel(lightInstance, "Set brightness %", "100", "Set Brightness");
		bPanel.setLightApp(this);

		// add light panel to top of JFrame content pane
		getContentPane().add(BorderLayout.NORTH, bPanel);
	}
	
	/*
	 * Main method initialises LightApp and calls the superclass go() method (which calls the draw() method)
	 */
	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {			
				LightApp.getUiInstance().go();				
			}
		} );

	}

	/*
	 * Get the current LightApp instance
	 */
	public static LightApp getUiInstance() {


		if(lightUiInstance == null) {
			lightUiInstance = new LightApp();
		}
		
		return lightUiInstance;
		
	}
	
	/*
	 * Getter for BrightnessPanel
	 */
	public BrightnessPanel getBrightnessPanelInstance() {
		return this.bPanel;
	}
}