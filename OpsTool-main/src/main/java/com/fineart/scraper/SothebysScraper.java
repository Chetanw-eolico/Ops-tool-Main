package com.fineart.scraper;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.core.fineart.FineartUtils;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

import au.com.bytecode.opencsv.CSVWriter;

public class SothebysScraper {
	
	private static final Pattern DIM_WORDS = Pattern.compile(
			"((\\d+)(.*)([a-z]|[A-Z])(in|cm|mm)+(.*)|[\\s]*([diameter: ])+[\\s*](.*)[\\s*]([cm.])+[\\s]*|(.*)[\\d]{1,}[\\s]*[\\D]{1,}(cm)(.*)|(.*)(cm)[\\s]*[\\d]{1,}[\\s]*[\\D]{1,}(.*)|"
					+ "(.*)(in.)+(.*)(cm)|(.*)(by)+(.*)(mm.|cm.)|(.*)(by)+(.*)(inches)|(.*)[\\d]+(cm)(.*)|(.*)cm[\\s][\\d]+|(.*)(\\d+)(.*)(in|cm|mm)+(.*)|dimensions(.*)cm|"
					+ "(\\d+)(.*)(in|cm|mm)+(.*)|(.*)(\\d+)(.*)(in|cm)+(.*))",
			Pattern.CASE_INSENSITIVE);
	
	private static final Pattern EDITION_WORDS = Pattern.compile(".*(Edition|edition|this work|Unique|unique).*");
	
	private static final Pattern MATERIAL_WORDS = Pattern.compile(
			"((engravings|Vintage|Paire|watercolour|Each: steel|yarn|telone|ferro|lamiera|multigum|tecnica|alluminio|Série|scultura|Silber|Encre|tavola|acrilico|engraved|lithographed|woodengraved|woodcut|gouche|Weißgold|panelling|decorated|Épreuve|coat of arms|Crayon|Fusain|imprimés|Rosegold 585|Crayons|Crayons|frosted|farbloses Glas|Platin 950|Schale|toile|Panneau|bent|beechwood|stained bent beechwood|backrest|stained|bentwood|cane|woven|Taschenuhr|Messingguss|Keramik|solid|mirror|foam|horns|rods|tar|knife|oil assemblage|Metal|monotype printed in colours|on japan|original canvas|on cream laid|each oil on canvas board|oil and canvas|on heavy wove|on laid|on thin wove|on arches|on BFK Rives|on TH Saunders|on Van Gelder laid|on van Gelder Zonen|on Somerset satin|on glossy wove|engraving|dark|on wove|dustgrain gravure and aquatint printed in colours|plastic foam lined case|oil on masonite|pencil and fabric collage|oil on hemp cloth|drypoint with hand coloring|sliced violin and cast bronze violin inside a violin case in a Plexiglas case|MIS and Lysonic inkjet prints on Thai mulberry paper with wax|Albâtre|PANNEAU|Carton|paint|spray-paint|quatrefoil|woodcut|beide|brass|photograpy|crayon|chalk|gouache|offsetlithograph|graphite|ink|leather|crayon|linocut|polished|mixed media|monoprint|digital|pastel|pen|print|plaster|tempera|pressed|vinyl| powder puff, spray pigment,|walnut|fiberglass|waterprint|digital pigment print|pienzo pigment prints|Batik|Olio|Pastello|Bubinga|Tre oli|Hershey bar|Acquerello|silk|color|decommissioned|ballpoint pen|rubber|glitter|wood|verde|pigment|c-print|plastiline|synthetic|rustoleum enamel|porcelain|jigsaw|neon lights mounted|spackle and latex on linoleum tiles|black soap|Dimensions|guitar amplifier|oil on|oil|pencil and mixed media on linen|Mahagoni|Casein|Öl auf|Pastel|photograpy|grey|oil|rosewood)([ ,].*)?" // Starts
					+ "|(.* )?(Aquarell|ivory|woods|Métal|wood|cineraria|wove paper|plastic|gesso|oil on canvas|acrylic|airbrush|albumen|ink|aluminum|bronze|ceramic|charcoal|chrome|chromogenic|cibachrome|cooper|etching|glass|glaze|gold|"
					+ "lithograph|engraving|plexiglas|Cuivre|sur|Ensemble de|ferro|walnut|tecnica|scultura|multigum|lamiera|alluminio|telone|acrilico|PANNEAU|tavola|Samtliga|Samtida|printed|Drawings|engraved|Série|paper|polished|edangered|Carton|carvings|frosted|encre|coat of arms|gouache|Albâtre|Crayons|sur|wood-engravings|printed in 5 colours|on buff paper|pencils|pencil|photogravure|sanguine|body white|wood-engraving|framing|upper corners rounded|watercolour|body colour|grey|white chalks|on laid paper|wove|chalk|sheets|ithograph|lithograh|lithographs|offsetlithograph|screen|X-ray|lithography|silkscreen|mahogany|metal|oak|on wallpaper|palladium|pattern|photograph|platinum|plywood|poster|albuminé|plomb|"
					+ "rosewood|(screen)?print|serigraph|silver|steel|teak|upholstery|watercolor|woodblock|woodcut|wool|glass|auf|bronze with wooden base|frame|Enamel|gilt|granite|hardboard|iron|volant|screenprints|wove|mauve|tissue|on japan|Pochoir|on Arches|Drypoint|aquatint|carborundum|photographic|polychrome|sissoo|polyester|handmade|and printed paper collage|wood|polymer paint|wrapper collage|on light blue translucent Plexiglas|, wax, brass, concrete,spray paint, incense|, pedal, DVD player,|encaustic and varnish|varnish|magazines|aluminium and glue|laid on |hoses|and coloured|and ink on foamcore|and enamel|antique|light box|wax crayon|wax crayon|coloured crayon|paper pulp|rhinestones|embroidery|beading|flashe|neon|UV ink|wire|linen|tin|colored|unpainted|sequins|Plexiglas|engraved|lithographed|woodengraved|woodcut|gouche|planche|particle board|screws|nails|patinated|staples|tape)([ ,\\-].*)?" // middle
					+ "|.* (on canvas|on board|on cream laid|on heavy wove|lamiera|tecnica|ferro|scultura|PANNEAU|multigum|boards|alluminio|telone|tavola|acrilico|on thin wove|stained|plinth|doors|opening|drawers|motifs|ornament|compartment|restored|patterning|panels|Série|Carton|couleurs|crayon|Crayon|encre|frosted|Crayons|paper with card support|Albâtre|on wove|stencil and screen printed paper collage on wooden skateboard deck|patina|holz|bronze|papier|leinwand|papier auf karton|on paper|on unstretched canvas|su tela|su cartone|crayons on paper|su carta|with pins|paper|and wood|brass|with hot glue|resinon linen|and iron|paper pulp|marble|resin|on paper over board|on graph paper|and painted latex|with portfolio box|on Plexiglas|with Danish oil|cibachrome print|on panel|over panel|on HMP paper|c-print|on bristol board|over panel|silhouettes|paper|puzzle| with wire, string or lace and transformer|on wood panel|and shea butter|variable|electronic equipment|on museum board|in wooden artist frame|engraved|lithographed|woodengraved|woodcut|gouche|in wooden artist|parts|patinated|toiles|on laid paper|panneaux|planches)" // Ends
					+ ").*",
			Pattern.CASE_INSENSITIVE);
	
	private static final Pattern SIGN_WORDS = Pattern.compile(
			".*(signed|designed|silver|inscribed|stencilled|étiquette|dessin|Au revers|gauche|Inscription|Longue|favrile|dated|lettered|Stamped|label|titled|signature|upper left|lower left|verso|"
					+ "annoted|dedicated|annotated|impression|crater-shaped|naux|noll|POMPON|LALANNE|JKM|PAUL|AG0|EBRANDT|JEAN|séquences|sign|DU|PN|sculpté|revers|émaillés|"
					+ "epruve|Despositio|dalpayrat|wallander|signature|Rückseitig|kosta|moulded|etikett|elise|bruxelles|dalmatian|wheel-engraved|monogram|éditée par|"
					+ "ink on the inside|stamped|dedicated|titleslip|titled|singed|title|embos|sed|incised|numbered|initialed|initialled|bears|dated|logelain|annotated|Consolidated|"
					+ "stretched|overlaid|impressed|inlaid|enamelled|produced|captioned|marked|carved|walnut|mark|marks|cracks|miss|cipher|label|cypher|initial|medallion|seal|decal|"
					+ "branded|embroidered|molded|left and right form|monogrammed|stenciled|Dédicacée|sign|janda|maeght|gießerstempel|gestempelt|datiert|paraphe|betitelt|krepp|berlin"
					+ "|auflage|timbre|porte|estampill|dat|num|Porte|monogram|Attribu|firmada|firm).*");
	
	public static final Set<Character> SPECIAL_SYMBOL_SET = new HashSet<Character>();

    static {
        SPECIAL_SYMBOL_SET.add(';');
        SPECIAL_SYMBOL_SET.add('(');
        SPECIAL_SYMBOL_SET.add(')');
        SPECIAL_SYMBOL_SET.add(':');
        SPECIAL_SYMBOL_SET.add(',');
        SPECIAL_SYMBOL_SET.add(' ');
        SPECIAL_SYMBOL_SET.add('.');
        SPECIAL_SYMBOL_SET.add('-');
        SPECIAL_SYMBOL_SET.add(']');
        SPECIAL_SYMBOL_SET.add('[');
		
    }
	
	private JdbcTemplate jdbcTemplate;
	private String username;

	public SothebysScraper(String username, JdbcTemplate jdbcTemplate, int pageStart, int pageEnd, int auctionSerialStart, int auctionSerialEnd) {

		this.jdbcTemplate = jdbcTemplate;
		this.username = username;
		
		String pageLink = "https://www.sothebys.com/en/results?from=&to=&f2=00000164-609b-d1db-a5e6-e9ff01230000&f2=00000164-609b-d1db-a5e6-e9ff08ab0000&f2=00000164-609b-d1db-a5e6-e9ff0b150000&f2=00000164-609a-d1db-a5e6-e9fff79f0000&f2=00000164-609b-d1db-a5e6-e9ff043c0000&f2=00000164-609a-d1db-a5e6-e9fffe5f0000&f2=00000164-609a-d1db-a5e6-e9fffdf80000&f2=00000164-609b-d1db-a5e6-e9ff0a800000&f2=00000164-609b-d1db-a5e6-e9ff06270000&f2=00000164-609a-d1db-a5e6-e9fff8660000&f2=00000164-609b-d1db-a5e6-e9ff08440000&f2=00000164-609b-d1db-a5e6-e9ff0ba60000&f2=00000164-609a-d1db-a5e6-e9fffd2c0000&f2=00000164-609a-d1db-a5e6-e9fff6760000&f2=00000164-609a-d1db-a5e6-e9fffa760000&f2=00000164-609a-d1db-a5e6-e9ffff270000&f2=00000164-609b-d1db-a5e6-e9ff07220000&f2=00000164-609b-d1db-a5e6-e9ff068c0000&f2=00000164-609b-d1db-a5e6-e9ff09100000&f2=00000164-609b-d1db-a5e6-e9ff055d0000&f2=00000164-609b-d1db-a5e6-e9ff0a350000&f2=00000164-609b-d1db-a5e6-e9ff04fc0000&f2=00000164-609b-d1db-a5e6-e9ff02b50000&f2=00000164-609a-d1db-a5e6-e9fff8ca0000&f2=00000164-609b-d1db-a5e6-e9ff07e20000&f2=00000164-609a-d1db-a5e6-e9fffadc0000&f2=00000164-609a-d1db-a5e6-e9fff5b50000&f2=00000164-609a-d1db-a5e6-e9fff73c0000&f2=00000164-609b-d1db-a5e6-e9ff03900000&f2=00000164-609b-d1db-a5e6-e9ff09a60000&f2=00000164-609a-d1db-a5e6-e9fffec40000&f2=00000164-609a-d1db-a5e6-e9fff6150000&f3=LIVE&f3=ONLINE&q=&p=";
		
		String sql = "select scraping_download_path from operations_team where username = '" + username + "'";
		
		String path = "";
		
		String artistsIndexPath = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {

			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return resultSet.getString("scraping_download_path").toLowerCase();
				} else {
					return "";
				}
			}
		});
		
		JFileChooser jfc = new JFileChooser(artistsIndexPath);
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int returnVal = jfc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			
			path = jfc.getSelectedFile().getAbsolutePath().replaceAll("\\\\", "//");
			
			String insertSql = "UPDATE operations_team SET scraping_download_path='" + path + "' WHERE  username = '" + username + "'";
			jdbcTemplate.update(insertSql);
			
			
		}
		
		for(int pageCounter = pageStart; pageCounter <= pageEnd; pageCounter++) { // page number loop
			String countedPageLink = pageLink + pageCounter;
			UserAgent userAgent1 = new UserAgent();
			try {
				userAgent1.visit(countedPageLink);
			} catch (ResponseException e2) {
				e2.printStackTrace();
			}
			int auctionListItemCounter = 1;
			Elements cardContainer = userAgent1.doc.findEvery("<a class='Card-info-container'");
			for(Element auctionListElement : cardContainer) { // Auction Listing Loop
				if(auctionListItemCounter < auctionSerialStart || auctionListItemCounter > auctionSerialEnd) {
					System.out.println("Inside continue. auctionListItemCounter: " + auctionListItemCounter);
					auctionListItemCounter++;
					continue;
				}
				System.out.println("Outside continue. auctionListItemCounter: " + auctionListItemCounter);
				AuctionBean auctionBean = new AuctionBean();
				try {
					populateAuctionDetails(auctionListElement, auctionBean);
				} catch (NotFound | ParseException e1) {
					e1.printStackTrace();
				}
				String auctionUrl = auctionListElement.getAtString("href");
				UserAgent userAgent2 = new UserAgent();
				try {
					userAgent2.visit(auctionUrl);
				} catch (ResponseException e1) {
					e1.printStackTrace();
				}
				String saleNumber = "";
				try {
					saleNumber = userAgent2.doc.findFirst("<div class='css-1ewow1l'>").findFirst("p").getTextContent().replaceAll("Sale", "").trim();
				} catch (NotFound e1) {
					System.out.println("Could not find Sale number with new style parsing. Trying the old style now...");
				}
				if(StringUtils.isEmpty(saleNumber)) {
					try {
						saleNumber = userAgent2.doc.findFirst("<div class='eventdetail-saleinfo'>").findFirst("span").getTextContent().replaceAll("Sale Number: ", "").trim();
					} catch (NotFound e) {
						System.out.println("Could not find Sale number with old style parsing too. Leaving the sale number blank now...");
					}
				}
				auctionBean.setFaac_auction_sale_code(saleNumber);
				
				String fileName = auctionBean.getFaac_auction_title() + "-" + auctionBean.getFaac_auction_sale_code() + "-" + auctionBean.getFaac_auction_start_date();
				fileName = fileName.replaceAll("[^\\w]","-") + ".csv"; //replace all special characters with hyphen
				CSVWriter csvWriter = null;
				try {
					csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(path + "/" + fileName), "UTF-8"));
				} catch (UnsupportedEncodingException | FileNotFoundException e1) {
					e1.printStackTrace();
				}
				writeCsvFile(csvWriter);
				
				populateAuctionLots(auctionBean, auctionUrl, csvWriter);
				auctionListItemCounter++;
			}
		}
	}
	
	/*
	 * Gets the auction lot links to populate data
	 */

	private void populateAuctionLots(AuctionBean auctionBean, String auctionUrl, CSVWriter csvWriter) {
		try {
			String scriptCode = "";
			int startIndex = 0;
			int endIndex = 0;
			String auctionYear = auctionUrl.replace("https://www.sothebys.com/en/auctions/", "").replace(auctionBean.getFaac_auction_sale_code().toLowerCase(), "").replace(".html", "");
			UserAgent userAgent = new UserAgent(); // create new userAgent
			userAgent.visit(auctionUrl); // visit a url
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			Document doc = Jsoup.parse(content);
			org.jsoup.select.Elements l_ele = doc.select("script[type=text/javascript]");

			for (org.jsoup.nodes.Element element : l_ele) {
				scriptCode = element.html();
				if (scriptCode.contains("function(window,undefined)")) {
					startIndex = (scriptCode.indexOf("//set ECAT.lot")) + 19;
					endIndex = scriptCode.indexOf("ECAT.lot = cleanArray(ECAT.lot)");
					scriptCode = scriptCode.substring(startIndex, endIndex);
					String l_lot_info_arr[] = scriptCode.split(";ECAT");

					for (int i = 0; i < l_lot_info_arr.length; i++) {
						if (l_lot_info_arr[i].contains("'id':")) {
							String[] ar = l_lot_info_arr[i].split("'id':");
							String lotStr = ar[1].substring(0, 5).replaceAll("[,']", "");
							String lotUrl = "http://www.sothebys.com/en/auctions/ecatalogue/" + auctionYear
									+ auctionBean.getFaac_auction_sale_code().toLowerCase() + "/lot." + lotStr + ".html";
							CsvBean csvBean = new CsvBean(); //every loop will have a new bean setting common auction details from auction bean
							csvBean.setAuction_name(auctionBean.getFaac_auction_title());
							csvBean.setAuction_location(auctionBean.getCah_auction_house_location());
							csvBean.setAuction_start_date(auctionBean.getFaac_auction_start_date());
							csvBean.setAuction_end_date(auctionBean.getFaac_auction_end_date());
							csvBean.setAuction_num(auctionBean.getFaac_auction_sale_code());
							populateLotDetails(lotUrl, csvBean, true);
							try {
								writeCsvRow(csvWriter, csvBean);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					break;
				}
			}
			if (!scriptCode.equals("")) {
			} else {
				l_ele = doc.select("script");
				scriptCode = "";
				for (org.jsoup.nodes.Element element : l_ele) {
					scriptCode = element.html();
					System.out.println(scriptCode);
					if (scriptCode.contains("@context")) {
						startIndex = (scriptCode.indexOf("offers\":[")) + 8;
						endIndex = scriptCode.indexOf("},\"performer");
						scriptCode = scriptCode.substring(startIndex, endIndex);
						JSONArray getArray = new JSONArray(scriptCode);
						String lotUrl = "";

						for (int i = 0; i < getArray.length(); i++) {
							JSONObject obj = getArray.getJSONObject(i);
							if (!(obj.isNull("url"))) {
								lotUrl = (String) obj.get("url");
								System.out.println(lotUrl);
								CsvBean csvBean = new CsvBean(); //every loop will have a new bean setting common auction details from auction bean
								csvBean.setAuction_name(auctionBean.getFaac_auction_title());
								csvBean.setAuction_location(auctionBean.getCah_auction_house_location());
								csvBean.setAuction_start_date(auctionBean.getFaac_auction_start_date());
								csvBean.setAuction_end_date(auctionBean.getFaac_auction_end_date());
								csvBean.setAuction_num(auctionBean.getFaac_auction_sale_code());
								populateLotDetails(lotUrl, csvBean, false);
								try {
									writeCsvRow(csvWriter, csvBean);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("CAN'T ADD LOTS.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeCsvFile(CSVWriter csvWriter) {
		try {
			StringBuilder stats = new StringBuilder();
			stats.append("auction_house_name|auction_location|auction_num|auction_start_date|auction_end_date|auction_name|");
			stats.append("lot_num|sublot_num|price_kind|price_estimate_min|price_estimate_max|price_sold|");
			stats.append("artist_name|artist_birth|artist_death|artist_nationality|");
			stats.append("artwork_name|artwork_year_identifier|artwork_start_year|artwork_end_year|");
			stats.append("artwork_materials|artwork_category|artwork_markings|");
			stats.append("artwork_edition|artwork_description|artwork_measurements_height|artwork_measurements_width|");
			stats.append("artwork_measurements_depth|artwork_size_notes|auction_measureunit|artwork_condition_in|");
			stats.append("artwork_provenance|artwork_exhibited|artwork_literature|artwork_images1|");
			stats.append("artwork_images2|artwork_images3|artwork_images4|artwork_images5|image1_name|image2_name|image3_name|image4_name|image5_name|lot_origin_url|");
			
			csvWriter.writeNext(stats.toString().split("\\|"));
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void writeCsvRow(CSVWriter csvWriter, CsvBean csvBean) {
		try {
			String imageName = csvBean.getAuction_name() + "-" + csvBean.getArtist_name() + "-" + System.currentTimeMillis();
			imageName = imageName.replaceAll("[^\\w]","-"); //replace all special characters with hyphen
			
			csvBean.setAuction_house_name(csvBean.getAuction_house_name() == null ? "" : csvBean.getAuction_house_name());
			csvBean.setAuction_location(csvBean.getAuction_location() == null ? "" : csvBean.getAuction_location());
			csvBean.setAuction_num(csvBean.getAuction_num() == null ? "" : csvBean.getAuction_num());
			csvBean.setAuction_start_date(csvBean.getAuction_start_date() == null ? "" : csvBean.getAuction_start_date());
			
			csvBean.setAuction_end_date(csvBean.getAuction_end_date() == null ? "" : csvBean.getAuction_end_date());
			csvBean.setAuction_name(csvBean.getAuction_name() == null ? "" : WordUtils.capitalizeFully(csvBean.getAuction_name()));//title case
			csvBean.setLot_num(csvBean.getLot_num() == null ? "" : csvBean.getLot_num());
			csvBean.setSublot_num(csvBean.getSublot_num() == null ? "" : csvBean.getSublot_num());
			csvBean.setPrice_kind(csvBean.getPrice_kind() == null ? "" : csvBean.getPrice_kind());
			csvBean.setPrice_estimate_min(csvBean.getPrice_estimate_min() == null ? "" : csvBean.getPrice_estimate_min());
			csvBean.setPrice_estimate_max(csvBean.getPrice_estimate_max() == null ? "" : csvBean.getPrice_estimate_max());
			csvBean.setPrice_sold(csvBean.getPrice_sold() == null ? "" : csvBean.getPrice_sold());
			csvBean.setArtist_name(csvBean.getArtist_name() == null ? "" : csvBean.getArtist_name());
			csvBean.setArtist_nationality(csvBean.getArtist_nationality() == null ? "" : csvBean.getArtist_nationality());
			csvBean.setArtwork_name(csvBean.getArtwork_name() == null ? "" : WordUtils.capitalizeFully(csvBean.getArtwork_name()));
			csvBean.setArtwork_year_identifier(csvBean.getArtwork_year_identifier() == null ? "" : csvBean.getArtwork_year_identifier());
			csvBean.setArtwork_start_year(csvBean.getArtwork_start_year() == null ? "" : csvBean.getArtwork_start_year());
			csvBean.setArtwork_end_year(csvBean.getArtwork_end_year() == null ? "" : csvBean.getArtwork_end_year());
			csvBean.setArtwork_materials(csvBean.getArtwork_materials() == null ? "" : csvBean.getArtwork_materials());
			csvBean.setArtwork_category(csvBean.getArtwork_category() == null ? "" : csvBean.getArtwork_category());
			csvBean.setArtwork_markings(csvBean.getArtwork_markings() == null ? "" : csvBean.getArtwork_markings());
			csvBean.setArtwork_edition(csvBean.getArtwork_edition() == null ? "" : csvBean.getArtwork_edition());
			csvBean.setArtwork_description(csvBean.getArtwork_description() == null ? "" : csvBean.getArtwork_description());
			csvBean.setArtwork_measurements_height(csvBean.getArtwork_measurements_height() == null ? "" : csvBean.getArtwork_measurements_height());
			csvBean.setArtwork_measurements_width(csvBean.getArtwork_measurements_width() == null ? "" : csvBean.getArtwork_measurements_width());
			csvBean.setArtwork_measurements_depth(csvBean.getArtwork_measurements_depth() == null ? "" : csvBean.getArtwork_measurements_depth());
			csvBean.setArtwork_size_notes(csvBean.getArtwork_size_notes() == null ? "" : csvBean.getArtwork_size_notes());
			csvBean.setAuction_measureunit(csvBean.getAuction_measureunit() == null ? "" : csvBean.getAuction_measureunit());
			csvBean.setArtwork_condition_in(csvBean.getArtwork_condition_in() == null ? "" : csvBean.getArtwork_condition_in());
			csvBean.setArtwork_provenance(csvBean.getArtwork_provenance() == null ? "" : csvBean.getArtwork_provenance());
			csvBean.setArtwork_exhibited(csvBean.getArtwork_exhibited() == null ? "" : csvBean.getArtwork_exhibited());
			csvBean.setArtwork_literature(csvBean.getArtwork_literature() == null ? "" : csvBean.getArtwork_literature());
			csvBean.setArtwork_images1(csvBean.getArtwork_images1() == null ? "" : csvBean.getArtwork_images1());
			csvBean.setArtwork_images2(csvBean.getArtwork_images2() == null ? "" : csvBean.getArtwork_images2());
			csvBean.setArtwork_images3(csvBean.getArtwork_images3() == null ? "" : csvBean.getArtwork_images3());
			csvBean.setArtwork_images4(csvBean.getArtwork_images4() == null ? "" : csvBean.getArtwork_images4());
			csvBean.setArtwork_images5(csvBean.getArtwork_images5() == null ? "" : csvBean.getArtwork_images5());
			
			String image1Name = StringUtils.isEmpty(csvBean.getArtwork_images1()) ? "" : imageName + "-a.jpg";
			String image2Name = StringUtils.isEmpty(csvBean.getArtwork_images2()) ? "" : imageName + "-b.jpg";
			String image3Name = StringUtils.isEmpty(csvBean.getArtwork_images3()) ? "" : imageName + "-c.jpg";
			String image4Name = StringUtils.isEmpty(csvBean.getArtwork_images4()) ? "" : imageName + "-d.jpg";
			String image5Name = StringUtils.isEmpty(csvBean.getArtwork_images5()) ? "" : imageName + "-e.jpg";
			
			String auctionDetails = csvBean.getAuction_house_name() + "|" + csvBean.getAuction_location() + "|" + csvBean.getAuction_num() + "|" 
					+ csvBean.getAuction_start_date() + "|" + csvBean.getAuction_end_date() + "|" + csvBean.getAuction_name() + "|" + csvBean.getLot_num() + "|" 
					+ csvBean.getSublot_num() + "|" + csvBean.getPrice_kind() 
					+ "|" + csvBean.getPrice_estimate_min() + "|" + csvBean.getPrice_estimate_max() + "|" + csvBean.getPrice_sold() + "|" 
					+ csvBean.getArtist_name() + "|" + csvBean.getArtist_birth() + "|" + csvBean.getArtist_death() + "|" + csvBean.getArtist_nationality() + "|"
					+ csvBean.getArtwork_name() + "|" + csvBean.getArtwork_year_identifier() + "|" + csvBean.getArtwork_start_year() + "|" 
					+ csvBean.getArtwork_end_year() + "|" + csvBean.getArtwork_materials() + "|" + csvBean.getArtwork_category() + "|"
					+ csvBean.getArtwork_markings() + "|" + csvBean.getArtwork_edition() + "|" + csvBean.getArtwork_description() + "|"
					+ csvBean.getArtwork_measurements_height() + "|" + csvBean.getArtwork_measurements_width() + "|" + csvBean.getArtwork_measurements_depth()
					+ "|" + csvBean.getArtwork_size_notes() + "|" + csvBean.getAuction_measureunit() + "|" + csvBean.getArtwork_condition_in() + "|"
					+ csvBean.getArtwork_provenance() + "|" + csvBean.getArtwork_exhibited() + "|" + csvBean.getArtwork_literature() + "|" 
					+ csvBean.getArtwork_images1() + "|" + csvBean.getArtwork_images2() + "|" + csvBean.getArtwork_images3() + "|" + csvBean.getArtwork_images4() 
					+ "|" + csvBean.getArtwork_images5() + "|" + image1Name + "|" + image2Name + "|" + image3Name 
					+ "|" + image4Name + "|" + image5Name + "|" + csvBean.getLot_origin_url();

			String row[] = auctionDetails.split("\\|");
			csvWriter.writeNext(row);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	

	private void populateLotDetails(String lotLink, CsvBean csvBean, boolean isOldStyleLot) {
		System.out.println("Parsing details for Sale Number: " + csvBean.getAuction_num() + " Auction Name: " + csvBean.getAuction_name());
		String lot_num = "";
		String price_kind = "";
		String price_estimate_min = "";
		String price_estimate_max = "";
		String price_sold = "";
		String artist_name = "";
		String artist_birth = "";
		String artist_death = "";
		String artist_nationality = "";
		String artwork_name = "";
		String artwork_year_identifier = "";
		String artwork_start_year = "";
		String artwork_end_year = "";
		String artwork_materials = "";
		String artwork_category = "";
		String artwork_markings = "";
		String artwork_edition = "";
		String artwork_description = "";
		String artwork_condition_in = "";
		String artwork_provenance = "";
		String artwork_exhibited = "";
		String artwork_literature = "";
		Pattern l_pattern = null;
		Matcher l_matcher = null;
		org.jsoup.select.Elements lotDetails = null;
		org.jsoup.select.Elements catalogue = null;
		org.jsoup.select.Elements catalogueDetails = null;
		
		try {
			Document doc = null;
			int i = 0;
			boolean description = false;
			boolean dimension = false;
			UserAgent userAgent = new UserAgent();
			userAgent.visit(lotLink);
			String content = userAgent.doc.innerHTML();
			if(content.contains("notfound.html")) {
				lotLink = lotLink.replaceAll(csvBean.getAuction_num().toLowerCase(), "");
				userAgent.visit(lotLink);
				content = userAgent.doc.innerHTML();
			}
			userAgent.close();
			doc = Jsoup.parse(content);
			processImages(userAgent, csvBean, isOldStyleLot);
			//System.out.println("Images parsed for this Lot...");
			try {
				
				if(isOldStyleLot) {

					// URL2 lot details
					org.jsoup.select.Elements priceSold = doc.select("div[class=price-sold]");
					org.jsoup.select.Elements artistName = doc.select("div[class=lotdetail-guarantee]");
					org.jsoup.select.Elements artworkName = doc.select("div[class=lotdetail-subtitle]");
					org.jsoup.select.Elements priceMin = doc.select("span[class=range-from]");
					org.jsoup.select.Elements priceMax = doc.select("span[class=range-to]");
					
					lot_num = doc.select("div[class=lotdetail-lotnumber visible-phone]").text();
					artist_birth = doc.select("div[class=lotdetail-artist-dates]").text().replaceAll("[^0-9]", "").trim();
					lotDetails = doc.select("div[class=lotdetail-description-text]");
					catalogue = doc.select("h3[class=lotdetail-section-sub-header]");
					catalogueDetails = doc.select("div[class=readmore-content]");
					
					if (priceSold.size() > 0) {
						price_sold = priceSold.first().text().replaceAll("[^0-9]", "").trim();
					}
					if (artistName.size() > 0) {
						artist_name = artistName.first().text();
					}
					if (artworkName.size() > 0) {
						artwork_name = artworkName.first().text();
					}
					if (priceMin.size() > 0) {
						price_estimate_min = priceMin.first().text().replace(",", "");
					}
					if (priceMax.size() > 0) {
						price_estimate_max = priceMax.first().text().replace(",", "");
					}
					description = true;
					System.out.println("Parsing old style lot number:" + lot_num);
					
					
					if (price_estimate_min.contains("-")) {
						price_estimate_max = price_estimate_min.split("-")[1].replaceAll("[^0-9]", "").trim();
						price_estimate_min = price_estimate_min.split("-")[0].replaceAll("[^0-9]", "").trim();
					}

					if (!StringUtils.isBlank(price_sold)) {
						price_kind = "sold price";
					} else if (!StringUtils.isBlank(price_estimate_max) || !StringUtils.isBlank(price_estimate_min)) {
						price_kind = "estimate";
					} else {
						price_kind = "unknown";
					}

					String[] items = artwork_name.split(" ");

					for (org.jsoup.nodes.Element detail : lotDetails) {
						
						String lotDetail = detail.text().replaceAll(new String("�".getBytes("UTF-8"), "UTF-8"), "")
								.replaceAll("\u00A0", " ");

						if (dimension && i == 1) {
							if (!StringUtils.isBlank(artwork_description)) {
								artwork_description = artwork_description + ", " + lotDetail;
							} else {
								artwork_description = lotDetail;
							}
							continue;
						}

						i++;
						if (lotDetail.trim().equals("")) {
							if (description && dimension) {
								i = 1;
							}
							description = true;
						} else if (artwork_name.contains(lotDetail) && StringUtils.isBlank(artist_name)) {
							artist_name = lotDetail;
						} else if (artwork_name.contains(lotDetail) && !StringUtils.isBlank(artist_name)) {
							artwork_name = lotDetail;
						} else if (lotDetail.matches("(.*)([\\d]{4})") && !description) {
							artist_birth = lotDetail.replaceAll("[^0-9]", "").trim();
						} else if (!description && i != 1) {
							boolean isExists = Arrays.stream(items).parallel().anyMatch(lotDetail::contains);
							if (!isExists) {
								artist_nationality = lotDetail;
							}
						} else if (description) {
							if (i == 1) {
								lotDetail = detail.html();
								lotDetail = lotDetail.replaceAll("<br />", ", ");
								lotDetail = lotDetail.replace("&nbsp;", "").replaceAll("�", "");
								lotDetail = lotDetail.replaceAll("<em>", "").replaceAll("</em>", "").replaceAll("\n", "");
							}

							String[] descDetails = lotDetail.split(", ");
							
							if (DIM_WORDS.matcher(lotDetail).matches() && lotDetail.contains(";") && !lotDetail.contains(", ")) {
								descDetails = lotDetail.split(";");
							}

							for (String value : descDetails) {
								value = value.trim();

								if (dimension && i == 1) {
									if (!StringUtils.isBlank(artwork_description)) {
										artwork_description = artwork_description + ", " + value;
									} else {
										artwork_description = value;
									}
									continue;
								}

								if (value.toLowerCase().contains("before") || value.toLowerCase().contains("after")
										|| value.toLowerCase().contains("circa")) {
									if (value.toLowerCase().contains("before")) {
										artwork_year_identifier = "before";
									} else if (value.toLowerCase().contains("after")) {
										artwork_year_identifier = "after";
									} else if (value.toLowerCase().contains("circa")) {
										artwork_year_identifier = "circa";
									}
								}

								if (EDITION_WORDS.matcher(value).matches()) {
									if (!"".equals(artwork_edition)) {
										artwork_edition = artwork_edition + ", " + value;
									} else {
										artwork_edition = value;
									}
								} else if ((value.toLowerCase().contains("mounted")
										|| value.toLowerCase().contains("framed") && !DIM_WORDS.matcher(value).matches())
										&& !MATERIAL_WORDS.matcher(value).matches()
										&& !MATERIAL_WORDS.matcher(value.toLowerCase()).matches()) {
									if (!"".equals(artwork_condition_in)) {
										artwork_condition_in = artwork_condition_in + ", " + value;
									} else {
										artwork_condition_in = value;
									}
								} else if (SIGN_WORDS.matcher(value).matches()
										|| SIGN_WORDS.matcher(value.toLowerCase()).matches()) {
									if (!"".equals(artwork_markings)) {
										artwork_markings = artwork_markings + ", " + value;
									} else {
										artwork_markings = value;
									}

									if (value.toLowerCase().matches(
											"(.*)(number|numbered|numerato|numerata|numerati)[\\s]*([\\d]+[\\/][\\d]+)(.*)")) {
										l_pattern = Pattern.compile(
												"(.*)(number|numbered|numerato|numerata|numerati)[\\s]*([\\d]+[\\/][\\d]+)(.*)");
										l_matcher = l_pattern.matcher(value.toLowerCase());
										l_matcher.find();
										artwork_edition = l_matcher.group(3).replace("�", "");
										artwork_edition = artwork_edition.replace("/", " of ");
										if (!"".equals(artwork_edition)) {
											artwork_edition = artwork_edition + ", " + value;
										} else {
											artwork_edition = value;
										}
									}
									if (value.matches("(.*)([\\d]{4})(.*)")) {
										if (value.contains("-")) {
											if (value.split("-")[0].matches("(.*)([\\d]{4})(.*)")) {
												artwork_start_year = value.split("-")[0];
											}
											if (value.split("-")[1].matches("(.*)([\\d]{4})(.*)")) {
												artwork_end_year = value.split("-")[1];
											}
										} else {
											artwork_start_year = value;
										}
									} else if (value.matches("(.*)([\\d]{2})(.*)")) {
										try {
											l_pattern = Pattern.compile("(.*)([\\d]{2})(.*)");
											if (value.contains("-")) {
												if (value.split("-")[0].matches("(.*)([\\d]{2})(.*)")) {
													l_matcher = l_pattern.matcher(value.split("-")[0]);
													l_matcher.find();
													artwork_start_year = "19" + l_matcher.group(2);
												}
												if (value.split("-")[1].matches("(.*)([\\d]{2})(.*)")) {
													l_matcher = l_pattern.matcher(value.split("-")[1]);
													l_matcher.find();
													artwork_end_year = "19" + l_matcher.group(2);
												}
											} else {
												l_matcher = l_pattern.matcher(value);
												l_matcher.find();
												artwork_start_year = "19" + l_matcher.group(2);
											}
										} catch (Exception e) {
										}
									}
								} else if (DIM_WORDS.matcher(value).matches()) {
									if (dimension) {
										if (!"".equals(csvBean.getArtwork_size_notes())) {
											csvBean.setArtwork_size_notes(csvBean.getArtwork_size_notes() + ", " + value);
										} else {
											csvBean.setArtwork_size_notes(value);
										}
										continue;
									}

									try {
										dimension = true;
										value = value.replaceAll("by", "x");

										l_pattern = Pattern.compile(
												"^([a-zA-Z]+)[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(cm)*)(.*)");
										l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
												.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));

										if (l_matcher.find()) {
											if (!StringUtils.isBlank(l_matcher.group(1))) {
												if (!"".equals(csvBean.getArtwork_size_notes())) {
													csvBean.setArtwork_size_notes(csvBean.getArtwork_size_notes() + ", " + l_matcher.group(1));
												} else {
													csvBean.setArtwork_size_notes(l_matcher.group(1));
												}
											}
											csvBean.setArtwork_measurements_height(l_matcher.group(4));
											csvBean.setArtwork_measurements_width(l_matcher.group(6));
											csvBean.setArtwork_measurements_depth("0");
											csvBean.setAuction_measureunit("cm");
										}

										l_matcher = Pattern.compile("\\((.*?)\\)").matcher(value);
										if (l_matcher.find()) {
											value = l_matcher.group(1);
										}

										l_pattern = Pattern.compile(
												"[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(cm)*)(.*)");
										l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
												.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));
										if (l_matcher.find()) {
											csvBean.setArtwork_measurements_height(l_matcher.group(3));
											csvBean.setArtwork_measurements_width(l_matcher.group(5));
											csvBean.setArtwork_measurements_depth(l_matcher.group(7));
											csvBean.setAuction_measureunit("cm");
										} else {
											l_pattern = Pattern.compile(
													"[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(cm)*)(.*)");
											l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
													.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));
											if (l_matcher.find()) {
												csvBean.setArtwork_measurements_height(l_matcher.group(3));
												csvBean.setArtwork_measurements_width(l_matcher.group(5));
												csvBean.setArtwork_measurements_depth("0");
												csvBean.setAuction_measureunit("cm");
											}
										}

										if (StringUtils.isBlank(csvBean.getArtwork_measurements_height())) {
											l_pattern = Pattern
													.compile("[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(cm)*)(.*)");
											l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
													.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));
											if (l_matcher.find()) {
												csvBean.setArtwork_measurements_height(l_matcher.group(5).replace("cm", "").trim());
												csvBean.setArtwork_measurements_width("0");
												csvBean.setArtwork_measurements_depth("0");
												csvBean.setAuction_measureunit("cm");
												csvBean.setArtwork_size_notes(l_matcher.group(2));
											}
										}

									} catch (Exception e) {
										System.out.println("Error getting dimension");
									}
								}

								else if (MATERIAL_WORDS.matcher(value).matches()) {
									if (!"".equals(artwork_materials)) {
										artwork_materials = artwork_materials + ", " + value;
									} else {
										artwork_materials = value;
									}
								}

								else if (value.matches("(Painted|Executed|Conceived).*([\\d]{4})-([\\d]{4}).*")
										&& value.contains("-")) {
									l_pattern = Pattern.compile("(Painted|Executed|Conceived).*([\\d]{4})-([\\d]{4}).*");
									l_matcher = l_pattern.matcher(value);
									if (l_matcher.find()) {
										artwork_start_year = l_matcher.group(2).trim();
										artwork_end_year = l_matcher.group(3).trim();
										if (!StringUtils.isBlank(artwork_description)) {
											artwork_description = artwork_description + ", " + value;
										} else {
											artwork_description = value;
										}
									}
								}

								else if (value.matches("(Painted|Executed|Conceived).*([\\d]{4}).*")) {
									l_pattern = Pattern.compile("(Painted|Executed|Conceived).*([\\d]{4}).*");
									l_matcher = l_pattern.matcher(value);
									if (l_matcher.find()) {
										artwork_start_year = l_matcher.group(2).trim();
										if (!StringUtils.isBlank(artwork_description)) {
											artwork_description = artwork_description + ", " + value;
										} else {
											artwork_description = value;
										}
									}
								}

								else if (value.matches("(.*)([\\d]{4})")) {
									artwork_start_year = value;
								}

							}
						}
					}

					if (artwork_name.contains("|")) {
						artist_name = artwork_name.split("\\|")[0].trim();
						artwork_name = artwork_name.split("\\|")[1].trim();
					}

					for (int k = 0; k < catalogue.size(); k++) {
						String lotCatalogue = catalogue.get(k).text();
						if (lotCatalogue.contains("Provenance")) {
							artwork_provenance = catalogueDetails.get(k).html().trim().replaceAll("</p>", "")
									.replaceAll("<p>", "").replaceAll("\n", "; ");
						} else if (lotCatalogue.contains("Exhibition") || lotCatalogue.contains("Exhibited")) {
							artwork_exhibited = catalogueDetails.get(k).html().trim().replaceAll("</p>", "")
									.replaceAll("<p>", "").replaceAll("\n", "; ");
						} else if (lotCatalogue.contains("Literature")) {
							artwork_literature = catalogueDetails.get(k).html().trim().replaceAll("</p>", "")
									.replaceAll("<p>", "").replaceAll("\n", "; ");
						}
					}

					if (StringUtils.isBlank(artwork_start_year) && artwork_name.matches("(.*)([\\d]{4})(.*)")) {
						artwork_start_year = artwork_name;
					}

					if (!StringUtils.isBlank(artwork_start_year)) {
						if (artwork_start_year.matches("(.*)([\\d]{4})(.*)")) {
							try {
								l_pattern = Pattern.compile("(.*)([\\d]{4})(.*)");
								l_matcher = l_pattern.matcher(artwork_start_year);
								l_matcher.find();
								artwork_start_year = l_matcher.group(2);
								if (!StringUtils.isBlank(artwork_end_year)) {
									l_matcher = l_pattern.matcher(artwork_end_year);
									l_matcher.find();
									artwork_end_year = l_matcher.group(2);
								}
							} catch (Exception e) {
							}
						}
					}

					if (artist_birth.length() == 8) {
						artist_death = artist_birth.substring(4);
						artist_birth = artist_birth.substring(0, 4);
					}

					if (!StringUtils.isBlank(artwork_materials)) {
						artwork_materials = artwork_materials.replaceAll("'", "\\\\'");
						artwork_category = FineartUtils.getMaterialCategory(getSqlSafeString(artwork_materials), jdbcTemplate);
					}
					
					if((StringUtils.isEmpty(csvBean.getArtwork_size_notes()) || csvBean.getArtwork_size_notes().equals("null")) && StringUtils.isNotEmpty(artwork_markings)) {
						String[] markingLines = artwork_markings.split("<br>");
						if(markingLines.length >= 3) {
							artwork_markings = markingLines[0];
							artwork_materials = markingLines[1];
							artwork_category = FineartUtils.getMaterialCategory(getSqlSafeString(artwork_materials), jdbcTemplate);
							processDimesions(markingLines[2], csvBean);
						} else {
							for(String markingLine : markingLines) {
								processDimesions(markingLine, csvBean);
							}
						}
					} else if(StringUtils.isEmpty(csvBean.getArtwork_size_notes()) || csvBean.getArtwork_size_notes().equals("null") && StringUtils.isNotEmpty(artwork_edition)) {
						String[] editionLines = artwork_edition.split("<br>");
						if(editionLines.length >= 3) {
							artwork_markings = editionLines[0];
							artwork_materials = editionLines[1];
							artwork_category = FineartUtils.getMaterialCategory(getSqlSafeString(artwork_materials), jdbcTemplate);
							processDimesions(editionLines[2], csvBean);
						} else {
							for(String editionLine : editionLines) {
								processDimesions(editionLine, csvBean);
							}
						}
					}
				}
				
				if(!isOldStyleLot) {
					org.jsoup.select.Elements priceMin = doc.select("p[class=css-1g8ar3q]");
					org.jsoup.select.Elements lotdetails = doc.select("div[class=css-1ewow1l]");

					lot_num = doc.select("span[class=css-16v44d6]").text();
					artwork_name = doc.select("h1[class=css-1ikrrc9]").text(); // updated by Vishwas
					price_sold = doc.select("span[class=css-15o7tlo]").text().replaceAll("[^0-9]", "").trim();
					catalogue = doc.select("div[id=LotCatalogueing]").select("h3");
					catalogueDetails = doc.select("div[id=LotCatalogueing]").select("div[class=css-1ewow1l]");
					
					if (priceMin.size() > 0) {
						price_estimate_min = priceMin.first().text().trim();
					}
					if (lotdetails.size() > 0) {
						lotDetails = lotdetails.first().select("p");
					}
					
					System.out.println("Parsing new style lot number:" + lot_num);
					
					if (price_estimate_min.contains("-")) {
						price_estimate_max = price_estimate_min.split("-")[1].replaceAll("[^0-9]", "").trim();
						price_estimate_min = price_estimate_min.split("-")[0].replaceAll("[^0-9]", "").trim();
					}

					if (!StringUtils.isBlank(price_sold)) {
						price_kind = "sold price";
					} else if (!StringUtils.isBlank(price_estimate_max) || !StringUtils.isBlank(price_estimate_min)) {
						price_kind = "estimate";
					} else {
						price_kind = "unknown";
					}

					for (org.jsoup.nodes.Element detail : lotDetails) {
						
						String lotDetailsCurrentLine = detail.text().replaceAll(new String("�".getBytes("UTF-8"), "UTF-8"), "").replaceAll("\u00A0", " ");

						if (dimension && i == 1) {
							if (!StringUtils.isBlank(artwork_description)) {
								artwork_description = artwork_description + ", " + lotDetailsCurrentLine;
							} else {
								artwork_description = lotDetailsCurrentLine;
							}
							continue;
						}

						i++;
						if (lotDetailsCurrentLine.trim().equals("")) {
							if (description && dimension) {
								i = 1;
							}
							description = true;
						} else if (artwork_name.contains(lotDetailsCurrentLine) && StringUtils.isBlank(artist_name)) {
							artist_name = lotDetailsCurrentLine;
						} else if (artwork_name.contains(lotDetailsCurrentLine) && !StringUtils.isBlank(artist_name)) {
							artwork_name = lotDetailsCurrentLine;
						} else if (lotDetailsCurrentLine.matches("(.*)([\\d]{4})") && !description) {
							artist_birth = lotDetailsCurrentLine.replaceAll("[^0-9]", "").trim();
						} else if (!description && i != 1) {
							String[] items = artwork_name.split(" ");
							boolean isExists = Arrays.stream(items).parallel().anyMatch(lotDetailsCurrentLine::contains);
							if (!isExists) {
								artist_nationality = lotDetailsCurrentLine;
							}
						} else if (description) {
							if (i == 1) {
								lotDetailsCurrentLine = detail.html();
								lotDetailsCurrentLine = lotDetailsCurrentLine.replaceAll("<br />", ", ");
								lotDetailsCurrentLine = lotDetailsCurrentLine.replace("&nbsp;", "").replaceAll("�", "");
								lotDetailsCurrentLine = lotDetailsCurrentLine.replaceAll("<em>", "").replaceAll("</em>", "").replaceAll("\n", "");
							}

							lotDetailsCurrentLine = lotDetailsCurrentLine.trim();

							if (dimension && i == 1) {
								if (!StringUtils.isBlank(artwork_description)) {
									artwork_description = artwork_description + ", " + lotDetailsCurrentLine;
								} else {
									artwork_description = lotDetailsCurrentLine;
								}
								continue;
							}

							if (lotDetailsCurrentLine.toLowerCase().contains("before") || lotDetailsCurrentLine.toLowerCase().contains("after") 
									|| lotDetailsCurrentLine.toLowerCase().contains("circa")) {
								if (lotDetailsCurrentLine.toLowerCase().contains("before")) {
									artwork_year_identifier = "before";
								} else if (lotDetailsCurrentLine.toLowerCase().contains("after")) {
									artwork_year_identifier = "after";
								} else if (lotDetailsCurrentLine.toLowerCase().contains("circa")) {
									artwork_year_identifier = "circa";
								}
							}

							if (EDITION_WORDS.matcher(lotDetailsCurrentLine).matches()) {
								if (!"".equals(artwork_edition)) {
									artwork_edition = artwork_edition + ", " + lotDetailsCurrentLine;
								} else {
									artwork_edition = lotDetailsCurrentLine;
								}
							} else if ((lotDetailsCurrentLine.toLowerCase().contains("mounted")
									|| lotDetailsCurrentLine.toLowerCase().contains("framed") && !DIM_WORDS.matcher(lotDetailsCurrentLine).matches())
									&& !MATERIAL_WORDS.matcher(lotDetailsCurrentLine).matches()
									&& !MATERIAL_WORDS.matcher(lotDetailsCurrentLine.toLowerCase()).matches()) {
								if (!"".equals(artwork_condition_in)) {
									artwork_condition_in = artwork_condition_in + ", " + lotDetailsCurrentLine;
								} else {
									artwork_condition_in = lotDetailsCurrentLine;
								}
							} else if (SIGN_WORDS.matcher(lotDetailsCurrentLine).matches()
									|| SIGN_WORDS.matcher(lotDetailsCurrentLine.toLowerCase()).matches()) {
								if (!"".equals(artwork_markings)) {
									artwork_markings = artwork_markings + ", " + lotDetailsCurrentLine;
								} else {
									artwork_markings = lotDetailsCurrentLine;
								}

								if (lotDetailsCurrentLine.toLowerCase().matches(
										"(.*)(number|numbered|numerato|numerata|numerati)[\\s]*([\\d]+[\\/][\\d]+)(.*)")) {
									l_pattern = Pattern.compile(
											"(.*)(number|numbered|numerato|numerata|numerati)[\\s]*([\\d]+[\\/][\\d]+)(.*)");
									l_matcher = l_pattern.matcher(lotDetailsCurrentLine.toLowerCase());
									l_matcher.find();
									artwork_edition = l_matcher.group(3).replace("�", "");
									artwork_edition = artwork_edition.replace("/", " of ");
									if (!"".equals(artwork_edition)) {
										artwork_edition = artwork_edition + ", " + lotDetailsCurrentLine;
									} else {
										artwork_edition = lotDetailsCurrentLine;
									}
								}
								if (lotDetailsCurrentLine.matches("(.*)([\\d]{4})(.*)")) {
									if (lotDetailsCurrentLine.contains("-")) {
										if (lotDetailsCurrentLine.split("-")[0].matches("(.*)([\\d]{4})(.*)")) {
											artwork_start_year = lotDetailsCurrentLine.split("-")[0];
										}
										if (lotDetailsCurrentLine.split("-")[1].matches("(.*)([\\d]{4})(.*)")) {
											artwork_end_year = lotDetailsCurrentLine.split("-")[1];
										}
									} else {
										artwork_start_year = lotDetailsCurrentLine;
									}
								} else if (lotDetailsCurrentLine.matches("(.*)([\\d]{2})(.*)")) {
									try {
										l_pattern = Pattern.compile("(.*)([\\d]{2})(.*)");
										if (lotDetailsCurrentLine.contains("-")) {
											if (lotDetailsCurrentLine.split("-")[0].matches("(.*)([\\d]{2})(.*)")) {
												l_matcher = l_pattern.matcher(lotDetailsCurrentLine.split("-")[0]);
												l_matcher.find();
												artwork_start_year = "19" + l_matcher.group(2);
											}
											if (lotDetailsCurrentLine.split("-")[1].matches("(.*)([\\d]{2})(.*)")) {
												l_matcher = l_pattern.matcher(lotDetailsCurrentLine.split("-")[1]);
												l_matcher.find();
												artwork_end_year = "19" + l_matcher.group(2);
											}
										} else {
											l_matcher = l_pattern.matcher(lotDetailsCurrentLine);
											l_matcher.find();
											artwork_start_year = "19" + l_matcher.group(2);
										}
									} catch (Exception e) {
									}
								}
							} else if (DIM_WORDS.matcher(lotDetailsCurrentLine).matches()) { //dimesion processing
								processDimesions(lotDetailsCurrentLine, csvBean);
							} else if (MATERIAL_WORDS.matcher(lotDetailsCurrentLine).matches()) {
								if (!"".equals(artwork_materials)) {
									artwork_materials = artwork_materials + ", " + lotDetailsCurrentLine;
								} else {
									artwork_materials = lotDetailsCurrentLine;
								}
							} else if (lotDetailsCurrentLine.matches("(Painted|Executed|Conceived).*([\\d]{4})-([\\d]{4}).*")
									&& lotDetailsCurrentLine.contains("-")) {
								l_pattern = Pattern.compile("(Painted|Executed|Conceived).*([\\d]{4})-([\\d]{4}).*");
								l_matcher = l_pattern.matcher(lotDetailsCurrentLine);
								if (l_matcher.find()) {
									artwork_start_year = l_matcher.group(2).trim();
									artwork_end_year = l_matcher.group(3).trim();
									if (!StringUtils.isBlank(artwork_description)) {
										artwork_description = artwork_description + ", " + lotDetailsCurrentLine;
									} else {
										artwork_description = lotDetailsCurrentLine;
									}
								}
							} else if (lotDetailsCurrentLine.matches("(Painted|Executed|Conceived).*([\\d]{4}).*")) {
								l_pattern = Pattern.compile("(Painted|Executed|Conceived).*([\\d]{4}).*");
								l_matcher = l_pattern.matcher(lotDetailsCurrentLine);
								if (l_matcher.find()) {
									artwork_start_year = l_matcher.group(2).trim();
									if (!StringUtils.isBlank(artwork_description)) {
										artwork_description = artwork_description + ", " + lotDetailsCurrentLine;
									} else {
										artwork_description = lotDetailsCurrentLine;
									}
								}
							} else if (lotDetailsCurrentLine.matches("(.*)([\\d]{4})")) {
								artwork_start_year = lotDetailsCurrentLine;
							}
						
						}
					}

					if (artwork_name.contains("|")) {
						artist_name = artwork_name.split("\\|")[0].trim();
						artwork_name = artwork_name.split("\\|")[1].trim();
					}

					for (int k = 0; k < catalogue.size(); k++) {
						String lotCatalogue = catalogue.get(k).text();
						if (lotCatalogue.contains("Provenance")) {
							artwork_provenance = catalogueDetails.get(k).html().trim().replaceAll("</p>", "")
									.replaceAll("<p>", "").replaceAll("\n", "; ");
						} else if (lotCatalogue.contains("Exhibition") || lotCatalogue.contains("Exhibited")) {
							artwork_exhibited = catalogueDetails.get(k).html().trim().replaceAll("</p>", "")
									.replaceAll("<p>", "").replaceAll("\n", "; ");
						} else if (lotCatalogue.contains("Literature")) {
							artwork_literature = catalogueDetails.get(k).html().trim().replaceAll("</p>", "")
									.replaceAll("<p>", "").replaceAll("\n", "; ");
						}
					}

					if (StringUtils.isBlank(artwork_start_year) && artwork_name.matches("(.*)([\\d]{4})(.*)")) {
						artwork_start_year = artwork_name;
					}

					if (!StringUtils.isBlank(artwork_start_year)) {
						if (artwork_start_year.matches("(.*)([\\d]{4})(.*)")) {
							try {
								l_pattern = Pattern.compile("(.*)([\\d]{4})(.*)");
								l_matcher = l_pattern.matcher(artwork_start_year);
								l_matcher.find();
								artwork_start_year = l_matcher.group(2);
								if (!StringUtils.isBlank(artwork_end_year)) {
									l_matcher = l_pattern.matcher(artwork_end_year);
									l_matcher.find();
									artwork_end_year = l_matcher.group(2);
								}
							} catch (Exception e) {
							}
						}
					}

					if (artist_birth.length() == 8) {
						artist_death = artist_birth.substring(4);
						artist_birth = artist_birth.substring(0, 4);
					}

					if (!StringUtils.isBlank(artwork_materials)) {
						artwork_materials = artwork_materials.replaceAll("'", "\\\\'");
						String[] materialArray = artwork_materials.split(",");
						artwork_category = FineartUtils.getMaterialCategory(getSqlSafeString(materialArray[0]), jdbcTemplate);
					}
				}
				
			} catch (Exception e) {
				System.out.println(e);
			}
			
			csvBean.setLot_num(lot_num);
			csvBean.setPrice_kind(price_kind);
			csvBean.setPrice_estimate_min(price_estimate_min);
			csvBean.setPrice_estimate_max(price_estimate_max);
			csvBean.setPrice_sold(price_sold);
			csvBean.setArtist_name(artist_name);
			csvBean.setArtist_birth(artist_birth);
			csvBean.setArtist_death(artist_death);
			csvBean.setArtist_nationality(artist_nationality);
			csvBean.setArtwork_name(artwork_name);
			csvBean.setArtwork_year_identifier(artwork_year_identifier);
			csvBean.setArtwork_start_year(artwork_start_year);
			csvBean.setArtwork_end_year(artwork_end_year);
			csvBean.setArtwork_materials(artwork_materials);
			csvBean.setArtwork_markings(artwork_markings);
			csvBean.setArtwork_edition(artwork_edition);
			csvBean.setArtwork_description(artwork_description);
			csvBean.setArtwork_condition_in(artwork_condition_in);
			csvBean.setArtwork_provenance(artwork_provenance);
			csvBean.setArtwork_exhibited(artwork_exhibited);
			csvBean.setArtwork_literature(artwork_literature);
			csvBean.setArtwork_category(artwork_category);
			csvBean.setAuction_house_name("Sotheby's");
			csvBean.setLot_origin_url(lotLink);
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void processDimesions(String lotDetailsCurrentLine, CsvBean csvBean) {
		//System.out.println(lotDetailsCurrentLine);
		lotDetailsCurrentLine = inchToNormalNumberConversion(lotDetailsCurrentLine);
		if(csvBean.getArtwork_size_notes() != null) {
			csvBean.setArtwork_size_notes(csvBean.getArtwork_size_notes() + "\n" + lotDetailsCurrentLine);
		} else {
			csvBean.setArtwork_size_notes(lotDetailsCurrentLine);
			
			int mmIndex = lotDetailsCurrentLine.indexOf("mm") == -1 ? 1000 : lotDetailsCurrentLine.indexOf("mm");
			int cmIndex = lotDetailsCurrentLine.indexOf("cm") == -1 ? 1000 : lotDetailsCurrentLine.indexOf("cm");
			int inIndex = lotDetailsCurrentLine.indexOf("in.") == -1 ? 1000 : lotDetailsCurrentLine.indexOf("in.");
			int inchesIndex = lotDetailsCurrentLine.indexOf("inches") == -1 ? 1000 : lotDetailsCurrentLine.indexOf("inches");
			
			int lowestIndex = Math.min(Math.min(Math.min(mmIndex, cmIndex), inIndex), inchesIndex);
			
			if(lowestIndex == mmIndex) {
				csvBean.setAuction_measureunit("mm");
			} else if(lowestIndex == cmIndex) {
				csvBean.setAuction_measureunit("cm");
			} else if(lowestIndex == inIndex) {
				csvBean.setAuction_measureunit("in");
			} else if(lowestIndex == inchesIndex) {
				csvBean.setAuction_measureunit("in");
			}
			
			String dimesionStrWithNote = lotDetailsCurrentLine.substring(0, lowestIndex);
			Matcher matcher = Pattern.compile("\\d+(\\.\\d+)?").matcher(dimesionStrWithNote.replaceAll(",", ".")); //find any number in the string with or without a dot (.)
			if(matcher.find()) {
				double height = Double.valueOf(matcher.group());
				csvBean.setArtwork_measurements_height(String.valueOf(height));
				if(matcher.find()) {
					double width = Double.valueOf(matcher.group());
					csvBean.setArtwork_measurements_width(String.valueOf(width));
					if(matcher.find()) {
						double depth = Double.valueOf(matcher.group());
						csvBean.setArtwork_measurements_depth(String.valueOf(depth));
					}
				}
			}
		}
		System.out.println(csvBean.getArtwork_size_notes());
		System.out.println("Height: " + csvBean.getArtwork_measurements_height() + " Width: " + csvBean.getArtwork_measurements_width() 
		+ " Depth: " + csvBean.getArtwork_measurements_depth() + " " + csvBean.getAuction_measureunit());
	}
	
	public String inchToNormalNumberConversion(String dim) {
	    dim =  dim.replace(" ¾", " 3/4").replace(" ½", " 1/2").replace(" ?", " 1/5").replace(" ?", " 2/5").replace(" ¼", " 1/4").replace(" ?", " 7/8").replace(" ?", " 3/5").replace(" ?", " 1/8");
        dim = dim.replaceAll(" 1/2", ".50");
        dim = dim.replaceAll(" 1/2/", ".50");
        dim = dim.replaceAll(" 1/4", ".25");
        dim=  dim.replaceAll("½",".5");
        dim = dim.replaceAll(" 3/4", ".75");
        dim = dim.replaceAll(" 1/8", ".125");
        dim = dim.replaceAll(" 3/8", ".375");
        dim = dim.replaceAll(" 5/8", ".625");
        dim = dim.replaceAll(" 7/8", ".875");
        dim = dim.replaceAll(" 1/16", ".0625");
        dim = dim.replaceAll(" 5/16", ".3125");
        dim = dim.replaceAll(" 9/16", ".5625");
        dim = dim.replaceAll(" 7/16", ".4375");
        dim = dim.replaceAll(" 11/16", ".6875");                
        dim = dim.replace(" 1/6", ".111");
        dim = dim.replace(" 2/6", ".333");
        dim = dim.replace(" 3/6", ".50");
        dim = dim.replace(" 4/6", ".666");
        dim = dim.replace(" 5/6", ".833");
        dim = dim.replace(" 1/8", ".125");
        dim = dim.replace(" 2/8", ".25");
        dim = dim.replace(" 3/8", ".375");
        dim = dim.replace(" 4/8", ".50");
        dim = dim.replace(" 5/8", ".625");
        dim = dim.replace(" 6/8", ".75");
        dim = dim.replace(" 7/8", ".875");
        dim = dim.replace(" 1/10", ".10");
        dim = dim.replace(" 2/10", ".20");
        dim = dim.replace(" 3/10", ".30");
        dim = dim.replace(" 4/10", ".40");
        dim = dim.replace(" 5/10", ".50");
        dim = dim.replace(" 6/10", ".60");
        dim = dim.replace(" 7/10", ".70");
        dim = dim.replace(" 8/10", ".80");
        dim = dim.replace(" 9/10", ".90");
        dim = dim.replace(" 1/12", ".0833");
        dim = dim.replace(" 2/12", ".166");
        dim = dim.replace(" 3/12", ".25");
        dim = dim.replace(" 4/12", ".333");
        dim = dim.replace(" 5/12", ".416");
        dim = dim.replace(" 6/12", ".50");
        dim = dim.replace(" 7/12", ".583");
        dim = dim.replace(" 8/12", ".666");
        dim = dim.replace(" 9/12", ".75");
        dim = dim.replace(" 10/12", ".0833");
        dim = dim.replace(" 11/12", ".916");
        dim = dim.replace(" 1/16", ".0625");
        dim = dim.replace(" 2/16", ".125");
        dim = dim.replace(" 3/16", ".187");
        dim = dim.replace(" 4/16", ".25");
        dim = dim.replace(" 5/16", ".313");
        dim = dim.replace(" 6/16", ".375");
        dim = dim.replace(" 7/16", ".438");
        dim = dim.replace(" 8/16", ".50");
        dim = dim.replace(" 9/16", ".563");
        dim = dim.replace(" 10/16", ".625");
        dim = dim.replace(" 11/16", ".687");
        dim = dim.replace(" 12/16", ".75");
        dim = dim.replace(" 13/16", ".813");
        dim = dim.replace(" 14/16", ".875");
        dim = dim.replace(" 15/16", ".937");
        dim = dim.replace(" 1/1 2", ".0833");
        dim = dim.replace("1 14", "");
        dim = dim.replace(" 1/3", ".34");
        dim = dim.replace(" 1/ 2", ".50");
        dim = dim.replace(" 2/4", ".50");
        dim = dim.replace(" 3/4", ".75");
        dim = dim.replace(" 1/5", ".20");
        dim = dim.replace(" 2/5", ".40");
        dim = dim.replace(" 3/5", ".60");
        return dim;
    }

	public void processImages(UserAgent userAgent3, CsvBean csvBean, boolean isOldStyleLot) throws NotFound {
		if(!isOldStyleLot) {
			int imageCounter = 1;
			Elements imageElements = userAgent3.doc.findEvery("<div class='css-1i3n52v'>");
			for(Element imageElement : imageElements) {
				String imageSrc = imageElement.findFirst("<img>").getAtString("srcSet");
				imageSrc = imageSrc.substring(0, imageSrc.indexOf(".jpg")) + ".jpg";
				if(imageCounter == 1) {
					csvBean.setArtwork_images1(imageSrc);
				} else if(imageCounter == 2) {
					csvBean.setArtwork_images2(imageSrc);
				} else if(imageCounter == 3) {
					csvBean.setArtwork_images3(imageSrc);
				} else if(imageCounter == 4) {
					csvBean.setArtwork_images4(imageSrc);
				} else if(imageCounter == 5) {
					csvBean.setArtwork_images5(imageSrc);
				}
				imageCounter++;
			}
		}
		if(isOldStyleLot) {
			Elements imageElements = userAgent3.doc.findEvery("<div id='lotDetail-carousel' class='slider'>").findEvery("<img>");
			int imageCounter = 1;
			for(Element imageElement : imageElements) {
				String imageSrc = imageElement.getAtString("src");
				imageSrc = imageSrc.substring(0, imageSrc.indexOf(".jpg")) + ".jpg";
				if(imageCounter == 1) {
					csvBean.setArtwork_images1(imageSrc);
				} else if(imageCounter == 2) {
					csvBean.setArtwork_images2(imageSrc);
				} else if(imageCounter == 3) {
					csvBean.setArtwork_images3(imageSrc);
				} else if(imageCounter == 4) {
					csvBean.setArtwork_images4(imageSrc);
				} else if(imageCounter == 5) {
					csvBean.setArtwork_images5(imageSrc);
				}
				imageCounter++;
			}
		}
		if(StringUtils.isEmpty(csvBean.getArtwork_images1()) && StringUtils.isEmpty(csvBean.getArtwork_images2())) {
			Elements imageElements = userAgent3.doc.findEvery("<div class='css-10remba'>");
			int imageCounter = 1;
			for(Element imageElement : imageElements) {
				String imageSrc = imageElement.findFirst("<img>").getAtString("srcset");
				imageSrc = imageSrc.substring(0, imageSrc.indexOf(".jpg")) + ".jpg";
				if(imageCounter == 1) {
					csvBean.setArtwork_images1(imageSrc);
				} else if(imageCounter == 2) {
					csvBean.setArtwork_images2(imageSrc);
				} else if(imageCounter == 3) {
					csvBean.setArtwork_images3(imageSrc);
				} else if(imageCounter == 4) {
					csvBean.setArtwork_images4(imageSrc);
				} else if(imageCounter == 5) {
					csvBean.setArtwork_images5(imageSrc);
				}
				imageCounter++;
			}
		}
		if(StringUtils.isEmpty(csvBean.getArtwork_images1()) && StringUtils.isEmpty(csvBean.getArtwork_images2())) {
			Elements imageElements = userAgent3.doc.findEvery("<div id='main-image-container' class='lotdetail-image zoom-hover-trigger'>");
			int imageCounter = 1;
			for(Element imageElement : imageElements) {
				String imageSrc = imageElement.findFirst("<img>").getAtString("src");
				imageSrc = imageSrc.substring(0, imageSrc.indexOf(".jpg")) + ".jpg";
				if(imageCounter == 1) {
					csvBean.setArtwork_images1(imageSrc);
				} else if(imageCounter == 2) {
					csvBean.setArtwork_images2(imageSrc);
				} else if(imageCounter == 3) {
					csvBean.setArtwork_images3(imageSrc);
				} else if(imageCounter == 4) {
					csvBean.setArtwork_images4(imageSrc);
				} else if(imageCounter == 5) {
					csvBean.setArtwork_images5(imageSrc);
				}
				imageCounter++;
			}
		}
	}

	public void populateAuctionDetails(Element auctionListElement, AuctionBean auctionBean) throws NotFound, ParseException {
		String auctionTitle = auctionListElement.findFirst("<div class='Card-title' style=''>").getTextContent();
		auctionBean.setFaac_auction_title(auctionTitle);
		String auctionDetails = auctionListElement.findFirst("<div class='Card-details' style=''>").getTextContent();
		String[] auctionDetailsArray = auctionDetails.split("\\|");
		String auctionDate = auctionDetailsArray[0];
		String auctionStartDate = "";
		String auctionEndDate = "";
		String auctionLocation = auctionDetailsArray[2];
		auctionBean.setCah_auction_house_location(auctionLocation);
		if(auctionDate.contains("–")) {
			String[] auctionDateArray = auctionDate.split("–");
			if(auctionDateArray[0].length() > 2) {
				auctionStartDate = auctionDate.substring(0, auctionDate.indexOf("–")) + auctionDate.substring(auctionDate.trim().lastIndexOf(" "), auctionDate.length() -1);
				auctionEndDate = auctionDate.substring(auctionDate.indexOf("–") + 1, auctionDate.length() -1);
			} else {
				auctionStartDate = auctionDate.substring(0, auctionDate.indexOf("–")) + auctionDate.substring(auctionDate.indexOf(" "), auctionDate.length() -1);
				auctionEndDate = auctionDate.substring(auctionDate.indexOf("–") + 1, auctionDate.length() -1);
			}
			
		} else {
			auctionStartDate = auctionDate;
		}
		SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy");
		Date d1 = f.parse(auctionStartDate);
		auctionStartDate = new SimpleDateFormat("dd-MMM-yyyy").format(d1);
		if(StringUtils.isNotEmpty(auctionEndDate)) {
			Date d2 = f.parse(auctionEndDate);
			auctionEndDate = new SimpleDateFormat("dd-MMM-yyyy").format(d2);
		}
		auctionBean.setFaac_auction_start_date(auctionStartDate);
		auctionBean.setFaac_auction_end_date(auctionEndDate);
		
		System.out.println("Start Date: " + auctionStartDate + " End Date: " + auctionEndDate);
	}
	
	private String getSqlSafeString(String input) {
		return input.replaceAll("'", "\\\\'");
	}
}
