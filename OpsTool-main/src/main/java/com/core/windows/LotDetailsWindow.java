package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.fineart.FineartUtils;

public class LotDetailsWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private int lotId;
	private String currencyCode;
	private int auctionId;
	private JTextField txtLotID;
	private JTextField txtLotNumber;
	private JTextField txtSublotNumber;
	private JTextField txtEstimateMin;
	private JTextField txtEstimateMax;
	private JTextField txtSoldPrice;
	private JTextField txtArtistID;
	private JComboBox<String> comboArtworkYearIdentifier;
	private JTextField txtStartYear;
	private JTextField txtEndYear;
	private JComboBox<String> comboCategory;
	private JTextField txtHeight;
	private JTextField txtWidth;
	private JTextField txtDepth;
	private JTextField txtArtworkName;
	private JTextField txtArtistName;
	private JTextField txtUnit;
	private JPanel basePanel;
	private JLabel lblArtworkName;
	private JLabel lblLotId;
	private JLabel lblLotNumber;
	private JLabel lblSublotNumber;
	private JLabel lblEstimateMaximum;
	private JLabel lblEstimateMaximum_1;
	private JLabel lblSoldPrice;
	private JLabel lblArtistId;
	private JLabel lblArtworkYearIdentifier;
	private JLabel lblArtworkStartYear;
	private Component lblArtworkEndYear;
	private JLabel lblArtworkCategory;
	private JLabel lblArtworkHeight;
	private JLabel lblArtworkWidth;
	private JLabel lblArtworkDepth;
	private Component lblArtistName;
	private Component lblUnit;
	private JLabel lblMaterial;
	private JTextArea materialTextArea;
	private JScrollPane scrollPane;
	private Component lblMarkings;
	private JScrollPane scrollPane_1;
	private JTextArea markingTextArea;
	private Component lblEdition;
	private JScrollPane scrollPane_2;
	private JTextArea editionTextArea;
	private JLabel lblDescriptionl;
	private JScrollPane scrollPane_3;
	private JTextArea descriptionTextArea;
	private JLabel lblSizeNotes;
	private JScrollPane scrollPane_4;
	private JTextArea literatureTextArea;
	private JLabel lblCondition;
	private JScrollPane scrollPane_5;
	private JLabel lblSizeNotes_1;
	private JTextArea conditionTextArea;
	private JScrollPane scrollPane_6;
	private JTextArea sizeNotesTextArea;
	private JLabel lblProvenance;
	private JScrollPane scrollPane_7;
	private JTextArea provenanceTextArea;
	private JLabel lblExhibited;
	private JScrollPane scrollPane_8;
	private JTextArea exhibitedTextArea;
	private JLabel lblOriginalUrl;
	private JTextField txtUrl;
	private JPanel saveCancelPanel;
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel navigationPanel;
	private JButton btnPrevious;
	private JButton btnNext;
	private JLabel lblImages;
	private JTextField txtImageCount;
	private String artistsIndexPath;
	protected java.util.Date auctionStartDate;
	protected int artworkId;
	
	public LotDetailsWindow(int lotId, int auctionId, String username, String artistIndexPath, JdbcTemplate jdbcTemplate, ViewSaleWindow viewSaleWindow) {
		this.lotId = lotId;
		this.auctionId = auctionId;
		this.username = username;
		this.artistsIndexPath = artistIndexPath;
		this.jdbcTemplate = jdbcTemplate;
		initGUI();
		loadLotDetails(lotId);
	}

	private void loadLotDetails(int localLotId) {
		String lotDetailsQuery = "SELECT  fineart_lots.fal_lot_ID, fineart_lots.fal_auction_ID, fineart_lots.fal_lot_no, "
				+ "fineart_lots.fal_sub_lot_no, fineart_lots.fal_lot_low_estimate, "
				+ "fineart_lots.fal_lot_high_estimate,  fineart_lots.fal_lot_sale_price, fineart_artists.fa_artist_ID, fineart_artists.fa_artist_name, "
				+ "fineart_artists.fa_artist_aka,  fineart_artists.fa_artist_nationality, fineart_artists.fa_artist_birth_year,  "
				+ "fineart_artists.fa_artist_death_year, fineart_artworks.faa_artwork_title, fineart_artworks.faa_artwork_start_year_identifier,  "
				+ "fineart_artworks.faa_artwork_material, fineart_artworks.faa_artwork_category, fineart_artworks.faa_artwork_edition, fineart_lots.fal_lot_condition, "
				+ "fineart_artworks.faa_artwork_exhibition, fineart_artworks.faa_artwork_literature, fineart_artworks.faa_artwork_height,  "
				+ "fineart_artworks.faa_artwork_width, fineart_artworks.faa_artwork_depth, fineart_artworks.faa_arwork_measurement_unit, fineart_lots.fal_lot_provenance, "
				+ "fineart_artworks.faa_artwork_size_details, fineart_artworks.faa_artwork_markings, fineart_artworks.faa_artwork_start_year, fineart_lots.fal_lot_source, "
				+ "fineart_artworks.faa_artwork_end_year, fineart_lots.fal_lot_image1, fineart_lots.fal_lot_image2, fineart_lots.fal_lot_image3,  "
				+ "fineart_lots.fal_lot_image4, fineart_lots.fal_lot_image5,  fineart_artworks.faa_artwork_description, fineart_artworks.faa_artwork_ID,  "
				+ "core_auction_houses.cah_auction_house_currency_code, ((fineart_lots.fal_lot_image1 != '' AND fineart_lots.fal_lot_image1 != 'na') + "
				+ "(fineart_lots.fal_lot_image2 !=  '' AND fineart_lots.fal_lot_image2 !=  'na') "
				+ "+ (fineart_lots.fal_lot_image3 != '' AND fineart_lots.fal_lot_image3 != 'na') + (fineart_lots.fal_lot_image4 != '' AND fineart_lots.fal_lot_image4 != 'na') + "
				+ "(fineart_lots.fal_lot_image5 !=  '' AND fineart_lots.fal_lot_image5 !=  'na')) "
				+ "AS image_count, fineart_auction_calendar.faac_auction_start_date FROM fineart_artworks INNER JOIN  fineart_lots ON fineart_artworks.faa_artwork_ID = "
				+ "fineart_lots.fal_artwork_ID INNER JOIN fineart_artists ON fineart_artworks.faa_artist_ID = "
				+ "fineart_artists.fa_artist_ID INNER JOIN fineart_auction_calendar ON fineart_lots.fal_auction_ID = "
				+ "fineart_auction_calendar.faac_auction_ID INNER JOIN core_auction_houses ON fineart_auction_calendar.faac_auction_house_ID = "
				+ "core_auction_houses.cah_auction_house_ID WHERE fineart_lots.fal_lot_ID = '" + localLotId + "' and fineart_lots.fal_auction_ID = '" + auctionId + "'";
		
		jdbcTemplate.query(lotDetailsQuery, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					lotId = localLotId;
					txtArtworkName.setText(resultSet.getString("faa_artwork_title"));
					txtLotID.setText(String.valueOf(resultSet.getInt("fal_lot_ID")));
					txtLotNumber.setText(String.valueOf(resultSet.getInt("fal_lot_no")));
					txtSublotNumber.setText(String.valueOf(resultSet.getInt("fal_sub_lot_no")));
					txtEstimateMin.setText(String.valueOf(resultSet.getInt("fal_lot_low_estimate")));
					txtEstimateMax.setText(String.valueOf(resultSet.getInt("fal_lot_high_estimate")));
					txtSoldPrice.setText(String.valueOf(resultSet.getInt("fal_lot_sale_price")));
					txtArtistID.setText(String.valueOf(resultSet.getInt("fa_artist_ID")));
					txtArtistName.setText(resultSet.getString("fa_artist_name") + " " + ((resultSet.getString("fa_artist_aka")).equals("na") ? "" : "(" 
					+ resultSet.getString("fa_artist_aka") + ")") + "-" + resultSet.getString("fa_artist_nationality") + "-" + resultSet.getString("fa_artist_birth_year") 
					+ "-" + resultSet.getString("fa_artist_death_year"));
					materialTextArea.setText(resultSet.getString("faa_artwork_material"));
					markingTextArea.setText(resultSet.getString("faa_artwork_markings"));
					editionTextArea.setText(resultSet.getString("faa_artwork_edition"));
					conditionTextArea.setText(resultSet.getString("fal_lot_condition"));
					descriptionTextArea.setText(resultSet.getString("faa_artwork_description"));
					literatureTextArea.setText(resultSet.getString("faa_artwork_literature"));
					sizeNotesTextArea.setText(resultSet.getString("faa_artwork_size_details"));
					provenanceTextArea.setText(resultSet.getString("fal_lot_provenance"));
					exhibitedTextArea.setText(resultSet.getString("faa_artwork_exhibition"));
					txtImageCount.setText(String.valueOf(resultSet.getInt("image_count")));
					txtUrl.setText(resultSet.getString("fal_lot_source"));
					comboCategory.setSelectedItem(resultSet.getString("faa_artwork_category"));
					comboArtworkYearIdentifier.setSelectedItem(resultSet.getString("faa_artwork_start_year_identifier"));
					txtStartYear.setText(String.valueOf(resultSet.getInt("faa_artwork_start_year")));
					txtEndYear.setText(String.valueOf(resultSet.getInt("faa_artwork_end_year")));
					txtHeight.setText(String.valueOf(resultSet.getInt("faa_artwork_height")));
					txtWidth.setText(String.valueOf(resultSet.getInt("faa_artwork_width")));
					txtDepth.setText(String.valueOf(resultSet.getInt("faa_artwork_depth")));
					txtUnit.setText(resultSet.getString("faa_arwork_measurement_unit"));
					currencyCode = resultSet.getString("cah_auction_house_currency_code");
					auctionStartDate = new Date(resultSet.getDate("faac_auction_start_date").getTime());
					artworkId = resultSet.getInt("faa_artwork_ID");
					
					txtArtworkName.setCaretPosition(0);
					txtArtistName.setCaretPosition(0);
					materialTextArea.setCaretPosition(0);
					markingTextArea.setCaretPosition(0);
					editionTextArea.setCaretPosition(0);
					conditionTextArea.setCaretPosition(0);
					descriptionTextArea.setCaretPosition(0);
					literatureTextArea.setCaretPosition(0);
					sizeNotesTextArea.setCaretPosition(0);
					provenanceTextArea.setCaretPosition(0);
					exhibitedTextArea.setCaretPosition(0);
					txtUrl.setCaretPosition(0);
				}
				return true;
			}
		});
	}
	
	protected void saveLotDetails() {
		try {
			String artworkName = FineartUtils.getSqlSafeString(txtArtworkName.getText().trim());
			String lotNumber = FineartUtils.getSqlSafeString(txtLotNumber.getText().trim());
			String sublotNumber = FineartUtils.getSqlSafeString(txtSublotNumber.getText().trim());
			String estimateMin = StringUtils.isEmpty(txtEstimateMin.getText().trim()) ? "0.00" : txtEstimateMin.getText().trim();
			String estimateMax = StringUtils.isEmpty(txtEstimateMax.getText().trim()) ? "0.00" : txtEstimateMax.getText().trim();
			String soldPrice = StringUtils.isEmpty(txtSoldPrice.getText().trim()) ? "0.00" : txtSoldPrice.getText().trim();
			String artistId = txtArtistID.getText().trim();
			String material = FineartUtils.getSqlSafeString(materialTextArea.getText().trim());
			String marking = FineartUtils.getSqlSafeString(markingTextArea.getText().trim());
			String edition = FineartUtils.getSqlSafeString(editionTextArea.getText().trim());
			String condition = FineartUtils.getSqlSafeString(conditionTextArea.getText().trim());
			String description = FineartUtils.getSqlSafeString(descriptionTextArea.getText().trim());
			String literature = FineartUtils.getSqlSafeString(literatureTextArea.getText().trim());
			String sizeNotes = FineartUtils.getSqlSafeString(sizeNotesTextArea.getText().trim());
			String provenance = FineartUtils.getSqlSafeString(provenanceTextArea.getText().trim());
			String exhibited = FineartUtils.getSqlSafeString(exhibitedTextArea.getText().trim());
			String category = comboCategory.getSelectedItem().toString();
			String artworkYearIdentifier = comboArtworkYearIdentifier.getSelectedItem().toString();
			String startYear = StringUtils.isEmpty(txtStartYear.getText().trim()) ? "0" : txtStartYear.getText().trim();
			String endYear = StringUtils.isEmpty(txtEndYear.getText().trim()) ? "0" : txtEndYear.getText().trim();
			String height = StringUtils.isEmpty(txtHeight.getText().trim()) ? "0.00" : txtHeight.getText().trim();
			String width = StringUtils.isEmpty(txtWidth.getText().trim()) ? "0.00" : txtWidth.getText().trim();
			String depth = StringUtils.isEmpty(txtDepth.getText().trim()) ? "0.00" : txtDepth.getText().trim();
			String unit = txtUnit.getText().trim();
			
			String auctionNameQuery = "SELECT  fineart_lots.fal_lot_image1, fineart_lots.fal_lot_image2, fineart_lots.fal_lot_image3, "
					+ "fineart_lots.fal_lot_image4, fineart_lots.fal_lot_image5 FROM  fineart_lots WHERE fineart_lots.fal_lot_ID = '" + lotId + "'";
			
			String artworkImageName = jdbcTemplate.query(auctionNameQuery, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						if(StringUtils.isNotEmpty(resultSet.getString("fal_lot_image1"))) {
							return resultSet.getString("fal_lot_image1");
						} else if(StringUtils.isNotEmpty(resultSet.getString("fal_lot_image2"))) {
							return resultSet.getString("fal_lot_image2");
						} else if(StringUtils.isNotEmpty(resultSet.getString("fal_lot_image3"))) {
							return resultSet.getString("fal_lot_image3");
						} else if(StringUtils.isNotEmpty(resultSet.getString("fal_lot_image4"))) {
							return resultSet.getString("fal_lot_image4");
						} else if(StringUtils.isNotEmpty(resultSet.getString("fal_lot_image5"))) {
							return resultSet.getString("fal_lot_image5");
						}
					}
					return "";
				}
			});
			
			//Get currency exchange rate
			
			float rateStr = 1.0f;
			
			if(!currencyCode.equals("USD")) {
				
				Date d1 = auctionStartDate;
				
			    Calendar cal = new GregorianCalendar(); 
			    cal.setTime(d1);
			    cal.add(Calendar.MONTH, 1);
			    
			    long millisecondsTo = cal.getTimeInMillis();
			    
			    Date d2 = new Date(millisecondsTo);
			    
			    String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(d1);
			    String dateTo = new SimpleDateFormat("yyyy-MM-dd").format(d2);
				
				String currencyRateSql = "select * from currency_exchange_rate WHERE cexr_date BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND cexr_base_currency = '" 
				+ currencyCode + "' LIMIT 1";
				
				rateStr  = jdbcTemplate.query(currencyRateSql, new ResultSetExtractor<Float>() {

					public Float extractData(ResultSet resultSet) throws SQLException, DataAccessException {
						if (resultSet.next()) {
							return resultSet.getFloat("cexr_usd_rate");
						} else {
							return 0.0f;
						}
					}
				});
			}
			
			float estimateMaxUSD = Float.parseFloat(StringUtils.isEmpty(estimateMax) ? "0.0" : estimateMax) * rateStr;
			float estimateMinUSD = Float.parseFloat(StringUtils.isEmpty(estimateMin) ? "0.0" : estimateMin) * rateStr;
			float soldPriceUSD = Float.parseFloat(StringUtils.isEmpty(soldPrice) ? "0.0" : soldPrice) * rateStr;
			
			String priceKind = "bought-in";
			
			if(StringUtils.isNotEmpty(soldPrice)) {
				priceKind = "sold";
			} else if( (StringUtils.isNotEmpty(estimateMax) || StringUtils.isNotEmpty(estimateMin)) && StringUtils.isEmpty(soldPrice) ) {
				priceKind = "bought-in";
			} else if(StringUtils.isEmpty(estimateMax) && StringUtils.isEmpty(estimateMin) && StringUtils.isEmpty(soldPrice) ) {
				priceKind = "yet to be sold";
			}
			
			String artworkUpdateQuery = "UPDATE fineart_artworks SET faa_artwork_title = '" + artworkName + "', faa_artwork_description = '" + description + "', "
					+ "faa_artist_ID = '" + artistId + "', faa_artwork_category = '" + category + "', faa_artwork_material = '" + material + "', "
					+ "faa_artwork_edition = '" + edition + "', faa_artwork_exhibition = '" + exhibited + "', faa_artwork_literature = '" + literature + "', "
					+ "faa_artwork_height = '" + height + "', faa_artwork_width = '" + width + "', faa_artwork_depth = '" + depth + "', "
					+ "faa_arwork_measurement_unit = '" + unit + "', faa_artwork_size_details = '" + sizeNotes + "', faa_artwork_markings = '" + marking + "', "
					+ "faa_artwork_start_year = '" + startYear + "', faa_artwork_end_year = '" + endYear + "', faa_artwork_start_year_identifier = '" + artworkYearIdentifier 
					+ "', faa_artwork_image1 = '" + artworkImageName + "', faa_artwork_record_updated = CURRENT_TIMESTAMP, "
					+ "faa_artwork_record_updatedby = '" + username + "' where fineart_artworks.faa_artwork_ID = '" + artworkId + "'";
			
			jdbcTemplate.update(artworkUpdateQuery);
			
			String lotUpdateQuery = "UPDATE fineart_lots SET fal_lot_no = '" + lotNumber + "', fal_sub_lot_no = '" + sublotNumber + "', "
					+ "fal_lot_category = '" + category + "', fal_lot_sale_date = '" + new java.sql.Date(auctionStartDate.getTime()) 
					+ "', fal_lot_high_estimate = '" + estimateMax + "', "
					+ "fal_lot_low_estimate = '" + estimateMin + "', fal_lot_high_estimate_USD = '" + estimateMaxUSD + "', fal_lot_low_estimate_USD = '" 
					+ estimateMinUSD + "', fal_lot_sale_price = '" + soldPrice + "', fal_lot_sale_price_USD = '" + soldPriceUSD + "', fal_lot_status = '" + priceKind + "', "
					+ "fal_lot_condition = '" + condition + "', fal_lot_height = '" + height + "', fal_lot_width = '" + width + "', fal_lot_depth = '" + depth + "', "
					+ "fal_lot_measurement_unit = '" + unit + "', fal_lot_size_details = '" + sizeNotes + "', fal_lot_material = '" + material + "', "
					+ "fal_lot_provenance = '" + provenance + "', fal_lot_record_updated = CURRENT_TIMESTAMP,  "
					+ "fal_lot_record_updatedby = '" + username + "' WHERE fineart_lots.fal_lot_ID = '" + lotId + "'";
			
			jdbcTemplate.update(lotUpdateQuery);
			
			JOptionPane.showMessageDialog(this, "Saved");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1350, 675));
		this.setSize(new Dimension(1350, 675));
		this.setTitle("Sale Details");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, 5);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),	"Cancel"); //$NON-NLS-1$
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		basePanel = new JPanel();
		basePanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		getContentPane().add(basePanel);
		basePanel.setLayout(null);
		
		lblArtworkName = new JLabel("Artwork Name:");
		lblArtworkName.setBounds(10, 13, 102, 14);
		basePanel.add(lblArtworkName);
		
		txtArtworkName = new JTextField();
		txtArtworkName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtArtworkName.setColumns(10);
		txtArtworkName.setBounds(154, 10, 533, 20);
		basePanel.add(txtArtworkName);
		
		lblLotId = new JLabel("Lot ID:");
		lblLotId.setBounds(10, 41, 134, 14);
		basePanel.add(lblLotId);
		
		txtLotID = new JTextField();
		txtLotID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtLotID.setDisabledTextColor(Color.BLACK);
		txtLotID.setEnabled(false);
		txtLotID.setBounds(154, 41, 201, 20);
		basePanel.add(txtLotID);
		txtLotID.setColumns(10);
		
		lblLotNumber = new JLabel("Lot Number:");
		lblLotNumber.setBounds(10, 69, 134, 14);
		basePanel.add(lblLotNumber);
		
		txtLotNumber = new JTextField();
		txtLotNumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtLotNumber.setColumns(10);
		txtLotNumber.setBounds(154, 69, 201, 20);
		basePanel.add(txtLotNumber);
		
		lblSublotNumber = new JLabel("Sublot Number:");
		lblSublotNumber.setBounds(10, 97, 134, 14);
		basePanel.add(lblSublotNumber);
		
		txtSublotNumber = new JTextField();
		txtSublotNumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSublotNumber.setColumns(10);
		txtSublotNumber.setBounds(154, 97, 201, 20);
		basePanel.add(txtSublotNumber);
		
		lblEstimateMaximum = new JLabel("Estimate Minimum:");
		lblEstimateMaximum.setBounds(10, 125, 134, 14);
		basePanel.add(lblEstimateMaximum);
		
		txtEstimateMin = new JTextField();
		txtEstimateMin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtEstimateMin.setColumns(10);
		txtEstimateMin.setBounds(154, 125, 201, 20);
		basePanel.add(txtEstimateMin);
		
		lblEstimateMaximum_1 = new JLabel("Estimate Maximum:");
		lblEstimateMaximum_1.setBounds(10, 156, 134, 14);
		basePanel.add(lblEstimateMaximum_1);
		
		txtEstimateMax = new JTextField();
		txtEstimateMax.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtEstimateMax.setColumns(10);
		txtEstimateMax.setBounds(154, 156, 201, 20);
		basePanel.add(txtEstimateMax);
		
		lblSoldPrice = new JLabel("Sold Price:");
		lblSoldPrice.setBounds(10, 184, 134, 14);
		basePanel.add(lblSoldPrice);
		
		txtSoldPrice = new JTextField();
		txtSoldPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSoldPrice.setColumns(10);
		txtSoldPrice.setBounds(154, 184, 201, 20);
		basePanel.add(txtSoldPrice);
		
		lblArtistId = new JLabel("Artist ID:");
		lblArtistId.setBounds(10, 212, 134, 14);
		basePanel.add(lblArtistId);
		
		txtArtistID = new JTextField();
		txtArtistID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtArtistID.setDisabledTextColor(Color.BLACK);
		txtArtistID.setEnabled(false);
		txtArtistID.setColumns(10);
		txtArtistID.setBounds(154, 212, 201, 20);
		basePanel.add(txtArtistID);
		
		lblArtworkYearIdentifier = new JLabel("Artwork Year Identifier:");
		lblArtworkYearIdentifier.setBounds(365, 41, 125, 14);
		basePanel.add(lblArtworkYearIdentifier);
		
		comboArtworkYearIdentifier = new JComboBox<String>();
		comboArtworkYearIdentifier.setModel(new DefaultComboBoxModel<String>(new String[] {"exact", "after", "before", "circa"}));
		comboArtworkYearIdentifier.setBounds(486, 41, 201, 20);
		basePanel.add(comboArtworkYearIdentifier);
		
		lblArtworkStartYear = new JLabel("Artwork Start Year:");
		lblArtworkStartYear.setBounds(365, 69, 125, 14);
		basePanel.add(lblArtworkStartYear);
		
		txtStartYear = new JTextField();
		txtStartYear.setColumns(10);
		txtStartYear.setBounds(486, 69, 201, 20);
		basePanel.add(txtStartYear);
		
		lblArtworkEndYear = new JLabel("Artwork End Year:");
		lblArtworkEndYear.setBounds(365, 97, 125, 14);
		basePanel.add(lblArtworkEndYear);
		
		txtEndYear = new JTextField();
		txtEndYear.setColumns(10);
		txtEndYear.setBounds(486, 97, 201, 20);
		basePanel.add(txtEndYear);
		
		lblArtworkCategory = new JLabel("Artwork Category:");
		lblArtworkCategory.setBounds(365, 128, 125, 14);
		basePanel.add(lblArtworkCategory);
		
		comboCategory = new JComboBox<String>();
		comboCategory.setModel(new DefaultComboBoxModel<String>(new String[] {"paintings", "sculptures", "prints", "photographs", "works on paper", "miniatures"}));
		comboCategory.setBounds(486, 128, 201, 20);
		basePanel.add(comboCategory);
		
		lblArtworkHeight = new JLabel("Artwork Height:");
		lblArtworkHeight.setBounds(365, 156, 125, 14);
		basePanel.add(lblArtworkHeight);
		
		txtHeight = new JTextField();
		txtHeight.setColumns(10);
		txtHeight.setBounds(486, 156, 201, 20);
		basePanel.add(txtHeight);
		
		lblArtworkWidth = new JLabel("Artwork Width:");
		lblArtworkWidth.setBounds(365, 184, 125, 14);
		basePanel.add(lblArtworkWidth);
		
		txtWidth = new JTextField();
		txtWidth.setColumns(10);
		txtWidth.setBounds(486, 184, 201, 20);
		basePanel.add(txtWidth);
		
		lblArtworkDepth = new JLabel("Artwork Depth:");
		lblArtworkDepth.setBounds(365, 212, 125, 14);
		basePanel.add(lblArtworkDepth);
		
		txtDepth = new JTextField();
		txtDepth.setColumns(10);
		txtDepth.setBounds(486, 212, 201, 20);
		basePanel.add(txtDepth);
		
		lblArtistName = new JLabel("Artist Name:");
		lblArtistName.setBounds(10, 240, 134, 14);
		basePanel.add(lblArtistName);
		
		txtArtistName = new JTextField();
		txtArtistName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtArtistName.setDisabledTextColor(Color.BLACK);
		txtArtistName.setEnabled(false);
		txtArtistName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openArtistMasterWindow();
			}
		});
		txtArtistName.setColumns(10);
		txtArtistName.setBounds(154, 240, 201, 20);
		basePanel.add(txtArtistName);
		
		lblUnit = new JLabel("Measurement Unit:");
		lblUnit.setBounds(365, 240, 125, 14);
		basePanel.add(lblUnit);
		
		txtUnit = new JTextField();
		txtUnit.setColumns(10);
		txtUnit.setBounds(486, 240, 201, 20);
		basePanel.add(txtUnit);
		
		lblMaterial = new JLabel("Material:");
		lblMaterial.setBounds(10, 271, 102, 14);
		basePanel.add(lblMaterial);
		
		materialTextArea = new JTextArea();
		materialTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		materialTextArea.setWrapStyleWord(true);
		materialTextArea.setLineWrap(true);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(69, 265, 618, 62);
		scrollPane.add(materialTextArea);
		scrollPane.setViewportView(materialTextArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		basePanel.add(scrollPane);
		
		lblMarkings = new JLabel("Markings:");
		lblMarkings.setBounds(10, 344, 102, 14);
		basePanel.add(lblMarkings);
		materialTextArea.setBounds(111, 405, 525, 126);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(69, 338, 618, 62);
		basePanel.add(scrollPane_1);
		
		markingTextArea = new JTextArea();
		markingTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		markingTextArea.setWrapStyleWord(true);
		markingTextArea.setLineWrap(true);
		scrollPane_1.add(markingTextArea);
		scrollPane_1.setViewportView(markingTextArea);
		
		lblEdition = new JLabel("Edition:");
		lblEdition.setBounds(10, 417, 102, 14);
		basePanel.add(lblEdition);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setBounds(69, 411, 618, 62);
		basePanel.add(scrollPane_2);
		
		editionTextArea = new JTextArea();
		editionTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		editionTextArea.setBounds(0, 0, 4, 22);
		editionTextArea.setWrapStyleWord(true);
		editionTextArea.setLineWrap(true);
		scrollPane_2.add(editionTextArea);
		scrollPane_2.setViewportView(editionTextArea);
		
		lblDescriptionl = new JLabel("Description:");
		lblDescriptionl.setBounds(697, 13, 102, 14);
		basePanel.add(lblDescriptionl);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_3.setBounds(778, 7, 525, 157);
		basePanel.add(scrollPane_3);
		
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		descriptionTextArea.setBounds(0, 0, 4, 22);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setLineWrap(true);
		scrollPane_3.add(descriptionTextArea);
		scrollPane_3.setViewportView(descriptionTextArea);
		
		lblSizeNotes = new JLabel("Literature:");
		lblSizeNotes.setBounds(697, 181, 102, 14);
		basePanel.add(lblSizeNotes);
		
		scrollPane_4 = new JScrollPane();
		scrollPane_4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_4.setBounds(778, 178, 525, 76);
		basePanel.add(scrollPane_4);
		
		literatureTextArea = new JTextArea();
		literatureTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		literatureTextArea.setBounds(0, 0, 4, 22);
		literatureTextArea.setWrapStyleWord(true);
		literatureTextArea.setLineWrap(true);
		scrollPane_4.add(literatureTextArea);
		scrollPane_4.setViewportView(literatureTextArea);
		
		lblCondition = new JLabel("Condition:");
		lblCondition.setBounds(10, 490, 102, 14);
		basePanel.add(lblCondition);
		
		scrollPane_5 = new JScrollPane();
		scrollPane_5.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_5.setBounds(69, 484, 618, 62);
		basePanel.add(scrollPane_5);
		
		conditionTextArea = new JTextArea();
		conditionTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		conditionTextArea.setBounds(0, 0, 4, 22);
		conditionTextArea.setWrapStyleWord(true);
		conditionTextArea.setLineWrap(true);
		scrollPane_5.add(conditionTextArea);
		scrollPane_5.setViewportView(conditionTextArea);
		
		lblSizeNotes_1 = new JLabel("Size Notes:");
		lblSizeNotes_1.setBounds(697, 268, 102, 14);
		basePanel.add(lblSizeNotes_1);
		
		scrollPane_6 = new JScrollPane();
		scrollPane_6.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_6.setBounds(778, 265, 525, 62);
		basePanel.add(scrollPane_6);
		
		sizeNotesTextArea = new JTextArea();
		sizeNotesTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		sizeNotesTextArea.setBounds(0, 0, 4, 22);
		sizeNotesTextArea.setWrapStyleWord(true);
		sizeNotesTextArea.setLineWrap(true);
		scrollPane_6.add(sizeNotesTextArea);
		scrollPane_6.setViewportView(sizeNotesTextArea);
		
		lblProvenance = new JLabel("Provenance:");
		lblProvenance.setBounds(697, 341, 102, 14);
		basePanel.add(lblProvenance);
		
		scrollPane_7 = new JScrollPane();
		scrollPane_7.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_7.setBounds(778, 338, 525, 62);
		basePanel.add(scrollPane_7);
		
		provenanceTextArea = new JTextArea();
		provenanceTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		provenanceTextArea.setBounds(0, 0, 4, 22);
		provenanceTextArea.setWrapStyleWord(true);
		provenanceTextArea.setLineWrap(true);
		scrollPane_7.add(provenanceTextArea);
		scrollPane_7.setViewportView(provenanceTextArea);
		
		lblExhibited = new JLabel("Exhibited:");
		lblExhibited.setBounds(697, 414, 102, 14);
		basePanel.add(lblExhibited);
		
		scrollPane_8 = new JScrollPane();
		scrollPane_8.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_8.setBounds(778, 411, 525, 62);
		basePanel.add(scrollPane_8);
		
		exhibitedTextArea = new JTextArea();
		exhibitedTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		exhibitedTextArea.setBounds(0, 0, 4, 22);
		exhibitedTextArea.setWrapStyleWord(true);
		exhibitedTextArea.setLineWrap(true);
		scrollPane_8.add(exhibitedTextArea);
		scrollPane_8.setViewportView(exhibitedTextArea);
		
		lblOriginalUrl = new JLabel("Original URL:");
		lblOriginalUrl.setBounds(697, 528, 102, 14);
		basePanel.add(lblOriginalUrl);
		
		txtUrl = new JTextField();
		txtUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openLotSource();
			}
		});
		txtUrl.setDisabledTextColor(Color.BLACK);
		txtUrl.setEnabled(false);
		txtUrl.setBounds(778, 522, 525, 24);
		basePanel.add(txtUrl);
		
		saveCancelPanel = new JPanel();
		saveCancelPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		saveCancelPanel.setBounds(365, 557, 373, 68);
		basePanel.add(saveCancelPanel);
		saveCancelPanel.setLayout(null);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveLotDetails();
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSave.setBounds(10, 11, 169, 46);
		btnSave.setMnemonic('S');
		saveCancelPanel.add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCancel.setBounds(194, 11, 169, 46);
		btnCancel.setMnemonic('C');
		saveCancelPanel.add(btnCancel);
		
		navigationPanel = new JPanel();
		navigationPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		navigationPanel.setBounds(753, 557, 373, 68);
		basePanel.add(navigationPanel);
		navigationPanel.setLayout(null);
		
		btnPrevious = new JButton("<-- Previous Lot");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadPreviousLot();
			}
		});
		btnPrevious.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPrevious.setBounds(10, 11, 169, 46);
		btnPrevious.setMnemonic(KeyEvent.VK_LEFT);
		navigationPanel.add(btnPrevious);
		
		btnNext = new JButton("Next Lot -->");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadNextLot();
			}
		});
		btnNext.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNext.setBounds(194, 11, 169, 46);
		btnNext.setMnemonic(KeyEvent.VK_RIGHT);
		navigationPanel.add(btnNext);
		
		lblImages = new JLabel("Image Count:");
		lblImages.setBounds(697, 490, 102, 14);
		basePanel.add(lblImages);
		
		txtImageCount = new JTextField();
		txtImageCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtImageCount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				viewAndUploadImages();
			}
		});
		txtImageCount.setDisabledTextColor(Color.BLACK);
		txtImageCount.setEnabled(false);
		txtImageCount.setBounds(778, 487, 525, 24);
		basePanel.add(txtImageCount);
		txtImageCount.setColumns(10);
	}

	protected void viewAndUploadImages() {
		LotDetailsWindow lotDetailsWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewAndUploadImageWindow frame = new ViewAndUploadImageWindow(username, lotId, jdbcTemplate, lotDetailsWindow);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void openLotSource() {
		try {
			URI uri = new URI(txtUrl.getText().trim());
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(uri);
				} catch (IOException e) {
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void loadNextLot() {
		String nextQuery = "select min(fineart_lots.fal_lot_ID) as lot_ID from fineart_lots where fineart_lots.fal_lot_ID > '" + lotId + "' and fineart_lots.fal_auction_ID = '" + auctionId + "'";
		int localLotId = jdbcTemplate.query(nextQuery, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getInt("lot_ID");
				} else {
					return 0;
				}
			}
		});
		loadLotDetails(localLotId);
	}

	protected void loadPreviousLot() {
		String previousQuery = "select max(fineart_lots.fal_lot_ID) as lot_ID from fineart_lots where fineart_lots.fal_lot_ID < '" + lotId + "' and fineart_lots.fal_auction_ID = '" 
				+ auctionId + "'";
		int localLotId = jdbcTemplate.query(previousQuery, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getInt("lot_ID");
				} else {
					return 0;
				}
			}
		});
		loadLotDetails(localLotId);
	}

	protected void openArtistMasterWindow() {
		LotDetailsWindow lotDeatilsWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LinkArtistWindow frame = new LinkArtistWindow(lotDeatilsWindow, jdbcTemplate, username, artistsIndexPath);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String getDescriptionText() {
		return descriptionTextArea.getText().trim();
	}

	public void setDescriptionText(String descriptionTextArea) {
		this.descriptionTextArea.setText(descriptionTextArea);
	}

	public String getArtistID() {
		return txtArtistID.getText().trim();
	}

	public void setArtistID(String txtArtistID) {
		this.txtArtistID.setText(txtArtistID);
	}

	public String getArtistName() {
		return txtArtistName.getText().trim();
	}

	public void setArtistName(String txtArtistName) {
		this.txtArtistName.setText(txtArtistName);
	}

	public int getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	public String getImageCount() {
		return txtImageCount.getText();
	}

	public void setImageCount(String txtImageCount) {
		this.txtImageCount.setText(txtImageCount);
	}
	
}
