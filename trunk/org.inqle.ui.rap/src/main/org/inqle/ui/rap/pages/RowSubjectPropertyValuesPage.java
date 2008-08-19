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

	private static final String DEFAULT_TITLE = "Property Values for Each Row";
	private static final String DEFAULT_DESCRIPTION = "Specify which column in your file should populate applicable properties.";
	
	public RowSubjectPropertyValuesPage() {
		super(DEFAULT_TITLE, DEFAULT_DESCRIPTION);
	}

}
