package org.inqle.ui.rap.pages;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.inqle.ui.rap.actions.FileDataImporterWizard;

public class AddSubjectPage extends DynaWizardPage implements SelectionListener {

	private static final String PAGE_TITLE = "Add a Subject";
	private static final String PAGE_DESCRIPTION = null;
	private static final String ROW_SUBJECT_BUTTON_TEXT = "Add multiple subjects, each pertaining to different rows.";
	private static final String TABLE_SUBJECT_BUTTON_TEXT = "Add a single subject, which pertains to all rows.";

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

		newTableSubjectButton = new Button (selfComposite, SWT.RADIO);
		newTableSubjectButton.setText(TABLE_SUBJECT_BUTTON_TEXT);
		newTableSubjectButton.setSelection(true);
		newTableSubjectButton.addSelectionListener(this);
		
		newRowSubjectButton = new Button (selfComposite, SWT.RADIO);
		newRowSubjectButton.setText(ROW_SUBJECT_BUTTON_TEXT);
		newRowSubjectButton.addSelectionListener(this);
		
	}

	public void widgetDefaultSelected(SelectionEvent selectionEvent) {
	}

	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
		if (clickedObject.equals(newTableSubjectButton)) {
			newRowSubjectButton.setSelection(false);
		}
		if (clickedObject.equals(newRowSubjectButton)) {
			newTableSubjectButton.setSelection(false);
		}
	}

	@Override
	public boolean onNextPage() {
		if (newTableSubjectButton.getSelection()) {
			disableForm();
			getFileDataImporterWizard().addTableSubjectPages();
		}
		if (newRowSubjectButton.getSelection()) {
			disableForm();
			getFileDataImporterWizard().addRowSubjectPages();
		}
		return super.onNextPage();
	}
	
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
