package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.inqle.ui.rap.widgets.DropdownFieldShower;

public abstract class SubjectPropertyMappingsPage extends SubjectPropertiesPage {

	private static Logger log = Logger.getLogger(SubjectPropertyMappingsPage.class);
	
	public SubjectPropertyMappingsPage(String title, String description) {
		super(title, description);
	}

	protected void addPropertyFormItem(String uri, String label, String comment) {
		DropdownFieldShower dropdownFieldShower = new DropdownFieldShower(
			formComposite,
			headers,
			label,
			comment
		);
		dropdownFieldShower.setFieldUri(uri);
		dataFieldShowers.add(dropdownFieldShower);
	}
}
