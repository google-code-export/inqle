package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.inqle.ui.rap.widgets.TextFieldShower;
import org.w3c.dom.Document;

public abstract class SubjectPropertyValuesPage extends DynaWizardPage {

	private static Logger log = Logger.getLogger(SubjectPropertyValuesPage.class);
	
	private String subjectClassUri;

	private List<TextFieldShower> textFields;
	
	public SubjectPropertyValuesPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertyValuesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering SubjectPropertyValuesPage...");
		String currentSubjectClassUri = getSubjectUri();
		if (currentSubjectClassUri.equals(subjectClassUri)) {
			return;
		}
		subjectClassUri = currentSubjectClassUri;
		String propertiesXml = PropertyLookup.lookupAllDataProperties(
				subjectClassUri, 
				10, 
				0);
		Document propertiesDocument = XmlDocumentUtil.getDocument(propertiesXml);
		List<Map<String, String>> rowValues = SparqlXmlUtil.getRowValues(propertiesDocument);
		
		makePropertyFormElements(rowValues);
	}

	/**
	 * Get the URI of the subject
	 * @return
	 */
	private String getSubjectUri() {
		FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
		return wizard.getSubjectClassUri(this);
	}

	protected void makePropertyFormElements(List<Map<String, String>> rowValues) {
		textFields = new ArrayList<TextFieldShower>();
		for (Map<String, String> row: rowValues) {
			String uri = row.get(PropertyLookup.QUERY_HEADER_URI);
			String label = row.get(PropertyLookup.QUERY_HEADER_LABEL);
			String comment = row.get(PropertyLookup.QUERY_HEADER_COMMENT);
			TextFieldShower textFieldShower = new TextFieldShower(
					selfComposite,
					label,
					comment,
					null,
					SWT.BORDER
			);
			textFieldShower.setFieldUri(uri);
			textFields.add(textFieldShower);
		}
	}

	public IDataFieldShower[] getDataFields() {
		IDataFieldShower[] dataFieldShowerArray = {};
		return textFields.toArray(dataFieldShowerArray);
	}

}
