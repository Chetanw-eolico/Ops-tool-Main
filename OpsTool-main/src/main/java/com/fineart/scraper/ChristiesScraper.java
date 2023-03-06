package com.fineart.scraper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;

import com.core.fineart.FineartUtils;
import com.core.windows.BaseWindow;
import com.jaunt.UserAgent;

import au.com.bytecode.opencsv.CSVWriter;  

public class ChristiesScraper extends BaseWindow implements ScraperInterface {

	private static final Pattern MATERIAL_WORDS = Pattern.compile(
			"((engravings|Vintage|Paire|watercolour|Each: steel|yarn|telone|ferro|lamiera|multigum|tecnica|alluminio|Série|scultura|Silber|Encre|tavola|acrilico|engraved|lithographed|woodengraved|woodcut|gouche|Weißgold|panelling|decorated|Épreuve|coat of arms|Crayon|Fusain|imprimés|Rosegold 585|Crayons|Crayons|frosted|farbloses Glas|Platin 950|Schale|toile|Panneau|bent|beechwood|stained bent beechwood|backrest|stained|bentwood|cane|woven|Taschenuhr|Messingguss|Keramik|solid|mirror|foam|horns|rods|tar|knife|oil assemblage|Metal|monotype printed in colours|on japan|original canvas|on cream laid|each oil on canvas board|oil and canvas|on heavy wove|on laid|on thin wove|on arches|on BFK Rives|on TH Saunders|on Van Gelder laid|on van Gelder Zonen|on Somerset satin|on glossy wove|engraving|dark|on wove|dustgrain gravure and aquatint printed in colours|plastic foam lined case|oil on masonite|pencil and fabric collage|oil on hemp cloth|drypoint with hand coloring|sliced violin and cast bronze violin inside a violin case in a Plexiglas case|MIS and Lysonic inkjet prints on Thai mulberry paper with wax|Albâtre|PANNEAU|Carton|paint|spray-paint|quatrefoil|woodcut|beide|brass|photograpy|crayon|chalk|gouache|offsetlithograph|graphite|ink|leather|crayon|linocut|polished|mixed media|monoprint|digital|pastel|pen|print|plaster|tempera|pressed|vinyl| powder puff, spray pigment,|walnut|fiberglass|waterprint|digital pigment print|pienzo pigment prints|Batik|Olio|Pastello|Bubinga|Tre oli|Hershey bar|Acquerello|silk|color|decommissioned|ballpoint pen|rubber|glitter|wood|verde|pigment|c-print|plastiline|synthetic|rustoleum enamel|porcelain|jigsaw|neon lights mounted|spackle and latex on linoleum tiles|black soap|Dimensions|guitar amplifier|oil on|oil|pencil and mixed media on linen|Mahagoni|Casein|Öl auf|Pastel|photograpy|grey|oil|rosewood)([ ,].*)?" // Starts
					+ "|(.* )?(Aquarell|ivory|woods|Métal|wood|cineraria|wove paper|plastic|gesso|oil on canvas|acrylic|airbrush|albumen|ink|aluminum|bronze|ceramic|charcoal|chrome|chromogenic|cibachrome|cooper|etching|glass|glaze|gold|"
					+ "lithograph|engraving|plexiglas|Cuivre|sur|Ensemble de|ferro|walnut|tecnica|scultura|multigum|lamiera|alluminio|telone|acrilico|PANNEAU|tavola|Samtliga|Samtida|printed|Drawings|engraved|Série|paper|polished|edangered|Carton|carvings|frosted|encre|coat of arms|gouache|Albâtre|Crayons|sur|wood-engravings|printed in 5 colours|on buff paper|pencils|pencil|photogravure|sanguine|body white|wood-engraving|framing|upper corners rounded|watercolour|body colour|grey|white chalks|on laid paper|wove|chalk|sheets|ithograph|lithograh|lithographs|offsetlithograph|screen|X-ray|lithography|silkscreen|mahogany|metal|oak|on wallpaper|palladium|pattern|photograph|platinum|plywood|poster|albuminé|plomb|"
					+ "rosewood|(screen)?print|serigraph|silver|steel|teak|upholstery|watercolor|woodblock|woodcut|wool|glass|auf|bronze with wooden base|frame|Enamel|gilt|granite|hardboard|iron|volant|screenprints|wove|mauve|tissue|on japan|Pochoir|on Arches|Drypoint|aquatint|carborundum|photographic|polychrome|sissoo|polyester|handmade|and printed paper collage|wood|polymer paint|wrapper collage|on light blue translucent Plexiglas|, wax, brass, concrete,spray paint, incense|, pedal, DVD player,|encaustic and varnish|varnish|magazines|aluminium and glue|laid on |hoses|and coloured|and ink on foamcore|and enamel|antique|light box|wax crayon|wax crayon|coloured crayon|paper pulp|rhinestones|embroidery|beading|flashe|neon|UV ink|wire|linen|tin|colored|unpainted|sequins|Plexiglas|engraved|lithographed|woodengraved|woodcut|gouche|planche|particle board|screws|nails|patinated|staples|tape)([ ,\\-].*)?" // middle
					+ "|.* (on canvas|on board|on cream laid|on heavy wove|lamiera|tecnica|ferro|scultura|PANNEAU|multigum|boards|alluminio|telone|tavola|acrilico|on thin wove|stained|plinth|doors|opening|drawers|motifs|ornament|compartment|restored|patterning|panels|Série|Carton|couleurs|crayon|Crayon|encre|frosted|Crayons|paper with card support|Albâtre|on wove|stencil and screen printed paper collage on wooden skateboard deck|patina|holz|bronze|papier|leinwand|papier auf karton|on paper|on unstretched canvas|su tela|su cartone|crayons on paper|su carta|with pins|paper|and wood|brass|with hot glue|resinon linen|and iron|paper pulp|marble|resin|on paper over board|on graph paper|and painted latex|with portfolio box|on Plexiglas|with Danish oil|cibachrome print|on panel|over panel|on HMP paper|c-print|on bristol board|over panel|silhouettes|paper|puzzle| with wire, string or lace and transformer|on wood panel|and shea butter|variable|electronic equipment|on museum board|in wooden artist frame|engraved|lithographed|woodengraved|woodcut|gouche|in wooden artist|parts|patinated|toiles|on laid paper|panneaux|planches)" // Ends
					+ ").*",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern DIM_WORDS = Pattern.compile(
			"((\\d+)(.*)([a-z]|[A-Z])(in|cm|mm)+(.*)|[\\s]*([diameter: ])+[\\s*](.*)[\\s*]([cm.])+[\\s]*|(.*)[\\d]{1,}[\\s]*[\\D]{1,}(cm)(.*)|(.*)(cm)[\\s]*[\\d]{1,}[\\s]*[\\D]{1,}(.*)|"
					+ "(.*)(in.)+(.*)(cm)|(.*)(by)+(.*)(mm.|cm.)|(.*)(by)+(.*)(inches)|(.*)[\\d]+(cm)(.*)|(.*)cm[\\s][\\d]+|(.*)(\\d+)(.*)(in|cm|mm)+(.*)|dimensions(.*)cm|"
					+ "(\\d+)(.*)(in|cm|mm)+(.*)|(.*)(\\d+)(.*)(in|cm)+(.*))",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern EDITION_WORDS = Pattern.compile(".*(Edition|edition|this work|Unique|unique).*");

	private static final Pattern SIGN_WORDS = Pattern.compile(
			".*(signed|designed|silver|inscribed|stencilled|étiquette|dessin|Au revers|gauche|Inscription|Longue|favrile|dated|lettered|Stamped|label|titled|signature|"
					+ "annoted|dedicated|annotated|impression|crater-shaped|naux|noll|POMPON|LALANNE|JKM|PAUL|AG0|EBRANDT|JEAN|séquences|sign|DU|PN|sculpté|revers|émaillés|"
					+ "epruve|Despositio|dalpayrat|wallander|signature|Rückseitig|kosta|moulded|etikett|elise|bruxelles|dalmatian|wheel-engraved|monogram|éditée par|"
					+ "ink on the inside|stamped|dedicated|titleslip|titled|singed|title|embos|sed|incised|numbered|initialed|initialled|bears|dated|logelain|annotated|Consolidated|"
					+ "stretched|overlaid|impressed|inlaid|enamelled|produced|captioned|marked|carved|walnut|mark|marks|cracks|miss|cipher|label|cypher|initial|medallion|seal|decal|"
					+ "branded|embroidered|molded|left and right form|monogrammed|stenciled|Dédicacée|sign|janda|maeght|gießerstempel|gestempelt|datiert|paraphe|betitelt|krepp|berlin"
					+ "|auflage|timbre|porte|estampill|dat|num|Porte|monogram|Attribu|firmada|firm).*");

	CSVWriter csv = null;
	String filename;
	String outputPath;
	String auctionHouseName = "Christie's";
	String auctionLocation = "";
	String auctionNum = "";
	String auctionDate = "";
	String auctionName = "";
	String auctionInternalId = "";
	private JdbcTemplate jdbcTemplate;

	private static final long serialVersionUID = 1L;

	public ChristiesScraper(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void initScraping(final Date startDate, final Date endDate, final List<String> fineartKeywordFiltersList,
			final String outputPath, final boolean isPastSales) {
		this.outputPath = outputPath;
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			public String doInBackground() {
				doScrape(startDate, endDate, fineartKeywordFiltersList, isPastSales);
				return "";
			}
		};
		// execute the background thread
		worker.execute();

	}

	@Override
	public void close() {
		closeWindow();
	}

	/*
	 * Gets the auction link from date range, filters out those links that are
	 * in excluded
	 */
	public void doScrape(Date startDate, Date endDate, List<String> fineartKeywordFiltersList, boolean isPastSales) {
		String auctionUrl = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String chrisitesLink = "";
		ArrayList<String> auctionsList = new ArrayList<String>();
		if (isPastSales) {
			chrisitesLink = "https://www.christies.com/Results?month=" + startDate.getMonth() + "&year="
					+ dateFormat.format(startDate).substring(0, dateFormat.format(startDate).indexOf("-"));
		} else {
			chrisitesLink = "https://www.christies.com/calendar?startdate=" + dateFormat.format(startDate) + "&enddate="
					+ dateFormat.format(endDate);
		}

		consoleArea.append("Starting...");
		consoleArea.append("\n");
		consoleArea.setCaretPosition(consoleArea.getDocument().getLength());

		try {
			UserAgent userAgent = new UserAgent();
			userAgent.visit(chrisitesLink);
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			Document doc = Jsoup.parse(content);
			Elements auctions = doc.select("section[id=sales] li");
			String auctionNo = "";

			for (Element auction : auctions) {
				if (!continueProcessing) {
					return;
				}
				auctionUrl = auctionDate = auctionName = auctionNo = "";
				auctionUrl = auction.select("a[target=_self]").attr("href").trim();

				if (auctionUrl.contains("SaleID=")) {
					auctionNo = auctionUrl.substring(auctionUrl.indexOf("SaleID="));
					auctionNo = auctionNo.substring(auctionNo.indexOf("=") + 1, auctionNo.indexOf("&"));
				} else if (auctionUrl.contains(".aspx")) {
					auctionNo = auctionUrl.substring(0, auctionUrl.indexOf(".aspx"));
					auctionNo = auctionNo.substring(auctionNo.lastIndexOf("-") + 1);
				}

				auctionNum = auction
						.select("span[class=image-description--sale-number hidden-xs hidden-sm p--primary_large]")
						.text().trim();

				auctionDate = auction.select("h4").text().trim();

				boolean urlExists = auctionsList.contains(auctionUrl);

				if (urlExists) {
					continue;
				} else {
					auctionsList.add(auctionUrl);
				}

				auctionName = auction.select("a[target=_self]").text().trim();

				if (auctionName.contains("|")) {
					auctionName = auctionName.replaceAll("\\|", "");
				}

				boolean foundFiltered = false;
				for (CharSequence filteredKeyword : fineartKeywordFiltersList) {
					if (!continueProcessing) {
						return;
					}
					if (auctionUrl.toLowerCase().contains(filteredKeyword)) {
						System.out.println("Filtered keyword found in URL, skipping...");
						consoleArea.append("\nFiltered keyword found in URL, skipping...");
						consoleArea.append("\n");
						consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
						foundFiltered = true;
						break;
					} else if (auctionName.toLowerCase().contains(filteredKeyword)) {
						System.out.println("Filtered keyword found in Auction Name, skipping...");
						consoleArea.append("\nFiltered keyword found in Auction Name, skipping...");
						consoleArea.append("\n");
						consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
						foundFiltered = true;
						break;
					}
				}
				if (foundFiltered) {
					continue;
				}

				try {
					this.init();
					System.out.println(auctionUrl);
					consoleArea.append("\n" + auctionUrl);
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
					this.populateAuctionLots(auctionUrl, auctionNo);
				} catch (Exception e) {
					System.out.println("::" + e);
					consoleArea.append("\n" + e.getMessage());
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
				}

				try {
					if (csv != null) {
						csv.close();
					}
				} catch (IOException e) {
				}
			}

			/*consoleArea.append("\nScraping done!");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());*/
			this.setVisible(false);
			this.dispose();
			JOptionPane.showMessageDialog(null, "Scraping done!", "Christies Scraper", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			System.out.println("Exception Occured..." + e.getMessage());
			consoleArea.append("\nException Occured..." + e.getMessage() );
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			JOptionPane.showMessageDialog(null, "An exception occurred, please try again. The exception is: " + e.getMessage(), "Christies Scraper",
					JOptionPane.ERROR_MESSAGE);
		}
		return;
	}

	/*
	 * Gets the path of auction house and creates CSV file
	 *
	 */
	public void init() {
		try {
			try {
				File outputDirectory = new File(outputPath);
				if (!outputDirectory.exists()) {
					boolean success = outputDirectory.mkdirs();
					if (success) {
						System.out.println("Output Directory Created");
						consoleArea.append("\nOutput Directory Created");
						consoleArea.append("\n");
						consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
					}
				}

			} catch (Exception e) {// Catch exception if any
				e.printStackTrace();
			}

			filename = outputPath + "/" + auctionNum + "-" + auctionDate + ".csv";
			csv = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));

			StringBuilder stats = new StringBuilder();
			stats.append("auction_house_name|auction_location|auction_num|auction_start_date|auction_name|");
			stats.append("lot_num|sublot_num|price_kind|price_estimate_min|price_estimate_max|price_sold|");
			stats.append("artist_name|artist_birth|artist_death|artist_nationality|");
			stats.append("artwork_name|artwork_year_identifier|artwork_start_year|artwork_end_year|");
			stats.append("artwork_materials|artwork_category|artwork_markings|");
			stats.append("artwork_edition|artwork_description|artwork_measurements_height|artwork_measurements_width|");
			stats.append("artwork_measurements_depth|artwork_size_notes|auction_measureunit|artwork_condition_in|");
			stats.append("artwork_provenance|artwork_exhibited|artwork_literature|artwork_images1|");
			stats.append("artwork_images2|artwork_images3|artwork_images4|artwork_images5|image1_name|image2_name|image3_name|image4_name|image5_name|lot_origin_url\n");
			csv.writeNext(stats.toString().split("\\|"));
		} catch (Exception e) {
			e.printStackTrace();
			consoleArea.append("\n" + e.getMessage());
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}

	/*
	 * Gets the auction lot links to populate data
	 */

	private void populateAuctionLots(String lotListingLink, String auctionNo) {
		try {
			String auctionLink = "StrUrl=http://www.christies.com/Salelanding/index.aspx?intsaleid=" + auctionNo.trim()
					+ "&action=paging"
					+ "&PageSize=1000&GeoCountryCode=IN&LanguageID=1&IsLoadAll=1&SearchType=salebrowse&ClientGuid=";
			String content = "";
			UserAgent userAgent = new UserAgent();
			userAgent.sendPOST("http://www.christies.com/interfaces/LotFinderAPI/SearchResults.asmx/GetSaleLandingLots",
					auctionLink);
			content = userAgent.doc.innerHTML();
			userAgent.close();
			String[] lotUrls = content.split("</LotLink>");
			for (String lotUrl : lotUrls) {
				if (!continueProcessing) {
					return;
				}
				if (lotUrl.contains("<LotLink>")) {
					String loturl = lotUrl.substring(lotUrl.indexOf("<LotLink>"));
					populateLotDetails(loturl.replace("<LotLink>", "").replace("&amp;amp;", "&").trim());
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("CAN'T ADD LOTS.");
			consoleArea.append("\nCAN'T ADD LOTS.");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * It extracts the lot data from the url passed.
	 */
	private void populateLotDetails(String lotLink) {
		String lot_num = "";
		String price_kind = "";
		String price_estimate_min = "";
		String price_estimate_max = "";
		String price_sold = "";
		String artist_name = "";
		String artist_birth = "";
		String artist_death = "";
		String artist_nationality = "";
		String artwork_year_identifier = "";
		String artwork_start_year = "";
		String artwork_end_year = "";
		String artwork_name = "";
		String artwork_name_html = "";
		String artwork_materials = "";
		String artwork_final = "";
		String artwork_markings = "";
		String artwork_edition = "";
		String artwork_description = "";
		String artwork_measurements_height = "";
		String artwork_measurements_width = "";
		String artwork_measurements_depth = "";
		String artwork_size_notes = "";
		String auction_measureunit = "";
		String artwork_condition_in = "";
		String artwork_provenance = "";
		String artwork_exhibited = "";
		String artwork_literature = "";
		String artwork_image = "";
		String artwork_images[] = new String[4];
		Pattern l_pattern = null;
		Matcher l_matcher = null;
		Elements lotDetails = null;
		Elements imageDetails = null;

		try {
			Document doc = null;
			int i = 0;
			UserAgent userAgent = new UserAgent();
			userAgent.visit(lotLink);
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			doc = Jsoup.parse(content);

			try {
				if (StringUtils.isBlank(auctionLocation)) {
					auctionLocation = doc.select("p[id=main_center_0_lblSaleLocation]").text().trim();
					auctionName = doc.select("div[id=main_center_0_lblSaleTitle]").text().trim();
					auctionDate = doc.select("p[id=main_center_0_lblSaleDate]").text().trim();
				}
				lot_num = doc.select("span[id=main_center_0_lblLotNumber]").text().trim();
				artwork_provenance = doc.select("p[id=main_center_0_lblLotProvenance]").text().trim();
				artwork_exhibited = doc.select("p[id=main_center_0_lblExhibited]").text().trim();
				artwork_literature = doc.select("p[id=main_center_0_lblLiterature]").text().trim();
				price_estimate_min = doc.select("span[id=main_center_0_lblPriceEstimatedPrimary]").first().text();
				lotDetails = doc.select("span[id=main_center_0_lblLotDescription]");
				artist_name = doc.select("span[id=main_center_0_lblLotPrimaryTitle]").text();
				artist_birth = artist_name.replaceAll("[^0-9]", "").trim();
				artwork_name = doc.select("h2[id=main_center_0_lblLotSecondaryTitle]").first().text();
				artwork_name_html = doc.select("h2[id=main_center_0_lblLotSecondaryTitle]").first().html();
				artwork_image = doc.select("a[class=panzoom--link]").attr("href");
				imageDetails = doc.select("picture[data-zoom-enabled=panzoom--link]")
						.select("source[media=(min-width: 1280px)]");
			} catch (Exception e) {
				e.printStackTrace();
				consoleArea.append("\n" + e.getMessage());
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}

			try {
				for (int j = 1; j < imageDetails.size(); j++) {
					if (!continueProcessing) {
						return;
					}
					artwork_images[j - 1] = imageDetails.get(j).attr("srcset");
					if (artwork_images[j - 1].contains(",")) {
						artwork_images[j - 1] = artwork_images[j - 1].split(",")[1].trim();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				consoleArea.append("\n" + e.getMessage());
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}

			try {
				if (imageDetails.size() > 0) {
					for (int j = imageDetails.size(); j < 5; j++) {
						if (!continueProcessing) {
							return;
						}
						artwork_images[j - 1] = "";
					}
				} else {
					for (int j = imageDetails.size(); j < 4; j++) {
						if (!continueProcessing) {
							return;
						}
						artwork_images[j] = "";
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				consoleArea.append("\n" + e.getMessage());
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}

			if (price_estimate_min.contains("-")) {
				price_estimate_max = price_estimate_min.split("-")[1].replaceAll("[^0-9]", "").trim();
				price_estimate_min = price_estimate_min.split("-")[0].replaceAll("[^0-9]", "").trim();
			}

			if (!StringUtils.isBlank(price_sold)) {
				price_kind = "price realized";
			} else if (!StringUtils.isBlank(price_estimate_max) || !StringUtils.isBlank(price_estimate_min)) {
				price_kind = "estimate";
			} else {
				price_kind = "unknown";
			}

			String[] names = artwork_name_html.split(" ");
			StringBuilder regexp = new StringBuilder();
			for (String name : names) {
				if (!continueProcessing) {
					return;
				}
				regexp.append("(?=.*").append(name).append(")");
			}

			for (Element detail : lotDetails) {
				if (!continueProcessing) {
					return;
				}
				i++;
				String lotDetail = detail.html().replace("<br />", ", ")
						.replace("<span id=\"main_center_0_lblLotDescription\">", "").replace("</span>", "");
				lotDetail = lotDetail.replace("&nbsp;", "");
				lotDetail = lotDetail.replaceAll("<i>", ", ").replaceAll("</i>", "");
				lotDetail = lotDetail.replaceAll("<em>", "").replaceAll("</em>", "").replaceAll("\n", "");

				if (i == 1) {
					lotDetail = detail.html();
					lotDetail = lotDetail.replaceAll("<br />", ", ");
					lotDetail = lotDetail.replace("&nbsp;", "");
					lotDetail = lotDetail.replaceAll("<em>", "").replaceAll("</em>", "").replaceAll("\n", "");
				}

				String[] descDetails = lotDetail.split(", ");

				for (String value : descDetails) {
					if (!continueProcessing) {
						return;
					}
					value = value.trim();

					l_pattern = Pattern.compile(regexp.toString());

					if (l_pattern.matcher(value).find()) {
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
					}

					else if (value.toLowerCase().contains("framed")) {
						if (!"".equals(artwork_condition_in)) {
							artwork_condition_in = artwork_condition_in + ", " + value;
						} else {
							artwork_condition_in = value;
						}
					}

					else if (SIGN_WORDS.matcher(value).matches()) {
						if (!"".equals(artwork_markings)) {
							artwork_markings = artwork_markings + ", " + value;
						} else {
							artwork_markings = value;
						}
					}

					else if (MATERIAL_WORDS.matcher(value).matches()) {
						if (value.matches("(.*)([\\d]{4})(.*)")) {
							artwork_start_year = value;
						} else {
							if (!"".equals(artwork_materials)) {
								artwork_materials = artwork_materials + ", " + value;
							} else {
								artwork_materials = value;
							}
						}
					}

					else if (DIM_WORDS.matcher(value).matches()) {

						value = value.replaceAll("by", "x");
						l_matcher = Pattern.compile("\\((.*?)\\)").matcher(value);
						while (l_matcher.find()) {
							value = l_matcher.group(1);
						}
						if (value.contains("cm")) {
							try {
								l_pattern = Pattern.compile(
										"[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(cm)*)(.*)");
								l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
										.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));
								l_matcher.find();

								artwork_measurements_height = l_matcher.group(3);
								artwork_measurements_width = l_matcher.group(5);
								artwork_measurements_depth = "0";
								auction_measureunit = "cm";
							} catch (Exception e) {
							}
						}
					}

					else if (value.matches("(.*)([\\d]{4})(.*)")) {
						artwork_start_year = value;
					}
				}
			}

			if (!StringUtils.isBlank(artwork_start_year) && artwork_name.matches("(.*)([\\d]{4})(.*)")) {
				artwork_start_year = artwork_name;
			}

			if (!StringUtils.isBlank(artwork_start_year)) {
				if (artwork_start_year.matches("(.*)([\\d]{4})(.*)")) {
					try {
						l_pattern = Pattern.compile("(.*)([\\d]{4})(.*)");
						l_matcher = l_pattern.matcher(artwork_start_year);
						l_matcher.find();
						artwork_start_year = l_matcher.group(2);
					} catch (Exception e) {
					}
				}
			}

			if (artist_birth.length() == 8) {
				artist_death = artist_birth.substring(4);
				artist_birth = artist_birth.substring(0, 4);
			}

			if (artist_name.matches("(.*)\\((.*?)\\)")) {
				try {
					l_pattern = Pattern.compile("(.*)\\((.*?)\\)");
					l_matcher = l_pattern.matcher(artist_name);
					l_matcher.find();
					artist_name = l_matcher.group(1).trim();
					artist_nationality = l_matcher.group(2);
					if (artist_nationality.contains(",")) {
						artist_nationality = artist_nationality.split(",")[0].trim();
					}
				} catch (Exception e) {
				}
			}

			if (!StringUtils.isBlank(artwork_materials)) {
				artwork_materials = artwork_materials.replaceAll("'", "\\\\'");
				artwork_final = FineartUtils.getMaterialCategory(artwork_materials, jdbcTemplate);
			}

			String imageName = auctionName + "-" + artist_name + "-" + System.currentTimeMillis();
			imageName = imageName.replaceAll("[^\\w]","-"); //replace all special characters with hyphen
			
			String auctionDetails = auctionHouseName + "|" + auctionLocation + "|" + auctionNum + "|" + auctionDate
					+ "|" + auctionName + "|" + lot_num + "|" + "|" + price_kind + "|" + price_estimate_min + "|"
					+ price_estimate_max + "|" + price_sold + "|" + WordUtils.capitalizeFully(artist_name.toLowerCase())
					+ "|" + artist_birth + "|" + artist_death + "|" + artist_nationality + "|"
					+ WordUtils.capitalizeFully(artwork_name.toLowerCase()) + "|" + artwork_year_identifier + "|"
					+ artwork_start_year + "|" + artwork_end_year + "|" + artwork_materials + "|" + artwork_final + "|"
					+ artwork_markings + "|" + artwork_edition + "|" + artwork_description + "|"
					+ artwork_measurements_height + "|" + artwork_measurements_width + "|" + artwork_measurements_depth
					+ "|" + artwork_size_notes + "|" + auction_measureunit + "|" + artwork_condition_in + "|"
					+ artwork_provenance + "|" + artwork_literature + "|" + artwork_exhibited + "|" + artwork_image
					+ "|" + artwork_images[0] + "|" + artwork_images[1] + "|" + artwork_images[2] + "|"
					+ artwork_images[3] + "|" + imageName + "-a.jpg" + "|" + imageName + "-b.jpg" + "|" + imageName + "-c.jpg" 
							+ "|" + imageName + "-d.jpg" + "|" + imageName + "-e.jpg" + "|" + lotLink;

			String row[] = auctionDetails.split("\\|");
			csv.writeNext(row);
		} catch (Exception e) {
			e.printStackTrace();
			consoleArea.append("\n" + e);
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}

	/*private String getMaterialCategory(String artwork_materials) {
		String artwork_final = "";

		try {
			String sql = "SELECT material_name, material_category FROM fineart_materials WHERE lower(material_name) = lower(?)";  
			artwork_final = jdbcTemplate.execute(sql, new PreparedStatementCallback<String>() {  
		    @Override  
		    public String doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException { 
		    	ResultSet resultSet = null;
		        ps.setString(1, artwork_materials);
		        resultSet = ps.executeQuery(sql);  
		        if (resultSet.next()) {
					return resultSet.getString("material_category");
				} else {
					String materials[] = artwork_materials.split(",");
					for (String material : materials) {
						ps.clearParameters();
						ps.setString(1, material.trim());
						resultSet = ps.executeQuery();
						if (resultSet.next()) {
							return resultSet.getString("material_category");
						}
					}
				}
		        return "";
		    }  
		    });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return artwork_final;
	}*/

}