package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AddSubjectOrFinishPage extends AddSubjectPage implements SelectionListener {

	private static final String PAGE_TITLE = "Add a Caption or a Subject or Finish and Import";
	private static final String PAGE_DESCRIPTION = null;
	private static final String FINISH_WIZARD_TEXT = "Finished Adding Subjects";
	private static final String FINISH_WIZARD_DESCRIPTION = "Ready to finish wizard and import the data.";
	
	private static Logger log = Logger.getLogger(AddSubjectOrFinishPage.class);
	
	public AddSubjectOrFinishPage() {
		super(PAGE_TITLE, PAGE_DESCRIPTION);
	}

	private Button finishWizardButton;

	@Override
	public void addElements() {
		super.addElements();
		
		finishWizardButton = new Button (selfComposite, SWT.PUSH);
		finishWizardButton.setText(FINISH_WIZARD_TEXT);
		finishWizardButton.addSelectionListener(this);
		new Label(selfComposite, SWT.WRAP).setText(FINISH_WIZARD_DESCRIPTION);
	}

	public void widgetDefaultSelected(SelectionEvent selectionEvent) {
	}

	@Override
	public void widgetSelected(SelectionEvent selectionEvent) {
		Object clickedObject = selectionEvent.getSource();
//		if (clickedObject.equals(newTableSubjectButton)) {
//			newRowSubjectButton.setSelection(false);
//			finishWizardButton.setSelection(false);
//		}
//		if (clickedObject.equals(newRowSubjectButton)) {
//			newTableSubjectButton.setSelection(false);
//			finishWizardButton.setSelection(false);
//		}
		if (clickedObject.equals(finishWizardButton)) {
//			newTableSubjectButton.setSelection(false);
//			newRowSubjectButton.setSelection(false);
			disableForm();
			log.info("AddSubjectOrFinishPage: add last page...");
			getFileDataImporterWizard().addSaveMappingLoadDataPage();
			log.info("AddSubjectOrFinishPage: added last page.");
		} else {
			super.widgetSelected(selectionEvent);
		}
	}

	public void disableForm() {
		super.disableForm();
		finishWizardButton.setEnabled(false);
	}

//	public boolean finishSelected() {
//		return finishWizardButton.getSelection();
//	}

}
