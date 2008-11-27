package org.inqle.ui.rap.table;

import org.inqle.ui.rap.pages.SubjectClassPage;


/**
 * This page permits the user to search for an RDF subject class, and to specify a new RDF subject class.
 * 
 * @author David Donohue
 * Feb 20, 2008
 */
public class RowSubjectClassPage extends SubjectClassPage {

	private static String TITLE = "What type of thing is this subject?";
	private static String DESCRIPTION = "Find and select the type of subject that this data is about." +
			"\nExample: If each ";
//	private Table table;
	
	/**
	 * Create a new page.
	 * @param modelBean the model bean to receive the checkbox selections
	 * @param modelBeanValueId the Javabean-compliant field within the model bean, to contain the checked items
	 * @param title the title of this page
	 * @param titleImage
	 */
	public RowSubjectClassPage() {
		super(TITLE, DESCRIPTION);
	}
}
