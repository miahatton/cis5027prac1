package cis5027.project.helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ScrollingTextBox {

	private JTextArea textBox;
	private JScrollPane scrollPane;
	JScrollBar scrollBar;
	
	
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
	
	public void scrollToBotton() {
		scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
	}
}
