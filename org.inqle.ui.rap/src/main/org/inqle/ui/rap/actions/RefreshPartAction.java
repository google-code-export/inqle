package org.inqle.ui.rap.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.inqle.ui.rap.IPart;

public class RefreshPartAction extends Action {

	private IPart partToRefresh;

	public RefreshPartAction(IPart partToRefresh) {
		this.partToRefresh = partToRefresh;
	}
	
	@Override
	public String getText() {
		return "Refresh";
	}

	@Override
	public void run() {
		partToRefresh.fireUpdatePart();
	}
}
