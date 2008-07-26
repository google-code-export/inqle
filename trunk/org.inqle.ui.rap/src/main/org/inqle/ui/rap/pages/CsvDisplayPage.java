/**
 * 
 */
package org.inqle.ui.rap.pages;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.inqle.ui.rap.actions.ICsvImporterWizard;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.table.CsvTableLabelProvider;

/**
 * @author David Donohue
 * Jun 4, 8
 */
public class CsvDisplayPage extends DynaWizardPage {
	
	private static final int COLUMN_WIDTH = 60;
	private static Logger log = Logger.getLogger(CsvDisplayPage.class);
	private Composite composite;
	private Table table;
	
	public CsvDisplayPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		log.info("CsvDisplayPage.createControl()");
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		table = new Table(composite, SWT.NONE);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		setControl(composite);
	}
	
	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
		log.info("CsvDisplayPage.refreshTableData()...");
		table.clearAll();
		if (getWizard() == null || (!(getWizard() instanceof ICsvImporterWizard))) {
			log.info("getWizard()=" + getWizard() + "; it is null or not a ICsvImporterWizard");
			return;
		}
		log.info("getWizard()= a ICsvImporterWizard");
		
		ICsvImporterWizard loadCsvFileWizard = (ICsvImporterWizard)getWizard();
		log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		loadCsvFileWizard.refreshCsvImporter();
		CsvImporter csvImporter = loadCsvFileWizard.getCsvImporter();
		log.info("csvImporter retrieved");
		String[][] data = csvImporter.getRawData();
		log.info("data= " + data);
		String[] headers = data[csvImporter.getHeaderIndex()];
		
		try {
			//Generate the table showing the data
			//composite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
//			Table table = new Table(composite, SWT.NONE);
//			
//			table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//			
//			table.setHeaderVisible(true);
//			table.setLinesVisible(true);

			//add columns
			for (String header: headers) {
				TableColumn column = new TableColumn(table,SWT.LEFT);
				column.setText(header);
				column.setResizable(true);
				column.setWidth(COLUMN_WIDTH);
				
				//column.pack();
				log.info("Added column: " + header);
			}
			
			TableViewer tableViewer = new TableViewer(table);
			
			ObservableListContentProvider olContentProvider = new ObservableListContentProvider();
			tableViewer.setContentProvider(olContentProvider);
			
			//CsvTableLabelProvider labelProvider = new CsvTableLabelProvider(csvImporter);
			CsvTableLabelProvider labelProvider = new CsvTableLabelProvider();
			tableViewer.setLabelProvider(labelProvider);
			
			int numToDisplay = 20;
			int numAvailable = data.length - (csvImporter.getHeaderIndex() + 1);
			if (numAvailable < numToDisplay) {
				numToDisplay = numAvailable;
			}
			String[][] dataToDisplay = new String[numToDisplay][];
			System.arraycopy( data, csvImporter.getHeaderIndex() + 1, dataToDisplay, 0, numToDisplay );
			
			WritableList writableListInput = new WritableList(Arrays.asList(dataToDisplay), String[].class);
			//log.debug("getRows():" + getRows());
			tableViewer.setInput(writableListInput);
			tableViewer.refresh();
			//composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			//composite.pack(true);
			//this.getShell().pack(true);
		} catch (Exception e) {
			log.error("Unable to render CSV Import Page Tabular data", e);
			e.printStackTrace();
		}
	}

//	@Override
//	public void createControl(Composite parent) {
//		log.info("CsvPredicatesPage.createControl()");
//		scrollingComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
//		
//		setControl(scrollingComposite);
//	}
	
	@Override
	public void addElements() {
		
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		refreshTableData();
	}
	
	/**
	 * Prevent the back button from being pressed.  
	 * Due to a limitation of RAP, we cannot refresh the table with new info.
	 */
//	@Override
//	public boolean onPreviousPage() {
//		this.setMessage("Due to RAP limitation, we are unable to go back.  You may instead close and restart this wizard.");
//		return false;
//	}

}
