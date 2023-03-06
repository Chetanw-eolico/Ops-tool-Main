package com.core.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class FineartArtworkDataMapper implements RowMapper<FineartArtworkData> {
	
	public FineartArtworkData mapRow(ResultSet resultSet, int i) throws SQLException {
		
		FineartArtworkData fineartArtworkData = new FineartArtworkData();
		
		fineartArtworkData.setFaa_artwork_ID(resultSet.getInt("faa_artwork_ID"));
		fineartArtworkData.setFaa_artwork_title(resultSet.getString("faa_artwork_title"));
		fineartArtworkData.setFaa_artwork_description(resultSet.getString("faa_artwork_description"));
		fineartArtworkData.setFaa_artist_ID(resultSet.getInt("faa_artist_ID"));
		fineartArtworkData.setFaa_artwork_category(resultSet.getString("faa_artwork_category"));
		fineartArtworkData.setFaa_artwork_material(resultSet.getString("faa_artwork_material"));
		fineartArtworkData.setFaa_artwork_edition(resultSet.getString("faa_artwork_edition"));
		fineartArtworkData.setFaa_artwork_exhibition(resultSet.getString("faa_artwork_exhibition"));
		fineartArtworkData.setFaa_artwork_literature(resultSet.getString("faa_artwork_literature"));
		fineartArtworkData.setFaa_artwork_height(resultSet.getFloat("faa_artwork_height"));
		fineartArtworkData.setFaa_artwork_width(resultSet.getFloat("faa_artwork_width"));
		fineartArtworkData.setFaa_artwork_depth(resultSet.getFloat("faa_artwork_depth"));
		fineartArtworkData.setFaa_arwork_measurement_unit(resultSet.getString("faa_arwork_measurement_unit"));
		fineartArtworkData.setFaa_artwork_size_details(resultSet.getString("faa_artwork_size_details"));
		fineartArtworkData.setFaa_artwork_markings(resultSet.getString("faa_artwork_markings"));
		fineartArtworkData.setFaa_artwork_start_year(resultSet.getInt("faa_artwork_start_year"));
		fineartArtworkData.setFaa_artwork_start_year_identifier(resultSet.getString("faa_artwork_start_year_identifier"));
		fineartArtworkData.setFaa_artwork_start_year_precision(resultSet.getString("faa_artwork_start_year_precision"));
		fineartArtworkData.setFaa_artwork_end_year(resultSet.getInt("faa_artwork_end_year"));
		fineartArtworkData.setFaa_artwork_end_year_identifier(resultSet.getString("faa_artwork_end_year_identifier"));
		fineartArtworkData.setFaa_artwork_end_year_precision(resultSet.getString("faa_artwork_end_year_precision"));
		fineartArtworkData.setFaa_artwork_image1(resultSet.getString("faa_artwork_image1"));
		
		return fineartArtworkData;
	}
}
