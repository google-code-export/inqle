/**
 * 
 */
package org.inqle.ui.component;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.dao.IOptionsDao;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

import com.antilia.web.button.AbstractLink;
import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.button.MenuItemsFactory;
import com.antilia.web.button.SmallSeparatorButton;
import com.antilia.web.dialog.IVeilScope;
import com.antilia.web.resources.DefaultStyle;
import com.antilia.web.veil.AntiliaVeilResource;
import com.google.inject.Inject;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionsAnswerEditPanel extends Panel implements IVeilScope {

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected IOptionsDao optionsDao;

	private static class EditButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		private Option option;
		
		public EditButton(String id, Option option) {
			super(id);
			this.option = option;
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
			OptionEditPanel optionEditingPanel = new OptionEditPanel(OptionsAnswerEditPanel.CONTENT_ID, option) {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSave(AjaxRequestTarget target, Form<?> form, Option bean) {
					super.onSave(target, form, bean);
				
					OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
					optionsPanel.setContent(optionsPanel.createdListComponent());
					optionsPanel.optionsDao.updateOption(bean);
					if(target != null) {
						target.addComponent(optionsPanel.getContainer());
					}
				}
				
				@Override
				protected void onCancel(AjaxRequestTarget target, Option bean) {
					OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
					optionsPanel.setContent(optionsPanel.createdListComponent());
					if(target != null) {
						target.addComponent(optionsPanel.getContainer());
					}
				}							    
			};			
			OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
			optionsPanel.setContent(optionEditingPanel);
			if(target != null) {
				target.addComponent(optionsPanel.getContainer());
			}
		}
		
	}
	
	private static class CreateButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		private Option option;
		
		private OptionsAnswer optionsAnswer;
		
		public CreateButton(String id, OptionsAnswer optionsAnswer) {
			super(id);
			this.option = new Option();
			this.optionsAnswer = optionsAnswer;
		}
		
		@Override
		protected ResourceReference getImage() {
			return DefaultStyle.IMG_NEW;
		}
		
		@Override
		protected String getLabel() {
			return "Create";
		}
		
		@Override
		protected String getTitleKey() {
			return "createOption";
		}
		
		@Override
		protected void onClick(AjaxRequestTarget target) {
			OptionEditPanel optionEditingPanel = new OptionEditPanel(OptionsAnswerEditPanel.CONTENT_ID, option) {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSave(AjaxRequestTarget target, Form<?> form, Option bean) {
					super.onSave(target, form, bean);
					optionsAnswer.addOption(this.getBean());					
					OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
					optionsPanel.optionsDao.persistOption(bean);
					optionsPanel.setContent(optionsPanel.createdListComponent());
					if(target != null) {
						target.addComponent(optionsPanel.getContainer());
					}
				}
				
				@Override
				protected void onCancel(AjaxRequestTarget target, Option bean) {
					OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
					optionsPanel.setContent(optionsPanel.createdListComponent());
					if(target != null) {
						target.addComponent(optionsPanel.getContainer());
					}
				}
			};			
			OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
			optionsPanel.setContent(optionEditingPanel);
			if(target != null) {
				target.addComponent(optionsPanel.getContainer());
			}
		}
		
	}
	
	private static class DeleteButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		private Option option;
						
		
		public DeleteButton(String id, Option option) {
			super(id);
			this.option = option;
		}
		
		@Override
		protected ResourceReference getImage() {
			return DefaultStyle.IMG_DELETE;
		}
		
		@Override
		protected String getLabel() {
			return null;
		}
		
		@Override
		protected String getTitleKey() {
			return "deleteOption";
		}
		
		@Override
		protected void onClick(AjaxRequestTarget target) {
			OptionsAnswerEditPanel optionsPanel = findParent(OptionsAnswerEditPanel.class);
			optionsPanel.optionsAnswer.removeOption(option);
			optionsPanel.optionsDao.deleteOption(option);
			optionsPanel.setContent(optionsPanel.createdListComponent());
			if(target != null) {
				target.addComponent(optionsPanel.getContainer());
			}					
		}
		
	}

	private OptionsAnswer optionsAnswer;
	
	private WebMarkupContainer container;
	
	public WebMarkupContainer getContainer() {
		return container;
	}

	protected static String CONTENT_ID = "content";
	
	protected Component content;
	
	
	/**
	 * @param id
	 */
	public OptionsAnswerEditPanel(String id, OptionsAnswer optionsAnswer) {
		super(id);
		this.optionsAnswer = optionsAnswer;
		container = new WebMarkupContainer("container") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				this.addOrReplace(content);
				super.onBeforeRender();
			}
		};
		container.setOutputMarkupId(true);	
		add(container);
		content = createdListComponent();
		add( new AntiliaVeilResource());
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
				menu.addMenuItem(new EditButton("edit", bean));
				menu.addMenuItem(new DeleteButton("delete", bean));
			}
			
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				factory.addItem(new SmallSeparatorButton());
				factory.addItem(new CreateButton("create", OptionsAnswerEditPanel.this.getOptionsAnswer()));
			}
			
		};
	}
	
	@Override
	public String getVeilId() {
		return container.getMarkupId();
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
