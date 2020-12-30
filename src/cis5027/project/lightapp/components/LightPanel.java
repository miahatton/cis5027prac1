package cis5027.project.lightapp.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class LightPanel extends JPanel {

	public void paintComponent(Graphics g) {
		
		// draw background in black
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		Light lamp = new Light(this.getWidth(), this.getHeight(), 25);
		
		lamp.draw(g);
		
	}
	
}
