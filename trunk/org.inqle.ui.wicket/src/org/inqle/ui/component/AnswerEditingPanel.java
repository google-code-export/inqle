/**
 * 
 */
package org.inqle.ui.component;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.antilia.web.button.AbstractButton;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class AnswerEditingPanel<E extends Serializable> extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<E> form;
		
	private E bean;
	
	/**
	 * @param id
	 */
	public AnswerEditingPanel(String id, E bean) {
		super(id);
		this.bean = bean;
		this.form = newFrom("form", bean);		
		add(this.form);
		this.form.add(newSaveButton("save"));
	}
		
	
	protected Component newSaveButton(String id) {
		return new AbstractButton(id, true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ResourceReference getImage() {
				return DefaultStyle.IMG_OK;
			}

			@Override
			protected String getLabel() {
				return "Save";
			}

			@Override
			protected String getLabelKey() {
				return "Save";
			}
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AnswerEditingPanel.this.onSubmit(target, form, bean);
			}
		};
	}
	
	protected void onSubmit(AjaxRequestTarget target, Form<?> form, E bean) {
		System.out.println("Saving" + bean.toString());
	}
	
	protected Form<E> newFrom(String id, E bean) {
		return new Form<E>(id, new CompoundPropertyModel<E>(bean)) {			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver() {
				return true;
			}
		};
	}

	public Form<E> getForm() {
		return form;
	}

}
