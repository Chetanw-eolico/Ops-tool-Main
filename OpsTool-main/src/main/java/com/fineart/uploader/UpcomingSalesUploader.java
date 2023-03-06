package com.fineart.uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.core.windows.BaseWindow;
import com.fineart.scraper.AuctionBean;
import com.jauntium.Browser;
import com.jauntium.Element;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

public class UpcomingSalesUploader extends BaseWindow implements UploaderInterface {

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
	static String linkedArtistID = "0";
	private List<AuctionBean> csvIssuesList;
	private String faac_auction_ID = "0";
	private String webDriver;
	Map<String, String>saleCodeAuctionIdMap;
	Map<String, String>auctionHouseAndIdMap;
	boolean showConsole;
	boolean uploadDataWithDownloadImages;
	boolean downloadImagesOnly;
	
	public UpcomingSalesUploader(String username, JdbcTemplate jdbcTemplate, String webDriver, boolean uploadDataWithDownloadImages, 
			boolean downloadImagesOnly, boolean showConsole) {
		super();
		this.username = username;
		this.jdbcTemplate = jdbcTemplate;
		this.webDriver = webDriver;
		this.showConsole = showConsole;
		this.uploadDataWithDownloadImages = uploadDataWithDownloadImages;
		this.downloadImagesOnly = downloadImagesOnly;
		loadUploadedSalesData();
		loadAuctionHouseAndIdMap();
	}

	@Override
	public void initUploading(String csvPath, String directoryPath) {
		try {
			this.csvPath = csvPath;
			this.csvIssuesList = new ArrayList<>();
			
			if(uploadDataWithDownloadImages || downloadImagesOnly) {
				if(webDriver.equals("Chrome")) {
					System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
				} else if(webDriver.equals("Firefox")) {
					System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
				} else if(webDriver.equals("Edge")) {
					System.setProperty("webdriver.edge.driver", "MicrosoftWebDriver.exe");
				}
				
				if(webDriver.equals("Chrome")) {
					this.browser = new Browser(new ChromeDriver());
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
	        worker.execute();	// execute the background thread
		} catch (Exception e) {
			e.printStackTrace();
			consoleArea.append("\n" + e.getMessage());
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}

	public void doUpload(String csvPath, String directoryPath) {
		try {
			
			CsvToBean<AuctionBean> csvToBean = new CsvToBean<>();
			ColumnPositionMappingStrategy<AuctionBean> strategy = new ColumnPositionMappingStrategy<>();
			
			String[] csvFields = {"fal_auction_ID", "faac_auction_sale_code", "faac_auction_start_date", "faac_auction_end_date", 
					"faac_auction_title", "cah_auction_house_name", "cah_auction_house_location", "faac_auction_lot_count", "missing_artists", 
					"faac_auction_published", "faac_auction_image", "image_source", "faac_auction_source"};
			
			strategy.setColumnMapping(csvFields);
			strategy.setType(AuctionBean.class);
			
			//NOTE: we are converting this back to UTF-8 when get data from fields
			CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath),"Windows-1252"));
			
			List<AuctionBean> fineArtList = csvToBean.parse(strategy, csvReader);
			
			Iterator<AuctionBean> csvData = fineArtList.iterator();
			
			String imagesDirectoryPath = directoryPath;
			//new File(imagesDirectoryPath); //create the directory
			
			int counter = 0;
			
			try {
				while (csvData.hasNext()) {
	            	if(!continueProcessing) {
						return;
					}
	            	if(counter == 0) {
	            		csvData.next();
	            		counter++;
	            		continue;
	            	}
	            	AuctionBean auctionBean = csvData.next();
	            	if(!downloadImagesOnly) {
	            		if(counter <= fineArtList.size()) {
		            		
	            			System.out.println("Processing Row " + counter + " ...");
			            	consoleArea.append("\nProcessing Row " + counter + " ...");
			            	consoleArea.append("\n");
			    			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			    			
			            	/*boolean isDisputed = isSaleAlreadyUploaded(auctionBean);
			            	if(isDisputed) {
			            		System.out.println("Cannot upload same calendar again. Sale already uploaded " + counter + " ...");
				            	consoleArea.append("\nCannot upload same calendar again. Sale already uploaded " + counter + " ...");
				            	consoleArea.append("\n");
				    			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			            		csvIssuesList.add(auctionBean);
			            	}*/
			    			
			    			uploadSale(auctionBean);
			    			
			            	if(uploadDataWithDownloadImages) {
			            		doDownloadImages(imagesDirectoryPath, auctionBean);
			            	}
			            	
			            	counter++;
		            	}
	            	} else {
	            		doDownloadImages(imagesDirectoryPath, auctionBean);
	            		counter++;
	            	}
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//For all disputed sales, write a new CSV for the information
            if(csvIssuesList.size() > 0) { 
            	writeIssuesCsvFile("unresolved-sales-" + Paths.get(csvPath).getFileName().toString(), directoryPath, csvIssuesList);
            }
            
            System.out.println("Sale Uploading And Images Downloading Done!");
        	consoleArea.append("Sale Uploading And Images Downloading Done!");
        	consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			
			JOptionPane.showMessageDialog(null, "Sale Uploading And Images Downloading Done!", "Upcoming Sale Uploader", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			this.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public boolean isSaleAlreadyUploaded(AuctionBean auctionBean) {
		if(saleCodeAuctionIdMap.containsKey(auctionBean.getFaac_auction_sale_code().trim())) {
			return true;
		} else {
			uploadSale(auctionBean);
			return false;
		}
	}*/
	
	public void uploadSale(AuctionBean auctionBean) {
		KeyHolder holder = null;
		if(StringUtils.isEmpty(auctionBean.getFal_auction_ID().trim()) && StringUtils.isEmpty(auctionBean.getFaac_auction_start_date())) {
			csvIssuesList.add(auctionBean);
			return;
		}
		if(StringUtils.isEmpty(auctionBean.getFal_auction_ID().trim()) && StringUtils.isEmpty(auctionBean.getFaac_auction_title())) {
			csvIssuesList.add(auctionBean);
			return;
		}
		if(StringUtils.isEmpty(auctionBean.getFal_auction_ID().trim()) && StringUtils.isEmpty(auctionBean.getCah_auction_house_name())) {
			csvIssuesList.add(auctionBean);
			return;
		}
		try {
			
			if(StringUtils.isEmpty(auctionBean.getFaac_auction_image()) && StringUtils.isNotEmpty(auctionBean.getFaac_auction_source())) {
				String imageName = "";
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_title())) {
					imageName = auctionBean.getFaac_auction_title() + "-" + System.currentTimeMillis();
				} else {
					imageName = "image-" + System.currentTimeMillis();
				}
				imageName = imageName.replaceAll("[^\\w]","-"); //replace all special characters with hyphen
				imageName = imageName + ".jpg";
				auctionBean.setFaac_auction_image(imageName);
			} else if(StringUtils.isEmpty(auctionBean.getFaac_auction_image()) && StringUtils.isNotEmpty(auctionBean.getImage_source())) {
				String imageName = "";
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_title())) {
					imageName = auctionBean.getFaac_auction_title() + "-" + System.currentTimeMillis();
				} else {
					imageName = "image-" + System.currentTimeMillis();
				}
				imageName = imageName.replaceAll("[^\\w]","-"); //replace all special characters with hyphen
				imageName = imageName + ".jpg";
				auctionBean.setFaac_auction_image(imageName);
			}
			
			String auctionStartDate = "0000-00-00";
			String auctionEndDate = "0000-00-00";
			SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy");
			
			try {
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_start_date())) {
					Date auctionStartDateDate = f.parse(auctionBean.getFaac_auction_start_date());
					auctionStartDate = new SimpleDateFormat("yyyy-MM-dd").format(auctionStartDateDate);
				}
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_end_date())) {
					Date auctionEndDateDate = f.parse(auctionBean.getFaac_auction_end_date());
					auctionEndDate = new SimpleDateFormat("yyyy-MM-dd").format(auctionEndDateDate);
				}
			} catch (java.text.ParseException e) {
				csvIssuesList.add(auctionBean);
				System.out.println("Date format should be in dd-MMM-yy format. Found start date: " + auctionBean.getFaac_auction_start_date());
				consoleArea.append("\n" + "Date format should be in dd-MMM-yy format. Found start date: " + auctionBean.getFaac_auction_start_date());
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				return;
			}
			
			final String auctionStartDateFinal = auctionStartDate;
			final String auctionEndDateFinal = auctionEndDate;
			
			if(StringUtils.isEmpty(auctionBean.getFal_auction_ID().trim())) {	//auction id not provided
				
				//check if SaleTitle-SaleDate-AuctionHouseName exists, throw error, key exists
				// ELSE insert
				
				if(StringUtils.isEmpty(auctionBean.getCah_auction_house_location())) {
					auctionBean.setCah_auction_house_location("Missing");
				}
				
				if(StringUtils.isEmpty(auctionBean.getFaac_auction_sale_code())) {
					auctionBean.setFaac_auction_sale_code("na");
				}
				
				if(StringUtils.isEmpty(auctionBean.getFaac_auction_source())) {
					auctionBean.setFaac_auction_source("");
				}
				
				String checkKeySql = "SELECT count(*) as record_count FROM fineart_auction_calendar "
						+ "INNER JOIN core_auction_houses ON fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID "
						+ "WHERE faac_auction_title = '" + FineartUtils.get_UTF8_String(auctionBean.getFaac_auction_title()) + "' AND faac_auction_start_date = '" 
						+ auctionStartDate + "' " + "AND cah_auction_house_name = '" + FineartUtils.getSqlSafeString(FineartUtils.get_UTF8_String(auctionBean.getCah_auction_house_name())) + "' LIMIT 1";
				
				Integer recordCount = (Integer)jdbcTemplate.queryForObject(checkKeySql, Integer.class);
				
				if(recordCount == 0) {	//key does not exist
					
					////INSERT auction_calendar
					
					String cah_auction_house_ID = auctionHouseAndIdMap.get(auctionBean.getCah_auction_house_name().trim() + "-" + auctionBean.getCah_auction_house_location().trim());
					
					String auctionCalSql = "insert into fineart_auction_calendar (faac_auction_sale_code, faac_auction_house_ID, faac_auction_title, "
							+ "faac_auction_start_date, faac_auction_end_date, faac_auction_image, faac_auction_source, faac_auction_record_created, faac_auction_record_updated, faac_auction_record_createdby, faac_auction_record_updatedby)"
							+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
					
					holder = new GeneratedKeyHolder();
					
					try {
						jdbcTemplate.update(new PreparedStatementCreator() {
							@Override
							public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
								PreparedStatement ps = con.prepareStatement(auctionCalSql, Statement.RETURN_GENERATED_KEYS);
								ps.setString(1, auctionBean.getFaac_auction_sale_code().trim());
								ps.setString(2, cah_auction_house_ID);
								ps.setString(3, FineartUtils.getUrlSafeString(FineartUtils.get_UTF8_String(auctionBean.getFaac_auction_title())));
								ps.setString(4, auctionStartDateFinal);
								if(StringUtils.isEmpty(auctionBean.getFaac_auction_end_date())) {
									ps.setString(5, "0000-00-00");
								} else {
									ps.setString(5, auctionEndDateFinal);
								}
								ps.setString(6, auctionBean.getFaac_auction_image());
								ps.setString(7, auctionBean.getFaac_auction_source());
								ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
								ps.setString(9, null);
								ps.setString(10, username);
								ps.setString(11, null);
										
								return ps;
							}
						}, holder);
					} catch (DuplicateKeyException e) {
						System.out.println("In exception state. Cannot upload the same sale again.###");
						csvIssuesList.add(auctionBean);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						csvIssuesList.add(auctionBean);
						return;
					}
					
					this.faac_auction_ID = String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
					saleCodeAuctionIdMap.put(auctionBean.getFaac_auction_sale_code().trim(), faac_auction_ID);
					
					System.out.println("Uploaded Calendar with generated id:" + faac_auction_ID);
					consoleArea.append("\n" + "Calendar Sale with generated id:" + faac_auction_ID);
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
					if(!showConsole) {
						closeWindow();
					}
				} else { // Key Code exists and Auction ID is not provided.
					System.out.println(auctionBean.getFaac_auction_title() + "-" + auctionBean.getFaac_auction_start_date() + "-" + auctionBean.getCah_auction_house_name() 
					+ " already exists in calendar. Please provide auction id to update. Skipping now...");
					csvIssuesList.add(auctionBean);
					consoleArea.append("\n" + auctionBean.getFaac_auction_title() + "-" + auctionBean.getFaac_auction_start_date() + "-" + auctionBean.getCah_auction_house_name() 
					+ " already exists in calendar. Please provide auction id to update. Skipping now...");
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				}
			} else {	//auction id provided
				
				//UPDATE auction_calendar with every column data
				
				StringBuffer sb = new StringBuffer("UPDATE fineart_auction_calendar SET ");
				
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_sale_code().trim())) {
					sb.append("faac_auction_sale_code = ' " + auctionBean.getFaac_auction_sale_code().trim() + "', ");
				}
				
				if(StringUtils.isNotEmpty(auctionBean.getCah_auction_house_name()) && StringUtils.isNotEmpty(auctionBean.getCah_auction_house_location())) {
					String cah_auction_house_ID = auctionHouseAndIdMap.get(auctionBean.getCah_auction_house_name() + "-" + auctionBean.getCah_auction_house_location());
					sb.append("faac_auction_house_ID = '" + cah_auction_house_ID + "', ");
				}
				
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_title())) {
					sb.append("faac_auction_title = '" + auctionBean.getFaac_auction_title() + "', ");
				}
				
				if(!auctionStartDateFinal.equals("0000-00-00")) {
					sb.append("faac_auction_start_date = '" + auctionStartDateFinal + "', ");
				}
				
				if(!auctionEndDateFinal.equals("0000-00-00")) {
					sb.append("faac_auction_end_date = '" + auctionEndDateFinal + "', ");
				}
				
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_image())) {
					sb.append("faac_auction_image = '" + auctionBean.getFaac_auction_image() + "', ");
				}
				
				if(StringUtils.isNotEmpty(auctionBean.getFaac_auction_source())) {
					sb.append("faac_auction_source = '" + auctionBean.getFaac_auction_source() + "', ");
				}
				
				sb.append(" faac_auction_record_updated = CURRENT_TIMESTAMP(), faac_auction_record_updatedby = '" + username + "'");
				
				String queryPart1 = sb.toString().trim();
				
				String queryPart2 =  " WHERE faac_auction_ID = " + auctionBean.getFal_auction_ID();
				
				String auctionCalSql = queryPart1 + queryPart2;
				
				jdbcTemplate.update(auctionCalSql);
				
				/*String auctionCalSql = "UPDATE fineart_auction_calendar SET faac_auction_sale_code = ?, faac_auction_house_ID = ?, faac_auction_title = ?, "
						+ "faac_auction_start_date = ?, faac_auction_end_date = ?, faac_auction_image = ?, faac_auction_source = ?, faac_auction_record_updated = ?, "
						+ "faac_auction_record_updatedby = ? WHERE faac_auction_ID = ?";*/
			}
			
			System.out.println("Updated Calendar with auction id:" + auctionBean.getFal_auction_ID());
			consoleArea.append("\n" + "Updated Calendar with auction id:" + auctionBean.getFal_auction_ID());
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
	}

	private void writeIssuesCsvFile(String csvFilename, String directoryPath, List<AuctionBean> csvIssuesList) {
		try {
			CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(directoryPath + "\\" + csvFilename), "UTF-8"));
			
			StringBuilder stats = new StringBuilder();
			stats.append("fal_auction_ID|faac_auction_sale_code|faac_auction_start_date|faac_auction_end_date|faac_auction_title"
					+ "|cah_auction_house_name|cah_auction_house_location|faac_auction_lot_count|missing_artists|faac_auction_published|faac_auction_image"
					+ "|image_source|faac_auction_source");
			
			csvWriter.writeNext(stats.toString().split("\\|"));
			
			for(AuctionBean auctionBean : csvIssuesList) {
				String auctionDetails = "" + "|" 
						+ auctionBean.getFaac_auction_sale_code().trim() + "|"
						+ auctionBean.getFaac_auction_start_date() + "|" 
						+ auctionBean.getFaac_auction_end_date() + "|" 
						+ auctionBean.getFaac_auction_title()  + "|" 
						+ auctionBean.getCah_auction_house_name() + "|" 
						+ auctionBean.getCah_auction_house_location() + "|" 
						+ "0" + "|" + "0" + "|"
						+ "no" + "|"
						+ auctionBean.getFaac_auction_image() + "|" + auctionBean.getImage_source() + "|" + auctionBean.getFaac_auction_source();

				String row[] = auctionDetails.split("\\|");
				csvWriter.writeNext(row);
			}
			csvWriter.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public void doDownloadImages(String directoryPath, AuctionBean auctionBean) {
		if(StringUtils.isNotEmpty(auctionBean.getImage_source())) {
			downloadImage(auctionBean.getFaac_auction_image(), auctionBean.getImage_source(), directoryPath);
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
			
			File file = new File(directoryPath + "/" + "images" + "/" + fileName);
			file.getParentFile().mkdirs();
		    browser.download(url, file); 
			
		} catch (Exception e) {
			System.out.println("Error in downloading image: " + e.getMessage());
        	consoleArea.append("\nError in downloading image: " + e.getMessage());
        	consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}
	
	public void loadUploadedSalesData() {
		
	saleCodeAuctionIdMap = new HashMap<>();
	String auctionCalSql = "SELECT fineart_auction_calendar.faac_auction_ID, fineart_auction_calendar.faac_auction_sale_code "
			+ "FROM fineart_auction_calendar";
			
			jdbcTemplate.query(auctionCalSql, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					while (resultSet.next()) {
						saleCodeAuctionIdMap.put(resultSet.getString("faac_auction_sale_code"), resultSet.getString("faac_auction_ID"));
					}
					return true;
				}
			});
	}
	
	private void loadAuctionHouseAndIdMap() {
		////get auction house id
		auctionHouseAndIdMap = new HashMap<>();
		String auctionHouseSql = "select * from core_auction_houses";
		
		jdbcTemplate.query(auctionHouseSql, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				while (resultSet.next()) {
					String auctionHouseNameLocationKey = resultSet.getString("cah_auction_house_name").trim() + "-" + resultSet.getString("cah_auction_house_location").trim();
					auctionHouseAndIdMap.put(auctionHouseNameLocationKey, resultSet.getString("cah_auction_house_ID"));
				}
				return true;
			}
		});
	}
}
