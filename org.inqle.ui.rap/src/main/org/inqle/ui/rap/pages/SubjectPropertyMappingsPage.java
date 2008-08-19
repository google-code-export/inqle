package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.jena.util.SparqlXmlUtil;
import org.inqle.http.lookup.OwlPropertyLookup;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvImporterWizard;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.widgets.DropdownFieldShower;
import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.w3c.dom.Document;

public abstract class SubjectPropertyMappingsPage extends DynaWizardPage {

	private static Logger log = Logger.getLogger(SubjectPropertyMappingsPage.class);

	private String subjectClassUri;

	private List<DropdownFieldShower> textMappings;
	
	public SubjectPropertyMappingsPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertyMappingsPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering SubjectPropertyMappingsPage...");
		String currentSubjectClassUri = getSubjectUri();
		if (subjectClassUri.equals(currentSubjectClassUri)) {
			return;
		}
		subjectClassUri = currentSubjectClassUri;
		String propertiesXml = OwlPropertyLookup.lookupAllDataProperties(
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
		textMappings = new ArrayList<DropdownFieldShower>();
		CsvImporter csvImporter = getCsvImporter();
		String[] headers = csvImporter.getHeaders();
		for (Map<String, String> row: rowValues) {
			String uri = row.get(OwlPropertyLookup.QUERY_HEADER_URI);
			String label = row.get(OwlPropertyLookup.QUERY_HEADER_LABEL);
			String comment = row.get(OwlPropertyLookup.QUERY_HEADER_COMMENT);
			DropdownFieldShower dropdownFieldShower = new DropdownFieldShower(
					selfComposite,
					headers,
					label,
					comment,
					null,
					SWT.BORDER
			);
			dropdownFieldShower.setFieldUri(uri);
			textMappings.add(dropdownFieldShower);
		}
	}
	
	private CsvImporter getCsvImporter() {
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		return loadCsvFileWizard.getCsvImporter();
	}

	public IDataFieldShower[] getDataFields() {
		IDataFieldShower[] dataFieldShowerArray = {};
		return textMappings.toArray(dataFieldShowerArray);
	}

}
