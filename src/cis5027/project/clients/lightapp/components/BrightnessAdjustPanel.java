package cis5027.project.clients.lightapp.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import cis5027.project.clients.helpers.Appliance;

public class BrightnessAdjustPanel extends JPanel {

	private JButton lightMinusBtn;
	private JButton lightPlusBtn;
	
	private int incrementer = 10;
	
	Light lightInstance;
	
	BrightnessPanel bpanel;
	
	public BrightnessAdjustPanel (Appliance appliance, BrightnessPanel panel) {
	
		// TODO put these below center
		lightMinusBtn = new JButton("Decrease Brightness 10%");
		lightPlusBtn = new JButton("Increase Brightness 10%");
		
		this.lightInstance = (Light) appliance;

		this.bpanel = panel;
		
		this.setLayout(new FlowLayout());
		this.add(lightMinusBtn);
		this.add(lightPlusBtn);

		setButtonActions();
	}

	private void setButtonActions() {

		lightMinusBtn.addActionListener(new DimmerListener());
		lightPlusBtn.addActionListener(new BrightenerListener());
	}
	
	// inner class for dimming button
	class DimmerListener implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			
			incrementLightColor(true);
		}
	}
	
	// inner class for brightening button
	class BrightenerListener implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			
			incrementLightColor(false);
			
		}
	}
	
	public void incrementLightColor(boolean dim) {

		if(dim) {
			
			incrementer *= -1; // make light dimmer
		}
		
		int colorValue = Math.min(bpanel.getBrightnessInputText() + incrementer, 100);	
		
		if (colorValue < 0) {
			colorValue = 0;
		}
			
		bpanel.setBrightnessAndInputField(colorValue);
		
		incrementer = 10;
		
		}
	
}
