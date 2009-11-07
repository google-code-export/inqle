/**
 * 
 */
package org.inqle.ui.component.view.answer.option;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.inqle.ui.model.MultipleChoiceAnswer;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.OptionsAnswer;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MultipleChoiceOptionsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private OptionsAnswer optionsAnswer;
		
	/**
	 * @param id
	 */
	public MultipleChoiceOptionsPanel(String id, MultipleChoiceAnswer optionsAnswer) {
		super(id);
		this.optionsAnswer = optionsAnswer;
		
		List<ICellPopulator<Option>> columns = new ArrayList<ICellPopulator<Option>>();

		columns.add(new ICellPopulator<Option>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Option>> cellItem, String componentId, IModel<Option> rowModel) {
				cellItem.add(new Label(componentId, "Check"));
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

}
