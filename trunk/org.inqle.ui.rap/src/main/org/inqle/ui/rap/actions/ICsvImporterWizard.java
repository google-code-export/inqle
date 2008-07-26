package org.inqle.ui.rap.actions;

import org.inqle.ui.rap.csv.CsvImporter;

public interface ICsvImporterWizard {

	public CsvImporter getCsvImporter();

	public void refreshCsvImporter();
}
