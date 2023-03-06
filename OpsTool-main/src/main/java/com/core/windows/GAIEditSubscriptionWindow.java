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
import java.sql.ResultSet;
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
import org.jsoup.internal.StringUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.core.fineart.FineartUtils;

public class GAIEditSubscriptionWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private GAIUserSubscriptionsWindow gAIUserSubscriptionsWindow;
	private JTextField txtUserId;
	private JTextField txtUserName;
	private JTextField textPaymentAmount;
	private JTextField textUserEmail;
	private JComboBox<String> subscriptionStatusCombo;
	private String userId;
	private String userName;
	private String userEmail;
	private JTextField txtPaymentId;
	private JComboBox<String> autoRenewCombo;
	
	public GAIEditSubscriptionWindow(String username, String userId, String userName, String userEmail, JdbcTemplate jdbcTemplate, 
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
			
			String query = "select * from subscription where user_id = " + userId;
			
			jdbcTemplate.query(query, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						subscriptionStatusCombo.setSelectedItem(resultSet.getString("subscription_status"));
						autoRenewCombo.setSelectedItem(resultSet.getString("subscription_autorenew"));
						txtPaymentId.setText(resultSet.getString("payment_ID"));
						textPaymentAmount.setText(resultSet.getString("payment_amount"));
					}
					return "";
				}
			});
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void editSubscription() {
		try {
			String subscriptionId = gAIUserSubscriptionsWindow.getTable().getValueAt(gAIUserSubscriptionsWindow.getTable().getSelectedRow(), 0).toString();
			String subscriptionStatus = subscriptionStatusCombo.getSelectedItem().toString();
			String autoRenew = autoRenewCombo.getSelectedItem().toString();
			String paymentId = StringUtils.isEmpty(txtPaymentId.getText().trim()) ? "na" : txtPaymentId.getText().trim();
			Float paymentAmount = Float.valueOf(textPaymentAmount.getText().trim());
				
			if(StringUtils.isEmpty(paymentId)) {
				JOptionPane.showMessageDialog(this,"Payment ID cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String query = "UPDATE subscription SET subscription_status = '" + subscriptionStatus 
					+ "', subscription_autorenew = '" + autoRenew 
					+ "', payment_ID = '" + paymentId 
					+ "', payment_amount = '" + paymentAmount 
					+ "', subscription_record_updated = CURRENT_TIMESTAMP" 
					+ " WHERE subscription.id = '" + subscriptionId + "'";
			
			jdbcTemplate.update(query);
			
			gAIUserSubscriptionsWindow.refreshTable();
			this.setVisible(false);
            this.dispose();
            
        } catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
		}
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setSize(new Dimension(500, 354));
		this.setTitle("Edit Subscription");
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
		
		JLabel lblNameSuffix = new JLabel("Subscription Status:");
		lblNameSuffix.setBounds(10, 100, 162, 14);
		basePanel.add(lblNameSuffix);
		
		subscriptionStatusCombo = new JComboBox<String>();
		subscriptionStatusCombo.setBackground(Color.WHITE);
		subscriptionStatusCombo.setModel(new DefaultComboBoxModel(new String[] {"valid", "invalid"}));
		subscriptionStatusCombo.setBounds(182, 97, 292, 20);
		basePanel.add(subscriptionStatusCombo);
		
		JLabel lblBirthYear = new JLabel("Auto Renew:");
		lblBirthYear.setBounds(10, 129, 162, 14);
		basePanel.add(lblBirthYear);
		
		JLabel lblDeathYear = new JLabel("Payment Amount:");
		lblDeathYear.setBounds(10, 192, 162, 14);
		basePanel.add(lblDeathYear);
		
		textPaymentAmount = new JTextField();
		textPaymentAmount.setBackground(Color.WHITE);
		textPaymentAmount.setDisabledTextColor(Color.BLACK);
		textPaymentAmount.setColumns(10);
		textPaymentAmount.setBounds(182, 187, 292, 20);
		basePanel.add(textPaymentAmount);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSubscription();
			}
		});
		btnSave.setMnemonic('A');
		btnSave.setBounds(62, 237, 170, 49);
		basePanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(256, 237, 170, 49);
		basePanel.add(btnCancel);
		
		autoRenewCombo = new JComboBox<String>();
		autoRenewCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"false", "true"}));
		autoRenewCombo.setBackground(Color.WHITE);
		autoRenewCombo.setBounds(182, 126, 292, 20);
		basePanel.add(autoRenewCombo);
		
		JLabel lblPaymentId = new JLabel("Payment ID:");
		lblPaymentId.setBounds(10, 157, 162, 14);
		basePanel.add(lblPaymentId);
		
		txtPaymentId = new JTextField();
		txtPaymentId.setBounds(182, 156, 293, 20);
		basePanel.add(txtPaymentId);
		txtPaymentId.setColumns(10);
		
	}
}