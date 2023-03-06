package com.core.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.Constants;
import com.fineart.uploader.CsvImageUploader;
import java.awt.Font;

public class ViewAndUploadImageWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbcTemplate;
	private String username;
	private LotDetailsWindow lotDetailsWindow;
	private Integer lotId;
	private String lastBrowsedImagePath;
	private JTextField txtImage1;
	private JTextField txtImage2;
	private JTextField txtImage3;
	private JTextField txtImage4;
	private JTextField txtImage5;
	
	public ViewAndUploadImageWindow(String username, Integer lotId, JdbcTemplate jdbcTemplate, LotDetailsWindow lotDetailsWindow) {
		this.username = username;
		this.lotId = lotId;
		this.jdbcTemplate = jdbcTemplate;
		this.lotDetailsWindow = lotDetailsWindow;
		initGUI();
		loadAuctionNameAndImages();
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

	private void loadAuctionNameAndImages() {
		String auctionNameQuery = "SELECT  fineart_lots.fal_lot_image1, fineart_lots.fal_lot_image2, fineart_lots.fal_lot_image3, "
				+ "fineart_lots.fal_lot_image4, fineart_lots.fal_lot_image5 FROM  fineart_lots WHERE fineart_lots.fal_lot_ID = '" + lotId + "'";
		
		jdbcTemplate.query(auctionNameQuery, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					txtImage1.setText(resultSet.getString("fal_lot_image1"));
					txtImage2.setText(resultSet.getString("fal_lot_image2"));
					txtImage3.setText(resultSet.getString("fal_lot_image3"));
					txtImage4.setText(resultSet.getString("fal_lot_image4"));
					txtImage5.setText(resultSet.getString("fal_lot_image5"));
				}
				return true;
			}
		});
	}

	private void initGUI() {
		this.setPreferredSize(new Dimension(900, 400));
		this.setSize(new Dimension(900, 400));
		this.setTitle("Image Manager");
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
		
		JLabel lblImage = new JLabel("Image 1:");
		lblImage.setBounds(10, 11, 57, 14);
		basePanel.add(lblImage);
		
		txtImage1 = new JTextField();
		txtImage1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtImage1.setDisabledTextColor(Color.BLACK);
		txtImage1.setEnabled(false);
		txtImage1.setBounds(76, 8, 479, 20);
		basePanel.add(txtImage1);
		txtImage1.setColumns(10);
		
		JButton btnSelect1 = new JButton("Select");
		btnSelect1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectImage1();
			}
		});
		btnSelect1.setBounds(565, 7, 109, 23);
		basePanel.add(btnSelect1);
		
		JLabel lblImage_1 = new JLabel("Image 2:");
		lblImage_1.setBounds(10, 60, 57, 14);
		basePanel.add(lblImage_1);
		
		txtImage2 = new JTextField();
		txtImage2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtImage2.setDisabledTextColor(Color.BLACK);
		txtImage2.setEnabled(false);
		txtImage2.setColumns(10);
		txtImage2.setBounds(76, 57, 479, 20);
		basePanel.add(txtImage2);
		
		JButton btnSelect2 = new JButton("Select");
		btnSelect2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectImage2();
			}
		});
		btnSelect2.setBounds(565, 56, 109, 23);
		basePanel.add(btnSelect2);
		
		JLabel lblImage_2 = new JLabel("Image 3:");
		lblImage_2.setBounds(10, 110, 57, 14);
		basePanel.add(lblImage_2);
		
		txtImage3 = new JTextField();
		txtImage3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtImage3.setDisabledTextColor(Color.BLACK);
		txtImage3.setEnabled(false);
		txtImage3.setColumns(10);
		txtImage3.setBounds(76, 107, 479, 20);
		basePanel.add(txtImage3);
		
		JButton btnSelect3 = new JButton("Select");
		btnSelect3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectImage3();
			}
		});
		btnSelect3.setBounds(565, 106, 109, 23);
		basePanel.add(btnSelect3);
		
		JLabel lblImage_3 = new JLabel("Image 4:");
		lblImage_3.setBounds(10, 164, 57, 14);
		basePanel.add(lblImage_3);
		
		txtImage4 = new JTextField();
		txtImage4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtImage4.setDisabledTextColor(Color.BLACK);
		txtImage4.setEnabled(false);
		txtImage4.setColumns(10);
		txtImage4.setBounds(76, 161, 479, 20);
		basePanel.add(txtImage4);
		
		JButton btnSelect4 = new JButton("Select");
		btnSelect4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectImage4();
			}
		});
		btnSelect4.setBounds(565, 160, 109, 23);
		basePanel.add(btnSelect4);
		
		JLabel lblImage_4 = new JLabel("Image 5:");
		lblImage_4.setBounds(10, 214, 57, 14);
		basePanel.add(lblImage_4);
		
		txtImage5 = new JTextField();
		txtImage5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtImage5.setDisabledTextColor(Color.BLACK);
		txtImage5.setEnabled(false);
		txtImage5.setColumns(10);
		txtImage5.setBounds(76, 211, 479, 20);
		basePanel.add(txtImage5);
		
		JButton btnSelect5 = new JButton("Select");
		btnSelect5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectImage5();
			}
		});
		btnSelect5.setBounds(565, 210, 109, 23);
		basePanel.add(btnSelect5);
		
		JButton btnSave = new JButton("Save & Upload");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		btnSave.setMnemonic('S');
		btnSave.setBounds(227, 268, 176, 60);
		basePanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnCancel.setMnemonic('C');
		btnCancel.setBounds(425, 269, 176, 60);
		basePanel.add(btnCancel);
		
		JButton btnView1 = new JButton("View");
		btnView1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewImage(txtImage1.getText());
			}
		});
		btnView1.setBounds(683, 7, 89, 23);
		basePanel.add(btnView1);
		
		JButton btnView2 = new JButton("View");
		btnView2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewImage(txtImage2.getText());
			}
		});
		btnView2.setBounds(683, 56, 89, 23);
		basePanel.add(btnView2);
		
		JButton btnView3 = new JButton("View");
		btnView3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewImage(txtImage3.getText());
			}
		});
		btnView3.setBounds(683, 106, 89, 23);
		basePanel.add(btnView3);
		
		JButton btnView4 = new JButton("View");
		btnView4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewImage(txtImage4.getText());
			}
		});
		btnView4.setBounds(683, 160, 89, 23);
		basePanel.add(btnView4);
		
		JButton btnView5 = new JButton("View");
		btnView5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewImage(txtImage5.getText());
			}
		});
		btnView5.setBounds(684, 210, 89, 23);
		basePanel.add(btnView5);
		
		JButton btnRemove1 = new JButton("Remove");
		btnRemove1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeImage(txtImage1);
			}
		});
		btnRemove1.setBounds(785, 7, 89, 23);
		basePanel.add(btnRemove1);
		
		JButton btnRemove2 = new JButton("Remove");
		btnRemove2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeImage(txtImage2);
			}
		});
		btnRemove2.setBounds(782, 56, 89, 23);
		basePanel.add(btnRemove2);
		
		JButton btnRemove3 = new JButton("Remove");
		btnRemove3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeImage(txtImage3);
			}
		});
		btnRemove3.setBounds(785, 106, 89, 23);
		basePanel.add(btnRemove3);
		
		JButton btnRemove4 = new JButton("Remove");
		btnRemove4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeImage(txtImage4);
			}
		});
		btnRemove4.setBounds(785, 160, 89, 23);
		basePanel.add(btnRemove4);
		
		JButton btnRemove5 = new JButton("Remove");
		btnRemove5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeImage(txtImage5);
			}
		});
		btnRemove5.setBounds(785, 210, 89, 23);
		basePanel.add(btnRemove5);
	}
	
	protected void removeImage(JTextField txtImage) {
		txtImage.setText("");
	}

	protected void viewImage(String imagePath) {
		try {
			File imageFile = new File(imagePath);
			if(imageFile.exists()) {
				URI uri = new URI(imagePath);
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(uri);
					} catch (IOException e) {
					}
				}
			} else {
				URI uri = new URI(Constants.BACKBLAZE_FINEART_BUCKET_URL + imagePath);
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(uri);
					} catch (IOException e) {
					}
				}
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	private void doSave() {
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            public String doInBackground() {
            	saveImages();
                return "";
            }
        };
       // execute the background thread
       worker.execute();
	}
	
	protected void saveImages() {
		try {
			CsvImageUploader imageUploader = null;
			String image1Path = txtImage1.getText();
			String image2Path = txtImage2.getText();
			String image3Path = txtImage3.getText();
			String image4Path = txtImage4.getText();
			String image5Path = txtImage5.getText();
			int imageCount = 0;
			
			if(StringUtils.isNotEmpty(image1Path) || StringUtils.isNotEmpty(image2Path) || StringUtils.isNotEmpty(image3Path) || StringUtils.isNotEmpty(image4Path) || 
					StringUtils.isNotEmpty(image5Path)) {
				imageUploader = new CsvImageUploader();
			}
			
			//image 1
			File imageFile = new File(image1Path);
			String insertSql1 = "UPDATE fineart_lots SET fal_lot_image1 = '" + imageFile.getName() + "', "
					+ "fal_lot_record_updated = CURRENT_TIMESTAMP, fal_lot_record_updatedby = '" + username + "' WHERE  fal_lot_ID = '" + lotId + "'";
			jdbcTemplate.update(insertSql1);
			if(imageFile.exists()) {
				imageUploader.uploadImageFromUrlToBackBlazeHost(imageFile);
				imageCount++; // increase the count when the image is uploaded and reference is attached in the db succesfully.
			} else {
				if(StringUtils.isNotEmpty(image1Path)) { //image host URL
					imageCount++; //this is already uploaded image.
				}
			}
			
			//image 2
			File image2File = new File(image2Path);
			String insertSql2 = "UPDATE fineart_lots SET fal_lot_image2 = '" + image2File.getName() + "', "
					+ "fal_lot_record_updated = CURRENT_TIMESTAMP, fal_lot_record_updatedby = '" + username + "' WHERE  fal_lot_ID = '" + lotId + "'";
			jdbcTemplate.update(insertSql2);
			if(image2File.exists()) {
				imageUploader.uploadImageFromUrlToBackBlazeHost(image2File);
				imageCount++; // increase the count when the image is uploaded and reference is attached in the db succesfully.
			} else {
				if(StringUtils.isNotEmpty(image2Path)) { //image host URL
					imageCount++; //this is already uploaded image.
				}
			}
			
			//image 3
			File image3File = new File(image3Path);
			String insertSql3 = "UPDATE fineart_lots SET fal_lot_image3 = '" + image3File.getName() + "', "
					+ "fal_lot_record_updated = CURRENT_TIMESTAMP, fal_lot_record_updatedby = '" + username + "' WHERE  fal_lot_ID = '" + lotId + "'";
			jdbcTemplate.update(insertSql3);
			if(image3File.exists()) {
				imageUploader.uploadImageFromUrlToBackBlazeHost(image3File);
				imageCount++; // increase the count when the image is uploaded and reference is attached in the db succesfully.
			} else {
				if(StringUtils.isNotEmpty(image3Path)) { //image host URL
					imageCount++; //this is already uploaded image.
				}
			}
			
			//image 4
			File image4File = new File(image4Path);
			String insertSql4 = "UPDATE fineart_lots SET fal_lot_image4 = '" + image4File.getName() + "', "
					+ "fal_lot_record_updated = CURRENT_TIMESTAMP, fal_lot_record_updatedby = '" + username + "' WHERE  fal_lot_ID = '" + lotId + "'";
			jdbcTemplate.update(insertSql4);
			if(image4File.exists()) {
				imageUploader.uploadImageFromUrlToBackBlazeHost(image4File);
				imageCount++; // increase the count when the image is uploaded and reference is attached in the db succesfully.
			} else {
				if(StringUtils.isNotEmpty(image4Path)) { //image host URL
					imageCount++; //this is already uploaded image.
				}
			}
			
			//image 5
			File image5File = new File(image5Path);
			String insertSql5 = "UPDATE fineart_lots SET fal_lot_image5 = '" + image5File.getName() + "', "
					+ "fal_lot_record_updated = CURRENT_TIMESTAMP, fal_lot_record_updatedby = '" + username + "' WHERE  fal_lot_ID = '" + lotId + "'";
			jdbcTemplate.update(insertSql5);
			if(image5File.exists()) {
				imageUploader.uploadImageFromUrlToBackBlazeHost(image5File);
				imageCount++; // increase the count when the image is uploaded and reference is attached in the db succesfully.
			} else {
				if(StringUtils.isNotEmpty(image5Path)) { //image host URL
					imageCount++; //this is already uploaded image.
				}
			}
			
			lotDetailsWindow.setImageCount(String.valueOf(imageCount));
			this.setVisible(false);
			this.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectImage(JTextField txtField) {
		JFileChooser jfc = new JFileChooser(lastBrowsedImagePath);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int returnVal = jfc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
			String parentPath = jfc.getSelectedFile().getParent().replaceAll("\\\\", "//");
			lastBrowsedImagePath = parentPath;
			String insertSql = "UPDATE operations_team SET upload_images_path='" + parentPath + "' WHERE  username = '" + username + "'";
			jdbcTemplate.update(insertSql);
			txtField.setText(outputPath);
		}
	}
	
	protected void selectImage1() {
		selectImage(txtImage1);
	}
	
	protected void selectImage2() {
		selectImage(txtImage2);
	}
	
	protected void selectImage3() {
		selectImage(txtImage3);
	}
	
	protected void selectImage4() {
		selectImage(txtImage4);
	}

	protected void selectImage5() {
		selectImage(txtImage5);
	}
}
