package org.inqle.ui.rap.widgets;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.Persister;

import com.hp.hpl.jena.ontology.OntModel;
 
/**
 * This dialog is intended to be used anywhere in the app, to create multiple new resources 
 * (along with their rdfs:label and rdfs:comment properties).
 * 
 * Upon clicking "Save" and validating the form,a new Model is created.  This model can be retrieved using the 
 * getModel() method.
 * @author David Donohue
 * Jul 23, 2008
 */
public abstract class AScrolledOntResourceDialog extends Dialog {

		protected Text messageText;
		private String uriPrefix;
		
		protected Composite formComposite;
		private ScrolledComposite scrolledComposite;
		protected OntModel ontModel;
		private String message;
		private String title;
//		private Composite pageComposite;
		
		private static Logger log = Logger.getLogger(AScrolledOntResourceDialog.class);
		
		/**
		 * ontClass is the super class, to be used for creating this OntResource
		 */
		protected AScrolledOntResourceDialog(Shell parentShell, String title, String description) {
			super(parentShell);
			setTitle(title);
			setMessage(description);
		}
		
		
		@Override
    protected Control createDialogArea(Composite parent) {
//    	Composite container = (Composite) super.createDialogArea(parent);
    	
			Shell shell = parent.getShell();
			shell.setText(getTitle());
			
    	scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
  		GridLayout gl = new GridLayout(1, true);
  		scrolledComposite.setLayout(gl);
  		scrolledComposite.setExpandHorizontal(true);
  		scrolledComposite.setExpandVertical(true);
  		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
  		scrolledComposite.setLayoutData(gridData);
  		
//  		pageComposite = new Composite(scrolledComposite, SWT.NONE);
//  		gl = new GridLayout(1, true);
//  		pageComposite.setLayout(gl);
  		
//  		messageText = new Text(pageComposite, SWT.WRAP | SWT.READ_ONLY);
//			if (getMessage() != null) {
//				messageText.setText(getMessage());
//			}
//			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//			messageText.setLayoutData(gridData);
  		
  		formComposite = new Composite(scrolledComposite, SWT.NONE);
  		
  		scrolledComposite.setContent(formComposite);
  		
  		scrolledComposite.addControlListener(new ControlAdapter() {
  			public void controlResized(ControlEvent e) {
//  				Rectangle r = scrolledComposite.getClientArea();
//  				scrolledComposite.setMinSize(formComposite.computeSize(r.width, SWT.DEFAULT));
  				scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  			}
  		});
  		
      
//      try {
			
			
//			Composite formComposite = new Composite(container, SWT.NONE);
//			GridLayout formLayout = new GridLayout(2, false);
//			formComposite.setLayout(formLayout);
//			GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//			formComposite.setLayoutData(gridData);
			
//        Label uriLabel = new Label(formComposite, SWT.NONE);
//        uriLabel.setText("Enter the URI");
//        uriText = new Text(formComposite, SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        uriText.setLayoutData(gridData);
			
			addFormElements();
			
			scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
      return scrolledComposite;
    }
    
    /**
     * Add the form elements
     */
    protected abstract void addFormElements();

		public String getUriPrefix() {
			if (uriPrefix != null) return uriPrefix;
			Persister persister = Persister.getInstance();
			AppInfo appInfo = persister.getAppInfo();
			return appInfo.getSite().getUriPrefix().getNamespaceUri();
		}
		
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID,
            "Save", true);
        createButton(parent, IDialogConstants.CANCEL_ID,
            "Cancel", false);
    }
    
    protected Point getInitialSize() {
        return new Point(800, 450);
    }
    
    @Override
    protected void okPressed() {
    	if (! validate()) {
    		return;
    	}
    	
    	this.ontModel = createModel();
    	
    	super.okPressed();
    }
    
		/**
		 * Process the form, and create a Model containing the statements.  Should start with the existing
		 * Model (if not null) and add statements to this.  Other wise must create the model first.
		 */
		protected abstract OntModel createModel();
    
    /**
     * Validate the form, set any needed error message, return true if the form is valid
     * @return
     */
		protected abstract boolean validate();
		
		public void setMessage(String messageString) {
			this.message = messageString;
			if (messageText != null) {
				messageText.setText(messageString);
			}
		}

		public OntModel getOntModel() {
			return ontModel;
		}

		public void setOntModel(OntModel ontModel) {
			this.ontModel = ontModel;
		}

		public void setUriPrefix(String uriPrefix) {
			this.uriPrefix = uriPrefix;
		}

		public String getMessage() {
			return message;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
}

