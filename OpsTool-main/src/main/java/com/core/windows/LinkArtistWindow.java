package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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
import org.springframework.jdbc.core.JdbcTemplate;

public class LinkArtistWindow extends JFrame implements ArtistTableInterface {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private LotDetailsWindow lotDetailsWindow;
	private JScrollPane tableScrollPane;
	private JTable table;
	DefaultTableModel tableModel;
	ListSelectionModel cellSelectionModel;
	private JPanel descriptionPanel;
	private JPanel controlPanel;
	private TableRowSorter<TableModel> sorter;
	private JLabel lblSearch;
	private JTextField searchTextField;
	private JButton btnViewLot;
	private JScrollPane scrollPane;
	private JTextArea descriptionTextArea;
	private JButton btnAddArtist;
	private String artistsIndexPath;
	private JButton btnCancel;
	private JButton btnEditArtist;
	
	public LinkArtistWindow(LotDetailsWindow lotDetailsWindow, JdbcTemplate jdbcTemplate, String username, String artistsIndexPath) {
		this.lotDetailsWindow = lotDetailsWindow;
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		this.artistsIndexPath = artistsIndexPath;
		initGUI();
	}
	
	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1300, 650));
		this.setSize(new Dimension(1300, 650));
		this.setTitle("Artist Master");
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
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		
		tableScrollPane = new JScrollPane();
		tableScrollPane.setPreferredSize(new Dimension(0, 500));
		tableScrollPane.setViewportBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		basePanel.add(tableScrollPane);
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tableScrollPane.add(table);
		
		descriptionPanel = new JPanel();
		descriptionPanel.setPreferredSize(new Dimension(0, 100));
		basePanel.add(descriptionPanel);
		descriptionPanel.setLayout(new BorderLayout(0, 0));
		
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setText(lotDetailsWindow.getDescriptionText());
		descriptionTextArea.setCaretPosition(0);
		
		scrollPane = new JScrollPane();
		scrollPane.add(descriptionTextArea);
		scrollPane.setViewportView(descriptionTextArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionPanel.add(scrollPane);
		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(0, 50));
		basePanel.add(controlPanel);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSearch = new JLabel("Search:");
		controlPanel.add(lblSearch);
		
		searchTextField = new JTextField();
		searchTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchTextField.setPreferredSize(new Dimension(200, 30));
		controlPanel.add(searchTextField);
		
		btnViewLot = new JButton("Link Selected");
		btnViewLot.setMnemonic('L');
		btnViewLot.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnViewLot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				linkArtist();
			}
		});
		controlPanel.add(btnViewLot);
		btnViewLot.setPreferredSize(new Dimension(150, 40));
		
		btnAddArtist = new JButton("Add Artist");
		btnAddArtist.setMnemonic('A');
		btnAddArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewArtist();
			}
		});
		btnAddArtist.setPreferredSize(new Dimension(150, 40));
		btnAddArtist.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnAddArtist);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		btnEditArtist = new JButton("Edit Selected");
		btnEditArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSelectedArtist();
			}
		});
		btnEditArtist.setPreferredSize(new Dimension(150, 40));
		btnEditArtist.setMnemonic('E');
		btnEditArtist.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnEditArtist);
		btnCancel.setPreferredSize(new Dimension(150, 40));
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnCancel);
		
		{
			tableModel = new DefaultTableModel();
			table = new JTable(tableModel) {
	            private static final long serialVersionUID = 1L;
				public boolean getScrollableTracksViewportWidth() {
	                return getPreferredSize().width < getParent().getWidth();
	            }
	        };
			table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			table.setModel(tableModel);
			table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
			tableScrollPane.add(table);
			tableScrollPane.setViewportView(table);
			
			sorter = new TableRowSorter<>(tableModel);
			table.setRowSorter(sorter);
			table.setDefaultEditor(Object.class, null);
		}
		{
			cellSelectionModel = table.getSelectionModel();
		    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		    cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {	// table row selection
					tableRowSelectionChanged();
				}
			});
		}
		{
			tableModel.addColumn("Artist ID");
			tableModel.addColumn("Artist Name");
			tableModel.addColumn("Nationality");
			tableModel.addColumn("Birth Year");
			tableModel.addColumn("Death Year");
			tableModel.addColumn("Artist AKA");
		}
		{
			JTableHeader header = table.getTableHeader();
			header.setPreferredSize(new Dimension(0, 50));
			header.setFont(new java.awt.Font("Segoe UI",1,15));
		}
		{
			searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			 @Override
	         public void insertUpdate(DocumentEvent e) {
				 loadTableData(searchTextField.getText());
	         }
	         @Override
	         public void removeUpdate(DocumentEvent e) {
	        	 loadTableData(searchTextField.getText());
	         }
	         @Override
	         public void changedUpdate(DocumentEvent e) {
	        	 loadTableData(searchTextField.getText());
	         }
	         
	      });
		}
	}
	
	protected void editSelectedArtist() {
		LinkArtistWindow linkArtistWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditArtistWindow frame = new EditArtistWindow(username, artistsIndexPath, jdbcTemplate, linkArtistWindow);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void addNewArtist() {
		LinkArtistWindow linkArtistWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNewArtistWindow frame = new AddNewArtistWindow(username, artistsIndexPath, jdbcTemplate, linkArtistWindow);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void linkArtist() {
		try {
			String artistId = table.getValueAt(table.getSelectedRow(), 0).toString();
			String artistName = table.getValueAt(table.getSelectedRow(), 1).toString();
			String nationality = table.getValueAt(table.getSelectedRow(), 2).toString();
			String birthYear = table.getValueAt(table.getSelectedRow(), 3).toString();
			String deathYear = table.getValueAt(table.getSelectedRow(), 4).toString();
			String artistAKA = table.getValueAt(table.getSelectedRow(), 5).toString();
			
			String artistDetails = "";
			if(!artistAKA.equals("na")) {
				artistDetails = artistName + " " + "(" + artistAKA + ")" + nationality + "-" + birthYear + "-" + deathYear;
			} else {
				artistDetails = artistName + "-" + nationality + "-" + birthYear + "-" + deathYear;
			}
			lotDetailsWindow.setArtistID(artistId);
			lotDetailsWindow.setArtistName(artistDetails);
			this.setVisible(false);
			this.dispose();
		} catch (java.lang.IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void tableRowSelectionChanged() {
		try {
			
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadTableData(String keywords) {
		try {
			int rowCount = tableModel.getRowCount();
			for (int i = rowCount - 1; i >= 0; i--) {
				tableModel.removeRow(i);
			}
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
			Directory directory = null;
			
			directory = FSDirectory.open(new File(artistsIndexPath));
			
			keywords = keywords.trim() + "*";
			
			String [] fields = {"fa_artist_name","fa_artist_aka"};
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer);
			queryParser.setDefaultOperator(QueryParser.Operator.AND);
			
			Query query = queryParser.parse(keywords);
			
			int hitsPerPage = 100;
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			for(int i=0; i<hits.length; ++i) {
			    int docId = hits[i].doc;
			    org.apache.lucene.document.Document document = searcher.doc(docId);
			    String artistId = document.get("fa_artist_ID");
			    String artistName = document.get("fa_artist_name");
			    String nationality = document.get("fa_artist_nationality");
			    String birthYear = document.get("fa_artist_birth_year");
			    String deathYear = document.get("fa_artist_death_year");
			    String artistAKA = document.get("fa_artist_aka");
			    tableModel.addRow(new Object[]{artistId, artistName, nationality, birthYear, deathYear, artistAKA});
			}
			table.getColumn("Artist Name").setPreferredWidth(200);
			table.setRowSelectionInterval(0, 0);
		} catch (java.lang.IllegalArgumentException e) {
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
}
