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

public class ExportSalesReportWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ExportSalesReportWindow(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Export Sales Report");
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
				fromDateChooser = new JDateChooser();
				getContentPane().add(fromDateChooser);
				fromDateChooser.setFont(new java.awt.Font("Segoe UI",0,14));
				fromDateChooser.setBounds(76, 52, 173, 40);
			}
			
			{
				toDateChooser = new JDateChooser();
				toDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				toDateChooser.setBounds(298, 52, 173, 40);
				getContentPane().add(toDateChooser);
			}
			
			JLabel lblFrom = new JLabel("From:");
			lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblFrom.setBounds(26, 64, 55, 19);
			getContentPane().add(lblFrom);
			
			JLabel lblNewLabel_1 = new JLabel("To:");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblNewLabel_1.setBounds(264, 64, 39, 19);
			getContentPane().add(lblNewLabel_1);
			
			JButton btnNewButton = new JButton("Export");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportSalesReportButtonActionPerformed();
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
			btnNewButton.setBounds(504, 52, 191, 40);
			getContentPane().add(btnNewButton);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void exportSalesReportButtonActionPerformed() {
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
			writeSalesReportCsvFile(csvWriter);
			
			String fromDate = new SimpleDateFormat("yyyy-MM-dd").format(fromDateChooser.getDate());
			String toDate = new SimpleDateFormat("yyyy-MM-dd").format(toDateChooser.getDate());
			
			String saleReportQuery = "SELECT fineart_auction_calendar.faac_auction_ID, "
					+ "fineart_auction_calendar.faac_auction_sale_code, fineart_auction_calendar.faac_auction_start_date, "
					+ "fineart_auction_calendar.faac_auction_end_date, fineart_auction_calendar.faac_auction_title, "
					+ "core_auction_houses.cah_auction_house_name, core_auction_houses.cah_auction_house_location, "
					+ "(select count(*) from fineart_lots where fineart_lots.fal_auction_ID = "
					+ "fineart_auction_calendar.faac_auction_ID) as lot_count, "
					+ "SUM( CASE WHEN fineart_artworks.faa_artist_ID = '1' THEN 1 ELSE 0 END ) AS missing_artist, "
					+ "fineart_auction_calendar.faac_auction_published, "
					+ "fineart_auction_calendar.faac_auction_image, "
					+ "fineart_auction_calendar.faac_auction_source FROM fineart_lots INNER JOIN fineart_artworks "
					+ "ON fineart_lots.fal_artwork_ID = fineart_artworks.faa_artwork_ID RIGHT JOIN fineart_auction_calendar "
					+ "ON fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID INNER JOIN core_auction_houses "
					+ "ON fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID "
					+ "WHERE fineart_auction_calendar.faac_auction_start_date BETWEEN CAST ( '" + fromDate + "' AS DATE ) "
							+ "AND CAST ( '" + toDate + "' AS DATE ) GROUP BY fineart_auction_calendar.faac_auction_ID";
			
			jdbcTemplate.query(saleReportQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					try {
						while (resultSet.next()) {
							SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String outputDateFormat = "dd-MMM-yy";
							
							String startDateDb = resultSet.getString("faac_auction_start_date");
							String endDateDb = resultSet.getString("faac_auction_end_date");
							
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
							
							String auctionDetails = resultSet.getString("faac_auction_ID") + "|" + resultSet.getString("faac_auction_sale_code") 
							+ "|" + auctionStartDate + "|" + auctionEndDate + "|" + resultSet.getString("faac_auction_title") 
									+ "|" + resultSet.getString("cah_auction_house_name") + "|" 
									+ resultSet.getString("cah_auction_house_location") + "|" 
									+ resultSet.getInt("lot_count") + "|" + resultSet.getInt("missing_artist") 
									+ "|" + resultSet.getString("faac_auction_published") + "|" + resultSet.getString("faac_auction_image") 
									+ "|" + "" + "|" + resultSet.getString("faac_auction_source") ;

							String row[] = auctionDetails.split("\\|");
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
	
	public static void writeSalesReportCsvFile(CSVWriter csvWriter) {
		try {
			StringBuilder stats = new StringBuilder();
			stats.append("fal_auction_ID|faac_auction_sale_code|faac_auction_start_date|faac_auction_end_date|faac_auction_title"
					+ "|cah_auction_house_name|cah_auction_house_location|faac_auction_lot_count|missing_artists|faac_auction_published|faac_auction_image"
					+ "|image_source|faac_auction_source");
			csvWriter.writeNext(stats.toString().split("\\|"));
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
}
