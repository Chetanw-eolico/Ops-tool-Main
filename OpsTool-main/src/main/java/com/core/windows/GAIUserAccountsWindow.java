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

public class GAIUserAccountsWindow extends JFrame {

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
	private JButton btnViewSubscriptions;
	private JButton btnRefreshTable;
	private JButton btnUser;
	
	public GAIUserAccountsWindow(JdbcTemplate jdbcTemplate, String username) {
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		initGUI();
		loadTableData();
	}
	
	private void loadTableData() {
		try {
			//This is original query
			String sqlQuery = "SELECT user_accounts.*, (SELECT count(*) AS subscription_count "
					+ "WHERE subscription.user_id = user_accounts.user_id) as subscription_count "
					+ "FROM subscription RIGHT JOIN user_accounts ON subscription.user_id = user_accounts.user_id "
					+ "GROUP BY user_accounts.user_id "
					+ "ORDER BY user_accounts.user_id DESC";
			
			//This is tempered query
			/*String sqlQuery = "SELECT user_accounts.*, (SELECT count(*) AS subscription_count "
					+ "WHERE subscription.user_id = user_accounts.user_id) as subscription_count "
					+ "FROM subscription RIGHT JOIN user_accounts ON subscription.user_id = user_accounts.user_id "
					+ "GROUP BY user_accounts.user_id "
					+ "ORDER BY user_accounts.user_id ASC limit 30";*/
			
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					while (resultSet.next()) {
						tableModel.addRow(new Object[]{resultSet.getInt("user_id"), resultSet.getString("created"), 
								resultSet.getString("full_name"), resultSet.getString("login_email"), 
								resultSet.getString("subscriber_type"), resultSet.getString("customer_id"), resultSet.getString("otp"), 
								resultSet.getString("otp_used"), resultSet.getInt("subscription_count")});
						
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		table.getColumn("ID").setPreferredWidth(50);
		table.getColumn("Date").setPreferredWidth(100);
		table.getColumn("Name").setPreferredWidth(100);
		table.getColumn("Email").setPreferredWidth(150);
		table.getColumn("Type").setPreferredWidth(50);
		table.getColumn("Gateway ID").setPreferredWidth(100);
		table.getColumn("OTP").setPreferredWidth(50);
		table.getColumn("OTP Used").setPreferredWidth(50);
		table.getColumn("Subscriptions").setPreferredWidth(50);
		table.getColumn("Subscriptions").setCellRenderer(new SubscriptionCountZeroCellRenderer());
	}

	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(1300, 650));
		this.setSize(new Dimension(1300, 650));
		this.setTitle("GAI User Accounts");
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
		
		btnViewSubscriptions = new JButton("View Subscriptions");
		btnViewSubscriptions.setMnemonic('V');
		btnViewSubscriptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewSubscriptionsActionPerformed();
			}
		});
		
		btnUser = new JButton("Edit Selected");
		btnUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editUserBtnActionPerformed();
			}
		});
		btnUser.setPreferredSize(new Dimension(150, 40));
		btnUser.setMnemonic('E');
		btnUser.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnUser);
		btnViewSubscriptions.setPreferredSize(new Dimension(170, 40));
		btnViewSubscriptions.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnViewSubscriptions);
		
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
			tableModel.addColumn("Name");
			tableModel.addColumn("Email");
			tableModel.addColumn("Type");
			tableModel.addColumn("Gateway ID");
			tableModel.addColumn("OTP");
			tableModel.addColumn("OTP Used");
			tableModel.addColumn("Subscriptions");
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

	protected void btnViewSubscriptionsActionPerformed() {
		try {
			GAIUserSubscriptionsWindow gAIUserSubscriptionsWindow = new GAIUserSubscriptionsWindow(username, jdbcTemplate, this);
			gAIUserSubscriptionsWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void editUserBtnActionPerformed() {
		GAIEditUserWindow editGAIUserWindow = new GAIEditUserWindow(username, jdbcTemplate, this);
		editGAIUserWindow.setVisible(true);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	
	class SubscriptionCountZeroCellRenderer extends DefaultTableCellRenderer {
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
	
}
