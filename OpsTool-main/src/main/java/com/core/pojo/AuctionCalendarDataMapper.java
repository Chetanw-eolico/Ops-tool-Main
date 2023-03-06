package com.core.pojo;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;


public class AuctionCalendarDataMapper implements RowMapper<AuctionCalendarData> {
	
	public AuctionCalendarData mapRow(ResultSet resultSet, int i) throws SQLException {
		
		AuctionCalendarData auctionCalendarData = new AuctionCalendarData();
		
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
		auctionCalendarData.setFaac_auction_ID(resultSet.getInt("faac_auction_ID"));
		auctionCalendarData.setFaac_auction_sale_code(resultSet.getString("faac_auction_sale_code"));
		auctionCalendarData.setFaac_auction_house_ID(resultSet.getInt("faac_auction_house_ID"));
		auctionCalendarData.setFaac_auction_title(resultSet.getString("faac_auction_title"));
		if(StringUtils.isEmpty(resultSet.getString("faac_auction_start_date")) || resultSet.getString("faac_auction_start_date").equals("0000-00-00")) {
			auctionCalendarData.setFaac_auction_start_date("N/A");
		} else {
			auctionCalendarData.setFaac_auction_start_date(dateFormat.format(resultSet.getDate("faac_auction_start_date")));
		}
		
		auctionCalendarData.setFaac_auction_end_date(resultSet.getDate("faac_auction_end_date"));
		auctionCalendarData.setFaac_auction_lot_count(resultSet.getInt("faac_auction_lot_count"));
		auctionCalendarData.setFaac_auction_published(resultSet.getString("faac_auction_published"));
		auctionCalendarData.setFaac_auction_record_created(resultSet.getDate("faac_auction_record_created"));
		auctionCalendarData.setFaac_auction_record_updated(resultSet.getDate("faac_auction_record_updated"));
		auctionCalendarData.setFaac_auction_record_createdby(resultSet.getString("faac_auction_record_createdby"));
		auctionCalendarData.setFaac_auction_record_updatedby(resultSet.getString("faac_auction_record_updatedby"));
		
		return auctionCalendarData;
	}
}
