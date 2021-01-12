package cis5027.project.lightapp.frames.apps;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import cis5027.project.lightapp.components.BrightnessAdjustPanel;
import cis5027.project.lightapp.components.BrightnessPanel;
import cis5027.project.lightapp.components.Light;
import cis5027.project.lightapp.components.LightPanel;


public class LightApp extends JFrame {
	
	private static LightApp		lightUiInstance;
	
	LightPanel lPanel;
	BrightnessPanel bPanel;
	BrightnessAdjustPanel aPanel;
	Light lightInstance;
	
	public LightApp() {
		
		super();
		
		setSize(400,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// initialise light panel
		lPanel = new LightPanel(400, 380);
		getContentPane().add(BorderLayout.CENTER, lPanel);
		
		lightInstance = (Light) lPanel.getAppInstance();
				
		// initialise brightness panel
		bPanel = new BrightnessPanel(lightInstance, "Set brightness %", "100", "Set Brightness");
		getContentPane().add(BorderLayout.NORTH, bPanel);
				
		// initialise brightness adjustment panel
		aPanel = new BrightnessAdjustPanel(lightInstance, bPanel);
		getContentPane().add(BorderLayout.SOUTH, aPanel);
		
		pack();
		setVisible(true);
	}
	
	// main
	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {			
				LightApp.getLightUiInstance();				
			}
		} );
		
	}

	public static LightApp getLightUiInstance() {


		if(lightUiInstance == null) {
			lightUiInstance = new LightApp();
		}
		
		return lightUiInstance;
		
	}
}