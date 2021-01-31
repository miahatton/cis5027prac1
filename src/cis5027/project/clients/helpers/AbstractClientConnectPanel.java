package cis5027.project.clients.helpers;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;



/**
 * @author miahatton
 * ClientConnectPanel class is a JPanel containing buttons to connect to server and output text box.
 */
abstract public class AbstractClientConnectPanel extends ValueButtonPanel {

	// constants
	protected static final String	DEFAULT_PORT = "5000";
	protected static final int 	MIN_PORT_NUM = 1024;
	protected static final int 	MAX_PORT_NUM = 65535;
	
	protected 	String lastReading;
	protected 	ScrollingTextBox	clientOutput;
	protected 	ApplianceApp app;
	protected 	JPanel containerPanel;
	protected 	AbstractClient client;
	protected 	int port;
	protected 	JButton stopButton;
	
	/*
	 * Constructor
	 * @param labelText
	 * @param defaultVal - in input text field
	 * @param btnText
	 * @param type - the type of client connecting
	 */
	public AbstractClientConnectPanel(String labelText, String btnText) {
		
		super(labelText, DEFAULT_PORT, btnText);
		
		stopButton = new JButton("Stop client");
		stopButton.setEnabled(false); // can't stop before you start!
		
		this.add(stopButton);
		
		clientOutput = new ScrollingTextBox(12,30);
		
		containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		
		containerPanel.add(this);
		containerPanel.add(clientOutput.getScrollPane());
		
		setButtonActions();
	}
	
	@Override
	protected void setButtonActions() {
		button.setActionCommand("START");
		stopButton.setActionCommand("STOP");
		button.addActionListener(this);
		stopButton.addActionListener(this);
	}
	
	/*
	 * Outputs messages to the user via the scrolling text box.
	 */
	public void displayMessage(String msg) {
		clientOutput.displayMessage(msg);
		clientOutput.scrollToBottom();
	}
	
	/*
	 * Calls the app's showUserErrorDialog method, making it available to the client
	 * @param errorType
	 * @param msg
	 */
	
	public void showUserErrorDialog(String errorType, String msg) {
		app.showUserErrorDialog(errorType, msg);
	}

	/*
	 *  Getters and setters
	 */
	
	public JPanel getContainerPanel() {
		return this.containerPanel;
	}
	
	public void setApp(ApplianceApp app) {
		this.app = app;
	}
	
	/*
	 * Gets the port number from the input box and performs validation.
	 */
	public int getPortNumber() throws PortFormatException {
		int port;
		try {
			port = Integer.parseInt(textField.getText());
			
			if (port <MIN_PORT_NUM | port > MAX_PORT_NUM) {
				throw new PortFormatException(port);
			}
			else {
				return port;
			}
			
		} catch (NumberFormatException e) {
			throw new PortFormatException(textField.getText());
		} 
		
	}

	/**
	 * Resets the buttons when the client connections have closed.
	 * Start button should be enabled, stop button disabled.
	 */
	public void resetButtons() {
		stopButton.setEnabled(false);
		button.setEnabled(true);		
	}
	
}
