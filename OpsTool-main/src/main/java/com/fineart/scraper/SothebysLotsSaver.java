package com.fineart.scraper;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

public class SothebysLotsSaver {
	
	public SothebysLotsSaver(int pageStart, int pageEnd, int auctionSerialStart, int auctionSerialEnd) {

		String pageLink = "https://www.sothebys.com/en/results?from=&to=&f2=00000164-609b-d1db-a5e6-e9ff01230000&f2=00000164-609b-d1db-a5e6-e9ff08ab0000&f2=00000164-609b-d1db-a5e6-e9ff0b150000&f2=00000164-609a-d1db-a5e6-e9fff79f0000&f2=00000164-609b-d1db-a5e6-e9ff043c0000&f2=00000164-609a-d1db-a5e6-e9fffe5f0000&f2=00000164-609a-d1db-a5e6-e9fffdf80000&f2=00000164-609b-d1db-a5e6-e9ff0a800000&f2=00000164-609b-d1db-a5e6-e9ff06270000&f2=00000164-609a-d1db-a5e6-e9fff8660000&f2=00000164-609b-d1db-a5e6-e9ff08440000&f2=00000164-609b-d1db-a5e6-e9ff0ba60000&f2=00000164-609a-d1db-a5e6-e9fffd2c0000&f2=00000164-609a-d1db-a5e6-e9fff6760000&f2=00000164-609a-d1db-a5e6-e9fffa760000&f2=00000164-609a-d1db-a5e6-e9ffff270000&f2=00000164-609b-d1db-a5e6-e9ff07220000&f2=00000164-609b-d1db-a5e6-e9ff068c0000&f2=00000164-609b-d1db-a5e6-e9ff09100000&f2=00000164-609b-d1db-a5e6-e9ff055d0000&f2=00000164-609b-d1db-a5e6-e9ff0a350000&f2=00000164-609b-d1db-a5e6-e9ff04fc0000&f2=00000164-609b-d1db-a5e6-e9ff02b50000&f2=00000164-609a-d1db-a5e6-e9fff8ca0000&f2=00000164-609b-d1db-a5e6-e9ff07e20000&f2=00000164-609a-d1db-a5e6-e9fffadc0000&f2=00000164-609a-d1db-a5e6-e9fff5b50000&f2=00000164-609a-d1db-a5e6-e9fff73c0000&f2=00000164-609b-d1db-a5e6-e9ff03900000&f2=00000164-609b-d1db-a5e6-e9ff09a60000&f2=00000164-609a-d1db-a5e6-e9fffec40000&f2=00000164-609a-d1db-a5e6-e9fff6150000&f3=LIVE&f3=ONLINE&q=&p=";
		String path = "";
		
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int returnVal = jfc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
		}
		
		for(int pageCounter = pageStart; pageCounter <= pageEnd; pageCounter++) { // page number loop
			new File(path + "/" + pageCounter).mkdir();
			String countedPageLink = pageLink + pageCounter;
			UserAgent userAgent1 = new UserAgent();
			try {
				userAgent1.visit(countedPageLink);
			} catch (ResponseException e2) {
				e2.printStackTrace();
			}
			int auctionListItemCounter = 1;
			Elements cardContainer = userAgent1.doc.findEvery("<a class='Card-info-container'");
			for(Element auctionListElement : cardContainer) { // Auction Listing Loop
				if(auctionListItemCounter < auctionSerialStart || auctionListItemCounter > auctionSerialEnd) {
					System.out.println("Inside continue. auctionListItemCounter: " + auctionListItemCounter);
					auctionListItemCounter++;
					continue;
				}
				System.out.println("Outside continue. auctionListItemCounter: " + auctionListItemCounter);
				AuctionBean auctionBean = new AuctionBean();
				try {
					populateAuctionDetails(auctionListElement, auctionBean);
				} catch (NotFound | ParseException e1) {
					e1.printStackTrace();
				}
				String auctionUrl = auctionListElement.getAtString("href");
				UserAgent userAgent2 = new UserAgent();
				try {
					userAgent2.visit(auctionUrl);
				} catch (ResponseException e1) {
					e1.printStackTrace();
				}
				String saleNumber = "";
				try {
					saleNumber = userAgent2.doc.findFirst("<div class='css-1ewow1l'>").findFirst("p").getTextContent().replaceAll("Sale", "").trim();
				} catch (NotFound e1) {
					System.out.println("Could not find Sale number with new style parsing. Trying the old style now...");
				}
				if(StringUtils.isEmpty(saleNumber)) {
					try {
						saleNumber = userAgent2.doc.findFirst("<div class='eventdetail-saleinfo'>").findFirst("span").getTextContent().replaceAll("Sale Number: ", "").trim();
					} catch (NotFound e) {
						System.out.println("Could not find Sale number with old style parsing too. Leaving the sale number blank now...");
					}
				}
				auctionBean.setFaac_auction_sale_code(saleNumber);
				
				String fileName = auctionBean.faac_auction_title + "-" + auctionBean.getFaac_auction_sale_code() + "-" + auctionBean.getFaac_auction_start_date();
				fileName = fileName.replaceAll("[^\\w]","-"); //replace all special characters with hyphen
				new File(path + "/" + pageCounter + "/" + fileName).mkdir();
				
				populateAuctionLots(path + "/" + pageCounter + "/" + fileName, auctionBean, auctionUrl);
				auctionListItemCounter++;
			}
		}
	}
	
	private void populateAuctionLots(String filePath, AuctionBean auctionBean, String auctionUrl) {
		try {
			String scriptCode = "";
			int startIndex = 0;
			int endIndex = 0;
			String auctionYear = auctionUrl.replace("https://www.sothebys.com/en/auctions/", "").replace(auctionBean.getFaac_auction_sale_code().toLowerCase(), "").replace(".html", "");
			UserAgent userAgent = new UserAgent(); // create new userAgent
			userAgent.visit(auctionUrl); // visit a url
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			Document doc = Jsoup.parse(content);
			org.jsoup.select.Elements l_ele = doc.select("script[type=text/javascript]");

			for (org.jsoup.nodes.Element element : l_ele) {
				scriptCode = element.html();
				if (scriptCode.contains("function(window,undefined)")) {
					startIndex = (scriptCode.indexOf("//set ECAT.lot")) + 19;
					endIndex = scriptCode.indexOf("ECAT.lot = cleanArray(ECAT.lot)");
					scriptCode = scriptCode.substring(startIndex, endIndex);
					String l_lot_info_arr[] = scriptCode.split(";ECAT");

					for (int i = 0; i < l_lot_info_arr.length; i++) {
						if (l_lot_info_arr[i].contains("'id':")) {
							String[] ar = l_lot_info_arr[i].split("'id':");
							String lotStr = ar[1].substring(0, 5).replaceAll("[,']", "");
							String lotUrl = "http://www.sothebys.com/en/auctions/ecatalogue/" + auctionYear
									+ auctionBean.getFaac_auction_sale_code().toLowerCase() + "/lot." + lotStr + ".html";
							CsvBean csvBean = new CsvBean(); //every loop will have a new bean setting common auction details from auction bean
							csvBean.setAuction_name(auctionBean.getFaac_auction_title());
							csvBean.setAuction_location(auctionBean.getCah_auction_house_location());
							csvBean.setAuction_start_date(auctionBean.getFaac_auction_start_date());
							csvBean.setAuction_end_date(auctionBean.getFaac_auction_end_date());
							csvBean.setAuction_num(auctionBean.getFaac_auction_sale_code());
							populateLotDetails(filePath, lotUrl, csvBean, true);
						}
					}
					break;
				}
			}
			if (!scriptCode.equals("")) {
			} else {
				l_ele = doc.select("script");
				scriptCode = "";
				for (org.jsoup.nodes.Element element : l_ele) {
					scriptCode = element.html();
					System.out.println(scriptCode);
					if (scriptCode.contains("@context")) {
						startIndex = (scriptCode.indexOf("offers\":[")) + 8;
						endIndex = scriptCode.indexOf("},\"performer");
						scriptCode = scriptCode.substring(startIndex, endIndex);
						JSONArray getArray = new JSONArray(scriptCode);
						String lotUrl = "";

						for (int i = 0; i < getArray.length(); i++) {
							JSONObject obj = getArray.getJSONObject(i);
							if (!(obj.isNull("url"))) {
								lotUrl = (String) obj.get("url");
								System.out.println(lotUrl);
								CsvBean csvBean = new CsvBean(); //every loop will have a new bean setting common auction details from auction bean
								csvBean.setAuction_name(auctionBean.getFaac_auction_title());
								csvBean.setAuction_location(auctionBean.getCah_auction_house_location());
								csvBean.setAuction_start_date(auctionBean.getFaac_auction_start_date());
								csvBean.setAuction_end_date(auctionBean.getFaac_auction_end_date());
								csvBean.setAuction_num(auctionBean.getFaac_auction_sale_code());
								populateLotDetails(filePath, lotUrl, csvBean, false);
							}
						}
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("CAN'T ADD LOTS.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void populateLotDetails(String filePath, String lotLink, CsvBean csvBean, boolean isOldStyleLot) {
		System.out.println("Sale Number: " + csvBean.getAuction_num() + " Auction Name: " + csvBean.getAuction_name());
		String lot_num = "";
		try {
			Document doc = null;
			int i = 0;
			UserAgent userAgent = new UserAgent();
			userAgent.visit(lotLink);
			String content = userAgent.doc.innerHTML();
			if(content.contains("notfound.html")) {
				lotLink = lotLink.replaceAll(csvBean.getAuction_num().toLowerCase(), "");
				userAgent.visit(lotLink);
				content = userAgent.doc.innerHTML();
			}
			userAgent.close();
			content = content + "<sale_number>" + csvBean.getAuction_num() + "</sale_number><auction_name>" + csvBean.getAuction_name() + "</auction_name>"
					+ "<auction_location>" + csvBean.getAuction_location() + "</auction_location><aucstart_date>"
					+ csvBean.getAuction_start_date() + "</aucstart_date><aucend_date>" + csvBean.getAuction_end_date() + "</aucend_date>";
			doc = Jsoup.parse(content);
			if(isOldStyleLot) {
				lot_num = doc.select("div[class=lotdetail-lotnumber visible-phone]").text();
			} else {
				lot_num = doc.select("span[class=css-16v44d6]").text();
			}
			
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/" + lot_num + ".html"), "UTF-8"));
			try {
			    out.write(content);
			} finally {
			    out.close();
			}
			
			
			/*FileWriter writer = new FileWriter(filePath + "/" + lot_num + ".html"); 
		    writer.write(content); 
		    writer.close();*/
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void populateAuctionDetails(Element auctionListElement, AuctionBean auctionBean) throws NotFound, ParseException {
		String auctionTitle = auctionListElement.findFirst("<div class='Card-title' style=''>").getTextContent();
		auctionBean.setFaac_auction_title(auctionTitle);
		String auctionDetails = auctionListElement.findFirst("<div class='Card-details' style=''>").getTextContent();
		String[] auctionDetailsArray = auctionDetails.split("\\|");
		String auctionDate = auctionDetailsArray[0];
		String auctionStartDate = "";
		String auctionEndDate = "";
		String auctionLocation = auctionDetailsArray[2];
		auctionBean.setCah_auction_house_location(auctionLocation);
		if(auctionDate.contains("–")) {
			String[] auctionDateArray = auctionDate.split("–");
			if(auctionDateArray[0].length() > 2) {
				auctionStartDate = auctionDate.substring(0, auctionDate.indexOf("–")) + auctionDate.substring(auctionDate.trim().lastIndexOf(" "), auctionDate.length() -1);
				auctionEndDate = auctionDate.substring(auctionDate.indexOf("–") + 1, auctionDate.length() -1);
			} else {
				auctionStartDate = auctionDate.substring(0, auctionDate.indexOf("–")) + auctionDate.substring(auctionDate.indexOf(" "), auctionDate.length() -1);
				auctionEndDate = auctionDate.substring(auctionDate.indexOf("–") + 1, auctionDate.length() -1);
			}
			
		} else {
			auctionStartDate = auctionDate;
		}
		SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy");
		Date d1 = f.parse(auctionStartDate);
		auctionStartDate = new SimpleDateFormat("dd-MMM-yyyy").format(d1);
		if(StringUtils.isNotEmpty(auctionEndDate)) {
			Date d2 = f.parse(auctionEndDate);
			auctionEndDate = new SimpleDateFormat("dd-MMM-yyyy").format(d2);
		}
		auctionBean.setFaac_auction_start_date(auctionStartDate);
		auctionBean.setFaac_auction_end_date(auctionEndDate);
		
		System.out.println("Start Date: " + auctionStartDate + " End Date: " + auctionEndDate);
	}
}
