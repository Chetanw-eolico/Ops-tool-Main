/*package com.core.fineart;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.fineart.scraper.ChristiesScraper;
import com.fineart.scraper.ScraperInterface;
import com.fineart.scraper.SothebiesScraper;

public class PastSalesWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JScrollPane baseScrollPane;
	private JButton scrapeButton;
	private JPanel basePanel;
	private JPanel componentPanel;
	private ChristiesPastSalesRowComponent christiesPastSalesRowComponent;
	private SothebyPastSalesRowComponent sothebyPastSalesRowComponent;
	private BonhamsPastSalesRowComponent bonhamsPastSalesRowComponent;
	private List<String> fineartKeywordFiltersList;
	public ResourceBundle resourceBundle;
	private String username;
	private JdbcTemplate jdbcTemplate;
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PastSalesWindow(String username, String publisherName, List<String> fineartKeywordFiltersList, JdbcTemplate jdbcTemplate) {
		
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		        String ObjButtons[] = {"Yes","No"};
		        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to close this window?", "Past Sales Window", 
		        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
		        if(PromptResult==JOptionPane.YES_OPTION){
		            setVisible(false);
		            dispose();
		        }
		    	
		    	setVisible(false);
	            dispose();
		    }
		});
		
		initGUI();
		this.fineartKeywordFiltersList = fineartKeywordFiltersList;
	}

	private void initGUI() {
		try {
			this.setSize(721, 500);
			this.setTitle("Operations Tool");
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			this.setPreferredSize(new java.awt.Dimension(628, 500));
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
			{
				basePanel = new JPanel();
				getContentPane().add(basePanel);
				basePanel.setLayout(null);
				basePanel.setPreferredSize(new java.awt.Dimension(550, 80));
				basePanel.setSize(550, 80);
				{
					baseScrollPane = new JScrollPane();
					basePanel.add(baseScrollPane);
					baseScrollPane.setBounds(10, 11, 678, 361);
					baseScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					baseScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

					componentPanel = new JPanel();
					componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));
					
					baseScrollPane.add(componentPanel);
					
					christiesPastSalesRowComponent = new ChristiesPastSalesRowComponent();
					sothebyPastSalesRowComponent = new SothebyPastSalesRowComponent();
					bonhamsPastSalesRowComponent = new BonhamsPastSalesRowComponent();
					
					componentPanel.add(christiesPastSalesRowComponent);
					componentPanel.add(sothebyPastSalesRowComponent);
					componentPanel.add(bonhamsPastSalesRowComponent);
					
					baseScrollPane.setViewportView(componentPanel);
				}
				scrapeButton = new JButton();
				scrapeButton.setBounds(204, 397, 150, 53);
				basePanel.add(scrapeButton);
				scrapeButton.setText("Start Scraping");
				scrapeButton.setPreferredSize(new Dimension(150, 53));
				scrapeButton.setFont(new java.awt.Font("Tahoma",1,14));
				{
					
					JButton btnCancel = new JButton("Cancel");
					btnCancel.setBounds(364, 397, 150, 53);
					basePanel.add(btnCancel);
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setVisible(false);
							dispose();
						}
					});
					btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
				}
				scrapeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						scrapeButtonActionPerformed(evt);
					}
				});
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void scrapeButtonActionPerformed(ActionEvent evt) {
		try {
			Date startDate = null;
			Date endDate = null;
			String outputPath = "";
			
			String sql = "select scraping_download_path from operations_team where username = '" + username + "'";
			String lastBrowsedPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("scraping_download_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
			
			
			JFileChooser jfc = new JFileChooser(lastBrowsedPath);
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			ScraperInterface fineartScraper = null;
			
			if(this.getChristiesPastSalesRowComponent().getRadioButton().isSelected()) {
				startDate = this.getChristiesPastSalesRowComponent().getFromDateChooser().getDate();
				endDate = this.getChristiesPastSalesRowComponent().getToDateChooser().getDate();
				fineartScraper = new ChristiesScraper(jdbcTemplate);
			} else if (this.getSothebyPastSalesRowComponent().getRadioButton().isSelected()) {
				startDate = this.getSothebyPastSalesRowComponent().getFromDateChooser().getDate();
				endDate = this.getSothebyPastSalesRowComponent().getToDateChooser().getDate();
				fineartScraper = new SothebiesScraper(jdbcTemplate);
			} else if (this.getBonhamsPastSalesRowComponent().getRadioButton().isSelected()) {
				startDate = this.getBonhamsPastSalesRowComponent().getFromDateChooser().getDate();
				endDate = this.getBonhamsPastSalesRowComponent().getToDateChooser().getDate();
			} else {
				JOptionPane.showMessageDialog(this,"Please select an auction house to scrape.", "Past Sales Window", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int returnVal = jfc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String insertSql = "UPDATE operations_team SET scraping_download_path='" + outputPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
			} else {
				fineartScraper.close();
				return;
			}
			fineartScraper.initScraping(startDate, endDate, fineartKeywordFiltersList, outputPath, true);
		} catch(NullPointerException npe) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ChristiesPastSalesRowComponent getChristiesPastSalesRowComponent() {
		return christiesPastSalesRowComponent;
	}

	public SothebyPastSalesRowComponent getSothebyPastSalesRowComponent() {
		return sothebyPastSalesRowComponent;
	}
	
	public BonhamsPastSalesRowComponent getBonhamsPastSalesRowComponent() {
		return bonhamsPastSalesRowComponent;
	}

	public List<String> getFineartKeywordFiltersList() {
		return fineartKeywordFiltersList;
	}
}
*/