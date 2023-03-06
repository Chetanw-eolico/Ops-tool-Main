package com.core.fineart;

import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;



public class FineartUtils {
	
	private static Pattern sqlTokenPattern;
	private static final HashMap<String,String> sqlTokens;
	private static final Random generator = new Random();
	private static SimpleDateFormat javaTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	//2020-07-13T07:51:34.140Z
	
	static
	{           
	    //MySQL escape sequences: http://dev.mysql.com/doc/refman/5.1/en/string-syntax.html
	    String[][] search_regex_replacement = new String[][]
	    {
	                //search string     search regex        sql replacement regex
	            {   "\u0000"    ,       "\\x00"     ,       "\\\\0"     },
	            {   "'"         ,       "'"         ,       "\\\\'"     },
	            {   "\""        ,       "\""        ,       "\\\\\""    },
	            {   "\b"        ,       "\\x08"     ,       "\\\\b"     },
	            {   "\n"        ,       "\\n"       ,       "\\\\n"     },
	            {   "\r"        ,       "\\r"       ,       "\\\\r"     },
	            {   "\t"        ,       "\\t"       ,       "\\\\t"     },
	            {   "\u001A"    ,       "\\x1A"     ,       "\\\\Z"     },
	            {   "\\"        ,       "\\\\"      ,       "\\\\\\\\"  }
	    };

	    sqlTokens = new HashMap<String,String>();
	    String patternStr = "";
	    for (String[] srr : search_regex_replacement)
	    {
	        sqlTokens.put(srr[0], srr[2]);
	        patternStr += (patternStr.isEmpty() ? "" : "|") + srr[1];            
	    }
	    sqlTokenPattern = Pattern.compile('(' + patternStr + ')');
	}
	
	public static void setComponentSelection(Component selectionComponent) {
		
		
		
		ChristiesPastSalesRowComponent christiesPastSalesRowComponent = null;
		SothebyPastSalesRowComponent sothebyPastSalesRowComponent = null;
		BonhamsPastSalesRowComponent bonhamsPastSalesRowComponent = null;
		
		//initialize all instances and un-select all radio buttons
		for(Component rowComponent : selectionComponent.getParent().getComponents()) {
			if(rowComponent instanceof ChristiesPastSalesRowComponent) {
				christiesPastSalesRowComponent = (ChristiesPastSalesRowComponent) rowComponent;
				christiesPastSalesRowComponent.getRadioButton().setSelected(false);
			} else if (rowComponent instanceof SothebyPastSalesRowComponent) {
				sothebyPastSalesRowComponent = (SothebyPastSalesRowComponent) rowComponent;
				sothebyPastSalesRowComponent.getRadioButton().setSelected(false);
			} else if(rowComponent instanceof BonhamsPastSalesRowComponent) {
				bonhamsPastSalesRowComponent = (BonhamsPastSalesRowComponent) rowComponent;
				bonhamsPastSalesRowComponent.getRadioButton().setSelected(false);
			}
		}
		
		//now only select the desired radio button
		if(selectionComponent instanceof ChristiesPastSalesRowComponent) {
			christiesPastSalesRowComponent.getRadioButton().setSelected(true);
		} else if (selectionComponent instanceof SothebyPastSalesRowComponent) {
			sothebyPastSalesRowComponent.getRadioButton().setSelected(true);
		} else if(selectionComponent instanceof BonhamsPastSalesRowComponent) {
			bonhamsPastSalesRowComponent.getRadioButton().setSelected(true);
		}
	}
	
	public static String getMaterialCategory(String artwork_materials, JdbcTemplate jdbcTemplate) {
		String artwork_final = "";

		try {
			String sql = "SELECT material_name, material_category FROM fineart_materials WHERE lower(material_name) like lower('%" + artwork_materials.trim() + "%')";  
			artwork_final = jdbcTemplate.execute(sql, new PreparedStatementCallback<String>() {  
		    @Override  
		    public String doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException { 
		    	ResultSet resultSet = null;
		        //ps.setString(1, artwork_materials);
		        resultSet = ps.executeQuery(sql);  
		        if (resultSet.next()) {
					return resultSet.getString("material_category");
				} else {
					String materials[] = artwork_materials.split(",");
					for (String material : materials) {
						String sql1 = "SELECT material_name, material_category FROM fineart_materials WHERE lower(material_name) = lower(' " + material + "')";
						//ps.clearParameters();
						//ps.setString(1, material.trim());
						resultSet = ps.executeQuery(sql1);
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
	}
	
	public static String getArtistPureName(String name) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}

		name = name.replaceAll("(<[/]?([a-zA-Z])+>)", "").replaceAll("\\.\\.\\.", "").replaceAll("# - ", "")
				.replaceAll("=-", "").replaceAll("\\(|\\)", "").replaceAll("Ãƒâ€°", "Ã‰").trim();

		name = name.toLowerCase();

		if (name.contains("attributed to") || name.contains("attribue a") || name.contains("attributed")) {
			name = name.replaceAll("attributed to", "").replaceAll("attribue a", "");
		} else if (name.contains("in the style of") || name.contains("style of")) {
			name = name.replaceAll("in the style of", "").replaceAll("style of", "");
		} else if (name.contains("im stile von")) {
			name = name.replaceAll("in the style of", "");
		} else if (name.contains("attr.") || name.contains("attr:")) {
			name = name.replaceAll("attr.", "");
		} else if (name.contains("atribuido a")) {
			name = name.replaceAll("attr.", "");
		} else if (name.contains("school of")) {
			name = name.replaceAll("school of", "");
		} else if (name.contains("manner of")) {
			name = name.replaceAll("manner of", "");
		} else if (name.contains("after")) {
			name = name.replaceAll("after", "");
		} else if (name.contains("(after)")) {
			name = name.replaceAll("\\(after\\)", "");
		} else if (name.contains("circle of") || name.contains("circle")) {
			name = name.replaceAll("circle of", "");
		} else if (name.contains("studio of") || name.contains("studio")) {
			name = name.replaceAll("studio of", "");
		} else if (name.contains("follower of")) {
			name = name.replaceAll("follower of", "");
		} else if (name.contains("workshop of")) {
			name = name.replaceAll("workshop of", "");
		} else if (name.contains("follower")) {
			name = name.replaceAll("follower", "");
		} else if (name.contains("d'aprÃ¨s")) {
			name = name.replaceAll("D'aprÃ¨s", "").replaceAll("d'aprÃ¨s", "");
		} else if (name.contains("attribuÃ© au")) {
			name = name.replaceAll("attribuÃ© au", "");
		} else if (name.contains("hans ateljÃ©")) {
			name = name.replaceAll("hans ateljÃ©", "");
		} else if (name.contains("hans krets")) {
			name = name.replaceAll("hans krets", "");
		} else if (name.contains("tillskriven")) {
			name = name.replaceAll("tillskriven", "");
		} else if (name.contains("tillskrivna")) {
			name = name.replaceAll("tillskrivna", "");
		} else if (name.contains("hans efterfÃ¶ljare")) {
			name = name.replaceAll("hans efterfÃ¶ljare", "");
		} else if (name.contains("hans art")) {
			name = name.replaceAll("hans art", "");
		} else if (name.contains("hennes ateljÃ©")) {
			name = name.replaceAll("hennes ateljÃ©", "");
		} else if (name.contains("hennes krets")) {
			name = name.replaceAll("hennes krets", "");
		} else if (name.contains("hennes efterfÃ¶ljare")) {
			name = name.replaceAll("hennes efterfÃ¶ljare", "");
		} else if (name.contains("hennes art")) {
			name = name.replaceAll("hennes art", "");
		} else if (name.contains("hans skola")) {
			name = name.replaceAll("hans skola", "");
		} else if (name.contains("hennes skola")) {
			name = name.replaceAll("hennes skola", "");
		} else if (name.contains("kopia efter")) {
			name = name.replaceAll("kopia efter", "");
		} else if (name.contains("hans stil")) {
			name = name.replaceAll("hans stil", "");
		} else if (name.contains("hennes stil")) {
			name = name.replaceAll("hennes stil", "");
		} else if (name.contains("cercle de") || name.contains("cercle")) {
			name = name.replaceAll("cercle de", "");
		} else if (name.contains("Entourage de") || name.contains("entourage")) {
			name = name.replaceAll("entourage de", "");
		} else if (name.contains("AttribuÃ© Ã ") || name.contains("attribuÃ© Ã ")) {
			name = name.replaceAll("attribuÃ© Ã ", "");
		} else if (name.contains("Suiveur de") || name.contains("suiveur de")) {
			name = name.replaceAll("suiveur de", "");
		} else if (name.contains("atelier de") || name.contains("atelier")) {
			name = name.replaceAll("atelier de", "");
		} else if (name.contains("Dâ€™aprÃ¨s") || name.contains("dâ€™aprÃ¨s")) {
			name = name.replaceAll("dâ€™aprÃ¨s", "");
		} else if (name.contains("A LA MANERA DE") || name.contains("a la manera de")) {
			name = name.replaceAll("a la manera de", "");
		} else if (name.contains("Werkstatt") || name.contains("werkstatt")) {
			name = name.replaceAll("werkstatt", "");
		} else if (name.contains("zugeschrieben") || name.contains("Zugeschrieben")) {
			name = name.replaceAll("zugeschrieben", "");
		} else if (name.contains("Nachfolge") || name.contains("nachfolge")) {
			name = name.replaceAll("nachfolge", "");
		} else if (name.contains("Umkreis") || name.contains("umkreis")) {
			name = name.replaceAll("umkreis", "");
		} else if (name.contains("Kopie") || name.contains("kopie")) {
			name = name.replaceAll("kopie", "");
		} else if (name.contains("Schule") || name.contains("schule")) {
			name = name.replaceAll("schule", "");
		} else if (name.contains("In der Art") || name.contains("in der art")) {
			name = name.replaceAll("in der art", "");
		} else if (name.contains("Nachfolger") || name.contains("nachfolger")) {
			name = name.replaceAll("nachfolger", "");
		} else if (name.contains("Nach") || name.contains("nach")) {
			name = name.replaceAll("nach", "");
		} else if (name.contains("Nachfolger des") || name.contains("nachfolger des")) {
			name = name.replaceAll("nachfolger des", "");
		} else if (name.contains("DespuÃ©s De") || name.contains("despuÃ©s de")) {
			name = name.replaceAll("despuÃ©s de", "");
		}

		name = name.trim();

		if (name.startsWith("sir")) {
			name = name.replaceAll("sir", "").trim();
		} else if (name.startsWith("captain")) {
			name = name.replaceAll("captain", "").trim();
		} else if (name.startsWith("lady")) {
			name = name.replaceAll("lady", "").trim();
		} else if (name.startsWith("mrs")) {
			name = name.replaceAll("mrs", "").trim();
		} else if (name.startsWith("mr")) {
			name = name.replaceAll("mr", "").trim();
		} else if (name.startsWith("rev ")) {
			name = name.replaceAll("rev ", "").trim();
		} else if (name.startsWith("rev.")) {
			name = name.replaceAll("rev.", "").trim();
		} else if (name.startsWith("reverend")) {
			name = name.replaceAll("reverend", "").trim();
		} else if (name.startsWith("prof")) {
			name = name.replaceAll("prof", "").trim();
		} else if (name.startsWith("professor")) {
			name = name.replaceAll("professor", "").trim();
		} else if (name.startsWith("colonel")) {
			name = name.replaceAll("colonel", "").trim();
		} else if (name.startsWith("col ")) {
			name = name.replaceAll("col ", "").trim();
		} else if (name.startsWith("col.")) {
			name = name.replaceAll("col.", "").trim();
		} else if (name.startsWith("madame")) {
			name = name.replaceAll("madame", "").trim();
		} else if (name.startsWith("miss")) {
			name = name.replaceAll("miss", "").trim();
		} else if (name.startsWith("major")) {
			name = name.replaceAll("major", "").trim();
		} else if (name.startsWith("dr ")) {
			name = name.replaceAll("dr ", "").trim();
		} else if (name.startsWith("dr.")) {
			name = name.replaceAll("dr.", "").trim();
		} else if (name.startsWith("lieutenant")) {
			name = name.replaceAll("lieutenant", "").trim();
		} else if (name.startsWith("lt")) {
			name = name.replaceAll("lt", "").trim();
		} else if (name.startsWith("general")) {
			name = name.replaceAll("general", "").trim();
		} else if (name.startsWith("mme")) {
			name = name.replaceAll("mme", "").trim();
		} else if (name.startsWith("mlle")) {
			name = name.replaceAll("mlle", "").trim();
		} else if (name.startsWith("baroness")) {
			name = name.replaceAll("baroness", "").trim();
		} else if (name.startsWith("chief")) {
			name = name.replaceAll("chief", "").trim();
		} else if (name.startsWith("princess")) {
			name = name.replaceAll("princess", "").trim();
		} else if (name.startsWith("dame")) {
			name = name.replaceAll("dame", "").trim();
		} else if (name.startsWith("sister")) {
			name = name.replaceAll("sister", "").trim();
		} else if (name.startsWith("maj ")) {
			name = name.replaceAll("maj ", "").trim();
		} else if (name.startsWith("maj.")) {
			name = name.replaceAll("maj.", "").trim();
		} else if (name.startsWith("lieutenant colonel")) {
			name = name.replaceAll("lieutenant colonel", "").trim();
		} else if (name.startsWith("lieutenant-commander")) {
			name = name.replaceAll("lieutenant-commander", "").trim();
		} else if (name.startsWith("general sir")) {
			name = name.replaceAll("general sir", "").trim();
		} else if (name.startsWith("capt")) {
			name = name.replaceAll("capt", "").trim();
		} else if (name.startsWith("father")) {
			name = name.replaceAll("father", "").trim();
		} else if (name.startsWith("rev Dr")) {
			name = name.replaceAll("rev Dr", "").trim();
		} else if (name.startsWith("rear admiral")) {
			name = name.replaceAll("rear admiral", "").trim();
		} else if (name.startsWith("admiral sir")) {
			name = name.replaceAll("admiral sir", "").trim();
		} else if (name.startsWith("sgt")) {
			name = name.replaceAll("sgt", "").trim();
		} else if (name.startsWith("major general")) {
			name = name.replaceAll("major general", "").trim();
		} else if (name.startsWith("senator")) {
			name = name.replaceAll("senator", "").trim();
		} else if (name.startsWith("hon sir")) {
			name = name.replaceAll("hon sir", "").trim();
		} else if (name.startsWith("baron")) {
			name = name.replaceAll("baron", "").trim();
		} else if (name.startsWith("major sir")) {
			name = name.replaceAll("major sir", "").trim();
		} else if (name.startsWith("brigadier general")) {
			name = name.replaceAll("brigadier general", "").trim();
		} else if (name.startsWith("comte")) {
			name = name.replaceAll("comte", "").trim();
		} else if (name.startsWith("gen ")) {
			name = name.replaceAll("gen ", "").trim();
		} else if (name.startsWith("gen.")) {
			name = name.replaceAll("gen.", "").trim();
		} else if (name.startsWith("doctor")) {
			name = name.replaceAll("doctor", "").trim();
		} else if (name.startsWith("private")) {
			name = name.replaceAll("private", "").trim();
		}

		if (name.endsWith(" iii")) {
			name = name.substring(0, name.length() - 4);
		} else if (name.endsWith(" ii")) {
			name = name.substring(0, name.length() - 3);
		} else if (name.endsWith(" i")) {
			name = name.substring(0, name.length() - 2);
		} else if (name.endsWith(" iv")) {
			name = name.substring(0, name.length() - 3);
		} else if (name.endsWith(" junior")) {
			name = name.substring(0, name.length() - 7);
		} else if (name.endsWith(" jnr.")) {
			name = name.substring(0, name.length() - 5);
		} else if (name.endsWith(" jr")) {
			name = name.substring(0, name.length() - 3);
		} else if (name.endsWith(" jr.")) {
			name = name.substring(0, name.length() - 4);
		} else if (name.endsWith(" senior")) {
			name = name.substring(0, name.length() - 7);
		} else if (name.endsWith(" snr")) {
			name = name.substring(0, name.length() - 4);
		} else if (name.endsWith(" sr")) {
			name = name.substring(0, name.length() - 3);
		} else if (name.endsWith(" sr.")) {
			name = name.substring(0, name.length() - 4);
		} else if (name.endsWith(" the younger")) {
			name = name.replaceAll("the younger", "").trim();
		} else if (name.endsWith(" (the younger)")) {
			name = name.replaceAll("(the younger)", "").trim();
		} else if (name.endsWith(" the elder")) {
			name = name.replaceAll("the elder", "").trim();
		} else if (name.endsWith(" il giovane")) {
			name = name.replaceAll("il giovane", "").trim();
		} else if (name.endsWith(" il vecchio")) {
			name = name.replaceAll("il vecchio", "").trim();
		} else if (name.endsWith(" snr.")) {
			name = name.substring(0, name.length() - 4);
		}

		if (name.startsWith(",")) {
			name = name.replace(",", "");
		}

		int coma = name.indexOf(",");
		if (name.indexOf(" ") < coma - 1) {
			name = name.substring(0, coma).trim();
		} else {
			if (coma != -1) {
				name = name.substring(coma + 1).trim() + " " + name.substring(0, coma).trim();
			}

		}
		name = washoutSpecialCharacters(name.toLowerCase()).replace("european", "").replace(" *", "").replaceAll("(b)[.](.*)", "");
		if (name.trim().endsWith("ra") || name.trim().endsWith("roi") || name.trim().endsWith("r.a.")
				|| name.trim().endsWith("o.m.") || name.trim().endsWith("rws") || name.trim().endsWith("rwa")
				|| name.trim().endsWith("are")) {
			name = name.replace("ra", "").replace("r.a.", "").replace("o.m.", "").replace("roi", "").replace("rws", "")
					.replace("rwa", "").replace("are", "");
		}
		name = name.replaceAll("22nd century", "").replaceAll("21st century", "").replaceAll("20th century", "").replaceAll("19th century", "")
				.replaceAll("18th century", "").replaceAll("17th century", "").replaceAll("16th century", "").replaceAll("15th century", "").replaceAll("14th century", "")
				.replaceAll("13th century", "").replaceAll("12th century", "").replaceAll("11th century", "").replaceAll("10th century", "").replaceAll("9th century", "")
				.replaceAll("8th century", "").replaceAll("7th century", "").replaceAll("6th century", "").replaceAll("5th century", "").replaceAll("4th century", "")
				.replaceAll("3rd century", "").replaceAll("2nd century", "").replaceAll("1st century", "");

		if (name.matches("(.*)(\\d{4})(.*)[-](.*)")) {
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("(.*)(\\d{4})(.*)[-](.*)");
			java.util.regex.Matcher m = p.matcher(name);
			m.find();
			name = m.group(1).trim();
		}
		
		return name.trim();
	}
	
	private static String washoutSpecialCharacters(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		string = StringEscapeUtils.unescapeHtml4(string);

		string = string.replaceAll("", "")
				.replaceAll("&ndash;", "â€“").replaceAll("&rsquo;", "'").replaceAll("&Rsquo;", "'")
				.replaceAll("&lsquo;", "â€˜").replaceAll("ÃƒÂ²â€œ", "").replaceAll("Ã¢â‚¬â„¢", "'")
				.replaceAll("Ã¡", "a").replaceAll("&egrave;", "Ã¨").replaceAll("&agrave;", "Ã ")
				.replaceAll("&Agrave;", "Ã€").replaceAll("&uuml;", "Ã¼").replaceAll("&Uuml;", "ÃƒÅ¡")
				.replaceAll("&oacute;", "Ã³").replaceAll("&Oacute;", "Ã“").replaceAll("&oacut;", "Ã³")
				.replaceAll("&uacute;", "Ãº").replaceAll("&Uacute;", "ÃƒÅ¡").replaceAll("&uacut;", "Ãº")
				.replaceAll("&ntilde;", "Ã±").replaceAll("&Ntilde;", "Ã‘").replaceAll("&ntild;", "Ã±")
				.replaceAll("&Amp;", "&").replaceAll("&amp;", "&").replaceAll("&Amp;Nbsp;", "& ")
				.replaceAll("Nbsp;", " ").replaceAll("&eacute;", "Ã©").replaceAll("&eacute", "Ã©")
				.replaceAll("&Eacute;", "Ã‰").replaceAll("&eacut;", "Ã©").replaceAll("&Ograve;", "Ã’")
				.replaceAll("&Quot;", "\"").replaceAll("&quo;", "\"").replaceAll("&quot;", "'")
				.replaceAll("&ecirc;", "Ãª").replaceAll("&Ocirc;", "Ã”").replaceAll("&ocirc;", "Ã´")
				.replaceAll("nbsp;", " ").replaceAll("&ccedil;", "Ã§").replaceAll("&Ccedil;", "Ã‡")
				.replaceAll("&frac12;", "1/2").replace("&lt;B&gt;&lt;I&gt;", "").replace("&lt;/I&gt;&lt;/B&gt;", "")
				.replace("lt;br /gt;", "").replace("&lt;br /&gt;", "").replace("lt;br /&gt;", "").replace("&Oum;", "Ã–")
				.replace("&Ouml;", "Ã–").replaceAll("&ouml;", "Ã¶").replace("&Aacut;", "Ã�")
				.replaceAll("&Aacute;", "Ã�").replaceAll("&aacute;", "Ã¡").replaceAll("&aacut;", "Ã¡")
				.replace("&yacute;", "Ã½").replace("â€¢", "").replace(" Ã¢â‚¬Â¢", "").replace("&igrave", "Ã¬")
				.replace("&ugrave;", "Ã¹").replace("&oslash;", "Ã¸").replace("&THORN;", "Ãž").replace("&otilde;", "Ãµ")
				.replaceAll("&AElig;", "Ã†").replaceAll("&yuml;", "y").replaceAll("&ordm;", "Âº")
				.replaceAll("Ãœ", "ÃƒÅ¡")
				.replaceAll("&acirc;", "Ã¢").replace("&Acirc;", "Ã‚").replaceAll("&aring;", "Ã¥").replaceAll("â€“", "-")
				.replaceAll("&iacut;", "Ã­").replaceAll("&iacute;", "Ã­").replaceAll("&Iacute;", "Ã�")
				.replaceAll("&auml;", "Ã¤").replaceAll("&pound;", "Â£").replaceAll("Ã¢â‚¬â„¢", "'")
				.replaceAll("&iquest;", "Â¿").replaceAll("&szlig;", "ÃŸ").replace("&#339;", "Å“")
				.replaceAll("(&euro;&nbsp;)|(&#8364;&nbsp;)", "â‚¬ ").replaceAll("&atilde;", "Ã£")
				.replace((char) 160, ' ').replace("<p>", "").replace("&#237;", "Ã­").replace("<em>", "")
				.replace("</em>", "").replace("<br />", "").replace((char) 8211, '-').replace((char) 8212, '-')
				.replace((char) 8216, '\'').replace((char) 8217, '\'').replaceAll((char) 8220 + "", "")
				.replaceAll((char) 8221 + "", "").replaceAll((int) 8230 + "", "...");
		return string;
	}
	
	/*public static String formatAccents(String input) {
		try {
			input = input.replaceAll("ã", "Ã").replaceAll("ž", "Ž").replaceAll("å", "Å").replaceAll("ÿ", "Ÿ").replaceAll("â©", "Â©").replaceAll("â•", "Â•")
			.replaceAll("â£", "Â£").replaceAll("â°", "Â°").replaceAll("âº", "Âº").replaceAll(" Ã ", " Ã€ ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}*/
	
	public static String getSqlSafeString(String input) {
		if(StringUtils.isEmpty(input)) {
			return "";
		}
		input = input.replaceAll("\\\\", "");
		
	    Matcher matcher = sqlTokenPattern.matcher(input);
	    StringBuffer sb = new StringBuffer();
	    while(matcher.find())
	    {
	        matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
	    }
	    matcher.appendTail(sb);
	    return sb.toString();
	}
	
	public static String get_UTF8_String(String input) {
		try {
			char[] charArr = input.toCharArray();
			StringBuilder sb = new StringBuilder();
			
			int strLength = input.length();
			for(int i=0; i<strLength; i++) {
				char aChar = charArr[i];
				if(aChar == 'Ã') {
					if(i == strLength-1 || charArr[i+1] == ' ') {	//if this is the last char in the string or must have an space after it.
						sb.append('Ã');
						sb.append(' '); //this is not space character, this is whitespace, needed to convert a single Ã character to à. Otherwise it will 
						//not convert and will show (?)
					} else {
						sb.append('Ã');
					}
				} else {
					sb.append(charArr[i]);
				}
			}
			
			input = sb.toString();
			input = new String(input.getBytes("Windows-1252"), "UTF-8");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public static String get_Windows1252_String(String input) {
		try {
			input = new String(input.getBytes("UTF-8"), "Windows-1252");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(input);
		return input;
	}
	
	public static String getUrlSafeString(String input) {
		if(StringUtils.isEmpty(input)) {
			return "";
		} else {
			input = input.trim();
			return input.replaceAll("\\\\", "–").replaceAll("/", "–").replaceAll("\\+", " Plus ").replaceAll("\\?", "–").replaceAll("-", "–").replaceAll("#", "–");
		}
	}
	
	public static String getSHA1String(String anyString) {
		return DigestUtils.sha1Hex(anyString);
	}
	
	/*
	 * Six Digit OTP generator
	 */
	public static int generateOTP() {
	    return 100000 + generator.nextInt(900000);
	}
	
	public static String getCurrentUTCTime() {
		OffsetDateTime now = OffsetDateTime.now( ZoneOffset.UTC );
		return now.toString();
	}
	
	public static String getOneDayValidityForSubsription(String dateTime) {
		Calendar cal = Calendar.getInstance(); 
		try {
			cal.setTime(javaTimeFormat.parse(dateTime));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, 1);
		return javaTimeFormat.format(cal.getTime());
	}
	
	public static String getOneMonthValidityForSubsription(String dateTime) {
		Calendar cal = Calendar.getInstance(); 
		try {
			cal.setTime(javaTimeFormat.parse(dateTime));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.MONTH, 1);
		return javaTimeFormat.format(cal.getTime());
	}
	
	public static String getOneYearValidityForSubsription(String dateTime) {
		Calendar cal = Calendar.getInstance(); 
		try {
			cal.setTime(javaTimeFormat.parse(dateTime));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.YEAR, 1);
		return javaTimeFormat.format(cal.getTime());
	}
	
	public static String reverseWords(String str)
    {
  
        Pattern pattern = Pattern.compile("\\s");
  
        String[] temp = pattern.split(str);
        String result = "";
  
        for (int i = 0; i < temp.length; i++) {
            if (i == temp.length - 1)
                result = temp[i] + result;
            else
                result = " " + temp[i] + result;
        }
        return result;
    }
}
