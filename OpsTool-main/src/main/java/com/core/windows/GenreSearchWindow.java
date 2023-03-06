package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class GenreSearchWindow extends JFrame implements GenreTableInterface {

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
	private JButton btnAddGenre;
	private JButton btnEditGenre;
	private GenreFieldInterface genreField;
	
	public GenreSearchWindow(JdbcTemplate jdbcTemplate, String username, GenreFieldInterface genreField) {
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		this.genreField = genreField;
		initGUI();
		loadTableData();
	}
	
	private void loadTableData() {
		try {
			String sqlQuery = "SELECT * from fineart_genres";
			jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Boolean>() {

				public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					while (resultSet.next()) {
						tableModel.addRow(new Object[]{resultSet.getInt("fg_genre_ID"), resultSet.getString("fg_genre")});
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		table.getColumn("ID").setPreferredWidth(50);
		table.getColumn("Genre").setPreferredWidth(550);
	}

	private void initGUI() {
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setPreferredSize(new Dimension(650, 650));
		this.setSize(new Dimension(650, 650));
		this.setTitle("Fineart Genres");
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
		
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(0, 50));
		basePanel.add(controlPanel);
		controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblSearch = new JLabel("Search:");
		controlPanel.add(lblSearch);
		
		searchTextField = new JTextField();
		searchTextField.setPreferredSize(new Dimension(200, 30));
		controlPanel.add(searchTextField);
		
		btnEditGenre = new JButton("Edit Selected");
		btnEditGenre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editGenreActionPerformed();
			}
		});
		
		btnAddGenre = new JButton("Add New");
		btnAddGenre.setMnemonic('A');
		btnAddGenre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddGenreActionPerformed();
			}
		});
		btnAddGenre.setPreferredSize(new Dimension(150, 40));
		btnAddGenre.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnAddGenre);
		btnEditGenre.setPreferredSize(new Dimension(150, 40));
		btnEditGenre.setMnemonic('E');
		btnEditGenre.setFont(new Font("Tahoma", Font.BOLD, 12));
		controlPanel.add(btnEditGenre);
		
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
			
			table.addMouseListener(new MouseAdapter() {
			    public void mousePressed(MouseEvent mouseEvent) {
			        JTable table =(JTable) mouseEvent.getSource();
			        Point point = mouseEvent.getPoint();
			        int row = table.rowAtPoint(point);
			        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
			        	if(!genreField.getGenreField().getText().contains(table.getValueAt(row, 1).toString())) {
			        		genreField.getGenreField().setText(genreField.getGenreField().getText() + table.getValueAt(row, 1) + ",") ;
			        	}
			        }
			    }
			});
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
			tableModel.addColumn("Genre");
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

	protected void btnAddGenreActionPerformed() {
		try {
			AddGenreWindow addGenreWindow = new AddGenreWindow(username, jdbcTemplate, this);
			addGenreWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void editGenreActionPerformed() {
		try {
			EditGenreWindow editGenreWindow = new EditGenreWindow(username, jdbcTemplate, this);
			editGenreWindow.setVisible(true);
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
