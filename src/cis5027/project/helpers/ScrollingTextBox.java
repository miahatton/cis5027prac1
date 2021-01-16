package cis5027.project.helpers;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class ScrollingTextBox {

	private JTextArea textBox;
	private JScrollPane scrollPane;
	
	
	public ScrollingTextBox(int rows, int columns) {
		
		textBox = new JTextArea(rows, columns);
		textBox.setLineWrap(true);
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		
		scrollPane = new JScrollPane(textBox);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	public JScrollPane getScrollPane() {
		
		return this.scrollPane;
	}
	
	public void displayMessage(String msg) {
		
		textBox.append(msg + "\n");
	}
	
}
