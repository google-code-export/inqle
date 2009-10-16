package org.inqle.ui;

import org.apache.wicket.Component;
import org.inqle.ui.model.IAnswer;

/**
 * Defines a factory for UI parta of answers.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public interface IAnswerUIBuilder {
	
	/**
	 * @param answer The answer to test.
	 * @return Returns true if this Builder can handle this kind of answer. Otherwise return false.
	 */
	public boolean canHandleAnswer(IAnswer answer);
	
	/**
	 *  Produces the presentation for the final user. e.g. in case of range answer 
	 *  this is just: Give me your answer x between a and b (a <= x <= b).
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createFinalUserUI(String id,IAnswer answer);

	/**
	 *  Produces the presentation for the admin user. e.g. in case of range answer 
	 *  this is just: Give me your a and b (with b > a). The x part will be left 
	 *  to the user. 
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createAdminUserUI(String id, IAnswer answer);
}
