package org.inqle.qa.gdata.gae;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.inqle.qa.Queryer;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;
import org.mortbay.log.Log;

import com.google.appengine.api.datastore.Entity;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;
import com.google.inject.Inject;

public class GaeGdataSpreadsheetImporter implements GdataSpreadsheetImporter {

	public static final String SUCCESS = "success";
	private final Queryer queryer;
	private final SpreadsheetService spreadsheetService;
	private final Logger log;
	private Map<String, String> prefixes = new HashMap<String, String>();
	private List<String> headerUris = new ArrayList<String>();
	private String defaultPrefix;

	@Inject
	private GaeGdataSpreadsheetImporter(Logger log, Queryer queryer, SpreadsheetService spreadsheetService) {
		this.queryer = queryer;
		this.spreadsheetService = spreadsheetService;
		this.log = log;
	}
	
	@Override
	public String importSpreadsheet(String worksheetFeedUrl) throws IOException, ServiceException {
//		URL worksheetFeedUrlUrl = spreadsheetEntry.getWorksheetFeedUrl();
		URL worksheetFeedUrlUrl = new URL(worksheetFeedUrl);
		WorksheetFeed worksheetFeed = spreadsheetService.getFeed(worksheetFeedUrlUrl, WorksheetFeed.class);
		int i=0;
		for (WorksheetEntry worksheetEntry : worksheetFeed.getEntries()) {
		  String currTitle = worksheetEntry.getTitle().getPlainText();
		  log.info("Import worksheet # " + i + ": Title=" + currTitle + "...");
		  if (i==0) {
			  importNamespacesFromWorksheet(worksheetEntry.getCellFeedUrl().toExternalForm());
		  } else {
			  String worsheetName = worksheetEntry.getTitle().getPlainText();
			  importDataFromWorksheet(worsheetName, worksheetEntry.getCellFeedUrl().toExternalForm());
		  }
		  i++;
		}
		return SUCCESS;
	}

	@Override
	public String importDataFromWorksheet(String classUri, String cellFeedUrl) throws IOException, ServiceException {
		URL cellFeedUrlUrl = new URL(cellFeedUrl);
		CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrlUrl, CellFeed.class);
		System.out.println("CellFeed: URL=" + cellFeedUrl);
		int lastCol = 0;
		int lastRow = 0;
		Entity entity = null;
		for (CellEntry cellEntry : cellFeed.getEntries()) {
			int col = cellEntry.getCell().getCol();
			int row = cellEntry.getCell().getRow();
			
			String shortObjectUri = cellEntry.getCell().getValue();
			String objectUri = getUri(shortObjectUri);
			
			if (row == 1) {
				headerUris.add(col-1, objectUri);
				continue;
			}
			boolean isNewRow = false;
			if (row != lastRow) isNewRow = true;
			
			if (isNewRow) {
				//store last entity
				log.info("created Entity: " + entity);
				
				//create new entity
				entity = new Entity(classUri, objectUri);
			} else {
//				String cellTitle = getHeader(col);
				entity.setProperty(headerUris.get(col-1), getCellValues(cellEntry.getPlainTextContent()));
			}
//			log.info(cellEntry.getTitle().getPlainText() + "=" + cellEntry.getPlainTextContent());
//			String shortId = cellEntry.getId().substring(cellEntry.getId().lastIndexOf('/') + 1);
//			log.info(" -- Cell(" + shortId + "/" + cellEntry.getTitle().getPlainText()
//		      + ") formula(" + cellEntry.getCell().getInputValue() + ") numeric("
//		      + cellEntry.getCell().getNumericValue() + ") value("
//		      + cellEntry.getCell().getValue() + ")");
			lastCol = col;
			lastRow = row;
		}
		return SUCCESS;
	}

	private Object getCellValues(String plainTextContent) {
		if (plainTextContent == null) return null;
		if (! plainTextContent.contains("\\n")) {
			return getCellValue(plainTextContent);
		}
		return "multiple lines...";
	}

	private Object getCellValue(String plainTextContent) {
		if (isShortUri(plainTextContent)) return "uri: " + plainTextContent;
		if (isLocalizedString(plainTextContent)) return "localized string: " + plainTextContent;
		return "unlocalized string:" + plainTextContent;
	}

	public static boolean isLocalizedString(String string) {
		return string.matches("[\\w\\-]{2,5}=.*");
	}

	private String getUri(String unknownUri) {
		if (isShortUri(unknownUri)) {
			String qnamePrefix = unknownUri.split(":")[0].trim();
			String qnameSuffix = unknownUri.substring(unknownUri.indexOf(":")).trim();
			return createUri(getPrefix(qnamePrefix), qnameSuffix);
		} else {
			return createUri(getDefaultPrefix(), unknownUri);
		}
	}

	/**
	 * Get the full prefix as a URI.  If the prefix is recognized from the prefixes worksheet, use that
	 * If not, then use the default prefix appended with the provided string
	 * @param qnamePrefix
	 * @return
	 */
	private String getPrefix(String qnamePrefix) {
		String prefix = prefixes.get(qnamePrefix);
		if (prefix != null) return prefix;
		prefix = getDefaultPrefix() + qnamePrefix + "/";
		return prefix;
	}

	private String getDefaultPrefix() {
		return defaultPrefix;
	}

	public static String createUri(String prefix, String suffix) {
		return prefix + suffix;
	}

	public static boolean isShortUri(String unknownUri) {
		if (unknownUri==null) return false;
		
		String regex = "([\\w\\-]+):([\\w\\-]+)";
		return unknownUri.matches(regex);
	}

	@Override
	public String importNamespacesFromWorksheet(String cellFeedUrl)
			throws IOException, ServiceException {
		//reinitialize the prefixes
		defaultPrefix = "";
		prefixes = new HashMap<String, String>();
		
		URL cellFeedUrlUrl = new URL(cellFeedUrl);
		CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrlUrl, CellFeed.class);
		System.out.println("CellFeed: URL=" + cellFeedUrl);
		String qnamePrefix = "";
		
		for (CellEntry cellEntry : cellFeed.getEntries()) {
			int col = cellEntry.getCell().getCol();
			int row = cellEntry.getCell().getRow();
			//skip the first row
			if (row==1) continue;
			if (col==1) {
				qnamePrefix = cellEntry.getPlainTextContent();
			} else if (col==2) {
				String uriPrefix = cellEntry.getPlainTextContent();
				prefixes.put(qnamePrefix, uriPrefix);
				
				if (defaultPrefix.length()==0) {
					defaultPrefix = uriPrefix;
					log.info("Stored default prefix=" + uriPrefix);
				}
				log.info("Stored prefix mapping: " + qnamePrefix + "=" + uriPrefix);
			}
		}
		return SUCCESS;
	}
}
