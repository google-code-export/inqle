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
public class FileDataImporterWizard extends DynaWizard {

	public FileDataImporterWizard(Model saveToModel, Shell shell) {
		super(saveToModel, shell);
		log.info("CCCCCCCCCCCCCreating new FileDataImporterWizard");
		// TODO Auto-generated constructor stub
	}

	static Logger log = Logger.getLogger(FileDataImporterWizard.class);
	Composite composite;
	
	private LoadFilePage loadFilePage;
	private LookupRdfPage lookupRdfPage;
	

	@Override
	public void addPages() {
		lookupRdfPage = new LookupRdfPage("Type of Subject", "Find and select the type of subject that this data is about.", null);
		addPage(lookupRdfPage);
		
		loadFilePage = new LoadFilePage("Specify the delimited text file to load.");
		addPage(loadFilePage);
		
		
		
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



}
