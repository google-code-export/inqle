package org.inqle.ui.rap.pages;

import java.util.List;
import java.util.Map;

import org.inqle.ui.rap.widgets.IDataFieldShower;
import org.inqle.ui.rap.widgets.TextFieldShower;

/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class RowSubjectPropertyValuesPage extends SubjectPropertyValuesPage {

	private static final String DEFAULT_TITLE = "What THINGCLASS values are stored in the table?";
	private static final String DEFAULT_DESCRIPTION = "For each applicable property of THINGCLASS, specify which column contains the corresponding value.  Example: All rows in the table contain data about your flower shop's products, and this subject is 'store product'.  Next to the the property 'price' you would select the column 'product price'.";
	
	public RowSubjectPropertyValuesPage() {
		super(DEFAULT_TITLE, DEFAULT_DESCRIPTION);
	}

	@Override
	public String getPageTitle() {
		return DEFAULT_TITLE.replaceAll("THINGCLASS", getThingClass());
	}

	@Override
	public String getPageDescription() {
		return DEFAULT_DESCRIPTION.replaceAll("THINGCLASS", getThingClass());
	}

}
