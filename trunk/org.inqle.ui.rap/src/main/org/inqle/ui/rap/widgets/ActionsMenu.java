package org.inqle.ui.rap.widgets;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ActionsMenu extends Composite implements SelectionListener {

	private List<IAction> actions;

	public ActionsMenu(Composite parent, int style, List<IAction> actions) {
		super(parent, style);
		setActions(actions);
		Composite composite = this;
		composite.setLayout(new GridLayout(2, false));
		for (IAction action: actions) {
			Button button = new Button(composite, SWT.BORDER);
			button.setText(action.getText());
			button.setData(action);
			button.addSelectionListener(this);
			Label label = new Label(composite, SWT.NONE);
			if (action.getDescription() != null && action.getDescription().length() > 0) {
				label.setText(action.getDescription());
			}
		}
	}
	
	private void setActions(List<IAction> actions) {
		this.actions = actions;
	}

	public List<IAction> getActions() {
		return actions;
	}

	public void widgetDefaultSelected(SelectionEvent event) {		
	}

	public void widgetSelected(SelectionEvent event) {
		Object source = event.getSource();
		if (source instanceof Button) {
			Button clickedButton = (Button)source;
			Object data = clickedButton.getData();
			if (data instanceof IAction) {
				IAction clickedAction = (IAction) data;
				clickedAction.run();
			}
		}
	}
	
	
	
}
