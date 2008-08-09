package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.pages.DynaWizardPage;
import org.inqle.ui.rap.widgets.TextField;

/**
 * Defines the URI of a static subject instance, 
 * which pertains to all rows in a data table to be imported
 * @author David Donohue
 * Aug 9, 2008
 */
public class TableSubjectUriPage extends DynaWizardPage implements SelectionListener {

	private static final Logger log = Logger.getLogger(TableSubjectUriPage.class);
	
	private static final String DEFAULT_TITLE = "Identify the Subjects";
	private static final String DEFAULT_DESCRIPTION = "Please identify the instances.";
	private static final String URI_TYPE_INQLE_GENERATED = "INQLE-generated";
	private static final String URI_TYPE_RANDOM_UUID = "URI prefix + random ID";
	private static final String URI_TYPE_COLUMN_VALUE = "URI prefix + value from specified column";
	
	private static final String[] SUBJECT_URI_CREATION_METHODS = {
		URI_TYPE_INQLE_GENERATED,
		URI_TYPE_RANDOM_UUID,
		URI_TYPE_COLUMN_VALUE
	};
//	private static java.util.List<String> SUBJECT_URI_CREATION_METHOD_LIST = Arrays.asList(SUBJECT_URI_CREATION_METHODS);
	private Button selectPertainsToAllRows;
	private Button selectPertainsToSomeRows;
	private TextField instanceUriField;
	private List subjectUriCreationMethodList;
	private TextField instanceUriPrefixField;
	private Label namingMethodLabel;

	private List uriSuffixColumnList;

	private Label uriSuffixColumnLabel;
	
	public TableSubjectUriPage() {
		this(DEFAULT_TITLE, null, DEFAULT_DESCRIPTION);
	}
			
	public TableSubjectUriPage(String title, ImageDescriptor titleImage, String description) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
		setDescription(description);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		GridData gridData;
		
		selectPertainsToAllRows = new Button(selfComposite, SWT.RADIO);
		selectPertainsToAllRows.setText("A particular instance pertains to all rows");
		selectPertainsToAllRows.addSelectionListener(this);
		
		instanceUriField = new TextField(selfComposite, "Enter URI of this instance", "Enter a URI that represents the thing");
		instanceUriField.setVisible(false);
		
		selectPertainsToSomeRows = new Button(selfComposite, SWT.RADIO);
		selectPertainsToSomeRows.setText("Each row might have a different instance");
		selectPertainsToSomeRows.addSelectionListener(this);
//		selectPertainsToSomeRows.setSelection(true);
		
		namingMethodLabel = new Label(selfComposite, SWT.NONE);
		namingMethodLabel.setText("Select the naming method for generating the URI for each row");
		namingMethodLabel.setVisible(false);
		
		subjectUriCreationMethodList = new List(selfComposite, SWT.SINGLE | SWT.BORDER);
		subjectUriCreationMethodList.setItems(SUBJECT_URI_CREATION_METHODS);
		subjectUriCreationMethodList.select(0);
		subjectUriCreationMethodList.setVisible(false);
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
	}

	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering DateTimeMapperPage...");
		refreshTableData();
	}
	
	public void refreshTableData() {
		try {
			log.info("get csvImporter...");
			CsvImporter csvImporter = getCsvImporter();
			log.info("csvImporter retrieved");
			
			String[][] data = csvImporter.getRawData();
			//log.info("data= " + data);
			String[] headers = data[csvImporter.getHeaderIndex()];
			uriSuffixColumnList.removeAll();
			uriSuffixColumnList.setItems(headers);
		} catch (Exception e) {
			log.error("Error refreshing table data", e);
		}
		
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(selectPertainsToAllRows)) {
			selectPertainsToSomeRows.setSelection(false);
			instanceUriField.setVisible(true);
			namingMethodLabel.setVisible(false);
			subjectUriCreationMethodList.setVisible(false);
//			instanceUriPrefixField.setVisible(false);
			
		}
		
		if (clickedObject.equals(selectPertainsToSomeRows)) {
			selectPertainsToAllRows.setSelection(false);
			instanceUriField.setVisible(false);
			namingMethodLabel.setVisible(true);
			subjectUriCreationMethodList.setVisible(true);
//			instanceUriPrefixField.setVisible(true);
		}
		
		if (! selectPertainsToSomeRows.getSelection()) { 
			instanceUriPrefixField.setVisible(false);
			uriSuffixColumnLabel.setVisible(false);
			uriSuffixColumnList.setVisible(false);
		} else {
			if (getSubjectCreationMethod().equals(URI_TYPE_INQLE_GENERATED)) {
				instanceUriPrefixField.setVisible(false);
			} else {
				instanceUriPrefixField.setVisible(true);
			}
			
			if (getSubjectCreationMethod().equals(URI_TYPE_COLUMN_VALUE)) {
				uriSuffixColumnLabel.setVisible(true);
				uriSuffixColumnList.setVisible(true);
			} else {
				uriSuffixColumnLabel.setVisible(false);
				uriSuffixColumnList.setVisible(false);
			}
		}
	}
	
	public String getInstanceUri() {
		if (selectPertainsToAllRows.getSelection()) {
			return instanceUriField.getTextValue();
		}
		return null;
	}
	
	public String getInstancePrefixUri() {
		if (selectPertainsToSomeRows.getSelection()) {
			return instanceUriPrefixField.getTextValue();
		}
		return null;
	}
	
	public boolean isValid() {
		if (UriMapper.isUri(getInstanceUri())) {
			return true;
		}
		if (selectPertainsToSomeRows.getSelection()) {
			if (getSubjectCreationMethod().equals(URI_TYPE_INQLE_GENERATED)) {
				return true;
			} else {
				if (UriMapper.isUri(getInstancePrefixUri())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getSubjectCreationMethodIndex() {
		return subjectUriCreationMethodList.getSelectionIndex();
	}
	
	public String getSubjectCreationMethod() {
		if (getSubjectCreationMethodIndex() < 0) {
			return null;
		}
		return SUBJECT_URI_CREATION_METHODS[getSubjectCreationMethodIndex()];
	}
	
	public int getUriSuffixColumnIndex() {
		return uriSuffixColumnList.getSelectionIndex();
	}
	
	private CsvImporter getCsvImporter() {
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return loadCsvFileWizard.getCsvImporter();
	}
}