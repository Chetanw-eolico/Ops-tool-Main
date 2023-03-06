package com.core.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.core.RowMapper;


public class SearchResultsDataMapper implements RowMapper<SearchResultsData> {
	
	public SearchResultsData mapRow(ResultSet resultSet, int i) throws SQLException {
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
		
		SearchResultsData quickAndAdvanceSearchResultsData = new SearchResultsData();
		
		quickAndAdvanceSearchResultsData.setArtistFullName(resultSet.getString("fa_artist_name"));
		quickAndAdvanceSearchResultsData.setMediaLibraryFile(resultSet.getString("fal_lot_image1"));
		quickAndAdvanceSearchResultsData.setMediaImage1(resultSet.getString("fal_lot_image1"));
		quickAndAdvanceSearchResultsData.setMediaImage2(resultSet.getString("fal_lot_image2"));
		quickAndAdvanceSearchResultsData.setMediaImage3(resultSet.getString("fal_lot_image3"));
		quickAndAdvanceSearchResultsData.setMediaImage4(resultSet.getString("fal_lot_image4"));
		quickAndAdvanceSearchResultsData.setMediaImage5(resultSet.getString("fal_lot_image5"));
		quickAndAdvanceSearchResultsData.setArtworkId(resultSet.getInt("faa_artwork_ID"));
		quickAndAdvanceSearchResultsData.setArtworkTitle(resultSet.getString("faa_artwork_title"));
		quickAndAdvanceSearchResultsData.setArtworkCategory(resultSet.getString("faa_artwork_category"));
		quickAndAdvanceSearchResultsData.setArtworkStartYear(resultSet.getString("faa_artwork_start_year"));
		quickAndAdvanceSearchResultsData.setArtworkEndYear(resultSet.getString("faa_artwork_end_year"));
		quickAndAdvanceSearchResultsData.setLotId(resultSet.getInt("fal_lot_ID"));
		quickAndAdvanceSearchResultsData.setLotNumber(resultSet.getString("fal_lot_no"));
		quickAndAdvanceSearchResultsData.setSublotNumber(resultSet.getString("fal_sub_lot_no"));
		quickAndAdvanceSearchResultsData.setLotStatus(resultSet.getString("fal_lot_status"));
		quickAndAdvanceSearchResultsData.setLotEstimateLow(resultSet.getLong("fal_lot_low_estimate"));
		quickAndAdvanceSearchResultsData.setLotEstimateHigh(resultSet.getLong("fal_lot_high_estimate"));
		quickAndAdvanceSearchResultsData.setLotEstimateLowUSD(resultSet.getLong("fal_lot_low_estimate_USD"));
		quickAndAdvanceSearchResultsData.setLotEstimateHighUSD(resultSet.getLong("fal_lot_high_estimate_USD"));
		quickAndAdvanceSearchResultsData.setLotSalePrice(resultSet.getLong("fal_lot_sale_price"));
		quickAndAdvanceSearchResultsData.setLotSalePriceUSD(resultSet.getLong("fal_lot_sale_price_USD"));
		quickAndAdvanceSearchResultsData.setAuctionNumber(resultSet.getString("faac_auction_ID"));
		quickAndAdvanceSearchResultsData.setAuctionSaleCode(resultSet.getString("faac_auction_sale_code"));
		quickAndAdvanceSearchResultsData.setAuctionTitle(resultSet.getString("faac_auction_title"));
		quickAndAdvanceSearchResultsData.setAuctionSaleStartDate(dateFormat.format(resultSet.getDate("faac_auction_start_date")));
		quickAndAdvanceSearchResultsData.setAuctionCurrencyCode(resultSet.getString("cah_auction_house_currency_code"));
		quickAndAdvanceSearchResultsData.setAuctionPriceType(resultSet.getString("fal_lot_price_type"));
		quickAndAdvanceSearchResultsData.setAuctionHouseId(resultSet.getInt("cah_auction_house_ID"));
		quickAndAdvanceSearchResultsData.setAuctionHouseTitle(resultSet.getString("cah_auction_house_name"));
		quickAndAdvanceSearchResultsData.setAuctionHouseCountry(resultSet.getString("cah_auction_house_country"));
		quickAndAdvanceSearchResultsData.setAuctionHouseLocation(resultSet.getString("cah_auction_house_location"));
		quickAndAdvanceSearchResultsData.setAuctionHouseDimensionUnit(resultSet.getString("faa_arwork_measurement_unit"));
		quickAndAdvanceSearchResultsData.setAuctionHouseCurrencyCode(resultSet.getString("cah_auction_house_currency_code"));
		quickAndAdvanceSearchResultsData.setArtistNameQualifier(resultSet.getString("fa_artist_birth_year_identifier"));
		quickAndAdvanceSearchResultsData.setArtworkYearStartQualifier(resultSet.getString("faa_artwork_start_year_identifier"));
		quickAndAdvanceSearchResultsData.setArtworkHeight(resultSet.getString("faa_artwork_height"));
		quickAndAdvanceSearchResultsData.setArtworkDepth(resultSet.getString("faa_artwork_depth"));
		quickAndAdvanceSearchResultsData.setArtworkMarkings(resultSet.getString("faa_artwork_markings"));
		quickAndAdvanceSearchResultsData.setArtworkWidth(resultSet.getString("faa_artwork_width"));
		quickAndAdvanceSearchResultsData.setArtworkMedium(resultSet.getString("faa_artwork_material"));
		quickAndAdvanceSearchResultsData.setArtworkDescription(resultSet.getString("faa_artwork_description"));
		quickAndAdvanceSearchResultsData.setArtworkProvenance(resultSet.getString("fal_lot_provenance"));
		quickAndAdvanceSearchResultsData.setArtworkCondition(resultSet.getString("fal_lot_condition"));
		quickAndAdvanceSearchResultsData.setArtworkExhibition(resultSet.getString("faa_artwork_exhibition"));
		quickAndAdvanceSearchResultsData.setArtworkEdition(resultSet.getString("faa_artwork_edition"));
		quickAndAdvanceSearchResultsData.setArtworkSizeDescription(resultSet.getString("faa_artwork_size_details"));
		quickAndAdvanceSearchResultsData.setArtistId(resultSet.getInt("faa_artist_ID"));
		quickAndAdvanceSearchResultsData.setArtist2Id(resultSet.getInt("faa_artist2_ID"));
		quickAndAdvanceSearchResultsData.setArtist3Id(resultSet.getInt("faa_artist3_ID"));
		quickAndAdvanceSearchResultsData.setLeft_days(resultSet.getInt("left_days"));
		quickAndAdvanceSearchResultsData.setArtworkLiterature(resultSet.getString("faa_artwork_literature"));
		quickAndAdvanceSearchResultsData.setMarker(resultSet.getString("marker"));
		quickAndAdvanceSearchResultsData.setArtistNationality(resultSet.getString("fa_artist_nationality"));
		quickAndAdvanceSearchResultsData.setArtistYearBirth(resultSet.getString("fa_artist_birth_year"));
		quickAndAdvanceSearchResultsData.setArtistYearDeath(resultSet.getString("fa_artist_death_year"));
		quickAndAdvanceSearchResultsData.setArtistNamePrefix(resultSet.getString("fa_artist_name_prefix"));
		quickAndAdvanceSearchResultsData.setArtistNameSuffix(resultSet.getString("fa_artist_name_suffix"));
		quickAndAdvanceSearchResultsData.setArtistDescription(resultSet.getString("fa_artist_description"));
		quickAndAdvanceSearchResultsData.setArtistAka(resultSet.getString("fa_artist_aka"));
		
		return quickAndAdvanceSearchResultsData;
	}

}
