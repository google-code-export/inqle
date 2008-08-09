package org.inqle.ui.rap.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

public abstract class AddSubjectPage extends DynaWizardPage {

	private static final String PAGE_TITLE = "Add a Subject";
	private static final String PAGE_DESCRIPTION = null;
	private static final String FINISH_WIZARD_TEXT = "Finished adding subjects.  Ready to finish wizard and import the data.";
	private static final String ROW_SUBJECT_BUTTON_TEXT = "Add multiple subjects, each pertaining to different rows.";
	private static final String TABLE_SUBJECT_BUTTON_TEXT = "Add a single subject, which pertains to all rows.";

	public AddSubjectPage() {
		super(PAGE_TITLE, null);
		setDescription(PAGE_DESCRIPTION);
	}

	public abstract String getPageDescription();

	public abstract String getPageTitle();

	private Button newTableSubjectButton;
	private Button newRowSubjectButton;
	private Button finishWizardButton;

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(1, true);
		selfComposite.setLayout(gl);

		newTableSubjectButton = new Button (selfComposite, SWT.RADIO);
		newTableSubjectButton.setText(TABLE_SUBJECT_BUTTON_TEXT);
		newTableSubjectButton.setSelection(true);
		
		newRowSubjectButton = new Button (selfComposite, SWT.RADIO);
		newRowSubjectButton.setText(ROW_SUBJECT_BUTTON_TEXT);
		
		finishWizardButton = new Button (selfComposite, SWT.RADIO);
		finishWizardButton.setText(FINISH_WIZARD_TEXT);
	}

}
