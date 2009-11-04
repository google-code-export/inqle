/**
 * 
 */
package org.inqle.ui.component.edit.answer.option;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.dao.IOptionsDao;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

import com.antilia.web.button.AbstractLink;
import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.button.MenuItemsFactory;
import com.antilia.web.button.SmallSeparatorButton;
import com.antilia.web.dialog.DefaultDialog;
import com.antilia.web.dialog.DialogLink;
import com.antilia.web.dialog.IVeilScope;
import com.antilia.web.dialog.util.ConfirmationDialog;
import com.antilia.web.resources.DefaultStyle;
import com.antilia.web.veil.AntiliaVeilResource;
import com.google.inject.Inject;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class OptionsAnswerEditDetailPanel extends Panel implements IVeilScope {

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected IOptionsDao optionsDao;

	private static abstract class Handler implements IOutcomeHandler<Option> {
		private static final long serialVersionUID = 1L;
		
		private OptionsAnswerEditDetailPanel optionsAnswerEditPanel;
		
		public Handler(OptionsAnswerEditDetailPanel optionsAnswerEditPanel) {
			this.optionsAnswerEditPanel = optionsAnswerEditPanel;
		}

		public OptionsAnswerEditDetailPanel getOptionsAnswerEditPanel() {
			return optionsAnswerEditPanel;
		}
	
	}
	
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
			OptionEditPanel optionEditingPanel = new OptionEditPanel(OptionsAnswerEditDetailPanel.CONTENT_ID, option,
				new Handler(findParent(OptionsAnswerEditDetailPanel.class)) {
											
					private static final long serialVersionUID = 1L;

					
					@Override
					public void onCancel(AjaxRequestTarget target, Option bean) {
						getOptionsAnswerEditPanel().setContent(getOptionsAnswerEditPanel().createdListComponent());
						if(target != null) {
							target.addComponent(getOptionsAnswerEditPanel().getContainer());
						}
					}
					
					@Override
					public void onSave(AjaxRequestTarget target, Form<?> form, Option bean) {
						getOptionsAnswerEditPanel().optionsDao.update(bean);
						onCancel(target, bean);
					}
				}
			);			
			OptionsAnswerEditDetailPanel optionsPanel = findParent(OptionsAnswerEditDetailPanel.class);
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
			OptionEditPanel optionEditingPanel = new OptionEditPanel(OptionsAnswerEditDetailPanel.CONTENT_ID, option,
				new Handler(findParent(OptionsAnswerEditDetailPanel.class)) {
						private static final long serialVersionUID = 1L;

				public void onSave(AjaxRequestTarget target, Form<?> form, Option bean) {
					optionsAnswer.addOption(bean);					
					getOptionsAnswerEditPanel().optionsDao.add(bean);
					onCancel(target, bean);
				}
				
				public void onCancel(AjaxRequestTarget target, Option bean) {
					getOptionsAnswerEditPanel().setContent(getOptionsAnswerEditPanel().createdListComponent());
					if(target != null) {
						target.addComponent(getOptionsAnswerEditPanel().getContainer());
					}
				}
			}
			);			
			OptionsAnswerEditDetailPanel optionsPanel = findParent(OptionsAnswerEditDetailPanel.class);
			optionsPanel.setContent(optionEditingPanel);
			if(target != null) {
				target.addComponent(optionsPanel.getContainer());
			}
		}
		
	}
	
    private static class DeleteButton extends DialogLink {
		
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
		protected String getLabelKey() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public DefaultDialog newDialog(String id) {
			
			return new ConfirmationDialog(id, this, "Do you want to delete option?") {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onOk(AjaxRequestTarget target, Form<?> form) {
					OptionsAnswerEditDetailPanel optionsPanel = findParent(OptionsAnswerEditDetailPanel.class);
					optionsPanel.optionsAnswer.removeOption(option);
					optionsPanel.optionsDao.remove(option);
					optionsPanel.setContent(optionsPanel.createdListComponent());
					if(target != null) {
						target.addComponent(optionsPanel.getContainer());
					}					
				}
			};
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
	public OptionsAnswerEditDetailPanel(String id, OptionsAnswer optionsAnswer) {
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
				factory.addItem(new CreateButton("create", OptionsAnswerEditDetailPanel.this.getOptionsAnswer()));
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
