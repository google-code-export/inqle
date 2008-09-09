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
import org.inqle.ui.rap.csv.CsvReader;
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
 * 
 * Now using FileDataImporterWizard
 */
@Deprecated
public class LoadCsvFileWizard extends DynaWizard implements ICsvReaderWizard {

	public LoadCsvFileWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
		log.info("CCCCCCCCCCCCCreating new LoadCsvFileWizard");
		// TODO Auto-generated constructor stub
	}

	//private final File tempDir = Persister.getTempDirectory();
	//private Persister persister;
	static Logger log = Logger.getLogger(LoadCsvFileWizard.class);
	Composite composite;
	//private Model modelToLoad = null;
//	private String defaultUri = RDF.INQLE;
	
	LoadFilePage loadFilePage;
	
	private CsvImporter csvImporter;
//	private ModelPart modelPart;
//	private Connection connection;
	private CsvSubjectPage csvSubjectPage;
	private CsvPredicatesPage csvPredicatesPage;
	
//	public LoadCsvFileWizard(ModelPart modelPart,	Connection connection) {
//		Persister persister = Persister.getInstance();
//		this.modelPart = modelPart;
//		this.connection = connection;
//		this.modelToLoad = persister.getModel(modelPart.getRdbModel());
//	}

	@Override
	public void addPages() {
		loadFilePage = new LoadFilePage("Specify the delimited text file to load.");
		addPage(loadFilePage);
		
		CsvDisplayPage csvDisplayPage = new CsvDisplayPage("View data to be imported.", null);
		addPage(csvDisplayPage);
		
		csvSubjectPage = new CsvSubjectPage("Specify info about the experiment subject.", null);
		addPage(csvSubjectPage);
		
		csvPredicatesPage = new CsvPredicatesPage("Specify the URI of a predicate representing each column.", null);
		addPage(csvPredicatesPage);
		
		//TODO add a page to capture any other descriptive info, e.g. 
		//e.g. info describing the subjects
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		//show the "importing..." dialog
		PopupDialog popup = new PopupDialog(getShell(), SWT.NONE, false, false, false, false, "Loading Data...", "Loading from file " + csvImporter.getCsvReader().getFile().getName() + "..." );
		popup.open();
		
		//prepare the CsvImporter...
		csvImporter.setIdType(csvSubjectPage.getIdTypeIndex());
		csvImporter.setSubjectIndex(csvSubjectPage.getSubjectColumnIndex());
		csvImporter.setSubjectPrefix(csvSubjectPage.getSubjectPrefix());
		csvImporter.setSubjectClassUri(csvSubjectPage.getSubjectClassUri());
		csvImporter.setColumnPredicateUris(csvPredicatesPage.getPredicateUris());
		
		//do the import
		boolean success = csvImporter.saveStatements(saveToModel);
		
		//close the "importing..." dialog
    popup.close();
    
    //show success
    if (success) {
    	if (csvImporter.getCountSavedStatements() == 0) {
    		MessageDialog.openWarning( getShell(), "Loaded no data", "Successfully processed file " + csvImporter.getCsvReader().getFile().getName() + ", however imported no records.\nPerhaps this file was already loaded into this dataset."); 
    	} else {
    		MessageDialog.openInformation( getShell(), "Success loading data", "Successfully loaded " + csvImporter.getCountSavedStatements() + " statements, " + csvImporter.getCountSavedRows() + " rows, from file " + csvImporter.getCsvReader().getFile().getName()); 
    	}
    } else {
    	String errorMessage = "Unable to load data from file " + csvImporter.getCsvReader().getFile().getName();
    	if (csvImporter.getError() != null) {
    		errorMessage +=  "\nError=" + csvImporter.getError().getMessage();
    	}
    	MessageDialog.openError(getShell(), "Error loading data", errorMessage);
    }
    log.info("Success? " + success);
    
		//prepare to be closed...
		prepareForClose();
		//close wizard regardless
		return true;
	}


	public CsvReader getCsvReader() {
		if (csvImporter == null) {
			log.info("RRRRRRRRRRefreshing csvImporter in LoadCsvFileWizard");
			refreshCsvReader();
		}
		return csvImporter.getCsvReader();
	}

	public void refreshCsvReader() {
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

	public void setCsvImporter(CsvImporter csvImporter) {
		this.csvImporter = csvImporter;
	}

//	public void closeUploader() {
//		loadFilePage.closeUploader();
//	}
	
	@Override
	public boolean canFinish() {
		//TODO test that prefix & subjectclass are URIs
		try {
			if (csvSubjectPage == null || csvSubjectPage.getIdTypeIndex() < 0) {
				return false;
			}
			if (csvSubjectPage.getIdTypeIndex() == CsvImporter.ID_TYPE_CELL_VALUE && csvSubjectPage.getSubjectColumnIndex() < 0) {
				return false;
			}
			if (csvSubjectPage.getSubjectPrefix() == null || csvSubjectPage.getSubjectPrefix().length() == 0) {
				return false;
			}
			if (csvSubjectPage.getSubjectClassUri() == null || csvSubjectPage.getSubjectClassUri().length() == 0) {
				return false;
			}
			if (csvPredicatesPage == null || csvPredicatesPage.getPredicateUris() == null || csvPredicatesPage.getPredicateUris().size() == 0) {
				return false;
			}
		} catch (Exception e) {
			log.error("Error validating wizard", e);
			return false;
		}
		return true;
	}
	
	@Override
	public void prepareForClose() {
		//log.info("Disposing of uploaderWidget=" + uploaderWidget);
		if (loadFilePage != null) {
			loadFilePage.closeUploader();
		}
		if (csvImporter != null) {
			csvImporter.cleanUp();
		}
	}

	public CsvImporter getCsvImporter() {
		return csvImporter;
	}
}
