package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.RDF;
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.ui.rap.CreateSubclassAction;
import org.inqle.ui.rap.CreateSubpropertyAction;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvImporterWizard;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.widgets.DropdownFieldShower;
import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.OntResource;

public abstract class SubjectPropertiesPage extends DynaWizardPage implements SelectionListener {

	private static Logger log = Logger.getLogger(SubjectPropertiesPage.class);

	protected String subjectClassUri;

	protected Composite formComposite;

	protected Button enterNewPropertyButton;

	protected String[] headers;
	
	protected List<IDataFieldShower> dataFieldShowers;
	
	public SubjectPropertiesPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertiesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		
		formComposite = new Composite(selfComposite, SWT.NONE);
		gl = new GridLayout(3, false);
		formComposite.setLayout(gl);
		
		new Label(formComposite, SWT.NONE).setText("Create/register a new property...");
		enterNewPropertyButton = new Button(formComposite, SWT.PUSH);
		enterNewPropertyButton.setText("Enter a new property");
		enterNewPropertyButton.addSelectionListener(this);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering SubjectPropertyMappingsPage...");
		String currentSubjectClassUri = getSubjectUri();
		if (subjectClassUri == null || subjectClassUri.equals(currentSubjectClassUri)) {
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
		dataFieldShowers = new ArrayList<IDataFieldShower>();
		CsvImporter csvImporter = getCsvImporter();
		headers = csvImporter.getHeaders();
		for (Map<String, String> row: rowValues) {
			String uri = row.get(PropertyLookup.QUERY_HEADER_URI);
			String label = row.get(PropertyLookup.QUERY_HEADER_LABEL);
			String comment = row.get(PropertyLookup.QUERY_HEADER_COMMENT);
			addPropertyFormItem(uri, label, comment);
		}
	}

	public IDataFieldShower[] getDataFields() {
		IDataFieldShower[] dataFieldShowerArray = {};
		return dataFieldShowers.toArray(dataFieldShowerArray);
	}
	
	private CsvImporter getCsvImporter() {
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		return loadCsvFileWizard.getCsvImporter();
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(enterNewPropertyButton)) {
			log.info("Clicked 'new property' button");
			CreateSubpropertyAction createSubpropertyAction = new CreateSubpropertyAction(
					selfComposite.getShell(), 
					Data.DATA_PROPERTY_DATASET_ROLE_ID, 
					RDF.DATA_PROPERTY);
			createSubpropertyAction.run();
			OntResource newProperty = createSubpropertyAction.getOntResource();
			addPropertyFormItem(newProperty.getURI(), newProperty.getLabel("EN"), newProperty.getComment("EN"));
		}
	}

	protected abstract void addPropertyFormItem(String uri, String label, String comment);
}
