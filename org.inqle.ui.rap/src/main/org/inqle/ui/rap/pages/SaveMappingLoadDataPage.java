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
import org.eclipse.swt.widgets.Text;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.data.rdf.jenabean.mapping.TableMapping;
import org.inqle.ui.rap.widgets.TextFieldShower;

public class SaveMappingLoadDataPage extends DynaWizardPage {

	private static final String TITLE = "Save Mapping and Import Data";
	private static final String DESCRIPTION = "Enter a name and description for your data mapping.  You can reuse this mapping in the future, when you import data files with a similar structure.";
	private TextFieldShower nameTextField;
	private TextFieldShower descriptionTextField;
//	private Button importButton;
//	private boolean saved = false;

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
				SWT.BORDER
		);
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
	
	@Deprecated
	public boolean isComplete() {
		boolean complete = false;
		if (getTableMappingName()!=null && 
				getTableMappingName().length()>0 &&
				getTableMappingDescription()!=null && 
				getTableMappingDescription().length()>0) {
			complete = true;
		}
		return complete;
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
