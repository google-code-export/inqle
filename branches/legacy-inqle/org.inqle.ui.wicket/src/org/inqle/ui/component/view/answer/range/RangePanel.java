/**
 * 
 */
package org.inqle.ui.component.view.answer.range;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.inqle.ui.model.RangeAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class RangePanel<R extends RangeAnswer<T>, T extends Number> extends Panel {

	private static final long serialVersionUID = 1L;

	private Class<T> formatClass;
	
	private R rangeAnswer;
	

	/**
	 * @param id
	 */
	public RangePanel(String id,  R rangeAnswer, Class<T> formatClass) {
		super(id);
		this.formatClass = formatClass;
		this.rangeAnswer = rangeAnswer;
		
		Label min = new Label("min", new AbstractReadOnlyModel<T>(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public T getObject() {
				return RangePanel.this.rangeAnswer.getMinimumResponse();
			}
		});
		
		add(min);
		
		TextField<T> value = new TextField<T>("value", new Model<T>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public T getObject() {
				return RangePanel.this.rangeAnswer.getSelectedVaue();
			}
			
			public void setObject(T object) {
				RangePanel.this.rangeAnswer.setSelectedVaue(object);
			};
			
		}, formatClass);
		add(value);
		
		Label max = new Label("max", new AbstractReadOnlyModel<T>(){
			
			private static final long serialVersionUID = 1L;

			@Override
			public T getObject() {
				return RangePanel.this.rangeAnswer.getMaximumResponse();
			}
		});
		
		add(max);
	}


	public Class<T> getFormatClass() {
		return formatClass;
	}


	public void setFormatClass(Class<T> formatClass) {
		this.formatClass = formatClass;
	}


	public R getRangeAnswer() {
		return rangeAnswer;
	}


	public void setRangeAnswer(R rangeAnswer) {
		this.rangeAnswer = rangeAnswer;
	}

}
