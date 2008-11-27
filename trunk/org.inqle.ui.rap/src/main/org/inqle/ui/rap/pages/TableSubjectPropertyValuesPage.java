package org.inqle.ui.rap.pages;


/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class TableSubjectPropertyValuesPage extends SubjectPropertyValuesPage {

	private static final String DEFAULT_TITLE = "What values/identifiers for this thing are NOT stored in the table (if any)?";
	private static final String DEFAULT_DESCRIPTION = "Enter identifiers for this thing. These are properties with a constant value.";

	public TableSubjectPropertyValuesPage() {
		super(DEFAULT_TITLE, DEFAULT_DESCRIPTION);
	}
	
	
}
