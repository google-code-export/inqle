package org.inqle.ui.factory;

import org.apache.wicket.Component;
import org.inqle.ui.model.IUIRenderable;

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
	public IUIRenderableBuilderService removeBuilder(IRenderableUIBuilder builder);
	
	/**
	 * Allows to add a builder.
	 * 
	 * @param builder
	 * @return
	 */
	public IUIRenderableBuilderService addBuilder(IRenderableUIBuilder builder);
	
	/**
	 *  Produces the presentation for the final user. e.g. in case of range answer 
	 *  this is just: Give me your answer x between a and b (a <= x <= b).
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createFinalUserUI(String id, IUIRenderable renderable);

	/**
	 *  Produces the presentation for the admin user. e.g. in case of range answer 
	 *  this is just: Give me your a and b (with b > a). The x part will be left 
	 *  to the user. 
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createAdminEditUI(String id,IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler);
	
	/**
	 *  Produces the presentation for the create user UI. e.g. in case of range answer 
	 *  this is just: Give me your a and b (with b > a). The x part will be left 
	 *  to the user. 
	 *   
	 * @param answer
	 * @return The corresponding Wicket component.
	 */
	public Component createAdminCreateUI(String id, IUIRenderable renderable, IOutcomeHandler<? extends IUIRenderable> handler);
}
