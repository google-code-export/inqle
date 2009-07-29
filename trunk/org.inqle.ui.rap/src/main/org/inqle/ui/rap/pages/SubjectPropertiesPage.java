package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.core.util.InqleInfo;
import org.inqle.core.util.ListMapUtil;
import org.inqle.core.util.SparqlXmlUtil;
import org.inqle.core.util.XmlDocumentUtil;
import org.inqle.data.rdf.Data;
import org.inqle.data.rdf.jenabean.mapping.DataMapping;
import org.inqle.data.rdf.jenabean.mapping.SubjectMapping;
import org.inqle.data.rdf.jenabean.mapping.TableMapping;
import org.inqle.http.lookup.LookupServlet;
import org.inqle.http.lookup.PropertyLookup;
import org.inqle.http.lookup.Requestor;
import org.inqle.ui.rap.actions.CreateHeaderPropertiesAction;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvReaderWizard;
import org.inqle.ui.rap.csv.CsvReader;
import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public abstract class SubjectPropertiesPage extends DynaWizardPage implements SelectionListener {

	private static Logger log = Logger.getLogger(SubjectPropertiesPage.class);

	protected String subjectClassUri;

	protected Composite formComposite;

//	protected Button enterNewDataPropertyButton;
//	protected Button enterNewSubjectPropertyButton;
	protected Button enterNewPropertyButton;
	
	protected String[] headers;
	
	protected List<IDataFieldShower> dataFieldShowers = new ArrayList<IDataFieldShower>();

//	protected Text enterNewDataPropertyButtonExplanation;
//	protected Text enterNewSubjectPropertyButtonExplanation;
	protected Text enterNewPropertyButtonExplanation;
	
	private ScrolledComposite scrolledComposite;

	private SubjectMapping subjectMapping;

//	private boolean pageInitialized = false;

//	private Text enterNewPropertyButtonExplanation;
	
	public SubjectPropertiesPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertiesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
//	public void addElements() {
	public void createControl(Composite parent) {
		//log.info("SubjectPropertiesPage.createControl...");
		
		scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		GridLayout gl = new GridLayout(1, true);
		scrolledComposite.setLayout(gl);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		scrolledComposite.setLayoutData(gridData);
//		formComposite = new Composite(formComposite, SWT.NONE);
		formComposite = new Composite(scrolledComposite, SWT.NONE);
		
		scrolledComposite.setContent(formComposite);
		
		gl = new GridLayout(2, false);
		formComposite.setLayout(gl);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		formComposite.setLayoutData(gridData);
		
		scrolledComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
//				Rectangle r = scrolledComposite.getClientArea();
//				scrolledComposite.setMinSize(formComposite.computeSize(r.width, SWT.DEFAULT));
				scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});		
		
		enterNewPropertyButton = new Button(formComposite, SWT.PUSH);
//		enterNewPropertyButton.setText("Add new properties for " + getThingClass().toUpperCase());
		enterNewPropertyButton.setText("Add new properties");
		enterNewPropertyButton.addSelectionListener(this);
		enterNewPropertyButtonExplanation = new Text(formComposite, SWT.WRAP | SWT.READ_ONLY | SWT.MULTI);
		enterNewPropertyButtonExplanation.setText(getEnterNewPropertyButtonLabel());
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		enterNewPropertyButtonExplanation.setLayoutData(gridData);
		
		Label spacer1 = new Label(formComposite, SWT.NONE);
		spacer1.setText(" ");
		Label spacer2 = new Label(formComposite, SWT.NONE);
		spacer2.setText(" ");
		
		Label columnHeader1 = new Label(formComposite, SWT.NONE);
		columnHeader1.setText("KNOWN PROPERTY");
		Label columnHeader2 = new Label(formComposite, SWT.NONE);
		columnHeader2.setText("COLUMN THAT CONTAINS THIS VALUE (IF ANY)");
		setControl(scrolledComposite);
		onEnterPageFromPrevious();
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		updateElements();
	}
	
	public void updateElements() {
		if (enterNewPropertyButtonExplanation==null) {
			//log.info("Page not yet initialized.  Exiting SubjectPropertiesPage.");
			return;
		}
		
		setTitle(getPageTitle());
		setDescription(getPageDescription());
		
		String currentSubjectClassUri = getSubjectUri();
		//log.info("currentSubjectClassUri=" + currentSubjectClassUri);
		if (currentSubjectClassUri == null || currentSubjectClassUri.equals(subjectClassUri)) {
			return;
		}
		removePropertyFormElements();
		
		subjectClassUri = currentSubjectClassUri;
		enterNewPropertyButtonExplanation.setText(getEnterNewPropertyButtonLabel());
		
		//is a TableMapping already identified?  If so, get any properties listed therein
		List<SortedMap<String, String>> rowValuesFromMapping = new ArrayList<SortedMap<String, String>>();
		if (subjectMapping != null && subjectMapping.getDataMappings() != null) {
			for (DataMapping dataMapping: subjectMapping.getDataMappings()) {
				if (dataMapping.getMapsPredicate()==null || dataMapping.getMapsHeader()==null) {
					continue;
				}
				SortedMap<String, String> valueMap = new TreeMap<String, String>();
				valueMap.put(PropertyLookup.QUERY_HEADER_URI, dataMapping.getMapsPredicate().toString());
				valueMap.put(PropertyLookup.QUERY_HEADER_PROPERTY_TYPE, dataMapping.getMapsPropertyType().toString());
				valueMap.put(PropertyLookup.QUERY_HEADER_LABEL, "URI=" + dataMapping.getMapsPredicate().toString());
				valueMap.put(PropertyLookup.QUERY_HEADER_COMMENT, "(description unavailable)");
				valueMap.put(PropertyLookup.QUERY_HEADER_PROPERTY_HEADER, dataMapping.getMapsHeader());
				valueMap.put(PropertyLookup.QUERY_HEADER_PROPERTY_VALUE, dataMapping.getMapsValue());
				
				rowValuesFromMapping.add(valueMap);
			}
		}
		
		//Lookup known properties for this subject class
		String dataAndSubjectPropertiesXml = PropertyLookup.lookupAllDataProperties(
				subjectClassUri, 
				100, 
				0);
		Document dataAndSubjectPropertiesDocument = XmlDocumentUtil.getDocument(dataAndSubjectPropertiesXml);
		log.info("dataAndSubjectPropertiesXml=" + dataAndSubjectPropertiesXml);
		String otherSubjectPropertiesXml = PropertyLookup.lookupPropertiesInSchemaDatamodels(
				subjectClassUri, 
				100, 
				0);
		Document otherSubjectPropertiesDocument = XmlDocumentUtil.getDocument(otherSubjectPropertiesXml);
		log.info("otherSubjectPropertiesDocument=" + otherSubjectPropertiesDocument);
		Document allLocalPropertiesDocument = SparqlXmlUtil.merge(dataAndSubjectPropertiesDocument, otherSubjectPropertiesDocument);
		
		log.trace("Looking up remote Data & Subject & preferred ontology properties for class <" + subjectClassUri + "> from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
		//do the search
		Map<String, String> params = new HashMap<String, String>();
		params.put(LookupServlet.PARAM_PROPERTIES_OF_DATA_AND_PREFERRED_ONTOLOGY, subjectClassUri);
//		Document remoteDataAndSubjectPropertiesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
		Document allRemotePropertiesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
		log.trace("Received Document object:\n" + XmlDocumentUtil.xmlToString(allRemotePropertiesDocument));
		
//		log.trace("Looking up in remote schema files properties of class <" + subjectClassUri + "> from lookup service at: " + InqleInfo.URL_CENTRAL_LOOKUP_SERVICE + "...");
//		//do the search
//		params = new HashMap<String, String>();
//		params.put(LookupServlet.PARAM_PROPERTIES_OF_SUBJECT_FROM_SCHEMA_FILES, subjectClassUri);
//		Document remotePropertiesFromSchemaFilesDocument = Requestor.retrieveXmlViaPost(InqleInfo.URL_CENTRAL_LOOKUP_SERVICE, params);
//		log.trace("Received Document object:\n" + XmlDocumentUtil.xmlToString(remotePropertiesFromSchemaFilesDocument));
		
//		Document allRemotePropertiesDocument = SparqlXmlUtil.merge(remoteDataAndSubjectPropertiesDocument, remotePropertiesFromSchemaFilesDocument);
				
		log.info("Merging all LOCAL results:\n" + XmlDocumentUtil.xmlToString(allLocalPropertiesDocument));
		List<SortedMap<String, String>> localRowValues = SparqlXmlUtil.getRowValues(allLocalPropertiesDocument);
		
		log.info("...with all REMOTE results:\n" + XmlDocumentUtil.xmlToString(allRemotePropertiesDocument));
		List<SortedMap<String, String>> remoteRowValues = SparqlXmlUtil.getRowValues(allRemotePropertiesDocument);
//		Document allPropertiesDocument = SparqlXmlUtil.merge(allLocalPropertiesDocument, allRemotePropertiesDocument);
		log.info("Merged remote results into:\n" + remoteRowValues);

		List<SortedMap<String, String>> allLocalValues = ListMapUtil.merge(rowValuesFromMapping, localRowValues);
		List<SortedMap<String, String>> rowValues = ListMapUtil.merge(allLocalValues, remoteRowValues);
		
		makePropertyFormElements(rowValues);
		
		refreshScrolledComposite();
//		scrolledComposite.pack(true);
	}

	public abstract String getPageDescription();

	public abstract String getPageTitle();

	private void refreshScrolledComposite() {
		scrolledComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		formComposite.layout();
	}

	public void addElements() {
		//do nothing; this was handled by createControl()
	}
	
	private String getEnterNewPropertyButtonLabel() {
		return "Create/register new properties for " + getThingClass().toUpperCase();
	}

	/**
	 * Get the URI of the subject
	 * @return
	 */
	private String getSubjectUri() {
		FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
		return wizard.getSubjectClassUri(this);
	}
	
	/**
	 * Get the a human readable representation of the thing's type
	 * @return
	 */
	protected String getThingClass() {
		FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
		return wizard.getThingClass(this);
	}

	protected void makePropertyFormElements(List<SortedMap<String, String>> rowValues) {
		dataFieldShowers = new ArrayList<IDataFieldShower>();
		CsvReader csvImporter = getCsvReader();
		headers = csvImporter.getHeaders();
		
		if (rowValues==null) return;
		
		for (Map<String, String> row: rowValues) {
			String uri = row.get(PropertyLookup.QUERY_HEADER_URI);
			if (uri != null) uri=uri.trim();
			String label = row.get(PropertyLookup.QUERY_HEADER_LABEL);
			if (label != null) label=label.trim();
			String comment = row.get(PropertyLookup.QUERY_HEADER_COMMENT);
			if (comment != null) comment=comment.trim();
			String propertyType = row.get(PropertyLookup.QUERY_HEADER_PROPERTY_TYPE);
			if (propertyType != null) propertyType=propertyType.trim();
			String header = row.get(PropertyLookup.QUERY_HEADER_PROPERTY_HEADER);
			if (header != null) header=header.trim();
			String value = row.get(PropertyLookup.QUERY_HEADER_PROPERTY_VALUE);
			if (value != null) value=value.trim();
			//log.info("Creating form element w/\nuri=" + uri + "\nlabel=" + label + "\ncomment=" + comment + "\npropertyType=" + propertyType);
			addPropertyFormItem(uri, label, comment, propertyType, header, value);
		}
	}

	protected void removePropertyFormElements() {
		if (dataFieldShowers == null) {
			return;
		}
		for (IDataFieldShower dataFieldShower: dataFieldShowers) {
			dataFieldShower.remove();
		}
	}
	
	public IDataFieldShower[] getDataFields() {
		IDataFieldShower[] dataFieldShowerArray = {};
		return dataFieldShowers.toArray(dataFieldShowerArray);
	}
	
	private CsvReader getCsvReader() {
		ICsvReaderWizard loadCsvFileWizard = (ICsvReaderWizard)getWizard();
		return loadCsvFileWizard.getCsvReader();
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		
		if (clickedObject.equals(enterNewPropertyButton)) {			
		//	log.info("Clicked 'new subject property' button");
			CreateHeaderPropertiesAction createHeaderPropertiesAction = new CreateHeaderPropertiesAction(
					formComposite.getShell(),
					getCsvReader().getHeaders(),
					Data.DATA_PROPERTY_DATASET_ROLE_ID, 
					getSubjectUri(), 
					getThingClass());
			createHeaderPropertiesAction.run();
			
			OntModel newModel = createHeaderPropertiesAction.getModel();
			ExtendedIterator propsEI = newModel.listAllOntProperties();
			while (propsEI.hasNext()) {
				OntProperty prop = (OntProperty)propsEI.next();
				
				if (prop==null || prop.getLabel("EN")==null || prop.getLabel("EN").length()==0) {
					continue;
				}
				
				log.info("Creating form for " + prop.getURI() + ", subproperty of: " + prop.getSuperProperty().toString());
				addPropertyFormItem(prop.getURI(), 
						prop.getLabel("EN"), 
						prop.getComment("EN"),
						prop.getSuperProperty().toString(),
						null,
						null);
			}
			
			refreshScrolledComposite();
		}
	}

	/**
	 * add the new property to the form
	 * @param uri
	 * @param label
	 * @param comment
	 */
	protected abstract void addPropertyFormItem(
			String uri, 
			String label, 
			String comment,
			String propertyTypeUri,
			String header,
			String value);
	
	public void setTableMapping(TableMapping tableMapping) {
		if (tableMapping==null) {
			subjectMapping = null;
		} else {
			subjectMapping = tableMapping.getSubjectMapping();
		}
		updateElements();
	}
	
}
