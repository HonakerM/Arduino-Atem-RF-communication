


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.text.Document;

public class BroadcastGraphic {
	JFrame mainFrame;
	JPanel mainPanel;
	JLabel headerLabel;
	JButton lcdComButton;
	JButton deviceChangeButton;
	JTextField lcdComInput;
	static boolean deviceAddition=false;
	static JTextPane guiConsoleDisplay;
	static DefaultTableModel cameraTableModel;

	public BroadcastGraphic() 	{
		deviceAdder();
		while(!deviceAddition) {
			;
		}

		//frame definition and layoutmanager definition
		mainFrame = new JFrame("Broadcast Thingy");
		mainPanel = new JPanel();
		GridBagLayout localGridBagLayout1 = new GridBagLayout();
		GridBagConstraints localGridBagConstraints1 = new GridBagConstraints();
		mainPanel.setLayout(localGridBagLayout1);


		//set up table contents
		String[] arrayOfString = { "Camera Number", "Devi	sce ID", "View"};
		Object[][] arrayOfObject = { { "0", "0", "0" } };
		cameraTableModel = new DefaultTableModel(arrayOfObject, arrayOfString);
		JTable mainTable = new JTable(cameraTableModel);
		cameraTableModel.removeRow(0);
		updateDevices(BroadcastInput.cameraDevices);





		//set table attributes
		mainTable.setColumnSelectionAllowed(false);
		mainTable.setRowSelectionAllowed(true);
		mainTable.setDefaultRenderer(Object.class, new NoBorderTableCellRenderer());
		mainTable.setDefaultEditor(Object.class, null);

		mainTable.getColumnModel().getColumn(0).setPreferredWidth(mainFrame.getWidth() / 3);
		mainTable.getColumnModel().getColumn(1).setPreferredWidth(mainFrame.getWidth() / 3);
		mainTable.getColumnModel().getColumn(2).setPreferredWidth(mainFrame.getWidth() / 3);
		mainTable.setSelectionMode(0);


		//set up table display attributes
		JScrollPane localJScrollPane = new JScrollPane(mainTable);
		localGridBagConstraints1.gridx = 0;
		localGridBagConstraints1.gridy = 2;
		localGridBagConstraints1.gridwidth = 2;
		localGridBagConstraints1.gridheight = 1;
		localGridBagConstraints1.fill = GridBagConstraints.BOTH;
		localGridBagConstraints1.weightx = 1;
		localGridBagConstraints1.weighty = 1;
		localGridBagConstraints1.anchor = GridBagConstraints.NORTH;
		localGridBagLayout1.setConstraints(localJScrollPane, localGridBagConstraints1);
		localJScrollPane.setBorder(BorderFactory.createTitledBorder ("Connected Devices"));

		mainPanel.add(localJScrollPane);



		//set up main label
		headerLabel = new JLabel();
		headerLabel.setText("Jesuit Broadcasting Camera RF main Control");
		localGridBagConstraints1.gridx = 0;
		localGridBagConstraints1.gridy = 0;
		localGridBagConstraints1.gridwidth = 1;
		localGridBagConstraints1.gridheight = 1;
		localGridBagConstraints1.fill = GridBagConstraints.BOTH;
		localGridBagConstraints1.weightx = 1;
		localGridBagConstraints1.weighty = 1;
		localGridBagConstraints1.anchor = GridBagConstraints.NORTH;
		localGridBagLayout1.setConstraints(headerLabel, localGridBagConstraints1);
		mainPanel.add(headerLabel);
	
		//set up text field to send messages
		lcdComInput = new JTextField();
		lcdComInput.setText("16 CHARACTERS!!!");
		localGridBagConstraints1.gridx = 0;
		localGridBagConstraints1.gridy = 3;
		localGridBagConstraints1.gridwidth = 1;
		localGridBagConstraints1.gridheight = 1;
		localGridBagConstraints1.fill = GridBagConstraints.BOTH;
		localGridBagConstraints1.weightx = 1;
		localGridBagConstraints1.weighty = 1;
		localGridBagConstraints1.anchor = GridBagConstraints.NORTH;
		localGridBagLayout1.setConstraints(lcdComInput, localGridBagConstraints1);
		lcdComInput.setHorizontalAlignment(JTextField.CENTER);
		lcdComInput.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) { 
				if (lcdComInput.getText().length() >= 16 ) // limit textfield to 3 characters
					e.consume(); 
			}  
		});
		mainPanel.add(lcdComInput);
		

		//button to send data
		lcdComButton = new JButton();
		lcdComButton.setText("send");
		localGridBagConstraints1.gridx = 1;
		localGridBagConstraints1.gridy = 3;
		localGridBagConstraints1.gridwidth = 1;
		localGridBagConstraints1.gridheight = 1;
		localGridBagConstraints1.fill = GridBagConstraints.BOTH;
		localGridBagConstraints1.weightx = 1;
		localGridBagConstraints1.weighty = 1;
		localGridBagConstraints1.anchor = GridBagConstraints.NORTH;
		localGridBagLayout1.setConstraints(lcdComButton, localGridBagConstraints1);
  		lcdComButton.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				if(lcdComButton.isEnabled()) {
  					BroadcastInput.send(lcdComInput.getText());
  				}
  			}
  		});
  		mainPanel.add(lcdComButton);

		//set up text field to send messages
		deviceChangeButton = new JButton();
		deviceChangeButton.setText("Change ID");
		localGridBagConstraints1.gridx = 0;
		localGridBagConstraints1.gridy = 1;
		localGridBagConstraints1.gridwidth = 1;
		localGridBagConstraints1.gridheight = 1;
		localGridBagConstraints1.fill = GridBagConstraints.BOTH;
		localGridBagConstraints1.weightx = 1;
		localGridBagConstraints1.weighty = 1;
		localGridBagConstraints1.anchor = GridBagConstraints.NORTH;
		localGridBagLayout1.setConstraints(deviceChangeButton, localGridBagConstraints1);
		deviceChangeButton.setHorizontalAlignment(JTextField.CENTER);
		deviceChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(deviceChangeButton.isEnabled()) {
					createFrame();
				}
			}
		});
		mainPanel.add(deviceChangeButton);


		guiConsoleDisplay = new JTextPane();
		guiConsoleDisplay.setText("Starting Console");
		JScrollPane guiConsoleScrollPane = new JScrollPane(guiConsoleDisplay);
		localGridBagConstraints1.gridx = 1;
		localGridBagConstraints1.gridy = 0;
		localGridBagConstraints1.gridwidth = 1;
		localGridBagConstraints1.gridheight = 2;
		localGridBagConstraints1.fill = GridBagConstraints.BOTH;
		localGridBagConstraints1.weightx = 1;
		localGridBagConstraints1.weighty = 1;
		localGridBagConstraints1.anchor = GridBagConstraints.NORTH;
		localGridBagLayout1.setConstraints(guiConsoleScrollPane, localGridBagConstraints1);
		mainPanel.add(guiConsoleScrollPane);




		// set frame attributes
		mainFrame.setDefaultCloseOperation(3);
		mainFrame.setExtendedState(6);
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				
			}
		};
		mainFrame.addWindowListener(exitListener);



		mainPanel.setBackground(Color.WHITE);
		mainFrame.setContentPane(mainPanel);
		mainFrame.pack();
		mainFrame.setBackground(Color.WHITE);
		mainFrame.setVisible(true);
		
		
		/*
		FONT SIZE SET YP
		*/
		//Main lable font
		Font labelFont = headerLabel.getFont();
		String labelText = headerLabel.getText();
		int stringWidth = headerLabel.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = headerLabel.getWidth();
		double widthRatio = (double)componentWidth / (double)stringWidth;
		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = headerLabel.getHeight();
		int fontSizeToUse = Math.min(newFontSize, componentHeight);
		headerLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse*2));
		
		//lcd Com frame
		labelFont = lcdComInput.getFont();
		labelText = lcdComInput.getText();
		stringWidth = lcdComInput.getFontMetrics(labelFont).stringWidth(labelText);
		componentWidth = lcdComInput.getWidth();
		widthRatio = (double)componentWidth / (double)stringWidth;
		newFontSize = (int)(labelFont.getSize() * widthRatio);
		componentHeight = lcdComInput.getHeight();
		fontSizeToUse = Math.min(newFontSize, componentHeight);
		lcdComInput.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
		
		//update frame for labels
		mainFrame.revalidate();
		
	}
	class NoBorderTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		NoBorderTableCellRenderer() {}

		public java.awt.Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2) {
			java.awt.Component localComponent = super.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean1, false, paramInt1, paramInt2);
			return localComponent;
		}
	}
	public static void log(String msg) {
		Document doc = guiConsoleDisplay.getDocument();
		try {
			doc.insertString(doc.getLength(), msg + "\r\n", null);
			guiConsoleDisplay.setCaretPosition(guiConsoleDisplay.getDocument().getLength());
		} catch (javax.swing.text.BadLocationException e) {}
	}

	public static void addDevice(Camera camera){
		cameraTableModel.addRow(new Object[] {camera.getCamera(),camera.getCameraID(),camera.getStatus() });
	}
	public static void updateDevices(ArrayList<Camera> list){
		cameraTableModel.setRowCount(0);
		for (int i = 0; i < list.size() ; i++) {
			cameraTableModel.addRow(new Object[] {list.get(i).getCamera(),list.get(i).getCameraID(),list.get(i).getStatus() });
		}
	}
	public static void createFrame()
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				//frame definition and layoutmanager definition
				JFrame mainFrame = new JFrame("Broadcast change");
				JPanel mainPanel = new JPanel();


				String[] deviceIds = new String[BroadcastInput.cameraDevices.size()];
				for(int i=0; i<BroadcastInput.cameraDevices.size();i++){
					deviceIds[i] = ""+BroadcastInput.cameraDevices.get(i).getCameraID();
				}

				JComboBox cameraList = new JComboBox(deviceIds);
				JTextField cameraField= new JTextField("new Number");
				JButton goButton = new JButton("change");
				goButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(goButton.isEnabled()) {
							BroadcastInput.setCameraNumber(Integer.parseInt(deviceIds[cameraList.getSelectedIndex()]),Integer.parseInt(cameraField.getText()));
							updateDevices(BroadcastInput.cameraDevices);
						}
					}
				});

				mainPanel.add(cameraList);
				mainPanel.add(cameraField);
				mainPanel.add(goButton);


				mainFrame.setContentPane(mainPanel);
				mainFrame.pack();
				mainFrame.setBackground(Color.WHITE);
				mainFrame.setVisible(true);

			}
		});
	}
	public void deviceAdder(){
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				//frame definition and layoutmanager definition
				JFrame mainFrame = new JFrame("Broadcast Error");
				JPanel mainPanel = new JPanel();



				JTextField cameraField= new JTextField("Number of Cameras");
				JButton goButton = new JButton("set");
				goButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(goButton.isEnabled()) {
							for(int i=0; i<Integer.parseInt(cameraField.getText());i++){
								BroadcastInput.cameraDevices.add(new Camera(i+1 ,i+1));

							}

							deviceAddition=true;
							mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
						}
					}
				});


				mainPanel.add(cameraField);
				mainPanel.add(goButton);




				mainFrame.setContentPane(mainPanel);
				mainFrame.pack();
				mainFrame.setBackground(Color.WHITE);
				mainFrame.setVisible(true);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);


			}
		});
	}
 	public void errorReporter(String message){
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				//frame definition and layoutmanager definition
				JFrame mainFrame = new JFrame("Broadcast Error");
				JPanel mainPanel = new JPanel();



				String[] deviceIds = new String[BroadcastInput.cameraDevices.size()];
				for(int i=0; i<BroadcastInput.cameraDevices.size();i++){
					deviceIds[i] = ""+BroadcastInput.cameraDevices.get(i).getCameraID();
				}

				JLabel errorMessageLabel = new JLabel(message);


				mainPanel.add(errorMessageLabel);



				mainFrame.setContentPane(mainPanel);
				mainFrame.pack();
				mainFrame.setBackground(Color.WHITE);
				mainFrame.setVisible(true);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
				mainFrame.requestFocus();

			}
		});
	}

	
   }


