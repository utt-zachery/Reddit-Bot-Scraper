import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import settings.SettingsHolder;

public class MainDisplay {

	private JFrame frame;
	private JTextField runame;
	private JPasswordField rpword;
	private SettingsHolder settings;
	private NoEditTableModel readModel;
	private  JTable readCase;
	private DefaultListModel<String> readModel3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsHolder gears = new SettingsHolder();
					MainDisplay window = new MainDisplay(gears);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainDisplay(SettingsHolder settings) {
		this.settings = settings;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		frame = new JFrame("Reddit Bot Control Panel");
		frame.setBounds(100, 100, 450, 375);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Trigger Phrases", null, panel, null);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(10);
		panel.add(verticalStrut);
		
		JPanel buttonPane = new JPanel();
		panel.add(buttonPane);
		buttonPane.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton("Add Phrase");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnNewButton.setFocusPainted(false);
		
		buttonPane.add(btnNewButton, BorderLayout.WEST);
		
		JButton btnNewButton_1 = new JButton("Delete Phrase");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnNewButton_1.setFocusPainted(false);
		buttonPane.add(btnNewButton_1, BorderLayout.EAST);
		buttonPane.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		panel.add(verticalStrut_1);
		
		JScrollPane phraseHolder = new JScrollPane();
		panel.add(phraseHolder);
		
		ObjectInputStream objectinputstream = null;
		try {
			FileInputStream streamIn = new FileInputStream("Resources" + File.separator+ "table.dat");
		     objectinputstream = new ObjectInputStream(streamIn);
		     readModel = (NoEditTableModel) objectinputstream.readObject();
		     readCase= new JTable(readModel);
		     readCase.removeColumn(readCase.getColumnModel().getColumn(3));
		     readCase.setSelectionMode(0);
		    phraseHolder.setViewportView(readCase);
		    objectinputstream.close();
		    streamIn.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
				
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (readCase.getSelectedRowCount() > 0)
				{
					readModel.removeRow(readCase.getSelectedRow());
			    	String[][] goodData = new String[readModel.getRowCount()][4];
					for (int count = 0; count < readModel.getRowCount(); count++){
						goodData[count][0] = (String)readModel.getValueAt(count, 0);
						goodData[count][1] = (String)readModel.getValueAt(count, 1);
						goodData[count][2] = (String)readModel.getValueAt(count, 2);
						goodData[count][3] = (String)readModel.getValueAt(count, 3);
						}
					String[] headers = {"Phrase Keywords", "Reply Content", "Subreddit","lastPost"};
					NoEditTableModel m = new NoEditTableModel(goodData, headers);
					
					File old = new File("Resources" + File.separator + "table.dat");
					old.delete();
					ObjectOutputStream oos = null;
					FileOutputStream fout = null;
					try{
					     fout =  new FileOutputStream("Resources" + File.separator + "table.dat", true);
					     oos = new ObjectOutputStream(fout);
					    oos.writeObject(m);
					    oos.close();
					} catch (Exception ex) {
					    ex.printStackTrace();
					}
			    
				}
			}
		});
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField username = new JTextField(20);
				JTextField password = new JTextField(20);
				JTextField subreddit = new JTextField(20);
				JPanel horizontal = new JPanel();
				horizontal.setLayout(new BoxLayout(horizontal, BoxLayout.PAGE_AXIS));
				JPanel row1 = new JPanel();
				row1.add(new JLabel("Phrase Keywords:  "));
				row1.add(username);
				JPanel row2 = new JPanel();
				row2.add(new JLabel("Response Content: "));
				row2.add(password);
				JPanel row3 = new JPanel();
				row3.add(new JLabel("Subreddit:                  "));
				row3.add(subreddit);
				horizontal.add(row1);
				horizontal.add(row2);
				horizontal.add(row3);
				Object[] message = {
						horizontal
				};

				int option = JOptionPane.showConfirmDialog(null, message, "Add Phrase", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (username.getText().length() > 0 && password.getText().length() > 0 && subreddit.getText().length() > 0 ) {
				    	String[] data = {username.getText(), password.getText(), subreddit.getText(), "thing"};
				    	readModel.addRow(data);
				    	String[][] goodData = new String[readModel.getRowCount()][4];
						for (int count = 0; count < readModel.getRowCount(); count++){
							goodData[count][0] = (String)readModel.getValueAt(count, 0);
							goodData[count][1] = (String)readModel.getValueAt(count, 1);
							goodData[count][2] = (String)readModel.getValueAt(count, 2);
							goodData[count][3] = (String)readModel.getValueAt(count, 2);
							}
						String[] headers = {"Phrase Keywords", "Reply Content", "Subreddit","lastPost"};
						NoEditTableModel m = new NoEditTableModel(goodData, headers);
						
						File old = new File("Resources" + File.separator + "table.dat");
						old.delete();
						ObjectOutputStream oos = null;
						FileOutputStream fout = null;
						try{
						     fout =  new FileOutputStream("Resources" + File.separator + "table.dat", true);
						     oos = new ObjectOutputStream(fout);
						    oos.writeObject(m);
						    oos.close();
						} catch (Exception ex) {
						    ex.printStackTrace();
						}
				    } 
				}
			}
		
			
		});
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Bot Notifications", null, panel_1, null);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		
		JPanel header1 = new JPanel();
		panel_1.add(header1);
		
		JLabel lblNewLabel = new JLabel("Notify Me When");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		header1.add(lblNewLabel);
		header1.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
		
		JPanel messagecontrol = new JPanel();
		panel_1.add(messagecontrol);
		messagecontrol.setLayout(new BorderLayout(0, 0));
		
		JCheckBox chckbxPrivateMessages = new JCheckBox("Private Messages");
		chckbxPrivateMessages.setFont(new Font("Tahoma", Font.PLAIN, 11));
		messagecontrol.add(chckbxPrivateMessages, BorderLayout.WEST);
		chckbxPrivateMessages.setFocusPainted(false);
	
		JCheckBox chckbxRepliesToBot = new JCheckBox("Replies to Bot Comments");
		chckbxRepliesToBot.setFont(new Font("Tahoma", Font.PLAIN, 11));
		messagecontrol.add(chckbxRepliesToBot, BorderLayout.EAST);
		messagecontrol.setMaximumSize(new Dimension(Integer.MAX_VALUE,150));
		chckbxRepliesToBot.setFocusPainted(false);
		chckbxPrivateMessages.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				settings.setSetting("sendEmailPM", chckbxPrivateMessages.isSelected());
			}
		});
		
		chckbxRepliesToBot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				settings.setSetting("sendEmailReply", chckbxRepliesToBot.isSelected());
			}
		});
		chckbxRepliesToBot.setSelected((Boolean)settings.getSetting("sendEmailReply"));
		chckbxPrivateMessages.setSelected((Boolean)settings.getSetting("sendEmailPM"));
		Component verticalStrut_4 = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut_4);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
		panel_1.add(separator);
		
		Component verticalStrut_2 = Box.createVerticalStrut(10);
		panel_1.add(verticalStrut_2);
		
		JPanel header2 = new JPanel();
		panel_1.add(header2);
		header2.setLayout(new BorderLayout(0, 0));
		header2.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
		
		JButton btnAddEmail = new JButton("Add Email");
		btnAddEmail.setFocusPainted(false);

		btnAddEmail.setFont(new Font("Tahoma", Font.PLAIN, 11));
		header2.add(btnAddEmail, BorderLayout.WEST);
		
		JPanel panel_3 = new JPanel();
		header2.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblSendEmailsTo = new JLabel("Send Emails To");
		panel_3.add(lblSendEmailsTo);
		lblSendEmailsTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSendEmailsTo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JButton btnRemoveEmail = new JButton("Remove Email");
		
		btnRemoveEmail.setFocusPainted(false);
		btnRemoveEmail.setFont(new Font("Tahoma", Font.PLAIN, 11));
		header2.add(btnRemoveEmail, BorderLayout.EAST);
		
		Component verticalStrut_6 = Box.createVerticalStrut(20);
		header2.add(verticalStrut_6, BorderLayout.SOUTH);
		
		JList<String> list = new JList<String>();
		
		
		 objectinputstream = null;
			try {
				FileInputStream streamIn = new FileInputStream("Resources" + File.separator+ "emails.dat");
			     objectinputstream = new ObjectInputStream(streamIn);
			    this.readModel3 = (DefaultListModel<String>) objectinputstream.readObject();
			     list.setModel(readModel3);
			     streamIn.close();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			JScrollPane viewpoint = new JScrollPane(list);
			btnAddEmail.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JPanel holder = new JPanel();
					holder.add(new JLabel("Email Address: "));
					
					JTextField einput = new JTextField(10);
					holder.add(einput);
					Object[] message = {
							holder	
					};

					int option = JOptionPane.showConfirmDialog(null, message, "Add Email", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION && einput.getText().length() > 0)
					{
						readModel3.addElement(einput.getText());
						
					
						DefaultListModel<String> m = new DefaultListModel<String>();
						
						for (int count = 0; count < readModel3.getSize(); count++){
							m.addElement(readModel3.getElementAt(count));
							}
						File old = new File("Resources" + File.separator + "emails.dat");
						old.delete();
						
						ObjectOutputStream oos = null;
						FileOutputStream fout = null;
						try{
						     fout =  new FileOutputStream("Resources" + File.separator + "emails.dat", true);
						     oos = new ObjectOutputStream(fout);
						    oos.writeObject(m);
						    oos.close();
						} catch (Exception ex) {
						    ex.printStackTrace();
						}
					}
				}
			});
			btnRemoveEmail.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!list.getSelectedValue().isEmpty())
					{
						readModel3.remove(list.getSelectedIndex());
						DefaultListModel<String> m = new DefaultListModel<String>();
						
						for (int count = 0; count < readModel3.getSize(); count++){
							m.addElement(readModel3.getElementAt(count));
							}
						File old = new File("Resources" + File.separator + "emails.dat");
						old.delete();
						
						ObjectOutputStream oos = null;
						FileOutputStream fout = null;
						try{
						     fout =  new FileOutputStream("Resources" + File.separator + "emails.dat", true);
						     oos = new ObjectOutputStream(fout);
						    oos.writeObject(m);
						    oos.close();
						} catch (Exception ex) {
						    ex.printStackTrace();
						}
					}
				}
			});
			panel_1.add(viewpoint);
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Reddit Settings", null, panel_2, null);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.PAGE_AXIS));
		
		Component verticalStrut_5 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_5);
		
		JPanel loginpane = new JPanel();
		loginpane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Reddit Login", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.add(loginpane);
		loginpane.setLayout(new BoxLayout(loginpane, BoxLayout.PAGE_AXIS));
		
		JPanel panel_5 = new JPanel();
		loginpane.add(panel_5);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_5.add(lblUsername);
		
		runame = new JTextField();
		panel_5.add(runame);
		runame.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		loginpane.add(panel_6);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_6.add(lblPassword);
		
		rpword = new JPasswordField();
		rpword.setColumns(10);
		panel_6.add(rpword);
		
		JPanel panel_8 = new JPanel();
		loginpane.add(panel_8);
		loginpane.setMaximumSize(new Dimension(Integer.MAX_VALUE,350));
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSave.setFocusPainted(false);
		runame.setText((String)settings.getSetting("uname"));
		rpword.setText((String)settings.getSetting("pword"));
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settings.setSetting("uname", runame.getText());
				settings.setSetting("pword", String.valueOf(rpword.getPassword()) );
			}
		});
		panel_8.add(btnSave);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_3);
		
		JPanel panel_9 = new JPanel();
		panel_2.add(panel_9);
		panel_9.setMaximumSize(new Dimension(175,Integer.MAX_VALUE));
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_10 = new JPanel();
		panel_9.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new BoxLayout(panel_10, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel("Search");
		label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_10.add(label);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(5);
		panel_10.add(horizontalStrut_2);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(1, 1, 99, 1));
		spinner_1.setMaximumSize(new Dimension(35, 25));
		spinner_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_10.add(spinner_1);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(5);
		panel_10.add(horizontalStrut_3);
		
		JLabel label_1 = new JLabel("pages of posts");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_10.add(label_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setMaximumSize(new Dimension(175,Integer.MAX_VALUE));
		panel_2.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_4.add(panel_7);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.X_AXIS));
		
		JLabel lblWait = new JLabel("Wait");
		lblWait.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_7.add(lblWait);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		panel_7.add(horizontalStrut_1);
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 11));
		spinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));
		spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
		panel_7.add(spinner);
		spinner_1.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				settings.setSetting("viewpage", (int)spinner_1.getValue());
			}
		});
		spinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				settings.setSetting("waitTime", (int)spinner.getValue()*60);
			}
		});
		spinner.setValue((int)(((int)settings.getSetting("waitTime") ) / 60.0));
		spinner_1.setValue((int)settings.getSetting("viewpage") );
		Component horizontalStrut = Box.createHorizontalStrut(5);
		panel_7.add(horizontalStrut);
		
		JLabel lblMinutesBetweenPosts = new JLabel("minutes between posts");
		lblMinutesBetweenPosts.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_7.add(lblMinutesBetweenPosts);
	}

}
