package org.inqle.ui.rap.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.layout.GridLayout;
import org.inqle.ui.rap.actions.FileDataImporterWizard;

public abstract class SubjectPropertyValuesPage extends DynaWizardPage {

	public SubjectPropertyValuesPage(String title, String description) {
		super(title, null);
		setDescription(description);
	}
	
	public SubjectPropertyValuesPage(String title, ImageDescriptor titleImage) {
		super(title, titleImage);
	}

	@Override
	public void addElements() {
		GridLayout gl = new GridLayout(2, false);
		selfComposite.setLayout(gl);
	}
	
	@Override
	public void onEnterPageFromPrevious() {
		String subjectUri = getSubjectUri();
		
	}

	private String getSubjectUri() {
		FileDataImporterWizard wizard = (FileDataImporterWizard)getWizard();
		return wizard.getSubjectClassUri(this);
	}

}
