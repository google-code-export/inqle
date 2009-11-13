/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;


/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class IndexPage extends BasePage {

	
	public IndexPage() {
		super();
		BookmarkablePageLink<Void> edit = new BookmarkablePageLink<Void>("edit", EditPage.class);
		add(edit);
		
		BookmarkablePageLink<Void> view = new BookmarkablePageLink<Void>("view", ViewPage.class);
		add(view);
	}
}
