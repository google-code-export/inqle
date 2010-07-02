package org.inqle.qa.gdata.gae;

import org.inqle.qa.Queryer;
import org.inqle.qa.gdata.GdataSpreadsheetImporter;

import com.google.inject.Inject;

public class GaeGdataSpreadsheetImporter implements GdataSpreadsheetImporter {

	private Queryer queryer;

	@Inject
	public GaeGdataSpreadsheetImporter(Queryer queryer) {
		this.queryer = queryer;
	}
	
	@Override
	public String importData(String worksheetFeedUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
