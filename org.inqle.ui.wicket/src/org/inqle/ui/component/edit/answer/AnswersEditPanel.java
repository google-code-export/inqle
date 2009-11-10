/**
 * 
 */
package org.inqle.ui.component.edit.answer;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.dao.IAnswersDao;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.factory.IUIRenderableBuilderService;
import org.inqle.ui.model.IAnswer;

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
public class AnswersEditPanel extends Panel implements IVeilScope {

	private static final long serialVersionUID = 1L;
	
	@Inject
	protected IAnswersDao dao;
	
	@Inject
	private IUIRenderableBuilderService renderService; 
	
	public static abstract class Handler implements IOutcomeHandler<IAnswer> {
		private static final long serialVersionUID = 1L;
		
		private AnswersEditPanel answersEditPanel;
			

		public Handler(AnswersEditPanel answersEditPanel) {
			this.answersEditPanel = answersEditPanel;
		}

		public AnswersEditPanel getAnswersEditPanel() {
			return answersEditPanel;
		}
		
	}

	private static class EditButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		private IAnswer answer;
		
		public EditButton(String id, IAnswer answer) {
			super(id);
			this.answer = answer;
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
			AnswersEditPanel answersEditPanel = findParent(AnswersEditPanel.class);
			Component editPanel =  answersEditPanel.renderService.createAdminEditUI(AnswersEditPanel.CONTENT_ID, answer, 
					new Handler(answersEditPanel) {				
						private static final long serialVersionUID = 1L;

						@Override
						public void onSave(AjaxRequestTarget target, Form<?> form, IAnswer bean) {
							getAnswersEditPanel().dao.update(bean);
							onCancel(target, bean);
						}
						
						@Override
						public void onCancel(AjaxRequestTarget target, IAnswer bean) {
							getAnswersEditPanel().setContent(getAnswersEditPanel().createdListComponent());
							if(target != null) {
								target.addComponent(getAnswersEditPanel().getContainer());
							}
						}
						
					}			
			);						
			answersEditPanel.setContent(editPanel);
			if(target != null) {
				target.addComponent(answersEditPanel.getContainer());
			}
		}
		
	}
	
	
    private static class DeleteButton extends DialogLink {
		
		private static final long serialVersionUID = 1L;

		private IAnswer answer;
						
		
		public DeleteButton(String id, IAnswer answer) {
			super(id);
			this.answer = answer;
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
			return null;
		}
		
		@Override
		public DefaultDialog newDialog(String id) {
			ConfirmationDialog dialog = new ConfirmationDialog(id, this, "Do you want to delete answer?") {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onOk(AjaxRequestTarget target, Form<?> form) {
					AnswersEditPanel answersEditPanel = findParent(AnswersEditPanel.class);
					answersEditPanel.dao.remove(answer);
					answersEditPanel.setContent(answersEditPanel.createdListComponent());
					if(target != null) {
						target.addComponent(answersEditPanel.getContainer());
					}					
				}
			};
			dialog.setCentered(true);
			return dialog;
		}		
	}

	private WebMarkupContainer container;
	
	public WebMarkupContainer getContainer() {
		return container;
	}

	protected static String CONTENT_ID = "content";
	
	protected Component content;
	
	
	/**
	 * @param id
	 */
	public AnswersEditPanel(String id) {
		super(id);
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
		return new AnswersListPanel(CONTENT_ID) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
				AnswersEditPanel.this.populateRowMenu(menu, row, bean);
				menu.addMenuItem(new EditButton("edit", bean));
				menu.addMenuItem(new DeleteButton("delete", bean));
			}
			
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {				
				AnswersEditPanel.this.addMenuItemsBeforeNavigation(factory);
				factory.addItem(new SmallSeparatorButton());
				factory.addItem(new AnswerTypePanel("create"));
			}
			
		};
	}
	
	protected void populateRowMenu(IMenuItemHolder menu, int row, IAnswer bean) {
	
	}
	
	protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
	
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

	public IUIRenderableBuilderService getRenderService() {
		return renderService;
	}

	public void setRenderService(IUIRenderableBuilderService renderService) {
		this.renderService = renderService;
	}
}
