/**
 * 
 */
package org.inqle.ui.component.edit.question;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.inqle.ui.factory.IUIRenderableBuilderService;
import org.inqle.ui.model.IAnswer;

import com.google.inject.Inject;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public abstract class SelectAnswerPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private Component answerLabel; 
	
	@Inject
	private IUIRenderableBuilderService builderService;
	
	/**
	 * @param id
	 */
	public SelectAnswerPanel(String id) {
		super(id);				
		setOutputMarkupId(true);		
		SelectAnswerDialogLink answerDialogLink = new SelectAnswerDialogLink("select") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onOk(AjaxRequestTarget target, Form<?> form, IAnswer answer) {
				SelectAnswerPanel.this.setAnswer(answer);
				target.addComponent(SelectAnswerPanel.this);
			}
			
		};
		add(answerDialogLink);
	}

	@Override
	protected void onBeforeRender() {
		if(SelectAnswerPanel.this.getAnswer() == null) {
			answerLabel = new Label("answer", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return SelectAnswerPanel.this.getAnswer() != null? SelectAnswerPanel.this.getAnswer().toString(): "";
				}
			});
		} else {
			answerLabel = builderService.createFinalUserUI("answer", SelectAnswerPanel.this.getAnswer());
			if(answerLabel == null) {
				answerLabel = new Label("answer", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return SelectAnswerPanel.this.getAnswer().toString();
					}
				});
			}
		}
		answerLabel.setOutputMarkupId(true);
		addOrReplace(answerLabel);
		super.onBeforeRender();
	}
	public abstract IAnswer getAnswer();

	public abstract void setAnswer(IAnswer answer);
		
}
