package cis5027.project.fanapp.frames.apps;


import java.awt.BorderLayout;
import javax.swing.JFrame;

import cis5027.project.fanapp.components.Fan;
import cis5027.project.fanapp.components.FanPanel;
import cis5027.project.fanapp.components.SpeedPanel;


public class FanApp extends JFrame {
	
	// static instance to support singleton
	private static FanApp	fanui_instance;
	
	private FanPanel 		fan_panel;
	private SpeedPanel		speed_panel;
	private Fan				fan_instance;
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {			
				FanApp.getFanUIInstance();				
			}
		} );
	}
	
	/**
	 * Public getinstance method to create an instance of the AppFrame class. 
	 *  
	 * @return an instance of AppFrame class. 
	 */
	public static FanApp getFanUIInstance() {
		if(fanui_instance == null) {
			fanui_instance = new FanApp();
		}
		
		return fanui_instance;
	}
	
	/**
	 * Constructor
	 */
	public FanApp() {
		super();
		
		fan_instance = new Fan(150, 150); // coordinates of centre
		
		fan_panel = new FanPanel(300, 300, fan_instance); // size of panel
		speed_panel = new SpeedPanel(fan_instance);
				
		add(speed_panel, BorderLayout.NORTH);
		add(fan_panel, BorderLayout.CENTER);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}