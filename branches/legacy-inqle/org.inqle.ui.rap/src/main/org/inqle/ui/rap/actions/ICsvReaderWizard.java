package org.inqle.ui.rap.actions;

import org.inqle.ui.rap.csv.CsvReader;

public interface ICsvReaderWizard {

	public CsvReader getCsvReader();

	public void refreshCsvReader();
}
