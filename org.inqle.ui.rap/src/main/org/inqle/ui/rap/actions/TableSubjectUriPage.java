package org.inqle.ui.rap.actions;


import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.inqle.data.rdf.jena.uri.UriMapper;
import org.inqle.ui.rap.csv.CsvReader;
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
		
		instanceUriField = new TextField(selfComposite, "Enter URI of this instance", "Enter a URI that represents the thing");
		
		gl = new GridLayout(2, true);
		Composite uriCreationArea = new Composite(selfComposite, SWT.NONE);
		uriCreationArea.setLayout(gl);
	}
	
//	public void refreshTableData() {
//		try {
//			CsvReader csvReader = getCsvReader();
//			
//			String[][] data = csvReader.getRawData();
//			//log.info("data= " + data);
//			String[] headers = data[csvReader.getHeaderIndex()];
//			
//		} catch (Exception e) {
//			log.error("Error refreshing table data", e);
//		}
//		
//	}

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
	
	private CsvReader getCsvReader() {
		ICsvReaderWizard loadCsvFileWizard = (ICsvReaderWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return loadCsvFileWizard.getCsvReader();
	}
}