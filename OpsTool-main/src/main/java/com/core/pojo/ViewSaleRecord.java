package com.core.pojo;

import java.util.Date;

public class ViewSaleRecord {
	int lotID;
	int lotNumber;
	String artworkName;
	String artistName;
	int imageCount;
	
	public int getLotID() {
		return lotID;
	}
	public void setLotID(int lotID) {
		this.lotID = lotID;
	}
	public int getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(int lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getArtworkName() {
		return artworkName;
	}
	public void setArtworkName(String artworkName) {
		this.artworkName = artworkName;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public int getImageCount() {
		return imageCount;
	}
	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}	
}
