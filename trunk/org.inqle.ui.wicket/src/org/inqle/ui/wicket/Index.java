/**
 * 
 */
package org.inqle.ui.wicket;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.inqle.ui.component.OptionEditPanel;
import org.inqle.ui.component.OptionsAnswerEditPanel;
import org.inqle.ui.component.RangeAnswerEditPanel;
import org.inqle.ui.model.MultipleChoiceAnswer;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;
import org.inqle.ui.model.RangeAnswer;
import org.inqle.ui.resources.Styles;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class Index extends WebPage {

	private RangeAnswer<Float> rangeAnswer;
	
	private Option option;
	
	private OptionsAnswer optionsAnswer;
	
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
		
		optionsAnswer = new MultipleChoiceAnswer();
		optionsAnswer.addOption(new Option("Hi, there!"));
		optionsAnswer.addOption(new Option("Hi, there2!"));
		optionsAnswer.addOption(new Option("Hi, there3!"));
		optionsAnswer.addOption(new Option("Very long answer! Very long answer! " +
				"Very long answer! Very long answer! Very long answer! Very long answer!"));
		
		OptionsAnswerEditPanel options = new OptionsAnswerEditPanel("options", optionsAnswer);
		add(options);
	}
}
