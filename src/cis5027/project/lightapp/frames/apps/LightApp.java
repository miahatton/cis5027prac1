package cis5027.project.lightapp.frames.apps;


import java.awt.BorderLayout;

import javax.swing.JFrame;

import cis5027.project.lightapp.components.BrightnessAdjustPanel;
import cis5027.project.lightapp.components.BrightnessPanel;
import cis5027.project.lightapp.components.LightPanel;


public class LightApp extends JFrame {
	
	public void go() {
		
		// create JFrame
		JFrame frame = new JFrame();
		frame.setSize(400,500);
				
		// exit program when frame closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// initialise light panel
		LightPanel lPanel = new LightPanel(400, 380);
		
		// initialise brightness panel
		BrightnessPanel bPanel = new BrightnessPanel(lPanel.getLightInstance());
		
		// initialise brightness adjustment panel
		BrightnessAdjustPanel aPanel = new BrightnessAdjustPanel(lPanel.getLightInstance(), bPanel);
		
		// add panels to frame
		frame.getContentPane().add(BorderLayout.CENTER, lPanel);
		frame.getContentPane().add(BorderLayout.NORTH, bPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, aPanel);
		
		// set visibility
		frame.setVisible(true);
		
	}
	
	// main
	public static void main(String[] args) {
		
		LightApp gui = new LightApp();
		gui.go();
		
	}
}