package com.core.pojo;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class AuctionHouseDataMapper implements RowMapper<AuctionHouseData> {
	
	public AuctionHouseData mapRow(ResultSet resultSet, int i) throws SQLException {
		
		AuctionHouseData auctionHouseData = new AuctionHouseData();
		
		auctionHouseData.setCah_auction_house_ID(resultSet.getInt("cah_auction_house_ID"));
		auctionHouseData.setCah_auction_house_name(resultSet.getString("cah_auction_house_name"));
		auctionHouseData.setCah_auction_house_country(resultSet.getString("cah_auction_house_country"));
		auctionHouseData.setCah_auction_house_location(resultSet.getString("cah_auction_house_location"));
		auctionHouseData.setCah_auction_house_currency_code(resultSet.getString("cah_auction_house_currency_code"));
		auctionHouseData.setCah_auction_house_website(resultSet.getString("cah_auction_house_website"));
		auctionHouseData.setCah_auction_house_record_created(resultSet.getDate("cah_auction_house_record_created"));
		auctionHouseData.setCah_auction_house_record_updated(resultSet.getDate("cah_auction_house_record_updated"));
		auctionHouseData.setCah_auction_house_record_createdby(resultSet.getString("cah_auction_house_record_createdby"));
		auctionHouseData.setCah_auction_house_record_updatedby(resultSet.getString("cah_auction_house_record_updatedby"));
		
		return auctionHouseData;
	}
}
