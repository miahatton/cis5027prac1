package cis5027.project.fanapp.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SpeedPanel extends JPanel implements ActionListener {
	
	//TODO refactor to extend ApplianceValuePanel
	
	JLabel 				lbl_speed_value;
	JTextField			txt_speed_value;
	JButton				btn_setSpeed;
	
	private Fan			fan_instance;
			
	public SpeedPanel(Fan fanObj) {
		this.lbl_speed_value 	= new JLabel("Fan speed (Delay in ms) :");
		this.txt_speed_value 	= new JTextField("10", 5);
		this.btn_setSpeed 		= new JButton("Set Speed");
		
		this.setLayout(new FlowLayout());
		this.add(lbl_speed_value);
		this.add(txt_speed_value);
		this.add(btn_setSpeed);
		
		this.fan_instance = fanObj;
		
		setButtonActions();
	}
	
	public void setFanInstance(Fan fanInstance) {
		this.fan_instance = fanInstance;
	}
	
	
	public String getFanSpeedValue() {
		return txt_speed_value.getText();
	}
	
	private void setButtonActions() {
		
		this.btn_setSpeed.addActionListener(this);
		this.txt_speed_value.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int timervalue = Integer.parseInt(txt_speed_value.getText());
		fan_instance.setFanSpeed(timervalue);
		
	}
}
