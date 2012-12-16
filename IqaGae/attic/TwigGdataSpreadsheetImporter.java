package org.inqle.qa.gdata.twig;

import java.io.IOException;
import java.util.logging.Logger;

import org.inqle.qa.Queryer;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.ServiceException;
import com.google.inject.Inject;

public class TwigGdataSpreadsheetImporter implements GdataSpreadsheetImporter {

	@Inject
	private TwigGdataSpreadsheetImporter(Logger log, Queryer queryer, SpreadsheetService spreadsheetService) {
		this.queryer = queryer;
		this.spreadsheetService = spreadsheetService;
		this.log = log;
	}
	
	@Override
	public String importDataFromWorksheet(String classUri, String cellFeedUrl)
			throws IOException, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String importSpreadsheet(String worksheetFeedUrl)
			throws IOException, ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
