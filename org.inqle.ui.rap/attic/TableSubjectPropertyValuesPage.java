package org.inqle.ui.rap.pages;


/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class TableSubjectPropertyValuesPage extends SubjectPropertyValuesPage {

	private static String DEFAULT_TITLE = "What values/identifiers for this THINGCLASS are NOT stored in the table (if any)?";
	private static String DEFAULT_DESCRIPTION = "Enter identifiers for this THINGCLASS. These are properties with a constant value.";

	public TableSubjectPropertyValuesPage() {
		super(DEFAULT_TITLE, DEFAULT_DESCRIPTION);
	}

	@Override
	public String getPageTitle() {
		return DEFAULT_TITLE.replaceAll("THINGCLASS", getThingClass().toUpperCase());
	}

	@Override
	public String getPageDescription() {
		return DEFAULT_DESCRIPTION.replaceAll("THINGCLASS", getThingClass().toUpperCase());
	}
	
	
}
