package org.inqle.ui.rap.pages;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.inqle.ui.rap.widgets.TextFieldShower;

public abstract class SubjectPropertyValuesPage extends SubjectPropertiesPage {

	public SubjectPropertyValuesPage(String title, String description) {
		super(title, description);
	}

	private static Logger log = Logger.getLogger(SubjectPropertyValuesPage.class);

	protected void addPropertyFormItem(String uri, String label, String comment, String propertyTypeUri, String header, String value) {
		TextFieldShower textFieldShower = new TextFieldShower(
			formComposite,
			label,
			comment,
			null,
			SWT.BORDER
		);
		textFieldShower.setFieldUri(uri);
		textFieldShower.setFieldPropertyType(propertyTypeUri);
		if (value != null) {
			textFieldShower.setTextValue(value);
		}
		dataFieldShowers.add(textFieldShower);
	}

}
