package com.core.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class SaleAndLotsDataMapper implements RowMapper<SaleAndLotsData> {
	
	public SaleAndLotsData mapRow(ResultSet resultSet, int i) throws SQLException {
		
		SaleAndLotsData saleAndLotsData = new SaleAndLotsData();
		
		saleAndLotsData.setFaac_auction_ID(resultSet.getString("faac_auction_ID"));
		saleAndLotsData.setFaac_auction_sale_code(resultSet.getString("faac_auction_sale_code"));
		saleAndLotsData.setFal_lot_no(resultSet.getString("fal_lot_no"));
		saleAndLotsData.setFal_sub_lot_no(resultSet.getString("fal_sub_lot_no"));
		
		return saleAndLotsData;
	}
}
