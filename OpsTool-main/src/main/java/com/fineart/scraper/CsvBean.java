package com.fineart.scraper;

public class CsvBean {
	
	String auction_house_name;
	
	String auction_location;
	
	String auction_num;
	
	String auction_start_date;
	
	String auction_end_date;
	
	String auction_name;
	
	String auction_measureunit;
	
	String lot_num;
	
	String sublot_num;
	
	String price_kind;
	
	String price_estimate_min;
	
	String price_estimate_max;
	
	String price_sold;
	
	String artist_name;
	
	String artist_birth;
	
	String artist_death;
	
	String artist_nationality;
	
	String artwork_name;
	
	String artwork_year_identifier;
	
	String artwork_start_year;
	
	String artwork_end_year;
	
	String artwork_materials;
	
	String artwork_category;
	
	String artwork_markings;
	
	String artwork_edition;
	
	String artwork_description;
	
	String artwork_measurements_height;
	
	String artwork_measurements_width;
	
	String artwork_measurements_depth;
	
	String artwork_size_notes;
	
	String artwork_condition_in;
	
	String artwork_provenance;
	
	String artwork_exhibited;
	
	String artwork_literature;
	
	String artwork_images1;
	
	String artwork_images2;
	
	String artwork_images3;
	
	String artwork_images4;
	
	String artwork_images5;
	
	String image1_name;
	
	String image2_name;
	
	String image3_name;
	
	String image4_name;
	
	String image5_name;
	
	String lot_origin_url;
	
	String artist_ID;
	
	String artwork_ID;
	
	String upload_report;
	
	public String getAuction_house_name() {
		return auction_house_name == null ? "" : auction_house_name.trim();
	}

	public void setAuction_house_name(String auction_house_name) {
		this.auction_house_name = auction_house_name;
	}

	public String getAuction_location() {
		return auction_location == null ? "" : auction_location.trim();
	}

	public void setAuction_location(String auction_location) {
		this.auction_location = auction_location;
	}

	public String getLot_num() {
		return lot_num == null ? "" : lot_num.replaceAll("[^0-9]", "").trim();
	}

	public void setLot_num(String lot_num) {
		this.lot_num = lot_num;
	}

	public String getAuction_num() {
		return auction_num == null ? "" : auction_num.trim();
	}

	public void setAuction_num(String auction_num) {
		this.auction_num = auction_num;
	}

	public String getAuction_start_date() {
		return auction_start_date == null ? "" : auction_start_date.trim();
	}

	public void setAuction_start_date(String auction_start_date) {
		this.auction_start_date = auction_start_date;
	}

	public String getAuction_name() {
		return auction_name == null ? "" : auction_name.trim();
	}

	public void setAuction_name(String auction_name) {
		this.auction_name = auction_name;
	}

	public String getSublot_num() {
		return sublot_num == null ? "" : sublot_num.trim();
	}

	public void setSublot_num(String sublot_num) {
		this.sublot_num = sublot_num;
	}

	public String getPrice_kind() {
		return price_kind == null ? "" : price_kind.trim();
	}

	public void setPrice_kind(String price_kind) {
		this.price_kind = price_kind;
	}

	public String getPrice_estimate_min() {
		return price_estimate_min == null ? "" : price_estimate_min.replaceAll("[^0-9]", "").trim();
	}

	public void setPrice_estimate_min(String price_estimate_min) {
		this.price_estimate_min = price_estimate_min;
	}

	public String getPrice_estimate_max() {
		return price_estimate_max == null ? "" : price_estimate_max.replaceAll("[^0-9]", "").trim();
	}

	public void setPrice_estimate_max(String price_estimate_max) {
		this.price_estimate_max = price_estimate_max;
	}

	public String getPrice_sold() {
		return price_sold == null ? "" : price_sold.trim();
	}

	public void setPrice_sold(String price_sold) {
		this.price_sold = price_sold;
	}

	public String getArtist_name() {
		return artist_name == null ? "" : artist_name.trim();
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public String getArtist_birth() {
		return artist_birth == null ? "" : artist_birth.trim();
	}

	public void setArtist_birth(String artist_birth) {
		this.artist_birth = artist_birth;
	}

	public String getArtist_death() {
		return artist_death == null ? "" : artist_death.trim();
	}

	public void setArtist_death(String artist_death) {
		this.artist_death = artist_death;
	}

	public String getArtist_nationality() {
		return artist_nationality == null ? "" : artist_nationality.trim();
	}

	public void setArtist_nationality(String artist_nationality) {
		this.artist_nationality = artist_nationality;
	}

	public String getArtwork_name() {
		return artwork_name == null ? "" : artwork_name.trim();
	}

	public void setArtwork_name(String artwork_name) {
		this.artwork_name = artwork_name;
	}

	public String getArtwork_materials() {
		return artwork_materials == null ? "" : artwork_materials.trim();
	}

	public void setArtwork_materials(String artwork_materials) {
		this.artwork_materials = artwork_materials;
	}

	public String getArtwork_markings() {
		return artwork_markings == null ? "" : artwork_markings.trim();
	}

	public void setArtwork_markings(String artwork_markings) {
		this.artwork_markings = artwork_markings;
	}

	public String getArtwork_edition() {
		return artwork_edition == null ? "" : artwork_edition.trim();
	}

	public void setArtwork_edition(String artwork_edition) {
		this.artwork_edition = artwork_edition;
	}

	public String getArtwork_description() {
		return artwork_description == null ? "" : artwork_description.trim();
	}

	public void setArtwork_description(String artwork_description) {
		this.artwork_description = artwork_description;
	}

	public String getArtwork_measurements_height() {
		return artwork_measurements_height == null ? "" : (artwork_measurements_height.replaceAll("[^0-9]", "").trim().length() > 9 ? "0.0" : artwork_measurements_height.replaceAll("[^0-9]", "").trim());
	}

	public void setArtwork_measurements_height(String artwork_measurements_height) {
		this.artwork_measurements_height = artwork_measurements_height;
	}

	public String getArtwork_measurements_width() {
		return artwork_measurements_width == null ? "" : (artwork_measurements_width.replaceAll("[^0-9]", "").trim().length() > 9 ? "0.0" : artwork_measurements_width.replaceAll("[^0-9]", "").trim());
	}

	public void setArtwork_measurements_width(String artwork_measurements_width) {
		this.artwork_measurements_width = artwork_measurements_width;
	}

	public String getArtwork_measurements_depth() {
		return artwork_measurements_depth == null ? "" : (artwork_measurements_depth.replaceAll("[^0-9]", "").trim().length() > 9 ? "0.0" : artwork_measurements_depth.replaceAll("[^0-9]", "").trim());
	}

	public void setArtwork_measurements_depth(String artwork_measurements_depth) {
		this.artwork_measurements_depth = artwork_measurements_depth;
	}

	public String getArtwork_size_notes() {
		return artwork_size_notes == null ? "" : artwork_size_notes.trim();
	}

	public void setArtwork_size_notes(String artwork_size_notes) {
		this.artwork_size_notes = artwork_size_notes;
	}

	public String getAuction_measureunit() {
		return auction_measureunit == null ? "" : (auction_measureunit.trim().length() > 9 ? "" :  auction_measureunit.trim());
	}

	public void setAuction_measureunit(String auction_measureunit) {
		this.auction_measureunit = auction_measureunit;
	}

	public String getArtwork_condition_in() {
		return artwork_condition_in == null ? "" : artwork_condition_in.trim();
	}

	public void setArtwork_condition_in(String artwork_condition_in) {
		this.artwork_condition_in = artwork_condition_in;
	}

	public String getArtwork_provenance() {
		return artwork_provenance == null ? "" : artwork_provenance.trim();
	}

	public void setArtwork_provenance(String artwork_provenance) {
		this.artwork_provenance = artwork_provenance;
	}

	public String getArtwork_exhibited() {
		return artwork_exhibited == null ? "" : artwork_exhibited.trim();
	}

	public void setArtwork_exhibited(String artwork_exhibited) {
		this.artwork_exhibited = artwork_exhibited;
	}

	public String getArtwork_literature() {
		return artwork_literature == null ? "" : artwork_literature.trim();
	}

	public void setArtwork_literature(String artwork_literature) {
		this.artwork_literature = artwork_literature;
	}

	public String getArtwork_images1() {
		return artwork_images1 == null ? "" : artwork_images1.trim();
	}

	public void setArtwork_images1(String artwork_images1) {
		this.artwork_images1 = artwork_images1;
	}

	public String getArtwork_images2() {
		return artwork_images2 == null ? "" : artwork_images2.trim();
	}

	public void setArtwork_images2(String artwork_images2) {
		this.artwork_images2 = artwork_images2;
	}

	public String getArtwork_images3() {
		return artwork_images3 == null ? "" : artwork_images3.trim();
	}

	public void setArtwork_images3(String artwork_images3) {
		this.artwork_images3 = artwork_images3;
	}

	public String getArtwork_images4() {
		return artwork_images4 == null ? "" : artwork_images4.trim();
	}

	public void setArtwork_images4(String artwork_images4) {
		this.artwork_images4 = artwork_images4;
	}

	public String getArtwork_images5() {
		return artwork_images5 == null ? "" : artwork_images5.trim();
	}

	public void setArtwork_images5(String artwork_images5) {
		this.artwork_images5 = artwork_images5;
	}

	public String getLot_origin_url() {
		return lot_origin_url == null ? "" : lot_origin_url.trim();
	}

	public void setLot_origin_url(String lot_origin_url) {
		this.lot_origin_url = lot_origin_url;
	}

	public String getArtwork_year_identifier() {
		return artwork_year_identifier == null ? "exact" : artwork_year_identifier.trim();
	}

	public void setArtwork_year_identifier(String artwork_year_identifier) {
		this.artwork_year_identifier = artwork_year_identifier;
	}

	public String getArtwork_start_year() {
		return artwork_start_year == null ? "" : artwork_start_year.replaceAll("[^0-9]", "").trim();
	}

	public void setArtwork_start_year(String artwork_start_year) {
		this.artwork_start_year = artwork_start_year;
	}

	public String getArtwork_end_year() {
		return artwork_end_year == null ? "" : artwork_end_year.replaceAll("[^0-9]", "").trim();
	}

	public void setArtwork_end_year(String artwork_end_year) {
		this.artwork_end_year = artwork_end_year;
	}

	public String getArtwork_category() {
		if(!artwork_category.equalsIgnoreCase("paintings") && !artwork_category.equalsIgnoreCase("sculptures") && !artwork_category.equalsIgnoreCase("prints") 
				&& !artwork_category.equalsIgnoreCase("photographs") && !artwork_category.equalsIgnoreCase("works on paper") 
				&& !artwork_category.equalsIgnoreCase("miniatures")) {
			return "paintings";
		}
		return artwork_category == null ? "paintings" : artwork_category.trim();
	}

	public void setArtwork_category(String artwork_category) {
		this.artwork_category = artwork_category;
	}

	public String getImage1_name() {
		return image1_name == null ? "na" : image1_name.trim();
	}

	public void setImage1_name(String image1_name) {
		this.image1_name = image1_name;
	}

	public String getImage2_name() {
		return image2_name == null ? "na" : image2_name.trim();
	}

	public void setImage2_name(String image2_name) {
		this.image2_name = image2_name;
	}

	public String getImage3_name() {
		return image3_name == null ? "na" : image3_name.trim();
	}

	public void setImage3_name(String image3_name) {
		this.image3_name = image3_name;
	}

	public String getImage4_name() {
		return image4_name == null ? "na" : image4_name.trim();
	}

	public void setImage4_name(String image4_name) {
		this.image4_name = image4_name;
	}

	public String getImage5_name() {
		return image5_name == null ? "na" : image5_name.trim();
	}

	public void setImage5_name(String image5_name) {
		this.image5_name = image5_name;
	}

	public String getArtist_ID() {
		return artist_ID;
	}

	public void setArtist_ID(String artist_ID) {
		this.artist_ID = artist_ID;
	}

	public String getArtwork_ID() {
		return artwork_ID;
	}

	public void setArtwork_ID(String artwork_ID) {
		this.artwork_ID = artwork_ID;
	}

	public String getUpload_report() {
		return upload_report;
	}

	public void setUpload_report(String upload_report) {
		this.upload_report = upload_report;
	}

	public String getAuction_end_date() {
		return auction_end_date.trim();
	}

	public void setAuction_end_date(String auction_end_date) {
		this.auction_end_date = auction_end_date;
	}
}