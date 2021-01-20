package cis5027.project.clients.fanapp.frames.apps;


import java.awt.BorderLayout;
import javax.swing.JFrame;

import cis5027.project.clients.fanapp.components.Fan;
import cis5027.project.clients.fanapp.components.FanPanel;
import cis5027.project.clients.fanapp.components.SpeedPanel;
import cis5027.project.clients.helpers.ApplianceApp;


public class FanApp extends ApplianceApp {
	
	// static instance to support singleton
	private static FanApp	fanui_instance;
	
	private FanPanel 		fan_panel;
	private SpeedPanel		speed_panel;
	private Fan				fan_instance;
	
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
		if(fanui_instance == null) {
			fanui_instance = new FanApp();
		}
		
		return fanui_instance;
	}
	
	/**
	 * Constructor
	 */
	public FanApp() {
		
		this.clientType = "fan";
	}
	
	protected void draw() {
		
		fan_panel = new FanPanel(300, 300); // size of panel
		fan_instance = (Fan) fan_panel.getApplianceInstance();
		speed_panel = new SpeedPanel(fan_instance, "Fan speed (delay in ms): ", "10", "Set speed");
		speed_panel.setFanAppInstance(this);		
		
		add(speed_panel, BorderLayout.NORTH);
		add(fan_panel, BorderLayout.CENTER);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	public SpeedPanel getSpeedPanelInstance() {
		return this.speed_panel;
	}
}