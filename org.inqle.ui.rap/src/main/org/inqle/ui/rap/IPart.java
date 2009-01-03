package org.inqle.ui.rap;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.ui.rap.tree.IDeltaListener;
/**
 * Implementations of IPart must implement a set of methods,
 * which facilitate the object to appear in the tree menu.  
 * Also, implementations add any context-specific menu items and
 * associated listeners.
 * @author David Donohue
 * Feb 6, 2008
 * 
 * removed: extends Listener
 */
public interface IPart extends INamedAndDescribed {	
	
	public Image getIcon();
	
	public void setParent(IPartType parent);
	
	public IPartType getParent();
	
	public String toString();

	public String getIconPath();

	/**
	 * Add any context menu items for this object
	 * @param workbenchWindow 
	 * @param contextMenu
	 */
	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow);

	public void addListener(IDeltaListener listener);

	public void removeListener(IDeltaListener listener);
	
	public void fireUpdate(IPart changedPart);

	public void fireUpdatePart();

	public Viewer getViewer(Composite composite);
	
	//public void setPersister(Persister persister);
	
	//public Persister getPersister();
	
	/**
	 * Get the object which this part mediates.  This could be 
	 * @return
	 */
	public Object getObject();
}
