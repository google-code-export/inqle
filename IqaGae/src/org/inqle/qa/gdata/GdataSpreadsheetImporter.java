package org.inqle.qa.gdata;

import java.io.IOException;

import com.google.gdata.util.ServiceException;

public interface GdataSpreadsheetImporter {

	String importSpreadsheet(String worksheetFeedUrl) throws IOException, ServiceException;

	String importWorksheet(String cellFeedUrl) throws IOException, ServiceException;
}
