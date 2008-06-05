package org.inqle.ui.rap.pages;

import java.io.File;

import org.apache.log4j.Logger;
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

public class LoadFilePage extends DynaWizardPage {
	
	private static final Logger log = Logger.getLogger(LoadFilePage.class);

	private Composite composite;
	private Upload uploadWidget;
	private File uploadedFile;
	
	public LoadFilePage(String pageName) {
		super(pageName, null);
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
		uploadWidget = new Upload(composite, SWT.NONE, servletUrl, true);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    uploadWidget.setLayoutData(gridData);
    
    uploadWidget.addUploadListener(new UploadAdapter() {

			public void uploadFinished(final UploadEvent uploadEvent) {
    		log.info("uploadFinished() received event " + uploadEvent);
        if (uploadEvent.isFinished()) {
          //String uploadedFileName = uploadWidget.getLastFileUploaded();
          uploadedFile = uploadWidget.getLastUploadedFile();
          log.info("Uploaded file " + uploadedFile.getAbsolutePath());
        }
    	}
    });
    
		
		setControl(composite);
	}

	public Upload getUploadWidget() {
		return uploadWidget;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void closeUploader() {
		uploadWidget.dispose();
	}

	@Override
	public void addElements(Composite composite) {
		// TODO Auto-generated method stub
		
	}
}
