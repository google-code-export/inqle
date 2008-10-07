package org.inqle.ui.rap.widgets;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jena.uri.UriMapper;

import com.hp.hpl.jena.ontology.OntResource;
 
/**
 * This dialog is intended to be used anywhere in the app, to create new resources 
 * (along with their rdfs:label and rdfs:comment properties).
 * 
 * Upon clicking "Save" and validating the form, the new Individual created here will be 
 * stored as a instance of the OntClass provided
 * @author David Donohue
 * Jul 23, 2008
 */
public class LoginDialog extends Dialog {

	private static final String DEFAULT_TITLE = "Login to your INQLE server";
	private static final String DEFAULT_MESSAGE = null;
	protected Text messageText;
//		private Text labelText;
//		private Text uriText;
//		private Text commentText;
	private Text loginText;
	protected Text passwordText;
	private String message;
	private String title;
	
	private static Logger log = Logger.getLogger(LoginDialog.class);
	
	/**
	 * @param parentShell
	 * @param ontClass upon saving this data, it will be created as a new instance of this ontClass
	 */
//		public AOntResourceDialog(Shell parentShell, OntClass ontClass) {
//      super(parentShell);
//      this.ontClass = ontClass;
//    }
	
	/**
	 * ontClass is the super class, to be used for creating this OntResource
	 */
	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}
	
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    
		Shell shell = parent.getShell();
		shell.setText(getTitle());
		
		if (getMessage() != null) {
			messageText = new Text(container, SWT.WRAP | SWT.READ_ONLY);
			messageText.setText(getMessage());
		}
		Composite formComposite = new Composite(container, SWT.NONE);
		GridLayout formLayout = new GridLayout(2, false);
		formComposite.setLayout(formLayout);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		formComposite.setLayoutData(gridData);
		
		new Label(formComposite, SWT.NONE).setText("User Name");
		loginText = new Text(formComposite, SWT.BORDER);
		loginText.setToolTipText("Use no spaces or special characters.");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		loginText.setLayoutData(gridData);
		loginText.forceFocus();
		
		new Label(formComposite, SWT.NONE).setText("Password");
		passwordText = new Text(formComposite, SWT.BORDER | SWT.PASSWORD);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		passwordText.setLayoutData(gridData);
		
    return container;
  }


	protected void createButtonsForButtonBar(Composite parent) {
      createButton(parent, IDialogConstants.OK_ID, "Login", true);
  }
  
  protected Point getInitialSize() {
      return new Point(300, 200);
  }
  
  protected void okPressed() {
  	
  	super.okPressed();
  }
	
	public void setMessage(String messageString) {
		this.message = messageString;
		if (messageText != null) {
			messageText.setText(messageString);
		}
	}

	public String getMessage() {
		if (message==null) {
			return DEFAULT_MESSAGE;
		}
		return message;
	}

	public String getTitle() {
		if (title==null) {
			return DEFAULT_TITLE;
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUserName() {
		return loginText.getText();
	}
	
	public String getPassword() {
		return passwordText.getText();
	}
}

