package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.fineart.FineartUtils;
import com.stripe.Stripe;
import com.stripe.model.Customer;

public class GAIEditUserWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private GAIUserAccountsWindow gAIUserAccountsWindow;
	private JTextField txtUserId;
	private JTextField txtUserName;
	private JTextField txtCustomerId;
	private JTextField textOTP;
	private JTextField txtOTPUsed;
	private String userId;
	private JTextField textUserEmail;
	private JTextField textRegisteredOn;
	private JComboBox<String> subscriberTypeCombo;
	private JTextField textNewPassword;
	private JTextField textConfirmNewPassword;
	private String originalEmail;
	
	public GAIEditUserWindow(String username, JdbcTemplate jdbcTemplate, GAIUserAccountsWindow gAIUserAccountsWindow) {
		this.jdbcTemplate = jdbcTemplate;
		this.gAIUserAccountsWindow = gAIUserAccountsWindow;
		initGUI();
		loadUserAccountData();
	}

	private void loadUserAccountData() {
		try {
			userId = gAIUserAccountsWindow.getTable().getValueAt(gAIUserAccountsWindow.getTable().getSelectedRow(), 0).toString();
			String query = "select * from user_accounts where user_id = " + userId;
			
			jdbcTemplate.query(query, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						txtUserId.setText(resultSet.getString("user_id"));
						txtUserName.setText(resultSet.getString("full_name"));
						textUserEmail.setText(resultSet.getString("login_email"));
						originalEmail = resultSet.getString("login_email");
						subscriberTypeCombo.setSelectedItem(resultSet.getString("subscriber_type"));
						txtCustomerId.setText(resultSet.getString("customer_id"));
						textOTP.setText(resultSet.getString("otp"));
						txtOTPUsed.setText(resultSet.getString("otp_used"));
						textRegisteredOn.setText(resultSet.getString("created"));
					}
					return "";
				}
			});
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateUserAccount() {
		try {
			
			String fullName = txtUserName.getText().trim();
			String email = textUserEmail.getText().trim();
			String subscriberType = subscriberTypeCombo.getSelectedItem().toString();
			String password = textNewPassword.getText();
			String confirmPassword = textConfirmNewPassword.getText().trim();
			String gatewayId = txtCustomerId.getText().trim();
			
			if(StringUtils.isEmpty(fullName)) {
				JOptionPane.showMessageDialog(this,"Name cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(StringUtils.isEmpty(email)) {
				JOptionPane.showMessageDialog(this, "Email cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(!originalEmail.equals(email)) {
				//do the change in email in Stripe as well.
				Stripe.apiKey = "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";
				Customer customer =  Customer.retrieve(gatewayId);

				Map<String, Object> params = new HashMap<>();
				params.put("email", email);

				customer.update(params);
			}
			
			String query = "";
			
			if(StringUtils.isEmpty(password)) {
				query = "UPDATE user_accounts SET full_name = '" + fullName 
						+ "', login_email = '" + email 
						+ "', subscriber_type = '" + subscriberType 
						+ "', updated = CURRENT_TIMESTAMP" 
						+ " WHERE user_accounts.user_id = '" + userId + "'";
			} else {
				query = "UPDATE user_accounts SET full_name = '" + fullName 
						+ "', login_email = '" + email + "', login_password = '" + FineartUtils.getSHA1String(password) 
						+ "', subscriber_type = '" + subscriberType 
						+ "', updated = CURRENT_TIMESTAMP" 
						+ " WHERE user_accounts.user_id = '" + userId + "'";
			}
			
			jdbcTemplate.update(query);
			
			this.setVisible(false);
            this.dispose();
            
        } catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setSize(new Dimension(500, 443));
		this.setTitle("Edit GAI User");
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
		txtUserName.setDisabledTextColor(Color.LIGHT_GRAY);
		txtUserName.setColumns(10);
		txtUserName.setBounds(182, 36, 292, 20);
		basePanel.add(txtUserName);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 72, 46, 14);
		basePanel.add(lblEmail);
		
		textUserEmail = new JTextField();
		textUserEmail.setBounds(180, 67, 294, 20);
		basePanel.add(textUserEmail);
		textUserEmail.setColumns(10);
		
		JLabel lblNameSuffix = new JLabel("Subscriber Type:");
		lblNameSuffix.setBounds(10, 100, 162, 14);
		basePanel.add(lblNameSuffix);
		
		subscriberTypeCombo = new JComboBox<String>();
		subscriberTypeCombo.setBackground(Color.WHITE);
		subscriberTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"user", "institute"}));
		subscriberTypeCombo.setBounds(182, 97, 292, 20);
		basePanel.add(subscriberTypeCombo);
		
		JLabel lblBirthYear = new JLabel("Gateway ID:");
		lblBirthYear.setBounds(10, 129, 162, 14);
		basePanel.add(lblBirthYear);
		
		JLabel lblDeathYear = new JLabel("OTP Received:");
		lblDeathYear.setBounds(10, 160, 162, 14);
		basePanel.add(lblDeathYear);
		
		txtCustomerId = new JTextField();
		txtCustomerId.setBackground(Color.WHITE);
		txtCustomerId.setEditable(false);
		txtCustomerId.setDisabledTextColor(Color.BLACK);
		txtCustomerId.setColumns(10);
		txtCustomerId.setBounds(182, 126, 292, 20);
		basePanel.add(txtCustomerId);
		
		JLabel lblArtistAka = new JLabel("OTP Used:");
		lblArtistAka.setBounds(10, 188, 162, 14);
		basePanel.add(lblArtistAka);
		
		textOTP = new JTextField();
		textOTP.setBackground(Color.WHITE);
		textOTP.setEditable(false);
		textOTP.setDisabledTextColor(Color.BLACK);
		textOTP.setColumns(10);
		textOTP.setBounds(182, 157, 133, 20);
		basePanel.add(textOTP);
		
		JLabel lblNationality = new JLabel("Registered On:");
		lblNationality.setBounds(10, 213, 162, 14);
		basePanel.add(lblNationality);
		
		txtOTPUsed = new JTextField();
		txtOTPUsed.setBackground(Color.WHITE);
		txtOTPUsed.setEditable(false);
		txtOTPUsed.setDisabledTextColor(Color.BLACK);
		txtOTPUsed.setColumns(10);
		txtOTPUsed.setBounds(182, 185, 292, 20);
		basePanel.add(txtOTPUsed);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateUserAccount();
			}
		});
		btnUpdate.setMnemonic('U');
		btnUpdate.setBounds(57, 344, 170, 49);
		basePanel.add(btnUpdate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(251, 344, 170, 49);
		basePanel.add(btnCancel);
		
		textRegisteredOn = new JTextField();
		textRegisteredOn.setBackground(Color.WHITE);
		textRegisteredOn.setEditable(false);
		textRegisteredOn.setBounds(180, 213, 294, 20);
		basePanel.add(textRegisteredOn);
		textRegisteredOn.setColumns(10);
		
		JLabel lblNewPassword = new JLabel("New Password:");
		lblNewPassword.setBounds(10, 251, 162, 14);
		basePanel.add(lblNewPassword);
		
		textNewPassword = new JTextField();
		textNewPassword.setBounds(182, 244, 292, 20);
		basePanel.add(textNewPassword);
		textNewPassword.setColumns(10);
		
		JLabel lblConfirmNewPassword = new JLabel("Confirm New Password:");
		lblConfirmNewPassword.setBounds(10, 281, 162, 14);
		basePanel.add(lblConfirmNewPassword);
		
		textConfirmNewPassword = new JTextField();
		textConfirmNewPassword.setBounds(182, 275, 292, 20);
		basePanel.add(textConfirmNewPassword);
		textConfirmNewPassword.setColumns(10);
		
		JButton btnGenerateOtp = new JButton("Generate New OTP");
		btnGenerateOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateOtpButtonActionActionPerformed();
			}
		});
		btnGenerateOtp.setBounds(325, 156, 149, 23);
		basePanel.add(btnGenerateOtp);
		
	}

	protected void generateOtpButtonActionActionPerformed() {
		try {
			int otp = FineartUtils.generateOTP();
			String sql = "UPDATE user_accounts SET otp = '" + otp 
					+ "', otp_used = 'no'" 
					+ " WHERE user_accounts.user_id = '" + userId + "'";
			
			jdbcTemplate.update(sql);
			textOTP.setText(String.valueOf(otp));
			txtOTPUsed.setText("no");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}