package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.pojo.ViewSaleRecord;
import java.awt.Font;

public class ViewSaleWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private int auctionId;
	private JScrollPane tableScrollPane;
	private JTable table;
	DefaultTableModel tableModel;
	ListSelectionModel cellSelectionModel;
	private JPanel controlPanel;
	private TableRowSorter<TableModel> sorter;
	private JLabel lblSearch;
	private JTextField searchTextField;
	private JButton btnViewLot;
	private String artistIndexPath;
	
	public ViewSaleWindow(int auctionId, String username, String artistsIndexPath, JdbcTemplate jdbcTemplate, MainWindow mainWindow) {
		this.auctionId = auctionId;
		this.username = username;
		this.artistIndexPath = artistsIndexPath;
		this.jdbcTemplate = jdbcTemplate;
		initGUI();
		loadTableData();
	}
	
	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1300, 650));
		this.setSize(new Dimension(1300, 650));
		this.setTitle("Sale Details");
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
		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(0, 40));
		basePanel.add(controlPanel);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSearch = new JLabel("Search:");
		controlPanel.add(lblSearch);
		
		searchTextField = new JTextField();
		searchTextField.setPreferredSize(new Dimension(200, 30));
		controlPanel.add(searchTextField);
		
		btnViewLot = new JButton("View Lot");
		btnViewLot.setMnemonic('V');
		btnViewLot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openLotDetails();
			}
		});
		controlPanel.add(btnViewLot);
		btnViewLot.setPreferredSize(new Dimension(100, 40));
		
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
			tableModel.addColumn("Lot ID");
			tableModel.addColumn("Lot No.");
			tableModel.addColumn("Artwork Name");
			tableModel.addColumn("Artist");
			tableModel.addColumn("Image Count");
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
	            search(searchTextField.getText());
	         }
	         @Override
	         public void removeUpdate(DocumentEvent e) {
	            search(searchTextField.getText());
	         }
	         @Override
	         public void changedUpdate(DocumentEvent e) {
	            search(searchTextField.getText());
	         }
	         public void search(String str) {
	            if (str.length() == 0) {
	               sorter.setRowFilter(null);
	            } else {
	            	sorter.setRowFilter(RowFilter.regexFilter(str));
	            }
	         }
	      });
		}
	}
	
	protected void openLotDetails() {
		ViewSaleWindow viewSaleWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int lotId = (Integer)table.getValueAt(table.getSelectedRow(), 0);
					LotDetailsWindow frame = new LotDetailsWindow(lotId, auctionId, username, artistIndexPath, jdbcTemplate, viewSaleWindow);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void tableRowSelectionChanged() {
		try {
			
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadTableData() {
		int rowCount = tableModel.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
		String loadArtworkQuery = "SELECT  fineart_lots.fal_lot_ID, fineart_lots.fal_lot_no, fineart_artworks.faa_artwork_title,  fineart_artists.fa_artist_name, "
				+ "((fineart_lots.fal_lot_image1 != '' AND fineart_lots.fal_lot_image1 != 'na') + (fineart_lots.fal_lot_image2 != '' AND fineart_lots.fal_lot_image2 != 'na')"
				+ " + (fineart_lots.fal_lot_image3 != '' AND fineart_lots.fal_lot_image3 != 'na') + (fineart_lots.fal_lot_image4 != '' AND "
				+ "fineart_lots.fal_lot_image4 != 'na') "
				+ "+ (fineart_lots.fal_lot_image5 != '' AND fineart_lots.fal_lot_image5 != 'na')) as image_count FROM  fineart_artworks INNER JOIN  fineart_lots ON "
				+ "fineart_artworks.faa_artwork_ID = fineart_lots.fal_artwork_ID INNER JOIN  fineart_artists ON "
				+ "fineart_artworks.faa_artist_ID = fineart_artists.fa_artist_ID WHERE fineart_lots.fal_auction_ID = '" + auctionId + "'";
		
		List<ViewSaleRecord> viewSaleRecordList = new ArrayList<>();
		
		jdbcTemplate.query(loadArtworkQuery, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				while (resultSet.next()) {
					ViewSaleRecord viewSaleRecord = new ViewSaleRecord();
					viewSaleRecord.setLotID(resultSet.getInt("fal_lot_ID"));
					viewSaleRecord.setLotNumber(resultSet.getInt("fal_lot_no"));
					viewSaleRecord.setArtworkName(resultSet.getString("faa_artwork_title"));
					viewSaleRecord.setArtistName(resultSet.getString("fa_artist_name"));
					viewSaleRecord.setImageCount(resultSet.getInt("image_count"));
					viewSaleRecordList.add(viewSaleRecord);
				}
				return true;
			}
		});
		
		for(ViewSaleRecord viewSaleRecord : viewSaleRecordList) {
			tableModel.addRow(new Object[]{viewSaleRecord.getLotID(), viewSaleRecord.getLotNumber(), viewSaleRecord.getArtworkName(), viewSaleRecord.getArtistName(), 
					viewSaleRecord.getImageCount()});
		}
		table.getColumn("Artwork Name").setPreferredWidth(500);
		table.getColumn("Artist").setPreferredWidth(400);
		table.getColumn("Image Count").setCellRenderer(new ImageCountZeroCellRenderer());
		table.getColumn("Artist").setCellRenderer(new MissingArtistCellRenderer());
		table.setRowSelectionInterval(0, 0);
	}
}

class ImageCountZeroCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if ((Integer)value == 0 ) {
            c.setBackground( Color.orange );
            c.setForeground(Color.black );
        } else {
        	c.setBackground( Color.white );
            c.setForeground(Color.black );
        }
        return c;
    }
}

class MissingArtistCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value.toString().equals("Missing")) {
            c.setBackground( Color.orange );
            c.setForeground(Color.black );
        } else {
        	c.setBackground( Color.white );
            c.setForeground(Color.black );
        }
        return c;
    }
}
