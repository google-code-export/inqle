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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.data.rdf.jenabean.Persister;

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
public abstract class AOntResourceDialog extends Dialog {

		protected Text messageText;
//		private Text labelText;
//		private Text uriText;
//		private Text commentText;
		protected TextFieldShower uriTextField;
		protected TextFieldShower labelTextField;
		protected TextFieldShower commentTextField;
//		private String messageString;
		private OntResource ontResource;
		private String uriPrefix;
		
		private static Logger log = Logger.getLogger(AOntResourceDialog.class);
		
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
		protected AOntResourceDialog(Shell parentShell) {
			super(parentShell);
		}
		
    protected Control createDialogArea(Composite parent) {
      Composite container = (Composite) super.createDialogArea(parent);
      
      try {
				Shell shell = parent.getShell();
				shell.setText(getTitle());
				messageText = new Text(container, SWT.WRAP | SWT.READ_ONLY);
				if (getMessage() != null) {
					messageText.setText(getMessage());
				}
				Composite formComposite = new Composite(container, SWT.NONE);
				GridLayout formLayout = new GridLayout(2, false);
				formComposite.setLayout(formLayout);
				GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
				formComposite.setLayoutData(gridData);
				
//        Label uriLabel = new Label(formComposite, SWT.NONE);
//        uriLabel.setText("Enter the URI");
//        uriText = new Text(formComposite, SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        uriText.setLayoutData(gridData);
				uriTextField = new TextFieldShower(
						formComposite,
						getUriLabel(),
						getUriDetail(),
						null,
						SWT.BORDER
				);
				if (getUriPrefix() != null) {
					uriTextField.setValue(getUriPrefix());
				}
				
//        Label labelLabel = new Label(formComposite, SWT.NONE);
//        labelLabel.setText("Label (name, usually 1 or 2 words)");
//        labelText = new Text(formComposite, SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        labelText.setLayoutData(gridData);
				
				labelTextField = new TextFieldShower(
						formComposite,
						getNameLabel(),
						getNameDetail(),
						null,
						SWT.BORDER
				);
				
//        Label commentLabel = new Label(formComposite, SWT.NONE);
//        commentLabel.setText("Comment (description)");
//        commentText = new Text(formComposite, SWT.MULTI | SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        commentText.setLayoutData(gridData);
				
				commentTextField = new TextFieldShower(
						formComposite,
						getDescriptionLabel(),
						getDescriptionDetail(),
						null,
						SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP
				);
			} catch (Exception e) {
				log.error("Error running AOntResourceDialog.createDialogArea()", e);
			}
//        commentTextField.setSize(new Point(500, 500));
//        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        commentTextField.setLayoutData(gridData);
      return container;
    }
    
    private String getUriPrefix() {
			if (uriPrefix != null) return uriPrefix;
			Persister persister = Persister.getInstance();
			AppInfo appInfo = persister.getAppInfo();
			return appInfo.getSite().getUriPrefix().getNamespaceUri();
		}

		public abstract String getTitle();
    
    public abstract String getMessage();
    
    public abstract String getUriLabel();
    
    public abstract String getUriDetail();
    
    public abstract String getNameLabel();
    
    public abstract String getNameDetail();
    
    public abstract String getDescriptionLabel();
    
    public abstract String getDescriptionDetail();

		protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID,
            "Save", true);
        createButton(parent, IDialogConstants.CANCEL_ID,
            "Cancel", false);
    }
    
    protected Point getInitialSize() {
        return new Point(500, 450);
    }
    
    protected void okPressed() {
    	if (! validate()) {
    		return;
    	}
    	
    	this.ontResource = createRdf();
    	
    	super.okPressed();
    }
    
		/**
		 * Default implementation creates a new individual of the OntClass ontClass,
		 * and adds label & comment properties
		 */
		protected OntResource createRdf() {
			//create the RDF OntResource
//    	Individual individual = ontClass.createIndividual(getUri());
    	OntResource ontResource = createOntResource();
    	
    	//add the label and comment
//    	ontResource.addProperty(RDF.LABEL_PROPERTY, getLabel());
//    	ontResource.addProperty(RDF.COMMENT_PROPERTY, getComment());
    	ontResource.addLabel(getLabel(), "EN");
    	ontResource.addComment(getComment(), "EN");
    	return ontResource;
		}

		/**
		 * Create the OntResource, to be decorated with the label and comment.
		 * Will likely need to use the method getUri() to retrieve the URI of the OntResource.
		 * @return
		 */
    protected abstract OntResource createOntResource();
    
		private boolean validate() {
			if (! UriMapper.isUri(getUri())) {
				setMessage(getUri() + " is not a valid URI.");
				return false;
			}
			if (getLabel()==null || getLabel().length() < 2) {
				setMessage("Please enter a name.");
				return false;
			}
			if (getComment()==null || getComment().length() < 2) {
				setMessage("Please enter a description.");
				return false;
			}
			return true;
		}
		
		public void setMessage(String messageString) {
			if (messageText != null) {
				messageText.setText(messageString);
			}
		}
		
		public String getUri() {
			return uriTextField.getValue();
		}
		
		public String getLabel() {
			return labelTextField.getValue();
		}
		
		public String getComment() {
			return commentTextField.getValue();
		}

		public OntResource getOntResource() {
			return ontResource;
		}
		
		public void setUriPrefix(String uriPrefix) {
			this.uriPrefix = uriPrefix;
		}
}

