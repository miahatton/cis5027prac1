package cis5027.project.helpers;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * @author miahatton
 * Scrolling text box is used by all GUIs to display messages.
 */
public class ScrollingTextBox {

	private JTextArea textBox;
	private JScrollPane scrollPane;
	JScrollBar scrollBar;
	
	/*
	 * Constructor
	 */
	public ScrollingTextBox(int rows, int columns) {
		
		// non-editable text box
		textBox = new JTextArea(rows, columns);
		textBox.setLineWrap(true);
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		
		// scroll pane with vertical scrollbar
		scrollPane = new JScrollPane(textBox);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	/*
	 * Getter for scrollpane.
	 */
	public JScrollPane getScrollPane() {
		
		return this.scrollPane;
	}
	
	/**
	 * Adds given message to the text box
	 * @param msg
	 */
	public void displayMessage(String msg) {
		
		textBox.append(msg + "\n");
	}
	
	/*
	 * Scroll to bottom of text box
	 */
	//TODO it's scrolling too early.
	public void scrollToBottom() {
		scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
	}
}
