package org.inqle.ui.rap.file;

import org.inqle.data.rdf.jenabean.mapping.*;
import org.inqle.ui.rap.csv.CsvReader;

public class FileDataImporter {

	private CsvReader csvReader;
	private TableMapping tableMapping;

	public FileDataImporter(CsvReader csvReader, TableMapping tableMapping) {
		this.csvReader = csvReader;
		this.tableMapping = tableMapping;
	}
	
	public void doImport() {
		for (SubjectMapping subjectMapping: tableMapping.getSubjectMappings()) {
			
		}
	}
}
