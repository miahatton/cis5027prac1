package cis5027.project.clients.lightapp.components;

import java.awt.event.ActionEvent;

import cis5027.project.clients.helpers.ValueButtonPanel;
import cis5027.project.clients.lightapp.frames.apps.LightApp;
import cis5027.project.helpers.BrightnessFormatException;

/**
 * @author miahatton
 * The Brightness Panel updates the brightness of the Light instance based on user input or input from the server.
 */
public class BrightnessPanel extends ValueButtonPanel {

	// label, text input, and button
	private 	Light		lightInstance;
	private 	String 		lastValidInput; 		// used to hold valid value in case of error
	private		LightApp	app;	
	
	/**
	 * Constructor
	 * @param light			instance of Light class
	 * @param labelText		text displayed next to input field
	 * @param defaultVal	default text value in input field
	 * @param btnText		button text
	 */
	public BrightnessPanel(Light light, String labelText, String defaultVal, String btnText) {
	
		// call super constructor
		super(labelText, defaultVal, btnText);
		setButtonActions();
		
		// Initialise light instance
		this.lightInstance = light;

		// store valid text value
		lastValidInput = defaultVal;

	}
	
	@Override
	protected void setButtonActions() {
		// action listener for button
		button.addActionListener(this);
		// same action listener for hitting return/enter in text box	
		textField.addActionListener(this);	
	}


	@Override
	/**
	 * When button is clicked (or user hits return in input field):
	 * 		* attempt to convert input brightness to integer
	 * 		* validate input
	 * 		* if successful, update the brightness of the light
	 * 		* else display a user error message and update lastValidInput value
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		String inputText = textField.getText();
		
		try {
			try {
				
				// convert text to integer
				int newBrightness = Integer.parseInt(inputText);
				
				// validate value
				if (newBrightness >=0 & newBrightness <= 100) {
					setBrightness(newBrightness);
				}
				else throw new BrightnessFormatException(inputText);
				
			} catch (NumberFormatException e) {
				
				throw new BrightnessFormatException(inputText);
				
			} 
		} catch (BrightnessFormatException e2) {
			app.showUserErrorDialog("Input error", "Not a number! Please enter a number between 0 and 100.");
			// replace text with last valid value used.
			textField.setText(lastValidInput);
		}
		
	}
	
	/**
	 * Sets the brightness of the light instance and saves the value as the last valid input.
	 * @param newBrightness
	 */
	public void setBrightness(int newBrightness) throws BrightnessFormatException {
		
		try {
			
			// set new light colour
			lightInstance.setLightColor(newBrightness);
			
			// note that this value was successful
			lastValidInput = Integer.toString(newBrightness);
			
		} catch (IllegalArgumentException e) {
			
			throw new BrightnessFormatException(Integer.toString(newBrightness));
			
		} 
		
	} 
	
	/**
	 * Accepts values passed from the server via the LightClient class and adjusts the lighting
	 * The lights dim and brighten on a continuous scale to maintain brightness.
	 * The light readings from the server range from 0 to 1000, this range is mapped to the light's possible brightness values
	 * between 100 and 0.
	 * @param lumens		reading from server
	 */
	public void convertReading(int lumens) {
		
		int newBrightness;
		
		try {
		
			newBrightness = (Integer) Math.round(100 - (lumens/10)); // map range 0->1000 to range 100->0
		
			if(newBrightness != Integer.parseInt(textField.getText())) {
				textField.setText(Integer.toString(newBrightness));
				try {

					setBrightness(newBrightness);
				} catch (BrightnessFormatException e) {
					throw new NumberFormatException(); 	// on this occasion it is not actually user input that causes the error
				}
			}
		
		} catch (NumberFormatException e1) {
			app.displayMessage("Bad reading received! Cannot convert " + lumens + ":  " + e1.toString());
		} 
		
	}
	
	/*
	 * Sets LightApp instance.
	 */
	public void setLightApp(LightApp app) {
		this.app = app;
	}
	
}
