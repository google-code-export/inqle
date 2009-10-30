/**
 * 
 */
package org.inqle.ui.component.edit;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.antilia.web.button.AbstractButton;
import com.antilia.web.button.AbstractLink;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public abstract class BaseEditingPanel<E extends Serializable> extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<E> form;
		
	private E bean;
	
	private FeedbackPanel feedBack;
	
	/**
	 * @param id
	 */
	public BaseEditingPanel(String id, E bean) {
		super(id);
		this.bean = bean;
		this.form = newFrom("form", bean);		
		add(this.form);		
		this.form.add(newSaveButton("save"));
		this.form.add(newCancelLink("cancel"));
		this.feedBack = new FeedbackPanel("feedBack");
		this.feedBack.setOutputMarkupId(true);
		add(this.feedBack);
	}
		
	
	@Override
	protected void onBeforeRender() {
		if(this.form.get("content")== null){
			this.form.add(createdContent("content"));
		}
		super.onBeforeRender();
	}
	
	protected Component createdContent(String contentId) {
		return new EmptyPanel(contentId);
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
				BaseEditingPanel.this.onSave(target, form, bean);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				BaseEditingPanel.this.onError(target, form, bean);
			}
		};
	}
	
	protected Component newCancelLink(String id) {
		return new AbstractLink(id) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ResourceReference getImage() {
				return DefaultStyle.IMG_CANCEL;
			}

			@Override
			protected String getLabel() {
				return "Cancel";
			}

			@Override
			protected String getLabelKey() {
				return "Cancel";
			}

			@Override
			protected void onClick(AjaxRequestTarget target) {
				BaseEditingPanel.this.onCancel(target, bean);
			}
			
			
		};
	}
	
	/**
	 * Form properly was submitted.
	 * 
	 * @param target
	 * @param form
	 * @param bean
	 */
	protected void onSave(AjaxRequestTarget target, Form<?> form, E bean) {	
		if(target != null) {
			target.addComponent(getFeedBack());
		}
	}
	
	protected void onCancel(AjaxRequestTarget target, E bean) {
		
	}
	
	/**
	 * From submit produced errors.
	 * 
	 * @param target
	 * @param form
	 * @param bean
	 */
	protected void onError(AjaxRequestTarget target, Form<?> form, E bean) {
		if(target != null) {
			target.addComponent(getFeedBack());
		}
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


	public E getBean() {
		return bean;
	}


	public void setBean(E bean) {
		this.bean = bean;
	}


	public FeedbackPanel getFeedBack() {
		return feedBack;
	}

}
