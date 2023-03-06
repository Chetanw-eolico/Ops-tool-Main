package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.fineart.FineartUtils;

import java.awt.Color;
import java.awt.Font;

public class EditAuctionHouseWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private AuctionHouseSearchWindow auctionHouseSearchWindow;
	private JTextField txtAuctionHouseId;
	private JTextField txtAuctionHouseName;
	private JTextField txtAuctionHouseCountry;
	private JTextField txtAuctionHouseLocation;
	private JTextField txtAuctionHouseCurrency;
	private JTextField textRecordCreatedOn;
	private JTextField txtAuctionHouseWebsite;
	private JTextField textRecordCreatedBy;
	private String auctionHouseId;
	private JTextField textRecordUpdatedOn;
	private JTextField textRecordUpdatedBy;
	
	

	public EditAuctionHouseWindow(String username, JdbcTemplate jdbcTemplate, AuctionHouseSearchWindow auctionHouseSearchWindow) {
		this.username = username;
		this.jdbcTemplate = jdbcTemplate;
		this.auctionHouseSearchWindow = auctionHouseSearchWindow;
		initGUI();
		loadArtistData();
	}

	private void loadArtistData() {
		try {
			auctionHouseId = auctionHouseSearchWindow.getTable().getValueAt(auctionHouseSearchWindow.getTable().getSelectedRow(), 0).toString();
			String artistDataQuery = "select * from core_auction_houses where cah_auction_house_ID = " + auctionHouseId;
			
			jdbcTemplate.query(artistDataQuery, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						txtAuctionHouseId.setText(resultSet.getString("cah_auction_house_ID"));
						txtAuctionHouseName.setText(resultSet.getString("cah_auction_house_name"));
						txtAuctionHouseCountry.setText(resultSet.getString("cah_auction_house_country"));
						txtAuctionHouseLocation.setText(resultSet.getString("cah_auction_house_location"));
						txtAuctionHouseCurrency.setText(resultSet.getString("cah_auction_house_currency_code"));
						txtAuctionHouseWebsite.setText(resultSet.getString("cah_auction_house_website"));
						textRecordCreatedOn.setText(resultSet.getString("cah_auction_house_record_created"));
						textRecordCreatedBy.setText(resultSet.getString("cah_auction_house_record_createdby"));
						textRecordUpdatedOn.setText(resultSet.getString("cah_auction_house_record_updated"));
						textRecordUpdatedBy.setText(resultSet.getString("cah_auction_house_record_updatedby"));
					}
					return "";
				}
			});
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateAuctionHouse() {
		try {
			String auctionHouseLocation = FineartUtils.getSqlSafeString(txtAuctionHouseLocation.getText().trim().equals("") ? "0" : txtAuctionHouseLocation.getText().trim());
			String auctionHouseCurrencyCode = FineartUtils.getSqlSafeString(txtAuctionHouseCurrency.getText().trim().equals("") ? "0" : txtAuctionHouseCurrency.getText().trim());
			String auctionHouseWebsite = FineartUtils.getSqlSafeString(txtAuctionHouseWebsite.getText().trim().equals("") ? "na" : txtAuctionHouseWebsite.getText().trim());
			
			String query = "UPDATE core_auction_houses SET cah_auction_house_location = '" + auctionHouseLocation 
					+ "', cah_auction_house_currency_code = '" + auctionHouseCurrencyCode 
					+ "', cah_auction_house_website = '" + auctionHouseWebsite 
					+ "', cah_auction_house_record_updated = CURRENT_TIMESTAMP, cah_auction_house_record_updatedby = '" + username 
					+ "' WHERE core_auction_houses.cah_auction_house_ID = '" + auctionHouseId + "'";
			
			jdbcTemplate.update(query);
			
			this.setVisible(false);
            this.dispose();
            
        } catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setSize(new Dimension(500, 436));
		this.setTitle("Edit Auction House");
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
		
		JLabel lblNewLabel = new JLabel("Auction House Id:");
		lblNewLabel.setBounds(10, 11, 162, 14);
		basePanel.add(lblNewLabel);
		
		txtAuctionHouseId = new JTextField();
		txtAuctionHouseId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseId.setDisabledTextColor(Color.LIGHT_GRAY);
		txtAuctionHouseId.setEnabled(false);
		txtAuctionHouseId.setBounds(182, 8, 292, 20);
		basePanel.add(txtAuctionHouseId);
		txtAuctionHouseId.setColumns(10);
		
		JLabel lblNamePrefix = new JLabel("Auction House Name:");
		lblNamePrefix.setBounds(10, 39, 162, 14);
		basePanel.add(lblNamePrefix);
		
		txtAuctionHouseName = new JTextField();
		txtAuctionHouseName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseName.setEnabled(false);
		txtAuctionHouseName.setDisabledTextColor(Color.LIGHT_GRAY);
		txtAuctionHouseName.setColumns(10);
		txtAuctionHouseName.setBounds(182, 36, 292, 20);
		basePanel.add(txtAuctionHouseName);
		
		JLabel lblNameSuffix = new JLabel("Auction House Country:");
		lblNameSuffix.setBounds(10, 67, 162, 14);
		basePanel.add(lblNameSuffix);
		
		txtAuctionHouseCountry = new JTextField();
		txtAuctionHouseCountry.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseCountry.setEnabled(false);
		txtAuctionHouseCountry.setDisabledTextColor(Color.LIGHT_GRAY);
		txtAuctionHouseCountry.setColumns(10);
		txtAuctionHouseCountry.setBounds(182, 64, 292, 20);
		basePanel.add(txtAuctionHouseCountry);
		
		JLabel lblBirthYear = new JLabel("Auction House Location:");
		lblBirthYear.setBounds(10, 95, 162, 14);
		basePanel.add(lblBirthYear);
		
		txtAuctionHouseLocation = new JTextField();
		txtAuctionHouseLocation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseLocation.setColumns(10);
		txtAuctionHouseLocation.setBounds(182, 92, 292, 20);
		basePanel.add(txtAuctionHouseLocation);
		
		JLabel lblDeathYear = new JLabel("Auction House Currency:");
		lblDeathYear.setBounds(10, 126, 162, 14);
		basePanel.add(lblDeathYear);
		
		txtAuctionHouseCurrency = new JTextField();
		txtAuctionHouseCurrency.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseCurrency.setColumns(10);
		txtAuctionHouseCurrency.setBounds(182, 123, 292, 20);
		basePanel.add(txtAuctionHouseCurrency);
		
		JLabel lblArtistAka = new JLabel("Record Created On:");
		lblArtistAka.setBounds(10, 182, 162, 14);
		basePanel.add(lblArtistAka);
		
		textRecordCreatedOn = new JTextField();
		textRecordCreatedOn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textRecordCreatedOn.setDisabledTextColor(Color.LIGHT_GRAY);
		textRecordCreatedOn.setEnabled(false);
		textRecordCreatedOn.setColumns(10);
		textRecordCreatedOn.setBounds(182, 179, 292, 20);
		basePanel.add(textRecordCreatedOn);
		
		JLabel lblNationality = new JLabel("Auction House Website:");
		lblNationality.setBounds(10, 154, 162, 14);
		basePanel.add(lblNationality);
		
		txtAuctionHouseWebsite = new JTextField();
		txtAuctionHouseWebsite.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseWebsite.setColumns(10);
		txtAuctionHouseWebsite.setBounds(182, 151, 292, 20);
		basePanel.add(txtAuctionHouseWebsite);
		
		JLabel lblDescription = new JLabel("Record Created By:");
		lblDescription.setBounds(10, 211, 162, 14);
		basePanel.add(lblDescription);
		
		textRecordCreatedBy = new JTextField();
		textRecordCreatedBy.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textRecordCreatedBy.setDisabledTextColor(Color.LIGHT_GRAY);
		textRecordCreatedBy.setEnabled(false);
		textRecordCreatedBy.setColumns(10);
		textRecordCreatedBy.setBounds(182, 208, 292, 20);
		basePanel.add(textRecordCreatedBy);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAuctionHouse();
			}
		});
		btnUpdate.setMnemonic('U');
		btnUpdate.setBounds(60, 324, 170, 49);
		basePanel.add(btnUpdate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(254, 324, 170, 49);
		basePanel.add(btnCancel);
		
		JLabel lblRecordUpdatedOn = new JLabel("Record Updated On:");
		lblRecordUpdatedOn.setBounds(10, 236, 162, 20);
		basePanel.add(lblRecordUpdatedOn);
		
		textRecordUpdatedOn = new JTextField();
		textRecordUpdatedOn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textRecordUpdatedOn.setDisabledTextColor(Color.LIGHT_GRAY);
		textRecordUpdatedOn.setEnabled(false);
		textRecordUpdatedOn.setBounds(182, 239, 292, 20);
		basePanel.add(textRecordUpdatedOn);
		textRecordUpdatedOn.setColumns(10);
		
		JLabel lblRecordUpdatedBy = new JLabel("Record Updated By:");
		lblRecordUpdatedBy.setBounds(10, 267, 162, 20);
		basePanel.add(lblRecordUpdatedBy);
		
		textRecordUpdatedBy = new JTextField();
		textRecordUpdatedBy.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textRecordUpdatedBy.setDisabledTextColor(Color.LIGHT_GRAY);
		textRecordUpdatedBy.setEnabled(false);
		textRecordUpdatedBy.setBounds(182, 270, 292, 20);
		basePanel.add(textRecordUpdatedBy);
		textRecordUpdatedBy.setColumns(10);
	}	
}