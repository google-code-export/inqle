/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.inqle.ui.component.OptionEditPanel;
import org.inqle.ui.component.RangeAnswerEditPanel;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.RangeAnswer;
import org.inqle.ui.resources.Styles;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class Index extends WebPage {

	private RangeAnswer<Float> rangeAnswer;
	
	private Option option;
	/**
	 * 
	 */
	public Index() {
		add(CSSPackageResource.getHeaderContribution(Styles.MAIN_CSS));
		rangeAnswer = new RangeAnswer<Float>();
		RangeAnswerEditPanel<Float> testEdit = new RangeAnswerEditPanel<Float>("testEdit", rangeAnswer, Float.class);
		add(testEdit);
		
		option = new Option();
		OptionEditPanel editOption = new OptionEditPanel("editOption",option);
		add(editOption);
		
	}
}
