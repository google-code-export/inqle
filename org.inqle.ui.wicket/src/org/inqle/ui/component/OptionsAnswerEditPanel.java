/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

import com.antilia.web.button.AbstractLink;
import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionsAnswerEditPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static class EditButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		public EditButton(String id) {
			super(id);
		}
		
		@Override
		protected ResourceReference getImage() {
			return DefaultStyle.IMG_EDIT;
		}
		
		@Override
		protected String getLabel() {
			return null;
		}
		
		@Override
		protected String getTitleKey() {
			return "editOption";
		}
		
		@Override
		protected void onClick(AjaxRequestTarget target) {
			
		}
		
	}
	
	private OptionsAnswer optionsAnswer;
	
	private WebMarkupContainer container;
	
	protected static String CONTENT_ID = "content";
	
	protected Component content;
	
	
	/**
	 * @param id
	 */
	public OptionsAnswerEditPanel(String id, OptionsAnswer optionsAnswer) {
		super(id);
		this.optionsAnswer = optionsAnswer;
		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);	
		add(container);
		content = createdListComponent();
	}
	
	@Override
	protected void onBeforeRender() {
		container.addOrReplace(content);
		super.onBeforeRender();
	}
	
	protected Component createdListComponent() {
		return new OptionsAnswerListPanel(CONTENT_ID, optionsAnswer) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateRowMenu(IMenuItemHolder menu, int row, Option bean) {
				menu.addMenuItem(new EditButton("edit"));
			}
		};
	}
	
	public void setContent(Component content) {
		this.content = content;
	}
	
	public String getContentId() {
		return CONTENT_ID;
	}
	
	public OptionsAnswer getOptionsAnswer() {
		return optionsAnswer;
	}
}
