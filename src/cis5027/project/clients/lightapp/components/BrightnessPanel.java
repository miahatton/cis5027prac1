package cis5027.project.clients.lightapp.components;

import java.awt.event.ActionEvent;

import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.clients.lightapp.frames.apps.LightApp;

public class BrightnessPanel extends ValueButtonPanel {

	// label, text input, and button
	private 	Light		lightInstance;
	private 	String 		inputBoxLastValidText; 		// used to hold valid value in case of error
	private		LightApp	app;	
	
	// constructor
	public BrightnessPanel(Light light, String labelText, String defaultVal, String btnText) {
	
		// call super constructor
		super(labelText, defaultVal, btnText);
		setButtonActions();
		
		// Initialise light instance
		this.lightInstance = light;

		// store valid text value
		inputBoxLastValidText = textField.getText();

	}
	
	public void setBrightnessInputAndClick(int newVal) {
		
		// TODO make this a superclass method that can also be applied to fan - I think it will be useful for final proj.
		textField.setText(Integer.toString(newVal));
		button.doClick();
		
	}

	public int getBrightnessInputText() {
		return Integer.parseInt(textField.getText());
	}
	
	@Override
	protected void setButtonActions() {
		// action listener for button
		button.addActionListener(this);
		// same action listener for hitting return/enter in text box	
		textField.addActionListener(this);	
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			
			// convert text to integer
			int newBrightness = Integer.parseInt(textField.getText());
			
			// Don't let brightness go above 100 or below 0	
			if (newBrightness > 100) {
				textField.setText("100");
			} else if (newBrightness < 0) {
				textField.setText("0");
			}
			
			// set new light colour
			lightInstance.setLightColor(newBrightness);
			
			// note that this value was successful
			inputBoxLastValidText = textField.getText();
			
		} catch (IllegalArgumentException e) {
			
			// replace text with last valid value used.
			textField.setText(inputBoxLastValidText);
			
		} 
		
	}
	
	public void convertReading(int lumens) {
		
		// map the full range of possible readings (from around 20 to around 70) to a value between 0 and 100
		
		int readingPercent = (Integer) Math.round(((lumens - 20)*100)/(70-20));

		try {
			setBrightnessInputAndClick(100 - readingPercent);
		} catch (IllegalArgumentException e) {
			app.displayMessage("Error occured calculating new light level: " + e.toString());
		}
	}
	
	public void setLightApp(LightApp app) {
		this.app = app;
	}
	
}
