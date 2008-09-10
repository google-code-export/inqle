package org.inqle.ui.rap.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Text;
import org.inqle.ui.rap.pages.DynaWizardPage;

public class SaveMappingLoadDataPage extends DynaWizardPage {

	private static final String TITLE = null;
	private static final String DESCRIPTION = null;
	private Text nameText;
	private Text descriptionText;

	public SaveMappingLoadDataPage(String title,
			ImageDescriptor titleImage) {
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
		// TODO Auto-generated method stub

	}

	public String getTableMappingName() {
		return nameText.getText();
	}

	public String getTableMappingDescription() {
		return descriptionText.getText();
	}

}
