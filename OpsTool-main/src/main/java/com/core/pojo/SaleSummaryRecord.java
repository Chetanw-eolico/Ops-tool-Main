package com.core.pojo;

import java.util.Date;

public class SaleSummaryRecord {

	int auctionId;
	String saleTitle;
	String auctionHouseName;
	String auctionHouseLocation;
	Date saleStartDate;
	Date saleEndDate;
	int missingArtists;
	int missingImages;
	String published;
	int lotCount;
	String entryPerson;
	String saleSource;
	String saleCode;
	
	public int getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	public String getSaleTitle() {
		return saleTitle;
	}
	public void setSaleTitle(String saleTitle) {
		this.saleTitle = saleTitle;
	}
	public String getAuctionHouseName() {
		return auctionHouseName;
	}
	public void setAuctionHouseName(String auctionHouseName) {
		this.auctionHouseName = auctionHouseName;
	}
	public String getAuctionHouseLocation() {
		return auctionHouseLocation;
	}
	public void setAuctionHouseLocation(String auctionHouseLocation) {
		this.auctionHouseLocation = auctionHouseLocation;
	}
	public Date getSaleStartDate() {
		return saleStartDate;
	}
	public void setSaleStartDate(Date saleStartDate) {
		this.saleStartDate = saleStartDate;
	}
	public Date getSaleEndDate() {
		return saleEndDate;
	}
	public void setSaleEndDate(Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public int getMissingArtists() {
		return missingArtists;
	}
	public void setMissingArtists(int missingArtists) {
		this.missingArtists = missingArtists;
	}
	public int getMissingImages() {
		return missingImages;
	}
	public void setMissingImages(int missingImages) {
		this.missingImages = missingImages;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public int getLotCount() {
		return lotCount;
	}
	public void setLotCount(int lotCount) {
		this.lotCount = lotCount;
	}
	public String getEntryPerson() {
		return entryPerson;
	}
	public void setEntryPerson(String entryPerson) {
		this.entryPerson = entryPerson;
	}
	public String getSaleSource() {
		return saleSource;
	}
	public void setSaleSource(String saleSource) {
		this.saleSource = saleSource;
	}
	public String getSaleCode() {
		return saleCode;
	}
	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}
}
