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
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.load.Loader;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.pages.CsvDisplayPage;
import org.inqle.ui.rap.pages.CsvPredicatesPage;
import org.inqle.ui.rap.pages.LoadFilePage;
import org.inqle.ui.rap.pages.CsvSubjectPage;
import org.inqle.ui.rap.tree.parts.ModelPart;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class LoadCsvFileWizard extends Wizard {

	//private final File tempDir = Persister.getTempDirectory();
	//private Persister persister;
	static Logger log = Logger.getLogger(LoadCsvFileWizard.class);
	Composite composite;
	private Model modelToLoad = null;
	private String defaultUri = RDF.INQLE;
	
	LoadFilePage loadCsvFilePage = new LoadFilePage("Load Data from CSV File");
	
	private CsvImporter csvImporter;
	private ModelPart modelPart;
	private Connection connection;
	
	public LoadCsvFileWizard(ModelPart modelPart,	Connection connection) {
		Persister persister = Persister.getInstance();
		this.modelPart = modelPart;
		this.connection = connection;
		this.modelToLoad = persister.getModel(modelPart.getRdbModel());
	}

	@Override
	public void addPages() {
		loadCsvFilePage = new LoadFilePage("Specify the delimited text file to load.");
		addPage(loadCsvFilePage);
		
		CsvDisplayPage csvDisplayPage = new CsvDisplayPage("View data to be imported.", null);
		addPage(csvDisplayPage);
		
		CsvSubjectPage csvSubjectPage = new CsvSubjectPage("Specify info about the experiment subject.", null);
		addPage(csvSubjectPage);
		
		CsvPredicatesPage csvPredicatesPage = new CsvPredicatesPage("Specify the URI of a predicate representing each column.", null);
		addPage(csvPredicatesPage);
		
		//TODO add a page to capture any other descriptive info, e.g. 
		//e.g. info describing the subjects
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		//closeUploader();
		//close wizard regardless
		return true;
	}
	
	public void importFile(File file) {
		PopupDialog popup = new PopupDialog(getShell(), SWT.NONE, true, false, false, false, "Loading Data...", "Loading from file " + file.getName() + "..." );
		popup.open();
		log.info("Rendered popup");
    Loader loader = new Loader(modelToLoad);
    boolean success = loader.load(file, defaultUri);
    popup.close();
    if (success) {
    	if (loader.getCountLoaded() == 0) {
    		MessageDialog.openWarning( getShell(), "Loaded no data", "Successfully processed file " + file.getName() + ", however imported no records.\nPerhaps this file was already loaded into this dataset."); 
    	} else {
    		MessageDialog.openInformation( getShell(), "Success loading data", "Successfully loaded " + loader.getCountLoaded() + " statements from file " + file.getName()); 
    	}
    } else {
    	String errorMessage = "Unable to load data from file " + file.getName();
    	if (loader.getError() != null) {
    		errorMessage +=  "\n" + loader.getError().getMessage();
    	}
    	MessageDialog.openError(getShell(), "Error loading data", errorMessage);
    }
    log.info("Success? " + success);
    
    //if (popup != null) popup.close();
	}

	public CsvImporter getCsvImporter() {
		if (csvImporter == null) {
			refreshCsvImporter();
		}
		return csvImporter;
	}

	public void refreshCsvImporter() {
		if (loadCsvFilePage.getUploadedFile() == null) {
			log.info("loadCsvFilePage.getUploadedFile()=null");
			return;
		}
		try {
			log.info("Get uploaded file...");
			File uploadedFile = loadCsvFilePage.getUploadedFile();
			log.info("Retrieve CSV importer...");
			//csvImporter = new CsvImporter(new FileInputStream(uploadedFile));
			csvImporter = new CsvImporter(uploadedFile);
		} catch (Exception e) {
			log.error("Unable to get uploaded file: " + loadCsvFilePage.getUploadedFile());
			//leave as null
		}
	}

	public void setCsvImporter(CsvImporter csvImporter) {
		this.csvImporter = csvImporter;
	}

	public void closeUploader() {
		loadCsvFilePage.closeUploader();
	}
	
//	public void importFile(String fileName) {
//    File file = new File( tempDir, fileName );
//    Loader loader = new Loader(modelToLoad);
//    loader.load(file, defaultUri);
//	}



}
