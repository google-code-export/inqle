package org.inqle.ui.rap.pages;


/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class TableSubjectPropertyMappingsPage extends SubjectPropertyMappingsPage {

	private static final String DEFAULT_TITLE = "What values for this thing are stored in the table (if any)?";
	
	private static final String DEFAULT_DESCRIPTION = "For each known property of this thing, specify which column contains the " +
			"value.  Example: All rows in the table contain data " +
			"about your flower shop's 'Yodeling Monkey-gram' product, and this caption represents " +
			"the 'Yodeling Monkey-gram' product.  Next to the the property 'price' you would select " +
			"the column 'Monkeygram price'.";

//	private List<TextFieldShower> textMappings;
	
	public TableSubjectPropertyMappingsPage() {
		super(DEFAULT_TITLE, DEFAULT_DESCRIPTION);
	}

//	@Override
//	public IDataFieldShower[] getDataFields() {
//		IDataFieldShower[] dataFieldShowerArray = {};
//		return textMappings.toArray(dataFieldShowerArray);
//	}
}
