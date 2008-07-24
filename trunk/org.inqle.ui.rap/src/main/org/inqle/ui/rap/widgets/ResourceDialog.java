package org.inqle.ui.rap.widgets;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.deferred.SetModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
public class ResourceDialog extends Dialog {
    //private Button saveButton;
		private OntClass ontClass;
		private Label messageLabel;
//		private Text labelText;
//		private Text uriText;
//		private Text commentText;
		private TextField uriTextField;
		private TextField labelTextField;
		private TextField commentTextField;
		
		/**
		 * @param parentShell
		 * @param ontClass upon saving this data, it will be created as a new instance of this ontClass
		 */
		public ResourceDialog(Shell parentShell, OntClass ontClass) {
        super(parentShell);
        this.ontClass = ontClass;
    }
		
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        messageLabel = new Label(container, SWT.NONE);
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
        		"Enter the URI",
        		"Enter the Universal Resource Identifier (URI) of this\n<" + ontClass.getURI() + ">" +
        				"\nA URI is typically configured like a web address.\nFor example, http://mypersonaluri.org/MyObject" +
        				"\nWhere possible, use a URI which has already beed defined elsewhere.",
        		9,
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
        		"Name (Label)",
        		"Enter a common name of this <" + ontClass.getURI() + ">" +
        				"\nFor example, enter 'Zimbabwe' (to represent the thing)\nor 'Weather Station' (to represent the class of things)\nor 'temperature in Celcius' (to represent the attribute')" +
        				"\nWhere possible, use the most common name.",
        		10,
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
        		"Description (Comment)",
        		"Enter a description about this <" + ontClass.getURI() + ">",
        		0,
        		null,
        		SWT.NONE,
        		SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP,
        		null
        );
        
        return container;
    }
    
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
				setMessage("Please enter a label.");
				return false;
			}
			if (getComment()==null || getComment().length() < 2) {
				setMessage("Please enter a comment.");
				return false;
			}
			return true;
		}
		
		private void setMessage(String string) {
			messageLabel.setText(string);
		}
		
		private String getUri() {
			return uriTextField.getTextValue();
		}
		
		private String getLabel() {
			return labelTextField.getTextValue();
		}
		
		private String getComment() {
			return commentTextField.getTextValue();
		}
}

