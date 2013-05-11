package org.inqle.ui.rap;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.inqle.ui.rap.tree.IDeltaListener;
import org.inqle.ui.rap.tree.NullDeltaListener;
import org.inqle.ui.rap.tree.parts.DatabasePart;
import org.inqle.ui.rap.views.ObjectViewer;

public abstract class Part implements IPart {

	protected String name = null;
	protected IPartType parent;
	protected IDeltaListener listener = null;
	protected String description;
	
	private static Logger log = Logger.getLogger(Part.class);
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setParent(IPartType parent) {
		this.parent = parent;
	}
	public IPartType getParent() {
		return parent;
	}

	//protected Persister persister;
	
	/**
	 * Override to point to custom icon in the classpath, as in
	 * "org/inqle/ui/rap/images/db.gif"
	 */
	public String getIconPath() {
		return null;
	}

	public Image getIcon() {
		if (getIconPath() == null) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		Image image = Graphics.getImage(getIconPath(), this.getClass().getClassLoader());
		return image;
	}
	
	/**
	 * Subclasses should override this method if they add any context menu info
	 * (and handle consequent event, by overriding handleEvent method).
	 */
//	public void addActions(IMenuManager manager, IWorkbenchWindow workbenchWindow) {
//		
//	}

	public List<IAction> getActions(IWorkbenchWindow workbenchWindow) {
		return null;
	}

	public void handleEvent(Event event) {
		
	}

	public void addListener(IDeltaListener listener) {
		if (listener==null) {
			log.error("Listener is null");
			throw new RuntimeException("Listener is null");
		}
		this.listener = listener;
	}
	
	public void removeListener(IDeltaListener listener) {
		if(this.listener.equals(listener)) {
			this.listener = NullDeltaListener.getSoleInstance();
		}
	}
	
	public void fireUpdate(IPart changedPart) {
		listener.updateTree(changedPart);
	}
	
	public void fireUpdatePart() {
		listener.updatePart(this);
	}
//	public void setPersister(Persister persister) {
//		this.persister = persister;
//	}
//	public Persister getPersister() {
//		return persister;
//	}
	
	public String toString() {
		return getClass() + "(" + getName() + ")";
	}
	
	/**
	 * Parts which actually represent a data object should return that object.
	 */
	public Object getObject() {
		return null;
	}
	
	/**
	 * Default: return a BeanViewer, which shows some fields and a RDF representation
	 * of the object
	 */
	public IDisposableViewer getViewer(Composite composite) {
		return new ObjectViewer(composite, getObject());
	}
}