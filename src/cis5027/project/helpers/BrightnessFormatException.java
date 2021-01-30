/**
 * @author miahatton 
 *
 */
package cis5027.project.helpers;

/**
 * @author miahatton
 * Custom Exception to handle issues with brightness input value
 * Brightness must be an integer between 0 and 100
 */
public class BrightnessFormatException extends UserInputException {

	/**
	 * Constructor
	 * @param inputText		The invalid text that caused the error
	 */
	public BrightnessFormatException(String inputText) {
		super(inputText);
		this.inputType = "brightness";
		this.rule = "an integer between 0 and 100";
	}
	
}
