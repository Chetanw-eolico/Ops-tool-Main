package com.fineart.scraper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;

import com.core.PatternNotFoundException;
import com.core.fineart.FineartUtils;
import com.core.windows.BaseWindow;
import com.jaunt.UserAgent;

import au.com.bytecode.opencsv.CSVWriter;

public class SothebiesScraper extends BaseWindow implements ScraperInterface {
	
	
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
    
    @SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object p_value) {
        if(p_value == null) {
            return true;
        }

        if(p_value instanceof String) {
            return ((String)p_value).trim().length() == 0;
        }

        if(p_value instanceof Collection) {
            return ((Collection)p_value).isEmpty();
        }

        if(p_value instanceof Map) {
            return ((Map)p_value).isEmpty();
        }

        if(p_value instanceof EmptyInterface) {
            return ((EmptyInterface)p_value).isEmpty();
        }
        System.out.println("Not Supported by this function..... value is of "+p_value.getClass()+" type.");
        return false;
    }
 
    public static String removeLeadingAndTrailingSymbols(String p_input) {
        if (p_input == null || p_input.trim().length() == 0) {
            return "";
        }
        char[] l_input = p_input.toCharArray();
        int from = 0;
        int to = l_input.length;
        int l_len = to;
        while (isSymbolExists(l_input[from++], from, l_len));
        while (isSymbolExists(l_input[--to], to - 1, l_len));

        return p_input.substring(from - 1, to + 1);
    }
    
 public static boolean isSymbolExists(char p_char, int p_index, int p_len) {
        return SPECIAL_SYMBOL_SET.contains(p_char) && (p_index < p_len && p_index >= 0);
    }

  public static String inchToNormalNumberConversion(String dim) {
	  System.out.println("InsideinchToNormalNumberConversion");
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
 static double   artwork_measurements_height;
	static double artwork_measurements_width;
	static double artwork_measurements_depth;
	static String artwork_measurements_unit="";
  
  public static void parseDimensions(String str) throws PatternNotFoundException {
        // define all patterns here for "regular" conversion.
        Pattern pWithHardCm = Pattern
                .compile("[\\d]+[\\.|,]?[\\d]* (by)?(x)? [\\d]+[\\.|,]?[\\d]*( (by)?(x)? [\\d]+[\\.|,]?[\\d]*)?( )?(cm)");
        if (str.contains("inches") || str.contains("in.")) {
        	System.out.println("Inside inch");
            str = inchToNormalNumberConversion(str);
        }
        Matcher m = pWithHardCm.matcher(str);
        String dim = "";

        if (m.find()) {
            dim = str.substring(m.start(), m.end()).replaceAll("cm", "")
                    .replaceAll(",", ".").replaceAll("by", "x").trim();
        }
        if (dim.isEmpty()) {
            Pattern p = Pattern
                    .compile("[\\d]+[\\.|,]?[\\d]* (by)?(x)? [\\d]+[\\.|,]?[\\d]*( (by)?(x)? [\\d]+[\\.|,]?[\\d]*)?( )?(cm)?");
            m = p.matcher(str);
            if (m.find()) {
                dim = str.substring(m.start(), m.end()).replaceAll("cm", "")
                        .replaceAll(",", ".").replaceAll("by", "x").trim();
            }
        }
        // MILAN sales pattern
        if (dim.isEmpty()) {
            Pattern p = Pattern
                    .compile("(cm)(\\.)? [\\d]+[,]?[\\d]*(x)?[\\d]+[,]?[\\d]*((by)?(x)?[\\d]+[,]?[\\d]*)?");
            m = p.matcher(str);
            if (m.find()) {
                dim = str.substring(m.start(), m.end()).replaceAll("cm\\.", "")
                        .replaceAll("cm", "").replaceAll("by", "x").replaceAll(
                        ",", ".").trim();
            }
        }

        if ((null != dim) && (!dim.isEmpty())) { // REGULAR CASE.
        	System.out.println("insidedim");
            double divider = 1f;
            if (str.contains("cm")) {
                divider = 1f;
            } //            else if (str.contains("mm")) {
            //                divider = 10f;
            //            } 
            else if (str.contains("inches") || str.contains("in.")) { // cm dims are outside of ()
                // divider = 2.54f;
                divider = 0.393700787f;
            }
            StringTokenizer stDim = new StringTokenizer(dim, "x");
            if (stDim.countTokens() == 3) {
            	System.out.println("inside if");
            	artwork_measurements_height=     Double.parseDouble(stDim.nextToken().trim()) / divider;
                        artwork_measurements_width=    Double.parseDouble(stDim.nextToken().trim()) / divider;
                      //  artwork_measurements_depth=  Double.parseDouble(stDim.nextToken().trim()) / divider;
                        artwork_measurements_unit ="cm";
            } else {
            	System.out.println("inside else");
            	
            	artwork_measurements_height=     Double.parseDouble(stDim.nextToken().trim());
            			artwork_measurements_width=    Double.parseDouble(stDim.nextToken().trim());
            		//			 artwork_measurements_depth=  Double.parseDouble(stDim.nextToken().trim());
               artwork_measurements_unit ="cm";
            }
        } //  changes for second issue
        else if (str.matches("\\D+(\\d+)(.*)(in|mm)+(.*)")) {
        	System.out.println("finalelseif");
            Pattern p2 = Pattern.compile("\\D+(\\d+)(.*)(in|mm)+(.*)");
            Matcher m2 = p2.matcher(str);
            if (m2.find()) {
            	artwork_measurements_height=
                        Double.parseDouble(m2.group(1).trim());

            }
            if (str.contains("x") && str.contains("mm")) {
                commonparseDimension(str);

            }
        } else {
        	System.out.println("finalelse");
            Pattern p2 = Pattern.compile("\\d(\\d)+?((\\.)?(\\d)+?)?( )?cm");
            Matcher m2 = p2.matcher(str);
            if (m2.find()) {
            	artwork_measurements_height=
                        Double.parseDouble(m2.group().replaceAll("cm", "")
                        .trim());
            }
           if (str.toLowerCase().contains("square")) {
        	   artwork_measurements_width=artwork_measurements_height;
            }
       }
    }
  
  
  private String strinetrailling(String strind) {
		
		if(strind.startsWith(",") || strind.startsWith(".") || strind.startsWith(";"))
		{
			strind = strind.substring(1,strind.length());
		}
		
		if(strind.endsWith(",") || strind.endsWith(".") || strind.endsWith(";"))
		{
			strind = strind.substring(0,strind.length()-1);
		}
	
		return strind.trim();
				
		
	}
  
  public static void commonparseDimension(String p_token) throws PatternNotFoundException {
        System.out.println("*******************************************p_token::"+p_token); 

//        p_token=p_token.replace(",", ".").replace("by", "x").replaceAll("<span(.*)","").replace("x", "x").replace("cm", "cm");

        p_token=p_token.replace(",", ".").replace("by", "x").replace("×", "x")
                    .replace("and", "x").replace("cm x width", "x").replaceAll("�","").trim();
        p_token =   removeLeadingAndTrailingSymbols(p_token);
        if(isEmpty(p_token)) {
            System.out.println("Nothing to parse or set");
            return;
        }
        
       
        String str="";
         if(p_token.matches("((.*)(mm)+)((.*)(in)+)(.*)")){
                    Pattern l_pattern2   =   null;
                    Matcher l_matcher   =   null;
                   
                    l_pattern2           =   Pattern.compile("((.*)(mm)+)((.*)(in)+)(.*)");
                    l_matcher           =   l_pattern2.matcher(p_token);
                    if(l_matcher.find()){
                        str                 =   l_matcher.group(1);
                    }
                    if(str.matches("(.*)([\\d]{3})(.*)")){
                    Pattern l_pattern1   =   null;
                    Matcher l_matcher1   =   null;
                    l_pattern1           =   Pattern.compile("(.*)([\\d]{3})(.*)");
                    l_matcher1           =   l_pattern1.matcher(str);
                    if(l_matcher1.find()){
                        String str2                 =   l_matcher1.group(2);
                        String str1                 =   l_matcher1.group(1);
                        if(str1.contains("length: ")){
                        	artwork_measurements_width=Double.parseDouble(str2);
                        }
                        else if(str1.contains("diameter: ")){
                        	artwork_measurements_width=Double.parseDouble(str2);
                       
                        }
                        else if(str1.contains("height: ")){
                        	artwork_measurements_height=Double.parseDouble(str2);
                        }
                        else{
                        	artwork_measurements_depth=Double.parseDouble(str2);
                        }
                    }
                    
                        }
                    if(str.contains("mm")){
                    	artwork_measurements_depth=artwork_measurements_depth/10;
                    	artwork_measurements_height=artwork_measurements_height/10;
                    	artwork_measurements_width=artwork_measurements_width/10;
                    	artwork_measurements_unit ="cm";
                        }
                    }
        System.out.println("No Pattern found for "+p_token);
        throw new PatternNotFoundException("No Pattern found for "+p_token);
    }
	
	
	
	
	
	
	
	
	

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
			".*(signed|designed|silver|inscribed|stencilled|étiquette|dessin|Au revers|gauche|Inscription|Longue|favrile|dated|lettered|Stamped|label|titled|signature|upper left|lower left|verso|"
					+ "annoted|dedicated|annotated|impression|crater-shaped|naux|noll|POMPON|LALANNE|JKM|PAUL|AG0|EBRANDT|JEAN|séquences|sign|DU|PN|sculpté|revers|émaillés|"
					+ "epruve|Despositio|dalpayrat|wallander|signature|Rückseitig|kosta|moulded|etikett|elise|bruxelles|dalmatian|wheel-engraved|monogram|éditée par|"
					+ "ink on the inside|stamped|dedicated|titleslip|titled|singed|title|embos|sed|incised|numbered|initialed|initialled|bears|dated|logelain|annotated|Consolidated|"
					+ "stretched|overlaid|impressed|inlaid|enamelled|produced|captioned|marked|carved|walnut|mark|marks|cracks|miss|cipher|label|cypher|initial|medallion|seal|decal|"
					+ "branded|embroidered|molded|left and right form|monogrammed|stenciled|Dédicacée|sign|janda|maeght|gießerstempel|gestempelt|datiert|paraphe|betitelt|krepp|berlin"
					+ "|auflage|timbre|porte|estampill|dat|num|Porte|monogram|Attribu|firmada|firm).*");

	CSVWriter csv = null;
	String filename;
	String outputPath;
	String auctionHouseName = "Sotheby's";
	String auctionLocation = "New York";
	String houseName = "sothebys-scrape";
	String auctionNum = "";
	String auctionDate = "";
	String auctionName = "";
	String auctionInternalId = "";
	private JdbcTemplate jdbcTemplate;

	private static final long serialVersionUID = 1L;

	public SothebiesScraper(JdbcTemplate jdbcTemplate) {
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		String sothebysLink = "";
		ArrayList<String> auctionsList = new ArrayList<String>();
		if (isPastSales) {
			sothebysLink = "https://www.sothebys.com/en/results?from=" + dateFormat.format(startDate) + "&to="
					+ dateFormat.format(endDate) + "&f0=" + startDate.getTime() + "-" + endDate.getTime();
		} else {
			sothebysLink = "https://www.sothebys.com/en/calendar?from=" + dateFormat.format(startDate) + "&to="
					+ dateFormat.format(endDate) + "&f0=" + startDate.getTime() + "-" + endDate.getTime();
		}

		consoleArea.append("Starting...");
		consoleArea.append("\n");
		consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		

		try {
			UserAgent userAgent = new UserAgent();
			userAgent.visit(sothebysLink);
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			Document doc = Jsoup.parse(content);
			int pageCount = 1;
			try {
				pageCount = Integer
						.parseInt(doc.select("li[class=SearchModule-pageCounts]").select("span").last().text().trim());
			} catch (Exception e) {
				System.out.println("Error getting page count");
			}

			for (int i = 1; i <= pageCount; i++) {
				if (!continueProcessing) {
					return;
				}
				String pageUrl = sothebysLink + "&p=" + i;
				userAgent.visit(pageUrl);
				String deptContent = userAgent.doc.innerHTML();
				userAgent.close();
				doc = Jsoup.parse(deptContent);

				Elements auctions = doc.select("li[class=SearchModule-results-item]");
				Date l_convertedDate = null;

				for (Element auction : auctions) {
					if (!continueProcessing) {
						return;
					}
					auctionUrl = auctionDate = auctionName = "";
					auctionUrl = auction.select("a[class=Card-info-container]").attr("href").trim();
					boolean urlExists = auctionsList.contains(auctionUrl);

					if (urlExists) {
						continue;
					} else {
						auctionsList.add(auctionUrl);
					}

					auctionDate = auction.select("div[class=Card-details]").text();
					auctionName = auction.select("div[class=Card-title]").text();

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

					if (auctionUrl.contains("/")) {
						auctionInternalId = auctionUrl.substring(auctionUrl.lastIndexOf("/") + 1).replace(".html", "");
					}

					if (auctionUrl.contains(".html") && auctionUrl.contains("-")) {
						auctionNum = auctionUrl.substring(auctionUrl.lastIndexOf("-") + 1).replace(".html", "");
					}

					if (auctionDate.contains("|")) {
						auctionDate = auctionDate.split("\\|")[0].trim();
						if (auctionDate.contains("–")) {
							auctionDate = auctionDate.split("–")[1].trim();
						}
					}

					dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
					if (auctionDate.trim() != "" && auctionDate.trim() != null) {
						l_convertedDate = dateFormat.parse(auctionDate);
					}

					if ((l_convertedDate.compareTo(startDate) > 0 && l_convertedDate.compareTo(endDate) < 0)
							|| (l_convertedDate.compareTo(startDate) == 0)
							|| (l_convertedDate.compareTo(endDate) == 0)) {
						try {
							this.init();
							System.out.println(auctionUrl);
							consoleArea.append("\n" + auctionUrl);
							consoleArea.append("\n");
							consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
							this.populateAuctionLots(auctionUrl);
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
					} else {
						System.out.println("Outside date range, skipping...");
						consoleArea.append("\nOutside date range, skipping...");
						consoleArea.append("\n");
						consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
					}
				}
			}
			/*consoleArea.append("\nScraping done!");
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());*/
			this.setVisible(false);
			this.dispose();
			JOptionPane.showMessageDialog(null, "Scraping done!", "Sothebies Scraper", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			System.out.println("Exception Occured");
			JOptionPane.showMessageDialog(null,
					"An exception occurred, please try again. The exception is: " + e.getMessage(), "Sothebies Scraper",
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
					}
				}

			} catch (Exception e) {// Catch exception if any
				e.printStackTrace();
			}

			filename = outputPath + "/" + auctionInternalId + "-" + auctionDate + ".csv";
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
			System.out.println(e);
			consoleArea.append("\n" + e);
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}

	/*
	 * Gets the auction lot links to populate data
	 */

	private void populateAuctionLots(String lotListingLink) {
		try {
			String scriptCode = "";
			int startIndex = 0;
			int endIndex = 0;
			String auctionYear = lotListingLink.replace("https://www.sothebys.com/en/auctions/", "")
					.replace(auctionInternalId, "").replace(".html", "");
			UserAgent userAgent = new UserAgent(); // create new userAgent
			userAgent.visit(lotListingLink); // visit a url
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			Document doc = Jsoup.parse(content);
			Elements l_ele = doc.select("script[type=text/javascript]");

			for (Element element : l_ele) {
				if (!continueProcessing) {
					return;
				}
				scriptCode = element.html();
				if (scriptCode.contains("function(window,undefined)")) {
					startIndex = (scriptCode.indexOf("//set ECAT.lot")) + 19;
					endIndex = scriptCode.indexOf("ECAT.lot = cleanArray(ECAT.lot)");
					scriptCode = scriptCode.substring(startIndex, endIndex);
					String l_lot_info_arr[] = scriptCode.split(";ECAT");

					for (int i = 0; i < l_lot_info_arr.length; i++) {
						if (!continueProcessing) {
							return;
						}
						if (l_lot_info_arr[i].contains("'id':")) {
							String[] ar = l_lot_info_arr[i].split("'id':");
							String lotStr = ar[1].substring(0, 5).replaceAll("[,']", "");
							String lotUrl = "http://www.sothebys.com/en/auctions/ecatalogue/" + auctionYear
									+ auctionInternalId + "/lot." + lotStr + ".html";
							populateLotDetails(lotUrl, true);
						}
					}
					break;
				}
			}
			if (!scriptCode.equals("")) {
			} else {
				l_ele = doc.select("script");
				scriptCode = "";
				for (Element element : l_ele) {
					if (!continueProcessing) {
						return;
					}
					scriptCode = element.html();
					System.out.println(scriptCode);
					consoleArea.append("\n" + scriptCode);
					consoleArea.append("\n");
					consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
					if (scriptCode.contains("@context")) {
						startIndex = (scriptCode.indexOf("offers\":[")) + 8;
						endIndex = scriptCode.indexOf("},\"performer");
						scriptCode = scriptCode.substring(startIndex, endIndex);
						JSONArray getArray = new JSONArray(scriptCode);
						String lotUrl = "";

						for (int i = 0; i < getArray.length(); i++) {
							if (!continueProcessing) {
								return;
							}
							JSONObject obj = getArray.getJSONObject(i);
							if (!(obj.isNull("url"))) {
								lotUrl = (String) obj.get("url");
								System.out.println(lotUrl);
								consoleArea.append("\n" + lotUrl);
								consoleArea.append("\n");
								consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
								populateLotDetails(lotUrl, false);
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
	private void populateLotDetails(String lotLink, boolean isAuctionId) {
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
		String artwork_final = "";
		String artwork_markings = "";
		String artwork_edition = "";
		String artwork_description = "";
//		String artwork_measurements_height = "";
	//	String artwork_measurements_width = "";
//		String artwork_measurements_depth = "";
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
		Elements catalogue = null;
		Elements catalogueDetails = null;
		Elements imageDetails = null;

		try {
			Document doc = null;
			int i = 0;
			boolean description = false;
			boolean dimension = false;
			UserAgent userAgent = new UserAgent();
			userAgent.visit(lotLink);
			String content = userAgent.doc.innerHTML();
			userAgent.close();
			doc = Jsoup.parse(content);

			try {
				if (isAuctionId) {
					// URL2 lot details
					Elements priceSold = doc.select("div[class=price-sold]");
					Elements artistName = doc.select("div[class=lotdetail-guarantee]");
					Elements artworkName = doc.select("div[class=lotdetail-subtitle]");
					Elements priceMin = doc.select("span[class=range-from]");
					Elements priceMax = doc.select("span[class=range-to]");
					Elements artworkImage = doc.select("div[id=main-image-container]").select("img");

					lot_num = doc.select("div[class=lotdetail-lotnumber visible-phone]").text();
					artist_birth = doc.select("div[class=lotdetail-artist-dates]").text().replaceAll("[^0-9]", "")
							.trim();
					lotDetails = doc.select("div[class=lotdetail-description-text]");
					catalogue = doc.select("h3[class=lotdetail-section-sub-header]");
					catalogueDetails = doc.select("div[class=readmore-content]");
					imageDetails = doc.select("img[data-type=AltImage]");

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
					if (artworkImage.size() > 0) {
						artwork_image = artworkImage.first().absUrl("src");
					}
					description = true;
				} else {
					// URL1 lot details
					Elements priceMin = doc.select("p[class=css-1g8ar3q]");
					Elements lotdetails = doc.select("div[class=css-1ewow1l]");

					lot_num = doc.select("span[class=css-16v44d6]").text();
					artwork_name = doc.select("h1[class=css-1n2kna8]").text();
					price_sold = doc.select("span[class=css-15o7tlo]").text().replaceAll("[^0-9]", "").trim();
					catalogue = doc.select("div[id=LotCatalogueing]").select("h3");
					catalogueDetails = doc.select("div[id=LotCatalogueing]").select("div[class=css-1ewow1l]");
					artwork_image = doc.select("div[class=css-10remba]").select("img").attr("src");
					imageDetails = doc.select("img[class=css-3rldmv]");

					if (priceMin.size() > 0) {
						price_estimate_min = priceMin.first().text().trim();
					}
					if (lotdetails.size() > 0) {
						lotDetails = lotdetails.first().select("p");
					}
				}
			} catch (Exception e) {
				System.out.println(e);
				consoleArea.append("\n" + e);
				consoleArea.append("\n");
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}

			try {
				for (int j = 1; j < imageDetails.size(); j++) {
					if (!continueProcessing) {
						return;
					}
					artwork_images[j - 1] = imageDetails.get(j).attr("src");
				}
			} catch (Exception e) {
				System.out.println(e);
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
				System.out.println(e);
			}

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

			for (Element detail : lotDetails) {
				if (!continueProcessing) {
					return;
				}

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
				}

				else if (artwork_name.contains(lotDetail) && StringUtils.isBlank(artist_name)) {
					artist_name = lotDetail;
				}

				else if (artwork_name.contains(lotDetail) && !StringUtils.isBlank(artist_name)) {
					artwork_name = lotDetail;
				}

				else if (lotDetail.matches("(.*)([\\d]{4})") && !description) {
					artist_birth = lotDetail.replaceAll("[^0-9]", "").trim();
				}

				else if (!description && i != 1) {
					boolean isExists = Arrays.stream(items).parallel().anyMatch(lotDetail::contains);
					if (!isExists) {
						artist_nationality = lotDetail;
					}
				}

				else if (description) {
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
						if (!continueProcessing) {
							return;
						}
						value = value.trim();
						System.out.println("value&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+value);
						if(value.contains("cm")) {
							if(value.contains(";"))
							{
								String[] values = value.split(";");
								value = values[values.length-1];
							}
							Pattern l_pattern_temp = Pattern.compile("(.*)(by)(.*)cm.");
							
						
									Matcher	l_matcher_temp =l_pattern_temp.matcher(value.replace("x", "by"));
							if (l_matcher_temp.find()) {
								artwork_measurements_height = Double.parseDouble(l_matcher_temp.group(1));
								artwork_measurements_width = Double.parseDouble(l_matcher_temp.group(3));
								artwork_measurements_depth = 0.0;
								auction_measureunit = "cm";
							}
						}
						
						if(value.contains("mm") ) {
						if(value.contains(","))
						{
							String[] values = value.split(",");
							value = values[values.length-1];
						}
							Pattern l_pattern_temp = Pattern.compile("(.*)(by)(.*)mm");
							
						
									Matcher	l_matcher_temp =l_pattern_temp.matcher(value.replace("x", "by"));
							if (l_matcher_temp.find()) {
								artwork_measurements_height = Double.parseDouble(l_matcher_temp.group(1).replace(",", ""));
								artwork_measurements_width = Double.parseDouble(l_matcher_temp.group(3));
								artwork_measurements_depth = 0.0;
								auction_measureunit = "mm";
							}
						}
						if (value.contains("inches") || value.contains("in."))
								{
							 value = inchToNormalNumberConversion(value);
							 Pattern l_pattern_temp1 = Pattern.compile("(.*)(by)(.*)(in.|inches)");
								Matcher	l_matcher_temp1 =l_pattern_temp1.matcher(value.replace("x", "by"));
								if (l_matcher_temp1.find()) {
									artwork_measurements_height = Double.parseDouble(l_matcher_temp1.group(1).replace(",", ""));
									artwork_measurements_width = Double.parseDouble(l_matcher_temp1.group(3));
									artwork_measurements_depth = 0.0;
									auction_measureunit = "in";
							
								}
								}
		
						
						

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
						}

						else if ((value.toLowerCase().contains("mounted")
								|| value.toLowerCase().contains("framed") && !DIM_WORDS.matcher(value).matches())
								&& !MATERIAL_WORDS.matcher(value).matches()
								&& !MATERIAL_WORDS.matcher(value.toLowerCase()).matches()) {
							if (!"".equals(artwork_condition_in)) {
								artwork_condition_in = artwork_condition_in + ", " + value;
							} else {
								artwork_condition_in = value;
							}
						}

						else if (SIGN_WORDS.matcher(value).matches()
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
						}

						else if (DIM_WORDS.matcher(value).matches()) {
							if (dimension) {
								if (!"".equals(artwork_size_notes)) {
									artwork_size_notes = artwork_size_notes + ", " + value;
								} else {
									artwork_size_notes = value;
								}
								continue;
							}

							try {
								dimension = true;
								value = value.replaceAll("by", "x");
								if(artwork_measurements_height==0.0)
								{

								l_pattern = Pattern.compile(
										"^([a-zA-Z]+)[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(cm)*)(.*)");
								l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
										.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));

								if (l_matcher.find()) {
									if (!StringUtils.isBlank(l_matcher.group(1))) {
										if (!"".equals(artwork_size_notes)) {
											artwork_size_notes = artwork_size_notes + ", " + l_matcher.group(1);
										} else {
											artwork_size_notes = l_matcher.group(1);
										}
									}
									artwork_measurements_height = Double.parseDouble(l_matcher.group(4));
									artwork_measurements_width = Double.parseDouble(l_matcher.group(6));
									artwork_measurements_depth = 0;
									auction_measureunit = "cm";
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
									artwork_measurements_height = Double.parseDouble(l_matcher.group(3));
									artwork_measurements_width = Double.parseDouble(l_matcher.group(5));
									artwork_measurements_depth = Double.parseDouble(l_matcher.group(7));
									auction_measureunit = "cm";
								} else {
									l_pattern = Pattern.compile(
											"[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(x)[\\s]*([\\d]+[\\.]{0,1}[\\d]*)[\\s]*(cm)*)(.*)");
									l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
											.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));
									if (l_matcher.find()) {
										artwork_measurements_height = Double.parseDouble(l_matcher.group(3));
										artwork_measurements_width = Double.parseDouble(l_matcher.group(5));
										artwork_measurements_depth = 0;
										auction_measureunit = "cm";
									}
								}
								
								
								parseDimensions(value);
}
								

								if (StringUtils.isBlank(Double.toString(artwork_measurements_height))) {
									l_pattern = Pattern
											.compile("[\\s]*(([\\D]*)([\\d]+[\\.]{0,1}[\\d]*)[\\s]*[\\D]*(cm)*)(.*)");
									l_matcher = l_pattern.matcher(value.replace("x�", "x").replace("�", "x")
											.replace("�cm", "cm").replace("in. x", "x").replace("by", ""));
									if (l_matcher.find()) {
										artwork_measurements_height = Double.parseDouble(l_matcher.group(5).replace("cm", "").trim());
										artwork_measurements_width = 0;
										artwork_measurements_depth = 0;
										auction_measureunit = "cm";
										artwork_size_notes = l_matcher.group(2);
									}
								}

							} catch (Exception e) {
								System.out.println("Error getting dimension");
								consoleArea.append("\n" + "Error getting dimension");
								consoleArea.append("\n");
								consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
							}
						}

						else if (MATERIAL_WORDS.matcher(value).matches()) {
							System.out.println("InsideMaterialWords********************************");
							if (!"".equals(artwork_materials)) {
								artwork_materials = artwork_materials + ", " + value;
								System.out.println("artwork_material&&&&&&&&&&&&&&&&&&&&&&&&&"+artwork_materials);
							} else {
								artwork_materials = value;
								System.out.println("artwork_materil************************"+artwork_materials);
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
				if (!continueProcessing) {
					return;
				}
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
				artwork_final = FineartUtils.getMaterialCategory(artwork_materials, jdbcTemplate);
			}
			
			//replaceAll("[^\\w]"
			
			String imageName = auctionName + "-" + artist_name + "-" + System.currentTimeMillis();
			imageName = imageName.replaceAll("[^\\w]","-"); //replace all special characters with hyphen

			String auctionDetails = auctionHouseName + "|" + auctionLocation + "|" + auctionNum + "|" + auctionDate
					+ "|" + auctionName + "|" + lot_num + "|" + "|" + price_kind + "|" + price_estimate_min + "|"
					+ price_estimate_max + "|" + price_sold + "|" + WordUtils.capitalizeFully(artist_name.toLowerCase())
					+ "|" + artist_birth + "|" + artist_death + "|" + artist_nationality + "|"
					+ WordUtils.capitalizeFully(strinetrailling(artwork_name.toLowerCase())) + "|" + artwork_year_identifier + "|"
					+ artwork_start_year + "|" + artwork_end_year + "|" + strinetrailling(artwork_materials) + "|" + artwork_final + "|"
					+ strinetrailling(artwork_markings) + "|" + artwork_edition + "|" + artwork_description + "|"
					+ artwork_measurements_height + "|" + artwork_measurements_width + "|" + artwork_measurements_depth
					+ "|" + artwork_size_notes + "|" + auction_measureunit + "|" + artwork_condition_in + "|"
					+ artwork_provenance + "|" + artwork_literature + "|" + artwork_exhibited + "|" + artwork_image
					+ "|" + artwork_images[0] + "|" + artwork_images[1] + "|" + artwork_images[2] + "|"
					+ artwork_images[3] + "|" + imageName + "-a.jpg" + "|" + imageName + "-b.jpg" + "|" + imageName + "-c.jpg" 
					+ "|" + imageName + "-d.jpg" + "|" + imageName + "-e.jpg" + "|" + lotLink;

			String row[] = auctionDetails.split("\\|");
			csv.writeNext(row);
		} catch (Exception e) {
			System.out.println(e);
			consoleArea.append("\n" + e);
			consoleArea.append("\n");
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
		}
	}

	/*private String getMaterialCategory(String artwork_materials) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String artwork_final = "";

		try {
			con = ConnectionDB.getConnectionMySqlOT();
			statement = con.prepareStatement(
					"SELECT material_name, material_category FROM fineart_materials WHERE lower(material_name) = lower(?)");
			statement.setString(1, artwork_materials);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				artwork_final = resultSet.getString("material_category");
			} else {
				String materials[] = artwork_materials.split(",");
				for (String material : materials) {
					statement.clearParameters();
					statement.setString(1, material.trim());
					resultSet = statement.executeQuery();
					if (resultSet.next()) {
						artwork_final = resultSet.getString("material_category");
						break;
					}
				}
			}

			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return artwork_final;
	}*/
}