package org.inqle.ui.component.edit.answer;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.inqle.ui.component.edit.answer.AnswersEditPanel.Handler;
import org.inqle.ui.factory.IAnswerTypesService;
import org.inqle.ui.model.DoubleRangeAnswer;
import org.inqle.ui.model.IAnswer;
import org.inqle.ui.model.IAnswerType;

import com.antilia.web.button.AbstractLink;
import com.antilia.web.button.IMenuItem;
import com.antilia.web.resources.DefaultStyle;
import com.google.inject.Inject;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class AnswerTypePanel extends Panel implements IMenuItem {

	private static final long serialVersionUID = 1L;

	@Inject
	private IAnswerTypesService answerTypesService;
	
	private IAnswerType<?> selected;
	
	public static class CreateButton extends AbstractLink {
		
		private static final long serialVersionUID = 1L;
				
		public CreateButton(String id) {
			super(id);
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
			AnswersEditPanel answersEditPanel = findParent(AnswersEditPanel.class);
			//TODO: how to see which kind of answer we create.
			IAnswer answer = new DoubleRangeAnswer();
			Component editPanel =  answersEditPanel.getRenderService().createAdminCreateUI(AnswersEditPanel.CONTENT_ID, answer, 
					new Handler(answersEditPanel)  {										
						private static final long serialVersionUID = 1L;

						@Override
						public void onSave(AjaxRequestTarget target, Form<?> form, IAnswer bean) {
							getAnswersEditPanel().dao.add(bean);
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
			
			answersEditPanel.setContent(editPanel);
			if(target != null) {
				target.addComponent(answersEditPanel.getContainer());
			}
		}
		
	}

	/**
	 * 
	 */
	public AnswerTypePanel(String id) {
		super(id);
		
		DropDownChoice<IAnswerType<?>> answerType  = new DropDownChoice<IAnswerType<?>>("answerType");		
		List<IAnswerType<?>> choices = new ArrayList<IAnswerType<?>>();
		for(IAnswerType<?> type : answerTypesService.getAnswerTypes()){
			choices.add(type);
		}
		answerType.setChoices(choices);
		answerType.setModel(new Model<IAnswerType<?>>() {			
			private static final long serialVersionUID = 1L;

			@Override
			public IAnswerType<?> getObject() {
				return AnswerTypePanel.this.selected;
			}
			
			@Override
			public void setObject(IAnswerType<?> object) {
				AnswerTypePanel.this.selected = selected;
			}
			
		});
		
		add(answerType);
		
		CreateButton createLink = new CreateButton("createLink");
		add(createLink);
	}


	public IAnswerType<?> getSelected() {
		return selected;
	}

	public void setSelected(IAnswerType<?> selected) {
		this.selected = selected;
	}
	
	@Override
	public int getOrder() {
		return 0;
	}
}
