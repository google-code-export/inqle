package org.inqle.ui.rap.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.inqle.ui.rap.actions.ICsvReaderWizard;
import org.inqle.ui.rap.csv.CsvReader;
import org.inqle.ui.rap.widgets.TextFieldShower;

public class DateTimeMapperPage extends DynaWizardPage implements SelectionListener {

	private Button selectGlobalDateTime;
	private Button selectRowDateTime;
	private TextFieldShower globalDateTextShower;
	private List dateColumnList;
	private static Logger log = Logger.getLogger(DateTimeMapperPage.class);

	public DateTimeMapperPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);
		
		selectGlobalDateTime = new Button(selfComposite, SWT.RADIO);
		selectGlobalDateTime.setText("All data being imported has the same date and time.");
		selectGlobalDateTime.addSelectionListener(this);
		
		globalDateTextShower = new TextFieldShower(
				selfComposite,
				"Enter the date and time for all data",
				"Example: 2009-01-20 13:00:00\n" +
						"(Use military time, with hours from 00 to 23)",
				null,
				SWT.BORDER | SWT.SINGLE
		);
		
		selectRowDateTime = new Button(selfComposite, SWT.RADIO);
		selectRowDateTime.setText("Different rows of data have different dates and/or times.");
		selectRowDateTime.addSelectionListener(this);
		
		new Label (selfComposite, SWT.NONE).setText("Date Column");
		dateColumnList = new List(selfComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		dateColumnList.setLayoutData(gridData);
	}

	@Override
	public void onEnterPageFromPrevious() {
		log.info("Entering DateTimeMapperPage...");
		refreshTableData();
	}
	
	public void refreshTableData() {
		try {
			log.info("get csvImporter...");
			CsvReader csvImporter = getCsvReader();
			log.info("csvImporter retrieved");
			
			String[][] data = csvImporter.getRawData();
			//log.info("data= " + data);
			String[] headers = data[csvImporter.getHeaderIndex()];
			dateColumnList.removeAll();
			dateColumnList.setItems(headers);
			
		} catch (Exception e) {
			log.error("Error refreshing table data", e);
		}
		
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(selectGlobalDateTime)) {
			selectRowDateTime.setSelection(false);
			globalDateTextShower.setEnabled(true);
			dateColumnList.setEnabled(false);
		}
		
		if (clickedObject.equals(selectRowDateTime)) {
			selectGlobalDateTime.setSelection(false);
			globalDateTextShower.setEnabled(false);
			dateColumnList.setEnabled(true);
		}
		
	}

	public String getGlobalDateTimeString() {
		if (selectGlobalDateTime.getSelection()) {
			return globalDateTextShower.getTextValue();
		}
		return null;
	}
	
	public Date getGlobalDateTime() {
		if (getGlobalDateTimeString() == null) {
			return null;
		}
		Date globalDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-DD hh:MM:ss");
		try {
			globalDate = dateFormat.parse(getGlobalDateTimeString());
		} catch (ParseException e) {
			log .warn("Unable to parse date '" + getGlobalDateTimeString() + "'.");
			return null;
		}
		return globalDate;
	}
	
	public int getRowDateColumnIndex() {
		if (! selectRowDateTime.getSelection()) {
			return -1;
		}
		return dateColumnList.getSelectionIndex();
	}
	
	private CsvReader getCsvReader() {
		ICsvReaderWizard csvReaderWizard = (ICsvReaderWizard)getWizard();
		//log.info("loadCsvFileWizard=" + loadCsvFileWizard);
		return csvReaderWizard.getCsvReader();
	}
	
	public boolean validate() {
		if (getRowDateColumnIndex() >= 0) {
			return true;
		}
		if (getGlobalDateTime() != null) {
			return true;
		}
		return false;
	}
}
