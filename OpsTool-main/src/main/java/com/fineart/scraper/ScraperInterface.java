package com.fineart.scraper;

import java.util.Date;
import java.util.List;

public interface ScraperInterface {
	void initScraping(final Date startDate, final Date endDate, final List<String> fineartKeywordFiltersList, final String outputPath, final boolean isPastSales);
	void close();
}
