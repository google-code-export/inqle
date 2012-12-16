/**
 * 
 */
package org.inqle.ui.rap.actions;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.widgets.Upload;
import org.eclipse.rwt.widgets.UploadAdapter;
import org.eclipse.rwt.widgets.UploadEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.inqle.core.util.InqleInfo;
import org.inqle.data.rdf.jena.Jenamodel;
import org.inqle.data.rdf.jena.load.Loader;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.cache.CacheTool;
import org.inqle.rdf.RDF;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author David Donohue
 * Feb 8, 2008
 * @see http://jena.sourceforge.net/DB/index.html
 */
public class LoadRdfFileWizard extends DynaWizard {

	private Model saveToModel;
	public LoadRdfFileWizard(Model saveToModel, Shell shell) {
		super(shell);
		this.saveToModel = saveToModel;
		// TODO Auto-generated constructor stub
	}

	//private final File tempDir = Persister.getTempDirectory();
//	private SDBDatabase sDBDatabase = null;
	//private Persister persister;
	static Logger log = Logger.getLogger(LoadRdfFileWizard.class);
	Composite composite;
	//private Model modelToLoad = null;
	private String defaultUri = RDF.INQLE;
	private Jenamodel datamodel;
	
//	private ModelPart modelPart = null;
	//LoadFromPage loadFromPage = new LoadFromPage("Location of Data");
	LoadFilePage loadFilePage = new LoadFilePage("Load Data from Local File");
	
	Upload uploaderWidget = null;
	/*
	 UploadAdapter uploadAdapter = new UploadAdapter() {
  	public void uploadFinished(final UploadEvent uploadEvent) {
  		log.info("uploadFinished() received event " + uploadEvent);
      if (uploadEvent.isFinished() && uploadEvent.getUploadedTotal() > 0) {
        String uploadedFileName = uploaderWidget.getLastFileUploaded();
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
	 * This generates the wizard page for creating a database sDBDatabase
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
			uploaderWidget = new Upload(composite, SWT.NONE, servletUrl, true);
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
	    uploaderWidget.setLayoutData(gridData);
	    log.info("Upload servlet =" + uploaderWidget.getServlet());
	    
	    
	    uploaderWidget.addUploadListener(new UploadAdapter() {
	    	public void uploadFinished(final UploadEvent uploadEvent) {
	    		log.info("uploadFinished() received event " + uploadEvent);
	        if (uploadEvent.isFinished()) {
	          //String uploadedFileName = uploaderWidget.getLastFileUploaded();
	          File uploadedFile = uploaderWidget.getLastUploadedFile();
	          //File uploadedFile = new File (tempDir, uploadedFileName);
	          if (uploadedFile == null) {
	          	log.info("Unable to upload file: uploadedFile = null");
	          } else {
	          	log.info("Uploaded file " + uploadedFile.getAbsolutePath());
	          	importFile(uploadedFile);
	          }
	        }
	    	}
	    });
	    
			
			setControl(composite);
		}

		public Upload getUploaderWidget() {
			return uploaderWidget;
		}
	}
	
	
	
	
	
//	public LoadRdfFileWizard(ModelPart modelPart,	SDBDatabase sDBDatabase) {
//		Persister persister = Persister.getInstance();
//		//this.persister = persister;
//		this.modelPart = modelPart;
//		this.connection = sDBDatabase;
//		this.modelToLoad = persister.getModel(modelPart.getRdbModel());
//		//log.info("Temp Dir = " + tempDir.getAbsolutePath() + ": can write? " + tempDir.canWrite());
//	}

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
		prepareForClose();
		//close wizard regardless
		return true;
	}
	
	public void importFile(File file) {
//		PopupDialog popup = new PopupDialog(getShell(), SWT.NONE, true, false, false, false, "Loading Data...", "Loading from file " + file.getName() + "..." );
//		popup.open();
		
		MessageDialog waitingDialog = new MessageDialog(
				getShell(), 
				"Loading Data...", 
				 null, 
				 "Loading from file " + file.getName() + "...", 
				 MessageDialog.NONE,
				  new String [] {  }, 0);
		waitingDialog.setBlockOnOpen(false);
		waitingDialog.open();
		
//		log.info("Rendered popup");
    Loader loader = new Loader(saveToModel);
    boolean success = loader.load(file, defaultUri);
    waitingDialog.close();
    if (success) {
    	log.info("Success loading RDF file.");
    	CacheTool.invalidateDataCache(InqleInfo.CORE_DATABASE_ID, datamodel.getId());
    	if (loader.getCountLoaded() == 0) {
    		MessageDialog.openWarning( getShell(), "Loaded no data", "Successfully processed file " + file.getName() + ", however imported no records.\nPerhaps this file was already loaded into this datamodel."); 
    	} else {
    		
    		if (datamodel != null) {
    			log.info("Flushing text index...");
    			//flush any text indexes for the datamodel
    			Persister persister = Persister.getInstance();
    			persister.flushIndexes(datamodel);
    			log.info("Finished flushing text index.");
    		}
    		
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

	@Override
	public void prepareForClose() {
		log.info("Disposing of uploaderWidget=" + uploaderWidget);
		if (uploaderWidget != null) {
			uploaderWidget.dispose();
		}
	}

	public Jenamodel getDatamodel() {
		return datamodel;
	}

	public void setDatamodel(Jenamodel namedModel) {
		this.datamodel = namedModel;
	}
	
	/**
	 * Clean up the uploader to prevent javascript errors on repeated calls
	 */
//	public void closeUploader() {
//		//uploaderWidget.removeUploadListener(uploadAdapter);
//		log.info("Disposing of uploaderWidget=" + uploaderWidget);
//		if (uploaderWidget != null) {
//			uploaderWidget.dispose();
//		}
//	}

}
