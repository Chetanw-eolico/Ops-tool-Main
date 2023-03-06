/*package com.fineart.uploader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.core.Constants;
import com.fineart.scraper.CsvBean;

public class ArtworkAddUpdateWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_2;
	private JTextField widthText;
	private JTextField depthText;
	private JTextField unitText;
	private JTextField startYearText;
	private JTextField artistIdText;
	private JTextField endYearText;
	private JTextField heightText;
	LotResolverWindow lotResolverWindow;
	ArtworkResolverWindow artworkResolverWindow;
	CsvBean csvBean;
	String action;
	private JPanel basePanel;
	private JTextArea descriptionText;
	private JTextArea materialText;
	private JTextArea editionText;
	private JTextArea exhibitionText;
	private JTextArea literatureText;
	private JTextArea sizeNotesText;
	private JTextArea markingsText;
	private JComboBox<String> categoryText;
	private JComboBox<String> syIdentifierText;
	private JComboBox<String> eyIdentifierText;
	private JComboBox<String> syPrecisionText;
	private JComboBox<String> eyPrecisionText;
	private JTextArea titleText;
	private JTextArea imageText;

	public ArtworkAddUpdateWindow(String action, CsvBean csvBean, LotResolverWindow lotResolverWindow, ArtworkResolverWindow artworkResolverWindow) {
		try {
			this.action = action;
			this.csvBean = csvBean;
			this.lotResolverWindow = lotResolverWindow;
			this.artworkResolverWindow = artworkResolverWindow;
			this.setPreferredSize(new java.awt.Dimension(1050, 641));
			this.setSize(new Dimension(1112, 569));
			this.setTitle(action + " artwork...");
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
			ToolTipManager.sharedInstance().setInitialDelay(100 );
			ToolTipManager.sharedInstance().setDismissDelay(10 * 1000 );
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			basePanel = new JPanel();
			contentPane.add(basePanel);
			basePanel.setLayout(null);
			
			JLabel lblArtworkTitle = new JLabel("Artwork Title:");
			lblArtworkTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblArtworkTitle.setBounds(276, 9, 98, 24);
			basePanel.add(lblArtworkTitle);
			
			JLabel lblDescription = new JLabel("Description:");
			lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblDescription.setBounds(276, 102, 98, 24);
			basePanel.add(lblDescription);
			
			descriptionText = new JTextArea(csvBean.getArtwork_description());
			descriptionText.setWrapStyleWord(true);
			descriptionText.setLineWrap(true);
			descriptionText.setBounds(366, 108, 300, 89);
			
			
			JScrollPane sp9 = new JScrollPane();
			sp9.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp9.setBounds(366, 108, 300, 89);
			basePanel.add(sp9);
			sp9.add(descriptionText);
			sp9.setViewportView(descriptionText);
			
			JLabel lblMaterial = new JLabel("Material:");
			lblMaterial.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblMaterial.setBounds(276, 408, 98, 24);
			basePanel.add(lblMaterial);
			
			materialText = new JTextArea(csvBean.getArtwork_materials());
			materialText.setWrapStyleWord(true);
			materialText.setLineWrap(true);
			materialText.setBounds(366, 410, 300, 89);
			
			JScrollPane sp1 = new JScrollPane();
			sp1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp1.setBounds(366, 410, 300, 89);
			basePanel.add(sp1);
			sp1.add(materialText);
			sp1.setViewportView(materialText);
			
			JLabel lblEdition = new JLabel("Edition:");
			lblEdition.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblEdition.setBounds(684, 121, 63, 24);
			basePanel.add(lblEdition);
			
			editionText = new JTextArea(csvBean.getArtwork_edition());
			editionText.setWrapStyleWord(true);
			editionText.setLineWrap(true);
			editionText.setBounds(763, 123, 300, 89);
			
			JScrollPane sp2 = new JScrollPane();
			sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp2.setBounds(763, 123, 300, 89);
			basePanel.add(sp2);
			sp2.add(editionText);
			sp2.setViewportView(editionText);
			
			JLabel lblExhibition = new JLabel("Exhibition:");
			lblExhibition.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblExhibition.setBounds(684, 9, 74, 24);
			basePanel.add(lblExhibition);
			
			exhibitionText = new JTextArea(csvBean.getArtwork_exhibited());
			exhibitionText.setWrapStyleWord(true);
			exhibitionText.setLineWrap(true);
			exhibitionText.setBounds(763, 11, 300, 89);
			
			JScrollPane sp3 = new JScrollPane();
			sp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp3.setBounds(763, 11, 300, 89);
			basePanel.add(sp3);
			sp3.add(exhibitionText);
			sp3.setViewportView(exhibitionText);
			
			JLabel lblLiterature = new JLabel("Literature:");
			lblLiterature.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblLiterature.setBounds(684, 235, 63, 24);
			basePanel.add(lblLiterature);
			
			literatureText = new JTextArea(csvBean.getArtwork_literature());
			literatureText.setWrapStyleWord(true);
			literatureText.setLineWrap(true);
			literatureText.setBounds(763, 237, 300, 89);
			
			JScrollPane sp4 = new JScrollPane();
			sp4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp4.setBounds(763, 237, 300, 89);
			basePanel.add(sp4);
			sp4.add(literatureText);
			sp4.setViewportView(literatureText);
			
			JLabel lblSizeNotes = new JLabel("Size Notes:");
			lblSizeNotes.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblSizeNotes.setBounds(276, 204, 98, 24);
			basePanel.add(lblSizeNotes);
			
			sizeNotesText = new JTextArea(csvBean.getArtwork_size_notes());
			sizeNotesText.setWrapStyleWord(true);
			sizeNotesText.setLineWrap(true);
			sizeNotesText.setBounds(366, 207, 300, 89);
			
			JScrollPane sp5 = new JScrollPane();
			sp5.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp5.setBounds(366, 207, 300, 89);
			basePanel.add(sp5);
			sp5.add(sizeNotesText);
			sp5.setViewportView(sizeNotesText);
			
			JLabel lblMarkings = new JLabel("Markings:");
			lblMarkings.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblMarkings.setBounds(276, 306, 98, 24);
			basePanel.add(lblMarkings);
			
			markingsText = new JTextArea(csvBean.getArtwork_markings());
			markingsText.setWrapStyleWord(true);
			markingsText.setLineWrap(true);
			markingsText.setBounds(366, 309, 300, 89);
			
			JScrollPane sp6 = new JScrollPane();
			sp6.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp6.setBounds(366, 309, 300, 89);
			basePanel.add(sp6);
			sp6.add(markingsText);
			sp6.setViewportView(markingsText);
			
			JLabel lblCategory = new JLabel("Category:");
			lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblCategory.setBounds(10, 46, 98, 24);
			basePanel.add(lblCategory);
			
			JLabel lblHeight = new JLabel("Height:");
			lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblHeight.setBounds(10, 81, 98, 24);
			basePanel.add(lblHeight);
			
			textField_2 = new JTextField();
			textField_2.setColumns(10);
			textField_2.setBounds(119, 81, 134, 24);
			
			JLabel lblWidth = new JLabel("Width:");
			lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblWidth.setBounds(10, 116, 98, 24);
			basePanel.add(lblWidth);
			
			widthText = new JTextField(csvBean.getArtwork_measurements_width());
			widthText.setColumns(10);
			widthText.setBounds(119, 116, 134, 24);
			basePanel.add(widthText);
			
			JLabel lblDepth = new JLabel("Depth:");
			lblDepth.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblDepth.setBounds(10, 151, 98, 24);
			basePanel.add(lblDepth);
			
			depthText = new JTextField(csvBean.getArtwork_measurements_depth());
			depthText.setColumns(10);
			depthText.setBounds(119, 151, 134, 24);
			basePanel.add(depthText);
			
			JLabel lblUnit = new JLabel("Unit:");
			lblUnit.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblUnit.setBounds(10, 183, 98, 24);
			basePanel.add(lblUnit);
			
			unitText = new JTextField(csvBean.getAuction_measureunit());
			unitText.setColumns(10);
			unitText.setBounds(119, 183, 134, 24);
			basePanel.add(unitText);
			
			JLabel lblArtworkStartYear = new JLabel("Start Year:");
			lblArtworkStartYear.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblArtworkStartYear.setBounds(10, 217, 98, 24);
			basePanel.add(lblArtworkStartYear);
			
			startYearText = new JTextField(csvBean.getArtwork_start_year());
			startYearText.setColumns(10);
			startYearText.setBounds(119, 217, 134, 24);
			basePanel.add(startYearText);
			
			JLabel lblPrecesion = new JLabel("(SY) Identifier:");
			lblPrecesion.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblPrecesion.setBounds(10, 252, 98, 24);
			basePanel.add(lblPrecesion);
			
			JLabel lblArtistId = new JLabel("Artist Id:");
			lblArtistId.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblArtistId.setBounds(10, 9, 98, 24);
			basePanel.add(lblArtistId);
			
			artistIdText = new JTextField(csvBean.getArtist_ID());
			artistIdText.setEditable(false);
			artistIdText.setColumns(10);
			artistIdText.setBounds(119, 9, 134, 24);
			basePanel.add(artistIdText);
			
			JLabel lblPrecesion_1 = new JLabel("(SY) Precision:");
			lblPrecesion_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblPrecesion_1.setBounds(10, 287, 98, 24);
			basePanel.add(lblPrecesion_1);
			
			JLabel lblEndYear = new JLabel("End Year:");
			lblEndYear.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblEndYear.setBounds(10, 322, 98, 24);
			basePanel.add(lblEndYear);
			
			endYearText = new JTextField(csvBean.getArtwork_end_year());
			endYearText.setColumns(10);
			endYearText.setBounds(119, 322, 134, 24);
			basePanel.add(endYearText);
			
			JLabel lbleyIdentifier = new JLabel("(EY) Identifier:");
			lbleyIdentifier.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lbleyIdentifier.setBounds(10, 357, 98, 24);
			basePanel.add(lbleyIdentifier);
			
			JLabel lbleyPrecision = new JLabel("(EY) Precision:");
			lbleyPrecision.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lbleyPrecision.setBounds(10, 392, 98, 24);
			basePanel.add(lbleyPrecision);
			
			JLabel lblImage = new JLabel("Image:");
			lblImage.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblImage.setBounds(10, 427, 98, 24);
			basePanel.add(lblImage);
			
			categoryText = new JComboBox<String>();
			categoryText.setModel(new DefaultComboBoxModel<String>(new String[] {"paintings", "sculptures", "prints", "photographs", "works on paper", "miniatures"}));
			categoryText.setBounds(119, 46, 134, 24);
			basePanel.add(categoryText);
			categoryText.setSelectedItem(csvBean.getArtwork_category());
			
			syIdentifierText = new JComboBox<String>();
			syIdentifierText.setModel(new DefaultComboBoxModel<String>(new String[] {"exact", "after", "before", "circa"}));
			syIdentifierText.setBounds(119, 252, 134, 24);
			basePanel.add(syIdentifierText);
			syIdentifierText.setSelectedItem(csvBean.getArtwork_year_identifier());
			
			eyIdentifierText = new JComboBox<String>();
			eyIdentifierText.setModel(new DefaultComboBoxModel<String>(new String[] {"exact", "after", "before", "circa"}));
			eyIdentifierText.setBounds(119, 357, 134, 24);
			basePanel.add(eyIdentifierText);
			
			syPrecisionText = new JComboBox<String>();
			syPrecisionText.setModel(new DefaultComboBoxModel<String>(new String[] {"decade", "century", "millennial"}));
			syPrecisionText.setBounds(119, 286, 134, 24);
			basePanel.add(syPrecisionText);
			
			eyPrecisionText = new JComboBox<String>();
			eyPrecisionText.setModel(new DefaultComboBoxModel<String>(new String[] {"decade", "century", "millennial"}));
			eyPrecisionText.setBounds(119, 389, 134, 24);
			basePanel.add(eyPrecisionText);
			
			titleText = new JTextArea(csvBean.getArtwork_name());
			titleText.setWrapStyleWord(true);
			titleText.setLineWrap(true);
			titleText.setBounds(366, 11, 300, 89);
			
			JScrollPane sp7 = new JScrollPane();
			sp7.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp7.setBounds(366, 11, 300, 89);
			basePanel.add(sp7);
			sp7.add(titleText);
			sp7.setViewportView(titleText);
			
			JPanel panel = new JPanel();
			panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			panel.setBounds(695, 337, 368, 162);
			basePanel.add(panel);
			panel.setLayout(null);
			
			JButton actionButton = new JButton();
			actionButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					actionButtonActionPerformed();
				}
			});
			actionButton.setBounds(37, 11, 300, 64);
			panel.add(actionButton);
			actionButton.setBackground(SystemColor.inactiveCaption);
			actionButton.setForeground(Color.BLACK);
			actionButton.setFont(new Font("Tahoma", Font.BOLD, 14));
			
			if(action.equals("add")) {
				actionButton.setText("Add");
			} else if(action.equals("update")) {
				actionButton.setText("Update");
			}
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelButtonActionPerformed();
				}
			});
			cancelButton.setBounds(37, 86, 300, 64);
			panel.add(cancelButton);
			cancelButton.setBackground(new Color(255, 228, 225));
			cancelButton.setFont(new Font("Tahoma", Font.BOLD, 14));
			
			imageText = new JTextArea(csvBean.getImage1_name());
			imageText.setEditable(false);
			imageText.setWrapStyleWord(true);
			imageText.setLineWrap(true);
			imageText.setBounds(119, 438, 239, 60);
			
			JScrollPane sp8 = new JScrollPane();
			sp8.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			sp8.setBounds(119, 438, 239, 60);
			basePanel.add(sp8);
			sp8.add(imageText);
			sp8.setViewportView(imageText);
			
			heightText = new JTextField(csvBean.getArtwork_measurements_height());
			heightText.setColumns(10);
			heightText.setBounds(118, 81, 134, 24);
			basePanel.add(heightText);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void cancelButtonActionPerformed() {
		this.dispose();
		setVisible(false);
	}

	protected void actionButtonActionPerformed() {
		KeyHolder holder = null;
		try {
			String title = StringUtils.isEmpty(titleText.getText().trim()) ? "na" : titleText.getText().trim();
	        String height = StringUtils.isEmpty(heightText.getText().trim()) ? "0.00" : heightText.getText().trim();
	        String width = StringUtils.isEmpty(widthText.getText().trim()) ? "0.00" : widthText.getText().trim();
	        String depth = StringUtils.isEmpty(depthText.getText().trim()) ? "0.00" : depthText.getText().trim();
	        String unit = StringUtils.isEmpty(unitText.getText().trim()) ? "na" : unitText.getText().trim();
	        String artworkStart = StringUtils.isEmpty(startYearText.getText().trim()) ? "0" : startYearText.getText().trim();
	        String artworkEnd = StringUtils.isEmpty(endYearText.getText().trim()) ? "0" : endYearText.getText().trim();
	        String image1 = StringUtils.isEmpty(imageText.getText().trim()) ? "na" : imageText.getText().trim();
	        String description = descriptionText.getText().trim();
	        String artistId = artistIdText.getText().trim();
	        String category = categoryText.getSelectedItem().toString().trim();
	        String material = materialText.getText().trim();
	        String edition = editionText.getText().trim();
	        String exhibition = exhibitionText.getText().trim();
	        String literature = literatureText.getText().trim();
	        String sizeNotes = sizeNotesText.getText().trim();
	        String markings = markingsText.getText().trim();
	        String syIdentifier = syIdentifierText.getSelectedItem().toString().trim();
	        String syPrecision = syPrecisionText.getSelectedItem().toString().trim();
	        String eyIdentifier = eyIdentifierText.getSelectedItem().toString().trim();
	        String eyPrecision = eyPrecisionText.getSelectedItem().toString().trim();
	        
			if(action.equals("add")) {
				////insert artworks
				String artworksQuery = "insert into fineart_artworks (faa_artwork_title, faa_artwork_description, faa_artist_ID, faa_artwork_category, faa_artwork_material, "
						+ "faa_artwork_edition, faa_artwork_exhibition, faa_artwork_literature, faa_artwork_height, "
						+ "faa_artwork_width, faa_artwork_depth, faa_arwork_measurement_unit, faa_artwork_size_details, faa_artwork_markings, faa_artwork_start_year, "
						+ "faa_artwork_start_year_identifier, faa_artwork_start_year_precision, faa_artwork_end_year, faa_artwork_end_year_identifier, "
						+ "faa_artwork_end_year_precision, faa_artwork_image1, faa_artwork_record_created, faa_artwork_record_updated, faa_artwork_record_createdby, "
						+ "faa_artwork_record_updatedby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				holder = new GeneratedKeyHolder();
				
				artworkResolverWindow.getJdbcTemplate().update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(artworksQuery, Statement.RETURN_GENERATED_KEYS);
						ps.setString(1, getSqlSafeString(title));
						ps.setString(2, getSqlSafeString(description));
						ps.setString(3, artistId);
						ps.setString(4, category);
						ps.setString(5, getSqlSafeString(material));
						ps.setString(6, getSqlSafeString(edition));
						ps.setString(7, getSqlSafeString(exhibition));
						ps.setString(8, getSqlSafeString(literature));
						ps.setString(9, height);
						ps.setString(10, width);
						ps.setString(11, depth);
						ps.setString(12, unit);
						ps.setString(13, getSqlSafeString(sizeNotes));
						ps.setString(14, getSqlSafeString(markings));
						ps.setString(15, artworkStart);
						ps.setString(16, syIdentifier);
						ps.setString(17, syPrecision);
						ps.setString(18, artworkEnd);
						ps.setString(19, eyIdentifier);
						ps.setString(20, eyPrecision);
						ps.setString(21, image1);
						ps.setTimestamp(22, new Timestamp(System.currentTimeMillis()));
						ps.setString(23, null);
						ps.setString(24, artworkResolverWindow.getUsername());
						ps.setString(25, null);
								
						return ps;
					}
				}, holder);
				
				String faa_artwork_ID = String.valueOf(holder.getKeys().get("GENERATED_KEY").toString());
				
				addArtworkToLocalIndex(faa_artwork_ID);
				
				JOptionPane.showMessageDialog(null, "Artwork Added!");
				artworkResolverWindow.getTxtArtworkNameDb().setText(title);
				artworkResolverWindow.setArtworkIdDb(faa_artwork_ID);
				csvBean.setArtwork_ID(faa_artwork_ID);
				csvBean.setArtwork_name(title);
				csvBean.setArtwork_description(description);
				csvBean.setArtwork_category(category);
				csvBean.setArtwork_materials(material);
				csvBean.setArtwork_edition(edition);
				csvBean.setArtwork_exhibited(exhibition);
				csvBean.setArtwork_literature(literature);
				csvBean.setArtwork_measurements_height(height);
				csvBean.setArtwork_measurements_width(width);
				csvBean.setArtwork_measurements_depth(depth);
				csvBean.setAuction_measureunit(unit);
				csvBean.setArtwork_size_notes(sizeNotes);
				csvBean.setArtwork_markings(markings);
				csvBean.setArtwork_start_year(artworkStart);
				csvBean.setArtwork_end_year(artworkEnd);
				csvBean.setImage1_name(image1);
				
				artworkResolverWindow.getLblImagelabeldb().setToolTipText("<html><b>Size:</b> " + height + " X " + width 
		        + " X " + depth + " " + unit + "<br/><b>Start Year:</b> " + artworkStart + "<br/><b>End Year:</b> " + artworkEnd + "</html>");
				
		        URL urlDb;
		        Image imageDb;
		        try {
		        	urlDb = new URL(Constants.BACKBLAZE_FINEART_BUCKET_URL + image1);
		        	imageDb = ImageIO.read(urlDb);
		        	imageDb = imageDb.getScaledInstance((int)artworkResolverWindow.getDbPanel().getWidth(),(int)artworkResolverWindow.getDbPanel().getHeight(),
		        			Image.SCALE_SMOOTH);
		        	artworkResolverWindow.getLblImagelabeldb().setIcon(new ImageIcon(imageDb));
				} catch (javax.imageio.IIOException iio) {
					System.out.println("DB: " + iio.getMessage());
				}
		        this.setVisible(false);
		        this.dispose();
				
				} else if(action.equals("update")) { // if update
					
					String updateQuery = "UPDATE fineart_artworks SET "
							+ "faa_artwork_title='" + getSqlSafeString(title) + "', "
							+ "faa_artwork_description='" + getSqlSafeString(description) + "', "
							+ "faa_artwork_category='" + category + "', "
							+ "faa_artwork_material='" + getSqlSafeString(material) + "', "
							+ "faa_artwork_edition='" + getSqlSafeString(edition) + "', "
							+ "faa_artwork_exhibition='" + getSqlSafeString(exhibition) + "', "
							+ "faa_artwork_literature='" + getSqlSafeString(literature) + "', "
							+ "faa_artwork_height='" + height + "', "
							+ "faa_artwork_width='" + width + "', "
							+ "faa_artwork_depth='" + depth + "', "
							+ "faa_arwork_measurement_unit='" + unit + "', "
							+ "faa_artwork_size_details='" + getSqlSafeString(sizeNotes) + "', "
							+ "faa_artwork_markings='" + getSqlSafeString(markings) + "', "
							+ "faa_artwork_start_year='" + artworkStart + "', "
							+ "faa_artwork_start_year_identifier='" + syIdentifier + "', "
							+ "faa_artwork_start_year_precision='" + syPrecision + "', "
							+ "faa_artwork_end_year='" + artworkEnd + "', "
							+ "faa_artwork_end_year_identifier='" + eyIdentifier + "', "
							+ "faa_artwork_end_year_precision='" + eyPrecision + "', "
							+ "faa_artwork_image1='" + image1 + "', "
							+ "faa_artwork_record_updated='" + new Timestamp(System.currentTimeMillis()) + "', "
							+ "faa_artwork_record_updatedby='" + artworkResolverWindow.getUsername() + "' "
							+ " WHERE faa_artwork_ID = " + lotResolverWindow.getDatabaseArtworkIdText();
					
					artworkResolverWindow.getJdbcTemplate().update(updateQuery);
					
					deleteExistingArtworkFromLocalIndex();
					addArtworkToLocalIndex(lotResolverWindow.getDatabaseArtworkIdText());
					JOptionPane.showMessageDialog(null, "Artwork Updated!");
					artworkResolverWindow.getTxtArtworkNameDb().setText(title);
					artworkResolverWindow.getLblImagelabeldb().setToolTipText("<html><b>Size:</b> " + height + " X " + width 
			        + " X " + depth + " " + unit + "<br/><b>Start Year:</b> " + artworkStart + "<br/><b>End Year:</b> " + artworkEnd + "</html>");
					
			        URL urlDb;
			        Image imageDb;
			        try {
			        	urlDb = new URL(Constants.BACKBLAZE_FINEART_BUCKET_URL + image1);
			        	imageDb = ImageIO.read(urlDb);
			        	imageDb = imageDb.getScaledInstance((int)artworkResolverWindow.getDbPanel().getWidth(),(int)artworkResolverWindow.getDbPanel().getHeight(),
			        			Image.SCALE_SMOOTH);
			        	artworkResolverWindow.getLblImagelabeldb().setIcon(new ImageIcon(imageDb));
					} catch (javax.imageio.IIOException iio) {
						System.out.println("DB: " + iio.getMessage());
					}
			        this.setVisible(false);
			        this.dispose();
				}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void deleteExistingArtworkFromLocalIndex() throws ParseException, IOException {
		IndexWriter writer = null;
		File file = new File(artworkResolverWindow.getArtworkIndexPath());
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        writer = new IndexWriter(FSDirectory.open(file), config);
        
        String [] fields = {"fa_artist_ID"};
        Query luceneQuery = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("faa_artwork_ID:" + lotResolverWindow.getDatabaseArtworkIdText());
        writer.deleteDocuments(luceneQuery);
        writer.close();
	}

	@SuppressWarnings("deprecation")
	private void addArtworkToLocalIndex(String faa_artwork_ID) {
		IndexWriter writer = null;
		try {
			File file = new File(artworkResolverWindow.getArtworkIndexPath());
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
	        writer = new IndexWriter(FSDirectory.open(file), config);
	        
	        String artworkTitle = StringUtils.isEmpty(titleText.getText().trim()) ? "na" : titleText.getText().trim();
	        String height = StringUtils.isEmpty(heightText.getText().trim()) ? "0.00" : heightText.getText().trim();
	        String width = StringUtils.isEmpty(widthText.getText().trim()) ? "0.00" : widthText.getText().trim();
	        String depth = StringUtils.isEmpty(depthText.getText().trim()) ? "0.00" : depthText.getText().trim();
	        String unit = StringUtils.isEmpty(unitText.getText().trim()) ? "na" : unitText.getText().trim();
	        String artworkStart = StringUtils.isEmpty(startYearText.getText().trim()) ? "0" : startYearText.getText().trim();
	        String artworkEnd = StringUtils.isEmpty(endYearText.getText().trim()) ? "0" : endYearText.getText().trim();
	        String image1 = StringUtils.isEmpty(imageText.getText().trim()) ? "na" : imageText.getText().trim();
	        

	    	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
	    	document.add(new Field("faa_artwork_ID", faa_artwork_ID, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_title", artworkTitle, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_description", descriptionText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artist_ID", artistIdText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_category", categoryText.getSelectedItem().toString().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_material", materialText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_edition", editionText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_exhibition", exhibitionText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_literature", literatureText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_height", height, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_width", width, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_depth", depth, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_arwork_measurement_unit", unit, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_size_details", sizeNotesText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_markings", markingsText.getText().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_start_year", artworkStart, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_start_year_identifier", syIdentifierText.getSelectedItem().toString().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_start_year_precision", syPrecisionText.getSelectedItem().toString().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_end_year", artworkEnd, Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_end_year_identifier", eyIdentifierText.getSelectedItem().toString().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_end_year_precision", eyPrecisionText.getSelectedItem().toString().trim(), Field.Store.YES, Field.Index.ANALYZED));
	        document.add(new Field("faa_artwork_image1", image1, Field.Store.YES, Field.Index.ANALYZED));
	        
	        writer.updateDocument(new Term("id", faa_artwork_ID), document);
	        writer.commit();
	        writer.close();
	        
	        System.out.println("Added to Artwork Local Index!");
	        
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
		titleText.setEnabled(false);
		heightText.setEnabled(false);
		widthText.setEnabled(false);
		depthText.setEnabled(false);
		unitText.setEnabled(false);
		startYearText.setEnabled(false);
		endYearText.setEnabled(false);
		imageText.setEnabled(false);
		descriptionText.setEnabled(false);
		categoryText.setEnabled(false);
		materialText.setEnabled(false);
		editionText.setEnabled(false);
		exhibitionText.setEnabled(false);
		literatureText.setEnabled(false);
		sizeNotesText.setEnabled(false);
		markingsText.setEnabled(false);
		syIdentifierText.setEnabled(false);
		syPrecisionText.setEnabled(false);
		eyIdentifierText.setEnabled(false);
		eyPrecisionText.setEnabled(false);
	}

	private String getSqlSafeString(String input) {
		return input.replaceAll("'", "\\\\'");
	}
}
*/