package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.inqle.ui.model.IUIRenderable;

/**
 * Defines a factory for UI parta of answers.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public interface IUIRenderableBuilder {
	
	/**
	 * @param answer The answer to test.
	 * @return Returns true if this Builder can handle this kind of answer. Otherwise return false.
	 */
	public boolean canHandleAnswer(IUIRenderable renderable);
	
	/**
	 *  Produces the presentation for the final user. e.g. in case of range answer 
	 *  this is just: Give me your answer x between a and b (a <= x <= b).
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createFinalUserUI(String id,IUIRenderable renderable);

	/**
	 *  Produces the presentation for the admin user. e.g. in case of range answer 
	 *  this is just: Give me your a and b (with b > a). The x part will be left 
	 *  to the user. 
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createAdminUserUI(String id, IUIRenderable renderable);
}
