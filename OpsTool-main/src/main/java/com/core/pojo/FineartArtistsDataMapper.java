package com.core.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class FineartArtistsDataMapper implements RowMapper<FineartArtistsData> {
	
	public FineartArtistsData mapRow(ResultSet resultSet, int i) throws SQLException {
		
		FineartArtistsData fineartArtistsData = new FineartArtistsData();
		
		fineartArtistsData.setFa_artist_ID(resultSet.getInt("fa_artist_ID"));
		fineartArtistsData.setFa_artist_name(resultSet.getString("fa_artist_name"));
		fineartArtistsData.setFa_artist_name_prefix(resultSet.getString("fa_artist_name_prefix"));
		fineartArtistsData.setFa_artist_name_suffix(resultSet.getString("fa_artist_name_suffix"));
		fineartArtistsData.setFa_artist_birth_year(resultSet.getInt("fa_artist_birth_year"));
		fineartArtistsData.setFa_artist_death_year(resultSet.getInt("fa_artist_death_year"));
		fineartArtistsData.setFa_artist_birth_year_identifier(resultSet.getString("fa_artist_birth_year_identifier"));
		fineartArtistsData.setFa_artist_death_year_identifier(resultSet.getString("fa_artist_death_year_identifier"));
		fineartArtistsData.setFa_artist_nationality(resultSet.getString("fa_artist_nationality"));
		fineartArtistsData.setFa_artist_aka(resultSet.getString("fa_artist_aka"));
		fineartArtistsData.setFa_artist_description(resultSet.getString("fa_artist_description"));
        
		return fineartArtistsData;
	}
}
