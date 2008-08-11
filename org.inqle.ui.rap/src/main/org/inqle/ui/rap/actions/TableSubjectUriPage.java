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
public class TableSubjectUriPage extends DynaWizardPage {

	private static final Logger log = Logger.getLogger(TableSubjectUriPage.class);
	
	private static final String DEFAULT_TITLE = "Identify the Subjects";
	private static final String DEFAULT_DESCRIPTION = "Please identify the instances.";

	private TextField instanceUriField;
	
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
		
		instanceUriField = new TextField(selfComposite, "Enter URI of this instance", "Enter a URI that represents the thing");
		instanceUriField.setVisible(false);
		
		gl = new GridLayout(2, true);
		Composite uriCreationArea = new Composite(selfComposite, SWT.NONE);
		uriCreationArea.setLayout(gl);
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
			
		} catch (Exception e) {
			log.error("Error refreshing table data", e);
		}
		
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}
	
	public String getInstanceUri() {
		return instanceUriField.getTextValue();
	}
	
	
	public boolean isValid() {
		if (UriMapper.isUri(getInstanceUri())) {
			return true;
		}
		return false;
	}
	
	private CsvImporter getCsvImporter() {
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return loadCsvFileWizard.getCsvImporter();
	}
}