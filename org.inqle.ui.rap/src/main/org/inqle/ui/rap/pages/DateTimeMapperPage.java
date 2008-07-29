package org.inqle.ui.rap.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.inqle.ui.rap.widgets.TextField;
import org.inqle.ui.rap.widgets.TextFieldShower;

public class DateTimeMapperPage extends DynaWizardPage implements SelectionListener {

	private Button selectGlobalDateTime;
	private Button selectRowDateTime;
	private TextFieldShower globalDateTextShower;
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
				"Example: 2009-01-20 13:00:00",
				null,
				SWT.BORDER
		);
		
		selectRowDateTime = new Button(selfComposite, SWT.RADIO);
		selectRowDateTime.setText("Different rows of data have different dates and/or times.");
		selectRowDateTime.addSelectionListener(this);
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(selectGlobalDateTime)) {
			selectRowDateTime.setSelection(false);
			globalDateTextShower.setEnabled(true);
		}
		
		if (clickedObject.equals(selectRowDateTime)) {
			selectGlobalDateTime.setSelection(false);
			globalDateTextShower.setEnabled(false);
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
}
