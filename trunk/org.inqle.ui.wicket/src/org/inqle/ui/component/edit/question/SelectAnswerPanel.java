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
import org.inqle.ui.model.IAnswer;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public abstract class SelectAnswerPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private Component answerLabel; 
	/**
	 * @param id
	 */
	public SelectAnswerPanel(String id) {
		super(id);
		
		answerLabel = new Label("answer", new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return SelectAnswerPanel.this.getAnswer() != null? SelectAnswerPanel.this.getAnswer().toString(): "";
			}
		}
		);
		answerLabel.setOutputMarkupId(true);
		add(answerLabel);
		
		SelectAnswerDialogLink answerDialogLink = new SelectAnswerDialogLink("select") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onOk(AjaxRequestTarget target, Form<?> form, IAnswer answer) {
				SelectAnswerPanel.this.setAnswer(answer);
				target.addComponent(SelectAnswerPanel.this.getAnswerLabel());
			}
			
		};
		add(answerDialogLink);
	}

	@Override
	protected void onBeforeRender() {
		
		super.onBeforeRender();
	}
	public abstract IAnswer getAnswer();

	public abstract void setAnswer(IAnswer answer);
	
	public Component getAnswerLabel() {
		return answerLabel;
	}
		
}
