package com.core.windows;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.fineart.FineartUtils;
import com.core.pojo.SaleSummaryRecord;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class AuctionDetailsWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private int auctionId;
	private JTextField txtAuctionID;
	private JTextField txtSaleTitle;
	private JTextField txtSaleCode;
	private JTextField txtAuctionHouseId;
	private JTextField txtLotCount;
	private JTextField txtSaleStart;
	private JTextField txtSaleEnd;
	private JTextField txtRecordCreated;
	private JTextField txtRecordUpdated;
	private JTextField textRecordCreatedBy;
	private JTextField txtRecordUpdatedBy;
	private MainWindow mainWindow;
	private JTextField txtAuctionHouse;
	private JTextField textAuctionHouseLocation;
	private JTextField textImageName;
	private JTextField textAuctionSource;
	
	public AuctionDetailsWindow(int auctionId, String username, JdbcTemplate jdbcTemplate, MainWindow mainWindow) {
		this.auctionId = auctionId;
		this.username = username;
		this.jdbcTemplate = jdbcTemplate;
		this.mainWindow = mainWindow;
		initGUI();
		loadData();
	}
	
	private void loadData() {
		String auctionDetailsSql = "SELECT core_auction_houses.cah_auction_house_name, core_auction_houses.cah_auction_house_location, fineart_auction_calendar.* "
				+ "FROM fineart_auction_calendar INNER JOIN core_auction_houses ON "
				+ "fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID "
				+ "WHERE fineart_auction_calendar.faac_auction_ID = '" + auctionId + "'";
		
		jdbcTemplate.query(auctionDetailsSql, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					txtAuctionID.setText(String.valueOf(resultSet.getInt("faac_auction_ID")));
					txtSaleCode.setText(resultSet.getString("faac_auction_sale_code"));
					txtAuctionHouseId.setText(String.valueOf(resultSet.getInt("faac_auction_house_ID")));
					txtAuctionHouse.setText(resultSet.getString("cah_auction_house_name"));
					textAuctionHouseLocation.setText(resultSet.getString("cah_auction_house_location"));
					txtSaleTitle.setText(resultSet.getString("faac_auction_title"));
					txtSaleTitle.setCaretPosition(0);
					txtSaleStart.setText(resultSet.getDate("faac_auction_start_date") == null ? "" :  resultSet.getDate("faac_auction_start_date").toString());
					txtSaleEnd.setText(resultSet.getDate("faac_auction_end_date") == null ? "" : resultSet.getDate("faac_auction_end_date").toString());
					txtLotCount.setText(String.valueOf(resultSet.getInt("faac_auction_lot_count")));
					txtRecordCreated.setText(resultSet.getDate("faac_auction_record_created").toString());
					txtRecordUpdated.setText(resultSet.getDate("faac_auction_record_updated") == null ? "" : resultSet.getDate("faac_auction_record_updated").toString());
					textRecordCreatedBy.setText(resultSet.getString("faac_auction_record_createdby"));
					txtRecordUpdatedBy.setText(resultSet.getString("faac_auction_record_updatedby") == null ? "" : resultSet.getString("faac_auction_record_updatedby"));
					textImageName.setText(resultSet.getString("faac_auction_image") == null ? "" : resultSet.getString("faac_auction_image"));
					textAuctionSource.setText(resultSet.getString("faac_auction_source") == null ? "" : resultSet.getString("faac_auction_source"));
				}
				return true;
			}
		});
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(700, 450));
		this.setSize(new Dimension(700, 450));
		this.setTitle("Auction Details");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		getContentPane().setLayout(null);
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		
		JLabel lblAuctionId = new JLabel("Auction ID:");
		lblAuctionId.setBounds(379, 42, 124, 20);
		getContentPane().add(lblAuctionId);
		
		txtAuctionID = new JTextField();
		txtAuctionID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionID.setDisabledTextColor(Color.LIGHT_GRAY);
		txtAuctionID.setEnabled(false);
		txtAuctionID.setBounds(513, 42, 158, 20);
		getContentPane().add(txtAuctionID);
		txtAuctionID.setColumns(10);
		
		JLabel lblSaleTitle = new JLabel("Sale Title:");
		lblSaleTitle.setBounds(10, 11, 124, 20);
		getContentPane().add(lblSaleTitle);
		
		txtSaleTitle = new JTextField();
		txtSaleTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSaleTitle.setColumns(10);
		txtSaleTitle.setBounds(187, 11, 484, 20);
		getContentPane().add(txtSaleTitle);
		
		JLabel lblSaleCode = new JLabel("Sale Code:");
		lblSaleCode.setBounds(10, 42, 124, 20);
		getContentPane().add(lblSaleCode);
		
		txtSaleCode = new JTextField();
		txtSaleCode.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSaleCode.setColumns(10);
		txtSaleCode.setBounds(187, 42, 158, 20);
		getContentPane().add(txtSaleCode);
		
		JLabel lblAuctionHouseId = new JLabel("Auction House ID:");
		lblAuctionHouseId.setBounds(10, 200, 124, 20);
		getContentPane().add(lblAuctionHouseId);
		
		txtAuctionHouseId = new JTextField();
		txtAuctionHouseId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouseId.setEnabled(false);
		txtAuctionHouseId.setColumns(10);
		txtAuctionHouseId.setBounds(187, 200, 158, 20);
		getContentPane().add(txtAuctionHouseId);
		
		JLabel lblLotCount = new JLabel("Lot Count:");
		lblLotCount.setBounds(379, 71, 124, 20);
		getContentPane().add(lblLotCount);
		
		txtLotCount = new JTextField();
		txtLotCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtLotCount.setDisabledTextColor(Color.LIGHT_GRAY);
		txtLotCount.setEnabled(false);
		txtLotCount.setColumns(10);
		txtLotCount.setBounds(513, 71, 158, 20);
		getContentPane().add(txtLotCount);
		
		JLabel lblSaleStart = new JLabel("Sale Start:");
		lblSaleStart.setBounds(10, 132, 167, 20);
		getContentPane().add(lblSaleStart);
		
		txtSaleStart = new JTextField();
		txtSaleStart.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSaleStart.setColumns(10);
		txtSaleStart.setBounds(187, 132, 158, 20);
		getContentPane().add(txtSaleStart);
		
		JLabel lblSaleEnd = new JLabel("Sale End:");
		lblSaleEnd.setBounds(10, 163, 167, 20);
		getContentPane().add(lblSaleEnd);
		
		txtSaleEnd = new JTextField();
		txtSaleEnd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSaleEnd.setColumns(10);
		txtSaleEnd.setBounds(187, 163, 158, 20);
		getContentPane().add(txtSaleEnd);
		
		JLabel lblRecordCreated = new JLabel("Record Created:");
		lblRecordCreated.setBounds(379, 101, 124, 20);
		getContentPane().add(lblRecordCreated);
		
		txtRecordCreated = new JTextField();
		txtRecordCreated.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtRecordCreated.setDisabledTextColor(Color.LIGHT_GRAY);
		txtRecordCreated.setEnabled(false);
		txtRecordCreated.setColumns(10);
		txtRecordCreated.setBounds(513, 101, 158, 20);
		getContentPane().add(txtRecordCreated);
		
		JLabel lblRecordUpdated = new JLabel("Record Updated:");
		lblRecordUpdated.setBounds(379, 165, 124, 20);
		getContentPane().add(lblRecordUpdated);
		
		txtRecordUpdated = new JTextField();
		txtRecordUpdated.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtRecordUpdated.setDisabledTextColor(Color.LIGHT_GRAY);
		txtRecordUpdated.setEnabled(false);
		txtRecordUpdated.setColumns(10);
		txtRecordUpdated.setBounds(513, 165, 158, 20);
		getContentPane().add(txtRecordUpdated);
		
		JLabel lblRecordCreatedBy = new JLabel("Record Created By:");
		lblRecordCreatedBy.setBounds(379, 132, 124, 20);
		getContentPane().add(lblRecordCreatedBy);
		
		textRecordCreatedBy = new JTextField();
		textRecordCreatedBy.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textRecordCreatedBy.setDisabledTextColor(Color.LIGHT_GRAY);
		textRecordCreatedBy.setEnabled(false);
		textRecordCreatedBy.setColumns(10);
		textRecordCreatedBy.setBounds(513, 132, 158, 20);
		getContentPane().add(textRecordCreatedBy);
		
		JLabel lblRecordUpdatedBy = new JLabel("Record Updated By:");
		lblRecordUpdatedBy.setBounds(379, 200, 124, 20);
		getContentPane().add(lblRecordUpdatedBy);
		
		txtRecordUpdatedBy = new JTextField();
		txtRecordUpdatedBy.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtRecordUpdatedBy.setDisabledTextColor(Color.LIGHT_GRAY);
		txtRecordUpdatedBy.setEnabled(false);
		txtRecordUpdatedBy.setColumns(10);
		txtRecordUpdatedBy.setBounds(513, 200, 158, 20);
		getContentPane().add(txtRecordUpdatedBy);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed();
			}
		});
		btnSave.setMnemonic('S');
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSave.setBounds(56, 352, 182, 48);
		getContentPane().add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancel.setBounds(248, 352, 182, 48);
		getContentPane().add(btnCancel);
		
		JLabel lblAuctionHouse = new JLabel("Auction House Name:");
		lblAuctionHouse.setBounds(10, 73, 167, 20);
		getContentPane().add(lblAuctionHouse);
		
		txtAuctionHouse = new JTextField();
		txtAuctionHouse.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAuctionHouse.setDisabledTextColor(Color.BLACK);
		txtAuctionHouse.setColumns(10);
		txtAuctionHouse.setBounds(187, 73, 158, 20);
		getContentPane().add(txtAuctionHouse);
		
		JLabel lblAuctionHouseLocation = new JLabel("Auction House Location:");
		lblAuctionHouseLocation.setBounds(10, 101, 167, 20);
		getContentPane().add(lblAuctionHouseLocation);
		
		textAuctionHouseLocation = new JTextField();
		textAuctionHouseLocation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textAuctionHouseLocation.setBounds(187, 104, 158, 20);
		getContentPane().add(textAuctionHouseLocation);
		textAuctionHouseLocation.setColumns(10);
		
		JLabel lblImageSource = new JLabel("Image Name:");
		lblImageSource.setBounds(10, 231, 167, 20);
		getContentPane().add(lblImageSource);
		
		textImageName = new JTextField();
		textImageName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textImageName.setDisabledTextColor(Color.LIGHT_GRAY);
		textImageName.setEnabled(false);
		textImageName.setBounds(187, 231, 484, 20);
		getContentPane().add(textImageName);
		textImageName.setColumns(10);
		
		JLabel lblAuctionSource = new JLabel("Auction Source:");
		lblAuctionSource.setBounds(10, 262, 167, 20);
		getContentPane().add(lblAuctionSource);
		
		textAuctionSource = new JTextField();
		textAuctionSource.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textAuctionSource.setBounds(187, 262, 484, 20);
		getContentPane().add(textAuctionSource);
		textAuctionSource.setColumns(10);
		
		JButton deleteSaleButton = new JButton("Delete Sale");
		deleteSaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSaleButtonActionPerformed();
			}
		});
		deleteSaleButton.setForeground(Color.RED);
		deleteSaleButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		deleteSaleButton.setBounds(513, 352, 158, 48);
		getContentPane().add(deleteSaleButton);
	}

	protected void deleteSaleButtonActionPerformed() {
		try {
			String ObjButtons[] = {"Yes","No"};
	        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure to delete this sale?", "Delete Sale Warning", 
	        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
	        if(PromptResult==JOptionPane.YES_OPTION) {
	        	String auctionId = txtAuctionID.getText().trim();
				String deletSaleSql = "DELETE fineart_auction_calendar, fineart_lots, fineart_artworks FROM `fineart_auction_calendar` "
						+ "LEFT JOIN `fineart_lots` ON `fineart_auction_calendar`.`faac_auction_ID` = `fineart_lots`.`fal_auction_ID` "
						+ "LEFT JOIN `fineart_artworks` ON `fineart_lots`.`fal_artwork_ID` = `fineart_artworks`.`faa_artwork_ID` "
						+ "WHERE fineart_auction_calendar.faac_auction_ID = " + auctionId;
				
				jdbcTemplate.update(deletSaleSql);
				JOptionPane.showMessageDialog(this, "Sale Deleted.");
				this.setVisible(false);
				this.dispose();
	        }
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
			return;
		}
	}

	protected void saveButtonActionPerformed() {
		String saleCode = txtSaleCode.getText().trim();
		String auctionHouseId = txtAuctionHouseId.getText().trim();
		String saleTitle = txtSaleTitle.getText().trim();
		String saleStart = txtSaleStart.getText().trim();
		String saleEnd = StringUtils.isEmpty(txtSaleEnd.getText().trim()) ? "0000-00-00" : txtSaleEnd.getText().trim();
		String auctionHouseName = txtAuctionHouse.getText().trim();
		String auctionHouseLocation = textAuctionHouseLocation.getText().trim();
		String auctionSource = textAuctionSource.getText().trim();
		
		if(StringUtils.isEmpty(saleCode)) {
			JOptionPane.showMessageDialog(this, "Sale code cannot be blank.");
			return;
		}
		if(StringUtils.isEmpty(auctionHouseId)) {
			JOptionPane.showMessageDialog(this, "Auction house ID cannot be blank.");
			return;
		}
		if(StringUtils.isEmpty(saleTitle)) {
			JOptionPane.showMessageDialog(this, "Sale title cannot be blank.");
			return;
		}
		if(StringUtils.isEmpty(saleStart)) {
			JOptionPane.showMessageDialog(this, "Sale start cannot be blank.");
			return;
		}
		if(StringUtils.isEmpty(auctionHouseName)) {
			JOptionPane.showMessageDialog(this, "Auction House Name cannot be blank.");
			return;
		}
		if(StringUtils.isEmpty(auctionHouseLocation)) {
			JOptionPane.showMessageDialog(this, "Auction House Location cannot be blank.");
			return;
		}
		
		String auctionHouseIdQuery = "SELECT cah_auction_house_ID FROM core_auction_houses "
				+ "WHERE cah_auction_house_name = '" + FineartUtils.getSqlSafeString(auctionHouseName) 
				+ "' AND cah_auction_house_location = '" + FineartUtils.getSqlSafeString(auctionHouseLocation) + "'";
		
		Integer auctionHouseIdInt = 0;
		
		try {
			auctionHouseIdInt = (Integer)jdbcTemplate.queryForObject(auctionHouseIdQuery, Integer.class);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			JOptionPane.showMessageDialog(this, "Auction House Name or Location does not exist in the database. Please try again.");
			return;
		}
		
		
		String updateQuery = "update fineart_auction_calendar SET faac_auction_sale_code = '" + saleCode + "', faac_auction_house_ID = '" + auctionHouseIdInt 
				+ "', faac_auction_title = '" + FineartUtils.getSqlSafeString(saleTitle) + "', faac_auction_start_date = '" + saleStart + "', faac_auction_end_date = '" + saleEnd 
				+ "', faac_auction_record_updated = '" + new Timestamp(System.currentTimeMillis()) 
				+ "', faac_auction_record_updatedby = '" + username + "', faac_auction_source = '" + auctionSource + "' where faac_auction_ID = '" + auctionId + "'";
		
		jdbcTemplate.update(updateQuery);
		
		mainWindow.loadTableData(mainWindow.getFromRow(), mainWindow.getToRow());
		
		this.setVisible(false);
		this.dispose();
	}	
}
