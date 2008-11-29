package org.inqle.ui.rap.pages;


/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class RowSubjectPropertyMappingsPage extends SubjectPropertyMappingsPage {

	private static String DEFAULT_TITLE = "What values for each THINGCLASS type are NOT stored " +
			"in the table, but are common for each row (if any)?";
	private static String DEFAULT_DESCRIPTION = "Specify literal values that apply to each THINGCLASS.  " +
			"Example: All rows in the table contain data about your flower shop's products, " +
			"and this subject represents the 'store product'.  It happens that every product " +
			"represented in this table comes from	the same distributor.  Next to the the property " +
			"'product distributor' you would enter the value 'ABC Distributor Co.''.";
	
	public RowSubjectPropertyMappingsPage() {
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
