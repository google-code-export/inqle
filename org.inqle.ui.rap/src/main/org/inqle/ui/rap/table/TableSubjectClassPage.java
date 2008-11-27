package org.inqle.ui.rap.table;

import org.inqle.ui.rap.pages.SubjectClassPage;


/**
 * This page permits the user to search for an RDF subject class, and to specify a new RDF subject class.
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public class TableSubjectClassPage extends SubjectClassPage {

	private static String TITLE = "Type of Caption";
	private static String DESCRIPTION = "What type of thing is this caption?  Find and select the type of thing, or enter a new one.  Example: if your data is about the population of the United States, you could add a caption with type: country.";
//	private Table table;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param title the title of this page
	 * @param titleImage
	 */
	public TableSubjectClassPage() {
		super(TITLE, DESCRIPTION);
	}
}
