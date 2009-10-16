package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.inqle.ui.model.IAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IUIRenderableBuilderService {

	/**
	 * Allows to remove a builder.
	 * 
	 * @param builder
	 * @return
	 */	
	public IUIRenderableBuilderService removeBuilder(IUIRenderableBuilder builder);
	
	/**
	 * Allows to add a builder.
	 * 
	 * @param builder
	 * @return
	 */
	public IUIRenderableBuilderService addBuilder(IUIRenderableBuilder builder);
	
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
	public Component createAdminUserUI(String id,IAnswer answer);
}
