package org.inqle.ui.rap.widgets;


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
import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jena.uri.UriMapper;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
 
/**
 * This dialog is intended to be used anywhere in the app, to create new resources 
 * (along with their rdfs:label and rdfs:comment properties).
 * 
 * Upon clicking "Save" and validating the form, the new Individual created here will be 
 * stored as a instance of the OntClass provided
 * @author David Donohue
 * Jul 23, 2008
 */
public abstract class AResourceDialog extends Dialog {
    //private Button saveButton;
		protected OntClass ontClass;
		protected Text messageText;
//		private Text labelText;
//		private Text uriText;
//		private Text commentText;
		protected TextField uriTextField;
		protected TextField labelTextField;
		protected TextField commentTextField;
		private String messageString;
		
		/**
		 * @param parentShell
		 * @param ontClass upon saving this data, it will be created as a new instance of this ontClass
		 */
		public AResourceDialog(Shell parentShell, OntClass ontClass) {
        super(parentShell);
        this.ontClass = ontClass;
    }
		
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        Shell shell = parent.getShell();
        shell.setText(getTitle());
        messageText = new Text(container, SWT.WRAP | SWT.READ_ONLY);
        if (getMessage() != null) {
        	messageText.setText(getMessage());
        }
        Composite formComposite = new Composite(container, SWT.NONE);
        GridLayout formLayout = new GridLayout(1, true);
        formComposite.setLayout(formLayout);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        formComposite.setLayoutData(gridData);
        
//        Label uriLabel = new Label(formComposite, SWT.NONE);
//        uriLabel.setText("Enter the URI");
//        uriText = new Text(formComposite, SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        uriText.setLayoutData(gridData);
        uriTextField = new TextField(
        		formComposite,
        		getUriLabel(),
        		getUriDetail(),
        		0,
        		null,
        		SWT.NONE,
        		SWT.BORDER,
        		null
        );
        
//        Label labelLabel = new Label(formComposite, SWT.NONE);
//        labelLabel.setText("Label (name, usually 1 or 2 words)");
//        labelText = new Text(formComposite, SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        labelText.setLayoutData(gridData);
        
        labelTextField = new TextField(
        		formComposite,
        		getNameLabel(),
        		getNameDetail(),
        		0,
        		null,
        		SWT.NONE,
        		SWT.BORDER,
        		null
        );
        
//        Label commentLabel = new Label(formComposite, SWT.NONE);
//        commentLabel.setText("Comment (description)");
//        commentText = new Text(formComposite, SWT.MULTI | SWT.BORDER);
//        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//        commentText.setLayoutData(gridData);
        
        commentTextField = new TextField(
        		formComposite,
        		getDescriptionLabel(),
        		getDescriptionDetail(),
        		0,
        		null,
        		SWT.NONE,
        		SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP,
        		null
        );
//        commentTextField.setSize(new Point(500, 500));
//        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        commentTextField.setLayoutData(gridData);
        return container;
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
    	//create the RDF Individual, adding the label and comment
    	Individual individual = ontClass.createIndividual(getUri());
    	individual.addProperty(RDF.LABEL_PROPERTY, getLabel());
    	individual.addProperty(RDF.COMMENT_PROPERTY, getComment());
    	super.okPressed();
    }
    
		
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
			this.messageString = messageString;
		}
		
		public String getUri() {
			return uriTextField.getTextValue();
		}
		
		public String getLabel() {
			return labelTextField.getTextValue();
		}
		
		public String getComment() {
			return commentTextField.getTextValue();
		}
}

