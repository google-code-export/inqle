package org.inqle.ui.rap.pages;


/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class TableSubjectPropertyValuesPage extends SubjectPropertyValuesPage {

	private static final String DEFAULT_TITLE = "Property Values for Entire Table";
	private static final String DEFAULT_DESCRIPTION = "For properties with a constant value for the entire table, specify what that value is.";

	public TableSubjectPropertyValuesPage() {
		super(DEFAULT_TITLE, DEFAULT_DESCRIPTION);
	}
	
	
}
