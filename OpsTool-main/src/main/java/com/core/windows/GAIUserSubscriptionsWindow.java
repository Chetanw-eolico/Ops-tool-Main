package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

public class GAIUserSubscriptionsWindow extends JFrame {

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
	private JButton btnGrantAccess;
	private JButton btnRefreshTable;
	private GAIUserAccountsWindow gAIUserAccountsWindow;
	private String userId;
	private String userName;
	private String userEmail;
	private JButton btnEditSelected;
	
	public GAIUserSubscriptionsWindow(String username, JdbcTemplate jdbcTemplate, GAIUserAccountsWindow gAIUserAccountsWindow) {
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		this.gAIUserAccountsWindow = gAIUserAccountsWindow;
		initGUI();
		loadTableData();
	}
	
	public void loadTableData() {
		try {
			userId = gAIUserAccountsWindow.getTable().getValueAt(gAIUserAccountsWindow.getTable().getSelectedRow(), 0).toString();
			userName = gAIUserAccountsWindow.getTable().getValueAt(gAIUserAccountsWindow.getTable().getSelectedRow(), 2).toString();
			userEmail = gAIUserAccountsWindow.getTable().getValueAt(gAIUserAccountsWindow.getTable().getSelectedRow(), 3).toString();
			
			this.setTitle("Subscriptions for - " + userName + " (" + userEmail + ")");
			
			String sqlQuery = "SELECT * FROM subscription WHERE subscription.user_id = '" + userId + "' ORDER BY subscription.subscription_record_created DESC";
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					while (resultSet.next()) {
						tableModel.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("subscription_record_created"), 
								resultSet.getString("subscription_type"), resultSet.getString("subscription_status"), 
								resultSet.getString("subscription_expires"), resultSet.getString("subscription_autorenew"), resultSet.getString("payment_ID"), 
								resultSet.getFloat("payment_amount"), resultSet.getString("card_details")});
						
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		table.getColumn("ID").setPreferredWidth(50);
		table.getColumn("Date").setPreferredWidth(100);
		table.getColumn("Type").setPreferredWidth(100);
		table.getColumn("Status").setPreferredWidth(100);
		table.getColumn("Expiration").setPreferredWidth(100);
		table.getColumn("Auto Renew").setPreferredWidth(50);
		table.getColumn("Payment ID").setPreferredWidth(100);
		table.getColumn("Amount").setPreferredWidth(50);
		table.getColumn("Card Details").setPreferredWidth(100);
		table.getColumn("Card Details").setCellRenderer(new AccessGrantedCellRenderer());
	}

	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1300, 650));
		this.setSize(new Dimension(1300, 650));
		this.setTitle("Subscriptions for - " + userName + " (" + userEmail + ")");
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
		tableScrollPane.add(table);
		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(0, 50));
		basePanel.add(controlPanel);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSearch = new JLabel("Search:");
		controlPanel.add(lblSearch);
		
		searchTextField = new JTextField();
		searchTextField.setPreferredSize(new Dimension(200, 30));
		controlPanel.add(searchTextField);
		
		btnGrantAccess = new JButton("Grant Access");
		btnGrantAccess.setMnemonic('A');
		btnGrantAccess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGrantAccessActionPerformed();
			}
		});
		
		btnEditSelected = new JButton("Edit Selected");
		btnEditSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEditSelectedActionPerformed();
			}
		});
		btnEditSelected.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEditSelected.setPreferredSize(new Dimension(170, 40));
		controlPanel.add(btnEditSelected);
		btnGrantAccess.setPreferredSize(new Dimension(170, 40));
		btnGrantAccess.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnGrantAccess);
		
		btnRefreshTable = new JButton("Refresh Table");
		btnRefreshTable.setMnemonic('R');
		btnRefreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTable();
			}
		});
		btnRefreshTable.setPreferredSize(new Dimension(150, 40));
		btnRefreshTable.setFont(new Font("Tahoma", Font.BOLD, 12));
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
			table.setFont(new java.awt.Font("Segoe UI",0,12));
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
			tableModel.addColumn("Date");
			tableModel.addColumn("Type");
			tableModel.addColumn("Status");
			tableModel.addColumn("Expiration");
			tableModel.addColumn("Auto Renew");
			tableModel.addColumn("Payment ID");
			tableModel.addColumn("Amount");
			tableModel.addColumn("Card Details");
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

	public void refreshTable() {
		try {
			int rowCount = tableModel.getRowCount();
			for (int i = rowCount - 1; i >= 0; i--) {
				tableModel.removeRow(i);
			}
			loadTableData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void btnEditSelectedActionPerformed() {
		try {
			if(table.getSelectedRow() == -1) {
				return;
			}
			GAIEditSubscriptionWindow gaiEditSubscriptionWindow = new GAIEditSubscriptionWindow(username, userId, userName, userEmail, 
					jdbcTemplate, this);
			gaiEditSubscriptionWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void btnGrantAccessActionPerformed() {
		try {
			GAIGrantAccessWindow gAIGrantAccessWindow = new GAIGrantAccessWindow(username, userId, userName, userEmail, jdbcTemplate, this);
			gAIGrantAccessWindow.setVisible(true);
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
	
	class AccessGrantedCellRenderer extends DefaultTableCellRenderer {
	    private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        if (value.equals("Access Granted")) {
	            c.setBackground( Color.orange );
	            c.setForeground(Color.black );
	        } else {
	        	c.setBackground( Color.white );
	            c.setForeground(Color.black );
	        }
	        return c;
	    }
	}	
}
