package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.inqle.ui.rap.widgets.DropdownFieldShower;

public abstract class SubjectPropertyMappingsPage extends SubjectPropertiesPage {

	private static Logger log = Logger.getLogger(SubjectPropertyMappingsPage.class);
	
	public SubjectPropertyMappingsPage(String title, String description) {
		super(title, description);
	}

	protected void addPropertyFormItem(String uri, String label, String comment, String propertyTypeUri, String header, String value) {
		DropdownFieldShower dropdownFieldShower = new DropdownFieldShower(
			formComposite,
			headers,
			label,
			comment
		);
		dropdownFieldShower.setFieldUri(uri);
		dropdownFieldShower.setFieldPropertyType(propertyTypeUri);
		if (header != null) {
			dropdownFieldShower.select(header);
		}
		dataFieldShowers.add(dropdownFieldShower);
	}
}
