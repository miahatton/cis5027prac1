package cis5027.project.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import cis5027.project.csvreader.CsvReader;
import cis5027.project.helpers.AbstractServer;

public class NewServer extends AbstractServer {

	CsvReader csvReader;
	String defaultFileLoc = "inputs/sensor_data.csv";
	
	JTextArea textBox;
	JTextField fileBox;
	JFrame frame;
	
	public NewServer () {
		
		csvReader = new CsvReader(defaultFileLoc);
	}
	
	public void go() {
		
		draw();
		
		frame.setVisible(true);
		}
	
	public void draw() {
		
		frame = new JFrame("Server");
		JPanel mainPanel = new JPanel(); 
		JPanel filePanel = new JPanel();
		
		fileBox = new JTextField(20);
		JButton loadButton = new JButton("Load");
		JButton startButton = new JButton("Start Server");
		JButton stopButton = new JButton("Stop Server");
		
		textBox = new JTextArea(15, 50);
		textBox.setLineWrap(true);
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textBox);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		loadButton.addActionListener(new LoadButtonListener());
		startButton.addActionListener(new StartButtonListener());
		stopButton.addActionListener(new StopButtonListener());
		
		filePanel.add(fileBox);
		filePanel.add(loadButton);
		
		mainPanel.add(startButton);
		mainPanel.add(stopButton);
		mainPanel.add(scrollPane);
		
		frame.getContentPane().add(BorderLayout.NORTH, filePanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,500);
		
	}
	
	@Override
	public void handleMessagesFromClient(String msg, ClientHandler client) {
		// TODO Auto-generated method stub
		
		/*
		 * When the client connects it should somehow send its name
		 * The server should note what kind of client it is
		 * 
		 */
	}

	@Override
	public void sendMessageToClient(String msg, ClientHandler client) {

		/*
		 * The server should send temperature readings to the temp client and light readings to the light client
		 */
		
	}
	
	
	public class LoadButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if (!fileBox.getText().equals(csvReader.getFileLocation())) {
				csvReader.setFileLocation(fileBox.getText());
			}
			
			//TODO check that file exists.
			
			textBox.append("Loading csv file from " + csvReader.getFileLocation() + "... \n");
			
		}
	}
	
	public class StartButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			textBox.append(fileBox.getText() + "\n");
			//TODO call server start method
			
		}
	}
	
	public class StopButtonListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			
			// TODO call server stop method.
		}
	}
	
	public static void main(String[] args) {
		
		new NewServer().go(); 
		
	}
}
