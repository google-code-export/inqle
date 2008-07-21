package org.inqle.ui.rap.tree;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.inqle.core.domain.INamedAndDescribed;
import org.inqle.data.rdf.AppInfo;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.actions.AppInfoWizardAction;
import org.inqle.ui.rap.actions.RefreshPartAction;
import org.inqle.ui.rap.tree.parts.AllParts;
import org.inqle.http.lookup.Requestor;

/**
 * View with a tree viewer. This class is contributed through the plugin.xml.
 */
public class PartsView extends ViewPart implements IMenuListener {

	public static final String ID = "org.inqle.ui.rap.partsView";
	private TreeViewer viewer;
	
	private MenuManager contextMenuManager;
	
	private Logger log = Logger.getLogger(PartsView.class);
	private Composite parent;
	private ViewContentProvider viewContentProvider;

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider, IDeltaListener {
        
		private ColumnViewer treeViewer;

		public void dispose() {
		}
        
		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}
        
		public Object getParent(Object child) {
			if (child instanceof IPart) {
				return ((IPart)child).getParent();
			}
			return null;
		}
        
		public Object[] getChildren(Object parent) {
			if (parent instanceof IPartType) {
				return ((IPartType)parent).getChildren();
			}
			return new Object[0];
		}

    public boolean hasChildren(Object parent) {
			if (parent instanceof IPartType)
				return ((IPartType)parent).hasChildren();
			return false;
		}
    
    /**
     * Add listeners to all new IPartTypes & children
     * Remove listeners from all old IPartTypes & children
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    	this.treeViewer = (TreeViewer)viewer;
      if(oldInput != null && oldInput instanceof IPart) {
          removeListenerFrom((IPart)oldInput);
      }
      if(newInput != null && newInput instanceof IPart) {
         addListenerTo((IPart)newInput);
      }
    }
    
    protected void addListenerTo(IPart part) {
    	part.addListener(this);
    	if (part instanceof IPartType) {
    		IPartType partType = (IPartType)part;
    		IPart[] children = partType.getChildren();
	      for (IPart child:children) {
	         addListenerTo(child);
	      }
    	}
    }

    protected void removeListenerFrom(IPart part) {
  		part.removeListener(this);
  		if (part instanceof IPartType) {
    		IPartType partType = (IPartType)part;
    		IPart[] children = partType.getChildren();
	      for (IPart child:children) {
	         removeListenerFrom(child);
	      }
    	}
  	}

    //TODO when this works in RAP, refresh only the changed portion of the tree
		public void updateTree(IPart part) {
			treeViewer.refresh();
			//these do not seem to work in RAP: 
			//treeViewer.refresh(part, true);
			//treeViewer.reveal(part);
		}

		//TODO use update method to update the node.  for now just refresh the shell
		public void updatePart(IPart part) {
			updateTree(part.getParent());
			//fails: treeViewer.reveal(part);
			//fails: StructuredSelection selection = new StructuredSelection(part);
			//fails: treeViewer.setSelection(selection, true);
		}
	}
	
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			if (obj instanceof INamedAndDescribed) {
				return ((INamedAndDescribed)obj).getName();
			}
			return obj.toString();
		}
		public Image getImage(Object obj) {
			if (obj instanceof IPart) {
			   IPart ipartObj = (IPart)obj;
			   return ipartObj.getIcon();
			}
			//should never happen because all tree parts should be instances of IPart
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
	public void createPartControl(Composite parent) {
		//if AppInfo not yet set up, show the setup wizard
		Persister persister = Persister.getInstance();
		while (persister.getAppInfo() == null) {
			try {
				AppInfoWizardAction appInfoWizardAction = new AppInfoWizardAction(getSite().getWorkbenchWindow());
				appInfoWizardAction.run();
				if (persister.getAppInfo() != null) {
					log.info("Registering Site with central INQLE server...");
					log.info("Success? " + Requestor.registerObject(persister.getAppInfo().getSite()));
				}
			} catch (Exception e) {
				log.error("Error running setup wizard", e);
			}
		}
		
		this.parent = parent;
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		
		//register the TreeViewer with the application as a selection provider
		getSite().setSelectionProvider(viewer);
		viewContentProvider = new ViewContentProvider();
		viewer.setContentProvider(viewContentProvider);
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.addSelectionChangedListener(new ViewSelectionChangeListener());
		contextMenuManager = getContextMenuManager();
		viewer.getTree().setMenu(contextMenuManager.createContextMenu(viewer.getTree()));
		
		AllParts allParts = new AllParts();
		allParts.addListener(viewContentProvider);
		viewer.setInput(allParts);
		
		//register the context menu
		getSite().registerContextMenu(contextMenuManager, viewer);
	}

	private MenuManager getContextMenuManager() {
		MenuManager manager = new MenuManager();
		
		//menu is dynamically built each time
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(this);
		
		return manager;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void menuAboutToShow(IMenuManager manager) {
		//get the selected IPart
		TreeItem[] selectedItems = viewer.getTree().getSelection();
		if(selectedItems.length != 1) {
      return;
    }
		TreeItem selectedItem = selectedItems[0];
		Object selectedObj = selectedItem.getData();
		if (! (selectedObj instanceof IPart)) {
			return;
		}
		IPart selectedPart = (IPart)selectedObj;
		
		//add any Actions for this part
		selectedPart.addActions(manager, getSite().getWorkbenchWindow());
		
		//add the Refresh option
		manager.add(new RefreshPartAction(selectedPart));
	}
}