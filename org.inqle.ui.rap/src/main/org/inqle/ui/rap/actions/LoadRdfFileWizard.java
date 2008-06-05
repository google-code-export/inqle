/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.File;

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
import org.inqle.ui.rap.tree.parts.ModelPart;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class LoadRdfFileWizard extends Wizard {

	//private final File tempDir = Persister.getTempDirectory();
	private Connection connection = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(LoadRdfFileWizard.class);
	Composite composite;
	private Model modelToLoad = null;
	private String defaultUri = RDF.INQLE;
	
	private ModelPart modelPart = null;
	//LoadFromPage loadFromPage = new LoadFromPage("Location of Data");
	LoadFilePage loadFilePage = new LoadFilePage("Load Data from Local File");
	
	Upload filePath = null;
	/*
	 UploadAdapter uploadAdapter = new UploadAdapter() {
  	public void uploadFinished(final UploadEvent uploadEvent) {
  		log.info("uploadFinished() received event " + uploadEvent);
      if (uploadEvent.isFinished() && uploadEvent.getUploadedTotal() > 0) {
        String uploadedFileName = filePath.getLastFileUploaded();
        log.info("Uploaded file " + uploadedFileName);
        File tempDir = new File ( System.getProperty(JAVA_TEMP_DIR_PROPERTY));
        File uploadedFile = new File (tempDir, uploadedFileName);
        //File uploadedFile = new File(uploadedFileName);
        importFile(uploadedFile.getName());
      }
  	}
  };
	*/
	/**
	 * This generates the wizard page for creating a database connection
	 * @author David Donohue
	 * Feb 8, 2008
	 */
//	class LoadFromPage extends WizardPage {
//		Button fileRadioButton = null;
//		Button webRadioButton = null;
//		
//		LoadFromPage(String pageName) {
//			super(pageName);
//		}
//		
//		public void createControl(Composite pageParent) {
//			
//			composite = new Composite(pageParent, SWT.NONE);
//	    // create the desired layout for this wizard page
//			GridLayout gl = new GridLayout(2, false);
//			composite.setLayout(gl);
//	    
//	    //create the form
//			GridData gridData;
//			
//			new Label (composite, SWT.NONE).setText("Load data from");	
//			fileRadioButton = new Button(composite, SWT.RADIO);
//	    fileRadioButton.setText("Local file");
//	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
//	    fileRadioButton.setLayoutData(gridData);
//	    
//	    new Label (composite, SWT.NONE).setText("");	
//	    webRadioButton = new Button(composite, SWT.RADIO);
//	    webRadioButton.setText("Remote file via the web");
//	    gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
//	    webRadioButton.setLayoutData(gridData);
//	    
//	    setControl(composite);
//
//		}
//		
//		public IWizardPage getNextPage(){	
//		   if (fileRadioButton.getSelection()) {
//		       return ((LoadRdfFileWizard)getWizard()).loadFilePage;
//		   }
//		   return ((LoadRdfFileWizard)getWizard()).loadFromPage;
//		}
//
//	}
	
	class LoadFilePage extends WizardPage {
		
		LoadFilePage(String pageName) {
			super(pageName);
		}
		
		public void createControl(Composite pageParent) {
			
			//clear the uploader
			//closeUploader();
			
			//shell = pageParent;
			composite = new Composite(pageParent, SWT.NONE);
	    // create the desired layout for this wizard page
			GridLayout gl = new GridLayout(2, false);
			composite.setLayout(gl);
	    
	    //create the form
			GridData gridData;
			
			new Label (composite, SWT.NONE).setText("Select file to upload");	
			String servletUrl = RWT.getRequest().getContextPath().trim() + "/upload";
			//String servletUrl = "/rap/upload";
			filePath = new Upload(composite, SWT.NONE, servletUrl, true);
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    filePath.setLayoutData(gridData);
	    log.info("Upload servlet =" + filePath.getServlet());
	    
	    
	    filePath.addUploadListener(new UploadAdapter() {
	    	public void uploadFinished(final UploadEvent uploadEvent) {
	    		log.info("uploadFinished() received event " + uploadEvent);
	        if (uploadEvent.isFinished()) {
	          //String uploadedFileName = filePath.getLastFileUploaded();
	          File uploadedFile = filePath.getLastUploadedFile();
	          //File uploadedFile = new File (tempDir, uploadedFileName);
	          log.info("Uploaded file " + uploadedFile.getAbsolutePath());
	          //File uploadedFile = new File(uploadedFileName);
	          //importFile(uploadedFile.getName());
	          importFile(uploadedFile);
	        }
	    	}
	    });
	    
			
			setControl(composite);
		}

		public Upload getFilePath() {
			return filePath;
		}
	}
	
	
	
	
	
	public LoadRdfFileWizard(ModelPart modelPart,	Connection connection) {
		Persister persister = Persister.getInstance();
		//this.persister = persister;
		this.modelPart = modelPart;
		this.connection = connection;
		this.modelToLoad = persister.getModel(modelPart.getRdbModel());
		//log.info("Temp Dir = " + tempDir.getAbsolutePath() + ": can write? " + tempDir.canWrite());
	}

	@Override
	public void addPages() {
		//addPage(loadFromPage);
		addPage(loadFilePage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		closeUploader();
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
	
//	public void importFile(String fileName) {
//    File file = new File( tempDir, fileName );
//    Loader loader = new Loader(modelToLoad);
//    loader.load(file, defaultUri);
//	}

	/**
	 * Clean up the uploader to prevent javascript errors on repeated calls
	 */
	public void closeUploader() {
		//filePath.removeUploadListener(uploadAdapter);
		if (filePath != null) {
			filePath.dispose();
		}
	}

}
