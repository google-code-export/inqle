package org.inqle.ui.rap.actions;


import java.util.Arrays;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.ui.rap.pages.DynaWizardPage;
import org.inqle.ui.rap.widgets.TextField;

public class SubjectInfoPage extends DynaWizardPage implements SelectionListener {

	private static final String DEFAULT_TITLE = "Identify the Subjects";
	private static final String DEFAULT_DESCRIPTION = "Please identify the instances.";
	private static final String URI_TYPE_INQLE_GENERATED = "INQLE-generated";
	private static final String URI_TYPE_RANDOM_UUID = "Your URI prefix + random UUID";
	private static final String URI_TYPE_COLUMN_VALUE = "Your URI prefix + Value from specified column (gets converted into a URI-safe format)";
	
	private static final String[] SUBJECT_URI_CREATION_METHODS = {
		URI_TYPE_INQLE_GENERATED,
		URI_TYPE_RANDOM_UUID,
		URI_TYPE_COLUMN_VALUE
	};
	private static java.util.List<String> SUBJECT_URI_CREATION_METHOD_LIST = Arrays.asList(SUBJECT_URI_CREATION_METHODS);
	private Button selectPertainsToAllRows;
	private Button selectPertainsToSomeRows;
	private TextField instanceUriField;
	private List subjectUriCreationMethodList;
	private TextField instanceUriPrefixField;
	
	public SubjectInfoPage() {
		this(DEFAULT_TITLE, null, DEFAULT_DESCRIPTION);
	}
			
	public SubjectInfoPage(String title, ImageDescriptor titleImage, String description) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
		setDescription(description);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		selectPertainsToAllRows = new Button(selfComposite, SWT.RADIO);
		selectPertainsToAllRows.setText("A particular instance pertains to all rows");
		selectPertainsToAllRows.addSelectionListener(this);
		
		instanceUriField = new TextField(selfComposite, "Enter URI of this instance", "Enter a URI that represents the thing");
		instanceUriField.setEnabled(false);
		
		selectPertainsToSomeRows = new Button(selfComposite, SWT.RADIO);
		selectPertainsToSomeRows.setText("Each row might have a different instance");
		selectPertainsToSomeRows.addSelectionListener(this);
		
		instanceUriField = new TextField(selfComposite, "Enter URI of this instance", "Enter a URI that represents the thing");
		
		new Label(selfComposite, SWT.NONE).setText("Select the naming method for generating the URI for each row");
		subjectUriCreationMethodList = new List(selfComposite, SWT.SINGLE);
		subjectUriCreationMethodList.setItems(SUBJECT_URI_CREATION_METHODS);
		
		instanceUriPrefixField = new TextField(selfComposite, "Enter URI prefix", "(not required for INQLE-generated naming method) Enter the first part of a URI, which will be appended with the value.");

	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(selectPertainsToAllRows)) {
			selectPertainsToSomeRows.setSelection(false);
			instanceUriField.setEnabled(true);
			subjectUriCreationMethodList.setEnabled(false);
			instanceUriPrefixField.setEnabled(false);
		}
		
		if (clickedObject.equals(selectPertainsToSomeRows)) {
			selectPertainsToAllRows.setSelection(false);
			instanceUriField.setEnabled(false);
			subjectUriCreationMethodList.setEnabled(true);
			instanceUriPrefixField.setEnabled(true);
		}
	}
	
	public String getInstanceUri() {
		if (selectPertainsToSomeRows.getSelection()) {
			return instanceUriField.getTextValue();
		}
		return null;
	}
	
	public boolean isValid() {
		if (UriMapper.isUri(getInstanceUri())) {
			return true;
		}
		if (selectPertainsToSomeRows.getSelection()) {
			if (subjectUriCreationMethodList.getSelectionIndex() == SUBJECT_URI_CREATION_METHOD_LIST.indexOf(URI_TYPE_INQLE_GENERATED)) {
				return true;
			} else {
				
			}
		}
		return false;
	}
}