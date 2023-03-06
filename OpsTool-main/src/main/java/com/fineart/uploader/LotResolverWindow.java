/*package com.fineart.uploader;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.fineart.scraper.CsvBean;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

public class LotResolverWindow extends JFrame {

	private static final long serialVersionUID = 54387030702948677L;
	private JTable table;
	List<org.apache.lucene.document.Document> suggestionsList;
	String username;
	JdbcTemplate jdbcTemplate;
	JPanel mainPanel;
	JLabel lblExactArtistMatch;
	JScrollPane tableScrollPane;
	DefaultTableModel tableModel;
	ListSelectionModel cellSelectionModel;
	JLabel lblIncsv;
	static String linkedArtistID = "0";
	CsvBean csvBean = new CsvBean();
	private JTextField databaseArtistIdText;
	private JTextField databaseArtworkIdText;
	private JTextArea reportTextArea;
	private JLabel lblLotDeails;
	private JPanel panel;
	private JLabel lblDatabaseArtistId;
	private JButton btnSuggestLink;
	private JButton viewArtworkDetailsBtn;
	private JLabel lblDatabaseArtworkId;
	private JLabel lblDecideActionFor;
	private JPanel panel_1;
	private JButton btnNewButton;
	private JButton btnSkipThisLot;
	private JLabel lblLotUrl;
	private JTextField lotUrlText;
	private JButton btnOpenOriginalLot;
	private String csvPath;
	private String directoryPath;
	private List<CsvBean> csvLots;
	private String artistsIndexPath;
	private String artworkIndexPath;
	private List<CsvBean> unresolvedLots;
	
	public LotResolverWindow(String username, String csvPath, String directoryPath, JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.csvPath = csvPath;
		this.directoryPath = directoryPath;
		this.username = username;
		
		unresolvedLots = new ArrayList<>();
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we) { 
		        performWindowClosingTasks();
		    }
		});
		
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
		
		sql = "select artwork_index_path from operations_team where username = '" + username + "'";
		
		artworkIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getString("artwork_index_path").toLowerCase();
				} else {
					return "";
				}
			}
		});
		
		initGUI();
	}

	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(1050, 541));
			this.setSize(new Dimension(1050, 541));
			this.setTitle("Resolve Lot Issues...");
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
			
			mainPanel = new JPanel();
			mainPanel.setSize(new java.awt.Dimension(1024, 600));
			mainPanel.setPreferredSize(new Dimension(1009, 444));
			mainPanel.setLayout(null);
			mainPanel.setVisible(true);
			this.getContentPane().add(mainPanel);
			
			lblExactArtistMatch = new JLabel("Lots with issues:");
			lblExactArtistMatch.setBounds(new Rectangle(10, 10, 100, 20));
			lblExactArtistMatch.setBounds(10, 0, 891, 29);
			mainPanel.add(lblExactArtistMatch);
			lblExactArtistMatch.setFont(new Font("Tahoma", Font.PLAIN, 14));
			
			tableScrollPane = new JScrollPane();
			tableScrollPane.setBounds(20, 40, 975, 105);
			tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			mainPanel.add(tableScrollPane);
			
			tableModel = new DefaultTableModel();
			table = new JTable(tableModel) {
	            private static final long serialVersionUID = 1L;
				public boolean getScrollableTracksViewportWidth() {
	                return getPreferredSize().width < getParent().getWidth();
	            }
	        };
			table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			table.setModel(tableModel);
			table.setFont(new java.awt.Font("Segoe UI",0,12));
			tableScrollPane.add(table);
			tableScrollPane.setViewportView(table);
			
			cellSelectionModel = table.getSelectionModel();
		    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		    cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {	// table row selection
					tableRowSelectionChanged();
				}
			});
			
			lblIncsv = new JLabel("Issues with this lot:");
			lblIncsv.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblIncsv.setBounds(19, 156, 161, 29);
			mainPanel.add(lblIncsv);
			
			
			tableModel.addColumn("S.No.");
			tableModel.addColumn("Auction House");
			tableModel.addColumn("Auction Number");
			tableModel.addColumn("Start Date");
			tableModel.addColumn("Auction Name");
			tableModel.addColumn("Lot Number");
			tableModel.addColumn("Sublot Number");
			
			table.getColumn("S.No.").setPreferredWidth(5);
			table.getColumn("Auction House").setPreferredWidth(20);
			table.getColumn("Auction Number").setPreferredWidth(15);
			table.getColumn("Start Date").setPreferredWidth(15);
			table.getColumn("Auction Name").setPreferredWidth(40);
			table.getColumn("Lot Number").setPreferredWidth(10);
			table.getColumn("Sublot Number").setPreferredWidth(10);
			
			CsvToBean<CsvBean> csvToBean = new CsvToBean<>();
			ColumnPositionMappingStrategy<CsvBean> strategy = new ColumnPositionMappingStrategy<>();
			
			String[] csvFields = {"auction_house_name", "auction_location", "auction_num", "auction_start_date", "auction_end_date", "auction_name", "lot_num", "sublot_num", 
					"price_kind", "price_estimate_min", "price_estimate_max", "price_sold", "artist_name", "artist_birth", "artist_death", "artist_nationality", 
					"artwork_name", "artwork_year_identifier", "artwork_start_year", "artwork_end_year", "artwork_materials", "artwork_category", "artwork_markings", "artwork_edition"
					, "artwork_description", "artwork_measurements_height", "artwork_measurements_width", "artwork_measurements_depth", "artwork_size_notes", 
					"auction_measureunit", "artwork_condition_in", "artwork_provenance", "artwork_exhibited", "artwork_literature", "artwork_images1", 
					"artwork_images2", "artwork_images3", "artwork_images4", "artwork_images5", "image1_name", "image2_name", "image3_name", 
					"image4_name", "image5_name", "lot_origin_url", "artist_ID", "artwork_ID", "upload_report"};
			
			strategy.setColumnMapping(csvFields);
			strategy.setType(CsvBean.class);
			
			//CSVReader csvReader = new CSVReader(reader);
			
			CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath),"UTF-8"));
			
			
			csvLots = csvToBean.parse(strategy, csvReader);
			
			initTableRows();

			
			reportTextArea = new JTextArea();
			reportTextArea.setBounds(29, 196, 966, 55);
			mainPanel.add(reportTextArea);
			
			lblLotDeails = new JLabel("Lot Deails:");
			lblLotDeails.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblLotDeails.setBounds(20, 273, 85, 29);
			mainPanel.add(lblLotDeails);
			
			panel = new JPanel();
			panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			panel.setBounds(29, 313, 494, 176);
			mainPanel.add(panel);
			panel.setLayout(null);
			
			lblDatabaseArtistId = new JLabel("Database Artist ID:");
			lblDatabaseArtistId.setBounds(10, 17, 128, 23);
			panel.add(lblDatabaseArtistId);
			
			Color color = new Color(0,0,0);
			
			databaseArtistIdText = new JTextField();
			databaseArtistIdText.setEditable(false);
			databaseArtistIdText.setBounds(154, 11, 151, 34);
			panel.add(databaseArtistIdText);
			databaseArtistIdText.setFont(new Font("Tahoma", Font.PLAIN, 14));
			databaseArtistIdText.setColumns(10);
			databaseArtistIdText.setDisabledTextColor(color);
			
			btnSuggestLink = new JButton("Suggest & Link");
			btnSuggestLink.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnSuggestLinkActionPerformed();
				}
			});
			btnSuggestLink.setBounds(318, 11, 166, 34);
			panel.add(btnSuggestLink);
			
			viewArtworkDetailsBtn = new JButton("View Details & Link");
			viewArtworkDetailsBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//viewArtworkDetailsBtnActionPerformed();
				}
			});
			viewArtworkDetailsBtn.setBounds(318, 69, 166, 34);
			panel.add(viewArtworkDetailsBtn);
			viewArtworkDetailsBtn.setEnabled(false);
			
			databaseArtworkIdText = new JTextField();
			databaseArtworkIdText.setEditable(false);
			databaseArtworkIdText.setBounds(154, 69, 151, 34);
			panel.add(databaseArtworkIdText);
			databaseArtworkIdText.setFont(new Font("Tahoma", Font.PLAIN, 14));
			databaseArtworkIdText.setColumns(10);
			databaseArtworkIdText.setDisabledTextColor(color);
			
			lblDatabaseArtworkId = new JLabel("Database Artwork ID:");
			lblDatabaseArtworkId.setBounds(10, 75, 128, 23);
			panel.add(lblDatabaseArtworkId);
			
			lblLotUrl = new JLabel("Lot URL:");
			lblLotUrl.setBounds(10, 131, 48, 23);
			panel.add(lblLotUrl);
			
			lotUrlText = new JTextField();
			lotUrlText.setEditable(false);
			lotUrlText.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lotUrlText.setColumns(10);
			lotUrlText.setBounds(63, 125, 242, 34);
			lotUrlText.setDisabledTextColor(color);
			
			panel.add(lotUrlText);
			
			btnOpenOriginalLot = new JButton("Open Original Lot");
			btnOpenOriginalLot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnOpenOriginalLotActionPerformed();
				}
			});
			btnOpenOriginalLot.setBounds(318, 125, 166, 34);
			panel.add(btnOpenOriginalLot);
			
			lblDecideActionFor = new JLabel("Decide action for this lot:");
			lblDecideActionFor.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblDecideActionFor.setBounds(554, 273, 200, 29);
			mainPanel.add(lblDecideActionFor);
			
			panel_1 = new JPanel();
			panel_1.setLayout(null);
			panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			panel_1.setBounds(533, 313, 462, 176);
			mainPanel.add(panel_1);
			
			btnNewButton = new JButton("Upload This Lot");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					uploadDisputedLot();
				}
			});
			btnNewButton.setForeground(SystemColor.desktop);
			btnNewButton.setBackground(SystemColor.inactiveCaption);
			btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnNewButton.setBounds(10, 11, 442, 87);
			panel_1.add(btnNewButton);
			
			btnSkipThisLot = new JButton("Resolve Later");
			btnSkipThisLot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnSkipThisLotActionPerformed();
				}
			});
			btnSkipThisLot.setForeground(new Color(0, 0, 0));
			btnSkipThisLot.setBackground(new Color(255, 228, 225));
			btnSkipThisLot.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnSkipThisLot.setBounds(10, 109, 442, 56);
			panel_1.add(btnSkipThisLot);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public void initTableRows() {
		Iterator<CsvBean> csvData = csvLots.iterator();
		int counter = 0;
		while (csvData.hasNext()) {
			if(counter == 0) {
				csvData.next();
				counter++;
				continue;
			}
			CsvBean csvBean = csvData.next();
			tableModel.addRow(new Object[]{counter, csvBean.getAuction_house_name(), csvBean.getAuction_num(), csvBean.getAuction_start_date(), 
					csvBean.getAuction_name(), csvBean.getLot_num(), csvBean.getSublot_num()});
			counter++;
		}
	}

	protected void btnSkipThisLotActionPerformed() {
		int selectedRow = table.getSelectedRow() + 1; //because of on 0 index the csv column is there
		unresolvedLots.add(csvLots.get(selectedRow));
		csvLots.remove(selectedRow);
		((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
	}

	protected void uploadDisputedLot() {
		try {
			String uiArtistId = databaseArtistIdText.getText().trim();
			if(uiArtistId.trim().equals("null") || StringUtils.isEmpty(uiArtistId)) {
				JOptionPane.showMessageDialog(null,  "Cannot upload lot as Artist Id is not linked. Please resolve the Artist issue first.");
				return;
			}
			int selectedRow = table.getSelectedRow() + 1; //because of on 0 index the csv column is there
			List<CsvBean> csvLotsClone = new ArrayList<>();
			csvLotsClone.addAll(csvLots);
			CsvBean selectedCsvBean = csvLotsClone.get(selectedRow);
			String artistName = selectedCsvBean.getArtist_name();
			String artistBirth = selectedCsvBean.getArtist_birth();
			CsvLotsUploader csvUploader = new CsvLotsUploader(username, true, jdbcTemplate, "Chrome", false, false, false);
			int indexCounter = 0;
			for(int loopCounter = 1; loopCounter < csvLotsClone.size(); loopCounter++) {
				String loopArtistName = csvLotsClone.get(loopCounter).getArtist_name();
				String loopArtistBirth = csvLotsClone.get(loopCounter).getArtist_birth();
				if(loopArtistName.equals(artistName) && loopArtistBirth.equals(artistBirth)) {
					System.out.println("Found " + loopArtistName + " " + loopArtistBirth + " at row " + (loopCounter+1) );
					CsvBean loopCsvBean = csvLotsClone.get(loopCounter);
					loopCsvBean.setArtist_ID(uiArtistId);
					csvUploader.uploadLot(loopCsvBean);
					csvLots.remove(loopCounter - indexCounter);
					indexCounter++;
				}
			}
			int rowCount = ((DefaultTableModel)table.getModel()).getRowCount();
			for (int i = rowCount - 1; i >= 0; i--) {
				((DefaultTableModel)table.getModel()).removeRow(i);
			}
			initTableRows();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	protected void performWindowClosingTasks() {
		writeIssuesCsvFile(Paths.get(csvPath).getFileName().toString(), directoryPath);
		this.setVisible(false);
		this.dispose();
	}

	protected void btnOpenOriginalLotActionPerformed() {
		try {
			URI uri = new URI(lotUrlText.getText().trim());
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

	protected void tableRowSelectionChanged() {
		try {
			Iterator<CsvBean> csvData = csvLots.iterator(); 
			int selectedRow = table.getSelectedRow() + 1; //because of on 0 index the csv column is there
			int counter = 0;
			while (csvData.hasNext()) {
				if(counter == selectedRow) {
					CsvBean csvBean = csvData.next();
					reportTextArea.setText(csvBean.getUpload_report());
					databaseArtistIdText.setText(csvBean.getArtist_ID());
					databaseArtworkIdText.setText(csvBean.getArtwork_ID());
					lotUrlText.setText(csvBean.getLot_origin_url());
					
					if(!csvBean.getArtist_ID().trim().equals("null")) {//if artist is NOT already linked
						btnSuggestLink.setEnabled(false);
					} else {
						btnSuggestLink.setEnabled(true);
					}
					
					if(csvBean.getArtwork_ID().trim().equals("null")) {//if artwork is already linked
						viewArtworkDetailsBtn.setEnabled(false);
					} else {
						viewArtworkDetailsBtn.setEnabled(true);
					}
					
					break;
	        	} else {
	        		csvData.next();
	        		counter++;
	        	}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
	}

	protected void btnSuggestLinkActionPerformed() {
		try {
			int selectedRow = table.getSelectedRow() + 1; //because of on 0 index the csv column is there
			CsvBean csvBean = csvLots.get(selectedRow);
			List<org.apache.lucene.document.Document> suggestionsList = suggestArtistsFromLocalIndex(csvBean);
			ArtistsSuggesterWindow suggesterWindow = new ArtistsSuggesterWindow(this, csvBean, suggestionsList, artistsIndexPath, artworkIndexPath, username, jdbcTemplate);
			suggesterWindow.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void writeIssuesCsvFile(String csvFilename, String directoryPath) {
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
			
			csvLots.remove(0); //remove the column name first
			unresolvedLots.addAll(csvLots);
			
			for(CsvBean csvBean : unresolvedLots) {
				String auctionDetails = csvBean.getAuction_house_name() + "|" + csvBean.getAuction_location() + "|" + csvBean.getAuction_num() + "|" 
						+ csvBean.getAuction_start_date() + "|" + csvBean.getAuction_end_date() + "|" + csvBean.getAuction_name() 
						+ "|" + csvBean.getLot_num() + "|" +  csvBean.getSublot_num() + "|" + csvBean.getPrice_kind() 
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
	
	@SuppressWarnings("deprecation")
	public List<org.apache.lucene.document.Document> suggestArtistsFromLocalIndex(CsvBean csvBean) {
		List<org.apache.lucene.document.Document> suggestionsList = new ArrayList<>();
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
			
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			Directory directory = null;
			
			directory = FSDirectory.open(new File(artistsIndexPath));
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			int hitsPerPage = 20;
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			ScoreDoc[] hits = null;
			org.apache.lucene.document.Document document = null;
			
			String [] fields = {"fa_artist_name"};
			Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("fa_artist_name:" + QueryParser.escape(artistName));
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			if(hits.length > 1) { // this is the case when same artist name (multiple similar) with birth and death = 0
				for(int i=0; i<hits.length; ++i) {
				    int docId = hits[i].doc;
				    document = searcher.doc(docId);
				    System.out.println("Adding ID: " + document.get("fa_artist_ID") + ", Name: " + document.get("fa_artist_name") + ", Birth Year: " 
						    + document.get("fa_artist_birth_year") + ", Death Year: " + document.get("fa_artist_death_year"));
				    suggestionsList.add(document);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Parse Exception in Lucene Search for Search Box Query");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suggestionsList;
	}
	
	private String getLucenceSafeString(String input) {
		return input.replaceAll(":", "-").replaceAll("/", "-").replaceAll("\\?", "-").replaceAll("\\(", "-").replaceAll("\\)", "-");
	}

	public String getDatabaseArtistIdText() {
		return databaseArtistIdText.getText().trim();
	}

	public void setDatabaseArtistIdText(String databaseArtistIdText) {
		this.databaseArtistIdText.setText(databaseArtistIdText);
	}

	public String getDatabaseArtworkIdText() {
		return this.databaseArtworkIdText.getText().trim();
	}

	public void setDatabaseArtworkIdText(String databaseArtworkIdText) {
		this.databaseArtworkIdText.setText(databaseArtworkIdText);
	}
	
	public String getReportTextArea() {
		return reportTextArea.getText().trim();
	}

	public void setReportTextArea(String reportTextArea) {
		this.reportTextArea.setText(reportTextArea);
	}

	private String getSqlSafeString(String input) {
		return input.replaceAll("'", "\\\\'");
	}
	
	protected void viewArtworkDetailsBtnActionPerformed() {
	int selectedRow = table.getSelectedRow() + 1; //because of on 0 index the csv column is there
	CsvBean csvBean = csvLots.get(selectedRow);
	
	org.apache.lucene.document.Document document = getArtworkFromLocalIndex(csvBean);
	
	ArtworkResolverWindow frame = new ArtworkResolverWindow(this, csvBean, document, artworkIndexPath, username, jdbcTemplate);
	frame.setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	public Document getArtworkFromLocalIndex(CsvBean csvBean) {

		try {
			String artistId = csvBean.getArtist_ID();
			
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
			Document document = null;
			
			String [] fields = {"fa_artist_ID"};
	        Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("faa_artwork_ID:" + getDatabaseArtworkIdText());
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			
			if(hits.length >= 1) {
				int docId = hits[0].doc;
				float searchScore = hits[0].score;
				if(searchScore > 0.8) {
					document = searcher.doc(docId);
				    System.out.println("Found Artwork ID (DB): " + document.get("faa_artwork_ID") + ", Name: " + document.get("faa_artwork_title"));
				    return document;
				}
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Parse Exception in Lucene Search for Search Box Query");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String insertIntoArtworks(CsvBean csvBean, KeyHolder holder, String artworksQuery, boolean requiresReview) {
		String faa_artwork_ID;
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(artworksQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, getSqlSafeString(csvBean.getArtwork_name()));
				if(requiresReview) {
					ps.setInt(2, 1);
				} else {
					ps.setInt(2, 0);
				}
				ps.setString(3, getSqlSafeString(csvBean.getArtwork_description()));
				ps.setString(4, csvBean.getArtist_ID());
				ps.setString(5, csvBean.getArtwork_category());
				ps.setString(6, getSqlSafeString(csvBean.getArtwork_materials()));
				ps.setString(7, getSqlSafeString(csvBean.getArtwork_edition()));
				ps.setString(8, getSqlSafeString(csvBean.getArtwork_exhibited()));
				ps.setString(9, getSqlSafeString(csvBean.getArtwork_literature()));
				ps.setString(10, StringUtils.isEmpty(csvBean.getArtwork_measurements_height()) ? "0.00" : csvBean.getArtwork_measurements_height());
				ps.setString(11, StringUtils.isEmpty(csvBean.getArtwork_measurements_width()) ? "0.00" : csvBean.getArtwork_measurements_width());
				ps.setString(12, StringUtils.isEmpty(csvBean.getArtwork_measurements_depth()) ? "0.00" : csvBean.getArtwork_measurements_depth());
				ps.setString(13, csvBean.getAuction_measureunit());
				ps.setString(14, getSqlSafeString(csvBean.getArtwork_size_notes()));
				ps.setString(15, getSqlSafeString(csvBean.getArtwork_markings()));
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
		
		faa_artwork_ID = String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
		return faa_artwork_ID;
	}
}
*/