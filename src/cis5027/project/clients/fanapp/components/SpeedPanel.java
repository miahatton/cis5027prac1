package cis5027.project.clients.fanapp.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cis5027.project.helpers.ValueButtonPanel;

public class SpeedPanel extends ValueButtonPanel implements ActionListener {
	
	private Fan			fanInstance;
			
	public SpeedPanel(Fan fanObj, String labelText, String defaultVal, String btnText) {
		super(labelText, defaultVal, btnText);
		
		this.fanInstance = fanObj;
	}
	
	public void setFanInstance(Fan fanInstance) {
		this.fanInstance = fanInstance;
	}
	
	
	public String getFanSpeedValue() {
		return textField.getText();
	}
	
	protected void setButtonActions() {
		
		button.addActionListener(this);
		textField.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int timervalue = Integer.parseInt(textField.getText());
		fanInstance.setFanSpeed(timervalue);
		
	}
}
