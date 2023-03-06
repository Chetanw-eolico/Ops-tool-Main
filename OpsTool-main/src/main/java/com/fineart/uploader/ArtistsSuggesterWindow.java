/*package com.fineart.uploader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fineart.scraper.CsvBean;

public class ArtistsSuggesterWindow extends JFrame{

	private static final long serialVersionUID = 3416464135553827999L;
	private JTable table;
	List<org.apache.lucene.document.Document> suggestionsList;
	String username;
	JdbcTemplate jdbcTemplate;
	private JTextField artistNameCsv;
	private JTextField artistDobCsv;
	private JTextField artistDodCsv;
	private JTextField artistNationalityCsv;
	private JTextField artistNameDb;
	private JTextField artistDobDb;
	private JTextField artistDodDb;
	private JTextField artistNationalityDb;
	private JTextField artistNamePrefixDb;
	private JTextField artistNameSuffixDb;
	private JTextField artistBIDb;
	private JTextField artistDiDb;
	private JTextField artistDecriptionDb;
	private JTextField artistIdDb;
	JButton btnAddNewArtist;
	JButton btnUpdateArtist;
	JPanel mainPanel;
	JLabel lblExactArtistMatch;
	JScrollPane tableScrollPane;
	DefaultTableModel tableModel;
	ListSelectionModel cellSelectionModel;
	JLabel lblIncsv;
	JPanel csvPanel;
	JLabel lblArtistName;
	JLabel lblDateOfBirth;
	private JLabel lblDateOfDeath;
	private JLabel lblNationality;
	private JPanel dbPanel;
	private JLabel label_4;
	private JLabel lblDateOfBirth_1;
	private JLabel lblDateOfDeath_1;
	private JLabel lblNationality_1;
	private JLabel lblNamePrefix;
	private JLabel lblNameSuffix;
	private JLabel lblBirthIdentifier;
	private JLabel lblDeathIdentifier;
	private JLabel lblArtistDescription;
	private JLabel lblArtistId;
	private JLabel lblInDatabaseyour;
	private JSeparator separator;
	static int tableSelectedRow = 0;
	CsvBean csvBean = new CsvBean();
	private JLabel lblArtistAka;
	private JTextField artistAkaDb;
	JTextField databaseArtistIdText;
	private JButton btnLinkWithThis;
	private String artistsIndexPath;
	private String artworkIndexPath;
	
	public ArtistsSuggesterWindow(LotResolverWindow resolveLotIssuesWindow, CsvBean csvBean, 
			List<org.apache.lucene.document.Document> suggestionsList, String artistsIndexPath, String artworkIndexPath, String username, JdbcTemplate jdbcTemplate) {
		this.suggestionsList = suggestionsList;
		this.jdbcTemplate = jdbcTemplate;
		this.csvBean = csvBean;
		this.artistsIndexPath = artistsIndexPath;
		this.artworkIndexPath = artworkIndexPath;
		this.username = username;
		
		initGUI(databaseArtistIdText, resolveLotIssuesWindow);
	}

	*//**
	 * Initialize the contents of the frame.
	 *//*
	private void initGUI(JTextField databaseArtistIdText, LotResolverWindow resolveLotIssuesWindow) {
		this.setPreferredSize(new java.awt.Dimension(1050, 641));
		this.setSize(new Dimension(1050, 641));
		this.setTitle("Resolve Artist Issues...");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		mainPanel = new JPanel();
		mainPanel.setSize(new java.awt.Dimension(1024, 600));
		mainPanel.setPreferredSize(new java.awt.Dimension(1024, 600));
		mainPanel.setLayout(null);
		mainPanel.setVisible(true);
		this.getContentPane().add(mainPanel);
		
		lblExactArtistMatch = new JLabel("Exact artist match could not be found. Below are the closest 20 matches, you may link with an existing artist or add a new -");
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
				tableRowSelectionChanged(suggestionsList);
			}
		});
		
		lblIncsv = new JLabel("In Csv File:");
		lblIncsv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIncsv.setBounds(19, 156, 85, 29);
		mainPanel.add(lblIncsv);
		
		csvPanel = new JPanel();
		csvPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		csvPanel.setBounds(20, 196, 368, 150);
		mainPanel.add(csvPanel);
		csvPanel.setLayout(null);
		
		lblArtistName = new JLabel("Artist Name:");
		lblArtistName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblArtistName.setBounds(8, 8, 80, 24);
		csvPanel.add(lblArtistName);
		
		Color color = new Color(0, 0, 0);
		
		artistNameCsv = new JTextField();
		//artistNameCsv.setEnabled(false);
		artistNameCsv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistNameCsv.setBounds(111, 10, 247, 22);
		csvPanel.add(artistNameCsv);
		artistNameCsv.setColumns(10);
		artistNameCsv.setText(csvBean.getArtist_name());
		artistNameCsv.setDisabledTextColor(color);
		
		artistDobCsv = new JTextField();
		//artistDobCsv.setEnabled(false);
		artistDobCsv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistDobCsv.setColumns(10);
		artistDobCsv.setBounds(111, 45, 247, 22);
		csvPanel.add(artistDobCsv);
		artistDobCsv.setText(csvBean.getArtist_birth());
		artistDobCsv.setDisabledTextColor(color);
		
		lblDateOfBirth = new JLabel("Date of Birth:");
		lblDateOfBirth.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDateOfBirth.setBounds(8, 43, 80, 24);
		csvPanel.add(lblDateOfBirth);
		
		artistDodCsv = new JTextField();
		//artistDodCsv.setEnabled(false);
		artistDodCsv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistDodCsv.setColumns(10);
		artistDodCsv.setBounds(111, 80, 247, 22);
		csvPanel.add(artistDodCsv);
		artistDodCsv.setText(csvBean.getArtist_death());
		artistDodCsv.setDisabledTextColor(color);
		
		lblDateOfDeath = new JLabel("Date of Death:");
		lblDateOfDeath.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDateOfDeath.setBounds(8, 78, 92, 24);
		csvPanel.add(lblDateOfDeath);
		
		artistNationalityCsv = new JTextField();
		//artistNationalityCsv.setEnabled(false);
		artistNationalityCsv.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistNationalityCsv.setColumns(10);
		artistNationalityCsv.setBounds(111, 115, 247, 22);
		csvPanel.add(artistNationalityCsv);
		artistNationalityCsv.setText(csvBean.getArtist_nationality());
		artistNationalityCsv.setDisabledTextColor(color);
		
		lblNationality = new JLabel("Nationality:");
		lblNationality.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNationality.setBounds(8, 113, 80, 24);
		csvPanel.add(lblNationality);
		
		dbPanel = new JPanel();
		dbPanel.setLayout(null);
		dbPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		dbPanel.setBounds(423, 196, 385, 389);
		mainPanel.add(dbPanel);
		
		label_4 = new JLabel("Artist Name:");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_4.setBounds(8, 8, 80, 24);
		dbPanel.add(label_4);
		
		artistNameDb = new JTextField();
		//artistNameDb.setEnabled(false);
		artistNameDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistNameDb.setBounds(112, 10, 263, 22);
		dbPanel.add(artistNameDb);
		if(suggestionsList.size() > 0) {
			artistNameDb.setText(suggestionsList.get(0).get("fa_artist_name"));
		}
		
		artistNameDb.setDisabledTextColor(color);
		
		artistDobDb = new JTextField();
		//artistDobDb.setEnabled(false);
		artistDobDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistDobDb.setColumns(10);
		artistDobDb.setBounds(112, 45, 263, 22);
		dbPanel.add(artistDobDb);
		if(suggestionsList.size() > 0) {
			artistDobDb.setText(suggestionsList.get(0).get("fa_artist_birth_year"));
		}
		
		artistDobDb.setDisabledTextColor(color);
		
		lblDateOfBirth_1 = new JLabel("Date of Birth:");
		lblDateOfBirth_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDateOfBirth_1.setBounds(8, 43, 80, 24);
		dbPanel.add(lblDateOfBirth_1);
		
		artistDodDb = new JTextField();
		//artistDodDb.setEnabled(false);
		artistDodDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistDodDb.setColumns(10);
		artistDodDb.setBounds(112, 80, 263, 22);
		dbPanel.add(artistDodDb);
		if(suggestionsList.size() > 0) {
			artistDodDb.setText(suggestionsList.get(0).get("fa_artist_death_year"));
		}
		
		artistDodDb.setDisabledTextColor(color);
		
		lblDateOfDeath_1 = new JLabel("Date of Death:");
		lblDateOfDeath_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDateOfDeath_1.setBounds(8, 78, 91, 24);
		dbPanel.add(lblDateOfDeath_1);
		
		artistNationalityDb = new JTextField();
		//artistNationalityDb.setEnabled(false);
		artistNationalityDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistNationalityDb.setColumns(10);
		artistNationalityDb.setBounds(112, 115, 263, 22);
		dbPanel.add(artistNationalityDb);
		if(suggestionsList.size() > 0) {
			artistNationalityDb.setText(suggestionsList.get(0).get("fa_artist_nationality"));
		}
		
		artistNationalityDb.setDisabledTextColor(color);
		
		lblNationality_1 = new JLabel("Nationality:");
		lblNationality_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNationality_1.setBounds(8, 113, 80, 24);
		dbPanel.add(lblNationality_1);
		
		artistNamePrefixDb = new JTextField();
		//artistNamePrefixDb.setEnabled(false);
		artistNamePrefixDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistNamePrefixDb.setColumns(10);
		artistNamePrefixDb.setBounds(112, 150, 263, 22);
		dbPanel.add(artistNamePrefixDb);
		if(suggestionsList.size() > 0) {
			artistNamePrefixDb.setText(suggestionsList.get(0).get("fa_artist_name_prefix"));
		}
		
		artistNamePrefixDb.setDisabledTextColor(color);
		
		lblNamePrefix = new JLabel("Name Prefix:");
		lblNamePrefix.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNamePrefix.setBounds(8, 148, 91, 24);
		dbPanel.add(lblNamePrefix);
		
		artistNameSuffixDb = new JTextField();
		//artistNameSuffixDb.setEnabled(false);
		artistNameSuffixDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistNameSuffixDb.setColumns(10);
		artistNameSuffixDb.setBounds(112, 185, 263, 22);
		dbPanel.add(artistNameSuffixDb);
		if(suggestionsList.size() > 0) {
			artistNameSuffixDb.setText(suggestionsList.get(0).get("fa_artist_name_suffix"));
		}
		
		artistNameSuffixDb.setDisabledTextColor(color);
		
		lblNameSuffix = new JLabel("Name Suffix:");
		lblNameSuffix.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNameSuffix.setBounds(8, 183, 91, 24);
		dbPanel.add(lblNameSuffix);
		
		lblBirthIdentifier = new JLabel("Birth Identifier:");
		lblBirthIdentifier.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBirthIdentifier.setBounds(8, 218, 105, 24);
		dbPanel.add(lblBirthIdentifier);
		
		
		artistBIDb = new JTextField();
		//artistBIDb.setEnabled(false);
		artistBIDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistBIDb.setBounds(112, 220, 263, 22);
		dbPanel.add(artistBIDb);
		if(suggestionsList.size() > 0) {
			artistBIDb.setText(suggestionsList.get(0).get("fa_artist_birth_year_identifier"));
		}
		
		artistBIDb.setDisabledTextColor(color);
		
		lblDeathIdentifier = new JLabel("Death Identifier:");
		lblDeathIdentifier.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDeathIdentifier.setBounds(8, 253, 105, 24);
		dbPanel.add(lblDeathIdentifier);
		
		artistDiDb = new JTextField();
		//artistDiDb.setEnabled(false);
		artistDiDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistDiDb.setColumns(10);
		artistDiDb.setBounds(112, 255, 263, 22);
		dbPanel.add(artistDiDb);
		if(suggestionsList.size() > 0) {
			artistDiDb.setText(suggestionsList.get(0).get("fa_artist_death_year_identifier"));
		}
		
		artistDiDb.setDisabledTextColor(color);
		
		lblArtistDescription = new JLabel("Artist Description:");
		lblArtistDescription.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblArtistDescription.setBounds(8, 288, 105, 24);
		dbPanel.add(lblArtistDescription);
		
		artistDecriptionDb = new JTextField();
		//artistDecriptionDb.setEnabled(false);
		artistDecriptionDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistDecriptionDb.setColumns(10);
		artistDecriptionDb.setBounds(112, 290, 263, 22);
		dbPanel.add(artistDecriptionDb);
		if(suggestionsList.size() > 0) {
			artistDecriptionDb.setText(suggestionsList.get(0).get("fa_artist_description"));
		}
		
		artistDecriptionDb.setDisabledTextColor(color);
		
		lblArtistId = new JLabel("Artist Id:");
		lblArtistId.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblArtistId.setBounds(8, 354, 105, 24);
		dbPanel.add(lblArtistId);
		
		artistIdDb = new JTextField();
		artistIdDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistIdDb.setEditable(false);
		artistIdDb.setColumns(10);
		artistIdDb.setBounds(112, 356, 263, 22);
		dbPanel.add(artistIdDb);
		if(suggestionsList.size() > 0) {
			artistIdDb.setText(suggestionsList.get(0).get("fa_artist_ID"));
		}
		
		artistIdDb.setDisabledTextColor(color);
		
		lblArtistAka = new JLabel("Artist Aka:");
		lblArtistAka.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblArtistAka.setBounds(8, 319, 91, 24);
		dbPanel.add(lblArtistAka);
		
		artistAkaDb = new JTextField();
		artistAkaDb.setText((String) null);
		artistAkaDb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		artistAkaDb.setDisabledTextColor(Color.BLACK);
		artistAkaDb.setColumns(10);
		artistAkaDb.setBounds(112, 323, 263, 22);
		if(suggestionsList.size() > 0) {
			artistAkaDb.setText(suggestionsList.get(0).get("fa_artist_aka"));
		}
		
		dbPanel.add(artistAkaDb);
		
			
		
		lblInDatabaseyour = new JLabel("In Database (Your selection):");
		lblInDatabaseyour.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInDatabaseyour.setBounds(423, 156, 265, 29);
		mainPanel.add(lblInDatabaseyour);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(405, 196, 2, 369);
		mainPanel.add(separator);
		
		btnAddNewArtist = new JButton("Add New Artist");
		btnAddNewArtist.setBackground(new Color(240, 255, 255));
		btnAddNewArtist.setBounds(818, 225, 177, 82);
		mainPanel.add(btnAddNewArtist);
		btnAddNewArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddNewArtistActionPerformed();
			}
		});
		btnAddNewArtist.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnUpdateArtist = new JButton("Update This Artist");
		btnUpdateArtist.setBackground(new Color(255, 222, 173));
		btnUpdateArtist.setBounds(818, 347, 177, 82);
		mainPanel.add(btnUpdateArtist);
		
		btnUpdateArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpdateArtistActionPerformed();
			}
		});
		btnUpdateArtist.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		btnLinkWithThis = new JButton("Link With This Artist");
		btnLinkWithThis.setBackground(SystemColor.inactiveCaption);
		btnLinkWithThis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLinkWithThisActionPerformed(resolveLotIssuesWindow);
			}
		});
		btnLinkWithThis.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLinkWithThis.setBounds(818, 483, 177, 82);
		mainPanel.add(btnLinkWithThis);
		
		tableModel.addColumn("Artist Name");
		tableModel.addColumn("Birth Year");
		tableModel.addColumn("Death Year");
		tableModel.addColumn("Name Prefix");
		tableModel.addColumn("Name Suffix");
		tableModel.addColumn("Artist Id");
		
		for(Document doc : suggestionsList) {
			tableModel.addRow(new Object[]{doc.get("fa_artist_name"), doc.get("fa_artist_birth_year"), doc.get("fa_artist_death_year"), 
					doc.get("fa_artist_name_prefix"), doc.get("fa_artist_name_suffix"), doc.get("fa_artist_ID")});
		}
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_L && e.isControlDown()) {
						btnLinkLotWithActionPerformed();
					} else if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
						btnAddNewArtistActionPerformed();
					} else if (e.getKeyCode() == KeyEvent.VK_U && e.isControlDown()) {
						btnUpdateArtistActionPerformed();
					} else if ((e.getKeyCode() == KeyEvent.VK_L || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_U) && e.isAltDown()) {
						resetActionButtons();
					}
				}
				return false;
			}
		});
	}
	
	protected void btnLinkWithThisActionPerformed(LotResolverWindow resolveLotIssuesWindow) {
		resolveLotIssuesWindow.setDatabaseArtistIdText(artistIdDb.getText().trim());
		csvBean.setArtist_ID(artistIdDb.getText().trim());
		//linkWithArtwork(csvBean, resolveLotIssuesWindow);
		resolveLotIssuesWindow.setReportTextArea("");
		this.setVisible(false);
		this.dispose();
	}
	
	

	@SuppressWarnings("deprecation")
	public void btnAddNewArtistActionPerformed() {
		IndexWriter writer = null;
		try {
			String query = "insert into fineart_artists (fa_artist_name, fa_artist_name_prefix, fa_artist_name_suffix, fa_artist_birth_year, fa_artist_death_year, "
					+ "fa_artist_birth_year_identifier, fa_artist_birth_year_precision, fa_artist_death_year_identifier, fa_artist_death_year_precision, "
					+ "fa_artist_aka, fa_artist_nationality, fa_artist_description, fa_artist_record_created, fa_artist_record_updated, fa_arist_record_createdby, "
					+ "fa_artist_record_updatedby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			String artistName = artistNameDb.getText().trim().trim().equals("") ? "na" : artistNameDb.getText().trim();
			String artistNamePrefix = artistNamePrefixDb.getText().trim().equals("") ? "na" : artistNamePrefixDb.getText().trim();
			String artistNameSuffix = artistNameSuffixDb.getText().trim().equals("") ? "na" : artistNameSuffixDb.getText().trim();
			String artistDob = artistDobDb.getText().trim().equals("") ? "0" : artistDobDb.getText().trim();
			String artistDod = artistDodDb.getText().trim().equals("") ? "0" : artistDodDb.getText().trim();
			String artistBI = artistBIDb.getText().trim().equals("") ? "exact" : artistBIDb.getText().trim();
			String artistDi = artistDiDb.getText().trim().equals("") ? "exact" : artistDiDb.getText().trim();
			String artistNationality = artistNationalityDb.getText().trim().equals("") ? "na" : artistNationalityDb.getText().trim();
			String artistAka = artistAkaDb.getText().trim().equals("") ? "na" : artistAkaDb.getText().trim();
			String artistDecription = artistDecriptionDb.getText().trim().equals("") ? null : artistDecriptionDb.getText().trim();
			
			KeyHolder holder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, artistName);
					ps.setString(2, artistNamePrefix);
					ps.setString(3, artistNameSuffix);
					ps.setString(4, artistDob);
					ps.setString(5, artistDod);
					ps.setString(6, artistBI);
					ps.setString(7, "decade");
					ps.setString(8, artistDi);
					ps.setString(9, "decade");
					ps.setString(10, artistAka);
					ps.setString(11, artistNationality);
					ps.setString(12, artistDecription);
					ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
					ps.setString(14, null);
					ps.setString(15, username);
					ps.setString(16, null);
							
					return ps;
				}
			}, holder);
			
			artistIdDb.setText(String.valueOf(holder.getKeys().get("GENERATED_KEY").toString()));
			
			
			File file = new File(artistsIndexPath);
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
	        writer = new IndexWriter(FSDirectory.open(file), config);
	        
	        org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        	document.add(new Field("fa_artist_ID", String.valueOf(holder.getKeys().get("GENERATED_KEY").toString()), Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_name", artistName, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_name_prefix", artistNamePrefix, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_name_suffix", artistNameSuffix, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_birth_year", artistDob, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_death_year", artistDod, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_birth_year_identifier", artistBI, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_death_year_identifier", artistDi, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_nationality", artistNationality, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_aka", artistAka, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_description", (artistDecription == null ? "" : artistDecription), Field.Store.YES, Field.Index.ANALYZED));
            
            writer.updateDocument(new Term("id", String.valueOf(holder.getKeys().get("GENERATED_KEY").toString())), document);
            suggestionsList.add(0, document);
            ((DefaultTableModel) table.getModel()).insertRow(0, new Object[]{artistName, artistDob, artistDod, artistNamePrefix, artistNameSuffix, 
            		String.valueOf(holder.getKeys().get("GENERATED_KEY").toString())});
            table.setRowSelectionInterval(0, 0);
            table.requestFocus();
            
            writer.commit();
            writer.close();
            
            disableFields();
            
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
	
	private void disableFields() {
		artistNameDb.setEditable(false);
		artistNamePrefixDb.setEditable(false);
		artistNameSuffixDb.setEditable(false);
		artistDobDb.setEditable(false);
		artistDodDb.setEditable(false);
		artistBIDb.setEditable(false);
		artistDiDb.setEditable(false);
		artistNationalityDb.setEditable(false);
		artistAkaDb.setEditable(false);
		artistDecriptionDb.setEditable(false);
		btnAddNewArtist.setEnabled(false);
		btnUpdateArtist.setEnabled(false);
	}

	@SuppressWarnings("deprecation")
	public void btnUpdateArtistActionPerformed() {
		IndexWriter writer = null;
		try {
			String artistName = artistNameDb.getText().trim().trim().equals("") ? "na" : artistNameDb.getText().trim();
			String artistNamePrefix = artistNamePrefixDb.getText().trim().equals("") ? "na" : artistNamePrefixDb.getText().trim();
			String artistNameSuffix = artistNameSuffixDb.getText().trim().equals("") ? "na" : artistNameSuffixDb.getText().trim();
			String artistDob = artistDobDb.getText().trim().equals("") ? "0" : artistDobDb.getText().trim();
			String artistDod = artistDodDb.getText().trim().equals("") ? "0" : artistDodDb.getText().trim();
			String artistBI = artistBIDb.getText().trim().equals("") ? "exact" : artistBIDb.getText().trim();
			String artistDi = artistDiDb.getText().trim().equals("") ? "exact" : artistDiDb.getText().trim();
			String artistNationality = artistNationalityDb.getText().trim().equals("") ? "na" : artistNationalityDb.getText().trim();
			String artistAka = artistAkaDb.getText().trim().equals("") ? "na" : artistAkaDb.getText().trim();
			String artistDecription = artistDecriptionDb.getText().trim().equals("") ? null : artistDecriptionDb.getText().trim();
			
			String query = "UPDATE fineart_artists SET fa_artist_name='" + artistName 
					+ "', fa_artist_birth_year='" + artistDob
					+ "', fa_artist_death_year='" + artistDod
					+ "', fa_artist_nationality='" + artistNationality
					+ "', fa_artist_name_prefix='" + artistNamePrefix
					+ "', fa_artist_name_suffix='" + artistNameSuffix
					+ "', fa_artist_birth_year_identifier='" + artistBI
					+ "', fa_artist_death_year_identifier='" + artistDi
					+ "', fa_artist_description='" + artistDecription
					+ "', fa_artist_aka='" + artistAka
					+ "', fa_artist_record_updated='" + new Timestamp(System.currentTimeMillis())
					+ "', fa_artist_record_updatedby='" + username
					+ "' WHERE fa_artist_ID= " + artistIdDb.getText().trim();
			
			jdbcTemplate.update(query);
			
			File file = new File(artistsIndexPath);
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
	        writer = new IndexWriter(FSDirectory.open(file), config);
	        
	        String [] fields = {"fa_artist_ID"};
	        Query luceneQuery = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("fa_artist_ID:" + artistIdDb.getText().trim());
	        
	        writer.deleteDocuments(luceneQuery);
			
			org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        	document.add(new Field("fa_artist_ID", artistIdDb.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_name", artistName, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_name_prefix", artistNamePrefix, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_name_suffix", artistNameSuffix, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_birth_year", artistDob, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_death_year", artistDod, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_birth_year_identifier", artistBI, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_death_year_identifier", artistDi, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_nationality", artistNationality, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_aka", artistAka, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("fa_artist_description", (artistDecription == null ? "" : artistDecription), Field.Store.YES, Field.Index.ANALYZED));
            
            writer.updateDocument(new Term("id", artistIdDb.getText().trim()), document);
            
            ((DefaultTableModel)table.getModel()).removeRow(tableSelectedRow);
            suggestionsList.remove(tableSelectedRow);
            
            suggestionsList.add(0, document);
            ((DefaultTableModel) table.getModel()).insertRow(0, new Object[]{artistName, artistDob, artistDod, artistNamePrefix, artistNameSuffix, 
            		artistIdDb.getText().trim()});
            table.setRowSelectionInterval(0, 0);
            table.requestFocus();
            
            writer.commit();
            writer.close();
            
            disableFields();
			
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
	
	protected void tableRowSelectionChanged(List<Document> suggestionsList) {
		int selectedRow = table.getSelectedRow();
		
		if(selectedRow != -1) {
			artistNameDb.setText(suggestionsList.get(selectedRow).get("fa_artist_name"));
			artistDobDb.setText(suggestionsList.get(selectedRow).get("fa_artist_birth_year"));
			artistDodDb.setText(suggestionsList.get(selectedRow).get("fa_artist_death_year"));
			artistNationalityDb.setText(suggestionsList.get(selectedRow).get("fa_artist_nationality"));
			artistNamePrefixDb.setText(suggestionsList.get(selectedRow).get("fa_artist_name_prefix"));
			artistNameSuffixDb.setText(suggestionsList.get(selectedRow).get("fa_artist_name_suffix"));
			artistBIDb.setText(suggestionsList.get(selectedRow).get("fa_artist_birth_year_identifier"));
			artistDiDb.setText(suggestionsList.get(selectedRow).get("fa_artist_death_year_identifier"));
			artistDecriptionDb.setText(suggestionsList.get(selectedRow).get("fa_artist_description"));
			artistIdDb.setText(suggestionsList.get(selectedRow).get("fa_artist_ID"));
			
			tableSelectedRow = selectedRow;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void linkWithArtwork(CsvBean csvBean, LotResolverWindow resolveLotIssuesWindow) {
		try {
			String artworkTitle = csvBean.getArtwork_name();
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
			org.apache.lucene.document.Document document = null;
			
			String [] fields = {"faa_artwork_title","faa_artist_ID"};
			Query query = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("faa_artwork_title:" + QueryParser.escape(artworkTitle) + " AND faa_artist_ID:" 
			+ artistId);
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			
			if(hits.length >= 1) {
				for(int i=0; i<hits.length; ++i) {
				    int docId = hits[i].doc;
				    float searchScore = hits[0].score;
				    if(searchScore > 0.8) {
				    	document = searcher.doc(docId);
					    csvBean.setArtwork_ID(document.get("faa_artwork_ID"));
					    resolveLotIssuesWindow.setDatabaseArtworkIdText(document.get("faa_artwork_ID"));
						csvBean.setUpload_report("");
						System.out.println("Found Artwork ID (DB): " + document.get("faa_artwork_ID") + ", Name: " + document.get("faa_artwork_title"));
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Parse Exception in Lucene Search for Search Box Query");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getLucenceSafeString(String input) {
		return input.replaceAll(":", "-").replaceAll("/", "-").replaceAll("\\?", "-").replaceAll("\\(", "-").replaceAll("\\)", "-");
	}
}
*/