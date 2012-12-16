package org.inqle.ui.rap.pages;


import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.inqle.ui.rap.widgets.TextFieldShower;

public class SaveMappingLoadDataPage extends DynaWizardPage implements SelectionListener {

	private static final String TITLE = "Save Mapping and Import Data";
	private static final String DESCRIPTION = "Enter a name and description for your data mapping.  You can reuse this mapping in the future, when you import data files with a similar structure.";
	private TextFieldShower nameTextField;
	private TextFieldShower descriptionTextField;
	private Button dontSaveMappingButton;
	private Button saveMappingButton;

	private static Logger log = Logger.getLogger(SaveMappingLoadDataPage.class);
	public SaveMappingLoadDataPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
		// TODO Auto-generated constructor stub
	}

	public SaveMappingLoadDataPage() {
		this(TITLE, DESCRIPTION);
	}

	public SaveMappingLoadDataPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}

	@Override
	public void addElements() {
		log.info("SaveMappingLoadDataPage.addElements()...");
		GridLayout layout = new GridLayout(1, true);
		selfComposite.setLayout(layout);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		selfComposite.setLayoutData(gridData);
		
		Composite formComposite = new Composite(selfComposite, SWT.NONE);
		layout = new GridLayout(2, false);
		formComposite.setLayout(layout);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		formComposite.setLayoutData(gridData);

		saveMappingButton = new Button(formComposite, SWT.RADIO);
		saveMappingButton.setText("Save this import strategy");
		saveMappingButton.addSelectionListener(this);
		saveMappingButton.setSelection(true);
		
		Label saveMappingDescription = new Label(formComposite, SWT.NONE);
		saveMappingDescription.setText(
				"If selected, the strategy for importing data will be saved.  " +
				"Select this option if you or someone might want to import CSV " +
				"text files of identical structure.");
		
		nameTextField = new TextFieldShower(
				formComposite,
				"Name of Table Mapping",
				null,
				null,
				SWT.BORDER
		);
		
		descriptionTextField = new TextFieldShower(
				formComposite,
				"Description of Table Mapping",
				null,
				null,
				SWT.BORDER | SWT.MULTI
		);
		
		dontSaveMappingButton = new Button(formComposite, SWT.RADIO);
		dontSaveMappingButton.setText("Do not save this import strategy");
		dontSaveMappingButton.addSelectionListener(this);
		
//		importButton = new Button(selfComposite, SWT.PUSH | SWT.BORDER);
//		importButton.addSelectionListener(this);
	}

	public String getTableMappingName() {
		if (nameTextField==null) return null;
		return nameTextField.getValue();
	}

	public String getTableMappingDescription() {
		if (descriptionTextField==null) return null;
		return descriptionTextField.getValue();
	}
	
	public boolean isComplete() {
		boolean complete = false;
		if (dontSaveMappingButton.getSelection()) {
			complete = true;
		}
		if (getTableMappingName()!=null && 
				getTableMappingName().length()>0 &&
				getTableMappingDescription()!=null && 
				getTableMappingDescription().length()>0) {
			complete = true;
		}
		
		return complete;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Object source = event.getSource();
		if (source.equals(saveMappingButton)) {
			saveMappingButton.setSelection(true);
			dontSaveMappingButton.setSelection(false);
			nameTextField.setEnabled(true);
			descriptionTextField.setEnabled(true);
		}
		if (source.equals(dontSaveMappingButton)) {
			dontSaveMappingButton.setSelection(true);
			saveMappingButton.setSelection(false);
			nameTextField.setEnabled(false);
			descriptionTextField.setEnabled(false);
		}
	}

	public boolean shouldSaveMapping() {
		if (saveMappingButton.getSelection()) {
			return true;
		}
		return false;
	}
	
	public void setShouldSaveMapping(boolean shouldSave) {
		if (! shouldSave) {
			dontSaveMappingButton.setSelection(true);
			saveMappingButton.setSelection(false);
			nameTextField.setEnabled(false);
			descriptionTextField.setEnabled(false);
		} else {
			dontSaveMappingButton.setSelection(false);
			saveMappingButton.setSelection(true);
			nameTextField.setEnabled(true);
			descriptionTextField.setEnabled(true);
		}
	}

//	public void widgetDefaultSelected(SelectionEvent event) {
//		
//	}

//	public void widgetSelected(SelectionEvent event) {
//		if (!canFlipToNextPage()) return;
//		Object source = event.getSource();
//		if (source.equals(importButton)) {
//			importButton.setEnabled(false);
//			
//			FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
//			wizard.saveAndDoImport();
//			saved = true;
//		}
//	}
}
