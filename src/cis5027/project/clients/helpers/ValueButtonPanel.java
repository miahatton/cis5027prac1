package cis5027.project.clients.helpers;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author miahatton
 * Used in all of the GUIs of the program, this is a superclass that adds a label, text input field and button to a panel.
 */
abstract public class ValueButtonPanel extends JPanel implements ActionListener {
	
	protected JLabel 		label;
	protected JTextField 	textField;
	protected JButton 	button;

	/**
	 * Constructor
	 * @param labelText		Helper text to appear next to input field
	 * @param defaultVal	Default text in input field
	 * @param btnText		Button text
	 */
	public ValueButtonPanel (String labelText, String defaultVal, String btnText) {
		this.label 		= new JLabel(labelText);
		this.textField 	= new JTextField(defaultVal, 5);
		this.button 	= new JButton(btnText);
		
		this.setLayout(new FlowLayout());
		this.add(label);
		this.add(textField);
		this.add(button);
	}
	
	/**
	 * Adds action listeners to buttons
	 */
	abstract protected void setButtonActions();
	
}
