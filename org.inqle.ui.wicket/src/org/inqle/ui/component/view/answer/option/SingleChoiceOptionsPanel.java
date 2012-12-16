/**
 * 
 */
package org.inqle.ui.component.view.answer.option;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;
import org.inqle.ui.model.SingleChoiceAnswer;

import com.antilia.web.button.AbstractLink;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class SingleChoiceOptionsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private OptionsAnswer optionsAnswer;
	
	private Option selected;
	
	private List<CheckBoxLink> checkBoxes;
	
	public static class CheckBoxLink extends AbstractLink {
		
		private Option option;
		
		public CheckBoxLink(String id, Option option, SingleChoiceOptionsPanel panel) {			
			super(id);
			this.option = option;
			setOutputMarkupId(true);
			panel.addCheckBox(this);
		}
		
		private static final long serialVersionUID = 1L;

		@Override
		protected ResourceReference getImage() {
			SingleChoiceOptionsPanel choiceOptionsPanel = findParent(SingleChoiceOptionsPanel.class);
			if(option.equals(choiceOptionsPanel.getSelected()))
				return DefaultStyle.IMG_CHECKBOX_CHECKED;
			else 
				return DefaultStyle.IMG_CHECKBOX_UNCHECKED;
		}
		
		@Override
		protected String getLabel() {
			return null;
		}
		
		@Override
		protected void onClick(AjaxRequestTarget target) {
			SingleChoiceOptionsPanel choiceOptionsPanel = findParent(SingleChoiceOptionsPanel.class);
			choiceOptionsPanel.setSelected(this.option);
			choiceOptionsPanel.updateChectBoxes(target);
		}		
	}
	
	/**
	 * @param id
	 */
	public SingleChoiceOptionsPanel(String id, SingleChoiceAnswer optionsAnswer) {
		super(id);
		this.optionsAnswer = optionsAnswer;
		checkBoxes = new ArrayList<CheckBoxLink>();
		setOutputMarkupId(true);
		
		List<ICellPopulator<Option>> columns = new ArrayList<ICellPopulator<Option>>();

		columns.add(new ICellPopulator<Option>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Option>> cellItem, String componentId, IModel<Option> rowModel) {
				cellItem.add(new CheckBoxLink(componentId, rowModel.getObject(), SingleChoiceOptionsPanel.this));
			}
			
			@Override
			public void detach() {
				
			}
		});
		
		columns.add(new ICellPopulator<Option>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Option>> cellItem, String componentId, IModel<Option> rowModel) {
				cellItem.add(new Label(componentId, rowModel.getObject().getTranslationKey()));
			}
			
			@Override
			public void detach() {
				
			}
		});
		ListDataProvider<Option> optionsProvider = new ListDataProvider<Option>(optionsAnswer.getOptions());
		DataGridView<Option> repeatingView = new DataGridView<Option>("rows", columns,optionsProvider);
		for(Option option: optionsAnswer.getOptions()) {
			repeatingView.add(new Label(repeatingView.newChildId(), option.getTranslationKey()));
		}
		add(repeatingView);
	}

	public OptionsAnswer getOptionsAnswer() {
		return optionsAnswer;
	}

	public void setOptionsAnswer(OptionsAnswer optionsAnswer) {
		this.optionsAnswer = optionsAnswer;
	}

	public Option getSelected() {
		return selected;
	}

	public void setSelected(Option selected) {
		this.selected = selected;
	}
	
	public void updateChectBoxes(AjaxRequestTarget target) {
		if(target != null) {
			for(CheckBoxLink boxLink: checkBoxes) {
				target.addComponent(boxLink);
			}			
		}
	}
	
	public void addCheckBox(CheckBoxLink link) {
		checkBoxes.add(link);
	}

}
