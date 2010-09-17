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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;
import com.google.inject.Inject;

public class GaeGdataSpreadsheetImporter implements GdataSpreadsheetImporter {

	public static final String SUCCESS = "success";
	private final DatastoreService datastoreService;
	private final SpreadsheetService spreadsheetService;
	private final Logger log;
//	private Map<String, String> prefixes = new HashMap<String, String>();
//	private String defaultPrefix;

	@Inject
	private GaeGdataSpreadsheetImporter(Logger log, DatastoreService datastoreService, SpreadsheetService spreadsheetService) {
		this.datastoreService = datastoreService;
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
		  String worsheetName = worksheetEntry.getTitle().getPlainText();
		  importDataFromWorksheet(worsheetName, worksheetEntry.getCellFeedUrl().toExternalForm());
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
		List<String> headers = new ArrayList<String>();
		
		for (CellEntry cellEntry : cellFeed.getEntries()) {
			int col = cellEntry.getCell().getCol();
			int row = cellEntry.getCell().getRow();
			
//			String cellValue = cellEntry.getCell().getValue();
			String cellText = cellEntry.getPlainTextContent();
			
			//the 1st row contains the headers
			if (row == 1) {
				headers.add(col-1, cellText);
				continue;
			}
			boolean isNewRow = false;
			if (row != lastRow) isNewRow = true;
			
			if (isNewRow) {
				//store last entity, if not null
				if (entity != null) {
					datastoreService.put(entity);
					log.info("stored Entity: " + entity);
				}
				
				//create new entity
				entity = new Entity(classUri, cellText);
			} else {
//				String cellTitle = getHeader(col);
				Object val = getObjectContainingCellValues(cellText, headers.get(col-1), entity.getKey());
				if (val != null) {
					entity.setProperty(headers.get(col-1), val);
				}
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
		
		//store the last entity
		if (entity != null) {
			datastoreService.put(entity);
			log.info("stored Entity: " + entity);
		}
		
		
		return SUCCESS;
	}

	private Object getObjectContainingCellValues(String cellText, String columnTitle, Key parentKey) {
		if (cellText == null) return null;
		if (cellText.indexOf("\n") < 0) {
			if (isShortUri(cellText)) {
				Entity mapping = getMappingEntity(cellText, 1, columnTitle, parentKey);
				log.info("Storing MMMMapping: " + mapping);
				datastoreService.put(mapping);
				return null;
			} else if (isLocalizedString(cellText)) {
				Entity ls = getLocalizedStringEntity(cellText, columnTitle, parentKey);
				datastoreService.put(ls);
				return null;
			} else {
				//cast the text as the appropriate data type
				try {
					Double doubleVal = Double.parseDouble(cellText);
					return doubleVal;
				} catch (NumberFormatException e) {
					return cellText;
				}
				
			}
		} else {
			String[] lines = cellText.split("\\n");
			String line1 = lines[0];
			if (isShortUri(line1)) {
				int i=0;
				for (String line: lines) {
					i++;
					Entity mapping = getMappingEntity(line, i, columnTitle, parentKey);
					datastoreService.put(mapping);
				}
				return null;
			} else if (isLocalizedString(line1)) {
				for (String line: lines) {
					Entity ls = getLocalizedStringEntity(line, columnTitle, parentKey);
					datastoreService.put(ls);
				}
				return null;
			} else {
				List<String> values = new ArrayList<String>();
				for (String line: lines) {
					values.add(line);
				}
				return values;
			}
		}
	}

	private Entity getMappingEntity(String line, int itemNumber, String columnTitle, Key parentKey) {
		String type = getPrefixFromShortUri(line);
		String id = getIdFromShortUri(line);
		Key entityKey = KeyFactory.createKey(type, id);
		Entity mapping = new Entity("Mapping", columnTitle + "/" + line, parentKey);
		mapping.setProperty("entityKey", entityKey.toString());
		mapping.setProperty("kind", type);
		mapping.setProperty("id", id);
		mapping.setProperty("parentProperty", columnTitle);
		mapping.setProperty("iqa_orderBy", itemNumber);
		return mapping;
	}

	private Entity getLocalizedStringEntity(String line, String columnTitle, Key parentKey) {
		String lang = getLangFromLocalizedString(line);
		String text = getTextFromLocalizedString(line);
		Entity ls = new Entity("LocalizedString", columnTitle + "/" + lang, parentKey);
		ls.setProperty("lang", lang);
		ls.setProperty("text", text);
		ls.setProperty("parentProperty", columnTitle);
		return ls;
	}

//	private Entity getLocalizedString(String line, String stringName, Key parentKey) {
//		Entity entity = new Entity("LocalizedString", stringName, parentKey);
//		int equalsPos = line.indexOf("=");
//		entity.setProperty("lang", line.substring(0, equalsPos));
//		entity.setProperty("text", line.substring(equalsPos + 1));
//		return entity;
//	}

//	private Object getCellValue(String plainTextContent) {
//		if (isShortUri(plainTextContent)) return getUri(plainTextContent);
//		if (isLocalizedString(plainTextContent)) {
//			Map<String, Object> cell = return "localized string: " + plainTextContent;
//		}
//		return "unlocalized string:" + plainTextContent;
//	}

	public static boolean isLocalizedString(String string) {
		if (string==null) return false;
		return string.matches("[\\w\\-]{2,5}=.*");
	}
	
	public static String getLangFromLocalizedString(String localizedString) {
		if (localizedString==null) return null;
		return localizedString.substring(0, localizedString.indexOf("=")).trim();
	}
	
	public static String getTextFromLocalizedString(String localizedString) {
		if (localizedString==null) return null;
		return localizedString.substring(localizedString.indexOf("=") + 1).trim();
	}

//	private String getUri(String unknownUri) {
//		if (isShortUri(unknownUri)) {
//			String qnamePrefix = unknownUri.split(":")[0].trim();
//			String qnameSuffix = unknownUri.substring(unknownUri.indexOf(":")).trim();
//			return createUri(getPrefix(qnamePrefix), qnameSuffix);
//		} else {
//			return createUri(getDefaultPrefix(), unknownUri);
//		}
//	}
	
	public static String getIdFromShortUri(String shortUri) {
		String id = shortUri.substring(shortUri.indexOf(":") + 1).trim();
		return id;
	}
	
	public static String getPrefixFromShortUri(String shortUri) {
		String qnamePrefix = shortUri.split(":")[0].trim();
		return qnamePrefix;
	}

	public static boolean isShortUri(String unknownUri) {
	if (unknownUri==null) return false;
	
	String regex = "([\\w\\-]+):([\\w\\-\\/]+)";
	return unknownUri.matches(regex);
}
	
//	/**
//	 * Get the full prefix as a URI.  If the prefix is recognized from the prefixes worksheet, use that
//	 * If not, then use the default prefix appended with the provided string
//	 * @param qnamePrefix
//	 * @return
//	 */
//	private String getPrefix(String qnamePrefix) {
//		String prefix = prefixes.get(qnamePrefix);
//		if (prefix != null) return prefix;
//		prefix = getDefaultPrefix() + qnamePrefix + "/";
//		return prefix;
//	}
//
//	private String getDefaultPrefix() {
//		return defaultPrefix;
//	}
//
//	public static String createUri(String prefix, String suffix) {
//		return prefix + suffix;
//	}

//	@Override
//	public String importNamespacesFromWorksheet(String cellFeedUrl)
//			throws IOException, ServiceException {
//		//reinitialize the prefixes
//		defaultPrefix = "";
//		prefixes = new HashMap<String, String>();
//		
//		URL cellFeedUrlUrl = new URL(cellFeedUrl);
//		CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrlUrl, CellFeed.class);
//		System.out.println("CellFeed: URL=" + cellFeedUrl);
//		String qnamePrefix = "";
//		
//		for (CellEntry cellEntry : cellFeed.getEntries()) {
//			int col = cellEntry.getCell().getCol();
//			int row = cellEntry.getCell().getRow();
//			//skip the first row
//			if (row==1) continue;
//			if (col==1) {
//				qnamePrefix = cellEntry.getPlainTextContent();
//			} else if (col==2) {
//				String uriPrefix = cellEntry.getPlainTextContent();
//				prefixes.put(qnamePrefix, uriPrefix);
//				
//				if (defaultPrefix.length()==0) {
//					defaultPrefix = uriPrefix;
//					log.info("Stored default prefix=" + uriPrefix);
//				}
//				log.info("Stored prefix mapping: " + qnamePrefix + "=" + uriPrefix);
//			}
//		}
//		return SUCCESS;
//	}
}
