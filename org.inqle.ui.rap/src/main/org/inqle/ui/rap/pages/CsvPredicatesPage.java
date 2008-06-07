/**
 * 
 */
package org.inqle.ui.rap.pages;

import java.util.ArrayList;

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
	private ScrolledComposite pageComposite;
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
//		log.info("refreshTableData()...");
		if (getWizard() == null || (!(getWizard() instanceof LoadCsvFileWizard))) {
			log.error("getWizard()=" + getWizard() + "; it is null or not a LoadCsvFileWizard");
			return;
		}
		
		LoadCsvFileWizard loadCsvFileWizard = (LoadCsvFileWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		CsvImporter csvImporter = loadCsvFileWizard.getCsvImporter();
		//log.info("csvImporter retrieved");
		String[][] data = csvImporter.getRawData();
		//log.info("data= " + data);
		String[] headers = data[csvImporter.getHeaderIndex()];
		
		//set the page layout
		selfComposite.dispose();
		GridData gridData;
		pageComposite = new ScrolledComposite(parentComposite, SWT.H_SCROLL | SWT.V_SCROLL);
		//ScrolledComposite formComposite = new ScrolledComposite(parentComposite, SWT.H_SCROLL | SWT.V_SCROLL);
		
//		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		pageComposite.setLayoutData(gridData);
//		pageComposite.setLayout (new GridLayout (1,false));
		
		//Generate the form at the top of the page
		formComposite = new Composite(pageComposite, SWT.NONE);
		
//		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
//		formComposite.setLayoutData(gridData);
		formComposite.setLayout (new GridLayout (2,false));
		
		predicateUriTexts = new Text[headers.length];
		int headerIndex = 0;
		for (String header: headers) {
			try {
				new Label (formComposite, SWT.NONE).setText(header);
				predicateUriTexts[headerIndex] = new Text(formComposite, SWT.BORDER);
				gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
				predicateUriTexts[headerIndex].setLayoutData(gridData);
				predicateUriTexts[headerIndex].setToolTipText("Enter the full URI of the RDF predicate that represents the data in this column.\nLeave blank to ignore the column.");
				
				headerIndex++;
			} catch (Exception e) {
				log.error("Unable to render form field for column " + header, e);
			}
		}
		
		pageComposite.setContent(formComposite);
		pageComposite.setExpandHorizontal(true);
		pageComposite.setExpandVertical(true);
		//pageComposite.setMinSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		formComposite.setSize(formComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		pageComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = pageComposite.getClientArea();
				pageComposite.setMinSize(formComposite.computeSize(r.width, SWT.DEFAULT));
			}
		});
	}

	@Override
	public void addElements(Composite composite) {
		
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		refreshTableData();
	}
	
	public java.util.List<String> getPredicateUris() {
		ArrayList<String> predicateUris = new ArrayList<String>();
		for (Text predicateUriText: predicateUriTexts) {
			predicateUris.add(predicateUriText.getText());
		}
		return predicateUris;
	}
}
