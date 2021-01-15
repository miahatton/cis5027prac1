package cis5027.project.helpers;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

abstract public class ValueButtonPanel extends JPanel {
	
	protected JLabel 		label;
	protected JTextField 	textField;
	protected JButton 	button;

	public ValueButtonPanel (String labelText, String defaultVal, String btnText) {
		this.label 		= new JLabel(labelText);
		this.textField 	= new JTextField(defaultVal, 5);
		this.button 	= new JButton(btnText);
		
		this.setLayout(new FlowLayout());
		this.add(label);
		this.add(textField);
		this.add(button);
		
		setButtonActions();
	}
	
	abstract protected void setButtonActions();
	
}
