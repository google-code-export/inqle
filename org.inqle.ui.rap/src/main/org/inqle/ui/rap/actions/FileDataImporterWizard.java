/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.pages.AddSubjectOrFinishPage;
import org.inqle.ui.rap.pages.AddSubjectPage;
import org.inqle.ui.rap.pages.CsvDisplayPage;
import org.inqle.ui.rap.pages.DateTimeMapperPage;
import org.inqle.ui.rap.pages.InfoPage;
import org.inqle.ui.rap.pages.LoadFilePage;
import org.inqle.ui.rap.pages.RowSubjectPropertyMappingsPage;
import org.inqle.ui.rap.pages.RowSubjectPropertyValuesPage;
import org.inqle.ui.rap.pages.SubjectPropertyValuesPage;
import org.inqle.ui.rap.pages.TableSubjectPropertyMappingsPage;
import org.inqle.ui.rap.pages.TableSubjectPropertyValuesPage;
import org.inqle.ui.rap.table.RowSubjectClassPage;
import org.inqle.ui.rap.table.SubjectClassPage;
import org.inqle.ui.rap.table.TableSubjectClassPage;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class FileDataImporterWizard extends DynaWizard implements ICsvImporterWizard {

	public FileDataImporterWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
	}

	private static Logger log = Logger.getLogger(FileDataImporterWizard.class);
	Composite composite;
	
	private LoadFilePage loadFilePage;
	private CsvImporter csvImporter;
	
	private AddSubjectPage addSubjectPage;
	
	//each time a new subject (of either type) is added, each of these 5 lists is appended with 
	//a new page of its type.
//	private List<ASubjectClassPage> subjectClassPages = new ArrayList<ASubjectClassPage>();
//	private List<ASubjectUriPage> subjectUriPages = new ArrayList<ASubjectUriPage>();
//	private List<ASubjectPropertyValuesPage> subjectPropertyValuesPages = new ArrayList<ASubjectPropertyValuesPage>();;
//	private List<ASubjectPropertyMappingsPage> subjectPropertyMappingsPages = new ArrayList<ASubjectPropertyMappingsPage>();;
//	private List<AddSubjectPage> addSubjectPages = new ArrayList<AddSubjectPage>();
	
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
		
		addSubjectPage = new AddSubjectPage();
		addPage(addSubjectPage);
		
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
		return false;
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

	public AddSubjectOrFinishPage getLastAddSubjectOrFinishPage() {
		//loop back thru the wizard, and get the first instance of AddSubjectPage which is not the very first
		IWizardPage[] wizardPages = getPages();
		for (int i=wizardPages.length; i>0; i--) {
			IWizardPage page = wizardPages[i];
			if (page instanceof AddSubjectOrFinishPage) {
				return (AddSubjectOrFinishPage)page;
			}
		}
		return null;
	}
	
//	public void disableLastAddSubjectPage() {
//		AddSubjectPage lastAddSubjectPage = getLastAddSubjectPage();
//		if (lastAddSubjectPage != null) {
//			lastAddSubjectPage.disableForm();
//		}
//	}
	
	public void addTableSubjectPages() {
		TableSubjectClassPage subjectClassPage = new TableSubjectClassPage();
		addPage(subjectClassPage);
		TableSubjectUriPage subjectUriPage = new TableSubjectUriPage();
		addPage(subjectUriPage);
		TableSubjectPropertyValuesPage propertyValuesPage = new TableSubjectPropertyValuesPage();
		addPage(propertyValuesPage);
		TableSubjectPropertyMappingsPage propertyMappingsPage = new TableSubjectPropertyMappingsPage();
		addPage(propertyMappingsPage);
		AddSubjectOrFinishPage addSubjectOrFinishPage = new AddSubjectOrFinishPage();
		addPage(addSubjectOrFinishPage);
		getContainer().updateButtons();
	}

	public void addRowSubjectPages() {
		RowSubjectClassPage subjectClassPage = new RowSubjectClassPage();
		addPage(subjectClassPage);
		RowSubjectUriPage subjectUriPage = new RowSubjectUriPage();
		addPage(subjectUriPage);
		RowSubjectPropertyValuesPage propertyValuesPage = new RowSubjectPropertyValuesPage();
		addPage(propertyValuesPage);
		RowSubjectPropertyMappingsPage propertyMappingsPage = new RowSubjectPropertyMappingsPage();
		addPage(propertyMappingsPage);
		AddSubjectOrFinishPage addSubjectOrFinishPage = new AddSubjectOrFinishPage();
		addPage(addSubjectOrFinishPage);
		getContainer().updateButtons();
		
	}

	public void addDoImportPage() {
//		DoImportPage doImportPage = new DoImportPage();
//		addPage(doImportPage);
		getContainer().updateButtons();
	}

	/**
	 * Get the subject class URI of the most recent SubjectClassPage, preceding the one passed 
	 * as an argument
	 * @param pageAfterSubjectClassPage
	 * @return
	 */
	public String getSubjectClassUri(IWizardPage pageAfterSubjectClassPage) {
		IWizardPage thePage = pageAfterSubjectClassPage;
		while (thePage.getPreviousPage() != null) {
			thePage = thePage.getPreviousPage();
			if (thePage instanceof SubjectClassPage) {
				SubjectClassPage subjectClassPage = (SubjectClassPage)thePage;
				return subjectClassPage.getSubjectUri();
			}
		}
		return null;
	}

}
