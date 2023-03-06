package com.core.fineart;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jauntium.Browser;

public class BrowserWorkerThread implements Runnable {
	
	private JdbcTemplate jdbcTemplate;

	public BrowserWorkerThread(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run() {
		try {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			Browser browser = new Browser(new ChromeDriver());
			
			int random6 = FineartUtils.generateOTP();
			
			System.out.println("Browsing Website for " + random6 + " - " + (random6 + 1000) );
			String artworksSitempaQuery = "SELECT fineart_artworks.faa_artwork_title, fineart_artworks.faa_artwork_ID FROM fineart_lots "
					+ "INNER JOIN fineart_artworks ON fineart_lots.fal_artwork_ID = fineart_artworks.faa_artwork_ID INNER JOIN fineart_auction_calendar "
					+ "ON fineart_lots.fal_auction_ID = fineart_auction_calendar.faac_auction_ID WHERE fineart_auction_calendar.faac_auction_published = 'yes' "
					+ "AND faa_artwork_title != '' AND fineart_lots.fal_lot_ID BETWEEN " + random6 + " AND " + (random6 + 1000);
	    	
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(artworksSitempaQuery);
			Collections.shuffle(rows);
			
	    	if(rows.size() > 0) {
	    		int rowCounter = 1;
	    		
	    		for(Map<String, Object> sitemapData : rows) {
	    			String artworkTitle = (String)sitemapData.get("faa_artwork_title");
	    			String artworkID = String.valueOf((Integer)sitemapData.get("faa_artwork_ID"));
	    			String auctionLotSlug = artworkTitle + "-" + artworkID;
	    			try {
	    				String auctionLotUrlStr = "https://www.globalartindex.com/" + "auction-lot-" + auctionLotSlug.trim();
	        			auctionLotUrlStr = auctionLotUrlStr.replaceAll(" ", "-");
	        			
	        			if(rowCounter > 4) {
	        				browser.close();
	        				browser = new Browser(new ChromeDriver());
		        			rowCounter = 1;
	        			} else {
	        				rowCounter++;
	        			}
	        			browser.visit(auctionLotUrlStr);
	        			
	        			Thread.sleep(30 * 1000);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
	    		}
	    	}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
