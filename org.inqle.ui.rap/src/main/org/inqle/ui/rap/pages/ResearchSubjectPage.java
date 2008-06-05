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
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.inqle.ui.rap.actions.LoadCsvFileWizard;
import org.inqle.ui.rap.csv.CsvImporter;
import org.inqle.ui.rap.table.CsvTableLabelProvider;
import org.inqle.ui.rap.table.QuerySolutionTableLabelProvider;

/**
 * @author David Donohue
 * Jun 4, 2008
 */
public class ResearchSubjectPage extends DynaWizardPage {
	
	private static Logger log = Logger.getLogger(ResearchSubjectPage.class);
	
	public ResearchSubjectPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create and populate the table, using the column names provided in 
	 * <code>setPropertyNames</code> and the row data provided by <code>setRdfTable</code>
	 * 
	 */
	public void refreshTableData() {
		log.info("refreshTableData()...");
		if (getWizard() == null || (!(getWizard() instanceof LoadCsvFileWizard))) {
			log.info("getWizard()=" + getWizard() + "; it is null or not a LoadCsvFileWizard");
			return;
		}
		log.info("getWizard()= a LoadCsvFileWizard");
		LoadCsvFileWizard loadCsvFileWizard = (LoadCsvFileWizard)getWizard();
		log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		loadCsvFileWizard.refreshCsvImporter();
		CsvImporter csvImporter = loadCsvFileWizard.getCsvImporter();
		log.info("csvImporter retrieved");
		String[][] data = csvImporter.getRawData();
		log.info("data= " + data);
		String[] headers = data[csvImporter.getHeaderIndex()];
		
		Table table = new Table(selfComposite, SWT.NONE);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		//add columns
		for (String header: headers) {
			TableColumn column = new TableColumn(table,SWT.LEFT);
			column.setText(header);
			column.setResizable(true);
			column.setWidth(200);
			
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
		//this.getShell().pack(true);
	}

	@Override
	public void addElements(Composite composite) {
		
		
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		refreshTableData();
	}
	
	/**
	 * Prevent the back button from being pressed.  
	 * Due to a limitation of RAP, we cannot refresh the table with new info.
	 */
	@Override
	public boolean onPreviousPage() {
		this.setMessage("Due to RAP limitation, we are unable to go back.  You may instead close and restart this wizard.");
		return false;
	}

}
