package com.core.windows;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.Constants;


public class LoginWindow extends javax.swing.JFrame {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final long serialVersionUID = 1L;
	private JPanel basePanel;
	private JLabel yourName;
	private JLabel yourPassword;
	private JButton okButton;
	private JPasswordField yourPasswordText;
	private static JTextField yourNameText;
	Action action;
	KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	Action enterHotKey;
	String firstname = "";
	String lastname = "";
	
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Auto-generated main method to display this JDialog
	 * @param jdbcTemplate2 
	 */
	/*public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Login to Operations Tool");
				LoginWindow1 loginWindow = null;
				try {
					loginWindow = new LoginWindow1(frame);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				loginWindow.setVisible(true);
			}
		});

	}*/

	public LoginWindow(JFrame frame, JdbcTemplate jdbcTemplate) throws SQLException {
		super();
		this.jdbcTemplate = jdbcTemplate;
		loadVersion();
		initGUI();
		
	}
	
	private void initGUI() {
		try {
			{
				basePanel = new JPanel();
				basePanel.setLayout(null);
				
				Action enterAction = new AbstractAction("Login") {
					 
				    private static final long serialVersionUID = 1L;

					@Override
				    public void actionPerformed(ActionEvent e) {
				        doLogin();
				    }
				};
				
				enterAction.putValue(Action.ACCELERATOR_KEY, enterKey);
				basePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Login");
				basePanel.getActionMap().put("Login", enterAction);
				
				this.getContentPane().add(basePanel);
				getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
				getRootPane().getActionMap().put("Cancel", new AbstractAction() {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				
				this.setResizable(false);
				this.setTitle("Login to Operations Tool...");
				
				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				
				{
					yourName = new JLabel();
					basePanel.add(yourName);
					yourName.setText("Your Name:");
					yourName.setBounds(39, 28, 114, 29);
					yourName.setFont(new java.awt.Font("Tahoma",1,14));
				}
				{
					yourPassword = new JLabel();
					basePanel.add(yourPassword);
					yourPassword.setText("Password:");
					yourPassword.setBounds(38, 93, 114, 29);
					yourPassword.setFont(new java.awt.Font("Tahoma",1,14));
				}
				{
					yourNameText = new JTextField();
					basePanel.add(yourNameText);
					yourNameText.setBounds(171, 32, 191, 20);
					yourNameText.setFont(new java.awt.Font("Tahoma",0,14));
					yourNameText.setSize(191, 30);
				}
				{
					yourPasswordText = new JPasswordField();
					basePanel.add(yourPasswordText);
					yourPasswordText.setBounds(171, 98, 191, 20);
					yourPasswordText.setFont(new java.awt.Font("Tahoma",0,14));
					yourPasswordText.setSize(191, 30);
				}
				{
					okButton = new JButton();
					basePanel.add(okButton);
					okButton.setBounds(104, 175, 175, 57);
					okButton.setText("Login");
					okButton.setFont(new java.awt.Font("Tahoma",1,16));
					
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							okButtonActionPerformed(evt);
						}
					});
				}
			}
			this.setSize(400, 300);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void okButtonActionPerformed(ActionEvent evt) {
		doLogin();
	}

	private void doLogin() {
		String username = yourNameText.getText().trim();
		String password = String.valueOf(yourPasswordText.getPassword());
		
		if(StringUtils.isEmpty(username)) {
			JOptionPane.showMessageDialog(this, "Username is blank.");
			return;
		}
		
		if(StringUtils.isEmpty(password)) {
			JOptionPane.showMessageDialog(this, "Password is blank.");
			return;
		}
		
		boolean loginAllowed = false;
		
		try {
			String sql = "select * from operations_team where active = '1' AND username = '" + username + "' AND password = '" + password + "'";
			loginAllowed = jdbcTemplate.query(sql, new ResultSetExtractor<Boolean>() {
			         
			         public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			            if(resultSet.next()) {
			 				setFirstname(resultSet.getString("firstname"));
			 				setLastname(resultSet.getString("lastname"));
			 				return true;
			 			} else {
			 				return false;
			 			}
			         }    	  
			      });
			
			
			if(loginAllowed) {
				MainWindow mainWindow = new MainWindow(username, getFirstname() + " " + getLastname(), jdbcTemplate);
				mainWindow.setVisible(true);
				this.setVisible(false);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid username or password.");
				return;
			}
			
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Sorry, internal error. Login failed!");
			e.printStackTrace();
		}
	}
	
	private void openUrl(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
			}
		}
	}

	public void loadVersion() throws SQLException {
		
		try {
			String sql = "select number, location from operations_tool_version";
			
			jdbcTemplate.query(sql, new ResultSetExtractor<Boolean>() {
		         
		         public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
		            if(resultSet.next()) {
		            	String versionNumber = resultSet.getString("number");
		            	String downloadLocation = resultSet.getString("location");
		            	if(!Constants.VERSION_NUMBER.equals(versionNumber)) {
		    				System.out.println("Current Version:"+Constants.VERSION_NUMBER + " Required:"+versionNumber);
		    				String ObjButtons[] = {"Yes","No"};
		    				int returnVal = JOptionPane.showOptionDialog(null,"A new version of this software is available now. Do you want to download it now?",
		    						"Desktop Publishing Tool", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, ObjButtons,ObjButtons[1]);
		    		        
		    				if(returnVal == JOptionPane.YES_OPTION) {
		    		        	URI uri;
								try {
									uri = new URI(downloadLocation);
									openUrl(uri);
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
		    		        }
		    		        System.exit(0);
		    			}
		 				return true;
		 			} else {
		 				return false;
		 			}
		         }    	  
		      });
			
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
			System.exit(0);
		}
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}