package org.inqle.ui.rap;

import org.eclipse.jface.viewers.IInputSelectionProvider;

public interface IDisposableViewer extends IInputSelectionProvider {

	public void dispose();
	
	public void clearData();
	
	public void setInput(Object input) ;
}
