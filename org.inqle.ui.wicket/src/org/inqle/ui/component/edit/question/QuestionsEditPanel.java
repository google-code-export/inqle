/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.inqle.ui.dao.IQuestionsDao;
import org.inqle.ui.factory.IOutcomeHandler;
import org.inqle.ui.model.Question;

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
public class QuestionsEditPanel extends Panel implements IVeilScope {

	private static final long serialVersionUID = 1L;
		
	
	@Inject
	protected IQuestionsDao questionsDao;
	
	private static abstract class Handler implements IOutcomeHandler<Question> {
		private static final long serialVersionUID = 1L;
		
		private QuestionsEditPanel questionsPanel;
		
		public Handler(QuestionsEditPanel questionsPanel) {
			this.questionsPanel = questionsPanel;
		}

		public QuestionsEditPanel getQuestionsPanel() {
			return questionsPanel;
		}
		
	}

	private static class EditButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		private Question question;
		
		public EditButton(String id, Question question) {
			super(id);
			this.question = question;
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
			QuestionEditPanel questionEditPanel = new QuestionEditPanel(QuestionsEditPanel.CONTENT_ID, question,
			 new Handler(findParent(QuestionsEditPanel.class)) {
				private static final long serialVersionUID = 1L;

				public void onSave(AjaxRequestTarget target, Form<?> form, Question bean) {
					getQuestionsPanel().setContent(getQuestionsPanel().createdListComponent());
					getQuestionsPanel().questionsDao.update(bean);
					if(target != null) {
						target.addComponent(getQuestionsPanel().getContainer());
					}
				}
				
				public void onCancel(AjaxRequestTarget target, Question bean) {
					QuestionsEditPanel questionsPanel = findParent(QuestionsEditPanel.class);
					questionsPanel.setContent(questionsPanel.createdListComponent());
					if(target != null) {
						target.addComponent(questionsPanel.getContainer());
					}
				}
			}
			);			
			QuestionsEditPanel optionsPanel = findParent(QuestionsEditPanel.class);
			optionsPanel.setContent(questionEditPanel);
			if(target != null) {
				target.addComponent(optionsPanel.getContainer());
			}
		}
		
	}
	
	private static class CreateButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;

		private Question question;
				
		public CreateButton(String id) {
			super(id);
			this.question = new Question();
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
			QuestionEditPanel optionEditingPanel = new QuestionEditPanel(QuestionsEditPanel.CONTENT_ID, question,
			   new Handler(findParent(QuestionsEditPanel.class)) {
				private static final long serialVersionUID = 1L;

				public void onSave(AjaxRequestTarget target, Form<?> form, Question bean) {
					getQuestionsPanel().questionsDao.add(bean);
					onCancel(target, bean);
				}
				
				public void onCancel(AjaxRequestTarget target, Question bean) {
					getQuestionsPanel().setContent(getQuestionsPanel().createdListComponent());
					if(target != null) {
						target.addComponent(getQuestionsPanel().getContainer());
					}
				}
			}
			);			
			QuestionsEditPanel questionsPanel = findParent(QuestionsEditPanel.class);
			questionsPanel.setContent(optionEditingPanel);
			if(target != null) {
				target.addComponent(questionsPanel.getContainer());
			}
		}
		
	}
	
	private static class DeleteButton extends DialogLink {
		
		private static final long serialVersionUID = 1L;

		private Question question;
						
		
		public DeleteButton(String id, Question option) {
			super(id);
			this.question = option;
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
			return new ConfirmationDialog(id, this, "Do you want to delete Question?") {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onOk(AjaxRequestTarget target, Form<?> form) {
					QuestionsEditPanel questionsPanel = findParent(QuestionsEditPanel.class);
					questionsPanel.questionsDao.remove(question);
					questionsPanel.setContent(questionsPanel.createdListComponent());
					if(target != null) {
						target.addComponent(questionsPanel.getContainer());
					}					
				}
			};
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
	public QuestionsEditPanel(String id) {
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
		return new QuestionsListPanel(CONTENT_ID) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateRowMenu(IMenuItemHolder menu, int row, Question bean) {
				menu.addMenuItem(new EditButton("edit", bean));
				menu.addMenuItem(new DeleteButton("delete", bean));
			}
			
			protected void addMenuItemsBeforeNavigation(MenuItemsFactory factory) {
				factory.addItem(new SmallSeparatorButton());
				factory.addItem(new CreateButton("create"));
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
}
