package com.core.pojo;


import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

public class SearchResultsData {
	
	String artistFullName;
	String artist2FullName;
	String artist3FullName;
	
	String artistNationality;
	String artist2Nationality;
	String artist3Nationality;
	
	String artistYearBirth;
	String artist2YearBirth;
	String artist3YearBirth;
	
	String artistYearDeath;
	String artist2YearDeath;
	String artist3YearDeath;
	
	String artistDescription;
	String artist2Description;
	String artist3Description;
	
	String artistAka;
	
	int artistId;
	int artist2Id;
	int artist3Id;
	
	String artistNamePrefix;
	String artistNameSuffix;
	
	String mediaLibraryFile;
	String mediaImage1;
	String mediaImage2;
	String mediaImage3;
	String mediaImage4;
	String mediaImage5;
	
	int artworkId;
	String artworkTitle;
	String artworkCategory;
	String artworkStartYear;
	String artworkEndYear;
	int lotId;
	String lotNumber;
	String sublotNumber;
	String lotStatus;
	Long lotEstimateLow;
	Long lotEstimateHigh;
	Long lotEstimateLowUSD;
	Long lotEstimateHighUSD;
	Long lotSalePrice;
	Long lotSalePriceUSD;
	String auctionNumber;
	String auctionSaleCode;
	String auctionTitle;
	String auctionSaleStartDate;
	String auctionCurrencyCode;
	String auctionPriceType;
	int auctionHouseId;
	String auctionHouseTitle;
	String auctionHouseCountry;
	String auctionHouseLocation;
	String auctionHouseDimensionUnit;
	String auctionHouseCurrencyCode;
	String artistNameQualifier;
	String artworkYearStartQualifier;
	String artworkHeight;
	String artworkWidth;
	String artworkDepth;
	String artworkMarkings;
	String artworkMedium;
	String artworkDescription;
	
	String artworkProvenance;
	String artworkCondition;
	String artworkExhibition;
	String artworkEdition;
	String artworkSizeDescription;
	String artworkLiterature;
	
	
	int left_days;
	String marker;
	boolean lotSavedInCollection;
	DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
	String artistSlug;
	
	public String getMediaLibraryFile() {
		return mediaLibraryFile;
	}
	public void setMediaLibraryFile(String mediaLibraryFile) {
		this.mediaLibraryFile = mediaLibraryFile;
	}
	public int getArtworkId() {
		return artworkId;
	}
	public void setArtworkId(int artworkId) {
		this.artworkId = artworkId;
	}
	public String getArtworkTitle() {
		if(StringUtils.isNotEmpty(artworkTitle)) {
			return artworkTitle.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkTitle(String artworkTitle) {
		this.artworkTitle = artworkTitle;
	}
	public String getArtworkCategory() {
		return artworkCategory;
	}
	public void setArtworkCategory(String artworkCategory) {
		this.artworkCategory = artworkCategory;
	}
	public String getArtworkStartYear() {
		if(artworkStartYear.equals("0")) {
			artworkStartYear = "";
		}
		return artworkStartYear;
	}
	public void setArtworkStartYear(String artworkStartYear) {
		this.artworkStartYear = artworkStartYear;
	}
	public int getLotId() {
		return lotId;
	}
	public void setLotId(int lotId) {
		this.lotId = lotId;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getSublotNumber() {
		return sublotNumber;
	}
	public void setSublotNumber(String sublotNumber) {
		this.sublotNumber = sublotNumber;
	}
	public String getLotStatus() {
		return lotStatus;
	}
	public void setLotStatus(String lotStatus) {
		this.lotStatus = lotStatus;
	}
	public Long getLotEstimateLow() {
		return lotEstimateLow;
	}
	public void setLotEstimateLow(Long lotEstimateLow) {
		this.lotEstimateLow = lotEstimateLow;
	}
	public Long getLotEstimateHigh() {
		return lotEstimateHigh;
	}
	public void setLotEstimateHigh(Long lotEstimateHigh) {
		this.lotEstimateHigh = lotEstimateHigh;
	}
	public Long getLotEstimateLowUSD() {
		return lotEstimateLowUSD;
	}
	public void setLotEstimateLowUSD(Long lotEstimateLowUSD) {
		this.lotEstimateLowUSD = lotEstimateLowUSD;
	}
	public Long getLotEstimateHighUSD() {
		return lotEstimateHighUSD;
	}
	public void setLotEstimateHighUSD(Long lotEstimateHighUSD) {
		this.lotEstimateHighUSD = lotEstimateHighUSD;
	}
	public Long getLotSalePrice() {
		return lotSalePrice;
	}
	public void setLotSalePrice(Long lotSalePrice) {
		this.lotSalePrice = lotSalePrice;
	}
	public Long getLotSalePriceUSD() {
		return lotSalePriceUSD;
	}
	public void setLotSalePriceUSD(Long lotSalePriceUSD) {
		this.lotSalePriceUSD = lotSalePriceUSD;
	}
	public String getAuctionNumber() {
		return auctionNumber;
	}
	public void setAuctionNumber(String auctionNumber) {
		this.auctionNumber = auctionNumber;
	}
	public String getAuctionSaleCode() {
		return auctionSaleCode;
	}
	public void setAuctionSaleCode(String auctionSaleCode) {
		this.auctionSaleCode = auctionSaleCode;
	}
	public String getAuctionTitle() {
		if(StringUtils.isNotEmpty(auctionTitle)) {
			return auctionTitle.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setAuctionTitle(String auctionTitle) {
		this.auctionTitle = auctionTitle;
	}
	public String getAuctionSaleStartDate() {
		return auctionSaleStartDate;
	}
	public void setAuctionSaleStartDate(String auctionSaleStartDate) {
		this.auctionSaleStartDate = auctionSaleStartDate;
	}
	public String getAuctionCurrencyCode() {
		return auctionCurrencyCode;
	}
	public void setAuctionCurrencyCode(String auctionCurrencyCode) {
		this.auctionCurrencyCode = auctionCurrencyCode;
	}
	public String getAuctionPriceType() {
		return auctionPriceType;
	}
	public void setAuctionPriceType(String auctionPriceType) {
		this.auctionPriceType = auctionPriceType;
	}
	public int getAuctionHouseId() {
		return auctionHouseId;
	}
	public void setAuctionHouseId(int auctionHouseId) {
		this.auctionHouseId = auctionHouseId;
	}
	public String getAuctionHouseTitle() {
		if(StringUtils.isNotEmpty(auctionHouseTitle)) {
			return auctionHouseTitle.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setAuctionHouseTitle(String auctionHouseTitle) {
		this.auctionHouseTitle = auctionHouseTitle;
	}
	public String getAuctionHouseCountry() {
		return auctionHouseCountry.replaceAll("\\\\", ""); 
	}
	public void setAuctionHouseCountry(String auctionHouseCountry) {
		this.auctionHouseCountry = auctionHouseCountry;
	}
	public String getAuctionHouseLocation() {
		if(StringUtils.isNotEmpty(auctionHouseLocation)) {
			return auctionHouseLocation.replaceAll("\\\\", ""); 
		}
		return "";
	}
	public void setAuctionHouseLocation(String auctionHouseLocation) {
		this.auctionHouseLocation = auctionHouseLocation;
	}
	public String getAuctionHouseDimensionUnit() {
		return auctionHouseDimensionUnit;
	}
	public void setAuctionHouseDimensionUnit(String auctionHouseDimensionUnit) {
		this.auctionHouseDimensionUnit = auctionHouseDimensionUnit;
	}
	public String getAuctionHouseCurrencyCode() {
		return auctionHouseCurrencyCode;
	}
	public void setAuctionHouseCurrencyCode(String auctionHouseCurrencyCode) {
		this.auctionHouseCurrencyCode = auctionHouseCurrencyCode;
	}
	public String getArtistNameQualifier() {
		return artistNameQualifier;
	}
	public void setArtistNameQualifier(String artistNameQualifier) {
		this.artistNameQualifier = artistNameQualifier;
	}
	public String getArtworkYearStartQualifier() {
		return artworkYearStartQualifier;
	}
	public void setArtworkYearStartQualifier(String artworkYearStartQualifier) {
		this.artworkYearStartQualifier = artworkYearStartQualifier;
	}
	public String getArtworkHeight() {
		return artworkHeight;
	}
	public void setArtworkHeight(String artworkHeight) {
		if (StringUtils.isEmpty(artworkHeight) || Float.valueOf(artworkHeight) == 0) {
			this.artworkHeight = "";
		} else {
			this.artworkHeight = artworkHeight;
		}
		
	}
	public String getArtworkWidth() {
		return artworkWidth;
	}
	public void setArtworkWidth(String artworkWidth) {
		if (StringUtils.isEmpty(artworkWidth) || Float.valueOf(artworkWidth) == 0) {
			this.artworkWidth = "";
		} else {
			this.artworkWidth = artworkWidth;
		}
	}
	public String getArtworkDepth() {
		return artworkDepth;
	}
	public void setArtworkDepth(String artworkDepth) {
		if (StringUtils.isEmpty(artworkDepth) || Float.valueOf(artworkDepth) == 0) {
			this.artworkDepth = "";
		} else {
			this.artworkDepth = artworkDepth;
		}
	}
	
	public String getArtworkMarkings() {
		if(StringUtils.isNotEmpty(artworkMarkings)) {
			return artworkMarkings.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkMarkings(String artworkMarkings) {
		this.artworkMarkings = artworkMarkings;
	}
	public String getArtworkMedium() {
		if(StringUtils.isNotEmpty(artworkMedium)) {
			return artworkMedium.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkMedium(String artworkMedium) {
		this.artworkMedium = artworkMedium;
	}
	public String getArtworkDescription() {
		//System.out.println(artworkDescription);
		
		artworkDescription = artworkDescription
				.replaceAll("<strong><br>Provenance: </strong><br>", "<strong>Provenance: </strong>") //if found empty do not show just headings
				.replaceAll("<strong><br>Literature: </strong><br>", "<strong>Literature: </strong>")
				.replaceAll("<strong><br>Exhibited: </strong><br>", "<strong>Exhibited: </strong>")
				.replaceAll("<strong><br>Lot Notes :</strong><br>", "<strong>Lot Notes :</strong>")
				.replaceAll("<strong><br>Special Notice: </strong><br>", "<strong>Special Notice: </strong>")
				.replaceAll("<strong><br>Catalogue Note: </strong><br>", "<strong>Catalogue Note: </strong>")
				.replaceAll("<strong><br>Pre-Lot Text: </strong><br>", "<strong>Pre-Lot Text: </strong>")
				.replaceAll("<strong><br>Condition Report: </strong><br>", "<strong>Condition Report: </strong>")
				.replaceAll("<strong><br>Post Lot Text: </strong><br>", "<strong>Post Lot Text: </strong>")
				.replaceAll("<strong><br>Other Information: </strong><br>", "<strong>Other Information: </strong>");
		
		artworkDescription = artworkDescription
				.replaceAll("<strong>Provenance: </strong> <strong>", "<strong>") //if found empty do not show just headings
				.replaceAll("<strong>Literature: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Exhibited: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Lot Notes :</strong> <strong>", "<strong>")
				.replaceAll("<strong>Special Notice: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Catalogue Note: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Pre-Lot Text: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Condition Report: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Post Lot Text: </strong> <strong>", "<strong>")
				.replaceAll("<strong>Other Information: </strong> <strong>", "<strong>");
				
		artworkDescription = artworkDescription.replaceAll("\\\\", "")
				.replaceAll("<strong>Provenance: </strong><strong>", "<strong>") //if found empty do not show just headings
				.replaceAll("<strong>Literature: </strong><strong>", "<strong>")
				.replaceAll("<strong>Exhibited: </strong><strong>", "<strong>")
				.replaceAll("<strong>Lot Notes :</strong><strong>", "<strong>")
				.replaceAll("<strong>Special Notice: </strong><strong>", "<strong>")
				.replaceAll("<strong>Catalogue Note: </strong><strong>", "<strong>")
				.replaceAll("<strong>Pre-Lot Text: </strong><strong>", "<strong>")
				.replaceAll("<strong>Condition Report: </strong><strong>", "<strong>")
				.replaceAll("<strong>Post Lot Text: </strong><strong>", "<strong>")
				.replaceAll("<strong>Other Information: </strong><strong>", "<strong>")
				.replaceAll("<strong>", "<strong><br><br>")
				.replaceAll("</strong>", "</strong><br><br>");
		
		//now remove catalogue note and condition report as we are not showing this information.
		
		String conditionReport = "";
		String catalogueNote = "";
		String lotNotes = "";
		String speacialNotice = "";
		
		int firstIndexCatNote = artworkDescription.indexOf("<strong><br>Catalogue Note: </strong><br>");
		if(firstIndexCatNote != -1) {
			int secondIndexCatNote = artworkDescription.indexOf("<br>", firstIndexCatNote + 41);
			
			if(secondIndexCatNote == -1) {
				secondIndexCatNote = artworkDescription.length();
			}
			
			catalogueNote = artworkDescription.substring(firstIndexCatNote, secondIndexCatNote);
		}
		
		int firstIndexCondRep = artworkDescription.indexOf("<strong><br>Condition Report: </strong><br>");
		if(firstIndexCondRep != -1) {
			int secondIndexCondRep = artworkDescription.indexOf("<br>", firstIndexCondRep + 44);
			
			if(secondIndexCondRep == -1) {
				secondIndexCondRep = artworkDescription.length();
			}
			
			conditionReport = artworkDescription.substring(firstIndexCondRep, secondIndexCondRep);
		}
		
		int firstIndexLotNotes = artworkDescription.indexOf("<strong><br>Lot Notes :</strong><br>");
		if(firstIndexLotNotes != -1) {
			int secondIndexLotNotes = artworkDescription.indexOf("<br>", firstIndexLotNotes + 37);
			
			if(secondIndexLotNotes == -1) {
				secondIndexLotNotes = artworkDescription.length();
			}
			
			lotNotes = artworkDescription.substring(firstIndexLotNotes, secondIndexLotNotes);
		}
		
		int firstIndexSpecialNotes = artworkDescription.indexOf("<br>Special Notice: </strong><br>");
		if(firstIndexSpecialNotes != -1) {
			int secondIndexSpecialtNotes = artworkDescription.indexOf("<br>", firstIndexSpecialNotes + 33);
			
			if(secondIndexSpecialtNotes == -1) {
				secondIndexSpecialtNotes = artworkDescription.length();
			}
			
			speacialNotice = artworkDescription.substring(firstIndexSpecialNotes, secondIndexSpecialtNotes);
		}
		
		artworkDescription = artworkDescription.replace(catalogueNote, "").replace(conditionReport, "").replace(lotNotes, "").replace(speacialNotice, "")
				.replace("<br>Pre-Lot Text:", "<br><strong>Pre-Lot Text:").replace("<strong><br>Description: </strong><br>", "<strong><br>Lot Description: </strong><br>")
				.replace("<strong><br>Description:</strong><br>", "<strong><br>Lot Description:</strong><br>").replaceAll("<strong><br><br><br>Description:</strong><br><br><br>", "<strong>Lot Description:</strong><br><br>");
		
		int firstIndexOtherInfo = artworkDescription.indexOf("<br>Other Information: </strong><br>");
		if(firstIndexOtherInfo != -1) {
			int secondIndexOtherInfo = firstIndexOtherInfo + 36;
			if(secondIndexOtherInfo == artworkDescription.length()) {
				artworkDescription = artworkDescription.replace("<br>Other Information: </strong><br>", "");
			}
		}
		
		artworkDescription = artworkDescription.replace("Read Condition Report Read Condition Report Register or Log-in to view condition report Or Saleroom Notice", "");
		
		if(artworkDescription.length() > 400) {
			String firstPart = artworkDescription.substring(0, 400);
			String secondPart = artworkDescription.substring(400, artworkDescription.length());
			
			firstPart = firstPart + "<span id='dots'>...</span><span id='more'>";
			secondPart = secondPart + "</span>" + "&nbsp;&nbsp;<button class='btn btn-primary' onclick='readMore()' id='readMore'><strong>Read more</strong></button>";
			artworkDescription = firstPart + secondPart;
		}
		//System.out.println(artworkDescription);
		
		return artworkDescription;
	}
	public void setArtworkDescription(String artworkDescription) {
		this.artworkDescription = artworkDescription;
	}
	public String getArtistYearBirth() {
		if(null == artistYearBirth) {
			return "";
		}
		return artistYearBirth;
	}
	public void setArtistYearBirth(String artistYearBirth) {
		this.artistYearBirth = artistYearBirth;
	}
	public String getArtistYearDeath() {
		if(null == artistYearDeath) {
			return "";
		}
		return artistYearDeath;
	}
	public void setArtistYearDeath(String artistYearDeath) {
		this.artistYearDeath = artistYearDeath;
	}
	public String getArtworkProvenance() {
		if(StringUtils.isNotEmpty(artworkProvenance)) {
			return artworkProvenance.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkProvenance(String artworkProvenance) {
		this.artworkProvenance = artworkProvenance;
	}
	public String getArtworkCondition() {
		if(StringUtils.isNotEmpty(artworkCondition)) {
			return artworkCondition.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkCondition(String artworkCondition) {
		this.artworkCondition = artworkCondition;
	}
	public String getArtworkExhibition() {
		if(StringUtils.isNotEmpty(artworkExhibition)) {
			return artworkExhibition.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkExhibition(String artworkExhibition) {
		this.artworkExhibition = artworkExhibition;
	}
	public String getArtworkEdition() {
		if(StringUtils.isNotEmpty(artworkEdition)) {
			return artworkEdition.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkEdition(String artworkEdition) {
		this.artworkEdition = artworkEdition;
	}
	public String getArtworkSizeDescription() {
		if(StringUtils.isNotEmpty(artworkSizeDescription)) {
			return artworkSizeDescription.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkSizeDescription(String artworkSizeDescription) {
		this.artworkSizeDescription = artworkSizeDescription;
	}
	
	
	public String getArtistDescription() {
		return artistDescription;
	}
	public void setArtistDescription(String artistDescription) {
		this.artistDescription = artistDescription;
	}
	public int getArtistId() {
		return artistId;
	}
	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
	
	public int getArtist2Id() {
		return artist2Id;
	}
	public void setArtist2Id(int artist2Id) {
		this.artist2Id = artist2Id;
	}
	public int getArtist3Id() {
		return artist3Id;
	}
	public void setArtist3Id(int artist3Id) {
		this.artist3Id = artist3Id;
	}
	public String getArtworkEndYear() {
		if(artworkEndYear.equals("0")) {
			artworkEndYear = "";
		}
		return artworkEndYear;
	}
	public void setArtworkEndYear(String artworkEndYear) {
		this.artworkEndYear = artworkEndYear;
	}
	public int getLeft_days() {
		return left_days;
	}
	public void setLeft_days(int left_days) {
		this.left_days = left_days;
	}
	public boolean isLotSavedInCollection() {
		return lotSavedInCollection;
	}
	public void setLotSavedInCollection(boolean lotSavedInCollection) {
		this.lotSavedInCollection = lotSavedInCollection;
	}
	public String getArtworkLiterature() {
		if(StringUtils.isNotEmpty(artworkLiterature)) {
			return artworkLiterature.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtworkLiterature(String artworkLiterature) {
		this.artworkLiterature = artworkLiterature;
	}
	public String getMarker() {
		return marker;
	}
	public void setMarker(String marker) {
		this.marker = marker;
	}
	public String getArtistFullName() {
		if(StringUtils.isNotEmpty(artistFullName)) {
			return artistFullName.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtistFullName(String artistFullName) {
		this.artistFullName = artistFullName;
	}
	
	public String getArtist2FullName() {
		if(StringUtils.isNotEmpty(artist2FullName)) {
			return artist2FullName.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtist2FullName(String artist2FullName) {
		this.artist2FullName = artist2FullName;
	}
	public String getArtist3FullName() {
		if(StringUtils.isNotEmpty(artist3FullName)) {
			return artist3FullName.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtist3FullName(String artist3FullName) {
		this.artist3FullName = artist3FullName;
	}
	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}
	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}
	public String getArtistNationality() {
		if(StringUtils.isNotEmpty(artistNationality)) {
			return artistNationality.replaceAll("\\\\", "");
		}
		return "";
	}
	public void setArtistNationality(String artistNationality) {
		this.artistNationality = artistNationality;
	}
	public String getArtistSlug() {
		StringBuffer artistNameWeb = new StringBuffer(artistFullName);
	    if(!artistNationality.equals("na")) {
	    	artistNameWeb.append(" - ").append(artistNationality);
	    }
	    if(Integer.parseInt(artistYearBirth) != 0) {
	    	artistNameWeb.append(" (").append(artistYearBirth);
	    } 
	    if(Integer.parseInt(artistYearDeath) != 0) {
	    	artistNameWeb.append(" - ").append(artistYearDeath).append(")");
		} else {
			if(Integer.parseInt(artistYearBirth) != 0) {
				artistNameWeb.append(")");
			}
		}
		return artistNameWeb.toString();
	}
	public void setArtistSlug(String artistSlug) {
		this.artistSlug = artistSlug;
	}
	public String getArtistNamePrefix() {
		return artistNamePrefix;
	}
	public void setArtistNamePrefix(String artistNamePrefix) {
		this.artistNamePrefix = artistNamePrefix;
	}
	public String getArtistNameSuffix() {
		return artistNameSuffix;
	}
	public void setArtistNameSuffix(String artistNameSuffix) {
		this.artistNameSuffix = artistNameSuffix;
	}
	public String getMediaImage1() {
		return mediaImage1;
	}
	public void setMediaImage1(String mediaImage1) {
		this.mediaImage1 = mediaImage1;
	}
	public String getMediaImage2() {
		return mediaImage2;
	}
	public void setMediaImage2(String mediaImage2) {
		this.mediaImage2 = mediaImage2;
	}
	public String getMediaImage3() {
		return mediaImage3;
	}
	public void setMediaImage3(String mediaImage3) {
		this.mediaImage3 = mediaImage3;
	}
	public String getMediaImage4() {
		return mediaImage4;
	}
	public void setMediaImage4(String mediaImage4) {
		this.mediaImage4 = mediaImage4;
	}
	public String getMediaImage5() {
		return mediaImage5;
	}
	public void setMediaImage5(String mediaImage5) {
		this.mediaImage5 = mediaImage5;
	}
	public String getArtist2Nationality() {
		return artist2Nationality;
	}
	public void setArtist2Nationality(String artist2Nationality) {
		this.artist2Nationality = artist2Nationality;
	}
	public String getArtist3Nationality() {
		return artist3Nationality;
	}
	public void setArtist3Nationality(String artist3Nationality) {
		this.artist3Nationality = artist3Nationality;
	}
	public String getArtist2YearBirth() {
		return artist2YearBirth;
	}
	public void setArtist2YearBirth(String artist2YearBirth) {
		this.artist2YearBirth = artist2YearBirth;
	}
	public String getArtist3YearBirth() {
		return artist3YearBirth;
	}
	public void setArtist3YearBirth(String artist3YearBirth) {
		this.artist3YearBirth = artist3YearBirth;
	}
	public String getArtist2YearDeath() {
		return artist2YearDeath;
	}
	public void setArtist2YearDeath(String artist2YearDeath) {
		this.artist2YearDeath = artist2YearDeath;
	}
	public String getArtist3YearDeath() {
		return artist3YearDeath;
	}
	public void setArtist3YearDeath(String artist3YearDeath) {
		this.artist3YearDeath = artist3YearDeath;
	}
	public String getArtist2Description() {
		return artist2Description;
	}
	public void setArtist2Description(String artist2Description) {
		this.artist2Description = artist2Description;
	}
	public String getArtist3Description() {
		return artist3Description;
	}
	public void setArtist3Description(String artist3Description) {
		this.artist3Description = artist3Description;
	}
	public String getArtistAka() {
		return artistAka;
	}
	public void setArtistAka(String artistAka) {
		this.artistAka = artistAka;
	}
}
