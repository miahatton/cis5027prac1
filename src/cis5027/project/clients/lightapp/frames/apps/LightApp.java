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
		
		// initialise light panel
		lPanel = new LightPanel(400, 380);
		getContentPane().add(BorderLayout.CENTER, lPanel);
		
		lightInstance = (Light) lPanel.getApplianceInstance();
				
		// initialise brightness panel
		bPanel = new BrightnessPanel(lightInstance, "Set brightness %", "100", "Set Brightness");
		bPanel.setLightApp(this);

		// add light panel to top of JFrame content pane
		getContentPane().add(BorderLayout.NORTH, bPanel);
		
		initialiseCPanel();
		
		getContentPane().add(BorderLayout.SOUTH, cPanel.getContainerPanel());
	}
	
	/**
	 * Inititalise the cPanel as a FanClientConnectPanel
	 */
	protected void initialiseCPanel() {
		cPanel = new LightClientConnectPanel("Choose port number: ", "Start Client");
		cPanel.setApp(this);
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