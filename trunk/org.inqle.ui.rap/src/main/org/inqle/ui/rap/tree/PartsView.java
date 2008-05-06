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
import org.inqle.ui.rap.IPart;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.AllParts;

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
	}
}


/*


class IPart {
	private String name;
	private IPartType shell;
	
	public IPart(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setParent(IPartType shell) {
		this.parent = shell;
	}
	public IPartType getParent() {
		return shell;
	}
	public String toString() {
		return getName();
	}
}


class IPartType extends IPart {
	private ArrayList children;
	public IPartType(String name) {
		super(name);
		children = new ArrayList();
	}
	public void addChild(IPart child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(IPart child) {
		children.remove(child);
		child.setParent(null);
	}
	public IPart[] getChildren() {
		return (IPart[]) children.toArray(new IPart[children.size()]);
	}
	public boolean hasChildren() {
		return children.size()>0;
	}
}


	/
   * Starting with the provided IPartType, recursively 
   * construct children and their children.
   * @param parentPartType
   * @return
   
	private IPartType getIPartType(IPartType parentPartType) {
		Object[] children = parentPartType.getChildren();
		IPartType shell = new IPartType(parentPartType.getName());
		//add any children
		for (Object child: children) {
			String childName = null;
			if (! (child instanceof IPart)) {
				continue;
			}
			childName = ((IPart)child).getName();
			if (child instanceof IPartType) {
				IPartType childPartType = (IPartType) child;
				IPartType subChildParent = getIPartType(childPartType);
				shell.addChild(subChildParent);
			} else {
				shell.addChild(new IPart(childName));
			}
		}
		return shell;
	}


private Menu getContextMenu() {
		contextMenu = new Menu (shell.getShell(), SWT.POP_UP);
		contextMenu.addListener(SWT.Show, new MenuShowListener());
		return contextMenu;
	}

**
	 * Add context-sensitive options to the right click menu
	 * @author David Donohue
	 * Feb 6, 2008
	 *
	public class MenuShowListener implements Listener {

		//we only are concerned with right-clickings of single IPart objects
		public void handleEvent(Event event) {
			//log.info("Right click received: " + event);
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
			//Menu contextMenu = getContextMenu();
			
			selectedPart.addContextMenuItems(contextMenu);
			contextMenu.setVisible(true);
	    //MessageDialog.openInformation(shell.getShell(), "Right Clicked", selectedPart.getName());
		}

	}
*/