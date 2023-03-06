package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javax.swing.JTextArea;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.pojo.SaleSummaryRecord;

public class AuctionHouseSearchWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private JScrollPane tableScrollPane;
	private JTable table;
	DefaultTableModel tableModel;
	ListSelectionModel cellSelectionModel;
	private JPanel controlPanel;
	private TableRowSorter<TableModel> sorter;
	private JLabel lblSearch;
	private JTextField searchTextField;
	private JButton btnAddAuctionHouse;
	private JButton btnRefreshTable;
	private JButton btnEditAuctionHouse;
	private JButton btnViewLotCount;
	
	public AuctionHouseSearchWindow(JdbcTemplate jdbcTemplate, String username) {
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		initGUI();
		loadTableData();
	}
	
	private void loadTableData() {
		try {
			String sqlQuery = "SELECT * from core_auction_houses";
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					while (resultSet.next()) {
						tableModel.addRow(new Object[]{resultSet.getInt("cah_auction_house_ID"), resultSet.getString("cah_auction_house_name"), 
								resultSet.getString("cah_auction_house_country"), resultSet.getString("cah_auction_house_location"), 
								resultSet.getString("cah_auction_house_currency_code")});
						
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		table.getColumn("ID").setPreferredWidth(50);
		table.getColumn("Name").setPreferredWidth(200);
		table.getColumn("Country").setPreferredWidth(200);
		table.getColumn("Location").setPreferredWidth(200);
		table.getColumn("Currency Code").setPreferredWidth(200);
	}

	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1300, 650));
		this.setSize(new Dimension(1300, 650));
		this.setTitle("Auction House Master");
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
		controlPanel.setPreferredSize(new Dimension(0, 50));
		basePanel.add(controlPanel);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSearch = new JLabel("Search:");
		controlPanel.add(lblSearch);
		
		searchTextField = new JTextField();
		searchTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchTextField.setPreferredSize(new Dimension(200, 30));
		controlPanel.add(searchTextField);
		
		btnAddAuctionHouse = new JButton("Add Auction House");
		btnAddAuctionHouse.setMnemonic('A');
		btnAddAuctionHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddAuctionHouseActionPerformed();
			}
		});
		
		btnEditAuctionHouse = new JButton("Edit Selected");
		btnEditAuctionHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSelectedAuctionHouse();
			}
		});
		btnEditAuctionHouse.setPreferredSize(new Dimension(150, 40));
		btnEditAuctionHouse.setMnemonic('E');
		btnEditAuctionHouse.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnEditAuctionHouse);
		btnAddAuctionHouse.setPreferredSize(new Dimension(170, 40));
		btnAddAuctionHouse.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnAddAuctionHouse);
		
		btnRefreshTable = new JButton("Refresh Table");
		btnRefreshTable.setMnemonic('R');
		btnRefreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowCount = tableModel.getRowCount();
				for (int i = rowCount - 1; i >= 0; i--) {
					tableModel.removeRow(i);
				}
				loadTableData();
			}
		});
		
		btnViewLotCount = new JButton("View Lot Count");
		btnViewLotCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewLotCountActionPerformed();
			}
		});
		btnViewLotCount.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnViewLotCount.setPreferredSize(new Dimension(150, 40));
		controlPanel.add(btnViewLotCount);
		btnRefreshTable.setPreferredSize(new Dimension(150, 40));
		btnRefreshTable.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnViewLotCount.setMnemonic('V');
		controlPanel.add(btnRefreshTable);
		
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
					//tableRowSelectionChanged();
				}
			});
		}
		{
			tableModel.addColumn("ID");
			tableModel.addColumn("Name");
			tableModel.addColumn("Country");
			tableModel.addColumn("Location");
			tableModel.addColumn("Currency Code");
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
		            	sorter.setRowFilter(RowFilter.regexFilter("(?i)" + str));
		            }
		         }
		      });
		}
	}

	protected void btnAddAuctionHouseActionPerformed() {
		try {
			AddAuctionHouseWindow addAuctionHouseWindow = new AddAuctionHouseWindow(username, jdbcTemplate, this);
			addAuctionHouseWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void editSelectedAuctionHouse() {
		EditAuctionHouseWindow editAuctionHouseWindow = new EditAuctionHouseWindow(username, jdbcTemplate, this);
		editAuctionHouseWindow.setVisible(true);
	}

	protected void btnViewLotCountActionPerformed() {
		try {
			String auctionHouseId = table.getValueAt(table.getSelectedRow(), 0).toString();
			String auctionHouseName = table.getValueAt(table.getSelectedRow(), 1).toString();
			String auctionHouseLocation = table.getValueAt(table.getSelectedRow(), 3).toString();
			
			String sqlQuery = "SELECT count(*) as lot_count FROM fineart_lots INNER JOIN fineart_auction_calendar ON "
					+ "fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID INNER JOIN core_auction_houses "
					+ "ON fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID "
					+ "WHERE core_auction_houses.cah_auction_house_ID = '" + auctionHouseId + "'";
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						String lotCount = resultSet.getString("lot_count");
						JOptionPane.showMessageDialog(null, lotCount, "Lot Count: " + auctionHouseName + " - " + auctionHouseLocation, JOptionPane.INFORMATION_MESSAGE);
					}
					return true;
				}
			});
		} catch (Exception e) {
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
