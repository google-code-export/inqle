package org.inqle.ui.rap.actions;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.widgets.Upload;
import org.eclipse.rwt.widgets.UploadAdapter;
import org.eclipse.rwt.widgets.UploadEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;


/**
 * File Upload Dialog
 * @author gnindl
 */
@Deprecated
public class UploadDialog extends Dialog {

  private Upload upload;
  private FileUploadAdapter uploadAdapter;
  
  private String title;
  private boolean showProgress;
  
  private File uploadedFile;
  private String filePath;
  private String uploadInfo;
  
 
  public UploadDialog( final Shell parent,
          			   final String title ) {
	  this( parent, title, true );
  }

  public UploadDialog( final Shell parent,
                      final String title,
                      final boolean showProgress ) 
  {
    super( parent );
    this.title = title;
    this.showProgress = showProgress;
  }
  
  protected void createButtonsForButtonBar( final Composite parent ) {
  }  
  
  protected Control createDialogArea( final Composite parent ) {
	   upload = new Upload( parent, SWT.NONE, null, showProgress );
	   upload.setLayoutData( new GridData( 300, SWT.DEFAULT ) );
	   uploadAdapter = new FileUploadAdapter();
	   upload.addUploadListener( uploadAdapter ); 
    return parent;
  }
  
  protected boolean canHandleShellCloseEvent() {
	  performFinish();
	  return true;
  }
  
  protected void configureShell( final Shell shell ) {
    super.configureShell( shell ); 
    if ( title != null ) {
      shell.setText( title );
    }
  }
  
  private boolean performFinish() {
	  upload.removeUploadListener(uploadAdapter);
      upload.dispose();
      close();
      return true;
  }
  
  
   
  public String getFilePath() {
    return filePath;
  }

  public String getUploadInfoDetail() {
	return uploadInfo;
  }
  
  public File getUploadedFile() {
	  return uploadedFile;
  }
  
  private class FileUploadAdapter extends UploadAdapter {
	  public void uploadFinished( final UploadEvent event ) {
	       if( event.isFinished() ) {
	         uploadedFile = new File( upload.getLastFileUploaded() );
	         
	         //provide detail upload information
	         uploadInfo = "File sucessfully uploaded!\r\n"
	        		 				+"Absolute Path: "+uploadedFile.getAbsoluteFile()+"\r\n"
	        		 				+"Name: "+uploadedFile.getName()+"\r\n"
	        		 				+"Total File size: " +event.getUploadedTotal()+"\r\n"
	        		 				+"Parcial size: "+event.getUploadedParcial();
	         
	         filePath = uploadedFile.getName();
	       }
	       else {
	    	   uploadInfo = "File couldn't be uploaded yet";
	       }
	       MessageDialog.openInformation(getShell(), "File successfully uploaded", uploadInfo);
	       performFinish();
	     }
  }
}