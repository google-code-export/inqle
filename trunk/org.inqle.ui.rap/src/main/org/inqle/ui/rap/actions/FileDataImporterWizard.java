/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.pages.CsvDisplayPage;
import org.inqle.ui.rap.pages.DateTimeMapperPage;
import org.inqle.ui.rap.pages.InfoPage;
import org.inqle.ui.rap.pages.LoadFilePage;
import org.inqle.ui.rap.table.LookupRdfPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class FileDataImporterWizard extends DynaWizard implements ICsvImporterWizard {

	public FileDataImporterWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
		log.info("CCCCCCCCCCCCCreating new FileDataImporterWizard");
		// TODO Auto-generated constructor stub
	}

	static Logger log = Logger.getLogger(FileDataImporterWizard.class);
	Composite composite;
	
	private LoadFilePage loadFilePage;
	private LookupRdfPage lookupRdfPage;
	private CsvImporter csvImporter;
	

	@Override
	public void addPages() {
		InfoPage firstPage = new InfoPage(
				"Import Data From Text File",
				"This wizard assists you in importing data from a delimited text file such as " +
				"comma-separated values files (CSV).",
				"Before using this wizard, you should acquire your data into a delimited text \n" +
				"format, such as CSV.  You can generate such a file from your spreadsheet program.\n\n" +
				
				"This wizard captures 2 types of information about your data file:\n" +
				"  * information about the data file as a whole\n" +
				"    e.g. 'All rows of data in this file concern this city.'\n " +
				"  * information about each of row of data within the file.\n" +
				"    e.g. 'Each row of data contains a distinct Weather data measurement.'\n\n" +
				
				"The data is imported into your database in a format called semantic or \n" +
				"Resource Description Framework (RDF) data.  This means that data is imported as \n" +
				"objects.  \n" +
				"  * Each data object has a unique identifier, called a Uniform Resource Identifier (URI).\n" +
				"    URIs look a lot like web addresses (URLs)\n" +
				"    e.g. the person David Donohue is represented by this URI:\n" +
				"    'http://daviddonohue.com/DD'\n" +
				"  * Each data object has a type (or class), which itself has a URI.\n" +
				"    e.g. David Donohue is of type Person, and the type Person has this URI\n" +
				"    http://xmlns.com/foaf/0.1/Person \n" +
				"  * Data objects have Attributes, which are also identified by a URI.\n" +
				"    Attributes have values- either literal values like '42' or other data\n" +
				"    objects, like 'http://daviddonohue.com/DD'." +
				"    For example, the Person type has an attribute\n" +
				"    http://xmlns.com/foaf/0.1/interest \n" +
				"    The value of this attribute is a website which interests the person.\n\n" +
				
				"" +
				""
		);
		addPage(firstPage);
		
		loadFilePage = new LoadFilePage("Specify the delimited text file to load.");
		loadFilePage.setDescription("This wizard is capable of importing many different formats of " +
				"delimited text files.  In general, you could save your spreadsheet of data as " +
				"'Comma-Separated Values (CSV)' and then import the CSV file here.");
		addPage(loadFilePage);
		
		CsvDisplayPage csvDisplayPage = new CsvDisplayPage("View data to be imported.", null);
		addPage(csvDisplayPage);
		
		DateTimeMapperPage measurementDateTimeMapperPage = new DateTimeMapperPage(
				"Specify Date & Time of the Data",
				null);
		measurementDateTimeMapperPage.setDescription("All data pertains to a particular date and time.  " +
				"Specify whether all the rows of data have the same date & time or whether each row has a different date and time.");
		addPage(measurementDateTimeMapperPage);
		
		lookupRdfPage = new LookupRdfPage("Type of Subject", "Find and select the type of subject that this data is about.", null);
		addPage(lookupRdfPage);
		
		
		
		
		
		//TODO add a page to capture any other descriptive info, e.g. 
		//e.g. info describing the subjects
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		//show the "importing..." dialog
//		PopupDialog popup = new PopupDialog(getShell(), SWT.NONE, false, false, false, false, "Loading Data...", "Loading from file " + importer.getFile().getName() + "..." );
//		popup.open();
		
		return true;
	}

//	public void closeUploader() {
//		loadFilePage.closeUploader();
//	}
	
	@Override
	public boolean canFinish() {
		//TODO test that prefix & subjectclass are URIs
		try {
			return false;
		} catch (Exception e) {
			log.error("Error validating wizard", e);
			return false;
		}
	}
	
	@Override
	public void prepareForClose() {
		//log.info("Disposing of uploaderWidget=" + uploaderWidget);
		if (loadFilePage != null) {
			loadFilePage.closeUploader();
		}
	}

	public CsvImporter getCsvImporter() {
		if (csvImporter == null) {
			log.info("RRRRRRRRRRefreshing csvImporter in LoadCsvFileWizard");
			refreshCsvImporter();
		}
		return csvImporter;
	}

	public void refreshCsvImporter() {
		if (loadFilePage.getUploadedFile() == null) {
			log.error("loadFilePage.getUploadedFile()=null");
			return;
		}
		try {
			log.info("Get uploaded file...");
			File uploadedFile = loadFilePage.getUploadedFile();
			log.info("Got file " + uploadedFile + ".  Retrieve CSV importer...");
			//csvImporter = new CsvImporter(new FileInputStream(uploadedFile));
			csvImporter = new CsvImporter(uploadedFile);
		} catch (Exception e) {
			log.error("Unable to get uploaded file: " + loadFilePage.getUploadedFile());
			//leave as null
		}
	}


}
