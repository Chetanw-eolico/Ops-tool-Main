package com.core.windows;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import java.awt.Font;


public class AddGenreWindow extends javax.swing.JFrame {
	
	private JdbcTemplate jdbcTemplate;
	
	private static final long serialVersionUID = 1L;
	private JPanel basePanel;
	private JLabel genreLable;
	private JButton okButton;
	private static JTextField genreText;
	Action action;
	KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	Action enterHotKey;
	String firstname = "";
	String lastname = "";

	private String username;

	private GenreTableInterface genreTable;
	
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddGenreWindow(String username, JdbcTemplate jdbcTemplate, GenreTableInterface genreTable) throws SQLException {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		this.genreTable = genreTable;
		initGUI();
		
	}
	
	private void initGUI() {
		try {
			{
				basePanel = new JPanel();
				basePanel.setLayout(null);
				
				Action enterAction = new AbstractAction("Add") {
					 
				    private static final long serialVersionUID = 1L;

					@Override
				    public void actionPerformed(ActionEvent e) {
				        addGenre();
				    }
				};
				
				enterAction.putValue(Action.ACCELERATOR_KEY, enterKey);
				basePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Add");
				basePanel.getActionMap().put("Login", enterAction);
				
				this.getContentPane().add(basePanel);
				getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
				getRootPane().getActionMap().put("Cancel", new AbstractAction() {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				
				this.setResizable(false);
				this.setTitle("Add New Genre");
				
				//setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				
				{
					genreLable = new JLabel();
					basePanel.add(genreLable);
					genreLable.setText("Genre:");
					genreLable.setBounds(10, 33, 47, 29);
					genreLable.setFont(new Font("Tahoma", Font.PLAIN, 12));
				}
				{
					genreText = new JTextField();
					basePanel.add(genreText);
					genreText.setBounds(54, 32, 330, 30);
					genreText.setFont(new java.awt.Font("Tahoma",0,14));
				}
				
				{
					okButton = new JButton();
					basePanel.add(okButton);
					okButton.setBounds(130, 98, 175, 57);
					okButton.setText("Add");
					okButton.setFont(new java.awt.Font("Tahoma",1,16));
					
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							okButtonActionPerformed(evt);
						}
					});
				}
			}
			this.setSize(400, 202);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void okButtonActionPerformed(ActionEvent evt) {
		addGenre();
	}

	private void addGenre() {
		try {
			String genre = genreText.getText().trim();
			if(StringUtils.isEmpty(genre)) {
				JOptionPane.showMessageDialog(this, "Genre is blank.");
				return;
			}
			String sql = "INSERT INTO fineart_genres (fg_genre, fg_genre_record_created, fg_genre_record_createdby) "
					+ "VALUES ('" + genre + "','" + new Timestamp(System.currentTimeMillis()) + "','" + username + "')";
			jdbcTemplate.update(sql);
			
			((DefaultTableModel) genreTable.getTable().getModel()).insertRow(0, new Object[]{0, genre});
            
			genreTable.getTable().setRowSelectionInterval(0, 0);
			genreTable.getTable().requestFocus();
            
            this.setVisible(false);
			this.dispose();
		} catch (org.springframework.dao.DuplicateKeyException e) {
			JOptionPane.showMessageDialog(this, "This genre already exists.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}