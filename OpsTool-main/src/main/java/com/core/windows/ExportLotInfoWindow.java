package com.core.windows;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTextField;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.pojo.SaleSummaryRecord;
import com.toedter.calendar.JDateChooser;

import au.com.bytecode.opencsv.CSVWriter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ExportLotInfoWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private JDateChooser startDateChooser;
	private JDateChooser endDateChooser;
	private JTextField aucHouseName;
	private JTextField artistName;
	private JTextField saleCode;
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ExportLotInfoWindow(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Export Lot Info Report");
		lblNewLabel.setBounds(262, 11, 197, 19);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		getContentPane().add(lblNewLabel);
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		       setVisible(false);
	            dispose();
		    }
		});
		
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setSize(721, 169);
			this.setTitle("Operations Tool");
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			this.setPreferredSize(new Dimension(400, 200));
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
			
			{
				aucHouseName = new JTextField();
				getContentPane().add(aucHouseName);
				aucHouseName.setFont(new java.awt.Font("Segoe UI",0,14));
				aucHouseName.setBounds(76, 52, 173, 40);
			}			
						
			{
				startDateChooser = new JDateChooser();
				getContentPane().add(startDateChooser);
				startDateChooser.setFont(new java.awt.Font("Segoe UI",0,14));
				startDateChooser.setBounds(76, 52, 173, 40);
			}
			
			{
				endDateChooser = new JDateChooser();
				endDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				endDateChooser.setBounds(298, 52, 173, 40);
				getContentPane().add(endDateChooser);
			}

			{
				artistName = new JTextField();
				getContentPane().add(artistName);
				artistName.setFont(new java.awt.Font("Segoe UI",0,14));
				artistName.setBounds(76, 52, 173, 40);
			}

			{
				saleCode = new JTextField();
				getContentPane().add(saleCode);
				saleCode.setFont(new java.awt.Font("Segoe UI",0,14));
				saleCode.setBounds(76, 52, 173, 40);
			}

			JLabel lblAucHouse = new JLabel("Auction House Name:");
			lblAucHouse.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblAucHouse.setBounds(26, 64, 55, 19);
			getContentPane().add(lblAucHouse);
			
			JLabel lblStart = new JLabel("Start Date:");
			lblStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblStart.setBounds(26, 64, 55, 19);
			getContentPane().add(lblStart);
			
			JLabel lblEnd = new JLabel("End Date:");
			lblEnd.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblEnd.setBounds(264, 64, 39, 19);
			getContentPane().add(lblEnd);
			
			JLabel lblArtist = new JLabel("Artist Name:");
			lblArtist.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblArtist.setBounds(26, 64, 55, 19);
			getContentPane().add(lblArtist);

			JLabel lblSalecode = new JLabel("Sale Code:");
			lblSalecode.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblSalecode.setBounds(26, 64, 55, 19);
			getContentPane().add(lblSalecode);

			JButton btnNewButton = new JButton("Export");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportLotInfoButtonActionPerformed();
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
			btnNewButton.setBounds(504, 52, 191, 40);
			getContentPane().add(btnNewButton);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void exportLotInfoButtonActionPerformed() {
		try {
			
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			jfc.setFileFilter(new FileNameExtensionFilter("csv files only","csv"));
			File file;
			
			int returnVal = jfc.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = jfc.getSelectedFile();
			} else {
				return;
			}
			
			CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file)));
			writeLotInfoCsvFile(csvWriter);
			
			String aucstartdate = new SimpleDateFormat("yyyy-MM-dd").format(startDateChooser.getDate());
			String aucenddate = new SimpleDateFormat("yyyy-MM-dd").format(endDateChooser.getDate());
                        String auchouse = aucHouseName.getText();
                        String artistname = artistName.getText();
			String aucid = saleCode.getText();
			
			String lotInfoQuery = "SELECT lot.fal_lot_ID AS 'Lot_ID', "
		+ "cah.cah_auction_house_name AS auction_house_name, "
		+ "cah.cah_auction_house_location AS auction_location, "
		+ "fac.faac_auction_ID AS auction_num, "
		+ "fac.faac_auction_start_date AS auction_start_date, "
		+ "fac.faac_auction_end_date AS auction_end_date, "
		+ "fac.faac_auction_title AS auction_name, "
		+ "lot.fal_lot_no AS lot_num, "
		+ "lot.fal_sub_lot_no AS sublot_num, "
		+ "lot.fal_lot_price_type AS price_kind, "
		+ "lot.fal_lot_low_estimate AS price_estimate_min, "
		+ "lot.fal_lot_high_estimate AS price_estimate_max, "
		+ "lot.fal_lot_sale_price AS price_sold, "
		+ "fartist.fa_artist_name AS artist_name, "
		+ "fartist.fa_artist_birth_year AS artist_birth, "
		+ "fartist.fa_artist_death_year AS artist_death, "
		+ "fartist.fa_artist_nationality AS artist_nationality, "
		+ "fart.faa_artwork_title AS artwork_name, "
		+ "fart.faa_artwork_start_year_identifier AS artwork_year_identifier, "
		+ "fart.faa_artwork_start_year AS artwork_start_year, "
		+ "fart.faa_artwork_end_year AS artwork_end_year, "
		+ "fart.faa_artwork_material AS artwork_materials, "
		+ "fart.faa_artwork_category AS artwork_category, "
		+ "fart.faa_artwork_markings AS artwork_markings, "
		+ "fart.faa_artwork_edition AS artwork_edition, "
		+ "fart.faa_artwork_description AS artwork_description, "
		+ "fart.faa_artwork_height AS artwork_measurements_height, "
		+ "fart.faa_artwork_width AS artwork_measurements_width, "
		+ "fart.faa_artwork_depth AS artwork_measurements_depth, "
		+ "fart.faa_artwork_size_details AS artwork_size_notes, "
		+ "fart.faa_arwork_measurement_unit AS auction_measureunit, "
		+ "lot.fal_lot_condition AS artwork_condition_in, "
		+ "lot.fal_lot_provenance AS artwork_provenance, "
		+ "fart.faa_artwork_exhibition AS artwork_exhibited, "
		+ "fart.faa_artwork_literature AS artwork_literature, "
		+ "NULL AS artwork_images1, "
		+ "NULL AS artwork_images2, "
		+ "NULL AS artwork_images3, "
		+ "NULL AS artwork_images4, "
		+ "NULL AS artwork_images5, "
		+ "lot.fal_lot_image1 AS image1_name, "
		+ "lot.fal_lot_image2 AS image2_name, "
		+ "lot.fal_lot_image3 AS image3_name, "
		+ "lot.fal_lot_image4 AS image4_name, "
		+ "lot.fal_lot_image5 AS image5_name "
		+ "FROM fineart_lots lot "
	    + "LEFT OUTER JOIN fineart_auction_calendar fac "
	    + "ON (lot.fal_auction_ID=fac.faac_auction_ID) "
	    + "LEFT OUTER JOIN core_auction_houses cah "
	    + "ON (fac.faac_auction_house_ID=cah.cah_auction_house_ID) "
	    + "LEFT OUTER JOIN fineart_artworks fart "
	    + "ON (fart.faa_artwork_ID =  lot.fal_artwork_ID) "
	    + "LEFT OUTER JOIN fineart_artists fartist "
	    + "ON (fart.faa_artist_ID= fartist.fa_artist_ID ) "
	    + "WHERE cah.cah_auction_house_name LIKE '%" + auchouse + "%' "
	    + "AND fac.faac_auction_start_date LIKE '%" + aucstartdate + "%' "
	    + "AND fac.faac_auction_end_date LIKE '%" + aucenddate + "%' "
	    + "AND fac.faac_auction_ID LIKE '%" + aucid + "%' "
	    + "AND fartist.fa_artist_name LIKE '%" + artistname + "%' ;";
			
			jdbcTemplate.query(lotInfoQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					try {
						while (resultSet.next()) {
							SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String outputDateFormat = "dd-MMM-yy";
							
							String startDateDb = resultSet.getString("auction_start_date");
							String endDateDb = resultSet.getString("auction_end_date");
							
							String auctionStartDate = "";
							String auctionEndDate = "";
							
							if(!startDateDb.equals("0000-00-00")) {
								Date auctionStartDateDate = inputDateFormat.parse(startDateDb);
								auctionStartDate = new SimpleDateFormat(outputDateFormat).format(auctionStartDateDate);
							} else {
								auctionStartDate = startDateDb;
							}
							if(!endDateDb.equals("0000-00-00")) {
								Date auctionEndDateDate = inputDateFormat.parse(endDateDb);
								auctionEndDate = new SimpleDateFormat(outputDateFormat).format(auctionEndDateDate);
							} else {
								auctionEndDate = endDateDb;
							}
							
							String lotDetails = resultSet.getString("Lot_ID") + "|" + resultSet.getString("auction_house_name") + "|" + resultSet.getString("auction_location")  + "|" + resultSet.getString("auction_num") + "|" + auctionStartDate + "|" + auctionEndDate + "|" + resultSet.getString("auction_name")  + "|" + resultSet.getString("lot_num")   + "|" + resultSet.getString("sublot_num") + "|" + resultSet.getString("price_kind") + "|" + resultSet.getString("price_estimate_min") + "|" + resultSet.getString("price_estimate_max") + "|" + resultSet.getString("price_sold")  + "|" + resultSet.getString("artist_name") + "|" + resultSet.getString("artist_birth") + "|" + resultSet.getString("artist_death") + "|" + resultSet.getInt("artist_nationality") + "|" + resultSet.getInt("artwork_name") + "|" + resultSet.getString("artwork_year_identifier") + "|" + resultSet.getString("artwork_start_year") + "|" + resultSet.getString("artwork_end_year") + "|" + resultSet.getString("artwork_materials")  + "|" + resultSet.getString("artwork_category")  + "|" + resultSet.getString("artwork_markings") + "|" + resultSet.getString("artwork_edition") + "|" + resultSet.getString("artwork_description") + "|" + resultSet.getString("artwork_measurements_height") + "|" + resultSet.getString("artwork_measurements_width") + "|" + resultSet.getString("artwork_measurements_depth") + "|" + resultSet.getString("artwork_size_notes") + "|" + resultSet.getString("auction_measureunit") + "|" + resultSet.getString("artwork_condition_in") + "|" + resultSet.getString("artwork_provenance") + "|" + resultSet.getString("artwork_exhibited") + "|" + resultSet.getString("artwork_literature") + "|" + resultSet.getString("artwork_images1") + "|" + resultSet.getString("artwork_images2") + "|" + resultSet.getString("artwork_images3") + "|" + resultSet.getString("artwork_images4") + "|" + resultSet.getString("artwork_images5") + "|" + resultSet.getString("image1_name") + "|" + resultSet.getString("image2_name") + "|" + resultSet.getString("image3_name") + "|" + resultSet.getString("image4_name") + "|" + resultSet.getString("image5_name");

							String row[] = lotDetails.split("\\|");
							csvWriter.writeNext(row);
							csvWriter.flush();
						}
						csvWriter.close();
						return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
			});
			JOptionPane.showMessageDialog(this, "Export Done");
			this.setVisible(false);
			this.dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception Occurred. Message is: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void writeLotInfoCsvFile(CSVWriter csvWriter) {
		try {
			StringBuilder stats = new StringBuilder();
			stats.append("Lot_ID|auction_house_name|auction_location|auction_num|auction_start_date|auction_end_date|auction_name|lot_num|sublot_num|price_kind|price_estimate_min|price_estimate_max|price_sold|artist_name|artist_birth|artist_death|artist_nationality|artwork_name|artwork_year_identifier|artwork_start_year|artwork_end_year|artwork_materials|artwork_category|artwork_markings|artwork_edition|artwork_description|artwork_measurements_height|artwork_measurements_width|artwork_measurements_depth|artwork_size_notes|auction_measureunit|artwork_condition_in|artwork_provenance|artwork_exhibited|artwork_literature|artwork_images1|artwork_images2|artwork_images3|artwork_images4|artwork_images5|image1_name|image2_name|image3_name|image4_name|image5_name");
			csvWriter.writeNext(stats.toString().split("\\|"));
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
}
