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

import org.apache.commons.lang3.StringUtils;
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

public class AddAuctionHouseWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private JTextField txtAuctionHouseName;
	private JTextField txtAuctionHouseCountry;
	private JTextField txtAuctionHouseLocation;
	private JTextField txtAuctionHouseCurrency;
	private JTextField txtAuctionHouseWebsite;
	private String auctionHouseId;
	
	

	public AddAuctionHouseWindow(String username, JdbcTemplate jdbcTemplate, AuctionHouseSearchWindow auctionHouseSearchWindow) {
		this.username = username;
		this.jdbcTemplate = jdbcTemplate;
		initGUI();
	}

	protected void addAuctionHouse() {
		try {
			String auctionHouseName = FineartUtils.getSqlSafeString(txtAuctionHouseName.getText().trim());
			String auctionHouseCountry = FineartUtils.getSqlSafeString(txtAuctionHouseCountry.getText().trim());
			String auctionHouseLocation = FineartUtils.getSqlSafeString(txtAuctionHouseLocation.getText().trim());
			String auctionHouseCurrencyCode = FineartUtils.getSqlSafeString(txtAuctionHouseCurrency.getText().trim());
			String auctionHouseWebsite = FineartUtils.getSqlSafeString(txtAuctionHouseWebsite.getText().trim());
			
			if(StringUtils.isEmpty(auctionHouseName)) {
				JOptionPane.showMessageDialog(null, "Name cannot be left blank");
				return;
			}
			if(StringUtils.isEmpty(auctionHouseCountry)) {
				JOptionPane.showMessageDialog(null, "Country cannot be left blank");
				return;
			}
			if(StringUtils.isEmpty(auctionHouseLocation)) {
				JOptionPane.showMessageDialog(null, "Location cannot be left blank");
				return;
			}
			if(StringUtils.isEmpty(auctionHouseCurrencyCode)) {
				JOptionPane.showMessageDialog(null, "Currency code cannot be left blank");
				return;
			}
			if(auctionHouseCurrencyCode.length() > 3) {
				JOptionPane.showMessageDialog(null, "Currency code cannot be more than 3 letters.");
				return;
			}
			
			
			String query = "INSERT INTO core_auction_houses (cah_auction_house_name, cah_auction_house_country, cah_auction_house_location, "
					+ "cah_auction_house_currency_code, cah_auction_house_website, cah_auction_house_record_created, cah_auction_house_record_createdby) "
					+ "VALUES ('" + auctionHouseName + "', '" + auctionHouseCountry + "', '" + auctionHouseLocation + "', '" + auctionHouseCurrencyCode 
					+ "', '" + auctionHouseWebsite + "', CURRENT_TIMESTAMP, '" + username + "') ";
			
			jdbcTemplate.update(query);
			
			this.setVisible(false);
            this.dispose();
            
        } catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setSize(new Dimension(500, 344));
		this.setTitle("Add Auction House");
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
		
		JLabel lblNamePrefix = new JLabel("Auction House Name:");
		lblNamePrefix.setBounds(10, 39, 162, 14);
		basePanel.add(lblNamePrefix);
		
		txtAuctionHouseName = new JTextField();
		txtAuctionHouseName.setDisabledTextColor(Color.LIGHT_GRAY);
		txtAuctionHouseName.setColumns(10);
		txtAuctionHouseName.setBounds(182, 36, 292, 20);
		basePanel.add(txtAuctionHouseName);
		
		JLabel lblNameSuffix = new JLabel("Auction House Country:");
		lblNameSuffix.setBounds(10, 67, 162, 14);
		basePanel.add(lblNameSuffix);
		
		txtAuctionHouseCountry = new JTextField();
		txtAuctionHouseCountry.setDisabledTextColor(Color.LIGHT_GRAY);
		txtAuctionHouseCountry.setColumns(10);
		txtAuctionHouseCountry.setBounds(182, 64, 292, 20);
		basePanel.add(txtAuctionHouseCountry);
		
		JLabel lblBirthYear = new JLabel("Auction House Location:");
		lblBirthYear.setBounds(10, 95, 162, 14);
		basePanel.add(lblBirthYear);
		
		txtAuctionHouseLocation = new JTextField();
		txtAuctionHouseLocation.setColumns(10);
		txtAuctionHouseLocation.setBounds(182, 92, 292, 20);
		basePanel.add(txtAuctionHouseLocation);
		
		JLabel lblDeathYear = new JLabel("Auction House Currency:");
		lblDeathYear.setBounds(10, 126, 162, 14);
		basePanel.add(lblDeathYear);
		
		txtAuctionHouseCurrency = new JTextField();
		txtAuctionHouseCurrency.setColumns(10);
		txtAuctionHouseCurrency.setBounds(182, 123, 292, 20);
		basePanel.add(txtAuctionHouseCurrency);
		
		JLabel lblNationality = new JLabel("Auction House Website:");
		lblNationality.setBounds(10, 154, 162, 14);
		basePanel.add(lblNationality);
		
		txtAuctionHouseWebsite = new JTextField();
		txtAuctionHouseWebsite.setColumns(10);
		txtAuctionHouseWebsite.setBounds(182, 151, 292, 20);
		basePanel.add(txtAuctionHouseWebsite);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAuctionHouse();
			}
		});
		btnAdd.setMnemonic('U');
		btnAdd.setBounds(70, 224, 170, 49);
		basePanel.add(btnAdd);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(264, 224, 170, 49);
		basePanel.add(btnCancel);
	}	
}