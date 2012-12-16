package org.inqle.ui.rap.widgets;


import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
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
		private Composite pageComposite;
		private Composite container;
		
		private static Logger log = Logger.getLogger(AScrolledOntResourceDialog.class);
		
		/**
		 * ontClass is the super class, to be used for creating this OntResource
		 */
		protected AScrolledOntResourceDialog(Shell parentShell, String title, String description) {
			super(parentShell);
			setTitle(title);
			this.message = description;
		}
		
		
		@Override
    protected Control createDialogArea(Composite parent) {
    	container = (Composite) super.createDialogArea(parent);
    	container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    	container.setLayout(new GridLayout());
		
		Shell shell = parent.getShell();
		shell.setText(getTitle());
		
		messageText = new Text(container, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
		if (message != null) {
			messageText.setText(getMessage());
		}
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		messageText.setLayoutData(gridData);
		
//		setMessage(message);
		
//		ScrolledComposite scrolled = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		scrolled.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		scrolled.setLayout(new GridLayout());
//		scrolled.setExpandVertical(true);
//		scrolled.setExpandHorizontal(true);
		
    	scrolledComposite = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL);
  		GridLayout gl = new GridLayout();
  		scrolledComposite.setLayout(gl);
  		scrolledComposite.setExpandHorizontal(true);
  		scrolledComposite.setExpandVertical(true);
  		gridData = new GridData(GridData.FILL_BOTH);
  		scrolledComposite.setLayoutData(gridData);
  		
  		pageComposite = new Composite(scrolledComposite, SWT.NONE);
  		gl = new GridLayout(1, true);
  		pageComposite.setLayout(gl);
  		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
  		pageComposite.setLayoutData(gridData);
  		
//  		messageText = new Text(pageComposite, SWT.WRAP | SWT.READ_ONLY);
//			if (getMessage() != null) {
//				messageText.setText(getMessage());
//			}
//			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//			messageText.setLayoutData(gridData);
  		
  		formComposite = new Composite(pageComposite, SWT.NONE);
  		
  		scrolledComposite.addControlListener(new ControlAdapter() {
  			public void controlResized(ControlEvent e) {
  				scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  			}
  		});
			
			addFormElements();
			
			scrolledComposite.setContent(pageComposite);
			
			scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
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
    
//    protected Point getInitialSize() {
//        return new Point(800, 450);
//    }
    
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

