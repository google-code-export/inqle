/**
 * 
 */
package org.inqle.ui.rap.widgets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.RDF;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * @author David Donohue
 * Dec 3, 2008
 */
public class HeaderPropertiesDialog extends AScrolledOntResourceDialog implements SelectionListener {

	private String[] headers;
	private List<Button> checkBoxes = new ArrayList<Button>();
	private List<Text> uriTexts = new ArrayList<Text>();
	private List<Button> typeCheckBoxes = new ArrayList<Button>();
	private List<Text> nameTexts = new ArrayList<Text>();
	private List<Text> descriptionTexts = new ArrayList<Text>();
	private String subjectClass;

	public HeaderPropertiesDialog(
			Shell parentShell, 
			OntModel ontModel, 
			String[] headers, 
			String subjectClass, 
			String subjectName) {
		super(parentShell, getPageTitle(subjectName), getPageDescription(subjectName));
		setOntModel(ontModel);
		this.headers = headers;
		this.subjectClass = subjectClass;
//		setTitle(getPageTitle(subjectName));
//		setMessage(getPageDescription(subjectName));
	}

//	@Override
//	public Control createDialogArea(Composite parent) {
//		Composite container = (Composite) super.createDialogArea(parent);
//		
//		
//		
//		return container;
//	}
	
	private static String getPageDescription(String subjectName) {
		String s = "Select 1 or more properties to create.  Each new property will be registered " +
				"as a property of " + subjectName.toUpperCase() + ".  \n" +
				"For each property, provide a URI, name, description.  If the property is an identifier \n" +
				"(for example, a country code, a name, a description, the ISBN number), \n" +
				"then select the checkbox under 'Identifier?'.";
		return s;
	}

	private static String getPageTitle(String subjectName) {
		return "Create new properties for " + subjectName.toUpperCase();
	}

	@Override
	protected void addFormElements() {
		GridLayout gl = new GridLayout(5, false);
		formComposite.setLayout(gl);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		formComposite.setLayoutData(gridData);
		
		new Label(formComposite, SWT.BOLD).setText("Select");
		new Label(formComposite, SWT.BOLD).setText("URI");
		Label typeLabel = new Label(formComposite, SWT.BOLD);
		typeLabel.setText("Identifier?");
		typeLabel.setToolTipText("Check this box if this property is an identifier, and does not contain data.");
		new Label(formComposite, SWT.BOLD).setText("Name");
		new Label(formComposite, SWT.BOLD).setText("Description");
		int index=0;
		for (String header: headers) {
			if (header == null) continue;
			String headerId = header.replaceAll(" ", "_");
			try {
				headerId = URLEncoder.encode(headerId, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				//leave as default
			}
			Button checkBox = new Button(formComposite, SWT.CHECK);
			checkBox.setData(index);
			checkBox.addSelectionListener(this);
			
			Text uriText = new Text(formComposite, SWT.BORDER);
			uriText.setText(getUriPrefix() + headerId);
			uriText.setEnabled(false);
			uriText.setSize(50,20);
//			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//			uriText.setLayoutData(gridData);
			
			Button typeCheckBox = new Button(formComposite, SWT.CHECK);
			typeCheckBox.setEnabled(false);
			typeCheckBox.setToolTipText("Check this box if this property is an identifier, and does not contain data.");
			
			Text nameText = new Text(formComposite, SWT.BORDER);
			nameText.setText(header);
			nameText.setEnabled(false);
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			nameText.setLayoutData(gridData);
			
			Text descriptionText = new Text(formComposite, SWT.BORDER);
			descriptionText.setEnabled(false);
			gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
			descriptionText.setLayoutData(gridData);
			
			checkBoxes.add(checkBox);
			uriTexts.add(uriText);
			typeCheckBoxes.add(typeCheckBox);
			nameTexts.add(nameText);
			descriptionTexts.add(descriptionText);
			
			index++;
		}

	}

	@Override
	protected OntModel createModel() {
		if (ontModel == null) {
			ontModel = ModelFactory.createOntologyModel();
		}
		for (int i=0; i<checkBoxes.size(); i++) {
			Button checkBox = checkBoxes.get(i);
			if (! checkBox.getSelection()) continue;
			Text uriText = uriTexts.get(i);
			
			OntProperty ontProperty = ontModel.createOntProperty(uriText.getText());
			ontProperty.addDomain(ResourceFactory.createResource(subjectClass));
			Button typeCheckBox = typeCheckBoxes.get(i);
			Property superProperty = null;
			if (typeCheckBox.getSelection()) {
				superProperty = ResourceFactory.createProperty(RDF.SUBJECT_PROPERTY);
			} else {
				superProperty = ResourceFactory.createProperty(RDF.DATA_PROPERTY);
			}
			ontProperty.setSuperProperty(superProperty);
			
			Text nameText = nameTexts.get(i);
			ontProperty.setLabel(nameText.getText(), "EN");
			Text descriptionText = descriptionTexts.get(i);
			ontProperty.setComment(descriptionText.getText(), "EN");
		}
		return ontModel;
	}

	@Override
	protected boolean validate() {
		return true;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		Button clickedButton = (Button)clickedObject;
		Object buttonVal = clickedButton.getData();
		int buttonInt = (Integer)buttonVal;
		uriTexts.get(buttonInt).setEnabled(clickedButton.getEnabled());
		typeCheckBoxes.get(buttonInt).setEnabled(clickedButton.getEnabled());
		nameTexts.get(buttonInt).setEnabled(clickedButton.getEnabled());
		descriptionTexts.get(buttonInt).setEnabled(clickedButton.getEnabled());
	}

}
