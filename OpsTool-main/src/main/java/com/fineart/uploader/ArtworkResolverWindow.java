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
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.lucene.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;

import com.core.Constants;
import com.fineart.scraper.CsvBean;

public class ArtworkResolverWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtArtworkNameCSV;
	private JTextField txtArtistIdCSV;
	private JTextField txtArtworkNameDb;
	private JTextField txtArtistIdDb;
	private CsvBean csvBean;
	private LotResolverWindow lotResolverWindow;
	private String artworkIndexPath;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private JLabel lblImagelabeldb;
	private JPanel dbPanel;
	private String artworkIdDb;

	public ArtworkResolverWindow(LotResolverWindow lotResolverWindow, CsvBean csvBean, Document document, 
			String artworkIndexPath, String username, JdbcTemplate jdbcTemplate) {
		try {
			this.lotResolverWindow = lotResolverWindow;
			this.csvBean = csvBean;
			this.artworkIndexPath = artworkIndexPath;
			this.jdbcTemplate = jdbcTemplate;
			this.username = username;
			this.artworkIdDb = document.get("faa_artwork_ID");
			
			this.setPreferredSize(new java.awt.Dimension(1050, 641));
			this.setSize(new Dimension(1195, 641));
			this.setTitle("Resolve Artwork Issues...");
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
			ToolTipManager.sharedInstance().setInitialDelay(100 );
			ToolTipManager.sharedInstance().setDismissDelay(10 * 1000 );
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			JPanel basePanel = new JPanel();
			contentPane.add(basePanel);
			basePanel.setLayout(null);
			
			JLabel lblCsvArtwork = new JLabel("CSV Artwork:");
			lblCsvArtwork.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblCsvArtwork.setBounds(10, 11, 113, 26);
			basePanel.add(lblCsvArtwork);
			
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			panel_1.setBounds(20, 48, 449, 533);
			basePanel.add(panel_1);
			panel_1.setLayout(null);
			
			JLabel lblArtworkName = new JLabel("Artwork Name:");
			lblArtworkName.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblArtworkName.setBounds(10, 11, 108, 28);
			panel_1.add(lblArtworkName);
			
			txtArtworkNameCSV = new JTextField(csvBean.getArtwork_name());
			txtArtworkNameCSV.setEditable(false);
			txtArtworkNameCSV.setToolTipText(csvBean.getArtwork_name());
			txtArtworkNameCSV.setFont(new Font("Tahoma", Font.PLAIN, 12));
			txtArtworkNameCSV.setBounds(128, 8, 311, 31);
			panel_1.add(txtArtworkNameCSV);
			txtArtworkNameCSV.setColumns(10);
			
			JLabel lblArtistId = new JLabel("Artist Id:");
			lblArtistId.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblArtistId.setBounds(10, 53, 108, 28);
			panel_1.add(lblArtistId);
			
			txtArtistIdCSV = new JTextField(csvBean.getArtist_ID());
			txtArtistIdCSV.setEditable(false);
			txtArtistIdCSV.setToolTipText(csvBean.getArtist_ID());
			txtArtistIdCSV.setFont(new Font("Tahoma", Font.PLAIN, 12));
			txtArtistIdCSV.setColumns(10);
			txtArtistIdCSV.setBounds(128, 50, 311, 31);
			panel_1.add(txtArtistIdCSV);
			
			JPanel csvPanel = new JPanel();
			csvPanel.setToolTipText("abc");
			csvPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			csvPanel.setBounds(10, 92, 429, 430);
			panel_1.add(csvPanel);
			csvPanel.setLayout(new BorderLayout(0, 0));
			
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			separator.setBounds(479, 11, 2, 570);
			basePanel.add(separator);
			
			JLabel lblDatabaseArtwork = new JLabel("Database Artwork:");
	        lblDatabaseArtwork.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        lblDatabaseArtwork.setBounds(491, 11, 146, 26);
	        basePanel.add(lblDatabaseArtwork);
	        
	        JPanel panel_3 = new JPanel();
	        panel_3.setLayout(null);
	        panel_3.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
	        panel_3.setBounds(501, 48, 449, 533);
	        basePanel.add(panel_3);
	        
	        JLabel label_1 = new JLabel("Artwork Name:");
	        label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        label_1.setBounds(10, 11, 108, 28);
	        panel_3.add(label_1);
	        
	        txtArtworkNameDb = new JTextField(document.get("faa_artwork_title"));
	        txtArtworkNameDb.setEditable(false);
	        txtArtworkNameDb.setToolTipText(document.get("faa_artwork_title"));
	        txtArtworkNameDb.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        txtArtworkNameDb.setColumns(10);
	        txtArtworkNameDb.setBounds(128, 8, 311, 31);
	        panel_3.add(txtArtworkNameDb);
	        
	        JLabel label_2 = new JLabel("Artist Id:");
	        label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        label_2.setBounds(10, 53, 108, 28);
	        panel_3.add(label_2);
	        
	        txtArtistIdDb = new JTextField(document.get("faa_artist_ID"));
	        txtArtistIdDb.setEditable(false);
	        txtArtistIdDb.setToolTipText(document.get("faa_artist_ID"));
	        txtArtistIdDb.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        txtArtistIdDb.setColumns(10);
	        txtArtistIdDb.setBounds(128, 50, 311, 31);
	        panel_3.add(txtArtistIdDb);
	        
	        dbPanel = new JPanel();
	        dbPanel.setToolTipText("abc");
	        dbPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
	        dbPanel.setBounds(10, 92, 429, 430);
	        panel_3.add(dbPanel);
	        dbPanel.setLayout(new BorderLayout(0, 0));
	        
	        lblImagelabeldb = new JLabel("Image could not be loaded!");
	        dbPanel.add(lblImagelabeldb);
			
	        lblImagelabeldb.setToolTipText("<html><b>Size:</b> " + document.get("faa_artwork_height") + " X " + document.get("faa_artwork_width") 
	        + " X " + document.get("faa_artwork_depth") + " " + document.get("faa_arwork_measurement_unit") + "<br/><b>Start Year:</b> " + document.get("faa_artwork_start_year") 
	        + "<br/><b>End Year:</b> " + document.get("faa_artwork_end_year") + "</html>");
			
	        URL urlDb;
	        Image imageDb;
	        try {
	        	urlDb = new URL(Constants.BACKBLAZE_FINEART_BUCKET_URL + document.get("faa_artwork_image1"));
	        	imageDb = ImageIO.read(urlDb);
	        	imageDb = imageDb.getScaledInstance((int)dbPanel.getWidth(),(int)csvPanel.getHeight(),Image.SCALE_SMOOTH);
		        lblImagelabeldb.setIcon(new ImageIcon(imageDb));
			} catch (javax.imageio.IIOException iio) {
				System.out.println("DB: " + iio.getMessage());
			}
			
			
			JLabel lblImagelabelcsv = new JLabel("Image could not be loaded!");
			csvPanel.add(lblImagelabelcsv);
			
			lblImagelabelcsv.setToolTipText("<html><b>Size:</b> " + csvBean.getArtwork_measurements_height() + " X " + csvBean.getArtwork_measurements_width() 
	        + " X " + csvBean.getArtwork_measurements_depth() + " " + csvBean.getAuction_measureunit() + "<br/><b>Start Year:</b> " + csvBean.getArtwork_start_year() 
	        + "<br/><b>End Year:</b> " + csvBean.getArtwork_end_year() + "</html>");
			
			lblImagelabeldb.setToolTipText("<html><b>Size:</b> " + document.get("faa_artwork_height") + " X " + document.get("faa_artwork_width") 
	        + " X " + document.get("faa_artwork_depth") + " " + document.get("faa_arwork_measurement_unit") + "<br/><b>Start Year:</b> " 
					+ document.get("faa_artwork_start_year") + "<br/><b>End Year:</b> " + document.get("faa_artwork_end_year") + "</html>");
			
			URL urlCsv;
	        Image imageCsv;
	        
	        try {
	        	urlCsv = new URL(csvBean.getArtwork_images1());
	        	imageCsv = ImageIO.read(urlCsv);
	        	imageCsv = imageCsv.getScaledInstance((int)csvPanel.getWidth(),(int)csvPanel.getHeight(),Image.SCALE_SMOOTH);
		        lblImagelabelcsv.setIcon(new ImageIcon(imageCsv));
		    } catch (javax.imageio.IIOException iio) {
				System.out.println("CSV: " + iio.getMessage());
			}
	        
	        JButton addNewArtworkButton = new JButton("Add New Artwork");
	        addNewArtworkButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		addNewArtworkButtonActionPerformed();
	        	}
	        });
	        addNewArtworkButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        addNewArtworkButton.setBackground(new Color(240, 255, 255));
	        addNewArtworkButton.setBounds(980, 139, 177, 82);
	        basePanel.add(addNewArtworkButton);
	        
	        JButton updateArtworkButton = new JButton("Update This Artwork");
	        updateArtworkButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		updateArtworkButtonActionPerformed();
	        	}
	        });
	        updateArtworkButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        updateArtworkButton.setBackground(new Color(255, 222, 173));
	        updateArtworkButton.setBounds(980, 261, 177, 82);
	        basePanel.add(updateArtworkButton);
	        
	        JButton linkArtworkButton = new JButton("Link With This Artwork");
	        linkArtworkButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		linkArtworkButtonActionPerformed();
	        	}
	        });
	        linkArtworkButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        linkArtworkButton.setBackground(SystemColor.inactiveCaption);
	        linkArtworkButton.setBounds(980, 397, 177, 82);
	        basePanel.add(linkArtworkButton);
	    }catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void addNewArtworkButtonActionPerformed() {
		try {
			ArtworkAddUpdateWindow frame = new ArtworkAddUpdateWindow("add", csvBean, lotResolverWindow, this);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateArtworkButtonActionPerformed() {
		try {
			ArtworkAddUpdateWindow frame = new ArtworkAddUpdateWindow("update", csvBean, lotResolverWindow, this);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void linkArtworkButtonActionPerformed() {
		lotResolverWindow.setDatabaseArtworkIdText(artworkIdDb);
		this.setVisible(false);
		this.dispose();
	}

	public String getArtworkIndexPath() {
		return artworkIndexPath;
	}

	public void setArtworkIndexPath(String artworkIndexPath) {
		this.artworkIndexPath = artworkIndexPath;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public JTextField getTxtArtworkNameDb() {
		return txtArtworkNameDb;
	}

	public void setTxtArtworkNameDb(JTextField txtArtworkNameDb) {
		this.txtArtworkNameDb = txtArtworkNameDb;
	}

	public JTextField getTxtArtistIdDb() {
		return txtArtistIdDb;
	}

	public void setTxtArtistIdDb(JTextField txtArtistIdDb) {
		this.txtArtistIdDb = txtArtistIdDb;
	}

	public JLabel getLblImagelabeldb() {
		return lblImagelabeldb;
	}

	public void setLblImagelabeldb(JLabel lblImagelabeldb) {
		this.lblImagelabeldb = lblImagelabeldb;
	}

	public JPanel getDbPanel() {
		return dbPanel;
	}

	public void setDbPanel(JPanel dbPanel) {
		this.dbPanel = dbPanel;
	}

	public String getArtworkIdDb() {
		return artworkIdDb;
	}

	public void setArtworkIdDb(String artworkIdDb) {
		this.artworkIdDb = artworkIdDb;
	}
}
*/