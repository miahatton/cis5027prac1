package cis5027.project.clients.fanapp.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cis5027.project.clients.fanapp.frames.apps.FanApp;
import cis5027.project.clients.helpers.ValueButtonPanel;

/**
 * @author thanu
 * @author miahatton
 * The SpeedPanel class updates the delay of the Fan instance either based on user input or input from the server.
 * Adapted from SpeedPanel class provided by Thanuja Mallikarachchi (Cardiff Metropolitan University)
 */
public class SpeedPanel extends ValueButtonPanel {
	
	private Fan			fanInstance;
	private FanApp		app;
	private int			lastValidValue;
	
	
	public SpeedPanel(Fan fanObj, String labelText, String defaultVal, String btnText) {
		super(labelText, defaultVal, btnText);
		setButtonActions();
		this.fanInstance = fanObj;
		lastValidValue = Integer.parseInt(textField.getText());
	}
	
	@Override
	protected void setButtonActions() {
		
		button.addActionListener(this);
		textField.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			int timervalue = Integer.parseInt(textField.getText());
			if(timervalue < 0) {
				throw new DelayInputException();
			}
			fanInstance.setFanSpeed(timervalue);
			lastValidValue = timervalue;
		} catch (NumberFormatException e1) {
			app.showUserErrorDialog("Input error", "Please enter an integer value for delay.");
			textField.setText(Integer.toString(lastValidValue));
		}
		
	}

	public void convertReading(double newTemperature) {
		int newSpeed;
		// check against thresholds
		
		if(newTemperature<18) {
			newSpeed = 1000; // effectively turn the fan off if the temperature gets too low
		} else if (newTemperature < 24) {	
			newSpeed = 100; // comfortable working temperature
		} else if (newTemperature < 27) {
			newSpeed = 20; // getting warm
		} else newSpeed = 1; // 27 degrees is far too hot!
		
		textField.setText(String.valueOf(newSpeed));
		fanInstance.setFanSpeed(newSpeed);
	}

	public void setFanAppInstance(FanApp fanAppInstance) {
		this.app = fanAppInstance;
	}

	public void setFanInstance(Fan fanInstance) {
		this.fanInstance = fanInstance;
	}
	
	
	public String getFanSpeedValue() {
		return textField.getText();
	}

	
}
