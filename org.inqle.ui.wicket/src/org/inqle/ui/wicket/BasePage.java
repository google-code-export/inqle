/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.inqle.ui.resources.Styles;

import com.antilia.web.resources.DefaultStyle;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class BasePage extends WebPage {

	
	/**
	 * 
	 */
	public BasePage() {
		add(CSSPackageResource.getHeaderContribution(DefaultStyle.CSS_MAIN));
		add(CSSPackageResource.getHeaderContribution(Styles.MAIN_CSS));	
	}
}
