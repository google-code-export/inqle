package org.inqle.qa.gdata.gae;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.inqle.qa.Queryer;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;
import org.mortbay.log.Log;

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
		  log.fine("Import worksheet # " + i + ": Title=" + currTitle + "...");
		  importWorksheet(worksheetEntry.getCellFeedUrl().toExternalForm());
		  i++;
		}
		return SUCCESS;
	}

	@Override
	public String importWorksheet(String cellFeedUrl) throws IOException, ServiceException {
		URL cellFeedUrlUrl = new URL(cellFeedUrl);
		CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrlUrl, CellFeed.class);
		System.out.println("CellFeed: URL=" + cellFeedUrl);
		int lastCol = 0;
		int lastRow = 0;
		for (CellEntry cellEntry : cellFeed.getEntries()) {
			int col = cellEntry.getCell().getCol();
			int row = cellEntry.getCell().getRow();
		  log.fine(cellEntry.getTitle().getPlainText() + "=" + cellEntry.getPlainTextContent());
		  String shortId = cellEntry.getId().substring(cellEntry.getId().lastIndexOf('/') + 1);
		  log.fine(" -- Cell(" + shortId + "/" + cellEntry.getTitle().getPlainText()
		      + ") formula(" + cellEntry.getCell().getInputValue() + ") numeric("
		      + cellEntry.getCell().getNumericValue() + ") value("
		      + cellEntry.getCell().getValue() + ")");
		}
		return SUCCESS;
	}
}
