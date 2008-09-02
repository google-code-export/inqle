package org.inqle.ui.rap.pages;


/**
 * A page that captures the literal values for a subject, for each row
 * @author David Donohue
 * Aug 18, 2008
 */
public class TableSubjectPropertyMappingsPage extends SubjectPropertyMappingsPage {

	private static final String DEFAULT_TITLE = "Property Values for Each Row";
	private static final String DEFAULT_DESCRIPTION = "Specify which column in your file should populate applicable properties.";

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
