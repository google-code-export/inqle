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
import org.inqle.data.rdf.jenabean.mapping.SubjectMapping;
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
	
	public RowSubjectUriPage() {
		this(DEFAULT_TITLE, null, DEFAULT_DESCRIPTION);
	}
			
	public RowSubjectUriPage(String title, ImageDescriptor titleImage, String description) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
		setDescription(description);
	}

	@Override
	public void addElements() {
		log.info("RowSubjectUriPage.addElements()...");
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		GridData gridData;
		
		namingMethodLabel = new Label(selfComposite, SWT.NONE);
		namingMethodLabel.setText("Select the naming method for generating the URI for each row");
		
		subjectUriCreationMethodList = new List(selfComposite, SWT.SINGLE | SWT.BORDER);
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

	@Override
	public void onEnterPageFromPrevious() {
//		log.info("Entering RowSubjectUriPage...");
		refreshTableData();
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
			headers = csvImporter.getHeaders();
			uriSuffixColumnList.removeAll();
			uriSuffixColumnList.setItems(headers);
//			log.info("Set headers list to:" + Arrays.asList(headers));
		} catch (Exception e) {
			log.error("Error refreshing table data", e);
		}
		
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
//		Object clickedObject = selectionEvent.getSource();
		
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
}