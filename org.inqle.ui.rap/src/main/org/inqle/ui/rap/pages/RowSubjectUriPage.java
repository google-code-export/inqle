package org.inqle.ui.rap.pages;


import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.mapping.SubjectMapping;
import org.inqle.data.rdf.jenabean.mapping.TableMapping;
import org.inqle.ui.rap.actions.FileDataImporterWizard;
import org.inqle.ui.rap.actions.ICsvReaderWizard;
import org.inqle.ui.rap.csv.CsvReader;
import org.inqle.ui.rap.widgets.TextField;

public class RowSubjectUriPage extends DynaWizardPage implements SelectionListener {

	private static final Logger log = Logger.getLogger(RowSubjectUriPage.class);
	
	private static final String DEFAULT_TITLE = "Identify the Subjects";
	private static final String DEFAULT_DESCRIPTION = "Please identify the instances.";
	
	private List subjectUriCreationMethodList;
	private TextField instanceUriPrefixField;
	private Label namingMethodLabel;

	private List uriSuffixColumnList;

	private Label uriSuffixColumnLabel;

	private String[] headers;

	private String uriPrefix;

	private TableMapping tableMapping;
	
	public RowSubjectUriPage() {
		this(DEFAULT_TITLE, null, DEFAULT_DESCRIPTION);
	}
			
	public RowSubjectUriPage(String title, ImageDescriptor titleImage, String description) {
		super(title, titleImage);
		setDescription(description);
	}

	@Override
	public void addElements() {
		log.info("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR RowSubjectUriPage.addElements()...");
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		GridData gridData;
		
		namingMethodLabel = new Label(selfComposite, SWT.NONE);
		namingMethodLabel.setText("Select the naming method for generating the URI for each row");
		
		subjectUriCreationMethodList = new List(selfComposite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		subjectUriCreationMethodList.setItems(SubjectMapping.SUBJECT_URI_CREATION_METHODS);
		subjectUriCreationMethodList.select(0);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		subjectUriCreationMethodList.setLayoutData(gridData);
		subjectUriCreationMethodList.addSelectionListener(this);
		
		gl = new GridLayout(2, true);
		Composite uriCreationArea = new Composite(selfComposite, SWT.NONE);
		uriCreationArea.setLayout(gl);
		
		instanceUriPrefixField = new TextField(uriCreationArea, "URI prefix", "Enter the first part of a URI, which will be appended with a value.");
		instanceUriPrefixField.setVisible(false);
		refreshUriPrefixField();
		
		gl = new GridLayout(1, true);
		Composite columnSelectionArea = new Composite(uriCreationArea, SWT.NONE);
		columnSelectionArea.setLayout(gl);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		columnSelectionArea.setLayoutData(gridData);
		
		uriSuffixColumnLabel = new Label (columnSelectionArea, SWT.NONE);
		uriSuffixColumnLabel.setText("Column containing value to append onto end of URI");
		uriSuffixColumnLabel.setVisible(false);
		uriSuffixColumnList = new List(columnSelectionArea, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
//		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		uriSuffixColumnList.setLayoutData(gridData);
		uriSuffixColumnList.setVisible(false);
//		log.info("uriSuffixColumnList null?" + (uriSuffixColumnList==null));
		refreshTableData();
	}

	private void refreshUriPrefixField() {
		if (getUriPrefix() != null) {
			instanceUriPrefixField.setTextValue(getUriPrefix());
		}
	}

	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering RowSubjectUriPage.  getUriPrefix()=" + getUriPrefix());
		refreshUriPrefixField();
		refreshTableData();
		if (tableMapping != null) {
			SubjectMapping subjectMapping = tableMapping.getSubjectMapping();
			subjectUriCreationMethodList.select(subjectMapping.getSubjectUriType());
			
			if (subjectMapping.getSubjectUriPrefix() != null) {
				instanceUriPrefixField.setTextValue(subjectMapping.getSubjectUriPrefix().toString());
			}
			
			if (subjectMapping.getSubjectHeader() != null) {
				String[] listedItems = uriSuffixColumnList.getItems();
				for (int i=0; i < listedItems.length; i++) {
					String listedString = listedItems[i];
					if (subjectMapping.getSubjectHeader().equals(listedString)) {
						uriSuffixColumnList.select(i);
						break;
					}
				}
			}

			updateFormControls();
		}
	}
	
	public void refreshTableData() {
		try {
//			log.info("Refreshing uriSuffixColumnList...");
			if (uriSuffixColumnList==null) {
//				log.info("uriSuffixColumnList is null.");
				return;
			}
			CsvReader csvImporter = getCsvReader();
//			String[][] data = csvImporter.getRawData();
//			//log.info("data= " + data);
//			String[] headers = data[csvImporter.getHeaderIndex()];
			if (csvImporter != null) {
				headers = csvImporter.getHeaders();
				uriSuffixColumnList.removeAll();
				uriSuffixColumnList.setItems(headers);
			} else {
				headers = null;
			}
			
//			log.info("Set headers list to:" + Arrays.asList(headers));
		} catch (Exception e) {
			log.error("Error refreshing table data", e);
		}
		
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
//		Object clickedObject = selectionEvent.getSource();
		
		updateFormControls();
	}
	
	private void updateFormControls() {
		if (getSubjectCreationMethod().equals(SubjectMapping.URI_TYPE_INQLE_GENERATED)) {
			instanceUriPrefixField.setVisible(false);
		} else {
			instanceUriPrefixField.setVisible(true);
		}
		
		if (getSubjectCreationMethod().equals(SubjectMapping.URI_TYPE_COLUMN_VALUE)) {
			uriSuffixColumnLabel.setVisible(true);
			uriSuffixColumnList.setVisible(true);
		} else {
			uriSuffixColumnLabel.setVisible(false);
			uriSuffixColumnList.setVisible(false);
		}
		
	}

	public String getInstancePrefixUri() {
//		if (selectPertainsToSomeRows.getSelection()) {
			return instanceUriPrefixField.getTextValue();
//		}
//		return null;
	}
	
	public boolean isValid() {
//		if (UriMapper.isUri(getInstanceUri())) {
//			return true;
//		}
//		if (selectPertainsToSomeRows.getSelection()) {
			if (getSubjectCreationMethod().equals(SubjectMapping.URI_TYPE_INQLE_GENERATED)) {
				return true;
			} else {
				if (UriMapper.isUri(getInstancePrefixUri())) {
					return true;
				}
			}
//		}
		return false;
	}
	
	public int getSubjectCreationMethodIndex() {
		return subjectUriCreationMethodList.getSelectionIndex();
	}
	
	public String getSubjectCreationMethod() {
		if (getSubjectCreationMethodIndex() < 0) {
			return null;
		}
		return SubjectMapping.SUBJECT_URI_CREATION_METHODS[getSubjectCreationMethodIndex()];
	}
	
	public int getUriSuffixColumnIndex() {
		return uriSuffixColumnList.getSelectionIndex();
	}
	
	public String getUriSuffixColumnHeader() {
		if (getUriSuffixColumnIndex() < 0) return null;
		return headers[getUriSuffixColumnIndex()];
	}
	
	private CsvReader getCsvReader() {
		ICsvReaderWizard loadCsvFileWizard = (ICsvReaderWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return loadCsvFileWizard.getCsvReader();
	}

	public String getUriPrefix() {
		
		if (uriPrefix != null) {
			return uriPrefix;
		} else {
			FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
			String subjectClassUri = wizard.getSubjectClassUri(this);
			Persister persister = Persister.getInstance();
//			return persister.getAppInfo().getSite().getUriPrefix().getNamespaceUri();
			return subjectClassUri + "/inqle_" + persister.getAppInfo().getSite().getId() + "/";
		}
	}

	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	public void setTableMapping(TableMapping tableMapping) {
		this.tableMapping = tableMapping;
		
	}
}