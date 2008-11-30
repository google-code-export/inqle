package org.inqle.ui.rap.pages;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.actions.FileDataImporterWizard;

public class AddSubjectPage extends DynaWizardPage implements SelectionListener {

	private static final String PAGE_TITLE = "Add a Caption or Subject";
	private static final String PAGE_DESCRIPTION = null;
	private static final String ROW_SUBJECT_BUTTON_TEXT = "Add a Subject";
	private static final String ROW_SUBJECT_DESCRIPTION = "A subject is a kind of thing which describes each row.\nExample: If each row of your table represents a different medical encounter among various patients and doctors, you could add patient as one subject type and doctor as another subject type.";
	
	private static final String TABLE_SUBJECT_BUTTON_TEXT = "Add a Caption";
	private static final String TABLE_SUBJECT_DESCRIPTION = "A caption is single thing which pertains to the entire table.\nExample: If all the data in your table was collected at the same facility and by the same organization, you could add that facility as one caption, and that organization as another caption.";
	
	public AddSubjectPage() {
		this(PAGE_TITLE, PAGE_DESCRIPTION);
	}

	public AddSubjectPage(String pageTitle, String pageDescription) {
		super(pageTitle, null);
		setDescription(pageDescription);
	}

	protected Button newTableSubjectButton;
	protected Button newRowSubjectButton;

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);

		newTableSubjectButton = new Button (selfComposite, SWT.PUSH);
		newTableSubjectButton.setText(TABLE_SUBJECT_BUTTON_TEXT);
//		newTableSubjectButton.setSelection(true);
		newTableSubjectButton.addSelectionListener(this);
		new Label(selfComposite, SWT.WRAP).setText(TABLE_SUBJECT_DESCRIPTION);
		
		new Text(selfComposite, SWT.NONE);
				
		newRowSubjectButton = new Button (selfComposite, SWT.PUSH);
		newRowSubjectButton.setText(ROW_SUBJECT_BUTTON_TEXT);
		newRowSubjectButton.addSelectionListener(this);
		new Label(selfComposite, SWT.WRAP).setText(ROW_SUBJECT_DESCRIPTION);
	}

	public void widgetDefaultSelected(SelectionEvent selectionEvent) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(newTableSubjectButton)) {
			disableForm();
			getFileDataImporterWizard().addTableSubjectPages();
//			newRowSubjectButton.setSelection(false);
		}
		if (clickedObject.equals(newRowSubjectButton)) {
			disableForm();
			getFileDataImporterWizard().addRowSubjectPages();
//			newTableSubjectButton.setSelection(false);
		}
	}

//	@Override
//	public boolean onNextPage() {
//		if (newTableSubjectButton.getSelection()) {
//			disableForm();
//			getFileDataImporterWizard().addTableSubjectPages();
//		}
//		if (newRowSubjectButton.getSelection()) {
//			disableForm();
//			getFileDataImporterWizard().addRowSubjectPages();
//		}
//		return super.onNextPage();
//	}
	
	protected FileDataImporterWizard getFileDataImporterWizard() {
		IWizard wizard = getWizard();
		if (wizard != null && wizard instanceof FileDataImporterWizard) {
			return (FileDataImporterWizard)wizard;
		}
		return null;
	}

	protected void disableForm() {
		newTableSubjectButton.setEnabled(false);
		newRowSubjectButton.setEnabled(false);
	}

}
