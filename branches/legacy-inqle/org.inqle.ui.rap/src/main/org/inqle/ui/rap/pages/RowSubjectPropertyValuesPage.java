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

	private static String DEFAULT_TITLE = "What values for each THINGCLASS are NOT stored " +
	"in the table, but are common for each row (if any)?";
private static String DEFAULT_DESCRIPTION = "Specify literal values that apply to each THINGCLASS.  " +
	"Example: All rows in the table contain data about your flower shop's products, " +
	"and this subject represents the 'store product'.  It happens that every product " +
	"represented in this table comes from	the same distributor.  Next to the the property " +
	"'product distributor' you would enter the value 'ABC Distributor Co.'.";

	public RowSubjectPropertyValuesPage() {
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
