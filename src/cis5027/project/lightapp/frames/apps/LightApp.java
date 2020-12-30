package cis5027.project.lightapp.frames.apps;


import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import cis5027.project.lightapp.components.LightPanel;


public class LightApp extends JFrame {
	
	public void go() {
		
		// create JFrame
		JFrame frame = new JFrame();
				
		// exit program when frame closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		LightPanel panel = new LightPanel();
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		
		// set size and visibility
		frame.setSize(400,400);
		frame.setVisible(true);
		
	}
	
	// test
	public static void main(String[] args) {
		
		LightApp gui = new LightApp();
		gui.go();
		
	}
}