package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

public class ArtistMergeWindow extends JFrame implements ArtistTableInterface {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	
	private JScrollPane primaryTableScrollPane;
	private JTable primaryTable;
	DefaultTableModel primaryTableModel;
	ListSelectionModel primaryCellSelectionModel;
	private JPanel primaryControlPanel;
	private TableRowSorter<TableModel> primaryTableRowSorter;
	private JLabel lblSearch1;
	private JTextField searchTextField1;
	private String artistsIndexPath;
	private JButton btnCancel;
	private JButton btnMergeArtist;
	private JButton btnViewLotCount1;
	
	private JScrollPane secondaryTableScrollPane;
	private JTable secondaryTable;
	DefaultTableModel secondaryTableModel;
	ListSelectionModel secondaryCellSelectionModel;
	private JPanel secondaryControlPanel;
	private TableRowSorter<TableModel> secondaryTableRowSorter;
	private JLabel lblSearch2;
	private JTextField searchTextField2;
	
	private JButton btnViewArtistDetails1;
	private JButton btnViewArtistDetails2;
	
	private JPanel mainControlPanel;
	private JButton btnViewLotCount2;
	
	public ArtistMergeWindow(JdbcTemplate jdbcTemplate, String username, String artistsIndexPath) {
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		this.artistsIndexPath = artistsIndexPath;
		initGUI();
	}
	
	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1300, 650));
		this.setSize(new Dimension(1300, 650));
		this.setTitle("Artist Merge");
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
		
		JPanel basePanel1 = new JPanel();
		basePanel.add(basePanel1);
		basePanel1.setLayout(new BoxLayout(basePanel1, BoxLayout.Y_AXIS));
		
		JPanel basePanel2 = new JPanel();
		basePanel.add(basePanel2);
		basePanel2.setLayout(new BoxLayout(basePanel2, BoxLayout.Y_AXIS));
		
		primaryTableScrollPane = new JScrollPane();
		primaryTableScrollPane.setPreferredSize(new Dimension(0, 100));
		primaryTableScrollPane.setViewportBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		primaryTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		basePanel1.add(primaryTableScrollPane);
		
		secondaryTableScrollPane = new JScrollPane();
		secondaryTableScrollPane.setPreferredSize(new Dimension(0, 200));
		secondaryTableScrollPane.setViewportBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		secondaryTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		basePanel2.add(secondaryTableScrollPane);
		
		primaryControlPanel = new JPanel();
		primaryControlPanel.setPreferredSize(new Dimension(0, 1));
		basePanel1.add(primaryControlPanel);
		primaryControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		secondaryControlPanel = new JPanel();
		secondaryControlPanel.setPreferredSize(new Dimension(0, 1));
		basePanel2.add(secondaryControlPanel);
		secondaryControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		mainControlPanel = new JPanel();
		mainControlPanel.setPreferredSize(new Dimension(0, 50));
		basePanel.add(mainControlPanel);
		mainControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSearch1 = new JLabel("Search:");
		primaryControlPanel.add(lblSearch1);
		
		lblSearch2 = new JLabel("Search:");
		secondaryControlPanel.add(lblSearch2);
		
		searchTextField1 = new JTextField();
		searchTextField1.setPreferredSize(new Dimension(200, 30));
		primaryControlPanel.add(searchTextField1);
		
		searchTextField2 = new JTextField();
		searchTextField2.setPreferredSize(new Dimension(200, 30));
		secondaryControlPanel.add(searchTextField2);
		
		{
			btnViewLotCount1 = new JButton("View Lot Count");
			btnViewLotCount1.setMnemonic('V');
			btnViewLotCount1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewLotCountPrimary();
				}
			});
			btnViewLotCount1.setPreferredSize(new Dimension(150, 40));
			btnViewLotCount1.setFont(new Font("Tahoma", Font.BOLD, 12));
			primaryControlPanel.add(btnViewLotCount1);
		}
		
		{
			btnViewLotCount2 = new JButton("View Lot Count");
			btnViewLotCount2.setMnemonic('L');
			btnViewLotCount2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewLotCountSeondary();
				}
			});
			btnViewLotCount2.setPreferredSize(new Dimension(150, 40));
			btnViewLotCount2.setFont(new Font("Tahoma", Font.BOLD, 12));
			secondaryControlPanel.add(btnViewLotCount2);
		}
		
		{
			btnViewArtistDetails1 = new JButton("View Details");
			btnViewArtistDetails1.setMnemonic('D');
			btnViewArtistDetails1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewArtistDetails(primaryTable);
				}
			});
			btnViewArtistDetails1.setPreferredSize(new Dimension(150, 40));
			btnViewArtistDetails1.setFont(new Font("Tahoma", Font.BOLD, 12));
			primaryControlPanel.add(btnViewArtistDetails1);
		}
		
		{
			btnViewArtistDetails2 = new JButton("View Details");
			btnViewArtistDetails2.setMnemonic('S');
			btnViewArtistDetails2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					viewArtistDetails(secondaryTable);
				}
			});
			btnViewArtistDetails2.setPreferredSize(new Dimension(150, 40));
			btnViewArtistDetails2.setFont(new Font("Tahoma", Font.BOLD, 12));
			secondaryControlPanel.add(btnViewArtistDetails2);
		}
		
		btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.setPreferredSize(new Dimension(150, 40));
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		mainControlPanel.add(btnCancel);
		
		btnMergeArtist = new JButton("Artist Merge");
		btnMergeArtist.setMnemonic('M');
		btnMergeArtist.setPreferredSize(new Dimension(150, 40));
		btnMergeArtist.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnMergeArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnMergeArtistActionPerformed();
			}
		});
		mainControlPanel.add(btnMergeArtist);
		
		{
			primaryTableModel = new DefaultTableModel();
			primaryTable = new JTable(primaryTableModel) {
	            private static final long serialVersionUID = 1L;
				public boolean getScrollableTracksViewportWidth() {
	                return getPreferredSize().width < getParent().getWidth();
	            }
	        };
			primaryTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
			primaryTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			primaryTable.setModel(primaryTableModel);
			primaryTable.setFont(new java.awt.Font("Segoe UI",0,12));
			primaryTableScrollPane.add(primaryTable);
			primaryTableScrollPane.setViewportView(primaryTable);
			
			primaryTableRowSorter = new TableRowSorter<>(primaryTableModel);
			primaryTable.setRowSorter(primaryTableRowSorter);
			primaryTable.setDefaultEditor(Object.class, null);
		}
		
		{
			secondaryTableModel = new DefaultTableModel();
			secondaryTable = new JTable(secondaryTableModel) {
	            private static final long serialVersionUID = 1L;
				public boolean getScrollableTracksViewportWidth() {
	                return getPreferredSize().width < getParent().getWidth();
	            }
	        };
	        secondaryTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
	        secondaryTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
	        secondaryTable.setModel(secondaryTableModel);
	        secondaryTable.setFont(new java.awt.Font("Segoe UI",0,12));
			secondaryTableScrollPane.add(secondaryTable);
			secondaryTableScrollPane.setViewportView(secondaryTable);
			
			secondaryTableRowSorter = new TableRowSorter<>(secondaryTableModel);
			secondaryTable.setRowSorter(secondaryTableRowSorter);
			secondaryTable.setDefaultEditor(Object.class, null);
		}
		
		
		{
			primaryCellSelectionModel = primaryTable.getSelectionModel();
		    primaryCellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		    primaryCellSelectionModel.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {	// table row selection
					tableRowSelectionChanged();
				}
			});
		}
		
		{
			secondaryCellSelectionModel = secondaryTable.getSelectionModel();
			secondaryCellSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			secondaryCellSelectionModel.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {	// table row selection
					tableRowSelectionChanged();
				}
			});
		}
		
		
		{
			primaryTableModel.addColumn("Artist ID");
			primaryTableModel.addColumn("Artist Name");
			primaryTableModel.addColumn("Nationality");
			primaryTableModel.addColumn("Birth Year");
			primaryTableModel.addColumn("Death Year");
			primaryTableModel.addColumn("Artist AKA");
		}
		
		{
			secondaryTableModel.addColumn("Artist ID");
			secondaryTableModel.addColumn("Artist Name");
			secondaryTableModel.addColumn("Nationality");
			secondaryTableModel.addColumn("Birth Year");
			secondaryTableModel.addColumn("Death Year");
			secondaryTableModel.addColumn("Artist AKA");
		}
		
		{
			JTableHeader header1 = primaryTable.getTableHeader();
			header1.setPreferredSize(new Dimension(0, 50));
			header1.setFont(new java.awt.Font("Segoe UI",1,15));
		}
		
		{
			JTableHeader header2 = secondaryTable.getTableHeader();
			header2.setPreferredSize(new Dimension(0, 50));
			header2.setFont(new java.awt.Font("Segoe UI",1,15));
		}
		
		{
			searchTextField1.getDocument().addDocumentListener(new DocumentListener() {
			 @Override
	         public void insertUpdate(DocumentEvent e) {
				 loadTableData(searchTextField1.getText(), primaryTable, primaryTableModel);
	         }
	         @Override
	         public void removeUpdate(DocumentEvent e) {
	        	 loadTableData(searchTextField1.getText(), primaryTable, primaryTableModel);
	         }
	         @Override
	         public void changedUpdate(DocumentEvent e) {
	        	 loadTableData(searchTextField1.getText(), primaryTable, primaryTableModel);
	         }
	         
	      });
		}
		
		{
			searchTextField2.getDocument().addDocumentListener(new DocumentListener() {
			 @Override
	         public void insertUpdate(DocumentEvent e) {
				 loadTableData(searchTextField2.getText(), secondaryTable, secondaryTableModel);
	         }
	         @Override
	         public void removeUpdate(DocumentEvent e) {
	        	 loadTableData(searchTextField2.getText(), secondaryTable, secondaryTableModel);
	         }
	         @Override
	         public void changedUpdate(DocumentEvent e) {
	        	 loadTableData(searchTextField2.getText(), secondaryTable, secondaryTableModel);
	         }
	         
	      });
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void btnMergeArtistActionPerformed() {
		try {
			int primaryTableSelectedRow = primaryTable.getSelectedRow();
			int secondaryTableSelectedRow = secondaryTable.getSelectedRow();
			if(primaryTableSelectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select the Primary artist");
				return;
			} else if(secondaryTableSelectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Please select the Secondary artist(s)");
				return;
			}
			
			String primaryArtistId = primaryTable.getValueAt(primaryTable.getSelectedRow(), 0).toString();
			String primaryArtistName = primaryTable.getValueAt(primaryTable.getSelectedRow(), 1).toString();
			
			List<String> secondaryArtistIds = new ArrayList<String>();
			List<String> secondaryArtistNames = new ArrayList<String>();
			int[] secondaryTableSelectedRows = secondaryTable.getSelectedRows();
			for(int selectedRow : secondaryTableSelectedRows) {
				secondaryArtistIds.add(secondaryTable.getValueAt(selectedRow, 0).toString());
				secondaryArtistNames.add(secondaryTable.getValueAt(selectedRow, 1).toString());
			}
			
			String secondaryArtistIdsStr = "";
			String secondaryArtistIdsAndNames = "";
			for(int i = 0; i < secondaryArtistIds.size(); i++) {
				if(primaryArtistId.equals(secondaryArtistIds.get(i))) {
					JOptionPane.showMessageDialog(this, "Secondary artist cannot be the same as primary artist.", "Merge Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				secondaryArtistIdsStr = secondaryArtistIds.get(i) + "," + secondaryArtistIdsStr;
				secondaryArtistIdsAndNames = secondaryArtistIds.get(i) + " - " + secondaryArtistNames.get(i) + "\n" + secondaryArtistIdsAndNames;
			}
			
			if(secondaryArtistIdsStr.length() > 1) {
				secondaryArtistIdsStr = secondaryArtistIdsStr.substring(0, secondaryArtistIdsStr.length() -1);
			}
			
			String ObjButtons[] = {"Yes","No"};
	        int PromptResult = JOptionPane.showOptionDialog(null, "Primary Artist:\n" + primaryArtistId + " - " + primaryArtistName + "\n\nSecondary Artists:\n" 
	        		+ secondaryArtistIdsAndNames + "\nAre you sure to merge?\n " , "Artists Merge Confirmation", 
	        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
	        
	        if(PromptResult==JOptionPane.YES_OPTION) {
	        	String updateQuery = "UPDATE fineart_artworks SET faa_artist_ID = " + primaryArtistId 
	        			+ ", faa_artwork_record_updatedby = '" + username + "', faa_artwork_record_updated = CURRENT_TIME() "
	        					+ "WHERE faa_artist_ID IN (" + secondaryArtistIdsStr + ")";
	        	
	        	jdbcTemplate.update(updateQuery);
	        	
	        	String deleteQuery = "DELETE FROM fineart_artists WHERE fa_artist_ID IN (" + secondaryArtistIdsStr + ")";
	        	
	        	jdbcTemplate.update(deleteQuery);
	        	
	        	File file = new File(artistsIndexPath);
				StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		        IndexWriter writer = new IndexWriter(FSDirectory.open(file), config);
		        
		        for(String secondaryArtistId : secondaryArtistIds) {
		        	String [] fields = {"fa_artist_ID"};
			        Query luceneQuery = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer).parse("fa_artist_ID:" + secondaryArtistId);
			        writer.deleteDocuments(luceneQuery);
		        }
		        writer.close();
		        
	        	try {
	    			int rowCount1 = primaryTableModel.getRowCount();
	    			for (int i = rowCount1 - 1; i >= 0; i--) {
	    				primaryTableModel.removeRow(i);
	    			}
	    			loadTableData(searchTextField1.getText(), primaryTable, primaryTableModel);
	    			
	    			int rowCount2 = secondaryTableModel.getRowCount();
	    			for (int i = rowCount2 - 1; i >= 0; i--) {
	    				secondaryTableModel.removeRow(i);
	    			}
	    			loadTableData(searchTextField2.getText(), secondaryTable, secondaryTableModel);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	        	
	        } else {
	        	return;
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void viewArtistDetails(JTable table) {
		try {
			int selectedRow = table.getSelectedRow();
			if(selectedRow == -1) {
				return;
			}
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						EditArtistWindow frame = new EditArtistWindow(username, artistsIndexPath, jdbcTemplate, table);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void viewLotCountPrimary() {
		try {
			String artistId = primaryTable.getValueAt(primaryTable.getSelectedRow(), 0).toString();
			String artistName = primaryTable.getValueAt(primaryTable.getSelectedRow(), 1).toString();
			
			String sqlQuery = "SELECT count(*) as lot_count FROM fineart_artworks where faa_artist_ID = " + artistId;
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						String lotCount = resultSet.getString("lot_count");
						JOptionPane.showMessageDialog(null, artistName + ": " + lotCount, artistName, JOptionPane.INFORMATION_MESSAGE);
					}
					return true;
				}
			});
			
		} catch (java.lang.IndexOutOfBoundsException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void viewLotCountSeondary() {
		try {
			String artistId = secondaryTable.getValueAt(secondaryTable.getSelectedRow(), 0).toString();
			String artistName = secondaryTable.getValueAt(secondaryTable.getSelectedRow(), 1).toString();
			
			String sqlQuery = "SELECT count(*) as lot_count FROM fineart_artworks where faa_artist_ID = " + artistId;
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						String lotCount = resultSet.getString("lot_count");
						JOptionPane.showMessageDialog(null, artistName + ": " + lotCount, artistName, JOptionPane.INFORMATION_MESSAGE);
					}
					return true;
				}
			});
			
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
	public void loadTableData(String keywords, JTable table, DefaultTableModel tableModel) {
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
		return primaryTable;
	}

	public void setTable(JTable table) {
		this.primaryTable = table;
	}
}
