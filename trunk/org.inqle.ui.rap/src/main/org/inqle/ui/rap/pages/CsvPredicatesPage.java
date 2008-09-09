/**
 * 
 */
package org.inqle.ui.rap.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.actions.LoadCsvFileWizard;
import org.inqle.ui.rap.csv.CsvImporter;

/**
 * @author David Donohue
 * Jun 4, 2008
 */
public class CsvPredicatesPage extends DynaWizardPage {
	
	private static Logger log = Logger.getLogger(CsvPredicatesPage.class);
	private Text[] predicateUriTexts;
	private ScrolledComposite scrollingComposite;
	private Composite formComposite;
	
	public CsvPredicatesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
		//Generate the form at the top of the page
		formComposite = new Composite(scrollingComposite, SWT.NONE);
		formComposite.setLayout (new GridLayout (2,false));
		
		log.info("CsvPredicatesPage.refreshTableData()...");
		if (getWizard() == null || (!(getWizard() instanceof LoadCsvFileWizard))) {
			log.error("getWizard()=" + getWizard() + "; it is null or not a LoadCsvFileWizard");
			return;
		}
		
		LoadCsvFileWizard loadCsvFileWizard = (LoadCsvFileWizard)getWizard();
		log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		CsvImporter csvImporter = loadCsvFileWizard.getCsvImporter();
		log.info("csvImporter retrieved");
		String[][] data = csvImporter.getCsvReader().getRawData();
		log.info("data= " + data);
		String[] headers = data[csvImporter.getCsvReader().getHeaderIndex()];
		
		//set the page layout
		//Composite parentComposite = selfComposite.getParent();
		//selfComposite.dispose();
		GridData gridData;
		
		predicateUriTexts = new Text[headers.length];
		int headerIndex = 0;
		List<String> predicateValues = getCsvImporter().getColumnPredicateUris();
		if (predicateValues == null) {
			predicateValues = new ArrayList<String>();
		}
		log.info("predicateUris values from form=" + Arrays.asList(getPredicateUris()));
		log.info("predicateValues from CSV Importer=" + predicateValues);
		for (String header: headers) {
			try {
				new Label (formComposite, SWT.NONE).setText(header);
				predicateUriTexts[headerIndex] = new Text(formComposite, SWT.BORDER);
				gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
				predicateUriTexts[headerIndex].setLayoutData(gridData);
				predicateUriTexts[headerIndex].setToolTipText("Enter the full URI of the RDF predicate that represents the data in this column.\nLeave blank to ignore the column.");
				
				if (predicateValues.size() > headerIndex) {
					predicateUriTexts[headerIndex].setText(predicateValues.get(headerIndex));
				}
				headerIndex++;
			} catch (Exception e) {
				log.error("Unable to render form field for column " + header, e);
			}
		}
		
		scrollingComposite.setContent(formComposite);
		scrollingComposite.setExpandHorizontal(true);
		scrollingComposite.setExpandVertical(true);
		//scrollingComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		formComposite.setSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		scrollingComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollingComposite.getClientArea();
				scrollingComposite.setMinSize(formComposite.computeSize(r.width, SWT.DEFAULT));
			}
		});
		//selfComposite.pack(true);
	}

	@Override
	public void addElements() {
	}
	
	@Override
	public void createControl(Composite parent) {
		log.info("CsvPredicatesPage.createControl()");
		scrollingComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		
		setControl(scrollingComposite);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		refreshTableData();
	}
	
	public java.util.List<String> getPredicateUris() {
		ArrayList<String> predicateUris = new ArrayList<String>();
		if (predicateUriTexts ==  null) {
			return predicateUris;
		}
		int predicateIndex = -1;
		for (Text predicateUriText: predicateUriTexts) {
			predicateIndex++;
			if (predicateUriText == null) {
				predicateUris.add(null);
				continue;
			}
			predicateUris.add(predicateUriText.getText());
		}
		return predicateUris;
	}
	
	private CsvImporter getCsvImporter() {
		LoadCsvFileWizard loadCsvFileWizard = (LoadCsvFileWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return loadCsvFileWizard.getCsvImporter();
	}
	
	@Override
	public boolean onPreviousPage() {
		bindValuesToCsvImporter();
		return true;
	}

	private void bindValuesToCsvImporter() {
		getCsvImporter().setColumnPredicateUris(getPredicateUris());
	}
}
