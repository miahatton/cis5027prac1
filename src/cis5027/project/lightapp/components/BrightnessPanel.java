package cis5027.project.lightapp.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BrightnessPanel extends JPanel implements ActionListener {

	// label, text input, and button
	private 	JLabel 		brightnessLabel;
	private 	JTextField 	brightnessInput;	
	private 	JButton 	setBrightnessBtn;
	private 	Light 		lightInstance;
	
	private 	int 		currentBrightnessPercent;
	private 	String 		inputBoxLastValidText; 		// used to hold valid value in case of error
	
	// constructor
	public BrightnessPanel(Light lamp) {
	
		// Initialize light instance
		this.lightInstance = lamp;
		
		// get brightness of light instance and convert to percentage of 255 (max value)
		currentBrightnessPercent = (int) lamp.getCurrentBrightness() * 100 / 255;
		
		// initialise widgets
		brightnessLabel = new JLabel("Set brightness %");
		brightnessInput = new JTextField(Integer.toString(currentBrightnessPercent), 3);
		setBrightnessBtn = new JButton("Set Brightness");
	
		// store valid text value
		inputBoxLastValidText = brightnessInput.getText();
		
		// add components to panel
		this.setLayout(new FlowLayout());
		this.add(brightnessLabel);
		this.add(brightnessInput);
		this.add(setBrightnessBtn);
		
		setBrightnessBtn.addActionListener(this);
	}
	
	// event handler
	public void actionPerformed(ActionEvent event) {
		
		try {
			
			// convert text to integer
			int newBrightness = Integer.parseInt(brightnessInput.getText());
			
			// Don't let brightness go above 100 or below 0	
			if (newBrightness > 100) {
				brightnessInput.setText("100");
			} else if (newBrightness < 0) {
				brightnessInput.setText("0");
			}
			
			// set new light colour
			lightInstance.setLightColor(newBrightness);
			
			// note that this value was successful
			inputBoxLastValidText = brightnessInput.getText();
			
		} catch (NumberFormatException e) {
			
			// replace text with last valid value used.
			brightnessInput.setText(inputBoxLastValidText);
			
		} catch (IllegalArgumentException e) {
			
			// replace text with last valid value used.
			brightnessInput.setText(inputBoxLastValidText);
			
		}

	}
	
	public void setBrightnessInputAndClick(int newVal) {
		
		brightnessInput.setText(Integer.toString(newVal));
		setBrightnessBtn.doClick();
		
	}

	public int getBrightnessInputText() {
		return Integer.parseInt(brightnessInput.getText());
	}
	
}
