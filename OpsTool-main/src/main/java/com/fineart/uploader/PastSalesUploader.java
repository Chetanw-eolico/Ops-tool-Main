package com.fineart.uploader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.core.fineart.FineartUtils;
import com.core.pojo.FineartArtistsData;
import com.core.pojo.FineartArtistsDataMapper;
import com.core.windows.BaseWindow;
import com.fineart.scraper.CsvBean;
import com.jauntium.Browser;
import com.jauntium.Element;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;



public class PastSalesUploader extends BaseWindow implements UploaderInterface {

	private static final long serialVersionUID = 1L;
	String csvPath;
	Browser browser;
	String username;
	JdbcTemplate jdbcTemplate;
	JButton btnAddNewArtist;
	JButton btnUpdateArtist;
	JPanel mainPanel;
	JLabel lblExactArtistMatch;
	JScrollPane tableScrollPane;
	DefaultTableModel tableModel;
	JTable table;
	ListSelectionModel cellSelectionModel;
	JLabel lblIncsv;
	JPanel csvPanel;
	JLabel lblArtistName;
	JTextField artistNameCsv;
	JTextField artistDobCsv;
	JLabel lblDateOfBirth;
	JTextField artistDodCsv;
	//private String artistsIndexPath;
	//private String saleLotsIndexPath;
	static String linkedArtistID = "0";
	private List<CsvBean> csvIssuesList;
	private boolean cleanUpload;
	private String faac_auction_ID = "0";
	private String webDriver;
	private String priceType;
	Map<String, String>saleCodeAuctionIdMap;
	Map<String, String>auctionHouseAndIdMap;
	Map<String, String>auctionHouseAndCurrencyMap;
	boolean showConsole;
	boolean uploadDataWithDownloadImages;
	boolean downloadImagesOnly;
	
	public PastSalesUploader(String username, boolean cleanUpload, JdbcTemplate jdbcTemplate, String webDriver, String priceType, boolean uploadDataWithDownloadImages, 
			boolean downloadImagesOnly, boolean showConsole) {
		super();
		this.username = username;
		this.cleanUpload = cleanUpload;
		this.jdbcTemplate = jdbcTemplate;
		this.webDriver = webDriver;
		this.priceType = priceType;
		this.showConsole = showConsole;
		this.uploadDataWithDownloadImages = uploadDataWithDownloadImages;
		this.downloadImagesOnly = downloadImagesOnly;
		//initializeArtistsIndexPath();
		//initializeSaleLotsIndexPath();
		loadAuctionHouseIdAndCurrency();
		loadUploadedData();
	}

	@Override
	public void initUploading(String csvPath, String directoryPath) {
		try {
			this.csvPath = csvPath;
			this.csvIssuesList = new ArrayList<>();
			//String filePath = "";
			//URL url;
			
			if(uploadDataWithDownloadImages || downloadImagesOnly) {
				//ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				if(webDriver.equals("Chrome")) {
					//url = classloader.getResource("chromedriver.exe");
					//filePath = url.toURI().getPath();
					//System.setProperty("webdriver.chrome.driver", "D:\\NewInit\\Work\\Tool\\OperationsTool\\chromedriver_win32\\chromedriver.exe");
					//TODO uncomment below line before packaging and comment above
					System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
				} else if(webDriver.equals("Firefox")) {
					//url = classloader.getResource("geckodriver.exe");
					//filePath = url.toURI().getPath();
					//System.setProperty("webdriver.gecko.driver", filePath);
					//TODO uncomment below line before packaging and comment above
					System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
				} else if(webDriver.equals("Edge")) {
					//url = classloader.getResource("MicrosoftWebDriver.exe");
					//filePath = url.toURI().getPath();
					//System.setProperty("webdriver.edge.driver", filePath);
					//TODO uncomment below line before packaging and comment above
					System.setProperty("webdriver.edge.driver", "MicrosoftWebDriver.exe");
				}
				
				if(webDriver.equals("Chrome")) {
					this.browser = new Browser(new ChromeDriver());
					//this.browser = new Browser(new FirefoxDriver());
				} else if(webDriver.equals("Firefox")) {
					this.browser = new Browser(new FirefoxDriver());
				} else if(webDriver.equals("Edge")) {
					this.browser = new Browser(new EdgeDriver());
				}
			}
			
			SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
	            public String doInBackground() {
	            	doUpload(csvPath, directoryPath);
	                return "";
	            }
	        };
	       // execute the background thread
	       worker.execute();
		} catch (Exception e) {
			e.printStackTrace();
			consoleArea.append("\n" + e.getMessage());
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}

	public void doUpload(String csvPath, String directoryPath) {
		try {
			System.out.println("inside doUpload...");
			//Reader reader = Files.newBufferedReader(Paths.get(csvPath));
			CsvToBean<CsvBean> csvToBean = new CsvToBean<>();
			ColumnPositionMappingStrategy<CsvBean> strategy = new ColumnPositionMappingStrategy<>();
			
			String[] csvFields = {"auction_house_name", "auction_location", "auction_num", "auction_start_date", "auction_end_date", "auction_name", "lot_num", "sublot_num", 
					"price_kind", "price_estimate_min", "price_estimate_max", "price_sold", "artist_name", "artist_birth", "artist_death", "artist_nationality", 
					"artwork_name", "artwork_year_identifier", "artwork_start_year", "artwork_end_year", "artwork_materials", "artwork_category", "artwork_markings", "artwork_edition"
					, "artwork_description", "artwork_measurements_height", "artwork_measurements_width", "artwork_measurements_depth", "artwork_size_notes", 
					"auction_measureunit", "artwork_condition_in", "artwork_provenance", "artwork_exhibited", "artwork_literature", "artwork_images1", 
					"artwork_images2", "artwork_images3", "artwork_images4", "artwork_images5", "image1_name", "image2_name", "image3_name", 
					"image4_name", "image5_name", "lot_origin_url"};
			strategy.setColumnMapping(csvFields);
			strategy.setType(CsvBean.class);
			
			//CSVReader csvReader = new CSVReader(reader);
			System.out.println("reading csv with windows-1252 encoding...");
			CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath),"Windows-1252"));
			System.out.println("reading done, converting to CsvBean...");
			
			List<CsvBean> fineArtList = csvToBean.parse(strategy, csvReader);
			
			System.out.println("conversion done, iterating bean...");
			
			//loadAuctionHouseId(fineArtList.get(1)); //the whole sheet will have the same id. At 0 index, we have column names only
			//loadAuctionHouseCurrency(fineArtList.get(1));
			
			Iterator<CsvBean> csvData = fineArtList.iterator();
			
			String imagesDirectoryPath = directoryPath + "\\images\\";
			new File(imagesDirectoryPath); //create the directory
			
			int counter = 0;
			
			try {
				while (csvData.hasNext()) {
	            	if(!continueProcessing) {
						return;
					}
	            	if(counter == 0) {
	            		System.out.println("reading row 0...");
	            		csvData.next();
	            		counter++;
	            		continue;
	            	}
	            	CsvBean csvBean = csvData.next();
	            	System.out.println("reading row: " + counter);
	            	if(!downloadImagesOnly) {
	            		if(counter <= fineArtList.size()) {
	            			String cah_auction_house_ID = auctionHouseAndIdMap.get(fineArtList.get(counter).getAuction_house_name().trim().replaceAll(" ", "-") 
	            					+ "-" + fineArtList.get(counter).getAuction_location().trim().replaceAll(" ", "-"));
	            			
	            			String auctionStartDate = "0000-00-00";
	            			SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy");
	            			
	            			if(StringUtils.isNotEmpty(fineArtList.get(counter).getAuction_start_date())) {
	            				Date auctionStartDateDate = f.parse(fineArtList.get(counter).getAuction_start_date());
	            				auctionStartDate = new SimpleDateFormat("yyyy-MM-dd").format(auctionStartDateDate);
	            			}
	            			
		            		String recordCheckSql = "SELECT fineart_lots.fal_lot_ID, fineart_lots.fal_lot_no, fineart_lots.fal_sub_lot_no, "
		            				+ "fineart_lots.fal_artwork_ID, fineart_auction_calendar.faac_auction_title, fineart_auction_calendar.faac_auction_sale_code, "
		            				+ "fineart_auction_calendar.faac_auction_start_date, core_auction_houses.cah_auction_house_ID  FROM fineart_lots "
		            				+ "INNER JOIN fineart_auction_calendar ON fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID "
		            				+ "INNER JOIN core_auction_houses ON fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID "
		            				+ "WHERE faac_auction_sale_code = '" + fineArtList.get(counter).getAuction_num().trim() + "' and "
		            				+ "fal_lot_no = '" + fineArtList.get(counter).getLot_num().trim() + "' and "
		            				+ "fal_sub_lot_no = '" + fineArtList.get(counter).getSublot_num().trim() + "' and "
		            				+ "faac_auction_start_date = '" + auctionStartDate + "' "
		            				+ "and cah_auction_house_ID = '" + cah_auction_house_ID + "' and "
		            				+ "faac_auction_title = '" + fineArtList.get(counter).getAuction_name() + "' ";
		            		
		            		try {
		            			String lotAndArtworkId = jdbcTemplate.query(recordCheckSql, new ResultSetExtractor<String>() {

			            			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			            				if (resultSet.next()) {
			            					return resultSet.getString("fal_lot_ID") + "-" + resultSet.getString("fal_artwork_ID");
			            				}
			            				return "";
			            			}
			            		});
			            		
			            		String lotId = "", artworkId = "";
			            		
			            		if(StringUtils.isNotEmpty(lotAndArtworkId)) {
			            			lotId = lotAndArtworkId.split("-")[0].trim();
				            		artworkId = lotAndArtworkId.split("-")[1].trim();
			            		}
			            		
				            	if(StringUtils.isNotEmpty(lotId) && StringUtils.isNotEmpty(artworkId)) {
				            		System.out.println("Old Entry Found with same lot, updating the lot now ######...");
				    				consoleArea.append("Old Entry Found with same lot, updating the lot now ######...");
				                	consoleArea.append("\n");
				        			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				        			counter++;
				        			
				        			//updateLot(csvBean, lotId);
				        			continue;
				            	}
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
		            		
		            		System.out.println("Processing Row " + counter + " ...");
			            	consoleArea.append("\nProcessing Row " + counter + " ...");
			            	consoleArea.append("\n");
			    			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			    			
			            	
			            	/*boolean isDisputed = checkLinkingAndDecideUpload(csvBean);
			            	if(isDisputed) {
			            		csvIssuesList.add(csvBean);
			            	}*/
			    			
			    			csvBean = linkWithArtistSQLWay(csvBean);
			    			uploadLot(csvBean);
			    			
			            	if(uploadDataWithDownloadImages) {
			            		doDownloadImages(imagesDirectoryPath, csvBean);
			            	}
			            	
			            	counter++;
		            	}
	            	} else {
	            		doDownloadImages(imagesDirectoryPath, csvBean);
	            		counter++;
	            	}
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//For all unresolved artists, write a new CSV to upload later.
            if(cleanUpload && csvIssuesList.size() > 0) { 
            	writeIssuesCsvFile("unresolved-" + Paths.get(csvPath).getFileName().toString(), directoryPath, csvIssuesList);
            }
            
            System.out.println("Data Uploading And Images Downloading Done!");
        	consoleArea.append("Data Uploading And Images Downloading Done!");
        	consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			
			System.out.println("Rescaling images now...");
        	consoleArea.append("Rescaling images now...");
        	consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			
			String lowQualityImageDirectoryPath = imagesDirectoryPath + "\\" + "low-quality";
			
			convertLowQualityImages(imagesDirectoryPath, lowQualityImageDirectoryPath);
			
			JOptionPane.showMessageDialog(null, "All actions done.", "Past Sales Uploader", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			this.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadLot(CsvBean csvBean) {
		try {
			KeyHolder holder = null;
			if(StringUtils.isEmpty(csvBean.getArtist_name()) || StringUtils.isEmpty(csvBean.getArtwork_name()) || StringUtils.isEmpty(csvBean.getAuction_house_name())
					 || StringUtils.isEmpty(csvBean.getAuction_location()) || StringUtils.isEmpty(csvBean.getAuction_name()) || StringUtils.isEmpty(csvBean.getAuction_num())
					 || StringUtils.isEmpty(csvBean.getAuction_start_date()) || StringUtils.isEmpty(csvBean.getLot_num())) {
				csvBean.setUpload_report("A mandatory field is left blank. Mandatory fields are: Artist Name, Artwork Name, Auction House Name, Auction House Location, "
						+ "Auction Name, Auction Number, Sale Start Date and Lot Number.");
				csvIssuesList.add(csvBean);
				return;
			}
			
			String auctionStartDate = "0000-00-00";
			String auctionEndDate = "0000-00-00";
			SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy");
			
			if(StringUtils.isNotEmpty(csvBean.getAuction_start_date())) {
				Date auctionStartDateDate = f.parse(csvBean.getAuction_start_date().trim());
				auctionStartDate = new SimpleDateFormat("yyyy-MM-dd").format(auctionStartDateDate);
			}
			if(StringUtils.isNotEmpty(csvBean.getAuction_end_date())) {
				Date auctionEndDateDate = f.parse(csvBean.getAuction_end_date());
				auctionEndDate = new SimpleDateFormat("yyyy-MM-dd").format(auctionEndDateDate);
			}
			
			final String auctionStartDateFinal = auctionStartDate;
			final String auctionEndDateFinal = auctionEndDate;
			
			String cah_auction_house_ID = auctionHouseAndIdMap.get(csvBean.getAuction_house_name().trim().replaceAll(" ", "-")
					+ "-" + csvBean.getAuction_location().trim().replaceAll(" ", "-"));
			
			try {
				if(!saleCodeAuctionIdMap.containsKey(csvBean.getAuction_num().trim() + "-" + auctionStartDate + "-" + cah_auction_house_ID 
						+ "-" + csvBean.getAuction_name().trim())) {
					
					////insert auction_calendar
					
					String auctionCalSql = "insert into fineart_auction_calendar (faac_auction_sale_code, faac_auction_house_ID, faac_auction_title, faac_auction_start_date, "
							+ "faac_auction_end_date, faac_auction_record_created, faac_auction_record_updated, faac_auction_record_createdby, faac_auction_record_updatedby)"
							+ " VALUES (?,?,?,?,?,?,?,?,?)";
					
					holder = new GeneratedKeyHolder();
					
					try {
						jdbcTemplate.update(new PreparedStatementCreator() {
							@Override
							public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
								PreparedStatement ps = con.prepareStatement(auctionCalSql, Statement.RETURN_GENERATED_KEYS);
								ps.setString(1, csvBean.getAuction_num());
								ps.setString(2, cah_auction_house_ID);
								ps.setString(3, FineartUtils.getUrlSafeString(FineartUtils.get_UTF8_String(csvBean.getAuction_name())));
								ps.setString(4, auctionStartDateFinal);
								if(StringUtils.isEmpty(csvBean.getAuction_end_date())) {
									ps.setString(5, "0000-00-00");
								} else {
									ps.setString(5, auctionEndDateFinal);
								}
								
								ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
								ps.setString(7, null);
								ps.setString(8, username);
								ps.setString(9, null);
										
								return ps;
							}
						}, holder);
					} catch (DuplicateKeyException e) {
						System.out.println("In exception state. Cannot upload the same sale again.###");
						csvBean.setUpload_report("Cannot upload the same sale again.");
						csvIssuesList.add(csvBean);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						csvBean.setUpload_report(e.getMessage());
						csvIssuesList.add(csvBean);
						return;
					}
					
					this.faac_auction_ID = String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
					saleCodeAuctionIdMap.put(csvBean.getAuction_num().trim() + "-" + auctionStartDate + "-" + cah_auction_house_ID 
							+ "-" + csvBean.getAuction_name().trim(), faac_auction_ID);
				} else {
					this.faac_auction_ID = saleCodeAuctionIdMap.get(csvBean.getAuction_num().trim() + "-" + auctionStartDate + "-" + cah_auction_house_ID 
							+ "-" + csvBean.getAuction_name().trim());
				}
				
				holder = new GeneratedKeyHolder();
				
				String faa_artwork_ID = insertIntoArtworks(csvBean, false);
				
				float rateStr = getAuctionDateCurrencyRate(csvBean);
				
				float fal_lot_high_estimate_USD = Float.parseFloat(StringUtils.isEmpty(csvBean.getPrice_estimate_max()) ? "0.0" : csvBean.getPrice_estimate_max()) * rateStr;
				float fal_lot_low_estimate_USD = Float.parseFloat(StringUtils.isEmpty(csvBean.getPrice_estimate_min()) ? "0.0" : csvBean.getPrice_estimate_min()) * rateStr;
				float fal_lot_sale_price_USD = Float.parseFloat(StringUtils.isEmpty(csvBean.getPrice_sold()) ? "0.0" : csvBean.getPrice_sold()) * rateStr;
				String priceKind = "bought-in";
				
				if(StringUtils.isNotEmpty(csvBean.getPrice_sold())) {
					priceKind = "sold";
				} else if( (StringUtils.isNotEmpty(csvBean.getPrice_estimate_max()) || StringUtils.isNotEmpty(csvBean.getPrice_estimate_min())) 
						&& StringUtils.isEmpty(csvBean.getPrice_sold()) ) {
					priceKind = "bought-in";
				} else if(StringUtils.isEmpty(csvBean.getPrice_estimate_max()) && StringUtils.isEmpty(csvBean.getPrice_estimate_min())  
					&& StringUtils.isEmpty(csvBean.getPrice_sold()) ) {
					priceKind = "yet to be sold";
				}
				
				if(csvBean.getPrice_kind().trim().equals("withdrawn")) {
					priceKind = "withdrawn";
				}
				
				final String priceKindFinal = priceKind;
				////insert lot
				
				String lotQuery = "INSERT INTO fineart_lots (fal_lot_no, fal_sub_lot_no, fal_artwork_ID, fal_lot_category, fal_auction_ID, fal_lot_sale_date, "
						+ "fal_lot_high_estimate,"
						+ " fal_lot_low_estimate, fal_lot_high_estimate_USD, fal_lot_low_estimate_USD, fal_lot_sale_price, fal_lot_sale_price_USD, fal_lot_price_type, "
						+ "fal_lot_status, fal_lot_condition, fal_lot_height, fal_lot_width, fal_lot_depth, fal_lot_measurement_unit, fal_lot_size_details, "
						+ "fal_lot_material, fal_lot_provenance, fal_lot_image1, fal_lot_image2, fal_lot_image3, fal_lot_image4, fal_lot_image5, fal_lot_published, "
						+ "fal_lot_record_created, fal_lot_record_updated, fal_lot_record_createdby, fal_lot_record_updatedby, fal_lot_source)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				holder = new GeneratedKeyHolder();
				
				String csvCategory = csvBean.getArtwork_category().trim().toLowerCase();
				if(csvCategory.contains("print")) {
					csvCategory = "prints";
				} else if(csvCategory.contains("paint")) {
					csvCategory = "paintings";
				} else if(csvCategory.contains("sculp")) {
					csvCategory = "sculptures";
				} else if(csvCategory.contains("photo")) {
					csvCategory = "photographs";
				} else if(csvCategory.contains("work")) {
					csvCategory = "works on paper";
				} else if(csvCategory.contains("mini")) {
					csvCategory = "miniatures";
				} else {
					csvCategory = "others";
				}
				String csvCategoryFinal = csvCategory;
				final String finaArtworkId = faa_artwork_ID;
				
				jdbcTemplate.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(lotQuery, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, csvBean.getLot_num());
						ps.setString(2, csvBean.getSublot_num());
						ps.setString(3, finaArtworkId);
						ps.setString(4, csvCategoryFinal);
						ps.setString(5, faac_auction_ID);
						ps.setString(6, auctionStartDateFinal);	// auction start date
						ps.setString(7, StringUtils.isEmpty(csvBean.getPrice_estimate_max()) ? "0.00" : csvBean.getPrice_estimate_max());
						ps.setString(8, StringUtils.isEmpty(csvBean.getPrice_estimate_min()) ? "0.00" : csvBean.getPrice_estimate_min());
						ps.setFloat(9, fal_lot_high_estimate_USD);
						ps.setFloat(10, fal_lot_low_estimate_USD);
						ps.setString(11, StringUtils.isEmpty(csvBean.getPrice_sold()) ? "0.00" : csvBean.getPrice_sold());
						ps.setFloat(12, fal_lot_sale_price_USD);
						ps.setString(13, priceType);
						ps.setString(14, priceKindFinal);
						ps.setString(15, FineartUtils.get_UTF8_String(csvBean.getArtwork_condition_in()));
						ps.setString(16, StringUtils.isEmpty(csvBean.getArtwork_measurements_height().trim()) ? "0.00" : csvBean.getArtwork_measurements_height().trim());
						ps.setString(17, StringUtils.isEmpty(csvBean.getArtwork_measurements_width().trim()) ? "0.00" : csvBean.getArtwork_measurements_width().trim());
						ps.setString(18, StringUtils.isEmpty(csvBean.getArtwork_measurements_depth().trim()) ? "0.00" : csvBean.getArtwork_measurements_depth().trim());
						ps.setString(19, csvBean.getAuction_measureunit());
						ps.setString(20, FineartUtils.get_UTF8_String(csvBean.getArtwork_size_notes()));
						ps.setString(21, FineartUtils.get_UTF8_String(csvBean.getArtwork_materials()));
						ps.setString(22, FineartUtils.get_UTF8_String(csvBean.getArtwork_provenance()));
						ps.setString(23, csvBean.getImage1_name());
						ps.setString(24, csvBean.getImage2_name());
						ps.setString(25, csvBean.getImage3_name());
						ps.setString(26, csvBean.getImage4_name());
						ps.setString(27, csvBean.getImage5_name());
						ps.setString(28, "no"); // lot published
						ps.setTimestamp(29, new Timestamp(System.currentTimeMillis()));
						ps.setString(30, null);
						ps.setString(31, username);
						ps.setString(32, null);
						ps.setString(33, csvBean.getLot_origin_url());
								
						return ps;
					}
				}, holder);
				
				String fal_lot_ID = String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
				
				System.out.println("Uploaded lot with generated id:" + fal_lot_ID);
				consoleArea.append("\n" + "Uploaded lot with generated id:" + fal_lot_ID);
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				if(!showConsole) {
					closeWindow();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				consoleArea.append("\n" + e.getMessage());
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateLot(CsvBean csvBean, String lotId) {
		try {
			if(StringUtils.isEmpty(csvBean.getArtist_name()) || StringUtils.isEmpty(csvBean.getArtwork_name()) || StringUtils.isEmpty(csvBean.getAuction_house_name())
					 || StringUtils.isEmpty(csvBean.getAuction_location()) || StringUtils.isEmpty(csvBean.getAuction_name()) || StringUtils.isEmpty(csvBean.getAuction_num())
					 || StringUtils.isEmpty(csvBean.getAuction_start_date()) || StringUtils.isEmpty(csvBean.getLot_num())) {
				csvBean.setUpload_report("A mandatory field is left blank. Mandatory fields are: Artist Name, Artwork Name, Auction House Name, Auction House Location, "
						+ "Auction Name, Auction Number, Sale Start Date and Lot Number.");
				csvIssuesList.add(csvBean);
				return;
			}
			
			try {
				float rateStr = getAuctionDateCurrencyRate(csvBean);
				
				float fal_lot_sale_price_USD = Float.parseFloat(StringUtils.isEmpty(csvBean.getPrice_sold()) ? "0.0" : csvBean.getPrice_sold()) * rateStr;
				String priceKind = "bought-in";
				
				if(StringUtils.isNotEmpty(csvBean.getPrice_sold())) {
					priceKind = "sold";
				} else if( (StringUtils.isNotEmpty(csvBean.getPrice_estimate_max()) || StringUtils.isNotEmpty(csvBean.getPrice_estimate_min())) 
						&& StringUtils.isEmpty(csvBean.getPrice_sold()) ) {
					priceKind = "bought-in";
				} else if(StringUtils.isEmpty(csvBean.getPrice_estimate_max()) && StringUtils.isEmpty(csvBean.getPrice_estimate_min())  
					&& StringUtils.isEmpty(csvBean.getPrice_sold()) ) {
					priceKind = "yet to be sold";
				}
				
				if(csvBean.getPrice_kind().trim().equals("withdrawn")) {
					priceKind = "withdrawn";
				}
				
				final String priceKindFinal = priceKind;
				////insert lot
				
				String lotQuery = "UPDATE fineart_lots SET fal_lot_sale_price = ?, fal_lot_sale_price_USD = ?, "
						+ "fal_lot_status = ?, fal_lot_record_updated = ?, fal_lot_record_updatedby = ? WHERE fal_lot_ID = ?";
				
				int numberOfRowsAffected = jdbcTemplate.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(lotQuery, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, StringUtils.isEmpty(csvBean.getPrice_sold()) ? "0.00" : csvBean.getPrice_sold());
						ps.setFloat(2, fal_lot_sale_price_USD);
						ps.setString(3, priceKindFinal);
						ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
						ps.setString(5, username);
						ps.setString(6, lotId);
								
						return ps;
					}
				});
				
				if(numberOfRowsAffected > 0) {
					System.out.println("Updated lot for lot id:" + lotId);
					consoleArea.append("\n" + "Updated lot for lot id:" + lotId);
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				}
				
				if(!showConsole) {
					closeWindow();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				consoleArea.append("\n" + e.getMessage());
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public float getAuctionDateCurrencyRate(CsvBean csvBean) throws ParseException {
		float rateStr = 1.0f;
		
		String cah_auction_house_currency_code = auctionHouseAndCurrencyMap.get(csvBean.getAuction_house_name().trim().replaceAll(" ", "-").trim() 
				+ "-" + csvBean.getAuction_location().trim().replaceAll(" ", "-").trim());
		
		if(!cah_auction_house_currency_code.equals("USD")) {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
			Date d1 = dateFormat.parse(csvBean.getAuction_start_date());
			
		    Calendar cal = new GregorianCalendar(); 
		    cal.setTime(d1);
		    cal.add(Calendar.MONTH, -6);
		    
		    long millisecondsTo = cal.getTimeInMillis();
		    
		    Date d2 = new Date(millisecondsTo);
		    
		    String dateTo = new SimpleDateFormat("yyyy-MM-dd").format(d1);
		    String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(d2);
			
			String currencyRateSql = "select * from currency_exchange_rate WHERE cexr_date BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND cexr_base_currency = '" 
			+ cah_auction_house_currency_code + "' ORDER BY cexr_date DESC LIMIT 1";
			
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
		return rateStr;
	}

	public String insertIntoArtworks(CsvBean csvBean, boolean requiresReview) {
		
		String artworksQuery = "insert into fineart_artworks (faa_artwork_title, faa_artwork_requires_review, faa_artwork_description, faa_artist_ID, faa_artwork_category, faa_artwork_material, "
				+ "faa_artwork_edition, faa_artwork_exhibition, faa_artwork_literature, faa_artwork_height, "
				+ "faa_artwork_width, faa_artwork_depth, faa_arwork_measurement_unit, faa_artwork_size_details, faa_artwork_markings, faa_artwork_start_year, "
				+ "faa_artwork_end_year, "
				+ "faa_artwork_image1, faa_artwork_record_created, faa_artwork_record_updated, faa_artwork_record_createdby, "
				+ "faa_artwork_record_updatedby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		KeyHolder holder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(artworksQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, FineartUtils.getUrlSafeString(FineartUtils.get_UTF8_String(csvBean.getArtwork_name())));
				if(requiresReview) {
					ps.setInt(2, 1);
				} else {
					ps.setInt(2, 0);
				}
				ps.setString(3, FineartUtils.get_UTF8_String(csvBean.getArtwork_description()));
				ps.setString(4, csvBean.getArtist_ID());
				ps.setString(5, csvBean.getArtwork_category());
				ps.setString(6, FineartUtils.get_UTF8_String(csvBean.getArtwork_materials()));
				ps.setString(7, FineartUtils.get_UTF8_String(csvBean.getArtwork_edition()));
				ps.setString(8, FineartUtils.get_UTF8_String(csvBean.getArtwork_exhibited()));
				ps.setString(9, FineartUtils.get_UTF8_String(csvBean.getArtwork_literature()));
				ps.setString(10, StringUtils.isEmpty(csvBean.getArtwork_measurements_height().trim()) ? "0.00" : csvBean.getArtwork_measurements_height().trim());
				ps.setString(11, StringUtils.isEmpty(csvBean.getArtwork_measurements_width().trim()) ? "0.00" : csvBean.getArtwork_measurements_width().trim());
				ps.setString(12, StringUtils.isEmpty(csvBean.getArtwork_measurements_depth().trim()) ? "0.00" : csvBean.getArtwork_measurements_depth().trim());
				ps.setString(13, csvBean.getAuction_measureunit());
				ps.setString(14, FineartUtils.get_UTF8_String(csvBean.getArtwork_size_notes()));
				ps.setString(15, FineartUtils.get_UTF8_String(csvBean.getArtwork_markings()));
				ps.setString(16, StringUtils.isEmpty(csvBean.getArtwork_start_year()) ? "0" : csvBean.getArtwork_start_year());
				ps.setString(17, StringUtils.isEmpty(csvBean.getArtwork_end_year()) ? "0" : csvBean.getArtwork_end_year());
				ps.setString(18, csvBean.getImage1_name());
				ps.setTimestamp(19, new Timestamp(System.currentTimeMillis()));
				ps.setString(20, null);
				ps.setString(21, username);
				ps.setString(22, null);
						
				return ps;
			}
		}, holder);
		
		return String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
	}
	
	public CsvBean linkWithArtistSQLWay(CsvBean csvBean) {
		try {
			String artistName = csvBean.getArtist_name();
			String artistBirthYear = csvBean.getArtist_birth();
			String artistDeathYear = csvBean.getArtist_death();
			
			if(StringUtils.isEmpty(artistBirthYear.trim())) {
				artistBirthYear = "0";
			}
			if(StringUtils.isEmpty(artistDeathYear.trim())) {
				artistDeathYear = "0";
			}
			
			artistName = FineartUtils.getArtistPureName(artistName);
			
			artistName = artistName.replace("'", "");
			
			String artistExistanceCheckQuery = "";
			
			if(!artistBirthYear.equals("0")) {
				artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_name = '" + FineartUtils.get_UTF8_String(artistName) 
				+ "' and fa_artist_birth_year = '" + artistBirthYear + "'";
			} else {
				artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_name = '" + FineartUtils.get_UTF8_String(artistName) + "'";
			}
			
			List<FineartArtistsData> artistDataList = jdbcTemplate.query(artistExistanceCheckQuery, new FineartArtistsDataMapper());
			
			//reverse the words
			if(artistDataList.size() <= 0) {
				artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_name = '" + FineartUtils.get_UTF8_String(FineartUtils.reverseWords(artistName)) + "'";
				artistDataList = jdbcTemplate.query(artistExistanceCheckQuery, new FineartArtistsDataMapper());
			}
			
			if(artistDataList.size() <= 0) {
				artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_aka like '%" + FineartUtils.get_UTF8_String(artistName) + "%'";
				artistDataList = jdbcTemplate.query(artistExistanceCheckQuery, new FineartArtistsDataMapper());
			}
			
			if(artistDataList.size() > 0) {
				csvBean.setArtist_ID(String.valueOf(artistDataList.get(0).getFa_artist_ID()));
				System.out.println("=====================MATCHED==================================");
			    System.out.println("(CSV):" + artistName + " (" + artistBirthYear + ")");
			    System.out.println("(DB):" + artistDataList.get(0).getFa_artist_name() + " (" + artistDataList.get(0).getFa_artist_birth_year() + ")");
			    System.out.println("==============================================================");
			    
			    consoleArea.append("=====================MATCHED==================================");
			    consoleArea.append("(CSV):" + artistName + " (" + artistBirthYear + ")");
			    consoleArea.append("(DB):" + artistDataList.get(0).getFa_artist_name() + " (" + artistDataList.get(0).getFa_artist_birth_year() + ")");
			    consoleArea.append("==============================================================");
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			} else {
				csvBean.setArtist_ID("1");
				System.out.println("=====================NOT MATCHED==================================");
			    System.out.println("(CSV):" + FineartUtils.get_UTF8_String(artistName) + " (" + artistBirthYear + ")");
			    System.out.println("==============================================================");
			    
			    consoleArea.append("=====================NOT MATCHED==================================");
			    consoleArea.append("(CSV):" + FineartUtils.get_UTF8_String(artistName) + " (" + artistBirthYear + ")");
			    consoleArea.append("==============================================================");
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvBean;
	}

	private void writeIssuesCsvFile(String csvFilename, String directoryPath, List<CsvBean> csvIssuesList) {
		try {
			CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(directoryPath + "\\" + csvFilename), "UTF-8"));
			
			StringBuilder stats = new StringBuilder();
			stats.append("auction_house_name|auction_location|auction_num|auction_start_date|auction_end_date|auction_name|");
			stats.append("lot_num|sublot_num|price_kind|price_estimate_min|price_estimate_max|price_sold|");
			stats.append("artist_name|artist_birth|artist_death|artist_nationality|");
			stats.append("artwork_name|artwork_year_identifier|artwork_start_year|artwork_end_year|");
			stats.append("artwork_materials|artwork_category|artwork_markings|");
			stats.append("artwork_edition|artwork_description|artwork_measurements_height|artwork_measurements_width|");
			stats.append("artwork_measurements_depth|artwork_size_notes|auction_measureunit|artwork_condition_in|");
			stats.append("artwork_provenance|artwork_exhibited|artwork_literature|artwork_images1|");
			stats.append("artwork_images2|artwork_images3|artwork_images4|artwork_images5|image1_name|image2_name|image3_name|image4_name|image5_name|lot_origin_url|");
			stats.append("artist_ID|artwork_ID|upload_report");
			
			csvWriter.writeNext(stats.toString().split("\\|"));
			
			for(CsvBean csvBean : csvIssuesList) {
				String auctionDetails = csvBean.getAuction_house_name() + "|" + csvBean.getAuction_location() + "|" + csvBean.getAuction_num() + "|" 
						+ csvBean.getAuction_start_date() + "|" + csvBean.getAuction_end_date() + "|" + csvBean.getAuction_name() 
						+ "|" + csvBean.getLot_num() + "|" + csvBean.getSublot_num() + "|" + csvBean.getPrice_kind() 
						+ "|" + csvBean.getPrice_estimate_min() + "|" + csvBean.getPrice_estimate_max() + "|" + csvBean.getPrice_sold() + "|" 
						+ csvBean.getArtist_name() + "|" + csvBean.getArtist_birth() + "|" + csvBean.getArtist_death() + "|" + csvBean.getArtist_nationality() + "|"
						+ csvBean.getArtwork_name() + "|" + csvBean.getArtwork_year_identifier() + "|" + csvBean.getArtwork_start_year() + "|" 
						+ csvBean.getArtwork_end_year() + "|" + csvBean.getArtwork_materials() + "|" + csvBean.getArtwork_category() + "|"
						+ csvBean.getArtwork_markings() + "|" + csvBean.getArtwork_edition() + "|" + csvBean.getArtwork_description() + "|"
						+ csvBean.getArtwork_measurements_height() + "|" + csvBean.getArtwork_measurements_width() + "|" + csvBean.getArtwork_measurements_depth()
						+ "|" + csvBean.getArtwork_size_notes() + "|" + csvBean.getAuction_measureunit() + "|" + csvBean.getArtwork_condition_in() + "|"
						+ csvBean.getArtwork_provenance() + "|" + csvBean.getArtwork_exhibited() + "|" + csvBean.getArtwork_literature() + "|" 
						+ csvBean.getArtwork_images1() + "|" + csvBean.getArtwork_images2() + "|" + csvBean.getArtwork_images3() + "|" + csvBean.getArtwork_images4() 
						+ "|" + csvBean.getArtwork_images5() + "|" + csvBean.getImage1_name() + "|" + csvBean.getImage2_name() + "|" + csvBean.getImage3_name() 
						+ "|" + csvBean.getImage4_name() + "|" + csvBean.getImage5_name() + "|" + csvBean.getLot_origin_url() + "|" + csvBean.getArtist_ID() 
						+ "|" + csvBean.getArtwork_ID() + "|" + csvBean.getUpload_report();

				String row[] = auctionDetails.split("\\|");
				csvWriter.writeNext(row);
			}
			csvWriter.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public void doDownloadImages(String directoryPath, CsvBean csvBean) {
		if(StringUtils.isNotEmpty(csvBean.getArtwork_images1())) {
			downloadImage(csvBean.getImage1_name(), csvBean.getArtwork_images1(), directoryPath);
		}
		if(StringUtils.isNotEmpty(csvBean.getArtwork_images2())) {
			downloadImage(csvBean.getImage2_name(), csvBean.getArtwork_images2(), directoryPath);
		}
		if(StringUtils.isNotEmpty(csvBean.getArtwork_images3())) {
			downloadImage(csvBean.getImage3_name(), csvBean.getArtwork_images3(), directoryPath);
		}
		if(StringUtils.isNotEmpty(csvBean.getArtwork_images4())) {
			downloadImage(csvBean.getImage4_name(), csvBean.getArtwork_images4(), directoryPath);
		}
		if(StringUtils.isNotEmpty(csvBean.getArtwork_images5())) {
			downloadImage(csvBean.getImage5_name(), csvBean.getArtwork_images5(), directoryPath);
		}
	}
	
	public void downloadImage(String fileName, String imagePath, String directoryPath) {
		try {
			browser.visit(imagePath);
			Element imgElement = null;
			try {
				imgElement = browser.doc.findFirst("<img src='.*jpg'>");
			} catch (com.jauntium.NotFound ex) {
				imgElement = browser.doc.findFirst("<img src='.*png'>");
			}
		    String url = imgElement.getAt("src");
		    
		    
		    System.out.println("Downloading image: " + url);
        	consoleArea.append("\nDownloading image: " + url);
        	consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			
		    browser.download(url, new File(directoryPath + fileName)); 
			
		} catch (Exception e) {
			try {
				try(InputStream in = new URL(imagePath).openStream()) {
				    Files.copy(in, Paths.get(directoryPath + fileName));
				    System.out.println("Copied file: " + fileName);
				}
			} catch (Exception ex) {
				System.out.println("Error in downloading image: " + e.getMessage());
	        	consoleArea.append("\nError in downloading image: " + e.getMessage());
	        	consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
	        	
			}
		}
	}
	
	public void loadUploadedData() {
		
	saleCodeAuctionIdMap = new HashMap<>();
	String auctionCalSql = "SELECT fineart_auction_calendar.faac_auction_ID, fineart_auction_calendar.faac_auction_sale_code, "
			+ "faac_auction_house_ID, faac_auction_start_date, faac_auction_end_date, faac_auction_title FROM fineart_auction_calendar";
			
			jdbcTemplate.query(auctionCalSql, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					while (resultSet.next()) {
						saleCodeAuctionIdMap.put(resultSet.getString("faac_auction_sale_code").trim() 
								+ "-" +resultSet.getString("faac_auction_start_date").trim()
								+ "-" +resultSet.getString("faac_auction_house_ID").trim()
								+ "-" +resultSet.getString("faac_auction_title").trim(), 
								resultSet.getString("faac_auction_ID"));
					}
					return true;
				}
			});
	}
	
	private void loadAuctionHouseIdAndCurrency() {
		////get auction house id
		auctionHouseAndIdMap = new HashMap<>();
		auctionHouseAndCurrencyMap = new HashMap<>();
		String auctionHouseSql = "select * from core_auction_houses";
		
		jdbcTemplate.query(auctionHouseSql, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				while (resultSet.next()) {
					String auctionHouseNameLocationKey = resultSet.getString("cah_auction_house_name").trim().replaceAll(" ", "-") 
							+ "-" + resultSet.getString("cah_auction_house_location").trim().replaceAll(" ", "-");
					
					auctionHouseAndIdMap.put(auctionHouseNameLocationKey, resultSet.getString("cah_auction_house_ID").trim());
					auctionHouseAndCurrencyMap.put(auctionHouseNameLocationKey, resultSet.getString("cah_auction_house_currency_code").trim());
				}
				return true;
			}
		});
	}
	
	public void convertLowQualityImages(String inputDirectoryPath, String outputDirectoryPath) {
		File imageOriginDir = new File(inputDirectoryPath);
		List<String> resizedFileNames = new ArrayList<>();

		String[] filesToBeResized = imageOriginDir.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isFile();
			  }
			});
		
		File imageDestDir = new File(outputDirectoryPath);
		String[] filesAlreadyResized = imageDestDir.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isFile();
			  }
			});
		
		for(String resizedImage : filesAlreadyResized) {
			resizedFileNames.add(resizedImage);
		}

		for (String imageFile : filesToBeResized) {
			try {
				if(resizedFileNames.contains(imageFile)) {
					System.out.println("File already resized: " + imageFile);
					consoleArea.append("File already resized: " + imageFile);
		        	consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
					continue;
				}
				
				System.out.println("Resizing image: " + imageFile);
				consoleArea.append("Resizing image: " + imageFile);
	        	consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				
				BufferedImage originalImage = ImageIO.read(new File(inputDirectoryPath + "//" + imageFile));

				BufferedImage destImage;

				float scaledW = 0, scaledH = 0;
				float originalImageWidth = originalImage.getWidth();
				float originalImageHeight = originalImage.getHeight();
				
				String pathName1000 = outputDirectoryPath + "//" + imageFile;
				
				File file = new File(pathName1000);
				file.getParentFile().mkdirs();

				if (originalImageWidth > 1000) {
					scaledW = 1000;
					scaledH = (1000 * originalImageHeight) / originalImageWidth;
					
					destImage = createAveragedImage(originalImage, (int) scaledW, (int) scaledH);
					ImageIO.write(destImage, "jpg", new File(pathName1000));
				} else {
					ImageIO.write(originalImage, "jpg", new File(pathName1000));
					destImage = originalImage;	
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Resizing Done.");
				consoleArea.append("Resizing Done.");
		    	consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}  
		}
		System.out.println("Resizing Done.");
		consoleArea.append("Resizing Done.");
    	consoleArea.append("\n");
		consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
	}
	
	private static BufferedImage createAveragedImage(BufferedImage inputImage, int dstwidth, int dstheight) {
		int xsrc, ysrc;
		int xdst, ydst;
		int srcwidth = inputImage.getWidth();
		int srcheight = inputImage.getHeight();
		double xerr, yerr;
		double xincr = (double) dstwidth / (double) srcwidth;
		double yincr = (double) dstheight / (double) srcheight;
		AveragingRgbRaster dstRaster = new AveragingRgbRaster(dstwidth);
		BufferedImage newImage = new BufferedImage(dstwidth, dstheight, inputImage.getType());

		ydst = 0;
		for (ysrc = 0, yerr = 0; ysrc < srcheight; ysrc++, yerr += yincr) {
			if (yerr > 1.0) {
				dstRaster.output(newImage, ydst);
				ydst++;
				yerr -= 1.0;
			}

			xdst = 0;
			for (xsrc = 0, xerr = 0; xsrc < srcwidth; xsrc++, xerr += xincr) {
				if (xerr > 1.0) {
					xerr -= 1.0;
					xdst++;
				}

				dstRaster.add(xdst, inputImage.getRGB(xsrc, ysrc));
			}
		}

		if (ydst < dstheight)
			dstRaster.output(newImage, ydst);

		return newImage;
	}

	private static class AveragingRgbRaster {
		private int width;
		private int[] r, g, b, cnt;

		public AveragingRgbRaster(int width) {
			this.width = width;
			r = new int[width];
			g = new int[width];
			b = new int[width];
			cnt = new int[width];
		}

		public void add(int x, int rgb) {
			r[x] += (rgb >> 16) & 0xFF;
			g[x] += (rgb >> 8) & 0xFF;
			b[x] += (rgb >> 0) & 0xFF;
			cnt[x]++;
		}

		public void output(BufferedImage outputImage, int y) {
			int rr, gg, bb, rgb, cnt2;

			for (int x = 0; x < width; x++) {
				cnt2 = cnt[x];
				if (cnt2 == 0) // just in case
				{
					outputImage.setRGB(x, y, 0xFF000000);
				} else {
					// output average RGB pixel value
					rr = r[x] / cnt2;
					gg = g[x] / cnt2;
					bb = b[x] / cnt2;
					rgb = 0xFF000000 | (rr << 16) | (gg << 8) | (bb);
					outputImage.setRGB(x, y, rgb);
				}
				r[x] = g[x] = b[x] = cnt[x] = 0;
			}
		}
	}
	
	/*@SuppressWarnings("deprecation")
	private CsvBean linkWithArtwork(CsvBean csvBean) {
		try {
			String artworkTitle = csvBean.getArtwork_name();
			String artistId = csvBean.getArtist_ID();
			String artworkStartYear = csvBean.getArtwork_start_year();
			String artworkEndYear = csvBean.getArtwork_end_year();
			
			if(StringUtils.isEmpty(artworkStartYear)) {
				artworkStartYear = "0";
			}
			
			if(StringUtils.isEmpty(artworkEndYear)) {
				artworkEndYear = "0";
			}
			
			if(StringUtils.isEmpty(artistId)) {
				artistId = "0";
			}
			
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			Directory directory = null;
			
			directory = FSDirectory.open(new File(artworkIndexPath));
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			int hitsPerPage = 5;
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			ScoreDoc[] hits = null;
			org.apache.lucene.document.Document document = null;
			
			String [] fields = {"faa_artwork_title","faa_artist_ID","faa_artwork_start_year","faa_artwork_end_year"};
			Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("faa_artwork_title:" + QueryParser.escape(artworkTitle) + " AND faa_artist_ID:" 
			+ artistId + " AND faa_artwork_start_year:" + artworkStartYear + " AND faa_artwork_end_year:" + artworkEndYear);
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			
			if(hits.length >= 1) {
				for(int i=0; i<hits.length; ++i) {
				    int docId = hits[i].doc;
				    float searchScore = hits[0].score;
				    if(searchScore > 5) {
				    	document = searcher.doc(docId);
					    csvBean.setArtwork_ID(document.get("faa_artwork_ID"));
					    //System.out.println("Found Artwork ID (DB): " + document.get("faa_artwork_ID") + ", Name: " + document.get("faa_artwork_title"));
					    consoleArea.append("\n" + "Found Artwork ID (DB): " + document.get("faa_artwork_ID") + ", Name: " + document.get("faa_artwork_title"));
						consoleArea.append("\n");
						consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				    }
				}
			}
		} catch (NullPointerException e) {
			//e.printStackTrace();
			System.out.println("Parse Exception in Lucene Search for Search Box Query");
			consoleArea.append("\n" + "Parse Exception in Lucene Search for Search Box Query");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvBean;
	}*/
	
	/*public CsvBean linkWithArtistSQLWay(CsvBean csvBean) {
	try {
		String artistName = csvBean.getArtist_name();
		String artistBirthYear = csvBean.getArtist_birth();
		String artistDeathYear = csvBean.getArtist_death();
		
		if(StringUtils.isEmpty(artistBirthYear.trim())) {
			artistBirthYear = "0";
		}
		if(StringUtils.isEmpty(artistDeathYear.trim())) {
			artistDeathYear = "0";
		}
		
		artistName = FineartUtils.getArtistPureName(artistName);
		
		String artistExistanceCheckQuery = "";
		
		if(!artistBirthYear.equals("0")) {
			artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_name = '" + getSqlSafeString(artistName) 
			+ "' and fa_artist_birth_year = '" + artistBirthYear + "'";
		} else {
			artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_name = '" + getSqlSafeString(artistName) + "'";
		}
		
		List<FineartArtistsData> artistDataList = jdbcTemplate.query(artistExistanceCheckQuery, new FineartArtistsDataMapper());
		
		if(artistDataList.size() <= 0) {
			artistExistanceCheckQuery = "select * from fineart_artists where fa_artist_aka = '" + getSqlSafeString(artistName) + "'";
			artistDataList = jdbcTemplate.query(artistExistanceCheckQuery, new FineartArtistsDataMapper());
		}
		
		if(artistDataList.size() > 0) {
			csvBean.setArtist_ID(String.valueOf(artistDataList.get(0).getFa_artist_ID()));
			System.out.println("=====================MATCHED==================================");
		    System.out.println("(CSV):" + artistName + " (" + artistBirthYear + ")");
		    System.out.println("(DB):" + artistDataList.get(0).getFa_artist_name() + " (" + artistDataList.get(0).getFa_artist_birth_year() + ")");
		    System.out.println("==============================================================");
		    
		    consoleArea.append("=====================MATCHED==================================");
		    consoleArea.append("(CSV):" + artistName + " (" + artistBirthYear + ")");
		    consoleArea.append("(DB):" + artistDataList.get(0).getFa_artist_name() + " (" + artistDataList.get(0).getFa_artist_birth_year() + ")");
		    consoleArea.append("==============================================================");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} else {
			System.out.println("=====================NOT MATCHED==================================");
		    System.out.println("(CSV):" + artistName + " (" + artistBirthYear + ")");
		    System.out.println("==============================================================");
		    
		    consoleArea.append("=====================NOT MATCHED==================================");
		    consoleArea.append("(CSV):" + artistName + " (" + artistBirthYear + ")");
		    consoleArea.append("==============================================================");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return csvBean;
}*/
	
	/*@SuppressWarnings("deprecation")
	private void addArtworkToLocalIndex(String faa_artwork_ID, CsvBean csvBean) {
		IndexWriter writer = null;
		try {
			File file = new File(artworkIndexPath);
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
	        writer = new IndexWriter(FSDirectory.open(file), config);
	        
	        String artworkTitle = StringUtils.isEmpty(csvBean.getArtwork_name()) ? "na" : csvBean.getArtwork_name();
	        String artworkDescription = StringUtils.isEmpty(csvBean.getArtwork_description()) ? "" : csvBean.getArtwork_description();
	        String artistId = csvBean.getArtist_ID();
	        String category = StringUtils.isEmpty(csvBean.getArtwork_category()) ? "paintings" : csvBean.getArtwork_category();
	        String material = StringUtils.isEmpty(csvBean.getArtwork_materials()) ? "" : csvBean.getArtwork_materials();
	        String edition = StringUtils.isEmpty(csvBean.getArtwork_edition()) ? "" : csvBean.getArtwork_edition();
	        String exhibition = StringUtils.isEmpty(csvBean.getArtwork_exhibited()) ? "" : csvBean.getArtwork_exhibited();
	        String literature = StringUtils.isEmpty(csvBean.getArtwork_literature()) ? "" : csvBean.getArtwork_literature();
	        String height = StringUtils.isEmpty(csvBean.getArtwork_measurements_height()) ? "0.00" : csvBean.getArtwork_measurements_height();
	        String width = StringUtils.isEmpty(csvBean.getArtwork_measurements_width()) ? "0.00" : csvBean.getArtwork_measurements_width();
	        String depth = StringUtils.isEmpty(csvBean.getArtwork_measurements_depth()) ? "0.00" : csvBean.getArtwork_measurements_depth();
	        String unit = StringUtils.isEmpty(csvBean.getAuction_measureunit()) ? "na" : csvBean.getAuction_measureunit();
	        String sizeDeails = StringUtils.isEmpty(csvBean.getArtwork_size_notes()) ? "" : csvBean.getArtwork_size_notes();
	        String markings = StringUtils.isEmpty(csvBean.getArtwork_markings()) ? "" : csvBean.getArtwork_markings();
	        String artworkStart = StringUtils.isEmpty(csvBean.getArtwork_start_year()) ? "0" : csvBean.getArtwork_start_year();
	        String artworkEnd = StringUtils.isEmpty(csvBean.getArtwork_end_year()) ? "0" : csvBean.getArtwork_end_year();
	        String image1 = StringUtils.isEmpty(csvBean.getImage1_name()) ? "na" : csvBean.getImage1_name();
	        

	    	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
	    	document.add(new Field("faa_artwork_ID", faa_artwork_ID, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_title", artworkTitle, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_description", artworkDescription, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artist_ID", artistId, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_category", category, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_material", material, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_edition", edition, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_exhibition", exhibition, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_literature", literature, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_height", height, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_width", width, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_depth", depth, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_arwork_measurement_unit", unit, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_size_details", sizeDeails, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_markings", markings, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_start_year", artworkStart, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_start_year_identifier", "exact", Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_start_year_precision", "decade", Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_end_year", artworkEnd, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_end_year_identifier", "exact", Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_end_year_precision", "decade", Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_image1", image1, Field.Store.YES, Field.Index.ANALYZED));
	        
	        writer.updateDocument(new Term("id", faa_artwork_ID), document);
	        writer.commit();
	        writer.close();
	        
	        //System.out.println("Added to Artwork Local Index!");
	        consoleArea.append("\n" + "Added to Artwork Local Index!");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
			try {
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}*/
	
	/*private void updateNumberOfLotsForThisSale(List<CsvBean> fineArtList) {
	Set<String> thisSheetSales = new HashSet<>();
	for(CsvBean csvBean : fineArtList) {
		thisSheetSales.add(csvBean.getAuction_num().trim());
	}
	
	for(String sale : thisSheetSales) {
		int lotCounter = 0;
		for (String saleCode : saleCodeAndLotsList)  {
			if(saleCode.startsWith(sale)) {
				lotCounter++;
			}
		}
		String updateQuery = "update fineart_auction_calendar SET faac_auction_lot_count = ? where faac_auction_sale_code = ?";
		jdbcTemplate.update(updateQuery, lotCounter, sale);
		System.out.println("Sale Number: " + sale + " Total Lots: " + lotCounter); 
		consoleArea.append("Sale Number: " + sale + " Total Lots: " + lotCounter);
    	consoleArea.append("\n");
		consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
	}
}
	@SuppressWarnings("deprecation")
	private void addKeyInSaleLotsKeyIndex(CsvBean csvBean) {
		IndexWriter writer = null;
		try {
			File file = new File(saleLotsIndexPath);
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
	        writer = new IndexWriter(FSDirectory.open(file), config);
	        
	        String auctionStartDate = "0000-00-00";
			SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy");
			
			if(StringUtils.isNotEmpty(csvBean.getAuction_start_date())) {
				Date auctionStartDateDate = f.parse(csvBean.getAuction_start_date());
				auctionStartDate = new SimpleDateFormat("yyyy-MM-dd").format(auctionStartDateDate);
			}
			
			String cah_auction_house_ID = auctionHouseAndIdMap.get(csvBean.getAuction_house_name().trim().replaceAll(" ", "-")
					+ "-" + csvBean.getAuction_location().trim().replaceAll(" ", "-"));
	        
	        String auctionRecordKey = csvBean.getAuction_num().trim() 
	        		+ "-" + csvBean.getLot_num().trim() 
	        		+ "-" + csvBean.getSublot_num().trim()
	        		+ "-" + auctionStartDate 
		            + "-" + cah_auction_house_ID;
    		auctionRecordKey = auctionRecordKey.trim();
	        
	        org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
	    	document.add(new Field("faac_auction_sale_code", csvBean.getAuction_num().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("fal_lot_no", csvBean.getLot_num().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("fal_sub_lot_no", csvBean.getSublot_num().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        
	        document.add(new Field("sale_lot_key", auctionRecordKey, Field.Store.YES, Field.Index.ANALYZED));
	        
	        writer.updateDocument(new Term("id", auctionRecordKey), document);
	        writer.commit();
	        writer.close();
	        
	        //System.out.println("Added to Artwork Local Index!");
	        consoleArea.append("\n" + "Added to Sale And Lot Local Index!");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
			try {
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/*@SuppressWarnings("deprecation")
	public CsvBean linkWithArtist(CsvBean csvBean) {
		try {
			String artistName = csvBean.getArtist_name();
			String artistBirthYear = csvBean.getArtist_birth();
			String artistDeathYear = csvBean.getArtist_death();
			
			if(StringUtils.isEmpty(artistBirthYear.trim())) {
				artistBirthYear = "0";
			}
			if(StringUtils.isEmpty(artistDeathYear.trim())) {
				artistDeathYear = "0";
			}
			
			artistName = FineartUtils.getArtistPureName(artistName);
			
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			Directory directory = null;
			
			directory = FSDirectory.open(new File(artistsIndexPath));
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			int hitsPerPage = 5;
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			ScoreDoc[] hits = null;
			org.apache.lucene.document.Document document = null;
			
			if(!artistBirthYear.equals("0")) {
				String [] fields = {"fa_artist_name","fa_artist_birth_year"};
				Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("fa_artist_name:" + QueryParser.escape(artistName) 
				+ " AND fa_artist_birth_year:" + artistBirthYear);
				searcher.search(query, collector);
				hits = collector.topDocs().scoreDocs;
			} else {
				String [] fields = {"fa_artist_name"};
				Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("fa_artist_name:" + QueryParser.escape(artistName));
				searcher.search(query, collector);
				hits = collector.topDocs().scoreDocs;
			}
			if(hits.length == 0) {
				String [] fields = {"fa_artist_aka"};
				Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("fa_artist_aka:" + QueryParser.escape(artistName));
				searcher.search(query, collector);
				hits = collector.topDocs().scoreDocs;
			}
			
			
			if(hits.length > 0) {
				float searchScore = hits[0].score;
				//System.out.println("Search Score: " + searchScore);
				if(searchScore >= 5.0) {
				    int docId = hits[0].doc;
				    document = searcher.doc(docId);
				    csvBean.setArtist_ID(document.get("fa_artist_ID"));
				    
				    System.out.println("=====================MATCHED==================================");
				    System.out.println("(CSV):" + artistName + " (" + artistBirthYear + ")");
				    System.out.println("(DB):" + document.get("fa_artist_name") + " (" + document.get("fa_artist_birth_year") + ")");
				    System.out.println("Match Score: " + searchScore);
				    System.out.println("==============================================================");
				    
				    
				    consoleArea.append("=====================MATCHED==================================\n");
				    consoleArea.append("(CSV):" + artistName + " (" + artistBirthYear + ")\n");
				    consoleArea.append("(DB):" + document.get("fa_artist_name") + " (" + document.get("fa_artist_birth_year") + ")\n");
				    consoleArea.append("Match Score: " + searchScore + "\n");
				    consoleArea.append("==============================================================");
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				
				} else {
					csvBean.setArtist_ID("1"); // Missing Artist ID
					int docId = hits[0].doc;
				    document = searcher.doc(docId);
				    System.out.println("=====================NOT MATCHED==================================");
				    System.out.println("(CSV):" + artistName + " (" + artistBirthYear + ")");
				    System.out.println("(DB):" + document.get("fa_artist_name") + " (" + document.get("fa_artist_birth_year") + ")");
				    System.out.println("Match Score: " + searchScore);
				    System.out.println("==================================================================");
				    
				    consoleArea.append("=====================NOT MATCHED==================================\n");
				    consoleArea.append("(CSV):" + artistName + " (" + artistBirthYear + ")\n");
				    consoleArea.append("(DB):" + document.get("fa_artist_name") + " (" + document.get("fa_artist_birth_year") + ")\n");
				    consoleArea.append("Match Score: " + searchScore + "\n");
				    consoleArea.append("==================================================================");
				    consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				}
			} else {
				csvBean.setArtist_ID("1"); // Missing Artist ID
				System.out.println("=====================NO MATCH AT ALL==================================");
			    System.out.println("(CSV):" + artistName + " (" + artistBirthYear + ")");
			    System.out.println("==================================================================");
			    
			    consoleArea.append("=====================NO MATCH AT ALL==================================\n");
			    consoleArea.append("(CSV):" + artistName + " (" + artistBirthYear + ")\n");
			    consoleArea.append("==================================================================");
			    consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}
		} catch (NullPointerException e) {
			//e.printStackTrace();
			csvBean.setArtist_ID("1"); // Missing Artist ID
			csvBean.setUpload_report("Parse Exception in Lucene Search for Search Box Query");
			System.out.println("Parse Exception in Lucene Search for Search Box Query");
			consoleArea.append("Parse Exception in Lucene Search for Search Box Query");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} catch (Exception e) {
			//e.printStackTrace();
			csvBean.setArtist_ID("1"); // Missing Artist ID
			csvBean.setUpload_report(e.getMessage());
		}
		return csvBean;
	}
	
	@SuppressWarnings("deprecation")
	public boolean saleLotKeyExists(String key) {
		try {
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			Directory directory = FSDirectory.open(new File(saleLotsIndexPath));
			
			String [] fields = {"sale_lot_key"};
			Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("sale_lot_key:" + QueryParser.escape(key));
			
			int hitsPerPage = 1;
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			if(hits.length > 0 && hits[0].score > 8) {
				int docId = hits[0].doc;
				org.apache.lucene.document.Document document = searcher.doc(docId);
				return true;
			} else {
				int docId = hits[0].doc;
				org.apache.lucene.document.Document document = searcher.doc(docId);
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void initializeArtistsIndexPath() {
		String sql = "select artists_index_path from operations_team where username = '" + username + "'";
		
		artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getString("artists_index_path").toLowerCase();
				} else {
					return "";
				}
			}
		});
	}
	
	public void initializeSaleLotsIndexPath() {
		String sql = "select salelots_index_path from operations_team where username = '" + username + "'";
		
		saleLotsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getString("salelots_index_path").toLowerCase();
				} else {
					return "";
				}
			}
		});
	}
	
	public boolean checkLinkingAndDecideUpload(CsvBean csvBean) {
		csvBean = linkWithArtistSQLWay(csvBean);
		if(csvBean.getArtist_ID() == null) {// artist could not be linked
			return true;
		}
		uploadLot(csvBean);
		return false;
	}
	*/
}
