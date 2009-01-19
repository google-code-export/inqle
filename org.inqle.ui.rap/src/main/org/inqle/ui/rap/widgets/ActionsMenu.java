package org.inqle.ui.rap.widgets;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

public class ActionsMenu extends Composite implements SelectionListener {

	private List<IAction> actions;

	public ActionsMenu(Composite parent, int style, List<IAction> actions) {
		super(parent, style);
		setActions(actions);
		Composite composite = this;
		composite.setLayout(new RowLayout());
		if (actions==null) return;
		for (IAction action: actions) {
			Link link = new Link(composite, SWT.NONE);
			link.setText("<a>" + action.getText() + "</a>");
			link.setData(action);
			link.addSelectionListener(this);
			if (action.getDescription() != null && action.getDescription().length() > 0) {
				link.setToolTipText(action.getDescription());
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
		if (source instanceof Link) {
			Link clickedLink = (Link)source;
			Object data = clickedLink.getData();
			if (data instanceof IAction) {
				IAction clickedAction = (IAction) data;
				clickedAction.run();
			}
		}
	}
	
	
	
}
