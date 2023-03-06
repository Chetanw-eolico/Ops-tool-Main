package com.core.windows;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.blackbaze.BackblazeImageDeleter;
import com.core.fineart.BrowserWorkerThread;
import com.core.pojo.AuctionCalendarData;
import com.core.pojo.AuctionCalendarDataMapper;
import com.core.pojo.AuctionHouseData;
import com.core.pojo.AuctionHouseDataMapper;
import com.core.pojo.FineartArtistsData;
import com.core.pojo.FineartArtistsDataMapper;
import com.core.pojo.FineartArtworkData;
import com.core.pojo.FineartArtworkDataMapper;
import com.core.pojo.Rates;
import com.core.pojo.SaleSummaryRecord;
import com.core.pojo.SearchResultsData;
import com.core.pojo.SearchResultsDataMapper;
import com.fineart.scraper.SothebysLotsSaver;
import com.fineart.uploader.CsvImageUploader;
import com.fineart.uploader.PastSalesUploader;
import com.fineart.uploader.UpcomingSalesUploader;
import com.google.gson.Gson;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import java.awt.Font;


public class MainWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private JMenu webdriverMenu;
	private JMenu priceTypeMenu;
	private JMenu preferencesMenu;
	private JMenuItem downloadImagesOnlyMenuItem;
	private JMenuItem uploadImagesMenuItem;
	private JMenuItem resizeImagesMenuItem;
	private JMenuItem uploadMissingArtistsMenuItem;
	private JMenuItem uploadAuctionLotsMenuItem;
	private JMenuItem uploadUpcomingAuctionsMenuItem;
	private JMenuItem sothebysScraperMenuItem;
	private JMenu dataMenu;
	private JMenu reportsMenu;
	
	private JMenuItem exportSalesReportMenuItem;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JPanel welcomePanel;
	private JMenuItem createArtistsIndexMenuItem;
	private JMenuItem createArtworkIndexMenuItem;
	private JMenuItem createFreeSearchIndexMenuItem;
	private JMenuItem createAuctionCalendarIndexMenuItem;
	private JMenuItem createAuctionHouseIndexMenuItem;
	private JMenuItem currencyUpdaterMenuItem;
	private JMenuItem auctionHouseMenuItem;
	private JMenuItem artistMenuItem;
	private JMenuItem deleteBackblazeImageMenuItem;
	private JMenu toolsActionMenu;
	
	private JMenuItem browseWebsiteMenu;
	
	private JMenu userAccountsMenu;
	private JMenuItem manageUsersMenuItem;
	
	private JMenuBar jMenuBar1;
	private String username;
	private String userFriendlyName;
	private JdbcTemplate jdbcTemplate;
	private JProgressBar progressBar;
	private JCheckBoxMenuItem chromeDriverMenuItem;
	private JCheckBoxMenuItem firefoxDriverMenuItem;
	private JCheckBoxMenuItem edgeDriverMenuItem;
	private JCheckBoxMenuItem premiumPriceTypeMenuItem;
	private JCheckBoxMenuItem hammerPriceTypeMenuItem;
	private JCheckBoxMenuItem downloadImagesMenuItem;
	private String webDriver = "Chrome";
	private String priceType = "premium";
	private boolean uploadDataWithDownloadImages = true;
	private JPanel tablePanel;
	private JPanel controlPanel;
	private JScrollPane tableScrollPane;
	private JTable table;
	DefaultTableModel tableModel;
	ListSelectionModel cellSelectionModel;
	private JTextField searchTextField;
	private JButton btnAuctionDetails;
	private JButton btnViewSale;
	private TableRowSorter<TableModel> sorter;
	private JLabel lblSearch;
	private JButton btnPublishSale;
	private JButton btnRefreshTable;
	private String artistsIndexPath;
	int offset = 0;
	int limit = 10;
	private JLabel lblAddNext;
	private JButton addRowsButton;
	private JLabel lblTo;
	private JTextField toRowsTextField;
	private JPanel controlPanelPart1;
	
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* Auto-generated main method to display this JFrame
	 * @param jdbcTemplate 
	*/
	public MainWindow(String username, String userFriendlyName, JdbcTemplate jdbcTemplate) {
		super();
		this.username = username;
		this.userFriendlyName = userFriendlyName;
		this.jdbcTemplate = jdbcTemplate;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		        System.exit(0);
		    }
		});
		initGUI();
		loadArtistIndexPath();
		loadTableData(offset, limit);
	}
	
	private void initGUI() {
		try {
			{
				this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				this.setTitle("Welcome " + userFriendlyName);
				welcomePanel = new JPanel();
				welcomePanel.setPreferredSize(new Dimension(500, 30));
				welcomePanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
				getContentPane().add(welcomePanel);
				{
					progressBar = new JProgressBar();
					progressBar.setVisible(false);
					welcomePanel.setLayout(new BorderLayout(0, 0));
					progressBar.setSize(new Dimension(500, 14));
					progressBar.setPreferredSize(new Dimension(500, 20));
					
					welcomePanel.add(progressBar);
				}
			}
			
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					{
						createArtistsIndexMenuItem = new JMenuItem();
						createArtistsIndexMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								createArtistsIndexMenuItemActionPerformed(evt);
							}
						});
					}
					{
						createArtworkIndexMenuItem = new JMenuItem();
						createArtworkIndexMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								createArtworkIndexMenuItemActionPerformed(evt);
							}
						});
						createArtworkIndexMenuItem.setText("Create Artwork Index");
					}
					{
						createFreeSearchIndexMenuItem = new JMenuItem();
						createFreeSearchIndexMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								createFreeSearchMenuItemActionPerformed(evt);
							}
						});
						createFreeSearchIndexMenuItem.setText("Create Free Search Index");
					}
					
					{
						createAuctionHouseIndexMenuItem = new JMenuItem();
						createAuctionHouseIndexMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								createAuctionHouseIndexMenuItemActionPerformed(evt);
							}
						});
						createAuctionHouseIndexMenuItem.setText("Create Auction House Index");
					}
					{
						createAuctionCalendarIndexMenuItem = new JMenuItem();
						createAuctionCalendarIndexMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								createAuctionHouseCalendarMenuItemActionPerformed(evt);
							}
						});
						createAuctionCalendarIndexMenuItem.setText("Create Auction Calendar Index");
					}
					{
						browseWebsiteMenu = new JMenuItem();
						browseWebsiteMenu.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								browseWebsiteMenuActionPerformed(evt);
							}
						});
						browseWebsiteMenu.setText("Browse Website");
					}
					{
						deleteBackblazeImageMenuItem = new JMenuItem();
						deleteBackblazeImageMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								BackblazeImageDeleter.initDeleter();
							}
						});
						deleteBackblazeImageMenuItem.setText("Delete Backblaze Image");
					}
				}
				{
					dataMenu = new JMenu();
					jMenuBar1.add(dataMenu);
					dataMenu.setText("Actions");
					
					{
						sothebysScraperMenuItem = new JMenuItem("Run Sothebys Scraper");
						sothebysScraperMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								new SothebysLotsSaver(2, 2, 1, 15);
							}
						});
						
					}
					
					{
						reportsMenu = new JMenu();
						jMenuBar1.add(reportsMenu);
						reportsMenu.setText("Reports");
						
						exportSalesReportMenuItem = new JMenuItem();
						exportSalesReportMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								exportSalesReportMenuItemActionPerformed();
							}
						});
						exportSalesReportMenuItem.setText("Export Sales Report");
						reportsMenu.add(exportSalesReportMenuItem);
					}
					
					{
						uploadAuctionLotsMenuItem = new JMenuItem();
						uploadAuctionLotsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								uploadPastAuctionsMenuItemActionPerformed();
							}
						});
						uploadAuctionLotsMenuItem.setText("Upload Auction Lots");
						dataMenu.add(uploadAuctionLotsMenuItem);
					}
					{
						uploadUpcomingAuctionsMenuItem = new JMenuItem();
						uploadUpcomingAuctionsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								uploadUpcomingAuctionsMenuItemActionPerformed();
							}
						});
						uploadUpcomingAuctionsMenuItem.setText("Upload Auction Calendar");
						dataMenu.add(uploadUpcomingAuctionsMenuItem);
						dataMenu.add(new JSeparator());
					}
					
					{
						downloadImagesOnlyMenuItem = new JMenuItem();
						downloadImagesOnlyMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								downloadImagesOnlyMenuItemActionPerformed();
							}
						});
						dataMenu.add(downloadImagesOnlyMenuItem);
						downloadImagesOnlyMenuItem.setText("Download Only Images");
					}
					
					{
						uploadImagesMenuItem = new JMenuItem();
						uploadImagesMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								uploadImagesActionPerformed(e);
							}
						});
						dataMenu.add(uploadImagesMenuItem);
						uploadImagesMenuItem.setText("Upload Images");
						dataMenu.add(new JSeparator());
					}
					{
						resizeImagesMenuItem = new JMenuItem();
						resizeImagesMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								resizeImagesActionPerformed(e);
							}
						});
						dataMenu.add(resizeImagesMenuItem);
						resizeImagesMenuItem.setText("Resize Images");
					}
					{
						uploadMissingArtistsMenuItem = new JMenuItem();
						uploadMissingArtistsMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								uploadMissinfArtistsActionPerformed(e);
							}
						});
						dataMenu.add(uploadMissingArtistsMenuItem);
						uploadMissingArtistsMenuItem.setText("Upload Missing Artists");
					}
				}
				{
					preferencesMenu = new JMenu();
					jMenuBar1.add(preferencesMenu);
					preferencesMenu.setText("Preferences");
					{
						webdriverMenu = new JMenu();
						preferencesMenu.add(webdriverMenu);
						webdriverMenu.setText("Web Driver");
					}
					{
						priceTypeMenu = new JMenu();
						preferencesMenu.add(priceTypeMenu);
						priceTypeMenu.setText("Price Type");
					}
					{
						chromeDriverMenuItem = new JCheckBoxMenuItem ("Chrome Driver");
						chromeDriverMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								chromeDriverMenuItem.setSelected(true);
								firefoxDriverMenuItem.setSelected(false);
								edgeDriverMenuItem.setSelected(false);
								webDriver = "Chrome";
							}
						});
						firefoxDriverMenuItem = new JCheckBoxMenuItem ("Firefox Driver");
						firefoxDriverMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								chromeDriverMenuItem.setSelected(false);
								firefoxDriverMenuItem.setSelected(true);
								edgeDriverMenuItem.setSelected(false);
								webDriver = "Firefox";
							}
						});
						edgeDriverMenuItem = new JCheckBoxMenuItem ("Edge Driver");
						edgeDriverMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								chromeDriverMenuItem.setSelected(false);
								firefoxDriverMenuItem.setSelected(false);
								edgeDriverMenuItem.setSelected(true);
								webDriver = "Edge";
							}
						});
						
						premiumPriceTypeMenuItem = new JCheckBoxMenuItem("Premium Prices");
						premiumPriceTypeMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								hammerPriceTypeMenuItem.setSelected(false);
								premiumPriceTypeMenuItem.setSelected(true);
								priceType = "premium";
							}
						});
						
						hammerPriceTypeMenuItem = new JCheckBoxMenuItem("Hammer Prices");
						hammerPriceTypeMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								premiumPriceTypeMenuItem.setSelected(false);
								hammerPriceTypeMenuItem.setSelected(true);
								priceType = "hammer";
							}
						});
						
						webdriverMenu.add(chromeDriverMenuItem);
						webdriverMenu.add(firefoxDriverMenuItem);
						webdriverMenu.add(edgeDriverMenuItem);
						
						priceTypeMenu.add(premiumPriceTypeMenuItem);
						priceTypeMenu.add(hammerPriceTypeMenuItem);
					}
					
					downloadImagesMenuItem = new JCheckBoxMenuItem ("Download Images");
					preferencesMenu.add(downloadImagesMenuItem);
					downloadImagesMenuItem.setSelected(true);
					getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
					{
						tablePanel = new JPanel();
						tablePanel.setPreferredSize(new Dimension(500, 530));
						tablePanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
						getContentPane().add(tablePanel);
						tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
						{
							tableScrollPane = new JScrollPane();
							tableScrollPane.setViewportBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
							tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							tablePanel.add(tableScrollPane);
						}
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
							tableModel.addColumn("Auction ID");
							tableModel.addColumn("Sale Title");
							tableModel.addColumn("Auction House");
							tableModel.addColumn("Location");
							tableModel.addColumn("Sale Start");
							tableModel.addColumn("Sale End");
							tableModel.addColumn("<HTML>Missing<br>Artists");
							tableModel.addColumn("<HTML>Missing<br>Images");
							tableModel.addColumn("Published");
							tableModel.addColumn("<HTML>Lot<br>Count");
							tableModel.addColumn("Entry Person");
						}
						{
							JTableHeader header = table.getTableHeader();
							header.setPreferredSize(new Dimension(0, 50));
							header.setFont(new java.awt.Font("Segoe UI",1,15));
						}
					}
					{
						controlPanel = new JPanel();
						controlPanel.setPreferredSize(new Dimension(10, 60));
						controlPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
						getContentPane().add(controlPanel);
						controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						{
							btnAuctionDetails = new JButton("Auction Details");
							btnAuctionDetails.setPreferredSize(new Dimension(200, 30));
							btnAuctionDetails.setMnemonic('A');
							controlPanel.add(btnAuctionDetails);
							btnAuctionDetails.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									openAuctionDetails(e);
								}
							});
						}
						{
							btnViewSale = new JButton("View Sale");
							btnViewSale.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									openSaleDetails(e);
								}
							});
							btnViewSale.setPreferredSize(new Dimension(200, 30));
							btnViewSale.setMnemonic('V');
							controlPanel.add(btnViewSale);
						}
						{
							btnPublishSale = new JButton("Publish Sale");
							btnPublishSale.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									publishSale();
								}
							});
							btnPublishSale.setPreferredSize(new Dimension(200, 30));
							btnPublishSale.setMnemonic('P');
							controlPanel.add(btnPublishSale);
						}
						{
							btnRefreshTable = new JButton("Refresh Table");
							btnRefreshTable.setPreferredSize(new Dimension(200, 30));
							btnRefreshTable.setMnemonic('R');
							btnRefreshTable.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									int rowCount = tableModel.getRowCount();
									for (int i = rowCount - 1; i >= 0; i--) {
										tableModel.removeRow(i);
									}
									loadTableData(0, rowCount + 1);
								}
							});
							controlPanel.add(btnRefreshTable);
						}
					}
					{
						controlPanelPart1 = new JPanel();
						controlPanelPart1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
						getContentPane().add(controlPanelPart1);
						controlPanelPart1.setPreferredSize(new Dimension(10, 60));
						controlPanelPart1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
						{
							lblSearch = new JLabel("Search:");
							controlPanelPart1.add(lblSearch);
						}
						{
							searchTextField = new JTextField();
							controlPanelPart1.add(searchTextField);
							searchTextField.setPreferredSize(new Dimension(200, 30));
							{
								lblAddNext = new JLabel("Add Next: ");
								controlPanelPart1.add(lblAddNext);
							}
							{
								toRowsTextField = new JTextField();
								controlPanelPart1.add(toRowsTextField);
								toRowsTextField.setPreferredSize(new Dimension(6, 30));
								toRowsTextField.setColumns(10);
							}
							{
								lblTo = new JLabel("Rows. ");
								controlPanelPart1.add(lblTo);
							}
							{
								addRowsButton = new JButton("Add");
								controlPanelPart1.add(addRowsButton);
								addRowsButton.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										addRowsButtonActionPerformed();
									}
								});
								addRowsButton.setPreferredSize(new Dimension(100, 30));
							}
							
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
					downloadImagesMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if(downloadImagesMenuItem.isSelected()) {
								System.out.println("Selected");
								downloadImagesMenuItem.setSelected(true);
								uploadDataWithDownloadImages = true;
							} else {
								System.out.println("Not Selected");
								downloadImagesMenuItem.setSelected(false);
								uploadDataWithDownloadImages = false;
							}
						}
					});
				}
				toolsActionMenu = new JMenu();
				jMenuBar1.add(toolsActionMenu);
				toolsActionMenu.setText("Tools");
				{
					currencyUpdaterMenuItem = new JMenuItem();
					currencyUpdaterMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							currencyUpdaterMenuItemActionPerformed(evt);
						}
					});
					toolsActionMenu.add(currencyUpdaterMenuItem);
					currencyUpdaterMenuItem.setText("Update Currency Rates");
					toolsActionMenu.add(new JSeparator());
				
					jSeparator2 = new JSeparator();
					toolsActionMenu.add(jSeparator2);
					
					auctionHouseMenuItem = new JMenuItem();
					toolsActionMenu.add(auctionHouseMenuItem);
					auctionHouseMenuItem.setText("Manage Auction Houses");
					auctionHouseMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							auctionHouseMenuItemActionPerformed();
						}
					});
					
					artistMenuItem = new JMenuItem();
					toolsActionMenu.add(artistMenuItem);
					artistMenuItem.setText("Manage Artists");
					artistMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							artistMenuItemActionPerformed();
						}
					});
					
					toolsActionMenu.add(new JSeparator());
					
					toolsActionMenu.add(createArtistsIndexMenuItem);
					createArtistsIndexMenuItem.setText("Create Artists Index");
					
					toolsActionMenu.add(createArtworkIndexMenuItem);
					toolsActionMenu.add(createFreeSearchIndexMenuItem);
					toolsActionMenu.add(createAuctionCalendarIndexMenuItem);
					toolsActionMenu.add(createAuctionHouseIndexMenuItem);
					
					toolsActionMenu.add(jSeparator2);
					
					//toolsActionMenu.add(deleteBackblazeImageMenuItem);
					//toolsActionMenu.add(browseWebsiteMenu);
					
					exitMenuItem = new JMenuItem();
					toolsActionMenu.add(exitMenuItem);
					exitMenuItem.setText("Exit");
					exitMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							System.exit(0);
						}
					});
				}
				
				userAccountsMenu = new JMenu();
				jMenuBar1.add(userAccountsMenu);
				userAccountsMenu.setText("Subscribers");
				{
					manageUsersMenuItem = new JMenuItem();
					manageUsersMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							manageUsersMenuItemActionPerformed(evt);
						}
					});
					userAccountsMenu.add(manageUsersMenuItem);
					manageUsersMenuItem.setText("Manage Accounts");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void uploadMissinfArtistsActionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser(".");
		FileFilter jpegFilter = new ExtensionFileFilter(null, new String[] { "XLS" });
		fileChooser.addChoosableFileFilter(jpegFilter);
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile.getName());

			try {
				String filename = selectedFile.getPath();
				System.out.println("SELECTED FILENAME IS>>>" + filename);
				WorkbookSettings ws = new WorkbookSettings();
				ws.setLocale(new Locale("en", "EN"));

				Workbook workbook = Workbook.getWorkbook(new File(filename), ws);

				Sheet sheet = workbook.getSheet(0);
				String sql = "UPDATE fineart_artworks SET faa_artist_ID = ?, faa_artwork_record_updated = ?, faa_artwork_record_updatedby = ? WHERE faa_artwork_ID = ?";
				for(int row = 1; row <= 500000; row++) {
					String artworkId = sheet.getCell(0, row).getContents();
					String artistId = sheet.getCell(5, row).getContents();
					if(StringUtils.isEmpty(artworkId)) {
						break;
					}
					System.out.println("Row Id:" + row + ", Artwork Id:" + artworkId + ", Artist Id:" + artistId) ;
					
					jdbcTemplate.execute(sql,new PreparedStatementCallback<Boolean>() {  
					    @Override
						public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					    	ps.setInt(1, Integer.parseInt(artistId.trim()));  
					        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));  
					        ps.setString(3, "Satyam");
					        ps.setInt(4, Integer.parseInt(artworkId.trim()));
					              
					        return ps.execute();  
						}  
					    });  
				}
				workbook.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected void browseWebsiteMenuActionPerformed(ActionEvent evt) {
		
		try {
			Thread browserWorkerThread = new Thread(new BrowserWorkerThread(jdbcTemplate));
			browserWorkerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void resizeImagesActionPerformed(ActionEvent e) {
		try {
			String sql = "select upload_images_path from operations_team where username = '" + username + "'";
			String lastBrowsedPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("upload_images_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
			
			JFileChooser jfc = new JFileChooser(lastBrowsedPath);
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int returnVal = jfc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				
				String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String insertSql = "UPDATE operations_team SET upload_images_path='" + outputPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
				
				String lowQualityImageDirectoryPath = outputPath + "\\" + "low-quality";
				
				SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
		            public String doInBackground() {
		            	PastSalesUploader pastSalesUploader = null;
		            	pastSalesUploader = new PastSalesUploader(username, true, jdbcTemplate, webDriver, priceType, uploadDataWithDownloadImages, false, true);
						pastSalesUploader.convertLowQualityImages(outputPath, lowQualityImageDirectoryPath);
						return "";
		            }
		        };
		       // execute the background thread
		       worker.execute();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	protected void artistMenuItemActionPerformed() {
		try {
			ArtistSearchWindow artistSearchWindow = new ArtistSearchWindow(jdbcTemplate, username, artistsIndexPath);
			artistSearchWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void auctionHouseMenuItemActionPerformed() {
		try {
			AuctionHouseSearchWindow auctionHouseSearchWindow = new AuctionHouseSearchWindow(jdbcTemplate, username);
			auctionHouseSearchWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void exportSalesReportMenuItemActionPerformed() {
		try {
			ExportSalesReportWindow exportSalesReportWindow = new ExportSalesReportWindow(jdbcTemplate);
			exportSalesReportWindow.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void manageUsersMenuItemActionPerformed(ActionEvent evt) {
		GAIUserAccountsWindow usersAndSubscriptionsWindow = new GAIUserAccountsWindow(jdbcTemplate, username);
		usersAndSubscriptionsWindow.setVisible(true);
	}

	protected void addRowsButtonActionPerformed() {
		
		limit = StringUtils.isNotEmpty(toRowsTextField.getText()) ? Integer.parseInt(toRowsTextField.getText().trim()) : 0;
		
		loadTableData(this.offset, limit);
	}

	protected void openSaleDetails(ActionEvent e) {
		MainWindow mainWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int auctionId = (Integer)table.getValueAt(table.getSelectedRow(), 0);
					ViewSaleWindow frame = new ViewSaleWindow(auctionId, username, artistsIndexPath, jdbcTemplate, mainWindow);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void openAuctionDetails(ActionEvent e) {
		MainWindow mainWindow = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int auctionId = (Integer)table.getValueAt(table.getSelectedRow(), 0);
					AuctionDetailsWindow frame = new AuctionDetailsWindow(auctionId, username, jdbcTemplate, mainWindow);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void loadTableData(int offset, int limit) {
		String saleRecordSql = "";
		saleRecordSql = "SELECT fineart_auction_calendar.faac_auction_ID, fineart_auction_calendar.faac_auction_record_createdby, fineart_auction_calendar.faac_auction_sale_code, "
				+ "fineart_auction_calendar.faac_auction_start_date, fineart_auction_calendar.faac_auction_end_date, "
				+ "fineart_auction_calendar.faac_auction_title, core_auction_houses.cah_auction_house_name, "
				+ "core_auction_houses.cah_auction_house_location, (select count(*) from fineart_lots "
				+ "where fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID) as lot_count, "
				+ "fineart_auction_calendar.faac_auction_published, fineart_auction_calendar.faac_auction_image, "
				+ "fineart_auction_calendar.faac_auction_source, SUM(CASE WHEN ( fal_lot_image1 = '' OR fal_lot_image1 = 'na' ) "
				+ "AND ( fal_lot_image1 = '' OR fal_lot_image1 = 'na' ) AND ( fal_lot_image2 = '' OR fal_lot_image2 = 'na' ) "
				+ "AND ( fal_lot_image3 = '' OR fal_lot_image3 = 'na' ) AND ( fal_lot_image4 = '' OR fal_lot_image4 = 'na' ) "
				+ "AND ( fal_lot_image5 = '' OR fal_lot_image5 = 'na' ) THEN 1 ELSE 0 END ) AS missing_images, "
				+ "SUM( CASE WHEN fineart_artworks.faa_artist_ID = '1' THEN 1 ELSE 0 END ) AS missing_artist "
				+ "FROM fineart_lots INNER JOIN fineart_artworks ON fineart_lots.fal_artwork_ID = fineart_artworks.faa_artwork_ID "
				+ "RIGHT JOIN fineart_auction_calendar ON fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID "
				+ "INNER JOIN core_auction_houses ON fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID "
				+ "LEFT JOIN fineart_artists ON fineart_artworks.faa_artist_ID = fineart_artists.fa_artist_ID "
				+ "GROUP BY fineart_auction_calendar.faac_auction_ID ORDER BY fineart_auction_calendar.faac_auction_ID DESC limit " + offset + ", " + limit;
			
			
		jdbcTemplate.query(saleRecordSql, new ResultSetExtractor<Boolean>() {

			public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				while (resultSet.next()) {
					SaleSummaryRecord saleSummaryRecord = new SaleSummaryRecord();
					saleSummaryRecord.setAuctionId(resultSet.getInt("faac_auction_ID"));
					saleSummaryRecord.setSaleTitle(resultSet.getString("faac_auction_title"));
					saleSummaryRecord.setAuctionHouseName(resultSet.getString("cah_auction_house_name"));
					saleSummaryRecord.setAuctionHouseLocation(resultSet.getString("cah_auction_house_location"));
					saleSummaryRecord.setSaleStartDate(resultSet.getDate("faac_auction_start_date"));
					saleSummaryRecord.setSaleEndDate(resultSet.getDate("faac_auction_end_date"));
					saleSummaryRecord.setPublished(resultSet.getString("faac_auction_published"));
					saleSummaryRecord.setEntryPerson(resultSet.getString("faac_auction_record_createdby"));
					saleSummaryRecord.setMissingArtists(resultSet.getInt("missing_artist"));
					saleSummaryRecord.setMissingImages(resultSet.getInt("missing_images"));
					saleSummaryRecord.setLotCount(resultSet.getInt("lot_count"));
					
					tableModel.addRow(new Object[]{saleSummaryRecord.getAuctionId(), saleSummaryRecord.getSaleTitle(), 
							saleSummaryRecord.getAuctionHouseName(), saleSummaryRecord.getAuctionHouseLocation(), saleSummaryRecord.getSaleStartDate(), 
							saleSummaryRecord.getSaleEndDate(), saleSummaryRecord.getMissingArtists(), saleSummaryRecord.getMissingImages(), saleSummaryRecord.getPublished(), 
							saleSummaryRecord.getLotCount(), saleSummaryRecord.getEntryPerson()});
					
				}
				return true;
			}
		});
		
		table.getColumn("Sale Title").setPreferredWidth(500);
		try {
			table.setRowSelectionInterval(0, 0);
		} catch (java.lang.IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
		this.offset = offset + limit + 1;
	}
	
	protected void tableRowSelectionChanged() {
		try {
			if(table.getValueAt(table.getSelectedRow(), 8).toString().equals("yes")) {
				btnPublishSale.setText("Un-Publish Sale");
			} else {
				btnPublishSale.setText("Publish Sale");
			}
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void publishSale() {
		String updateSaleQuery = "";
		String updateLotsQuery = "";
		int selectedRow = table.getSelectedRow();
		int auctionId = (Integer)table.getValueAt(selectedRow, 0);
		if(table.getValueAt(table.getSelectedRow(), 8).toString().equals("yes")) {
			updateSaleQuery = "update fineart_auction_calendar SET faac_auction_published = 'no', faac_auction_record_updated = '" 
					+ new Timestamp(System.currentTimeMillis()) + "', faac_auction_record_updatedby = '" + username + "' where faac_auction_ID = '" + auctionId + "'";
			updateLotsQuery = "update fineart_lots set fal_lot_published = 'no', fal_lot_record_updated = '" + new Timestamp(System.currentTimeMillis()) 
					+ "', fal_lot_record_updatedby = '" + username + "' where fal_auction_ID = '" + auctionId + "'";
			btnPublishSale.setText("Publish Sale");
		} else {
			updateSaleQuery = "update fineart_auction_calendar SET faac_auction_published = 'yes', faac_auction_record_updated = '" 
					+ new Timestamp(System.currentTimeMillis()) + "', faac_auction_record_updatedby = '" + username + "' where faac_auction_ID = '" + auctionId + "'";
			updateLotsQuery = "update fineart_lots set fal_lot_published = 'yes', fal_lot_record_updated = '" + new Timestamp(System.currentTimeMillis()) 
					+ "', fal_lot_record_updatedby = '" + username + "' where fal_auction_ID = '" + auctionId + "'";
			btnPublishSale.setText("Un-Publish Sale");
		}
		jdbcTemplate.update(updateSaleQuery);
		jdbcTemplate.update(updateLotsQuery);
		loadTableData(offset, limit);
		table.setRowSelectionInterval(selectedRow, selectedRow);
	}

	@SuppressWarnings("deprecation")
	private void createArtistsIndexMenuItemActionPerformed(ActionEvent arg0) {
		
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure?", "Artists Local Index", 
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION) {
        	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
    			public String doInBackground() {
    				try {
    					
    					String sql = "select artists_index_path from operations_team where username = '" + username + "'";
    					
    					String artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

    						public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
    							if (resultSet.next()) {
    								return resultSet.getString("artists_index_path").toLowerCase();
    							} else {
    								return "";
    							}
    						}
    					});
    					
    					JFileChooser jfc = new JFileChooser(artistsIndexPath);
    					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    					
    					int returnVal = jfc.showOpenDialog(null);
    					if(returnVal == JFileChooser.APPROVE_OPTION) {
    						
    						//FileUtils.deleteDirectory(jfc.getSelectedFile());
    						
    						String path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
    						
    						String insertSql = "UPDATE operations_team SET artists_index_path='" + path + "/" + "artist" + "' WHERE  username = '" + username + "'";
    						jdbcTemplate.update(insertSql);
    						
    						File file = new File(path + "/" + "artist" + "1");
        					
        					String artistQuery = "SELECT count(*) FROM fineart_artists";
        					
        					Integer totalArtists = (Integer)jdbcTemplate.queryForObject(artistQuery, Integer.class);
        					
        					StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        			        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        			        IndexWriter writer = new IndexWriter(FSDirectory.open(file), config);
        			        
        			        progressBar.setMaximum(totalArtists);
        			        progressBar.setValue(0); 
        			        progressBar.setStringPainted(true);
        			        progressBar.setVisible(true);
        			        
        			        for(int count = 1; count < totalArtists; count = count + 10000) {
        			        	System.out.println("Count: " + count);
        			        	List<FineartArtistsData> suggestionsList = jdbcTemplate.query("select * from fineart_artists limit " + count + ",10000", new FineartArtistsDataMapper());
        				        for (FineartArtistsData autoSuggestData : suggestionsList) {
        				        	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        				        	document.add(new Field("fa_artist_ID", String.valueOf(autoSuggestData.getFa_artist_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_name", autoSuggestData.getFa_artist_name(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_name_prefix", autoSuggestData.getFa_artist_name_prefix(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_name_suffix", autoSuggestData.getFa_artist_name_suffix(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_birth_year", String.valueOf(autoSuggestData.getFa_artist_birth_year()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_death_year", String.valueOf(autoSuggestData.getFa_artist_death_year()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_birth_year_identifier", autoSuggestData.getFa_artist_birth_year_identifier(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_death_year_identifier", autoSuggestData.getFa_artist_death_year_identifier(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_nationality", autoSuggestData.getFa_artist_nationality(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_aka", autoSuggestData.getFa_artist_aka(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fa_artist_description", (autoSuggestData.getFa_artist_description() == null ? "": autoSuggestData.getFa_artist_description()) , Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            String artistName = document.get("fa_artist_name");
        						    String nationality = document.get("fa_artist_nationality");
        						    String birthYear = document.get("fa_artist_birth_year");
        						    String deathYear = document.get("fa_artist_death_year");
        						    
        						    StringBuffer artistNameWeb = new StringBuffer(artistName);
        						    if(!nationality.equals("na")) {
        						    	artistNameWeb.append(" – ").append(nationality);
        						    }
        						    if(Integer.parseInt(birthYear) != 0) {
        						    	artistNameWeb.append(" (").append(birthYear);
        						    } 
        						    if(Integer.parseInt(deathYear) != 0) {
        						    	artistNameWeb.append(" – ").append(deathYear).append(")");
        							} else {
        								if(Integer.parseInt(birthYear) != 0) {
        									artistNameWeb.append(")");
        								}
        							}
        						    
        				            document.add(new Field("fa_artist_slug", artistNameWeb.toString(), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            writer.updateDocument(new Term("id", String.valueOf(autoSuggestData.getFa_artist_ID())), document);
        				        }
        				        progressBar.setValue(count);
        				        System.out.println("Done with " + count);
        			        }
        			        System.out.println("Artist Index Creation Done!");
        			        progressBar.setVisible(false);
        			        writer.close();
    					}
    			    } catch (Exception e) {
    					e.printStackTrace();
    				}
    				return "";
    			}
    		};
    		// execute the background thread
    		worker.execute();
        }
	}
	
	@SuppressWarnings("deprecation")
	private void createArtworkIndexMenuItemActionPerformed(ActionEvent arg0) {
		
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure?", "Artwork Local Index", 
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION) {
        	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
    			public String doInBackground() {
    				IndexWriter writer = null;
    				try {
    					
    					String sql = "select artwork_index_path from operations_team where username = '" + username + "'";
    					
    					String artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

    						public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
    							if (resultSet.next()) {
    								return resultSet.getString("artwork_index_path").toLowerCase();
    							} else {
    								return "";
    							}
    						}
    					});
    					
    					JFileChooser jfc = new JFileChooser(artistsIndexPath);
    					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    					
    					int returnVal = jfc.showOpenDialog(null);
    					if(returnVal == JFileChooser.APPROVE_OPTION) {
    						
    						String path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
    						
    						File file = new File(path + "/" + "artwork" + "1");
    						
    						StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        			        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        			        writer = new IndexWriter(FSDirectory.open(file), config);
        			        
        			        String insertSql = "UPDATE operations_team SET artwork_index_path='" + path + "/" + "artwork" + "' WHERE  username = '" + username + "'";
    						jdbcTemplate.update(insertSql);
        					
        					String lastArtworkIDQuery = "SELECT faa_artwork_ID from fineart_artworks order by faa_artwork_ID DESC limit 1";
        					
        					Integer endId = (Integer)jdbcTemplate.queryForObject(lastArtworkIDQuery, Integer.class);
        					
        					progressBar.setMaximum(endId);
        			        progressBar.setValue(0); 
        			        progressBar.setStringPainted(true);
        			        progressBar.setVisible(true);
        			        
        			        for(int count = 1; count < endId; count = count + 10000) {
        			        	int thisLoopStart = count;
        			        	int thisLoopEndId = count + 10000;
        			        	/*if(thisLoopStart == 7300001) {
        			        		System.out.println("Here");
        			        	}*/
        			        	System.out.println("Generating Artwork Index from ID: " + thisLoopStart + ", to ID: " + thisLoopEndId);
        			        	List<FineartArtworkData> suggestionsList = jdbcTemplate.query("select * from fineart_artworks where faa_artwork_ID BETWEEN " + thisLoopStart + " and " + thisLoopEndId, new FineartArtworkDataMapper());
        				        for (FineartArtworkData autoSuggestData : suggestionsList) {
        				        	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        				        	document.add(new Field("faa_artist_ID", String.valueOf(autoSuggestData.getFaa_artist_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				        	
        				        	document.add(new Field("faa_artwork_ID", String.valueOf(autoSuggestData.getFaa_artwork_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_title", autoSuggestData.getFaa_artwork_title(), Field.Store.YES, Field.Index.ANALYZED));
        				        	document.add(new Field("faa_artwork_slug", autoSuggestData.getFaa_artwork_title() + "-" + String.valueOf(autoSuggestData.getFaa_artwork_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            writer.updateDocument(new Term("id", String.valueOf(autoSuggestData.getFaa_artwork_ID())), document);
        				        }
        				        progressBar.setValue(count);
        				        System.out.println("Done with " + count);
        			        }
        			        System.out.println("Artwork Index Creation Done!");
        			        progressBar.setVisible(false);
        			        writer.close();
    					}
    			    } catch (Exception e) {
    			    	try {
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
    					e.printStackTrace();
    				}
    				return "";
    			}
    		};
    		// execute the background thread
    		worker.execute();
        }
	}
	
	@SuppressWarnings("deprecation")
	private void createAuctionHouseCalendarMenuItemActionPerformed(ActionEvent evt) {
		
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure?", "Auction Calendar Local Index", 
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION) {
        	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
    			public String doInBackground() {
    				try {
    					
    					String sql = "select artists_index_path from operations_team where username = '" + username + "'";
    					
    					String artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

    						public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
    							if (resultSet.next()) {
    								return resultSet.getString("artists_index_path").toLowerCase();
    							} else {
    								return "";
    							}
    						}
    					});
    					
    					JFileChooser jfc = new JFileChooser(artistsIndexPath);
    					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    					
    					int returnVal = jfc.showOpenDialog(null);
    					if(returnVal == JFileChooser.APPROVE_OPTION) {
    						
    						String path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
    						
    						File file = new File(path + "/" + "auction-calendar" + "1");
        					
        					String artistQuery = "SELECT count(*) FROM fineart_auction_calendar";
        					
        					Integer totalAuctionCalendar = (Integer)jdbcTemplate.queryForObject(artistQuery, Integer.class);
        					
        					StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        			        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        			        IndexWriter writer = new IndexWriter(FSDirectory.open(file), config);
        			        
        			        progressBar.setMaximum(totalAuctionCalendar);
        			        progressBar.setValue(0); 
        			        progressBar.setStringPainted(true);
        			        progressBar.setVisible(true);
        			        
        			        for(int count = 1; count < totalAuctionCalendar; count = count + 10000) {
        			        	System.out.println("Count: " + count);
        			        	List<AuctionCalendarData> suggestionsList = jdbcTemplate.query("select * FROM fineart_auction_calendar limit " + count + ",10000", new AuctionCalendarDataMapper());
        				        for (AuctionCalendarData autoSuggestData : suggestionsList) {
        				        	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        				        	document.add(new Field("faac_auction_ID", String.valueOf(autoSuggestData.getFaac_auction_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_sale_code", autoSuggestData.getFaac_auction_sale_code(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_house_ID", String.valueOf(autoSuggestData.getFaac_auction_house_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_title", autoSuggestData.getFaac_auction_title(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_lot_count", String.valueOf(autoSuggestData.getFaac_auction_lot_count()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_published", autoSuggestData.getFaac_auction_published(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_start_date", String.valueOf(autoSuggestData.getFaac_auction_start_date()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_end_date", String.valueOf(autoSuggestData.getFaac_auction_end_date()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("faac_auction_slug", autoSuggestData.getFaac_auction_title() + "-" + autoSuggestData.getFaac_auction_sale_code() 
        				            + "-" + String.valueOf(autoSuggestData.getFaac_auction_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            writer.updateDocument(new Term("id", String.valueOf(autoSuggestData.getFaac_auction_ID())), document);
        				        }
        				        progressBar.setValue(count);
        				        System.out.println("Done with " + count);
        			        }
        			        System.out.println("Auction Calendar Index Creation Done!");
        			        progressBar.setVisible(false);
        			        writer.close();
    					}
    			    } catch (Exception e) {
    					e.printStackTrace();
    				}
    				return "";
    			}
    		};
    		// execute the background thread
    		worker.execute();
        }
	}
	
	@SuppressWarnings("deprecation")
	private void createAuctionHouseIndexMenuItemActionPerformed(ActionEvent evt) {
		
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure?", "Auction House Local Index", 
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION) {
        	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
    			public String doInBackground() {
    				try {
    					
    					String sql = "select artists_index_path from operations_team where username = '" + username + "'";
    					
    					String artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

    						public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
    							if (resultSet.next()) {
    								return resultSet.getString("artists_index_path").toLowerCase();
    							} else {
    								return "";
    							}
    						}
    					});
    					
    					JFileChooser jfc = new JFileChooser(artistsIndexPath);
    					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    					
    					int returnVal = jfc.showOpenDialog(null);
    					if(returnVal == JFileChooser.APPROVE_OPTION) {
    						
    						String path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
    						
    						File file = new File(path + "/" + "auction-house" + "1");
        					
        					String artistQuery = "SELECT count(*) FROM core_auction_houses";
        					
        					Integer totalAuctionHouses = (Integer)jdbcTemplate.queryForObject(artistQuery, Integer.class);
        					
        					StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        			        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        			        IndexWriter writer = new IndexWriter(FSDirectory.open(file), config);
        			        
        			        progressBar.setMaximum(totalAuctionHouses);
        			        progressBar.setValue(0); 
        			        progressBar.setStringPainted(true);
        			        progressBar.setVisible(true);
        			        
        			        for(int count = 1; count < totalAuctionHouses; count = count + 10000) {
        			        	System.out.println("Count: " + count);
        			        	List<AuctionHouseData> suggestionsList = jdbcTemplate.query("select * from core_auction_houses limit " + count + ",10000", new AuctionHouseDataMapper());
        				        for (AuctionHouseData autoSuggestData : suggestionsList) {
        				        	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        				        	document.add(new Field("cah_auction_house_ID", String.valueOf(autoSuggestData.getCah_auction_house_ID()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_name", autoSuggestData.getCah_auction_house_name(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_country", autoSuggestData.getCah_auction_house_country(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_location", autoSuggestData.getCah_auction_house_location(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_currency_code", String.valueOf(autoSuggestData.getCah_auction_house_currency_code()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_website", String.valueOf(autoSuggestData.getCah_auction_house_website()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_slug", autoSuggestData.getCah_auction_house_name() + "-" + autoSuggestData.getCah_auction_house_country() 
        				            + "-" + String.valueOf(autoSuggestData.getCah_auction_house_location()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            writer.updateDocument(new Term("id", String.valueOf(autoSuggestData.getCah_auction_house_ID())), document);
        				        }
        				        progressBar.setValue(count);
        				        System.out.println("Done with " + count);
        			        }
        			        System.out.println("Auction House Index Creation Done!");
        			        progressBar.setVisible(false);
        			        writer.close();
    					}
    			    } catch (Exception e) {
    					e.printStackTrace();
    				}
    				return "";
    			}
    		};
    		// execute the background thread
    		worker.execute();
        }
	}
	
	@SuppressWarnings("deprecation")
	private void createFreeSearchMenuItemActionPerformed(ActionEvent arg0) {
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure?", "Sale-Lots Local Index", 
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        if(PromptResult==JOptionPane.YES_OPTION) {
        	SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
    			public String doInBackground() {
    				IndexWriter writer = null;
    				try {
    					
    					String sql = "select freesearch_index_path from operations_team where username = '" + username + "'";
    					
    					String freeSearchIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

    						public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
    							if (resultSet.next()) {
    								return resultSet.getString("freesearch_index_path").toLowerCase();
    							} else {
    								return "";
    							}
    						}
    					});
    					
    					JFileChooser jfc = new JFileChooser(freeSearchIndexPath);
    					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    					
    					int returnVal = jfc.showOpenDialog(null);
    					if(returnVal == JFileChooser.APPROVE_OPTION) {
    						
    						//FileUtils.deleteDirectory(jfc.getSelectedFile());
    						
    						String path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
    						
    						File file = new File(path + "/" + "free-search" + "1");
    						
    						StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        			        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        			        writer = new IndexWriter(FSDirectory.open(file), config);
    						
    						String insertSql = "UPDATE operations_team SET freesearch_index_path='" + path + "/" + "free-search" + "' WHERE  username = '" + username + "'";
    						jdbcTemplate.update(insertSql);
    						
    						String lastLotIDQuery = "select fal_lot_ID from fineart_lots order by fal_lot_ID DESC limit 1";
        					
        					Integer endId = (Integer)jdbcTemplate.queryForObject(lastLotIDQuery, Integer.class);
        					
        					progressBar.setMaximum(endId);
        			        progressBar.setValue(0); 
        			        progressBar.setStringPainted(true);
        			        progressBar.setVisible(true);
        			        
        			        final String searchSelectQuery = "SELECT fineart_artists.fa_artist_ID, fineart_artists.fa_artist_name, fineart_artists.fa_artist_name_prefix, "
        			    			+ "fineart_artists.fa_artist_name_suffix, fineart_artists.fa_artist_birth_year, fineart_artists.fa_artist_death_year, "
        			    			+ "fineart_artists.fa_artist_birth_year_identifier,  fineart_artists.fa_artist_birth_year_precision, "
        			    			+ "fineart_artists.fa_artist_death_year_identifier, fineart_artists.fa_artist_death_year_precision, fineart_artists.fa_artist_aka, "
        			    			+ "fineart_artists.fa_artist_nationality, fineart_artists.fa_artist_description, fineart_artworks.faa_artwork_ID, "
        			    			+ "fineart_artworks.faa_artwork_title, fineart_artworks.faa_artwork_requires_review, fineart_artworks.faa_artwork_description, "
        			    			+ "fineart_artworks.faa_artist_ID, fineart_artworks.faa_artist2_ID, fineart_artworks.faa_artist3_ID, fineart_artworks.faa_artist4_ID, "
        			    			+ "fineart_artworks.faa_artwork_category, fineart_artworks.faa_artwork_material, "
        			    			+ "fineart_artworks.faa_artwork_edition, fineart_artworks.faa_artwork_exhibition, fineart_artworks.faa_artwork_literature, "
        			    			+ "fineart_artworks.faa_artwork_height, fineart_artworks.faa_artwork_width, fineart_artworks.faa_artwork_depth, "
        			    			+ "fineart_artworks.faa_arwork_measurement_unit, fineart_artworks.faa_artwork_size_details, fineart_artworks.faa_artwork_markings, "
        			    			+ "fineart_artworks.faa_artwork_start_year, fineart_artworks.faa_artwork_start_year_identifier, "
        			    			+ "fineart_artworks.faa_artwork_start_year_precision, fineart_artworks.faa_artwork_end_year, "
        			    			+ "fineart_artworks.faa_artwork_end_year_identifier, fineart_artworks.faa_artwork_end_year_precision, "
        			    			+ "fineart_lots.fal_lot_ID, fineart_lots.fal_lot_no, fineart_lots.fal_sub_lot_no, fineart_lots.fal_artwork_ID, "
        			    			+ "fineart_lots.fal_auction_ID, fineart_lots.fal_lot_sale_date, fineart_lots.fal_lot_high_estimate, "
        			    			+ "fineart_lots.fal_lot_low_estimate, fineart_lots.fal_lot_high_estimate_USD, fineart_lots.fal_lot_low_estimate_USD, "
        			    			+ "fineart_lots.fal_lot_sale_price, fineart_lots.fal_lot_sale_price_USD, fineart_lots.fal_lot_price_type, "
        			    			+ "fineart_lots.fal_lot_status, fineart_lots.fal_lot_condition, fineart_lots.fal_lot_provenance, "
        			    			+ "fineart_lots.fal_lot_image1, fineart_lots.fal_lot_image2, fineart_lots.fal_lot_image3, fineart_lots.fal_lot_image4, "
        			    			+ "fineart_lots.fal_lot_image5, fineart_lots.fal_lot_published, `core_auction_houses`.`cah_auction_house_ID`, "
        			    			+ "`core_auction_houses`.`cah_auction_house_name`, `core_auction_houses`.`cah_auction_house_country`, "
        			    			+ "`core_auction_houses`.`cah_auction_house_location`, `core_auction_houses`.`cah_auction_house_currency_code`, "
        			    			+ "`core_auction_houses`.`cah_auction_house_website`, fineart_auction_calendar.faac_auction_ID, "
        			    			+ "fineart_auction_calendar.faac_auction_sale_code, fineart_auction_calendar.faac_auction_house_ID, "
        			    			+ "fineart_auction_calendar.faac_auction_title, fineart_auction_calendar.faac_auction_start_date, "
        			    			+ "fineart_auction_calendar.faac_auction_end_date, fineart_auction_calendar.faac_auction_lot_count, "
        			    			+ "fineart_auction_calendar.faac_auction_published, "
        			    			+ "DateDiff(fineart_auction_calendar.faac_auction_start_date, CurDate()) AS left_days, "
        			    			+ "'none' as marker FROM fineart_artists INNER JOIN fineart_artworks ON fineart_artists.fa_artist_ID = fineart_artworks.faa_artist_ID "
        			    			+ "INNER JOIN fineart_lots ON fineart_artworks.faa_artwork_ID = fineart_lots.fal_artwork_ID INNER JOIN fineart_auction_calendar ON "
        			    			+ "fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID INNER JOIN core_auction_houses ON "
        			    			+ "fineart_auction_calendar.faac_auction_house_ID = core_auction_houses.cah_auction_house_ID ";
        			        
        			        for(int count = 1; count <= endId; count = count + 10000) {
        			        	int thisLoopStart = count;
        			        	int thisLoopEndId = count + 10000;
        			        	System.out.println("Generating Free Search Index from ID: " + thisLoopStart + ", to ID: " + thisLoopEndId);
        			        	List<SearchResultsData> suggestionsList = jdbcTemplate.query(searchSelectQuery + " where fineart_lots.fal_lot_ID BETWEEN " + thisLoopStart + " and " + thisLoopEndId, new SearchResultsDataMapper());
        			        	
        				        for (SearchResultsData autoSuggestData : suggestionsList) {
        				        	
        				        	org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        				        	
        				        	document.add(new Field("fa_artist_ID", String.valueOf(autoSuggestData.getArtistId()), Field.Store.YES, Field.Index.ANALYZED));
        				        	document.add(new Field("fa_artist_name", autoSuggestData.getArtistFullName(), Field.Store.YES, Field.Index.ANALYZED));
        				        	document.add(new Field("fa_artist_nationality", autoSuggestData.getArtistNationality(), Field.Store.YES, Field.Index.ANALYZED));
        				        	document.add(new Field("fa_artist_aka", autoSuggestData.getArtistAka(), Field.Store.YES, Field.Index.ANALYZED));
        				        	
        				        	document.add(new Field("faa_artwork_ID", String.valueOf(autoSuggestData.getArtworkId()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_title", autoSuggestData.getArtworkTitle(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_description", autoSuggestData.getArtworkDescription(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artist_ID", String.valueOf(autoSuggestData.getArtistId()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_category", String.valueOf(autoSuggestData.getArtworkCategory()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_material", String.valueOf(autoSuggestData.getArtworkMedium()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_edition", autoSuggestData.getArtworkEdition(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_exhibition", autoSuggestData.getArtworkExhibition(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_literature", autoSuggestData.getArtworkLiterature(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_markings", autoSuggestData.getArtworkMarkings(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faa_artwork_slug", autoSuggestData.getArtworkTitle() + "-" + String.valueOf(autoSuggestData.getArtworkId()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("fal_lot_ID", String.valueOf(autoSuggestData.getLotId()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("fal_lot_provenance", autoSuggestData.getArtworkProvenance(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("fal_lot_condition", autoSuggestData.getArtworkCondition(), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("faac_auction_ID", autoSuggestData.getAuctionNumber(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_sale_code", autoSuggestData.getAuctionSaleCode(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_title", autoSuggestData.getAuctionTitle(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("faac_auction_start_date", autoSuggestData.getAuctionSaleStartDate(), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("faac_auction_slug", autoSuggestData.getAuctionTitle() + "-" + autoSuggestData.getAuctionSaleCode() 
        				            + "-" + String.valueOf(autoSuggestData.getAuctionNumber()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("cah_auction_house_ID", String.valueOf(autoSuggestData.getAuctionHouseId()), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_currency_code", autoSuggestData.getAuctionHouseCurrencyCode(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_name", autoSuggestData.getAuctionHouseTitle(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_country", autoSuggestData.getAuctionHouseCountry(), Field.Store.YES, Field.Index.ANALYZED));
        				            document.add(new Field("cah_auction_house_location", autoSuggestData.getAuctionHouseLocation(), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            document.add(new Field("cah_auction_house_slug", autoSuggestData.getAuctionHouseTitle() + "-" + autoSuggestData.getAuctionHouseCountry() 
        				            + "-" + String.valueOf(autoSuggestData.getAuctionHouseLocation()), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            String artistName = document.get("fa_artist_name");
        						    String nationality = document.get("fa_artist_nationality");
        						    String birthYear = autoSuggestData.getArtistYearBirth();
        						    String deathYear = autoSuggestData.getArtistYearDeath();
        						    
        						    StringBuffer artistNameWeb = new StringBuffer(artistName);
        						    if(!nationality.equals("na")) {
        						    	artistNameWeb.append(" – ").append(nationality);
        						    }
        						    if(Integer.parseInt(birthYear) != 0) {
        						    	artistNameWeb.append(" (").append(birthYear);
        						    } 
        						    if(Integer.parseInt(deathYear) != 0) {
        						    	artistNameWeb.append(" – ").append(deathYear).append(")");
        							} else {
        								if(Integer.parseInt(birthYear) != 0) {
        									artistNameWeb.append(")");
        								}
        							}
        						    
        				            document.add(new Field("fa_artist_slug", artistNameWeb.toString(), Field.Store.YES, Field.Index.ANALYZED));
        				            
        				            
        				            writer.updateDocument(new Term("id", String.valueOf(autoSuggestData.getLotId())), document);
        				            
        				        }
        				        progressBar.setValue(count);
        				        System.out.println("Done with " + count);
        				    }
        			        System.out.println("Free Search Index Creation Done!");
        			        progressBar.setVisible(false);
        			        writer.close();
    					}
    			    } catch (Exception e) {
    			    	try {
    			    		e.printStackTrace();
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
    					e.printStackTrace();
    				}
    				return "";
    			}
    		};
    		// execute the background thread
    		worker.execute();
        }
	}
	
	private void currencyUpdaterMenuItemActionPerformed(ActionEvent arg0) {
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure to update the latest currency rates in the database?", "Currency Rates Update", 
        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
        
        if(PromptResult==JOptionPane.YES_OPTION) {
        	CurrencyRatesUploader currencyRatesUploader = new CurrencyRatesUploader();
        	try {
        		
        		String[] currencies = {"INR","DEM","CNY","KRW","AUD","CHF","CAD","CZK","EUR","GBP","HKD","SEK"};
        		String query = "insert into currency_exchange_rate (cexr_base_currency, cexr_date, cexr_usd_rate, cexr_record_created, "
    					+ "cexr_record_updated, cexr_record_createdby, cexr_record_updatedby) values (?, ?, ?, ?, ?, ?, ?)";
        		
        		for(String currency : currencies) {
        			Calendar.getInstance().get(Calendar.MONTH);
            		
            		URL url = new URL("https://fx.sauder.ubc.ca/cgi/fxdata");
                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("b", "USD");
                    params.put("c", currency);
                    params.put("y", "monthly");
                    params.put("q", "price");
                    params.put("f", "HTML");
                    params.put("fm", Calendar.getInstance().get(Calendar.MONTH));
                    params.put("fy", Calendar.getInstance().get(Calendar.YEAR));
                    
                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);
            		
            		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
    				
    				String output;
    				StringBuilder stringBuilder = new StringBuilder();
    				while ((output = br.readLine()) != null) {
    					stringBuilder.append(output);
    				}
            		
            		output = stringBuilder.toString();
            		Document doc = Jsoup.parse(output);
            		String rate = doc.select("td[align=right]").last().text().trim();
            		
            		
            		System.out.println("Currency: " + currency + " Rate: " + rate);
            		
            		currencyRatesUploader.updateCurrency(query, currency, System.currentTimeMillis(), Double.parseDouble(rate), jdbcTemplate);
            	}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	updateMadCurrency(currencyRatesUploader);
        	JOptionPane.showMessageDialog(this,"Currency rates update done.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
        	return;
        }
	}
	
	private void updateMadCurrency(CurrencyRatesUploader currencyRatesUploader) {
		try {
			String[] currencies = {"MAD"};
    		String query = "insert into currency_exchange_rate (cexr_base_currency, cexr_date, cexr_usd_rate, cexr_record_created, "
					+ "cexr_record_updated, cexr_record_createdby, cexr_record_updatedby) values (?, ?, ?, ?, ?, ?, ?)";
			
			for(String currency : currencies) {
				URL url = new URL("https://api.ofx.com/PublicSite.ApiService/SpotRateHistory/allTime/USD/" + currency + "?DecimalPlaces=6&ReportingInterval=monthly");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				
				String output;
				if ((output = br.readLine()) != null) {
					Gson gson = new Gson();
					Rates rates = gson.fromJson(output, Rates.class);
					Double rate = rates.getHistoricalPoints().get(rates.getHistoricalPoints().size() - 1).getInverseInterbankRate();
					Long epochTime = rates.getHistoricalPoints().get(rates.getHistoricalPoints().size() - 1).getPointInTime();
					
					currencyRatesUploader.updateCurrency(query, currency, epochTime, rate, jdbcTemplate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadPastAuctionsMenuItemActionPerformed() {
		try {
			String sql = "select scraping_upload_path from operations_team where username = '" + username + "'";
			String lastBrowsedPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("scraping_upload_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
			JFileChooser jfc = new JFileChooser(lastBrowsedPath);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			int returnVal = jfc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String insertSql = "UPDATE operations_team SET scraping_upload_path='" + outputPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						PastSalesUploader pastSalesUploader = null;
						try {
							pastSalesUploader = new PastSalesUploader(username, true, jdbcTemplate, webDriver, priceType, uploadDataWithDownloadImages, false, true);
							pastSalesUploader.initUploading(jfc.getSelectedFile().getAbsolutePath(), jfc.getSelectedFile().getParent());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});				
			} else {
				return;
			}
		} catch(NullPointerException npe) {}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void uploadUpcomingAuctionsMenuItemActionPerformed() {
		try {
			String sql = "select scraping_upload_path from operations_team where username = '" + username + "'";
			String lastBrowsedPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("scraping_upload_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
			JFileChooser jfc = new JFileChooser(lastBrowsedPath);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			int returnVal = jfc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String insertSql = "UPDATE operations_team SET scraping_upload_path='" + outputPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UpcomingSalesUploader upcomingSalesUploader = null;
						try {
							upcomingSalesUploader = new UpcomingSalesUploader(username, jdbcTemplate, webDriver, uploadDataWithDownloadImages, false, true);
							upcomingSalesUploader.initUploading(jfc.getSelectedFile().getAbsolutePath(), jfc.getSelectedFile().getParent());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});				
			} else {
				return;
			}
		} catch(NullPointerException npe) {}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void downloadImagesOnlyMenuItemActionPerformed() {
		try {
			String sql = "select scraping_upload_path from operations_team where username = '" + username + "'";
			String lastBrowsedPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("scraping_upload_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
			
			JFileChooser jfc = new JFileChooser(lastBrowsedPath);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			int returnVal = jfc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String insertSql = "UPDATE operations_team SET scraping_upload_path='" + outputPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
				
				String ObjButtons[] = {"Yes","No","Cancel"};
		        int PromptResult = JOptionPane.showOptionDialog(null,"Yes = For Auction Calendar, No = For Auction Results.", "Choose An Option", 
		        		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
		        if(PromptResult==JOptionPane.YES_OPTION) {
		        	SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							UpcomingSalesUploader fineArtCsvUploader = null;
							try {
								fineArtCsvUploader = new UpcomingSalesUploader(username, jdbcTemplate, webDriver, uploadDataWithDownloadImages, true, true);
								fineArtCsvUploader.initUploading(jfc.getSelectedFile().getAbsolutePath(), jfc.getSelectedFile().getParent());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});	
		        } else if(PromptResult==JOptionPane.NO_OPTION) {
		        	SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							PastSalesUploader fineArtCsvUploader = null;
							try {
								fineArtCsvUploader = new PastSalesUploader(username, true, jdbcTemplate, webDriver, priceType, uploadDataWithDownloadImages, true, true);
								fineArtCsvUploader.initUploading(jfc.getSelectedFile().getAbsolutePath(), jfc.getSelectedFile().getParent());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});		
		        } else {
		        	
		        }
						
			} else {
				return;
			}
		} catch(NullPointerException npe) {}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void uploadImagesActionPerformed(ActionEvent e) {
		try {
			String sql = "select upload_images_path from operations_team where username = '" + username + "'";
			String lastBrowsedPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("upload_images_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
			JFileChooser jfc = new JFileChooser(lastBrowsedPath);
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int returnVal = jfc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String outputPath = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
				String insertSql = "UPDATE operations_team SET upload_images_path='" + outputPath + "' WHERE  username = '" + username + "'";
				jdbcTemplate.update(insertSql);
				
				CsvImageUploader csvImagesUploader = new CsvImageUploader();
				csvImagesUploader.initUploading(jfc.getSelectedFile().getAbsolutePath(), jfc.getSelectedFile().getParent());
			} else {
				return;
			}
		} catch(NullPointerException npe) {}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getUsername() {
		return userFriendlyName;
	}

	public void setUsername(String username) {
		this.userFriendlyName = username;
	}

	public int getFromRow() {
		return offset;
	}

	public void setFromRow(int fromRow) {
		this.offset = fromRow;
	}
	
	public int getToRow() {
		return limit;
	}

	public void setToRow(int toRow) {
		this.limit = toRow;
	}
	
	private void loadArtistIndexPath() {
		try {
			String sql = "select artists_index_path from operations_team where username = '" + username + "'";
			
			artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
	
				public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
					if (resultSet.next()) {
						return resultSet.getString("artists_index_path").toLowerCase();
					} else {
						return "";
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ExtensionFileFilter extends FileFilter {
		String description;
		String extensions[];

		public ExtensionFileFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0] + "{ " + extensions.length
						+ "} ";
			} else {
				this.description = description;
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		public String getDescription() {
			return description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length()
							- extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}
}