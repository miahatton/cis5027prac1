package cis5027.project.clients.fanapp.components;

import java.awt.event.ActionEvent;

import cis5027.project.clients.fanapp.frames.apps.FanApp;
import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.clients.helpers.DelayFormatException;

/**
 * @author thanu
 * @author miahatton
 * The SpeedPanel class updates the delay of the Fan instance either based on user input or input from the server.
 * Adapted from SpeedPanel class provided by Thanuja Mallikarachchi (Cardiff Metropolitan University)
 */
public class SpeedPanel extends ValueButtonPanel {
	
	private Fan			fanInstance;
	private FanApp		app;
	private String		lastValidValue;
	
	
	public SpeedPanel(Fan fanObj, String labelText, String defaultVal, String btnText) {
		super(labelText, defaultVal, btnText);
		setButtonActions();
		this.fanInstance = fanObj;
		lastValidValue = defaultVal;
	}
	
	@Override
	protected void setButtonActions() {
		
		button.addActionListener(this);
		textField.addActionListener(this);
	}

	@Override
	/**
	 * When the button is clicked (or user hits return in text field)
	 * 		* get the text from the text field
	 * 		* attempt to conver to integer
	 * 		* if successful, update fan speed
	 * 		* if unsuccessful, show user error message and set text field to last valid value.
	 */
	public void actionPerformed(ActionEvent e) {
		
		String inputText = textField.getText();
		
		try {
			try {
				// convert input text to integer
				int timervalue = Integer.parseInt(textField.getText());
				
				// validate input
				if(timervalue < 0) {
					throw new DelayFormatException(inputText);
				}
				
				// set fan speed
				fanInstance.setFanSpeed(timervalue);
				
				// save value as lastValidValue
				lastValidValue = Integer.toString(timervalue);
				
			} catch (NumberFormatException e1) {
				throw new DelayFormatException(inputText);
			} 
			
		} catch (DelayFormatException e2) {
			app.showUserErrorDialog("Input error", e2.toString());
			textField.setText(lastValidValue);
		}
	}

	/**
	 * Accepts value sent from server to client and checks temperature thresholds, adjusting fan speed if necessary.
	 * @param newTemperature		temperature reading from client
	 */
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
		
		// update the text field and set fan speed
		if (newSpeed != fanInstance.getFanSpeed()) {
			textField.setText(String.valueOf(newSpeed));
			fanInstance.setFanSpeed(newSpeed);
		}
	}

	/**
	 * Get input fan speed from text box
	 * @return new fan speed as a string
	 */
	public String getFanSpeedValue() {
		return textField.getText();
	}
	
	
	/*
	 * Getters and setters
	 */
	
	public void setFanAppInstance(FanApp fanAppInstance) {
		this.app = fanAppInstance;
	}

	public void setFanInstance(Fan fanInstance) {
		this.fanInstance = fanInstance;
	}


	
}
