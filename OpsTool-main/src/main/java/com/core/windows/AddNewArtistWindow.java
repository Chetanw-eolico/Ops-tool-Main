package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.core.fineart.FineartUtils;

public class AddNewArtistWindow extends JFrame implements GenreFieldInterface {
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private JTextField txtArtistName;
	private JTextField txtNamePrefix;
	private JTextField txtNameSuffix;
	private JTextField txtBirthYear;
	private JTextField txtDeathYear;
	private JComboBox<String> comboBirthIdentifier;
	private JComboBox<String> comboBirthPrecision;
	private JComboBox<String> comboDeathIdentifier;
	private JComboBox<String> comboDeathPrecision;
	private JTextField txtAka;
	private JTextField txtNationality;
	private JTextField txtDescription;
	private String artistsIndexPath;
	private String artistId;
	private ArtistTableInterface artistTable;
	private JTextField genreTextField;
	private JTextField imageNameTextField;
	private JLabel artistImageLabel;
	private String lastBrowsedImagePath;
	private JTextArea bioTextArea;
	
	

	public AddNewArtistWindow(String username, String artistsIndexPath, JdbcTemplate jdbcTemplate, ArtistTableInterface artistTable) {
		this.username = username;
		this.artistsIndexPath = artistsIndexPath;
		this.jdbcTemplate = jdbcTemplate;
		this.artistTable = artistTable;
		initGUI();
		loadLastBrowsedImagePath();
	}

	private void loadLastBrowsedImagePath() {
		String sql = "select upload_images_path from operations_team where username = '" + username + "'";
		lastBrowsedImagePath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getString("upload_images_path").toLowerCase();
				} else {
					return "";
				}
			}
		});
	}
	
	private void initGUI() {
		this.setPreferredSize(new Dimension(300, 200));
		this.setSize(new Dimension(1320, 519));
		this.setTitle("Add New Artist");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, 5);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JPanel basePanel = new JPanel();
		getContentPane().add(basePanel);
		basePanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Artist Name:");
		lblNewLabel.setBounds(10, 11, 162, 14);
		basePanel.add(lblNewLabel);
		
		txtArtistName = new JTextField();
		txtArtistName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtArtistName.setBounds(182, 8, 292, 20);
		basePanel.add(txtArtistName);
		txtArtistName.setColumns(10);
		
		JLabel lblNamePrefix = new JLabel("Name Prefix:");
		lblNamePrefix.setBounds(10, 39, 162, 14);
		basePanel.add(lblNamePrefix);
		
		txtNamePrefix = new JTextField();
		txtNamePrefix.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtNamePrefix.setColumns(10);
		txtNamePrefix.setBounds(182, 36, 292, 20);
		basePanel.add(txtNamePrefix);
		
		JLabel lblNameSuffix = new JLabel("Name Suffix:");
		lblNameSuffix.setBounds(10, 67, 162, 14);
		basePanel.add(lblNameSuffix);
		
		txtNameSuffix = new JTextField();
		txtNameSuffix.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtNameSuffix.setColumns(10);
		txtNameSuffix.setBounds(182, 64, 292, 20);
		basePanel.add(txtNameSuffix);
		
		JLabel lblBirthYear = new JLabel("Birth Year:");
		lblBirthYear.setBounds(10, 95, 162, 14);
		basePanel.add(lblBirthYear);
		
		txtBirthYear = new JTextField();
		txtBirthYear.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtBirthYear.setColumns(10);
		txtBirthYear.setBounds(182, 92, 292, 20);
		basePanel.add(txtBirthYear);
		
		JLabel lblDeathYear = new JLabel("Death Year:");
		lblDeathYear.setBounds(10, 126, 162, 14);
		basePanel.add(lblDeathYear);
		
		txtDeathYear = new JTextField();
		txtDeathYear.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtDeathYear.setColumns(10);
		txtDeathYear.setBounds(182, 123, 292, 20);
		basePanel.add(txtDeathYear);
		
		JLabel lblBirthYearIdentifier = new JLabel("Birth Year Identifier:");
		lblBirthYearIdentifier.setBounds(10, 159, 162, 14);
		basePanel.add(lblBirthYearIdentifier);
		
		comboBirthIdentifier = new JComboBox<String>();
		comboBirthIdentifier.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBirthIdentifier.setModel(new DefaultComboBoxModel<String>(new String[] {"exact", "after", "before", "circa"}));
		comboBirthIdentifier.setBounds(182, 156, 292, 20);
		basePanel.add(comboBirthIdentifier);
		
		JLabel lblBirthYearPrecision = new JLabel("Birth Year Precision:");
		lblBirthYearPrecision.setBounds(10, 187, 162, 14);
		basePanel.add(lblBirthYearPrecision);
		
		comboBirthPrecision = new JComboBox<String>();
		comboBirthPrecision.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBirthPrecision.setModel(new DefaultComboBoxModel<String>(new String[] {"decade", "century", "millennial"}));
		comboBirthPrecision.setBounds(182, 184, 292, 20);
		basePanel.add(comboBirthPrecision);
		
		JLabel lblDeathYearIdentifier = new JLabel("Death Year Identifier:");
		lblDeathYearIdentifier.setBounds(10, 215, 162, 14);
		basePanel.add(lblDeathYearIdentifier);
		
		comboDeathIdentifier = new JComboBox<String>();
		comboDeathIdentifier.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboDeathIdentifier.setModel(new DefaultComboBoxModel<String>(new String[] {"exact", "after", "before", "circa"}));
		comboDeathIdentifier.setBounds(182, 212, 292, 20);
		basePanel.add(comboDeathIdentifier);
		
		JLabel lblDeathYearPrecision = new JLabel("Death Year Precision:");
		lblDeathYearPrecision.setBounds(10, 243, 162, 14);
		basePanel.add(lblDeathYearPrecision);
		
		comboDeathPrecision = new JComboBox<String>();
		comboDeathPrecision.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboDeathPrecision.setModel(new DefaultComboBoxModel<String>(new String[] {"decade", "century", "millennial"}));
		comboDeathPrecision.setBounds(182, 240, 292, 20);
		basePanel.add(comboDeathPrecision);
		
		JLabel lblArtistAka = new JLabel("Artist AKA:");
		lblArtistAka.setBounds(10, 271, 162, 14);
		basePanel.add(lblArtistAka);
		
		txtAka = new JTextField();
		txtAka.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtAka.setColumns(10);
		txtAka.setBounds(182, 268, 292, 20);
		basePanel.add(txtAka);
		
		JLabel lblNationality = new JLabel("Nationality:");
		lblNationality.setBounds(10, 299, 162, 14);
		basePanel.add(lblNationality);
		
		txtNationality = new JTextField();
		txtNationality.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtNationality.setColumns(10);
		txtNationality.setBounds(182, 296, 292, 20);
		basePanel.add(txtNationality);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 328, 162, 14);
		basePanel.add(lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtDescription.setColumns(10);
		txtDescription.setBounds(182, 325, 292, 20);
		basePanel.add(txtDescription);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveArtist();
			}
		});
		btnSave.setMnemonic('S');
		btnSave.setBounds(521, 381, 170, 49);
		basePanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(715, 381, 170, 49);
		basePanel.add(btnCancel);
		
		JLabel lblBio = new JLabel("Artist Bio:");
		lblBio.setBounds(484, 11, 116, 14);
		basePanel.add(lblBio);
		
		JScrollPane bioScrollPane = new JScrollPane();
		bioScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		bioScrollPane.setBounds(484, 39, 463, 303);
		basePanel.add(bioScrollPane);
		
		bioTextArea = new JTextArea();
		bioTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		bioTextArea.setBounds(0, 0, 4, 22);
		bioScrollPane.add(bioTextArea);
		bioScrollPane.setViewportView(bioTextArea);
		bioTextArea.setLineWrap(true);
		bioTextArea.setWrapStyleWord(true);
		
		JLabel lblGenre = new JLabel("Genre:");
		lblGenre.setBounds(10, 353, 71, 14);
		basePanel.add(lblGenre);
		
		genreTextField = new JTextField();
		genreTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genreTextField.setEditable(false);
		genreTextField.setBounds(182, 350, 703, 20);
		basePanel.add(genreTextField);
		genreTextField.setColumns(10);
		
		JButton addGenreButton = new JButton("+");
		addGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addGenreButtonActionPerformed();
			}
		});
		addGenreButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		addGenreButton.setBounds(895, 347, 52, 23);
		basePanel.add(addGenreButton);
		
		JLabel lblImageName = new JLabel("Image Name:");
		lblImageName.setBounds(610, 11, 104, 14);
		basePanel.add(lblImageName);
		
		imageNameTextField = new JTextField();
		imageNameTextField.setEditable(false);
		imageNameTextField.setBounds(692, 12, 503, 20);
		basePanel.add(imageNameTextField);
		imageNameTextField.setColumns(10);
		
		JButton updateImageButton = new JButton("Update");
		updateImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageUpdateButtonActionPerformed();
			}
		});
		updateImageButton.setBounds(1205, 11, 89, 23);
		basePanel.add(updateImageButton);
		
		artistImageLabel = new JLabel("");
		artistImageLabel.setBounds(957, 39, 337, 303);
		basePanel.add(artistImageLabel);
	}
	
	protected void addGenreButtonActionPerformed() {
		try {
			GenreSearchWindow genreSearchWindow = new GenreSearchWindow(jdbcTemplate, username, this);
			genreSearchWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void imageUpdateButtonActionPerformed() {
		try {
			
			JFileChooser jfc = new JFileChooser(lastBrowsedImagePath);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			int returnVal = jfc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String parentPath = jfc.getSelectedFile().getParent().replaceAll("\\\\", "//");
				lastBrowsedImagePath = parentPath;
				String insertSql = "UPDATE operations_team SET upload_images_path='" + parentPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
				
				File imageFile = new File(outputPath);
				
				Image image = ImageIO.read(imageFile).getScaledInstance(artistImageLabel.getWidth(), artistImageLabel.getHeight(),Image.SCALE_SMOOTH);
				ImageIcon imgThisImg = new ImageIcon(image);
				artistImageLabel.setIcon(imgThisImg);
				
				imageNameTextField.setText(imageFile.getName());
				//String imageName = imageFile.getName();
						
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	protected void saveArtist() {
		IndexWriter writer = null;
		try {
			String query = "insert into fineart_artists (fa_artist_name, fa_artist_name_prefix, fa_artist_name_suffix, fa_artist_birth_year, fa_artist_death_year, "
					+ "fa_artist_birth_year_identifier, fa_artist_birth_year_precision, fa_artist_death_year_identifier, fa_artist_death_year_precision, "
					+ "fa_artist_aka, fa_artist_nationality, fa_artist_description, fa_artist_bio, fa_artist_genre, fa_artist_image, "
					+ "fa_artist_record_created, fa_artist_record_updated, fa_arist_record_createdby, "
					+ "fa_artist_record_updatedby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			String artistName = FineartUtils.getSqlSafeString(txtArtistName.getText().trim().trim().equals("") ? "na" : txtArtistName.getText().trim());
			String artistNamePrefix = FineartUtils.getSqlSafeString(txtNamePrefix.getText().trim().equals("") ? "na" : txtNamePrefix.getText().trim());
			String artistNameSuffix = FineartUtils.getSqlSafeString(txtNameSuffix.getText().trim().equals("") ? "na" : txtNameSuffix.getText().trim());
			String artistDob = txtBirthYear.getText().trim().equals("") ? "0" : txtBirthYear.getText().trim();
			String artistDod = txtDeathYear.getText().trim().equals("") ? "0" : txtDeathYear.getText().trim();
			String artistBI = comboBirthIdentifier.getSelectedItem().toString();
			String artistDi = comboDeathIdentifier.getSelectedItem().toString();
			String artistBP = comboBirthPrecision.getSelectedItem().toString();
			String artistDP = comboDeathPrecision.getSelectedItem().toString();
			String artistNationality = FineartUtils.getSqlSafeString(txtNationality.getText().trim().equals("") ? "na" : txtNationality.getText().trim());
			String artistAka = FineartUtils.getSqlSafeString(txtAka.getText().trim().equals("") ? "na" : txtAka.getText().trim());
			String artistDecription = FineartUtils.getSqlSafeString(txtDescription.getText().trim().equals("") ? null : txtDescription.getText().trim());
			
			String artistBio = FineartUtils.getSqlSafeString(bioTextArea.getText().trim().equals("") ? null : bioTextArea.getText().trim());
			String artistGenre = FineartUtils.getSqlSafeString(genreTextField.getText().trim().equals("") ? null : genreTextField.getText().trim());
			String artistImage = FineartUtils.getSqlSafeString(imageNameTextField.getText().trim().equals("") ? null : imageNameTextField.getText().trim());
			
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
					ps.setString(7, artistBP);
					ps.setString(8, artistDi);
					ps.setString(9, artistDP);
					ps.setString(10, artistAka);
					ps.setString(11, artistNationality);
					ps.setString(12, artistDecription);
					ps.setString(13, artistBio);
					ps.setString(14, artistGenre);
					ps.setString(15, artistImage);
					ps.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
					ps.setString(17, null);
					ps.setString(18, username);
					ps.setString(19, null);
							
					return ps;
				}
			}, holder);
			
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
            
           // artistId, artistName, nationality, birthYear, deathYear, artistAKA
            
            ((DefaultTableModel) artistTable.getTable().getModel()).insertRow(0, new Object[]{String.valueOf(holder.getKeys().get("GENERATED_KEY").toString()), 
            		artistName, artistNationality, artistDob, artistDod, artistAka});
            artistTable.getTable().setRowSelectionInterval(0, 0);
            artistTable.getTable().requestFocus();
            
            writer.commit();
            writer.close();
            
            this.setVisible(false);
            this.dispose();
            
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
	
	@Override
	public JTextField getGenreField() {
		return genreTextField;
	}
}
