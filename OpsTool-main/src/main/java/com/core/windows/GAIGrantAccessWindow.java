package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.core.fineart.FineartUtils;

public class GAIGrantAccessWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private GAIUserSubscriptionsWindow gAIUserSubscriptionsWindow;
	private JTextField txtUserId;
	private JTextField txtUserName;
	private JTextField txtPaymentId;
	private JTextField textPaymentAmount;
	private JTextField textUserEmail;
	private JComboBox<String> subscriptionTypeCombo;
	private String userId;
	private String userName;
	private String userEmail;
	
	public GAIGrantAccessWindow(String username, String userId, String userName, String userEmail, JdbcTemplate jdbcTemplate, 
			GAIUserSubscriptionsWindow gAIUserSubscriptionsWindow) {
		this.jdbcTemplate = jdbcTemplate;
		this.gAIUserSubscriptionsWindow = gAIUserSubscriptionsWindow;
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		initGUI();
		loadUserAccountData();
	}

	private void loadUserAccountData() {
		try {
			txtUserId.setText(userId);
			txtUserName.setText(userName);
			textUserEmail.setText(userEmail);
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void addAccess() {
		try {
			int userId = Integer.valueOf(txtUserId.getText());
			String subscriptionType = subscriptionTypeCombo.getSelectedItem().toString();
			String paymentId = StringUtils.isEmpty(txtPaymentId.getText().trim()) ? "na" : txtPaymentId.getText().trim();
			Float paymentAmount = Float.valueOf(textPaymentAmount.getText().trim());
			String subscriptionStatus = "valid";
			String autoRenew = "false";
			String cardDetails = "Access Granted";
			
			//First invalidate all the previous subscriptions for this user.
			
			String subscriptionUpdateQuery = "update subscription set subscription_autorenew  = 'false', subscription_status = 'invalid', "
					+ "subscription_record_updated = CURRENT_TIMESTAMP() where user_id = '" + userId + "'";
			jdbcTemplate.update(subscriptionUpdateQuery);
			
			String currentTimeUTC = FineartUtils.getCurrentUTCTime();
			
			String expiresOn = "";
			
			if(subscriptionType.toLowerCase().contains("day")) {	//this is a day pass
				expiresOn = FineartUtils.getOneDayValidityForSubsription(currentTimeUTC);
			} else if(subscriptionType.toLowerCase().contains("month")) {	//this is a subscription
				expiresOn = FineartUtils.getOneMonthValidityForSubsription(currentTimeUTC);
			} else if (subscriptionType.toLowerCase().contains("annual")) {
				expiresOn = FineartUtils.getOneYearValidityForSubsription(currentTimeUTC);
			}
			
			final String currentTimeUTCFinal = currentTimeUTC.replace("T", " ").replace("Z", "");
			
			final String expiration = expiresOn.replace("T", " ").replace("Z", "");
			
			String newSubscriptionQuery = "INSERT INTO subscription (user_id, subscription_type,  subscription_status, subscription_expires, subscription_autorenew, "
					+ "payment_ID, payment_amount, card_details, subscription_record_created ) "
					+ "VALUES (?,?,?,?,?,?,?,?,?)";
			
			KeyHolder holder = new GeneratedKeyHolder();
			
			try {
				jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(newSubscriptionQuery, Statement.RETURN_GENERATED_KEYS);
						ps.setInt(1, userId);
						ps.setString(2, subscriptionType);
						ps.setString(3, subscriptionStatus);
						ps.setString(4, expiration);
						ps.setString(5, autoRenew);
						ps.setString(6, paymentId);
						ps.setFloat(7, paymentAmount);
						ps.setString(8, cardDetails);
						ps.setString(9, currentTimeUTCFinal);
								
						return ps;
					}
				}, holder);
				
				String subscriptionId = String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
				
				((DefaultTableModel) gAIUserSubscriptionsWindow.getTable().getModel()).insertRow(0, new Object[]{subscriptionId, FineartUtils.getCurrentUTCTime(),
						subscriptionType, subscriptionStatus, expiresOn, autoRenew, paymentId, paymentAmount, cardDetails});
				gAIUserSubscriptionsWindow.getTable().setRowSelectionInterval(0, 0);
				gAIUserSubscriptionsWindow.getTable().requestFocus();
			} catch (Exception e) {
				e.printStackTrace();
			}		
		
			this.setVisible(false);
            this.dispose();
            
        } catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
		}
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setSize(new Dimension(500, 354));
		this.setTitle("Add GAI Access");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, 5);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JPanel basePanel = new JPanel();
		getContentPane().add(basePanel);
		basePanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User Id:");
		lblNewLabel.setBounds(10, 11, 162, 14);
		basePanel.add(lblNewLabel);
		
		txtUserId = new JTextField();
		txtUserId.setEditable(false);
		txtUserId.setDisabledTextColor(Color.BLACK);
		txtUserId.setBounds(182, 8, 292, 20);
		basePanel.add(txtUserId);
		txtUserId.setColumns(10);
		
		JLabel lblNamePrefix = new JLabel("Full Name:");
		lblNamePrefix.setBounds(10, 39, 162, 14);
		basePanel.add(lblNamePrefix);
		
		txtUserName = new JTextField();
		txtUserName.setEditable(false);
		txtUserName.setForeground(Color.BLACK);
		txtUserName.setBackground(Color.WHITE);
		txtUserName.setDisabledTextColor(Color.LIGHT_GRAY);
		txtUserName.setColumns(10);
		txtUserName.setBounds(182, 36, 292, 20);
		basePanel.add(txtUserName);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 72, 46, 14);
		basePanel.add(lblEmail);
		
		textUserEmail = new JTextField();
		textUserEmail.setEditable(false);
		textUserEmail.setForeground(Color.BLACK);
		textUserEmail.setBackground(Color.WHITE);
		textUserEmail.setBounds(180, 67, 294, 20);
		basePanel.add(textUserEmail);
		textUserEmail.setColumns(10);
		
		JLabel lblNameSuffix = new JLabel("Subscription Type:");
		lblNameSuffix.setBounds(10, 100, 162, 14);
		basePanel.add(lblNameSuffix);
		
		subscriptionTypeCombo = new JComboBox<String>();
		subscriptionTypeCombo.setBackground(Color.WHITE);
		subscriptionTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"Day Pass", "Basic Monthly", "Basic Annual"}));
		subscriptionTypeCombo.setBounds(182, 97, 292, 20);
		basePanel.add(subscriptionTypeCombo);
		
		JLabel lblBirthYear = new JLabel("Payment ID:");
		lblBirthYear.setBounds(10, 129, 162, 14);
		basePanel.add(lblBirthYear);
		
		JLabel lblDeathYear = new JLabel("Payment Amount:");
		lblDeathYear.setBounds(10, 160, 162, 14);
		basePanel.add(lblDeathYear);
		
		txtPaymentId = new JTextField();
		txtPaymentId.setBackground(Color.WHITE);
		txtPaymentId.setDisabledTextColor(Color.BLACK);
		txtPaymentId.setColumns(10);
		txtPaymentId.setBounds(182, 126, 292, 20);
		basePanel.add(txtPaymentId);
		
		textPaymentAmount = new JTextField();
		textPaymentAmount.setBackground(Color.WHITE);
		textPaymentAmount.setDisabledTextColor(Color.BLACK);
		textPaymentAmount.setColumns(10);
		textPaymentAmount.setBounds(182, 157, 292, 20);
		basePanel.add(textPaymentAmount);
		
		JButton btnAddAccess = new JButton("Add Access");
		btnAddAccess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAccess();
			}
		});
		btnAddAccess.setMnemonic('A');
		btnAddAccess.setBounds(62, 231, 170, 49);
		basePanel.add(btnAddAccess);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(256, 231, 170, 49);
		basePanel.add(btnCancel);
		
	}
}