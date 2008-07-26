/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.widgets.Upload;
import org.eclipse.rwt.widgets.UploadAdapter;
import org.eclipse.rwt.widgets.UploadEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.load.Loader;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.pages.CsvDisplayPage;
import org.inqle.ui.rap.pages.CsvPredicatesPage;
import org.inqle.ui.rap.pages.InfoPage;
import org.inqle.ui.rap.pages.LoadFilePage;
import org.inqle.ui.rap.pages.CsvSubjectPage;
import org.inqle.ui.rap.table.LookupRdfPage;
import org.inqle.ui.rap.tree.parts.ModelPart;

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
				"",
				"This wizard assists you in importing data from a delimited text " +
				"file such as comma-separated values files, which can be generated from your spreadsheet program." +
				"\n\nThis wizard seeks to capture the most accurate and comprehensive data possible from your" +
				"data file.  Toward this, it will ask you for information about the data file as a whole, " +
				"as well as about each of row of data within the file."
		);
		addPage(firstPage);
		
		loadFilePage = new LoadFilePage("Specify the delimited text file to load.");
		loadFilePage.setDescription("This wizard is capable of importing many different formats of " +
				"delimited text files.  In general, you could save your spreadsheet of data as " +
				"'Comma-Separated Values (CSV)' and then import the CSV file here.");
		addPage(loadFilePage);
		
		CsvDisplayPage csvDisplayPage = new CsvDisplayPage("View data to be imported.", null);
		addPage(csvDisplayPage);
		
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
